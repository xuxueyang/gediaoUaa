package uaa.service.dto.upload;


public class UploadResultDTO {
    private String id;
    private String uploadFileName;
    private String name;
    private String path;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
