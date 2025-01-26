package com.helix.dove.common.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

/**
 * Common Page Response Object
 */
@Data
public class CommonPage<T> {
    private Long pageNum;
    private Long pageSize;
    private Long totalPage;
    private Long total;
    private List<T> list;

    /**
     * Convert MyBatis Plus Page to CommonPage
     */
    public static <T> CommonPage<T> restPage(IPage<T> pageResult) {
        CommonPage<T> result = new CommonPage<>();
        result.setPageNum(pageResult.getCurrent());
        result.setPageSize(pageResult.getSize());
        result.setTotal(pageResult.getTotal());
        result.setTotalPage(pageResult.getPages());
        result.setList(pageResult.getRecords());
        return result;
    }

    /**
     * Convert List to CommonPage
     */
    public static <T> CommonPage<T> restPage(List<T> list) {
        CommonPage<T> result = new CommonPage<>();
        result.setPageNum(1L);
        result.setPageSize((long) list.size());
        result.setTotal((long) list.size());
        result.setTotalPage(1L);
        result.setList(list);
        return result;
    }
} 