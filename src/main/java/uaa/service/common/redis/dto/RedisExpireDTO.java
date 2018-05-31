package uaa.service.common.redis.dto;

public class RedisExpireDTO extends RedisKeyDTO {
    private Long timeout;

    public RedisExpireDTO() {
    }

    public Long getTimeout() {
        return this.timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }
}
