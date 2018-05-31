package com.nix.cinema.service.impl;

import com.nix.cinema.Exception.WebException;
import com.nix.cinema.common.ReturnObject;
import com.nix.cinema.model.BookInfoModel;
import com.nix.cinema.model.MemberModel;
import com.nix.cinema.service.BaseService;
import com.nix.cinema.service.WebSocketServer;
import com.nix.cinema.util.ReturnUtil;
import com.nix.cinema.util.ServiceUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kiss
 * @date 2018/05/25 23:07
 */
@Service
public class BookInfoService extends BaseService<BookInfoModel> {
    @Autowired
    private MemberService memberService;

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

    public ReturnObject importBookInfo(MultipartFile multipartFile) throws Exception {
        Workbook workBook = getWorkBook(multipartFile);
        //获取工作表
        Sheet sheet = workBook.getSheetAt(0);
        List<BookInfoModel> bookInfos = new ArrayList<>();
        //获取行,行号作为参数传递给getRow方法,第一行从0开始计算
        //忽略第一行
        //获得当前sheet的开始行
        int firstRowNum  = sheet.getFirstRowNum();
        //获得当前sheet的结束行
        int lastRowNum = sheet.getLastRowNum();
        for (int i = firstRowNum + 1;i  <= lastRowNum ;i ++ ) {
            Row row =  sheet.getRow(i);
            //获取单元格,row已经确定了行号,列号作为参数传递给getCell,第一列从0开始计算
            BookInfoModel bookInfoModel = new BookInfoModel();
            bookInfoModel.setSn(getCellValue(row.getCell(0)));
            bookInfoModel.setName(getCellValue(row.getCell(1)));
            Assert.isTrue(bookInfoModel.getName() != null && !bookInfoModel.getName().isEmpty(),
                    getErrorMsg("图书名不能为空",i));
            bookInfoModel.setAuthor(getCellValue(row.getCell(2)));
            bookInfoModel.setTranslator(getCellValue(row.getCell(3)));
            if (getCellValue(row.getCell(4)) != null || !getCellValue(row.getCell(4)).isEmpty()) {
                try {
                    bookInfoModel.setPrice(new BigDecimal(Double.parseDouble(getCellValue(row.getCell(4)))));
                } catch (Exception e) {
                    Assert.isTrue(false,
                            getErrorMsg("价格错误",i));
                }
            }
            bookInfoModel.setISBNCode(getCellValue(row.getCell(5)));
            bookInfoModel.setComeUpTime(row.getCell(6).getDateCellValue());
            try {
                String status = getCellValue(row.getCell(7));
                if ("1".equals(status) || "0".equals(status)) {
                    status = status.equals("1") ? "true" : "false";
                }
                bookInfoModel.setStatus(Boolean.parseBoolean(status));
            } catch (Exception e) {
                Assert.isTrue(false,
                        getErrorMsg("图书名状态错误",i));
            }
            String username = getCellValue(row.getCell(8));
            MemberModel member = memberService.findByUsername(username);
            Assert.isTrue(bookInfoModel.getName() != null ,
                    getErrorMsg("入库用户错误",i));
            bookInfoModel.setMember(member);
            bookInfoModel.setEnteringDate(row.getCell(9).getDateCellValue());
            bookInfoModel.setIntroduce(getCellValue(row.getCell(10)));
            bookInfos.add(bookInfoModel);
        }
        for (BookInfoModel bookInfoModel:bookInfos) {
            add(bookInfoModel,null);
        }
        return ReturnUtil.success("导入成功",null);
    }
    public Workbook getWorkBook(MultipartFile file) {
        //获得文件名
        String fileName = file.getOriginalFilename();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if(fileName.endsWith("xls")){
                //2003
                workbook = new HSSFWorkbook(is);
            }else if(fileName.endsWith("xlsx")){
                //2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
        }
        return workbook;
    }
    private String getErrorMsg(String msg,int i) {
        return "第" + i + "行：" + msg;
    }
    private String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        //把数字当成String来读，避免出现1读成1.0的情况
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
        //判断数据的类型
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: //数字
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING: //字符串
                cellValue = String.valueOf(cell);
                break;
            case Cell.CELL_TYPE_BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_BLANK: //空值
                cellValue = "";
                break;
            case Cell.CELL_TYPE_ERROR: //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }
}
