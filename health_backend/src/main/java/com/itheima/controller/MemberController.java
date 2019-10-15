package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.domain.constant.MessageConstant;
import com.itheima.domain.enity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.SetmealService;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report")
public class MemberController {
    @Reference
    MemberService memberService;
    @Reference
    SetmealService setmealService;
    @Autowired
    JedisPool jedisPool;

    /**
     * 会员数量折线图
     *
     * @return
     */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {
        try {
            Map<String, Object> map = memberService.getMemberReport();
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    /**
     * 套餐预约占比饼形图
     *
     * @return
     */
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() {
        try {
            Map map = setmealService.getSetmealReport();
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    /**
     * 运营数据统计
     *
     * @return
     */
    @RequestMapping("/getBusinessReportData")
    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    public Result getBusinessReportData() {
        try {
            Map<String, Object> map = memberService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    @RequestMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpSession session, HttpServletResponse response) throws Exception {
        try {
            //从jedis缓存中获取数据
            String reportData = jedisPool.getResource().get("reportData");
            Map<String,Object> result = JSON.parseObject(reportData, Map.class);

            if (result == null) {
                //调用service查询表中元素
                 result = memberService.getBusinessReportData();
            }

            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");
            String reportDate = (String) result.get("reportDate");
            //获取文件的绝对路径
            String path = session.getServletContext().getRealPath("/") + "template/report_template.xlsx";
            //创建workbook,工作簿
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(path)));
            //创建sheet
            XSSFSheet sheet = workbook.getSheetAt(0);
            //创建行
            XSSFRow row = sheet.getRow(2);
            XSSFCell cell = row.getCell(5);
            cell.setCellValue(reportDate);

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int rowNum = 12;
            for (Map map : hotSetmeal) {
                String name = (String) map.get("name");
                Integer setmeal_count = (Integer) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }
            //进行输出
            ServletOutputStream outputStream = response.getOutputStream();

            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            response.setContentType("application/vnd.ms-excel");
            workbook.write(outputStream);
            //关闭流
            outputStream.flush();
            outputStream.close();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
