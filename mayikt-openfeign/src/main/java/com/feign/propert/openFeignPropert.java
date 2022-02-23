package com.feign.propert;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author : weiwei
 * @date : 2021-12-1 16:51
 * @description :
 * @modified By :
 * @Version : 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "cloud.fegin")
@Component
public class openFeignPropert {

    private Integer timeout;

    private String name;

    private String url;

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
