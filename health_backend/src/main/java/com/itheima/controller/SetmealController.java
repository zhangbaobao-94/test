package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.domain.constant.MessageConstant;
import com.itheima.domain.constant.RedisConstant;
import com.itheima.domain.enity.PageResult;
import com.itheima.domain.enity.QueryPageBean;
import com.itheima.domain.enity.Result;
import com.itheima.domain.pojo.Setmeal;
import com.itheima.domain.utils.AliUtils;
import com.itheima.domain.utils.QiniuUtils;
import com.itheima.service.SetmealService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;
    @Autowired
    JedisPool jedisPool;

    /**
     * 使用七牛云上传文件
     *
     * @param imgFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
        //获取文件的原始名称
        String originalFilename = imgFile.getOriginalFilename();
        //获取文件的后缀名
        String extension = FilenameUtils.getExtension(originalFilename);
        //给文件重新起一个名称
        String fileName = UUID.randomUUID().toString() + "." + extension;
        try {
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            //把上传的图片名称存放到Redis中
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, fileName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);

        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 用阿里云上传文件
     *
     * @param imgFile
     * @return
     */
    @RequestMapping("/upload1")
    public Result aliUpload(@RequestParam(value = "imgFile") MultipartFile imgFile) {
        String originalFilename = imgFile.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename);
        String fileName = UUID.randomUUID().toString() + "." + extension;
        try {
            AliUtils.upload(fileName, imgFile.getBytes());
            //把上传的图片名称存放到Redis中
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, fileName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 添加检查套餐
     *
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        try {
            setmealService.add(setmeal, checkgroupIds);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    /**
     * 分页条件查询
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/page")
    public PageResult page(@RequestBody QueryPageBean queryPageBean) {
        return setmealService.page(queryPageBean);
    }

    /**
     * 删除操作
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Integer id) {

        try {
            setmealService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
    }

    @RequestMapping("/findById")
    public Result update(Integer id) {
        try {
            Map<String,Object> map = setmealService.findById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    /**
     * 检查套餐的编辑
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        try {
            setmealService.update(setmeal, checkgroupIds);
            return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }
}
