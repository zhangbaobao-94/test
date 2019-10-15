package com.itheima.Jobs;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.domain.constant.RedisConstant;
import com.itheima.domain.utils.AliUtils;
import com.itheima.service.OrdersettingService;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrdersettingService ordersettingService;


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
                System.out.println("成功删除文件" + img);
            }
        }
    }

    public void clearExcel() throws Exception {
        //调用dao删除t_ordersetting里面的数据
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        ordersettingService.deleteByOrderDate(today);
    }
}
