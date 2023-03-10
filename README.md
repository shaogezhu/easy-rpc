## é¡¹ç®ä»ç»ð¸



ä¸æ¬¾åºäº`Netty`+`Zookeeper`+`Spring`å®ç°çè½»éçº§`Java RPC`æ¡æ¶ãæä¾æå¡æ³¨åï¼åç°ï¼è´è½½åè¡¡ï¼æ¯æ`API`è°ç¨ï¼`Spring`éæå`Spring Boot starter`ä½¿ç¨ãæ¯ä¸ä¸ªå­¦ä¹ `RPC`å·¥ä½åççè¯å¥½ç¤ºä¾ã



éè¿è¿ä¸ªç®æé¡¹ç®çå­¦ä¹ ï¼å¯ä»¥è®©ä½ ä»é¶å¼å§å®ç°ä¸ä¸ªç±»ä¼¼` Dubbo` æå¡æ¡æ¶ mini ç`RPC`ï¼å­¦å°` RPC` çåºå±åçä»¥ååç§ `Java` ç¼ç å®è·µçè¿ç¨ãä¸é¢çä¸ä¸`RPC`çè°ç¨æµç¨ï¼

<img src="https://shaogezhu.cn/assets/2022-11/rpc2.png" style="zoom:66%;" />



## åè½&è®¾è®¡ð

### ç®å½ç»æ

```txt
Easy-rpcæ¡æ¶
ââeasy-rpc-core	--rpcæ ¸å¿å®ç°ç±»
ââeasy-rpc-spring-starter	--ç»ä»¶çspring-starteræ¥å¥ç±»
âârpc-framework-consumer	--[ç¤ºä¾]æå¡æ¶è´¹è
âârpc-framework-interface	--å­æ¾æå¡æ¥å£
âârpc-framework-provider	--[ç¤ºä¾]æå¡æä¾è
```


### åè½ï¼

- ç®åæå­¦çä»£ç åæ¡æ¶ï¼**å¨ä»£ç ä¸­å«æå¤§éæ³¨è§£**
- åºäº`Netty`å®ç°é¿è¿æ¥éä¿¡ï¼åæ¬å¿è·³æ£æµãè§£å³ç²åååç­
- åºäº`Zookeeper`å®ç°åå¸å¼æå¡æ³¨åä¸åç°
- å®ç°äºè½®è¯¢ãéæºãå æéæºç­è´è½½åè¡¡ç®æ³
- å®ç°äºåæ­¥è°ç¨ãå¼æ­¥è°ç¨å¤ç§è°ç¨æ¹å¼
- æ¯æ`jdk`ã`javassist`çå¨æä»£çæ¹å¼
- æ¯æ`fastJson`ã`hessian`ã`kryo`ã`jdk`çåºååæ¹å¼
- æ¯æç®ææ©å±ç¹ï¼æ³åè°ç¨ç­åè½
- å å¥äº`Spring Boot Starter`



### è®¾è®¡ï¼

**`easy-rpc`æ¡æ¶è°ç¨æµç¨ï¼**

