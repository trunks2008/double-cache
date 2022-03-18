package com.cn.dc.biz.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: double-cache
 * @author: Hydra
 * @create: 2020-12-20 18:43
 **/
@Data
@Accessors(chain = true)
@TableName(value = "t_order")
public class Order {

    @TableId(value = "id")
    Long id;

    String orderNumber;

    Double money;

    Integer status;


}
