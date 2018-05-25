package com.nix.cinema.controller.admin;
import com.nix.cinema.common.annotation.AdminController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kiss
 * @date 2018/05/05 20:02
 */
@AdminController
@RequestMapping("/system")
public class SystemController {
    private final static String UPLOAD_IMAGE_PATH = "/file/images/";
    @PostMapping("/upload")
    public Map upload(@RequestParam("file") MultipartFile file,
                      @RequestParam("fileType") String fileType) throws Exception {
        Map<String,Object> data = new HashMap<>();
        String fileName = file.getOriginalFilename();
        String url;
        switch (fileType) {
            case "image": {
                File image = new File(this.getClass().getResource("/").getFile() + UPLOAD_IMAGE_PATH + fileName);
                file.transferTo(image);
                url = UPLOAD_IMAGE_PATH + fileName;
                data.put("message", "操作成功");
                data.put("state", "SUCCESS");
            }break;
            default:return null;
        }
        data.put("url", url);
        return data;
    }

}
