package com.itheima.domain.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import java.io.ByteArrayInputStream;

public class AliUtils {
    private static String accessKeyId = "LTAI4FupkmcPM51FWHk7SfAq";
    private static String accessKeySecret = "xj8D2awHKmOX8ImwZI96YWQOEOjxV7";
    private static String bucketName = "itheima-health-java";
    private static String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";

    /**
     * 上传文件,上传Bytes数组
     *
     * @param objectName
     * @param bytes
     */
    public static void upload(String objectName, byte[] bytes) {
        //创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        //上传文件
        ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));
        //关闭OSSClient
        ossClient.shutdown();
    }

    /**
     * 删除单个文件
     */
    public static void delete(String objectName) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 删除文件。
        ossClient.deleteObject(bucketName, objectName);
        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
