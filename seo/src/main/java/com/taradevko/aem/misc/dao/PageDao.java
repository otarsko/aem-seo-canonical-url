package com.taradevko.aem.misc.dao;

public interface PageDao {

    String getPagePath(String resourcePath);
    String externalizePagePath(String pagePath);
}