![æ¶æå¾](https://shaogezhu.cn/assets/2022-11/rpc9.png)



- **ä»£çå±**ï¼è´è´£å¯¹åºå±è°ç¨ç»èçå°è£ï¼
- **é¾è·¯å±**ï¼è´è´£æ§è¡ä¸äºèªå®ä¹çè¿æ»¤é¾è·¯ï¼å¯ä»¥ä¾åæäºæ¬¡æ©å±ï¼
- **è·¯ç±å±**ï¼è´è´£å¨éç¾¤ç®æ æå¡ä¸­çè°ç¨ç­éç­ç¥ï¼
- **åè®®å±**ï¼è´è´£è¯·æ±æ°æ®çè½¬ç å°è£ç­ä½ç¨ï¼
- **æ³¨åä¸­å¿**ï¼å³æ³¨æå¡çä¸ä¸çº¿ï¼ä»¥åä¸äºæéï¼éç½®å¨æè°æ´ç­åè½ï¼
- **å®¹éå±**ï¼å½æå¡è°ç¨åºç°å¤±è´¥ä¹åéè¦æå®¹éå±çååºè¾å©ï¼




## å¿«éå¼å§ð

### ç¯å¢åå¤

- JDK8 æä»¥ä¸
- Maven 3
- Zookeeper åæºæèéç¾¤å®ä¾



### å¯å¨ç¤ºä¾

**æ¹å¼ä¸**ï¼ä½¿ç¨æ¬é¡¹ç®ä¸­çæµè¯ç¨ä¾

1. å°é¡¹ç®åéå°æ¬å°

   ```shell
   git clone git@github.com:shaogezhu/easy-rpc.git
   ```

2. IDEAæå¼é¡¹ç®

   ä½¿ç¨ IDEA æå¼ï¼ç­å¾é¡¹ç®åå§åå®æã

3. è¿è¡`Zookeeper

   å¦ææ²¡æå®è£çè¿éè¦åå»ä¸è½½ã

4. ä¿®æ¹éç½®æä»¶

   ä¿®æ¹å®¢æ·ç«¯åæå¡ç«¯`rpc.properties`éç½®æä»¶ä¸­zookeeperçå°å(éç½®æä»¶ä¸­ä½é»çå°åä¸º`127.0.0.1:2181`)

5. å¯å¨é¡¹ç®ï¼æç§å¾ä¸­é¡ºåºï¼

   PSï¼å¯å¨é¡¹ç®åï¼è¦ç¡®ä¿`zookeeper`å·²å¯å¨.

â		<img src="https://shaogezhu-images.oss-cn-beijing.aliyuncs.com/my/run-project.png" style="zoom:80%;" />

6. æå¼æµè§å¨æµè¯

   å¨æµè§å¨ä¸­è¾å¥`http://localhost:8081/user/test`æè`http://localhost:8081/user/list`ï¼ç¶åæ¥çé¡¹ç®çè¾åºæ¥å¿ã



**æ¹å¼äº**ï¼å°è¯¥`rpc`æ¡æ¶è¿ç¨å°èªå·±é¡¹ç®ä¸­

1. ä¸è½½æºç 

   ```shell
   git clone git@github.com:shaogezhu/easy-rpc.git
   ```

2. ç¼è¯å®è£ jar åå°æ¬å°ä»åºï¼æ³¨æå¦ææ¯æå¡å¨ä¸é¢ï¼éè¦ä¸ä¼ å°ç§æä»åºï¼

   ```shell
   mvn clean install
   ```

3. æ°å»º`Spring Boot Maven`å·¥ç¨

   å¨æ¬å°æ°å»ºä¸¤ä¸ªå·¥ç¨ï¼ç¨äºæ¨¡æå®¢æ·ç«¯åæå¡ç«¯ã

â	![](https://shaogezhu-images.oss-cn-beijing.aliyuncs.com/my/example.png)

â

4. å¼å¥å¥ä¾èµ

   å¨é¡¹ç®ä¸­ç`pom`å¼å¥ååå®è£çä¾èµï¼å®¢æ·ç«¯ãæå¡ç«¯é½éè¦å¼å¥ï¼
   ```xml
   <dependency>
       <groupId>com.shaogezhu</groupId>
       <artifactId>easy-rpc-spring-starter</artifactId>
       <version>1.0-SNAPSHOT</version>
   </dependency>
   ```

5. å®ä¹æå¡æ¥å£

   ```java
   /**
    * @Author peng
    * @Date 2023/2/25
    * @description: èªå®ä¹çæµè¯ç±»
    */
   public interface DataService {
   
       /**
        * åéæ°æ®
        * @param msg åå®¹
        * @return æå¡ç«¯æ¶æ¯
        */
       String sendData(String msg);
   }
   ```

6. å®ç°æ¥å£ï¼ä½¿ç¨èªå®ä¹æ³¨è§£`@EasyRpcService` æ´é²ä¸ä¸ªæå¡æ¥å£

   ```java
   /**
    * @Author peng
    * @Date 2023/3/11
    * @description: å®ç°ç±»
    */
   @EasyRpcService
   public class DataServiceImpl implements DataService {
   
       @Override
       public String sendData(String body) {
           System.out.println("è¿éæ¯æå¡æä¾èï¼body is " + body);
           return "success from server";
       }
   }
   ```

7. æå¡ç«¯éç½®

   å¨æå¡ç«¯æ¨¡åç`resource`æä»¶å¤¹ä¸æ°å»º`rpc.properties`æä»¶ï¼å¹¶å å¥ä»¥ä¸éç½®

   ```properties
   #æå¡ç«¯å¯¹å¤æ´é²çç«¯å£
   rpc.serverPort=8010
   #é¡¹ç®åç§°
   rpc.applicationName=rpc-provider
   #æ³¨åä¸­å¿ï¼zookeeperï¼çå°å
   rpc.registerAddr=127.0.0.1:2181
   #æ³¨åä¸­å¿ç±»å
   rpc.registerType=zookeeper
   #åºååæ¹å¼
   rpc.serverSerialize=fastJson
   ```

8. ä½¿ç¨èªå®ä¹æ³¨è§£ `@EasyRpcReference` èªå¨æ³¨å¥æå¡ç«¯æ´é²çæ¥å£æå¡

   ```java
   @RestController
   @RequestMapping(value = "/data")
   public class DataController {
   
       @EasyRpcReference
       private DataService dataService;
   
       @GetMapping(value = "/send/{msg}")
       public String sendMsg(@PathVariable(name = "msg") String msg){
           return dataService.sendData(msg);
       }
   }
   ```

9. å®¢æ·ç«¯éç½®

   å¨å®¢æ·ç«¯æ¨¡åç`resource`æä»¶å¤¹ä¸æ°å»º`rpc.properties`æä»¶ï¼å¹¶å å¥ä»¥ä¸éç½®

   ```properties
   #é¡¹ç®åç§°
   rpc.applicationName=rpc-consumer
   #æ³¨åä¸­å¿ï¼zookeeperï¼çå°å
   rpc.registerAddr=127.0.0.1:2181
   #æ³¨åä¸­å¿ç±»å
   rpc.registerType=zookeeper
   #ä»£çæ¹å¼ï¼jdkãjavassistï¼
   rpc.proxyType=jdk
   #è·¯ç±ç­ç¥ï¼è´è½½åè¡¡ï¼
   rpc.router=random
   #å®¢æ·ç«¯åºååæ¹å¼
   rpc.clientSerialize=fastJson
   ```

10. å¯å¨é¡¹ç®

    é¦åå¯å¨æå¡ç«¯ï¼æå¡æä¾èï¼ï¼åå¯å¨å®¢æ·ç«¯ï¼æå¡æ¶è´¹èï¼ã

11. æµè¯

    æå¼æµè§å¨ï¼è¾å¥`http://localhost:8081/user/send/helloworld`ãæå­ç¬¦ä¸²è¿åå°±è¯´æè¿è¡æåã



## FAQ

**1ã`zookeeper` è¿æ¥å¤±è´¥**

![](https://shaogezhu-images.oss-cn-beijing.aliyuncs.com/my/linkerror.png)

è§£å³æ¹æ³ï¼

ï¼1ï¼å¨æ¬å°æºå¨æèå¨æå¡å¨ä¸å®è£è¿è¡ `zookeeper` å®ä¾ï¼

ï¼2ï¼å¨éç½®æä»¶ä¸­æ­£ç¡®éç½® `zookeeper` å°åï¼



## ç¹å«é¸£è°¢

æè°¢DannyIdeaï¼å°æï¼èå¸ï¼å¨è¿ä¸ªé¡¹ç®ä¸­ç»äºæå¾å¤§å¯ååå¸®å©ðððã

ð **å¦æè§å¾è¿ä¸ªé¡¹ç®å¯¹ä½ æå¸®å©ï¼å¯ç¹å»å³ä¸è§WatchãStaré¡¹ç®ï¼è·åé¡¹ç®ç¬¬ä¸æ¶é´æ´æ°ï¼æ¬¢è¿æäº¤IssuesåPRé¡¹ç®ã**
