package com.rainlf.model;

import lombok.Data;
import java.util.List;

/**
 * @author : rain
 * @date : 2020/7/10 14:02
 */
@Data
public class BingResponse {
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
