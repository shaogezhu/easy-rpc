package com.shaogezhu.easy.rpc.spring.starter.config;

import com.shaogezhu.easy.rpc.core.server.Server;
import com.shaogezhu.easy.rpc.core.server.ServerShutdownHook;
import com.shaogezhu.easy.rpc.core.server.ServiceWrapper;
import com.shaogezhu.easy.rpc.spring.starter.common.EasyRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

import static com.shaogezhu.easy.rpc.core.common.cache.CommonServerCache.SERVER_CONFIG;

/**
 * @Author peng
 * @Date 2023/3/11
 * @description: 服务端自动配置类
 */
public class RpcServerAutoConfiguration implements InitializingBean, ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServerAutoConfiguration.class);

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        Server server = null;
        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(EasyRpcService.class);
        if (beanMap.size() == 0) {
            //说明当前应用内部不需要对外暴露服务
            return;
        }
        printBanner();
        long begin = System.currentTimeMillis();
        server = new Server();
        server.initServerConfig();
        for (String beanName : beanMap.keySet()) {
            Object bean = beanMap.get(beanName);
            EasyRpcService easyRpcService = bean.getClass().getAnnotation(EasyRpcService.class);
            ServiceWrapper dataServiceServiceWrapper = new ServiceWrapper(bean, easyRpcService.group());
            dataServiceServiceWrapper.setServiceToken(easyRpcService.serviceToken());
            dataServiceServiceWrapper.setLimit(easyRpcService.limit());
            dataServiceServiceWrapper.setWeight(easyRpcService.weight());
            server.registyService(dataServiceServiceWrapper);
            LOGGER.info(">>>>>>>>>>>>>>> [easy-rpc] {} export success! >>>>>>>>>>>>>>> ", beanName);
        }
        ServerShutdownHook.registryShutdownHook();
        server.startServerApplication();
        long end = System.currentTimeMillis();
        LOGGER.info(" ================== [{}] started success in {}s ================== ", SERVER_CONFIG.getApplicationName(), ((double) end - (double) begin) / 1000);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void printBanner() {
        System.out.println();
        System.out.println("==============================================");
        System.out.println("|||---------- Easy Rpc Starting Now! ----------|||");
        System.out.println("==============================================");
        System.out.println("源代码地址: https://github.com/shaogezhu/easy-rpc");
        System.out.println("version: 1.0.0");
        System.out.println();
    }
}
