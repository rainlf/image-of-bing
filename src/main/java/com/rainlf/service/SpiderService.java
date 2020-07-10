package com.rainlf.service;

/**
 * @author : rain
 * @date : 2020/7/10 13:51
 */
public interface SpiderService {

    void downloadOneDay(String targetDir);

    void downloadEightDay(String targetDir);
}
