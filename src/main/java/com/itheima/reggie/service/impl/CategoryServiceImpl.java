package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据条件删除
     * @param id
     */
    @Override
    public void remove(Long id) {

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件，根据id进行查询
        queryWrapper.eq(Dish::getCategoryId, id);
        int count = dishService.count(queryWrapper);

        // 判断是否关联了其他菜品
        if (count > 0){
            // 已经关联菜品，抛出异常
            throw new CustomException("已经关联菜品无法删除");
        }

        // 判断是否关联了其他套餐
        LambdaQueryWrapper<Setmeal> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Setmeal::getCategoryId, id);
        int count1 = setmealService.count(queryWrapper1);
        if (count1 > 0){
            // 已经关联套餐，抛出异常
            throw new CustomException("已经关联套餐无法删除");
        }

        // 正常删除
        super.removeById(id);
    }
}
