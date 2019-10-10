package com.itheima.Jobs;

import com.itheima.domain.constant.RedisConstant;
import com.itheima.domain.utils.AliUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;


    public void clearImg() {
        //把Redis中的两个set集合的数据求差值
        Set<String> imgs = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (imgs != null) {
            for (String img : imgs) {
                //调用七牛云的删除方法
                /*QiniuUtils.deleteFileFromQiniu(img);*/

                //调用阿里云的删除方法
                AliUtils.delete(img);
                //把上传图片的集合对应的数值进行删除
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, img);
                //测试输出
                System.out.println("成功删除文件"+img);
            }
        }
    }
}
