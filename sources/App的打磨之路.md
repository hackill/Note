---
title: App的打磨之路
date: 2016-06-25 10:24:51
tags: [Android,随笔]
---
#### 前言：
俗话说磨刀不误砍柴工，一个优秀的产品从一个不错的点子直到用户的手中，是需要一个团队不遗余力协同合作不断打磨出来的；同样，一个好的App除正常的代码编写外，还需要经过其他方面的不断打磨才能正式交互，最终到达用户的手中。该文主要讲述一个应用除开发外还需要进行哪些工作才能合格交互，在此抛砖引玉，希望对有需要的朋友一点启示！
#### 一、单元测试
单元测试是编写测试代码，用来检测特定的、明确的、细颗粒的功能。单元测试不仅仅用来保证当前代码的正确性，更重要的是用来保证代码修复、改进或重构之后的正确性。对于想打造优秀产品的码农来说是必不可少的，虽然在大部分公司实现有居多困难。
一般来说，单元测试任务主要包括以下几个方面：
1. 接口功能测试
主要用来保证接口功能的正确性。
2. 数据结构测试
主要用来保证接口中的数据结构的正确性，比如变量有无初始值，是否溢出等。
3. 边界条件测试
边界条件判定是单元测试中最常用的，在开发中也是最容易遇到的BUG，边界条件判定的类型主要有以下几种情形：
    - 变量是对象：如对象是否为NULL等；
    - 变量是数值：数值边界：最小值、最大值、无穷小、无穷大，溢出边界：最小值-1、最大值+1，临近边界：最小值+1、最大值-1；
    - 变量是字符串：字符串是否为空，字符串的长度进行数值变量的判定；
    - 变量是集合：集合是否为空，集合的大小进行数值变量的判定；
4. 独立执行通路测试
主要保证代码的覆盖率，如语句覆盖：保证每一条语句都执行到，分支覆盖：保证每一个分支都执行到，条件覆盖：保证每一个条件都覆盖到true和false的情形，路径覆盖：保证每一个路径都执行到；
5. 异常处理通路测试
主要保证所有的异常都经过测试。

