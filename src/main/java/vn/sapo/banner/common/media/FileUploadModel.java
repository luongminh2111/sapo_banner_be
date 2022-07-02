package vn.sapo.banner.common.media;

import lombok.Data;

@Data
public class FileUploadModel {
    private byte[] bytes;
    private String folderName;
    private String fileName;
    private String sourceFileName;
    private String src;
    private boolean publicRead;
    private String contentType;
    
    private int objectId;

    public FileUploadModel() {
    }

    public FileUploadModel(FileUploadModel source) {
        this.bytes = source.bytes;
        this.folderName = source.folderName;
        this.sourceFileName = source.fileName;
        this.fileName = source.fileName;
        this.src = source.src;
        this.publicRead = source.publicRead;
        this.contentType = source.contentType;
    }
}
