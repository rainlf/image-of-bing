package com.rainlf.model;

import lombok.Data;

/**
 * @author rain
 * @date 2022-08-02 13:21
 */
@Data
public class AppArguments {
    public static String targetDir = "./store";
    public static String targetScript = "./auto_commit.bat";
    public static String imageDir = "/image/";
    public static String videoDir = "/video/";
    public static String videoHdDir = "/video_hd/";
    public static String videoMobileDir = "/video_mobile/";
    public static String day1Url = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&video=1";
    public static String day8Url = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=8&video=1";

}
