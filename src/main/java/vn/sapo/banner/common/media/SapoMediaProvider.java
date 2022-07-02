package vn.sapo.banner.common.media;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.transfer.Transfer;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.util.IOUtils;
import lombok.extern.apachecommons.CommonsLog;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import vn.sapo.banner.common.exception.FormValidateException;
import vn.sapo.banner.common.util.DateUtils;
import vn.sapo.banner.common.util.ImageUtils;
import vn.sapo.banner.common.util.NetUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@CommonsLog
public class SapoMediaProvider implements MediaProvider {
    private final String BUCKET_NAME;
    private final AmazonS3 _s3Client;
    private final TransferManager _transferManager;
    private final String profile;
    private final int uploadLimitMb;

    public SapoMediaProvider(MediaProperties mediaConfig, AwsProperties awsConfig, String profile) {
        int TIME_OUT = mediaConfig.getTimeout();
        this.BUCKET_NAME = awsConfig.getBucket_name();
        this.uploadLimitMb = mediaConfig.getUploadLimitMb();
        this.profile = profile;
        AWSCredentials credentials = new BasicAWSCredentials(awsConfig.getAccess_key(), awsConfig.getSecret_key());
        ClientConfiguration config = new ClientConfiguration();
        config.setConnectionTimeout(TIME_OUT);
        config.setSocketTimeout(TIME_OUT);
        config.setRequestTimeout(TIME_OUT);
        config.setClientExecutionTimeout(TIME_OUT);
        config.setUseGzip(true);

        val clientBuilder = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withClientConfiguration(config)
                .withRegion(Regions.AP_SOUTHEAST_1);
        _s3Client = clientBuilder.build();

        _transferManager = TransferManagerBuilder.standard().withS3Client(_s3Client).build();
    }

    @Override
    public String upload(int tenantId, String type, FileUploadModel model) {
        try {
            return _uploadToS3(tenantId, type, model);
        } catch (Exception e) {
            log.error("exception", e);
        }

        return null;
    }

    private String _uploadToS3(int tenantId, String type, FileUploadModel model) throws IOException {
        String key = "";
        String contentType = validateAndGetContentType(model);
        if (tenantId <= 0 || model.getBytes() == null)
            return "";

        key = _buildS3Key(tenantId, type, model);

        ObjectMetadata metaData = new ObjectMetadata();
        metaData.setContentLength(model.getBytes().length);
        metaData.setContentType(contentType);

        val inputStream = new ByteArrayInputStream(model.getBytes());
        try {
            _s3Client.putObject(BUCKET_NAME, key, inputStream, metaData);
        } catch (Exception ex) {
            log.error("fail upload to s3", ex);
        } finally {
            inputStream.close();
        }

        if (model.isPublicRead()) {
            _s3Client.setObjectAcl(BUCKET_NAME, key, CannedAccessControlList.PublicRead);
        }

        return key;
    }

    private String validateAndGetContentType(FileUploadModel model) {
        String contentType = "";
        if (!StringUtils.isEmpty(model.getContentType()))
            contentType = (model.getContentType());
        else
            contentType = (ImageUtils.getContentType(model.getFileName()));

        if (contentType.equalsIgnoreCase("application/zip")) {
            if (model.getBytes().length > 1048576 * uploadLimitMb) {
                Map<String, Object> errors = new HashMap<String, Object>();
                errors.put("upload file", "Kích thước file zip tối đa được upload là " + uploadLimitMb + "MB");
                throw new FormValidateException(errors);
            }
        } else {
            if (model.getBytes().length > 1048576 * uploadLimitMb) {
                Map<String, Object> errors = new HashMap<String, Object>();
                errors.put("upload file", "Kích thước file tối đa được upload là " + uploadLimitMb + "MB");
                throw new FormValidateException(errors);
            }
        }
        return contentType;
    }

    private String _buildS3Key(int tenantId, String type, FileUploadModel model) {
        String key = "";
        if (!StringUtils.isEmpty(model.getFolderName())) {
            key = NetUtils.buildUri(new String[]{MediaHelper.generateFolderLinkByTenantId(tenantId, profile), type,
                    model.getFolderName(), model.getFileName()});
        } else {
            key = NetUtils.buildUri(
                    new String[]{MediaHelper.generateFolderLinkByTenantId(tenantId, profile), type, model.getFileName()});
        }

        return key.toLowerCase();
    }

