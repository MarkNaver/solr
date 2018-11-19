package com.demo.solr.entity.demo;

import org.apache.solr.client.solrj.beans.Field;

/**
 * Product
 * 商品实体类
 *
 * @author WuYuxiang
 * @date 2018/8/28
 */
public class Product {

    /**
     * ID 唯一标识
     */
    @Field
    private int id;

    /**
     * 商品名称
     */
    @Field
    private String title;

    /**
     * 商品类型
     */
    @Field
    private String type;

    /**
     * 商品价格
     */
    @Field
    private float price;

    /**
     * 发货地区
     */
    @Field
    private String area;

    /**
     * 商品编号
     */
    @Field
    private String productId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
