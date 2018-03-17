package edu.scu.my_shop.result;

import java.util.List;

/**
 * @Author: 周秦春
 * @Description: 封装了分页的内容以及其他分页信息
 * @Date: Create in 2017/8/24 14:08
 * @ModifyBy:
 */
public class PageResult<T> {

    /**
     * 总页数
     */
    private int totalPages;

    /**
     * 总的元素个数
     */
    private long totalElements;

    /**
     * 第几页
     */
    private int number;

    /**
     * 当前页的元素个数
     */
    private int numberOfElements;

    /**
     * 存储的内容
     */
    private List<T> content;

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

}
