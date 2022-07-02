package vn.sapo.banner.common.media;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface MediaProvider {

    /**
     * Upload file to S3
     *
     * @param tenantId
     * @param type     (example: "products", "themes",...)
     * @param model
     * @return S3 key of file.
     * @throws IOException
     */
    String upload(int tenantId, String type, FileUploadModel model)
            throws IOException;

    /**
     * Upload files and create thumbs if image
     *
     * @param tenantId
     * @param type       (example: "products", "themes",...)
     * @param listModels
     * @throws IOException
     */
    HashMap<String, String> multiUpload(int tenantId, String type,
                                        List<FileUploadModel> listModels) throws IOException;

    /**
     * Download file from S3.
     *
     * @param tenantId
     * @param type     (example: "products", "themes",...)
     * @param model
     * @return binary data of file.
     * @throws IOException
     */
    byte[] download(int tenantId, String type, FileUploadModel model)
            throws IOException;

    /**
     * Copy file from S3
     *
     * @param tenantId
     * @param type     (example: "products", "themes",...)
     * @param source
     * @param dest
     */
    String copy(int tenantId, String type, FileUploadModel source,
                FileUploadModel dest);

    /**
     * Copy file between stores.
     */
    String copy(int srcTenantId, int destTenantId, String type,
                FileUploadModel source, FileUploadModel dest);

    /**
     * remove file in S3, remove thumbs if image.
     *
     * @param tenantId
     * @param type     (example: "products", "themes",...)
     * @param model
     */
    void remove(int tenantId, String type, FileUploadModel model);

    /**
     * remove files in S3
     *
     * @param tenantId
     * @param type       (example: "products", "themes",...)
     * @param listModels
     */
    void remove(int tenantId, String type,
                List<FileUploadModel> listModels);

    String getDownloadLink(String key, long expiredSeconds);
}
