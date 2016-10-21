package com.qsocialnow.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LoginConfigBean {

    @Value("${login.url}")
    private String loginUrl;
    
    @Value("${zookeeper.login.path")
    private String zookeeperLoginPath;

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }
    
    public String getZookeeperLoginPath() {
		return zookeeperLoginPath;
	}
    
    public void setZookeeperLoginPath(String zookeeperLoginPath) {
		this.zookeeperLoginPath = zookeeperLoginPath;
	}

}
