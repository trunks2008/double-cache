package com.cn.dc.biz.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: double-cache
 * @author: Hydra
 * @create: 2022-03-11 13:28
 **/
@Data
@Accessors(chain = true)
@TableName(value = "t_stock")
public class Stock {

    @TableId(value = "id")
    Long id;

    Long proId;

    Integer total;

    Integer sold;

    Integer frozen;

}
