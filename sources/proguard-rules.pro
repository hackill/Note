#基本指令
################################################################################################################################################################
-optimizationpasses 5	# 指定代码的压缩级别
-dontusemixedcaseclassnames	# 表示混淆时不使用大小写混合类名
-dontskipnonpubliclibraryclasses	# 表示不跳过library中的非public的类
-dontskipnonpubliclibraryclassmembers	# 指定不去忽略包可见的库类的成员
-dontoptimize	# 表示不进行优化，建议使用此选项，因为根据proguard-android-optimize.txt中的描述，优化可能会造成一些潜在风险，不能保证在所有版本的Dalvik上都正常运行
-dontpreverify  # 表示不进行预校验,这个预校验是作用在Java平台上的，Android平台上不需要这项功能，去掉之后还可以加快混淆速度
-verbose	# 表示打印混淆的详细信息
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*	# 混淆时所采用的算法

-dump dump.txt	# 描述apk内所有class文件的内部结构
-printseeds seeds.txt	# 列出了没有被混淆的类和成员
-printusage unused.txt	# 列出了源代码中被删除在apk中不存在的代码
-printmapping mapping.txt	# 表示混淆前后代码的对照表
################################################################################################################################################################

#公共组件
################################################################################################################################################################
-keep public class * extends android.app.Activity	# 保留继承自Activity类不被混淆
-keep public class * extends android.app.Application	# 保留继承自Application类不被混淆
-keep public class * extends android.support.multidex.MultiDexApplication	# 保留继承自MultiDexApplication类不被混淆
-keep public class * extends android.app.Service	# 保留继承自Service类不被混淆
-keep public class * extends android.content.BroadcastReceiver	# 保留继承自BroadcastReceiver类不被混淆
-keep public class * extends android.content.ContentProvider	# 保留继承自ContentProvider类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper	# 保留继承自BackupAgentHelper类不被混淆
-keep public class * extends android.preference.Preference	# 保留继承自Preference类不被混淆
-keep public class com.google.vending.licensing.ILicensingService	# 保留Google包下ILicensingService类不被混淆
-keep public class com.android.vending.licensing.ILicensingService	# 保留Android包下ILicensingService类不被混淆

-keepattributes *Annotation*,InnerClasses,Signature,SourceFile,LineNumberTable	# 保留相关属性

-keepclasseswithmembernames class * {	# 保持native方法不被混淆
    native <methods>;
}

-keepclassmembers public class * extends android.view.View{	# 保持自定义控件类不被混淆
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclasseswithmembers class * {	# 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {	# 表示不混淆Activity中参数是View的方法，主要针对在xml中配置onClick事件
   public void *(android.view.View);
}

-keepclassmembers enum * {	# 保持枚举类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {	# 保持Parcelable不被混淆
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class * implements java.io.Serializable {	# 保持Serializable不被混淆
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclassmembers class **.R$* {	# 表示不混淆R文件下的静态字段
    public static <fields>;
}

-keepclassmembers class * extends android.webkit.webViewClient {	# 保留WebView
	public void *(android.webkit.webView, jav.lang.String);
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
################################################################################################################################################################

#第三方库
################################################################################################################################################################
#support-v4
-dontwarn android.support.v4.**
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v4.** { *; }
-keep public class * extends android.app.Fragment

#support-v7
-dontwarn android.support.v7.**
-keep class android.support.v7.internal.** { *; }
-keep interface android.support.v7.internal.** { *; }
-keep class android.support.v7.** { *; }

#support design
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

#EventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);    # Only required if you use AsyncExecutor
}
-keepclassmembers class * {	# 保持EventBus中Event事件接收
    void *(**On*Event);
}

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#log4j
-dontwarn org.apache.log4j.**
-keep class  org.apache.log4j.** { *;}

#gson
-dontwarn com.google.gson.**
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }

#支付宝支付
-dontwarn com.alipay.**
-dontwarn com.ta.utdid2.**
-dontwarn com.ut.device.**
-keep class com.alipay.** { *; }
-keep class com.ta.utdid2.** { *; }
-keep class com.ut.device.** { *; }
-keep public class * extends android.os.IInterface

#微信支付
-dontwarn com.tencent.mm.**
-keepattributes Signature
-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}
-keep class com.tencent.wxop.** { *; }
-keep class com.tencent.mm.**{*;}

#ButterKnife
-dontwarn butterknife.internal.**
-keep class butterknife.** { *; }
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
  @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
 @butterknife.* <methods>;
}

