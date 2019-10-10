package com.itheima.domain.utils;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class POIUtils {


    public static List<String[]> upload(MultipartFile excelFile) throws IOException {
        //对文件进行判断
        checkFile(excelFile);
        //获取workbook工作簿
        Workbook workbook = getWorkbook(excelFile);
        List<String[]> list = new ArrayList<>();
        //获取sheet
        if (workbook != null) {
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                //获取当前的sheet
                Sheet sheet = workbook.getSheetAt(i);
                if (sheet == null) {
                    continue;
                }
                //获取第一行
                int firstRowNum = sheet.getFirstRowNum();
                //获取最后一行
                int lastRowNum = sheet.getLastRowNum();
                for (int j = firstRowNum + 1; j <= lastRowNum; j++) {
                    //获取当前行
                    Row row = sheet.getRow(j);
                    if (row == null) {
                        continue;
                    }
                    //获取第一个单元格
                    short firstCellNum = row.getFirstCellNum();
                    short lastCellNum = row.getLastCellNum();
                    //设置数组的长度
                    String[] cells = new String[row.getPhysicalNumberOfCells()];
                    for (int k = firstCellNum; k < lastCellNum; k++) {
                        Cell cell = row.getCell(k);
                        String stringFormat = getStringFormat(cell);
                        cells[k] = stringFormat;
                    }
                    list.add(cells);
                }
            }
        }
        workbook.close();
        return list;
    }

    /**
     * 对文件进行校验,判断文件是否为Excel文件
     *
     * @param file
     * @throws IOException
     */
    public static void checkFile(MultipartFile file) throws IOException {
        if (file == null) {
            throw new FileNotFoundException("文件不存在");
        }
        //获取文件的名称
        String originalFilename = file.getOriginalFilename();
        if (!originalFilename.endsWith("xls") && !originalFilename.endsWith("xlsx")) {
            throw new IOException(originalFilename + "文件不是Excel文件");
        }
    }

    /**
     * 根据不同的格式获取workbook工作簿
     *
     * @param file
     * @return
     */
    public static Workbook getWorkbook(MultipartFile file) {
        //获取文件名称
        String originalFilename = file.getOriginalFilename();
        //创建工作簿workbook
        Workbook workbook = null;
        try {
            //获取文件的io流
            InputStream is = file.getInputStream();
            if (originalFilename.endsWith("xls")) {
                workbook = new HSSFWorkbook(is);
            } else if (originalFilename.endsWith("xlsx")) {
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }

    public static String getStringFormat(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        //如果当前单元格为日期类型,需要特殊处理
        String dataFormatString = cell.getCellStyle().getDataFormatString();
        if (dataFormatString.equals("m/d/yy")) {
            cellValue = new SimpleDateFormat("yyyy/MM/dd").format(cell.getDateCellValue());
            return cellValue;
        }
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
        //判断数据的类型
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC://数字
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING://字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN://Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA://公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_BLANK://空值
                cellValue = "";
                break;
            case Cell.CELL_TYPE_ERROR://故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }
}
