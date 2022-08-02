package com.rainlf.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : rain
 * @date : 2020/7/10 14:40
 */
@Data
@NoArgsConstructor
public class UrlInfo {
    public String imageUrl;
    public String mp4Url;
    public String mp4HdUrl;
    public String mp4MobileUrl;

    private static String videoPrefix = "https:";
    private static String imagePrefix = "https://cn.bing.com";

    public UrlInfo(BingResponse.Image image) {
        if (image.getVid() == null) {
            imageUrl = imagePrefix + image.getUrl();
        } else {
            imageUrl = videoPrefix + image.getVid().getImage();
            mp4Url = videoPrefix + image.getVid().getSources().get(0).get(2);
            mp4HdUrl = videoPrefix + image.getVid().getSources().get(1).get(2);
            mp4MobileUrl = videoPrefix + image.getVid().getSources().get(2).get(2);
        }
    }

    private String getName(String value) {
        return value.substring(value.indexOf("=") + 1, value.indexOf("&"));
    }
}
