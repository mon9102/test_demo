package com.testDemo.bean;

/**
 * @author : zouren
 * @date : 2020/3/12 17:11
 */
public class CustomerRightsServerDto {
    private String apcServiceItemPackageId;
    private String name;
    private int use_value;
    private int use_type=0;
    private int use;

    public String getApcServiceItemPackageId() {
        return apcServiceItemPackageId;
    }

    public void setApcServiceItemPackageId(String apcServiceItemPackageId) {
        this.apcServiceItemPackageId = apcServiceItemPackageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUse_value() {
        return use_value;
    }

    public void setUse_value(int use_value) {
        this.use_value = use_value;
    }

    public int getUse_type() {
        return use_type;
    }

    public void setUse_type(int use_type) {
        this.use_type = use_type;
    }

    public int getUse() {
        return use;
    }

    public void setUse(int use) {
        this.use = use;
    }
}

