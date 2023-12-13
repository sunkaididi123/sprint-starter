# 开发者SDK文档

如果你需要使用我们的 API ，请你联系作者取得 jar，在你的项目中引入 jar 包，然后放入你的 maven 仓库里面。



#### 引入依赖

```xml
<dependency>
    <groupId>com.xwhking</groupId>
    <artifactId>yuapi-starter</artifactId>
    <version>0.0.1</version>
</dependency>
```



#### 配置信息

你可以使用在 springboot 项目中的配置文件配置用户信息 accessKey， secretKey，userId

配置案例：

```yaml
com:
  xwhking:
    client:
      access-key: XXX
      secret-key: XXX
      user-id: XXX
```

配置好信息以后就可以通过自动注入来使用哦

```java
@Resource
private XWHKINGClient xwhkingClient;
```



如果不想这样配置，想直接生成一个 client 的话如下：

```java
XWHKINGClient xwhkingClient = new ClientConfig().getCient(accessKey,secretKey,userId)
```



#### 使用

我们的项目暂时提供 4 个接口

##### 获取一句话

通过输入一个必须的参数 `type` 就可以获得一言的一句话

```java
xwhkingClient.invokeGetOneSentence("netEaseCloud")
```

参数选择：

系统有13个枚举值，你只能输入后面的英文，如果输入其他就会请求错误。

```java
Origin("原创","origin"),
Others("其他","others"),
Carton("动画","carton"),
Philosophy("哲学","philosophy"),
Movie("影视","movie"),
Clever("抖机灵","clever"),
Literary("文学","literary"),
Game("游戏","game"),
Comic("漫画","comic"),
NetEaseCloud("网易云","netEaseCloud"),
Internet("网络","internet"),
Poem("诗词","poem"),
CheckSoup("鸡汤","checkSoup");
```



响应结果

```json
{
    "code":0,
    "data":{
        "id":"5210",
        "content":"“浪漫”源于骑士文学，形容中世纪骑士阶级和贵妇人之间注定无法有结果的爱情；“Romance”的本意就是知其不可而为之,是明知不会有结果却无法停止爱你。",
        "type":"网易云",
        "sentenceFrom":"网易云",
        "fromWho":"此账号已注销",
        "createTime":"2023-08-18T13:19:27.000+00:00",
        "updateTime":"2023-08-18T13:19:27.000+00:00",
        "isDelete":0
           },
    "message":"ok"
}
```



##### 获取相应内容的二维码的base64编码

通过一个必须的参数`content`,content 就是你要生成二维码的内容

```java
xwhkingClient.invokeGetQrCode("Hello World!")   
```

生成的结果

```json
{
    "code":0,
    "data":"data:image/jpeg;base64,/9j/...中间已删除大部分内容.../9k=",
    "message":"ok"
}
```





##### 获取搞笑表情的url

通过一个必须的参数 `keyword` 传递表情包的关键信息

```java
xwhkingClient.invokeGetExpression("帅哥")
```

生成的结果

```json
{
    "code":0,
    "data":["https://p3-heycan-dy-sign.byteimg.com/tos-cn-i-3jr8j4ixpe/a87add6b1524430285952f1b1b76f04a~tplv-3jr8j4ixpe-resize:450:450.jpeg?lk3s=43402efa&x-expires=1733352468&x-signature=SjVFtdpcFkRHZAApz67NysVbuqs%3D&format=.jpeg","https://p9-heycan-dy-sign.byteimg.com/tos-cn-i-3jr8j4ixpe/64b19c9eac354d4db3fe134d049c34e0~tplv-3jr8j4ixpe-resize:450:450.jpeg?lk3s=43402efa&x-expires=1733352483&x-signature=IJmy5QBXfFwuHosKDJ6U3uE%2FFFM%3D&format=.jpeg","https://p3-heycan-dy-sign.byteimg.com/tos-cn-i-3jr8j4ixpe/a656f44343c342efa2827381a3af91bf~tplv-3jr8j4ixpe-resize:450:450.jpeg?lk3s=43402efa&x-expires=1733352456&x-signature=8cASlUxKR4Rri4NNyXBWkbgpQ48%3D&format=.jpeg"],
	 "message":"ok"
}

```



##### 获取今日分享

```java
xwhkingClient.invokeDaily()
```



响应结果

```json
{
    "code":0,
    "data":{
        "content":"One always has enough time, if one will apply it well.",
        "note":"人总是有足够的时间，如果他好好利用。",
        "dateline":"2023-12-06",
        "fenxiang_img":"https://staticedu-wps.cache.iciba.com/image/1e7171a9443e5bcc940ab4e683a40264.png"
    },
    "message":"ok"
}
```