    @Override
    public HashMap<String, String> multiUpload(int tenantId, String type, List<FileUploadModel> listModels) {
        try {
            HashMap<String, String> keys = new HashMap<String, String>();
            List<Upload> uploads = new ArrayList<Upload>();
            if (listModels != null && !listModels.isEmpty()) {
                for (FileUploadModel model : listModels) {
                    String contentType = validateAndGetContentType(model);
                    String key = _buildS3Key(tenantId, type, model);
                    keys.put(model.getFileName(), key);
                    val inputStream = new ByteArrayInputStream(model.getBytes());
                    ObjectMetadata metaData = new ObjectMetadata();
                    metaData.setContentLength(model.getBytes().length);
                    metaData.setContentType(contentType);

                    PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, key, inputStream, metaData);
                    if (ImageUtils.isPublicRead(model.getFileName())) {
                        putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
                    }
                    uploads.add(_transferManager.upload(putObjectRequest));
                }
            }
            while (uploads.size() > uploads.stream().filter(Transfer::isDone).count()) {
                Thread.sleep(200);
            }
            return keys;
        } catch (Exception e) {
            log.error("exception", e);
        }
        return null;
    }

    @Override
    public byte[] download(int tenantId, String type, FileUploadModel model) throws IOException {
        byte[] data = null;

        String key = _buildS3Key(tenantId, type, model);
        S3Object s3object = _s3Client.getObject(BUCKET_NAME, key);
        S3ObjectInputStream stream = s3object.getObjectContent();
        InputStream reader = new BufferedInputStream(stream);
        data = IOUtils.toByteArray(reader);
        reader.close();

        return data;
    }

    @Override
    public String copy(int tenantId, String type, FileUploadModel source, FileUploadModel dest) {
        try {
            String key = _copyInS3(tenantId, type, source, dest);
            return key;
        } catch (Exception e) {
            log.error("exception", e);
        }

        return null;
    }

    private String _copyInS3(int tenantId, String type, FileUploadModel source, FileUploadModel dest) {

        String sourceKey = _buildS3Key(tenantId, type, source);
        String destKey = _buildS3Key(tenantId, type, dest);

        _s3Client.copyObject(new CopyObjectRequest(BUCKET_NAME, sourceKey, BUCKET_NAME, destKey));

        if (source.isPublicRead()) {
            _s3Client.setObjectAcl(BUCKET_NAME, destKey, CannedAccessControlList.PublicRead);
        }

        return destKey;
    }

    @Override
    public String copy(int srcTenantId, int destTenantId, String type, FileUploadModel source, FileUploadModel dest) {
        try {
            return _copyInS3(srcTenantId, destTenantId, type, source, dest);
        } catch (Exception e) {
            log.error("exception", e);
        }

        return null;
    }

    private String _copyInS3(int srcTenantId, int destTenantId, String type, FileUploadModel source,
                             FileUploadModel dest) {

        String sourceKey = _buildS3Key(srcTenantId, type, source);
        String destKey = _buildS3Key(destTenantId, type, dest);

        _s3Client.copyObject(new CopyObjectRequest(BUCKET_NAME, sourceKey, BUCKET_NAME, destKey));

        if (source.isPublicRead()) {
            _s3Client.setObjectAcl(BUCKET_NAME, destKey, CannedAccessControlList.PublicRead);
        }

        return destKey;
    }

    @Override
    public void remove(int tenantId, String type, FileUploadModel model) {
        try {
            _removeInS3(tenantId, type, model);
        } catch (Exception e) {
            log.error("exception", e);
        }
    }

    private void _removeInS3(int tenantId, String type, FileUploadModel model) {
        String key = _buildS3Key(tenantId, type, model);
        _s3Client.deleteObject(BUCKET_NAME, key);
    }

    @Override
    public void remove(int tenantId, String type, List<FileUploadModel> listModels) {
        List<String> keys = new ArrayList<String>();
        if (listModels != null && !listModels.isEmpty()) {
            for (FileUploadModel model : listModels) {
                String key = _buildS3Key(tenantId, type, model);
                keys.add(key);
            }
        }
        if (!keys.isEmpty()) {
            List<KeyVersion> keysVerions = keys.stream().map(KeyVersion::new).collect(Collectors.toList());
            val deleteObjectsRequest = new DeleteObjectsRequest(BUCKET_NAME);
            deleteObjectsRequest.setQuiet(true);

            deleteObjectsRequest.setKeys(keysVerions);
            try {
                _s3Client.deleteObjects(deleteObjectsRequest);
            } catch (Exception e) {
                log.error("exception", e);
            }
        }
    }

    @Override
    public String getDownloadLink(String key, long expiredSeconds) {
        try {
            // System.out.println("Generating pre-signed URL.");
            java.util.Date expiration = DateUtils.getUTC();
            // System.out.print(expiredSeconds);
            long milliSeconds = expiration.getTime();
            if (expiredSeconds == 0) {
                milliSeconds += 10006015; // add 15 minutes.
            } else {
                milliSeconds += expiredSeconds * 1000;
            }
            expiration.setTime(milliSeconds);
            // System.out.print(expiration);
            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(BUCKET_NAME, key);
            generatePresignedUrlRequest.setMethod(HttpMethod.GET);
            generatePresignedUrlRequest.setExpiration(expiration);

            URL url = _s3Client.generatePresignedUrl(generatePresignedUrlRequest);

            String result = url.toString();

            return result.replace(BUCKET_NAME + ".s3-ap-southeast-1.amazonaws.com",
                    "s3-ap-southeast-1.amazonaws.com/" + BUCKET_NAME);
        } catch (AmazonClientException exception) {
            log.error("exception", exception);
        }

        return "";
    }
}