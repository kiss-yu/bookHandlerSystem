package com.nix.cinema.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author Kiss
 * @date 2018/05/27 19:39
 */
public final class ServiceUtil {

    public static void updateImage(String before,String now,String defaultImage,MultipartFile multipartFile) throws IOException {
        if (!before.equals(now)) {
            if (!before.equals(defaultImage)) {
                File beforeFile = new File(ServiceUtil.class.getResource("/").getFile() + before);
                File nowFile = new File(ServiceUtil.class.getResource("/").getFile() + now);
                beforeFile.delete();
                multipartFile.transferTo(nowFile);
            }
        }
    }
}