JUnit是Java单元测试框架，已经在Android Studio中默认依赖。目前主流的有JUnit3和JUnit4。JUnit3中，测试用例需要继承TestCase类。JUnit4中，测试用例无需继承TestCase类，只需要使用@Test等注解。以下通过一个实例来更好的展示单元测试过程：
先在应用下建立一个计算工具类，方便写单元测试：
```
package com.vise.note.util;

/**
 * 计算相关工具类
 * Created by xyy on 16/6/25.
 */
public class CalculatorUtil {
    public double plus(double a, double b){
        return a + b;
    }

    public double minus(double a, double b){
        return a - b;
    }

    public double multiply(double a, double b){
        return a * b;
    }

    public double divide(double a, double b) throws Exception {
        if(b == 0){
            throw new Exception("除数不能为零！");
        }
        return a / b;
    }

}

```
Android Studio提供了一个快速创建测试类的方法，只需在编辑器内右键点击CalculatorTest类的声明，选择Go to > Test，然后点击"Create a new test…"，到此会弹出两个选项，一个是androidTest，一个是test目录下，由于该测试不需要用到模拟器，可以运行在本地电脑Java虚拟机上，所以此处选择test目录下，随后在test与应用同包的目录下生成CalculatorUtilTest.java文件，内容如下（方法内部实现是手动添加的）：
```
package com.vise.note.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by xyy on 16/6/25.
 */
public class CalculatorUtilTest {

    private CalculatorUtil calculatorUtil;

    @Before
    public void setUp() throws Exception {
        calculatorUtil = new CalculatorUtil();
    }

    @After
    public void tearDown() throws Exception {
        calculatorUtil = null;
    }

    @Test
    public void testPlus() throws Exception {
        assertEquals(6d, calculatorUtil.plus(1d, 5d), 0);
    }

    @Test
    public void testMinus() throws Exception {
        assertEquals(-4d, calculatorUtil.minus(1d, 5d), 0);
    }

    @Test
    public void testMultiply() throws Exception {
        assertEquals(5d, calculatorUtil.multiply(1d, 5d), 0);
    }

    @Test
    public void testDivide() throws Exception {
        assertEquals(0.2d, calculatorUtil.divide(1d, 5d), 0);
    }
}
```
最后就可以直接选择CalculatorUtilTest直接运行了。到此，单元测试就告一段落了，下面是讲述性能分析，这个也很重要哦！^-^
#### 二、性能分析
##### 1、Memory Monitor
在Android Studio中运行项目后，点击Android Monitor中的Monitor就可以看到如下图所示的Memory使用及CPU运行情况：
![这里写图片描述](http://img.blog.csdn.net/20160717204116339)
下面还可以查看GPU和Network的相关情况，其中NetWork的频繁使用是造成应用耗电的关键，70%左右的电量是被上报数据，检查位置信息，定时检索后台广告信息所使用掉的，如何平衡之间的使用也是很重要的。
##### 2、Heap Snapshot
依据上面Memory Monitor描述，找到Memory中第三个图标“Dump Java Heap”，每次点击之后会生成一个.hprof的文件，点击一个.hprof文件，查看右侧的Analyzer Tasks,能看到两个选项，一个是‘Detect Leaeked Activites’，另一个是'Find Duplicate Strings'，点击右上角的绿色播放按钮，会自动分析heap dump去定位泄露的activity和重复的string，出现如下的Analysis Results：
![这里写图片描述](http://img.blog.csdn.net/20160717203349141)
![这里写图片描述](http://img.blog.csdn.net/20160717203423173)
从上面两幅图中可以看出，第一个选项表示查看的信息可以有三种类型:App heap/Image heap/Zygote heap.分别代表App堆内存信息，图片堆内存信息，Zygote进程的堆内存信息。还有一个选项可以选择Class List View和Package Tree View两种视图展示方式。

各属性中英文对照表

|名称|意义|
|---|---|
|Total Count|内存中该类的对象个数|
|Heap Count|堆内存中该类的对象个数|
|Sizeof|物理大小|
|Depth|深度|
|Shallow size|对象本身占有内存大小|
|Retained Size|释放该对象后，节省的内存大小|
|Dominating Size|管辖的内存大小|

##### 3、LeakCanary
- 使用方法
在 build.gradle 中加入引用，不同的编译使用不同的引用：
```
dependencies {
   debugCompile 'com.squareup.leakcanary:leakcanary-android:1.3'
   releaseCompile 'com.squareup.leakcanary:leakcanary-android-no-op:1.3'
}
```
在 Application 中：
```
public class ExampleApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
    LeakCanary.install(this);
  }
}
```
应用运行起来后，LeakCanary会自动去分析当前的内存状态，如果检测到泄漏会发送到通知栏，点击通知栏就可以跳转到具体的泄漏分析页面。
- 工作机制
1、RefWatcher.watch() 创建一个 KeyedWeakReference 到要被监控的对象。
2、然后在后台线程检查引用是否被清除，如果没有，调用GC。
3、如果引用还是未被清除，把 heap 内存 dump 到 APP 对应的文件系统中的一个 .hprof 文件中。
4、在另外一个进程中的 HeapAnalyzerService 有一个 HeapAnalyzer 使用HAHA 解析这个文件。
5、得益于唯一的 reference key, HeapAnalyzer 找到 KeyedWeakReference，定位内存泄露。
6、HeapAnalyzer 计算 到 GC roots 的最短强引用路径，并确定是否是泄露。如果是的话，建立导致泄露的引用链。
7、引用链传递到 APP 进程中的 DisplayLeakService， 并以通知的形式展示出来。
更多关于LeakCanary的使用介绍请参考：[LeakCanary 中文使用说明](http://www.liaohuqiu.net/cn/posts/leak-canary-read-me/)

注：以上都是针对Android Studio IDE的性能分析方式。

#### 三、签名
签名的前提得有签名文件，生成签名文件的方式大同小异，IDE基本都有这个功能，这里以Android Studio为列讲述生成签名文件的过程。选择工具栏Build->Generate Signed APK,打开后选择对应的module，点击next，如图所示：
![这里写图片描述](http://img.blog.csdn.net/20160717204325154)
点击Create new，进入如下界面：
![这里写图片描述](http://img.blog.csdn.net/20160717204342468)
```
信息注释
Key store path:签名文件路径
Password:签名库密码
Confirm:确认签名库密码
Alias:别名
Password:该别名下签名密码
Confirm:确认该别名下签名密码
Validity:认证年限
First and Last Name:你的全名
Organizational Unit:组织单位
Organization:组织
City or Locality:城市或地区
State or Province:川或省
Country Code:国家代码
```
按照指示填写对应信息，点击OK就生成了签名文件。
还一种方式是使用命令的方式创建，进入Java的bin目录下，如我的Java目录为：/Library/Java/Home/bin，通过keytool工具来创建keystore库，输入以下命令：
```
keytool -genkeypair -alias - xyy.keystore -keyalg RSA -validity 100 -keystore xyy.keystore
```
```
命令说明如下：
-genkeypair：指定生成数字证实
-alias：指定生成数字证书的别名
-keyalg：指定生成数字证书的算法  这里如RSA算法
-validity：指定生成数字证书的有效期
-keystore ：指定生成数字证书的存储路径（这里默认在keytool目录下）
```
再按照提示一步步输入对应的信息，最后就生成了一个名为xyy.keystore的签名文件。
有了签名文件后，将签名文件放到对应需要签名的工程目录module下，再在module对应的build文件中添加如下签名信息(签名信息对应输入自己设置的秘钥信息)：
```
android{
	...
	signingConfigs {
        debug {
			storeFile file("xyy.keystore")
            storePassword "xyy"
            keyAlias "Note"
            keyPassword "xyy"
        }

        release {
            storeFile file("xyy.keystore")
            storePassword "xyy"
            keyAlias "Note"
            keyPassword "xyy"
        }
    }
    
    buildTypes {
        debug {
            minifyEnabled false
            zipAlignEnabled true
            shrinkResources false
            signingConfig signingConfigs.debug
        }

        release {
            //是否混淆（注：如果混淆文件未配置使用false）
            minifyEnabled false
            //是否支持Zip Align
            zipAlignEnabled true
            //是否清理无用资源
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
            //使用签名配置
            signingConfig signingConfigs.release

        }
    }
    ...
}
```
这样配置完后每次build工程生成的文件都会使用debug下的签名信息了，gradle配置详情可参考我的另一篇博客[Android Studio常用Gradle操作](http://blog.csdn.net/xiaoyaoyou1212/article/details/50277555)。
#### 四、混淆
##### 1、混淆原理
Java 是一种跨平台的、解释型语言，Java 源代码编译成中间”字节码”存储于 class 文件中。由于跨平台的需要，Java 字节码中包括了很多源代码信息，如变量名、方法名，并且通过这些名称来访问变量和方法，这些符号带有许多语义信息，很容易被反编译成 Java 源代码。为了防止这种现象，我们可以使用 Java 混淆器对 Java 字节码进行混淆。
混淆就是对发布出去的程序进行重新组织和处理，使得处理后的代码与处理前代码完成相同的功能，而混淆后的代码很难被反编译，即使反编译成功也很难得出程序的真正语义。被混淆过的程序代码，仍然遵照原来的档案格式和指令集，执行结果也与混淆前一样，只是混淆器将代码中的所有变量、函数、类的名称变为简短的英文字母代号，在缺乏相应的函数名和程序注释的况下，即使被反编译，也将难以阅读。同时混淆是不可逆的，在混淆的过程中一些不影响正常运行的信息将永久丢失，这些信息的丢失使程序变得更加难以理解。
##### 2、混淆语法
```
-include {filename}    从给定的文件中读取配置参数
-basedirectory {directoryname}    指定基础目录为以后相对的档案名称
-injars {class_path}    指定要处理的应用程序jar,war,ear和目录
-outjars {class_path}    指定处理完后要输出的jar,war,ear和目录的名称
-libraryjars {classpath}    指定要处理的应用程序jar,war,ear和目录所需要的程序库文件
-dontskipnonpubliclibraryclasses    指定不去忽略非公共的库类
-dontskipnonpubliclibraryclassmembers    指定不去忽略包可见的库类的成员

保留选项
-keep {Modifier} {class_specification}    保护指定的类文件和类的成员
-keepnames {class_specification}    保护指定的类和类的成员的名称（如果他们不会在压缩步骤中删除）
-keepclassmembers {modifier} {class_specification}    保护指定类的成员，如果此类受到保护他们会保护的更好
-keepclassmembernames {class_specification}    保护指定的类的成员的名称（如果他们不会在压缩步骤中删除）
-keepclasseswithmembers {class_specification}    保护指定的类和类的成员，但条件是所有指定的类和类成员是要存在
-keepclasseswithmembernames {class_specification}    保护指定的类和类的成员的名称，如果所有指定的类成员出席（在压缩步骤之后）
-printseeds {filename}    列出类和类的成员-keep选项的清单，标准输出到给定的文件
-keepattributes {attribute_name,...}    保护给定的可选属性，例如LineNumberTable, LocalVariableTable, SourceFile, Deprecated, Synthetic, Signature, and InnerClasses.

压缩相关
-dontshrink    不压缩输入的类文件
-printusage {filename}
-dontwarn   如果有警告也不终止
-whyareyoukeeping {class_specification}

优化相关
-dontoptimize    不优化输入的类文件
-assumenosideeffects {class_specification}    优化时假设指定的方法，没有任何副作用
-allowaccessmodification    优化时允许访问并修改有修饰符的类和类的成员

混淆相关
-dontobfuscate    不混淆输入的类文件
-printmapping {filename}
-applymapping {filename}    重用映射增加混淆
-obfuscationdictionary {filename}    使用给定文件中的关键字作为要混淆方法的名称
-overloadaggressively    混淆时应用侵入式重载
-useuniqueclassmembernames    确定统一的混淆类的成员名称来增加混淆
-flattenpackagehierarchy {package_name}    重新包装所有重命名的包并放在给定的单一包中
-repackageclass {package_name}    重新包装所有重命名的类文件中放在给定的单一包中
-dontusemixedcaseclassnames    混淆时不会产生形形色色的类名
-renamesourcefileattribute {string}    设置源文件中给定的字符串常量
```
|关键字|描述|
|--------|--------|
|keep|保留类和类中的成员，防止它们被混淆或移除。|
|keepnames|保留类和类中的成员，防止它们被混淆，但当成员没有被引用时会被移除。|
|keepclassmembers|只保留类中的成员，防止它们被混淆或移除。|
|keepclassmembernames|只保留类中的成员，防止它们被混淆，但当成员没有被引用时会被移除。|
|keepclasseswithmembers|保留类和类中的成员，防止它们被混淆或移除，前提是指名的类中的成员必须存在，如果不存在则还是会混淆。|
|keepclasseswithmembernames|保留类和类中的成员，防止它们被混淆，但当成员没有被引用时会被移除，前提是指名的类中的成员必须存在，如果不存在则还是会混淆。|

|通配符|描述|
|--------|--------|
|&lt;field&gt;|匹配类中的所有字段|
|&lt;method&gt;|匹配类中的所有方法|
|&lt;init&gt;|匹配类中的所有构造函数|
|`*`|匹配任意长度字符，但不含包名分隔符(.)。比如说我们的完整类名是`com.vise.note.MainActivity`，使用`com.*`，或者`com.vise.*`都是无法匹配的，因为`*`无法匹配包名中的分隔符，正确的匹配方式是`com.vise.*.*`，或者`com.vise.note.*`，这些都是可以的。但如果你不写任何其它内容，只有一个`*`，那就表示匹配所有的东西。|
|`**`|匹配任意长度字符，并且包含包名分隔符`(.)`。比如`proguard-android.txt`中使用的`-dontwarn android.support.**`就可以匹配`android.support`包下的所有内容，包括任意长度的子包。|
|`***`|匹配任意参数类型。比如`void set*(***)`就能匹配任意传入的参数类型，`*** get*()`就能匹配任意返回值的类型。|
|`…`|匹配任意长度的任意类型参数。比如`void test(…)`就能匹配任意`void test(String a)`或者是`void test(int a, String b)`这些方法。|
##### 3、混淆文件编写
混淆是apk上线前挺重要的一个环节,Android使用的是ProGuard，可以起到压缩，混淆，预检，优化的作用。纵观大部分项目的混淆文件，其大部分内容都是固定的，从中可以整理出一个通用的模板，模板内容大致分为以下几个部分：基本指令、公共组件、第三方库、实体类、反射相关及JS调用相关。其中前两部分基本不会有太大变化，第三方库网上基本都会提供混淆方式，下文也会依据网上资源整理出大部分的三方库保留方式，而后面几个部分就与具体项目相关了，掌握思路后依照具体项目定制就行。

3.1、基本指令
```
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
```

3.2、公共组件
```
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
```

3.3、第三方库
```
#EventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
-keepclassmembers class * {	# 保持EventBus中Event事件接收
    void *(**On*Event);
}
#注：此处以EventBus为例，描述第三方库的保留方式，更多的第三方库保留方式已提交到github上。
```
3.4、实体类
```
-keep class 你的实体类包名.** { *; }
```

3.5、反射相关
```
-keep class 你的类所在的包.** { *; }
```

3.6、JS调用相关
```
-keepattributes *JavascriptInterface*
-keep class **.Webview2JsInterface { *; }  # 保持WebView对HTML页面的API不被混淆
-keepclassmembers class fqcn.of.javascript.interface.for.webview {	# 保留WebView
   public *;
}
-keep class 你的类所在的包.** { *; }
#如果是内部类则使用如下方式
-keepclasseswithmembers class 你的类所在的包.父类$子类 { <methods>; }
```
到此混淆相关介绍就已完成，完整的混淆文件已提交到github上，路径如下：[https://github.com/xiaoyaoyou1212/Note/blob/master/app/proguard-rules.pro](https://github.com/xiaoyaoyou1212/Note/blob/master/app/proguard-rules.pro).其中包含了大部分第三方库的混淆方式。

注：混淆后生成的相关信息文件在当前module的build/outputs/mapping下，目录下可能会有渠道名，下面再分为debug和release，与你的配置相关，其中的mapping.txt文件需要重点关注，该文件表示混淆前后代码的对照表，这个文件非常重要,如果你的代码混淆后会产生bug的话，log提示中是混淆后的代码，希望定位到源代码的话就可以根据mapping.txt反推,每次发布都要保留它方便该版本出现问题时调出日志进行排查，它可以根据版本号或是发布时间命名来保存或是放进代码版本控制中。

#### 五、打包

#### 六、反编译

#### 七、加固

#### 八、APK瘦身
##### 1、APK文件结构
Android应用是用Java编写的，利用Android SDK编译代码，并且把所有的数据和资源文件打包成一个APK (Android Package）文件，这是一个后缀名为.apk的压缩文件，APK文件中包含了一个Android应用程序的所有内容，是Android平台用于安装应用程序的文件。APK就是一个zip压缩包，解开这个APK包我们可以看到以下的结构：

|目录|描述|
|-----|-----|
|assets目录|存放需要打包到apk中的静态文件|
|lib目录|程序依赖的native库|
|res目录|存放应用程序的资源|
|META-INF目录|存放应用程序签名和证书的目录|
|AndroidManifest.xml|应用程序的配置文件|
|classes.dex|dex可执行文件|
|resources.arsc|资源配置文件|

- assets目录
用于存放需要打包到APK中的静态文件，和res的不同点在于，assets目录支持任意深度的子目录，用户可以根据自己的需求任意部署文件夹架构，而且res目录下的文件会在R文件中生成对应的资源ID，assets不会自动生成对应的ID，访问的时候需要AssetManager类。
- lib目录
这里存放应用程序依赖的native库文件，一般是用C/C++编写，这里的lib库可能包含4中不同类型，根据CPU型号的不同，大体可以分为ARM、ARM-v7a、MIPS、X86，分别对应着ARM架构，ARM-V7架构，MIPS架构和X86架构，而每个架构又分32位和64位；其中，不同的CPU架构对应着不同的目录，每个目录中可以放很多对应版本的so库，且这个目录的结构固定，用户只能按照这个目录存放自己的so库。目前市场上使用的移动终端大多是基于ARM或者ARM-V7a架构的，X86和MIPS架构的移动智能终端比较少，所以有些应用程序lib目录下只包含armeabi目录或者armeabi-v7a目录，也就是说，这四个目录要根据CPU的架构来选，而市面上ARM架构的手机占大多数，所以一般的APK只包含ARM和ARM-V7a的so。
- res目录
res是resource的缩写，这个目录存放资源文件，存在这个文件夹下的所有文件都会映射到Android工程的R文件中，生成对应的ID，访问的时候直接使用资源ID即R.id.filename，res文件夹下可以包含多个文件夹，其中anim存放动画文件；drawable目录存放图像资源；layout目录存放布局文件；values目录存放一些特征值，colors.xml存放color颜色值，dimens.xml定义尺寸值，string.xml定义字符串的值，styles.xml定义样式对象；xml文件夹存放任意xml文件，在运行时可以通过Resources.getXML()读取；raw是可以直接复制到设备中的任意文件，无需编译。
- META-INF目录
保存应用的签名信息，签名信息可以验证APK文件的完整性。AndroidSDK在打包APK时会计算APK包中所有文件的完整性，并且把这些完整性保存到META-INF文件夹下，应用程序在安装的时候首先会根据META-INF文件夹校验APK的完整性，这样就可以保证APK中的每一个文件都不能被篡改。以此来确保APK应用程序不被恶意修改或者病毒感染，有利于确保Android应用的完整性和系统的安全性。META-INF目录下包含的文件有CERT.RSA，CERT.DSA，CERT.SF和MANIFEST.MF，其中CERT.RSA是开发者利用私钥对APK进行签名的签名文件，CERT.SF，MANIFEST.MF记录了文件中文件的SHA-1哈希值。
- AndroidManifest.xml
是Android应用程序的配置文件，是一个用来描述Android应用“整体资讯”的设定文件，简单来说，相当于Android应用向Android系统“自我介绍”的配置文件，Android系统可以根据这个“自我介绍”完整地了解APK应用程序的资讯，每个Android应用程序都必须包含一个AndroidManifest.xml文件，且它的名字是固定的，不能修改。我们在开发Android应用程序的时候，一般都把代码中的每一个Activity，Service，ContentProvider和BroadcastReceiver在AndroidManifest.xml中注册，只有这样系统才能启动对应的组件，另外这个文件还包含一些权限声明以及使用的SDK版本信息等等。程序打包时，会把AndroidManifest.xml进行简单的编译，便于Android系统识别，编译之后的格式是AXML格式。
- classes.dex
传统的Java程序，首先先把Java文件编译成class文件，字节码都保存在了class文件中，Java虚拟机可以通过解释执行这些class文件。而Dalvik虚拟机是在Java虚拟机进行了优化，执行的是Dalvik字节码，而这些Dalvik字节码是由Java字节码转换而来，一般情况下，Android应用在打包时通过AndroidSDK中的dx工具将Java字节码转换为Dalvik字节码。dx工具可以对多个class文件进行合并，重组，优化，可以达到减小体积，缩短运行时间的目的。
- resources.arsc
用来记录资源文件和资源ID之间的映射关系，用来根据资源ID寻找资源。Android的开发是分模块的，res目录专门用来存放资源文件，当在代码中需要调用资源文件时，只需要调用findviewbyId()就可以得到资源文件，每当在res文件夹下放一个文件，aapt就会自动生成对应的ID保存在R文件中，我们调用这个ID就可以，但是只有这个ID还不够，R文件只是保证编译程序不报错，实际上在程序运行时，系统要根据ID去寻找对应的资源路径，而resources.arsc文件就是用来记录这些ID和资源文件位置对应关系的文件。

从以上目录结构中可以看出，如果需要缩小apk的大小，主要针对的是assets目录、lib目录、res目录及classes.dex文件。其中的assets目录主要是静态加载到apk中的文件，这个可以根据是否需要来进行手动管理，没什么优化的空间，下文主要针对classes.dex文件、res目录及lib目录来进行优化讲解。

##### 2、Apk瘦身方式
2.1、Proguard混淆优化代码
Proguard是一个很强悍的工具，它可以帮你在代码编译时对代码进行混淆，优化和压缩。它有一个专门用来减少apk文件大小的功能叫做tree-shaking。Proguard 会遍历你的所有代码然后找出无用处的代码。所有这些不可达（或者不需要）的代码都会在生成最终的apk文件之前被清除掉。Proguard 也会重命名你的类属性，类和接口，然整个代码尽可能地保持轻量级水平。

2.2、Lint检查优化资源
混淆只能优化java代码，不能对无用资源进行清理，而Lint则可以检查所有无用的资源文件，只要使用命令`./gradlew lint`或者在Android Studio工程中点击Analyze->Inspect Code,选择Whole Project点击ok就行。它在检测完之后会提供一份详细的资源文件清单，并将无用的资源列在“UnusedResources: Unused resources” 区域之下。只要你不通过反射来反问这些无用资源，你就可以放心地移除这些文件了。

2.3、压缩图片
图片资源的优化原则是：在不降低图片效果、保证APK显示效果的前提下缩小图片文件的大小。
- AAPT：Aapt(Android Asset Packaging Tool)就内置了保真图像压缩算法。例如，一个只需256色的真彩PNG图片会被aapt通过一个颜色调色板转化成一个8-bit PNG文件，这可以帮助你减少图片文件的大小。
- 使用tinypng优化大部分图片资源：tinypng是一个支持压缩png和jpg图片格式的网站，通过其独特的算法（通过一种叫“量化”的技术，把原本png文件的24位真彩色压缩为8位的索引演示，是一种矢量压缩方法，把颜色值用数值123等代替。）可以实现在无损压缩的情况下图片文件大小缩小到原来的30%-50%。
tinypng的缺点是在压缩某些带有过渡效果（带alpha值）的图片时，图片会失真，这种图片可以将png图片转换为下面介绍的webP格式，可以在保证图片质量的前提下大幅缩小图片的大小。
- 使用webP图片格式：WebP是谷歌研发出来的一种图片数据格式，它是一种支持有损压缩和无损压缩的图片文件格式，派生自图像编码格式 VP8。根据 Google 的测试，无损压缩后的 WebP 比 PNG 文件少了 45％ 的文件大小，即使这些 PNG 文件经过其他压缩工具压缩之后，WebP 还是可以减少 28％ 的文件大小。目前很多公司已经将webP技术运用到Android APP中，比如FaceBook、腾讯、淘宝。webP相比于png最明显的问题是加载稍慢，不过现在的智能设备硬件配置越来越高，这都不是事儿。
假如你打算在 App 中使用 WebP，除了 Android4.0 以上提供的原生支持外，其他版本以可以使用官方提供的解析库webp-android-backport编译成so使用。
通常UI提供的图片都是png或者jpg格式，我们可以通过智图或者isparta将其它格式的图片转换成webP格式，isparta可实现批量转换，墙裂推荐！
- 使用tintcolor实现按钮反选效果：通常按钮的正反旋图片我们都是通过提供一张按钮正常图片和一张按钮反选图片，然后通过selector实现，两张图片除了alpha值不一样外其它的内容都是重复的，在Android 5.0及以上的版本可以通过tintcolor实现只提供一张按钮的图片，在程序中实现按钮反选效果，前提是图片的内容一样，只是正反选按钮的颜色不一样。

2.4、限制支持CPU架构
一般说来Android使用Java代码即可以满足大部分需求，不过还是有一小部分案例需要使用一些native code。CPU的架构主要分为以下几种：ARM架构，ARM-V7架构，MIPS架构和X86架构，目前市场上使用的移动终端大多是基于ARM或者ARM-V7a架构的，X86和MIPS架构的移动智能终端比较少，所以有些应用程序lib目录下只包含armeabi目录或者armeabi-v7a目录，也就是说lib目录要根据CPU的架构来选，而市面上ARM架构的手机占大多数，所以一般的APK只包含ARM和ARM-V7a的so。

2.5、其他优化技巧
- 对资源文件进行取舍
Android支持多种设备。Android的系统设计让它可以支持设备的多样性：屏幕密度，屏幕形状，屏幕大小等等。到了Android4.4，它支持的屏幕密度包括：ldpi,mdpi,hdpi,xhdpi,xxhdpi以及xxxhdpi，但Android支持这么多的屏幕密度并不意味着需要为每一个屏幕密度提供相应的资源文件。我们可以选择目前主流手机的分辨率对应的xhdpi和xxhdpi，如果某些设备不是这几个屏幕密度的，不用担心，Android系统会自动使用存在的资源为设备计算然后提供相近大小的资源文件。
- 资源文件最少化配置
Android开发经常会依赖各种外部开源代码库，但是这些库里面并不是所有的资源文件你都会用到。从Android Gradle Plugin 0.7 开始，你可以配置你的app的build系统。这主要是通过配置resConfig和resConfigs以及默认的配置选项。下面的DSL(Domain Specific Language)就会阻止aapt(Android Asset Packaging Tool)打包app中不需要的资源文件。
```
defaultConfig {
    resConfigs "en", "de", "fr", "it"
    resConfigs "nodpi", "hdpi", "xhdpi", "xxhdpi", "xxxhdpi"
}
```
- 尽可能地重用
重用资源是最重要的优化技巧之一。比如在一个ListView或者RecyclerView，重用可以帮助你在列表滚动时保持界面流畅;重用还可以帮你减少apk文件的大小,例如，Android 提供了几个工具为一个asset文件重新着色，在Android L中你可以使用android:tint和android:tintMode来达到效果，在老版本中则可以使用ColorFilter;如果系统中有两种图片，一种图片是另一种图片翻转180°得到的，那么你就可以移除一种图片，通过代码实现。
- 在合适的时候使用代码渲染图像

##### 参考链接：
1、[Android 项目的代码混淆，Android proguard 使用说明](http://blog.csdn.net/catoop/article/details/47208833)
2、[Android安全攻防战，反编译与混淆技术完全解析](http://blog.csdn.net/guolin_blog/article/details/50451259)
3、[如何给你的Android 安装文件（APK）瘦身](http://greenrobot.me/devpost/putting-your-apks-on-diet/)
4、[关于APK瘦身值得分享的一些经验](http://blog.csdn.net/ekeuy/article/details/44900741)