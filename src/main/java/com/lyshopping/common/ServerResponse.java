package com.lyshopping.common;

import java.io.Serializable;

/**
 * @author liuying
 * 通用的数据响应对象
 **/
public class ServerResponse<T> implements Serializable {

    private int status;
    private String msg;
    private  T data;

    private ServerResponse(int status){
       this.status = status;
    }

    private ServerResponse(int status, T data){
        this.status = status;
        this.data = data;
    }

    private ServerResponse(int status, String msg, T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private ServerResponse(int Status, String msg){
        this.status = status;
        this.msg = msg;
    }

    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public static <T>  ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccess(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccess(String msg, T Data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public

}
