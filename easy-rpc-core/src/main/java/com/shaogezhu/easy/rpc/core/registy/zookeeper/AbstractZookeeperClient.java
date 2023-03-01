package com.shaogezhu.easy.rpc.core.registy.zookeeper;

import org.apache.zookeeper.Watcher;

import java.util.List;

/**
 * @Author peng
 * @Date 2023/2/27
 * @description: Zookeeper 模版抽象类
 */
public abstract class AbstractZookeeperClient {

    /**
     * 注册地址 ip:port
     */
    private String zkAddress;
    private int baseSleepTimes;
    private int maxRetries;

    public AbstractZookeeperClient(String zkAddress) {
        this.zkAddress = zkAddress;
        //默认3000ms
        this.baseSleepTimes = 1000;
        this.maxRetries = 3;
    }

    public AbstractZookeeperClient(String zkAddress, Integer baseSleepTimes, Integer maxRetryTimes) {
        this.zkAddress = zkAddress;
        if (baseSleepTimes == null) {
            this.baseSleepTimes = 1000;
        } else {
            this.baseSleepTimes = baseSleepTimes;
        }
        if (maxRetryTimes == null) {
            this.maxRetries = 3;
        } else {
            this.maxRetries = maxRetryTimes;
        }
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public int getBaseSleepTimes() {
        return baseSleepTimes;
    }

    public void setBaseSleepTimes(int baseSleepTimes) {
        this.baseSleepTimes = baseSleepTimes;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    /**
     * 更新指定目录下的字节点数据
     *
     * @param address
     * @param data
     */
    public abstract void updateNodeData(String address, String data);

    /**
     * 获取客户端对象
     *
     * @return
     */
    public abstract Object getClient();

    /**
     * 拉取节点的数据
     *
     * @param path
     * @return
     */
    public abstract String getNodeData(String path);

    /**
     * 获取指定目录下的字节点数据
     *
     * @param path
     * @return
     */
    public abstract List<String> getChildrenData(String path);

    /**
     * 创建持久化类型节点数据信息
     *
     * @param address
     * @param data
     */
    public abstract void createPersistentData(String address, String data);

    /**
     * 创建有序且持久类型节点数据信息
     *
     * @param address
     * @param data
     */
    public abstract void createPersistentWithSeqData(String address, String data);


    /**
     * 创建有序且临时类型节点数据信息
     *
     * @param address
     * @param data
     */
    public abstract void createTemporarySeqData(String address, String data);


    /**
     * 创建临时节点数据类型信息
     *
     * @param address
     * @param data
     */
    public abstract void createTemporaryData(String address, String data);

    /**
     * 设置某个节点的数值
     *
     * @param address
     * @param data
     */
    public abstract void setTemporaryData(String address, String data);

    /**
     * 断开zk的客户端链接
     */
    public abstract void destroy();


    /**
     * 展示节点下边的数据
     *
     * @param address
     * @return
     */
    public abstract List<String> listNode(String address);


    /**
     * 删除节点下边的数据
     *
     * @param address
     * @return
     */
    public abstract boolean deleteNode(String address);


    /**
     * 判断是否存在其他节点
     *
     * @param address
     * @return
     */
    public abstract boolean existNode(String address);


    /**
     * 监听path路径下某个节点的数据变化
     *
     * @param path
     * @param watcher
     */
    public abstract void watchNodeData(String path, Watcher watcher);

    /**
     * 监听子节点下的数据变化
     *
     * @param path
     * @param watcher
     */
    public abstract void watchChildNodeData(String path, Watcher watcher);

}