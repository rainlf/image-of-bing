package com.rainlf.domain;

import lombok.Data;
import java.util.List;

/**
 * @author : rain
 * @date : 2020/7/10 14:02
 */
@Data
public class BingInfo {

    private List<Image> images;

    @Data
    public static class Image {
        String url;
        Vid vid;
    }

    @Data
    public static class Vid {
        String image;
        List<List<String>> sources;
    }
}
