package uaa.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Uaa.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    private Config config = new Config();
    private String version;
    private String client;
    private String projectType;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }


    public static class Config{
        private Email email;
        private FileService fileService;

        public FileService getFileService() {
            return fileService;
        }

        public void setFileService(FileService fileService) {
            this.fileService = fileService;
        }

        public Email getEmail() {
            return email;
        }

        public void setEmail(Email email) {
            this.email = email;
        }
    }

    public static class Email {
        private  Subject subject = new Subject();

        public Subject getSubject() {
            return subject;
        }

        public void setSubject(Subject subject) {
            this.subject = subject;
        }
    }

    public  static class Subject {
        private String create;

        public String getCreate() {
            return create;
        }

        public void setCreate(String create) {
            this.create = create;
        }
    }
    public static class FileService{
        private String ipAddr;
        private String username;
        private String password;
        private String uploadServerFileRootPath;
        private String uploadServerImageRootPath;

        public String getUploadServerFileRootPath() {
            return uploadServerFileRootPath;
        }

        public void setUploadServerFileRootPath(String uploadServerFileRootPath) {
            this.uploadServerFileRootPath = uploadServerFileRootPath;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }


        public String getIpAddr() {
            return ipAddr;
        }

        public void setIpAddr(String ipAddr) {
            this.ipAddr = ipAddr;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUploadServerImageRootPath() {
            return uploadServerImageRootPath;
        }

        public void setUploadServerImageRootPath(String uploadServerImageRootPath) {
            this.uploadServerImageRootPath = uploadServerImageRootPath;
        }
    }
}
