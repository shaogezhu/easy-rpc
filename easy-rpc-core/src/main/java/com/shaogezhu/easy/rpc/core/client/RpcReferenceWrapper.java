package com.shaogezhu.easy.rpc.core.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author peng
 * @Date 2023/3/4
 * @description: rpc远程调用包装类
 */
public class RpcReferenceWrapper<T> {

    private Class<T> aimClass;

    private Map<String,Object> attatchments = new ConcurrentHashMap<>();

    public Class<T> getAimClass() {
        return aimClass;
    }

    public void setAimClass(Class<T> aimClass) {
        this.aimClass = aimClass;
    }

    /**
     * 失败重试次数
     */
    public int getRetry(){
        return (int) attatchments.getOrDefault("retry",0);
    }

    public void setRetry(int retry){
        this.attatchments.put("retry",retry);
    }

    public void setTimeOut(int timeOut) {
        attatchments.put("timeOut", timeOut);
    }

    public String getTimeOut() {
        return String.valueOf(attatchments.getOrDefault("timeOut",""));
    }
    public boolean isAsync(){
        return Boolean.parseBoolean(String.valueOf(attatchments.get("async")));
    }

    public void setAsync(boolean async){
        this.attatchments.put("async",async);
    }

    public String getUrl(){
        return String.valueOf(attatchments.get("url"));
    }

    public void setUrl(String url){
        attatchments.put("url",url);
    }

    public String getServiceToken(){
        return String.valueOf(attatchments.get("serviceToken"));
    }

    public void setServiceToken(String serviceToken){
        attatchments.put("serviceToken",serviceToken);
    }

    public String getGroup(){
        return String.valueOf(attatchments.get("group"));
    }

    public void setGroup(String group){
        attatchments.put("group",group);
    }

    public Map<String, Object> getAttatchments() {
        return attatchments;
    }

    public void setAttatchments(Map<String, Object> attatchments) {
        this.attatchments = attatchments;
    }
}