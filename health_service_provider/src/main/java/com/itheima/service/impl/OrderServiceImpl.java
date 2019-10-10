package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberMapper;
import com.itheima.dao.OrderMapper;
import com.itheima.dao.OrdersettingMapper;
import com.itheima.domain.constant.MessageConstant;
import com.itheima.domain.enity.Result;
import com.itheima.domain.pojo.Member;
import com.itheima.domain.pojo.Order;
import com.itheima.domain.pojo.OrderSetting;
import com.itheima.domain.utils.DateUtils;
import com.itheima.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersettingMapper ordersettingMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 提交预约功能
     *
     * @param map
     * @return
     */
    @Override
    public Result submit(Map map) throws Exception {
        /**
         * 1.先校验预约的日期能预约t_ordersetting中
         * 2.看预约人数是否满了
         * 3.看预约的人在t_member中是否存在,通过手机号
         *      4.如果存在,获取id值,orderDate,setmealId在t_order表中查询是否已存在
         *          6.不存在,进行信息的预约,在t_order表中进行添加
         *          7.存在,返回已预约的信息提示
         *      5.不存在,在t_member中进行信息的添加,获取id值,进行信息的预约,在t_order中进行信息的添加
         * 8.预约成功后,把t_ordersetting表中的reservations的数值+1
         * 9.把t_order中的id值作为返回的data值
         */

        String orderDate = (String) map.get("orderDate");
        String telephone = (String) map.get("telephone");
        String setmealId = (String) map.get("setmealId");
        String sex = (String) map.get("sex");
        String name = (String) map.get("name");
        String idCard = (String) map.get("idCard");
        String orderType = (String) map.get("orderType");

        //先根据orderDate在t_ordersetting表中查询对象
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = ordersettingMapper.findDate(date);
        //判断对象是否为空
        if (orderSetting == null) {
            //为空,返回信息提示
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //不为空,获取里面的属性number,reservations,比较大小
        if (orderSetting.getReservations() >= orderSetting.getNumber()) {
            //如果大于等于,则返回信息提示
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        //如果小于,则继续校验
        //根据手机号telephone在t_member表中查询对象,判断是否为会员
        Member member = memberMapper.findByTelephone(telephone);
        //成功预约后,更新表t_ordersetting,reservations的值+1
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        if (member != null) {
            //在t_order表中查询是否已经预约
            Integer member_id = member.getId();
            Order order = new Order(member_id, date, Integer.parseInt(setmealId));

            long result = orderMapper.isOrder(order);
            if (result > 0) {
                //如果有查询到,则代表已预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        } else {
            //没有注册,进行注册,即在t_member中添加对象
            Member member_register = new Member();
            member_register.setName(name);
            member_register.setSex(sex);
            member_register.setIdCard(idCard);
            member_register.setPhoneNumber(telephone);
            member_register.setRegTime(new Date());
            memberMapper.add(member_register);
            Integer member_id = member_register.getId();
        }
        Member member_telephone = memberMapper.findByTelephone(telephone);
        Order order = new Order(null, member_telephone.getId(), date, orderType, Order.ORDERSTATUS_NO, Integer.parseInt(setmealId));
        orderMapper.add(order);
        //更新t_ordersetting表中的reservations的值+1
        ordersettingMapper.updateReservations(orderSetting);
        return new Result(true, MessageConstant.ORDER_SUCCESS, order.getId());
    }

    /**
     * 根据order表的id值查询相关联表的值
     *
     * @param id
     * @return
     */
    @Override
    public Map findById(Integer id) {
        Map map = orderMapper.findAllBy(id);
        return map;
    }
}
