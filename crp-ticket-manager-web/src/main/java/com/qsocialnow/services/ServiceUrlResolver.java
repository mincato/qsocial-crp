package com.qsocialnow.services;

public interface ServiceUrlResolver {

    String resolveUrl(String clientName, String serviceUrlPath) throws Exception;

}