#zxing
-dontwarn com.google.zxing.**
-keep class com.google.zxing.** {*;}

#okhttp
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** {*;}
-keep class okio.** { *; }

#retrofit
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes Exceptions

#recyclerview-animators
-dontwarn jp.wasabeef.**
-keep class jp.wasabeef.** {*;}

#multistateview
-dontwarn com.kennyc.view.**
-keep class com.kennyc.view.** { *; }

#ormlite
-dontwarn com.j256.**
-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }

#facebook
-dontwarn com.facebook.**
-keep class com.facebook.**
-keep enum com.facebook.**
-keep public interface com.facebook.**

#umeng
-dontwarn com.umeng.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**
-keep public class com.umeng.socialize.* {*;}

#android-async-http
-dontwarn com.loopj.android.http.**
-keep class com.loopj.android.http.** { *; }

#volley
-dontwarn com.android.volley.**
-keep class com.android.volley.** {*;}
-keep class com.android.volley.toolbox.** {*;}
-keep class com.android.volley.Response$* { *; }
-keep class com.android.volley.Request$* { *; }
-keep class com.android.volley.RequestQueue$* { *; }
-keep class com.android.volley.toolbox.HurlStack$* { *; }
-keep class com.android.volley.toolbox.ImageLoader$* { *; }

#jpush极光推送
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

#人脸识别
-dontwarn com.isnc.facesdk.**
-dontwarn com.matrixcv.androidapi.face.**
-keep class **.R$* {*;}
-keep class com.isnc.facesdk.aty.**{*;}
-keep class com.isnc.facesdk.**{*;}
-keep class com.isnc.facesdk.common.**{*;}
-keep class com.isnc.facesdk.net.**{*;}
-keep class com.isnc.facesdk.view.**{*;}
-keep class com.isnc.facesdk.viewmodel.**{*;}
-keep class com.matrixcv.androidapi.face.**{*;}

#litepal数据库
-dontwarn org.litepal.*
-keep class org.litepal.** { *; }
-keep enum org.litepal.**
-keep interface org.litepal.** { *; }
-keep public class * extends org.litepal.**
-keepattributes *Annotation*
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keepclassmembers class * extends org.litepal.crud.DataSupport{*;}

#xutils
-dontwarn com.lidroid.xutils.**
-keep class com.lidroid.xutils.util.LogUtils {*;}
-keep class com.lidroid.xutils.** { *; }
-keep class * extends java.lang.annotation.Annotation { *; }
-keepclassmembers class **.R$* {
    public static <fields>;
}

#zbar
-dontwarn net.sourceforge.zbar.**
-keep class net.sourceforge.zbar.** { *; }

#xstream
-dontwarn com.thoughtworks.xstream.**
-keep class com.thoughtworks.xstream.io.xml.** { *; }

#百度地图
-dontwarn com.baidu.**
-keep class com.baidu.** {*; }
-keep class assets.** {*; }
-keep class vi.com.gdi.bgl.** {*; }

#百度定位
-dontwarn com.baidu.location.**
-keep class com.baidu.location.** { *; }

#tencent libammsdk 微信
-dontwarn com.tencent.**
-keep class com.tencent.** { *; }

#tencent QQ
-dontwarn com.tencent.**
-keep class com.tencent.** { *; }

#sina
-dontwarn com.sina.weibo.sdk.**
-keep class com.sina.weibo.sdk.** { *; }

#mframework
-dontwarn m.framework.**
-keep class m.framework.** { *; }

#tencent mta统计
-dontwarn com.tencent.stat.**
-keep class com.tencent.stat.** { *; }

#nineoldandroids
-dontwarn com.nineoldandroids.**
-keep class com.nineoldandroids.** { *; }

#universalimageloader
-dontwarn com.nostra13.universalimageloader.**
-keep class com.nostra13.universalimageloader.** { *; }

#sharesdk core
-dontwarn cn.sharesdk.framework.**
-keep class cn.sharesdk.framework.** { *; }

#sharesdk 短信
-dontwarn cn.sharesdk.system.text.**
-keep class cn.sharesdk.system.text.** { *; }

#sharesdk 新浪微博
-dontwarn cn.sharesdk.sina.weibo.**
-keep class cn.sharesdk.sina.weibo.** { *; }

