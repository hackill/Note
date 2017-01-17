## 前言
基于RxJava+Retrofit精心打造的Android基础框架，包含网络、下载、缓存、事件总线、数据库、图片加载、UI模块，基本都是项目中必用功能，每个模块充分解耦，可随意替换。
XSnow，X：未知一切，取其通用之意；Snow：雪，取其纯净之意。该框架通用纯净，只依赖公共核心库，每个模块都可单独作为一个库，如只需要网络相关，那么只需拷贝common和net包就行。

- 项目地址：[https://github.com/xiaoyaoyou1212/XSnow](https://github.com/xiaoyaoyou1212/XSnow)

- 项目依赖：`compile project('com.vise.xiaoyaoyou:xsnow:1.0.0')`

## 网络
网络算是项目的灵魂，基本每个项目都离不开网络，而一个简单好用，又支持各种配置的网络访问库就显得尤为重要了。该模块也是XSnow的核心功能，使用简单，支持定制常用配置，如各种拦截器、缓存策略、请求头等。上层项目基于RxJava+Retrofit请求网络时需要每个接口都写一个服务接口，这样非常不便利，如何将响应结果通用处理就成为该模块的重点，后面采用泛型转换方式，将响应结果ResponseBody通过map操作符转换成需要的T，具体实现参考项目中net包下的func包，如果需要Http响应码，也可以将响应结果包装成Response<ResponseBody>这样进行转换成T，考虑到项目中很少需要Http响应码来进行判定，一般使用服务器自定义的响应码就可以了，故该模块采用ResponseBody统一接收这种处理方式。

### 主要功能：

- 支持OKHttp本身的HTTP缓存，也支持外部自定义的在线离线缓存，可配置常用缓存策略，如优先获取缓存策略等。

- 支持请求与响应统一处理，不需要上层每个模块都定义ApiService接口。

- 支持泛型T接收处理响应数据，也可根据服务器返回的统一数据模式定制如包含Code、Data、Message的通用Model ApiResult<T>。

- 支持异常统一处理，定制了ApiException拦截处理，可根据服务器各种响应码定制异常提示。

- 支持不需订阅者的回调数据处理。

- 支持自定义请求头。

## 下载
基本来源于此项目[https://github.com/ssseasonnn/RxDownload](https://github.com/ssseasonnn/RxDownload)，这里我就不赘述了，具体使用详情可参考该项目地址，RxDownload算是基于RxJava+Retrofit写得比较优秀的下载框架了。

## 缓存
包含内存、磁盘二级缓存以及SharedPreferences缓存，可自由拓展。

### 主要功能：

- 磁盘缓存支持KEY加密存储，可定制缓存时长。

- SharedPreferences支持内容安全存储，采用Base64加密解密。

## 事件总线
采用Rx响应式编程思想建立的RxBus模块，采用注解方式标识事件消耗地，通过遍历查找事件处理方法。

### 主要功能：

- 支持可插拔，可替换成EventBus库，只需上层采用的同样是注解方式，那么上层是不需要动任何代码的。

## 数据库
采用greenDao数据库，其优势就不多说了，网上有解释，其主要优点就是性能高。该模块定制数据库操作接口，有统一的实现类DBManager，上层只需实现getAbstractDao()方法告知底层DaoSession，增删改查操作不需要关心具体细节，调用DBManager中的方法就行。

## 图片加载
采用Glide库进行图片加载，支持轻量级图片加载，该模块支持可插拔，可根据需求替换成任意图片加载库，如果项目中对于图片处理要求比较高，那么可以替换成Facebook提供的Fresco库。

## UI
包含BaseActivity、BaseFragment以及万能适配器，适配器可满足所有基于BaseAdapter的适配器组装，使用方便，易拓展。

*注：该框架引用了日志系统和公共工具库，这两个库都很轻量级，具体使用详情可分别参考[https://github.com/xiaoyaoyou1212/ViseLog](https://github.com/xiaoyaoyou1212/ViseLog)和[https://github.com/xiaoyaoyou1212/ViseUtils](https://github.com/xiaoyaoyou1212/ViseUtils)。*