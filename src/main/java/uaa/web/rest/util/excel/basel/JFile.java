package uaa.web.rest.util.excel.basel;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public class JFile implements Serializable {
    private String fileNameNoExtension;
    private String fileExtension;
    private String expectedFullFileName;
    private byte[] fileContent;
    private String relativePath;
    private String ftpPath;

    public JFile() {
    }

    public String getFileNameNoExtension() {
        return this.fileNameNoExtension;
    }

    public String getFileExtension() {
        return this.fileExtension;
    }

    public String getExpectedFullFileName() {
        return this.expectedFullFileName;
    }

    public byte[] getFileContent() {
        return this.fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public long contentLength() throws IOException {
        return (long)this.fileContent.length;
    }

    @JsonIgnore
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(this.fileContent);
    }

    public String getRelativePath() {
        return this.relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public void setFileNameNoExtension(String fileNameNoExtension) {
        this.fileNameNoExtension = fileNameNoExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public void setExpectedFullFileName(String expectedFullFileName) {
        this.expectedFullFileName = expectedFullFileName;
    }

    public String getFtpPath() {
        return this.ftpPath;
    }

    public void setFtpPath(String ftpPath) {
        this.ftpPath = ftpPath;
    }
}