#sharesdk 微信 core
-dontwarn cn.sharesdk.wechat.utils.**
-keep class cn.sharesdk.wechat.utils.** { *; }

#sharesdk 微信 好友
-dontwarn cn.sharesdk.wechat.friends.**
-keep class cn.sharesdk.wechat.friends.** { *; }

#sharesdk 微信 朋友圈
-dontwarn cn.sharesdk.wechat.moments.**
-keep class cn.sharesdk.wechat.moments.** { *; }

#sharesdk 微信 收藏
-dontwarn cn.sharesdk.wechat.favorite.**
-keep class cn.sharesdk.wechat.favorite.** { *; }

#### 高德相关依赖
#集合包:3D地图3.3.2 导航1.8.0 定位2.5.0
-dontwarn com.amap.api.**
-dontwarn com.autonavi.**
-keep class com.amap.api.**{*;}
-keep class com.autonavi.**{*;}

#地图服务 2.4.0
-dontwarn com.amap.api.services.**
-keep class com.map.api.services.** {*;}
#3D地图 2.5.0
-dontwarn com.amap.api.mapcore.**
-dontwarn com.amap.api.maps.**
-dontwarn com.autonavi.amap.mapcore.**
-keep class com.amap.api.mapcore.**{*;}
-keep class com.amap.api.maps.**{*;}
-keep class com.autonavi.amap.mapcore.**{*;}
#定位 1.3.1
-dontwarn com.amap.api.location.**
-dontwarn com.aps.**
-keep class com.amap.api.location.**{*;}
-keep class com.aps.**{*;}
#导航 1.1.2
-dontwarn com.amap.api.navi.**
-dontwarn com.autonavi.**
-keep class com.amap.api.navi.** {*;}
-keep class com.autonavi.** {*;}

## bugly 1.2.8
-dontwarn com.tencent.bugly.**
-keep class com.tencent.bugly.** {*;}

#秋百万的cube-sdk,android-Ultra-Pull-To-Refresh
-dontwarn in.srain.cube.**

#fastjson
-dontwarn com.alibaba.fastjson.**
-keepattributes Signature
-keepattributes *Annotation*

#讯飞语音
-dontwarn com.iflytek.**
-keep class com.iflytek.** {*;}

#ping++支付  未包含百度钱包
-dontwarn com.alipay.**
-keep class com.alipay.** {*;}
-dontwarn  com.ta.utdid2.**
-keep class com.ta.utdid2.** {*;}
-dontwarn  com.ut.device.**
-keep class com.ut.device.** {*;}
-dontwarn  com.tencent.**
-keep class com.tencent.** {*;}
-dontwarn  com.unionpay.**
-keep class com.unionpay.** {*;}
-dontwarn com.pingplusplus.**
-keep class com.pingplusplus.** {*;}

## 友盟自动更新 2.6.0.1
-keepclassmembers class * { public <init>(org.json.JSONObject); }
-keep public class cn.irains.parking.cloud.pub.R$*{ public static final int *; }
-keep public class * extends com.umeng.**
-keep class com.umeng.** { *; }

##友盟统计分析 5.5.3
-keepclassmembers class * { public <init>(org.json.JSONObject); }
-keepclassmembers enum com.umeng.analytics.** {
public static **[] values();
public static ** valueOf(java.lang.String);
}

#信鸽
## jg:1.1
## mid:2.20
## wup:1.0.0-SNAPSHOT, 1.0.0.E-SNAPSHOT
## xg_sdk:2.38_20150405_2046, 2.45_20160510_1845
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep class com.tencent.android.tpush.**  {* ;}
-keep class com.tencent.mid.**  {* ;}
-keepattributes *Annotation*
################################################################################################################################################################

#实体类
################################################################################################################################################################
#-keep class 你的实体类包名.** { *; }
################################################################################################################################################################

#反射相关
################################################################################################################################################################
#-keep class 你的类所在的包.** { *; }
################################################################################################################################################################

#JS调用相关
################################################################################################################################################################
-keepattributes *JavascriptInterface*
-keep class **.Webview2JsInterface { *; }  # 保持WebView对HTML页面的API不被混淆
-keepclassmembers class fqcn.of.javascript.interface.for.webview {	# 保留WebView
   public *;
}
#-keep class 你的类所在的包.** { *; }
#如果是内部类则使用如下方式
#-keepclasseswithmembers class 你的类所在的包.父类$子类 { <methods>; }
################################################################################################################################################################