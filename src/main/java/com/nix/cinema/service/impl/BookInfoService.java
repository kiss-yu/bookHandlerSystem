package com.nix.cinema.service.impl;

import com.nix.cinema.model.BookInfoModel;
import com.nix.cinema.service.BaseService;
import com.nix.cinema.service.WebSocketServer;
import com.nix.cinema.util.ServiceUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author Kiss
 * @date 2018/05/25 23:07
 */
@Service
public class BookInfoService extends BaseService<BookInfoModel> {
    private final static String DEFAULT_IMAGE = "default.jpg";
    private final static String IMAGE_PATH = "/images/bookInfo/";
    public final static Object BORROW_CLOCK = new Object();
    public BookInfoModel add(BookInfoModel model,MultipartFile portraitImg) throws Exception {
        model.setSn(model.getSn() == null || model.getSn().isEmpty() ? model.generateSn() : model.getSn());

        if (portraitImg != null) {
            model.setImage(IMAGE_PATH + portraitImg.getOriginalFilename());
        } else {
            model.setImage(IMAGE_PATH + DEFAULT_IMAGE);
        }
        super.add(model);
        if (portraitImg != null) {
            File image = new File(this.getClass().getResource("/").getFile() + IMAGE_PATH + portraitImg.getOriginalFilename());
            portraitImg.transferTo(image);
        }
        WebSocketServer.setBasicMsgSumCount();
        return model;
    }
    public BookInfoModel update(BookInfoModel model,MultipartFile portraitImg) throws Exception {
        if (portraitImg != null) {
            BookInfoModel before = findById(model.getId());
            model.setImage(IMAGE_PATH + portraitImg.getOriginalFilename());
            ServiceUtil.updateImage(before.getImage(),model.getImage(),IMAGE_PATH + DEFAULT_IMAGE,portraitImg);
        }
        return super.update(model);
    }

    @Transactional(rollbackFor = Exception.class)
    public BookInfoModel borrow(BookInfoModel model) throws Exception {
        model.setStatus(false);
        return super.update(model);
    }

    public BookInfoModel returnBack(BookInfoModel model) throws Exception {
        model.setStatus(true);
        return super.update(model);
    }
}
