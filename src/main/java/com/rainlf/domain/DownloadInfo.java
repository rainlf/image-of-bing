package com.rainlf.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : rain
 * @date : 2020/7/10 14:40
 */
@Data
@NoArgsConstructor
public class DownloadInfo {
    public String imageUrl;
    public String mp4Url;
    public String mp4HdUrl;
    public String mp4MobileUrl;

    private static String httpPrefix = "http:";
    private static String httpsPrefix = "https://cn.bing.com";

    public DownloadInfo(BingInfo.Image image) {
        if (image.getVid() == null) {
            imageUrl = httpsPrefix + image.getUrl();
        } else {
            imageUrl = httpPrefix + image.getVid().getImage();
            mp4Url = httpPrefix + image.getVid().getSources().get(0).get(2);
            mp4HdUrl = httpPrefix + image.getVid().getSources().get(1).get(2);
            mp4MobileUrl = httpPrefix + image.getVid().getSources().get(2).get(2);
        }
    }

    private String getName(String value) {
        return value.substring(value.indexOf("=") + 1, value.indexOf("&"));
    }
}
