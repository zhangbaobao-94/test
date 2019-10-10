package com.itheima.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

public class POITest {

    /**
     * 用POI写入数据
     */
    @Test
    public void test1() throws Exception {
        //在内存中创建工作簿
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        //创建sheet
        XSSFSheet sheet = xssfWorkbook.createSheet("传智健康");
        //创建行
        XSSFRow title = sheet.createRow(2);
        //创建单元格
        title.createCell(1).setCellValue("姓名");
        title.createCell(2).setCellValue("年龄");
        title.createCell(3).setCellValue("性别");

        XSSFRow row = sheet.createRow(1);
        row.createCell(1).setCellValue("路飞");
        row.createCell(2).setCellValue(18);
        row.createCell(3).setCellValue("男");

        //以输出流在磁盘上进行输出
        FileOutputStream outputStream = new FileOutputStream("D:\\itcast.xlsx");
        xssfWorkbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
        xssfWorkbook.close();
    }

    /**
     * 用POI读取数据1
     */
    @Test
    public void test2() throws IOException {
        //创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook("D:\\itcast.xlsx");
        //创建sheet
        XSSFSheet sheet = workbook.getSheetAt(0);
        //获取row
        for (Row cells : sheet) {
            //获取单元格Cell
            for (Cell cell : cells) {
//                String stringCellValue = cell.getStringCellValue();
//                System.out.println(stringCellValue);
                System.out.println(cell);

            }
        }
        workbook.close();
    }

    /**
     * 用POI读取数据3
     */
//    @Test
//    public void test3() throws IOException {
//        //创建工作簿
//        XSSFWorkbook workbook = new XSSFWorkbook("D:\\itcast1.xlsx");
//        //创建sheet
//        XSSFSheet sheet = workbook.getSheetAt(0);
//        //获取最后一行(有数据的),行号是从0开始算的
//        int lastRowNum = sheet.getLastRowNum();
//
//        for (int i = 0; i <= lastRowNum; i++) {
//            //获取最后一个单元格,是按列数算的
//            XSSFRow row = sheet.getRow(i);
//            short lastCellNum = row.getLastCellNum();//得到的是列数,如总共4列
//            System.out.println("最后一列是:"+lastCellNum);//获取第一列的索引,是从0开始算的
//            System.out.println("第一列是:"+row.getFirstCellNum());
//            System.out.println("当前行的列数是:"+row.getPhysicalNumberOfCells());
//            for (int j = 0; j < lastCellNum; j++) {
//                System.out.println(row.getCell(j));
////                String value = sheet.getRow(i).getCell(j).getStringCellValue();
////                System.out.println(value);
//            }
//        }
//        workbook.close();
//    }


}
