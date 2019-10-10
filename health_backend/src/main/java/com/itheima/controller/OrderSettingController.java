package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.domain.constant.MessageConstant;
import com.itheima.domain.enity.Result;
import com.itheima.domain.pojo.OrderSetting;
import com.itheima.domain.utils.POIUtils;
import com.itheima.service.OrdersettingService;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrdersettingService ordersettingService;

    /**
     * 文件的上传
     *
     * @param excelFile
     * @return
     */
    @RequestMapping("/upload1")
    public Result upload(MultipartFile excelFile) {
        String originalFilename = excelFile.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename);
        Workbook workbook = null;
        //根据文件创建workbook
        try {
            if ("xls".equals(extension)) {
                workbook = new HSSFWorkbook(excelFile.getInputStream());
            } else if ("xlsx".equals(extension)) {
                workbook = new XSSFWorkbook(excelFile.getInputStream());
            } else {
                return new Result(false, MessageConstant.UPLOAD_FAIL);
            }
            //获取sheet
            Sheet sheet = workbook.getSheetAt(0);
            //获取行
            int lastRowNum = sheet.getLastRowNum();
            List<OrderSetting> orderSettingList = new ArrayList<>();
            for (int i = 0; i <= lastRowNum; i++) {

                if (i == 0) {
                    //跳过标题行
                    continue;
                }
                //获取单元格
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                Date dateCellValue = row.getCell(0).getDateCellValue();
                Double numericCellValue = row.getCell(1).getNumericCellValue();
                OrderSetting orderSetting = new OrderSetting();
                if (dateCellValue != null && numericCellValue != null) {
                    orderSetting.setOrderDate(dateCellValue);
                    orderSetting.setNumber(numericCellValue.intValue());
                    orderSettingList.add(orderSetting);
                }
            }
            //完成数据的保存
            ordersettingService.saveFile(orderSettingList);
            return new Result(true, MessageConstant.UPLOAD_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.UPLOAD_FAIL);
        }
    }

    /**
     * 文件的上传
     *
     * @param excelFile
     * @return
     */
    @RequestMapping("/upload")
    public Result uploadFile(MultipartFile excelFile) {
        try {
            List<String[]> list = POIUtils.upload(excelFile);
            if (list == null) {
                return new Result(false, MessageConstant.UPLOAD_FAIL);
            }
            List<OrderSetting> orderSettings = new ArrayList<>();
            for (String[] strings : list) {
                OrderSetting orderSetting = new OrderSetting(new Date(strings[0]), Integer.parseInt(strings[1]));
                orderSettings.add(orderSetting);
            }

            ordersettingService.saveFile(orderSettings);
            return new Result(true, MessageConstant.UPLOAD_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.UPLOAD_FAIL);
        }
    }

    /**
     * 批量导入预约设置信息
     * @param date
     * @return
     */
    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date) {
        try {
            List<Map<String, Object>> list = ordersettingService.getOrderSettingByMonth(date);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    /**
     * 单独修改可预约人数
     * @param orderSetting
     * @return
     */
    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {
        try {
            ordersettingService.editNumberByDate(orderSetting);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }
}

