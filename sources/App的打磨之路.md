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
##### 1、Maven打包
##### 2、Apktool打包
##### 3、批量快速打包
##### 4、Gradle定制化打包


```
概述
每当发新版本时，美团团购Android客户端会被分发到各个应用市场，比如豌豆荚，360手机助手等。为了统计这些市场的效果（活跃数，下单数等），需要有一种方法来唯一标识它们。

团购客户端目前通过渠道号（channel）来区分不同的市场，代码中使用Config.channel变量记录该渠道号。比如，豌豆荚市场中美团应用的渠道号是wandoujia，360手机助手中美团应用的渠道号为qihu360。客户端访问API时会在请求参数中带上渠道号，以便后台接下来计算不同渠道的效果。

每次发版时，市场部会提供一个渠道列表，Android RD会根据这些渠道相应地生成等量的渠道包。随着渠道越来越多（截止本文写作时已有900多个渠道），客户端打渠道包的方式也一直在演进，本文接下来就详细介绍美团应用的打包之旅。

Maven
Maven是一个软件项目管理和自动构建工具，配合使用android-maven-plugin插件，以及maven-resources-plugin插件可以很方便的生成渠道包，下面简要介绍下打包过程，更多Maven以及插件的使用方法请参考相关文档。

首先，在AndroidManifest.xml的<application>节点中添加如下<meta-data>元素，用来定义渠道的来源：

<!-- 使用Maven打包时会用具体的渠道号替换掉${channel} -->
<meta-data
        android:name="channel"
        android:value="${channel}" />
定义好渠道来源后，接下来就可以在程序启动时读取渠道号了：

private String getChannel(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString("channel");
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return "";

    }
要替换AndroidManifest.xml文件定义的渠道号，还需要在pom.xml文件中配置Resources插件：

<resources>           
    <resource>
        <directory>${project.basedir}</directory>
        <filtering>true</filtering>
        <targetPath>${project.build.directory}/filtered-manifest</targetPath>
        <includes>
            <include>AndroidManifest.xml</include>
        </includes>
    </resource>
</resources>
准备工作已经完成，现在需要的就是实际的渠道号了。下面的脚本会遍历渠道列表，逐个替换并打包：

#!/bin/bash

package(){
    while read line
    do
        mvn clean
        mvn  -Dchannel=$line package
    done < $1
}

package $1
在前期渠道很少时这种方法还可以接受，但只要渠道稍微增多该方法就不再适用了，原因是每打一个包都要执行一遍构建过程，效率太低。

apktool
apktool是一个逆向工程工具，可以用它解码（decode）并修改apk中的资源。接下来详细介绍如何使用apktool生成渠道包。

前期工作和用Maven打包一样，也需要在AndroidManifest.xml文件中定义<meta-data>元素，并在应用启动的时候读取清单文件中的渠道号。具体请参考上面的代码。

和Maven不一样的是，每次打包时不再需要重新构建项目。打包时，只需生成一个apk，然后在该apk的基础上生成其他渠道包即可。

首先，使用apktool decode应用程序，在终端中输入如下命令：

apktool d your_original_apk build
上面的命令会在build目录中decode应用文件，decode完成后的目录如下：



接下来，替换AndroidManifest.xml文件中定义的渠道号，下面是一段python脚本：

import re

def replace_channel(channel, manifest):
    pattern = r'(<meta-data\s+android:name="channel"\s+android:value=")(\S+)("\s+/>)'
    replacement = r"\g<1>{channel}\g<3>".format(channel=channel)
    return re.sub(pattern, replacement, manifest)
然后，使用apktool构建未签名的apk：

apktool b build your_unsigned_apk
最后，使用jarsigner重新签名apk：

jarsigner -sigalg MD5withRSA -digestalg SHA1 -keystore your_keystore_path -storepass your_storepass -signedjar your_signed_apk, your_unsigned_apk, your_alias
上面就是使用apktool打包的方法，通过使用脚本可以批量地生成渠道包。不像Maven，每打一个包都需要执行一次构建过程，该方法只需构建一次，大大节省了时间。

但是好景不长，我们的渠道包越来越多，目前已有近900个渠道，打完所有的渠道包需要近3个小时。有没有更快的打包方式呢？且看下节。

META-INF
如果能直接修改apk的渠道号，而不需要再重新签名能节省不少打包的时间。幸运的是我们找到了这种方法。直接解压apk，解压后的根目录会有一个META-INF目录，如下图所示：



如果在META-INF目录内添加空文件，可以不用重新签名应用。因此，通过为不同渠道的应用添加不同的空文件，可以唯一标识一个渠道。

下面的python代码用来给apk添加空的渠道文件，渠道名的前缀为mtchannel_：

import zipfile
zipped = zipfile.ZipFile(your_apk, 'a', zipfile.ZIP_DEFLATED) 
empty_channel_file = "META-INF/mtchannel_{channel}".format(channel=your_channel)
zipped.write(your_empty_file, empty_channel_file)
添加完空渠道文件后的目录，META-INFO目录多了一个名为mtchannel_meituan的空文件：



接下来就可以在Java代码中读取空渠道文件名了：

public static String getChannel(Context context) {
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        String ret = "";
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.startsWith("mtchannel")) {
                    ret = entryName;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String[] split = ret.split("_");
        if (split != null && split.length >= 2) {
            return ret.substring(split[0].length() + 1);

        } else {
            return "";
        }
    }
这样，每打一个渠道包只需复制一个apk，在META-INF中添加一个使用渠道号命名的空文件即可。这种打包方式速度非常快，900多个渠道不到一分钟就能打完。
```

```
概述
前一篇文章(美团Android自动化之旅—生成渠道包)介绍了Android中几种生成渠道包的方式，基本解决了打包慢的问题。

但是，随着渠道越来越多，不同渠道对应用的要求也不尽相同。例如，有的渠道要求美团客户端的应用名为美团，有的渠道要求应用名为美团团购。又比如，有些渠道要求应用不能使用第三方统计工具（如flurry）。总之，每次打包都需要对这些渠道进行适配。

之前的做法是为每个需要适配的渠道创建一个Git分支，发版时再切换到相应的分支，并合并主分支的代码。适配的渠道比较少的话这种方式还可以接受，如果分支比较多，对开发人员来说简直就是噩梦。还好，自从有了Gradle flavor，一切都变得简单了。本文假定读者使用过Gradle，如果还不了解建议先阅读相关文档。

Flavor
先来看build.gradle文件中的一段代码：

android {
    ....

    productFlavors {
        flavor1 {
            minSdkVersion 14
        }
    }
}
上例定义了一个flavor：flavor1，并指定了应用的minSdkVersion为14（当然还可以配置更多的属性，具体可参考相关文档）。与此同时，Gradle还会为该flavor关联对应的sourceSet，默认位置为src/<flavorName>目录，对应到本例就是src/flavor1。

接下来，要做的就是根据具体的需求在build.gradle文件中配置flavor，并添加必要的代码和资源文件。以flavor1为例，运行gradle assembleFlavor1命令既可生成所需的适配包。下面主要介绍美团团购Android客户端的一些适配案例。

案例
使用不同的包名
美团团购Android客户端之前有两个版本：手机版(com.meituan.group)和hd版(com.meituan.group.hd)，两个版本使用了不同的代码。目前hd版对应的代码已不再维护，希望能直接使用手机版的代码。解决该问题可以有多种方法，不过使用flavor相对比较简单，示例如下：

productFlavors {
    hd {
        applicationId "com.meituan.group.hd"
    }
}
上面的代码添加了一个名为hd的flavor，并指定了应用的包名为com.meituan.group.hd，运行gradle assembleHd命令即可生成hd适配包。

控制是否自动更新
美团团购Android客户端在启动时会默认检查客户端是否有更新，如果有更新就会提示用户下载。但是有些渠道和应用市场不允许这种默认行为，所以在适配这些渠道时需要禁止自动更新功能。

解决的思路是提供一个配置字段，应用启动的时候检查该字段的值以决定是否开启自动更新功能。使用flavor可以完美的解决这类问题。

Gradle会在generateSources阶段为flavor生成一个BuildConfig.java文件。BuildConfig类默认提供了一些常量字段，比如应用的版本名（VERSION_NAME），应用的包名（PACKAGE_NAME）等。更强大的是，开发者还可以添加自定义的一些字段。下面的示例假设wandoujia市场默认禁止自动更新功能：

android {
    defaultConfig {
        buildConfigField "boolean", "AUTO_UPDATES", "true"
    }

    productFlavors {
        wandoujia {
            buildConfigField "boolean", "AUTO_UPDATES", "false"
        }        
    }

}
上面的代码会在BuildConfig类中生成AUTO_UPDATES布尔常量，默认值为true，在使用wandoujia flavor时，该值会被设置成false。接下来就可以在代码中使用AUTO_UPDATES常量来判断是否开启自动更新功能了。最后，运行gradle assembleWandoujia命令即可生成默认不开启自动升级功能的渠道包，是不是很简单。

使用不同的应用名
最常见的一类适配是修改应用的资源。例如，美团团购Android客户端的应用名是美团，但有的渠道需要把应用名修改为美团团购；还有，客户端经常会和一些应用分发市场合作，需要在应用的启动界面中加上第三方市场的Logo，类似这类适配形式还有很多。
Gradle在构建应用时，会优先使用flavor所属dataSet中的同名资源。所以，解决思路就是在flavor的dataSet中添加同名的字符串资源，以覆盖默认的资源。下面以适配wandoujia渠道的应用名为美团团购为例进行介绍。

首先，在build.gradle配置文件中添加如下flavor：

android {
    productFlavors {
        wandoujia { 
        }
    }
}
上面的配置会默认src/wandoujia目录为wandoujia flavor的dataSet。

接下来，在src目录内创建wandoujia目录，并添加如下应用名字符串资源（src/wandoujia/res/values/appname.xml）：

<resources>
    <string name="app_name">美团团购</string>
</resources>
默认的应用名字符串资源如下（src/main/res/values/strings.xml）:

<resources>
    <string name="app_name">美团</string>
</resources>
最后，运行gradle assembleWandoujia命令即可生成应用名为美团团购的应用了。

使用第三方SDK
某些渠道会要求客户端嵌入第三方SDK来满足特定的适配需求。比如360应用市场要求美团团购Android客户端的精品应用模块使用他们提供的SDK。问题的难点在于如何只为特定的渠道添加SDK，其他渠道不引入该SDK。使用flavor可以很好的解决这个问题，下面以为qihu360 flavor引入com.qihoo360.union.sdk:union:1.0 SDK为例进行说明：

android {
    productFlavors {
        qihu360 {
        }
    }
}
...
dependencies {
    provided 'com.qihoo360.union.sdk:union:1.0'
    qihu360Compile 'com.qihoo360.union.sdk:union:1.0'
}
上例添加了名为qihu360的flavor，并且指定编译和运行时都依赖com.qihoo360.union.sdk:union:1.0。而其他渠道只是在构建的时候依赖该SDK，打包的时候并不会添加它。

接下来，需要在代码中使用反射技术判断应用程序是否添加了该SDK，从而决定是否要显示360 SDK提供的精品应用。部分代码如下：

class MyActivity extends Activity {
    private boolean useQihuSdk;

    @override
    public void onCreate(Bundle savedInstanceState) {
        try {
            Class.forName("com.qihoo360.union.sdk.UnionManager");
            useQihuSdk = true;
        } catch (ClassNotFoundException ignored) {

        }
    }
}
最后，运行gradle assembleQihu360命令即可生成包含360精品应用模块的渠道包了。
```

#### 六、反编译

反编译，又称为逆向编译技术，是指将可执行文件变成高级语言源程序的过程。反编译技术依赖于编译技术，是编译过程的逆过程。
编译程序把一个源程序翻译成目标程序的工作过程分为五个阶段：词法分析；语法分析；语义检查和中间代码生成；代码优化；目标代码生成。词法分析的任务是对由字符组成的单词进行处理，从左至右逐个字符地对源程序进行扫描，产生一个个的单词符号，把作为字符串的源程序改造成为单词符号串的中间程序。语法分析以单词符号作为输入，分析单词符号串是否形成符合语法规则的语法单位，如表达式、赋值、循环等，最后看是否构成一个符合要求的程序。语义分析是审查源程序有无语义错误，为代码生成阶段收集类型信息。中间代码是源程序的一种内部表示，或称中间语言。中间代码的作用是可使编译程序的结构在逻辑上更为简单明确，特别是可使目标代码的优化比较容易实现。代码优化是指对程序进行多种等价变换，使得从变换后的程序出发，能生成更有效的目标代码。目标代码生成是编译的最后一个阶段。
反编译器也有前端和后端。前端是一个机器依赖的模块，句法分析二进制程序、分析其指令的语义、并且生成该程序的低级中间表示法和每一子程序的控制流向图。通用的反编译机器是一个与语言和机器无关的模块，分析低级中间代码，将它转换成对任何高级语言都可接受的高级表示法，并且分析控制流向图的结构、把它们转换成用高级控制结构表现的图。最后，后端是一个目标语言依赖的模块，生成目标语言代码。

C++、C语言一般不能反编译为源代码，只能反编译为asm（汇编）语言，因为C较为底层，编译之后不保留任何元信息，而计算机运行的二进制实际上就代表了汇编指令，所以反编译为汇编是较为简单的。

C#、Java这类高级语言，尤其是需要运行环境的语言，如果没有混淆，非常容易反编译。原因很简单，这类语言只会编译为中间语言（C#为MSIL，Java为Bytecode），而中间语言与原语言本身较为相似，加上保留的元信息（记录类名、成员函数等信息）就可以反向生成源代码，注意是由反编译器生成，不会与源代码完全相同，但可以编译通过。这些特性本来是为反射技术准备的，却被反编译器利用，现在的C#反编译器ILSpy甚至可以反向工程。

反编译apk得到的几个文件和文件夹
1。 assets 文件夹
此文件夹可以存放资源文件。至于是神马资源文件，怎么读取，这些我们就不用深究了，因为不常改。
2。 res 文件夹
主要用来存放资源。此文件夹下可以创建子文件，常见的有：
anim 动画
drawable 图片
color 颜色
menu 菜单
layout 布局
values 常量值
xml 任意xml文件
我们修改apk时，一般就在这里修改。比如汉化，改名称，去广告，改字体颜色，大小神马的。
3。smali
里面主要是dex文件反编译得到的smali文件。不会java的改不起。顶多搜几个关键词汉化一下。值得注意的是，汉化时不能直接输入汉字，而应输入对应的十六进制代码。
4。AndroidManifest.xml
此文件是apk中最重要的文件之一。它是apk的全局配置文件，提供了android系统所需要的关于该应用的必要信息。

res目录浅说：
1 anim文件夹
    后缀名为.xml。动画文件都放在这里。
2 drawable文件夹
用于存放图片资源，图片或者xml。
(1) 图片
图片格式：png，9.png，jpg，gif。
注意：9.png是一种特殊的png格式，与一般的png格式有区别！很多人在改完图片后将两种格式混淆，导致回编译失败。后面有制作9.png格式图片的方法。
(2) xml
xml文件通常为自定义的形状shape或图片选择器类selector似的东西，就是不同状态下不同的图片，用于设置background什么的。
(3) drawable文件夹为了对屏幕的适配，
①对分辨率笼统的分：名字可以为：drawable-ldpi
drawable-mdpi
drawable-hdpi
drawable-xhdp
drawable-nodpi
drawable-tvdpi
分辨率依次由高到低
②具体到某一分辨率：drawable-分辨率，比如：drawable-1280x720
③如果有横竖屏的区别，则命名为：drawable-land/port-mdpi，
3 color文件夹
用于存放color列表，和drawable的xml一样，表示不同状态下的不同颜色
4 menu文件夹
菜单资源文件夹。
5 layout文件夹
布局文件夹。此文件夹的名字也是可以起到屏幕适配的功能的。
①横竖屏：layout-land/port
②分辨率：layout-1280x720
6 values文件夹
存放常量值的文件夹。里面常见的xml文件为：
arrays.xml ：  资源数组.
colors.xml :   颜色值
dimens.xml ：  像素值.
strings.xml ： 字符串值.
styles.xml ：  样式值.
此文件夹，也有屏幕适配作用。比如：dimens在不同分辨率下的值。同样的是加-1280x720这样的后缀。
对于strings，values文件夹有各种语言的版本对应。默认为英文。中文的文件夹名字应该是：
values-zh-rCN 简体中文
values-zh-rTW 繁体中文
7 raw文件夹
存放不需要系统编译成二进制的文件，例如字体文件等，同assets文件夹类似。
8 xml文件夹
存放任意的xml文件。具体看名字。

改apk时，我们主要改两种，即图片和xml。先讲图片：
改图片最简单了，注意好格式，分辨率即可。但有一点需注意，就是9.png和png格式图片的区别。许多人没注意这点，导致回编译总是失败。下面讲9.png格式图片的制作。
1 介绍 9patch
NinePatch.图片以*.9.png结尾，和普通图片的区别是四周多了一个边框
左边那条黑色线代表图片垂直拉伸的区域，上边的那条黑色线代表水平拉伸区域，右边的黑色线代表内容绘制的垂直区域，下边的黑色线代表内容绘制的水平区域，右边和下边的线是可选的，左边和上边的线不能省略。
采用NinePatch.图片做背景，可使背景随着内容的拉伸（缩小）而拉伸（缩小）
总之，通俗的将，9patch格式的图片就是有些部位能伸缩，有些部位不能的特殊png格式图片。
2 将png转化为9.png步骤如下
第一步，下载9妹工具，一楼工具包里有，双击打开draw9patch.bat，将图片拖到软件界面编辑。
第二步，勾选软件下面的三个选项，全选就对了。
第三步，把鼠标放到图片中央，此时你会看到图片四周有1像素的透明带
第四步，在左边和上边的透明带上各画一条黑线（鼠标拖动即可）
注意：当图片过大时上边的透明带会被黄色的提示框遮住，此时只能多点击黄色区域自己摸索透明带的位置。
第五步，保存，这个不用多说吧。
至此，将图片替换回原来的文件夹即可。需要注意的是，刚编辑的9patch.图片不能直接拖到原apk中，而应回编译后再替换进去。

再说修改xml。改字体，软件全局背景，汉化，去广告神马的都通过这种方式。我们常改的是layout，drawable，values目录下的xml文件，如果要改桌面插件，要去xml文件夹中修改。必要时也可尝试修改AndroidManifest.xml。下面是常见代码（分类非标准）
1 引用（@）
1)引用自定义的资源
形式 @[package:]type/name
如 android:text="@string/hello"
这里使用"@"前缀引入对一个资源的引用，后面的string/hello就是我们自定义的资源。
2) 引用系统资源
形式 @android:type/name
如 android:textColor="@android:color/opaque_red" 指定package:android
3) 引用主题属性
形式 ？android:type/name
即引用当前主题中的属性的值。
2 文本（text）
textColor 文本颜色
textColorHighlight 被选中文字的底色，默认为蓝色
textColorHint 提示信息文字的颜色，默认为灰色。与hint一起使用
textColorLink 文字链接的颜色
textScaleX 设置文字之间间隔，默认为1.0f
textSize 文字大小
textStyle 字形
typeface 文本字体
height 文本区域的高度
maxHeight 文本区域的最大高度
width 文本区域的宽度
lines 文本的行数
3 视图（view）
background 背景色/背景图片（@null 透明）
clickable 是否响应点击事件
focusable 是否获得焦点
minHeight 视图最小高度 
minWidth 视图最小宽度
padding 上下左右的边距
scrollbar 滚动条（none 隐藏，horizontal 水平，vertical 垂直）
tag 文本标签
visibility 是否显示View（visible 默认值，显示；invisible 不显示，但是仍然占用空间；gone 不显示，不占用空间）
4 布局（layout）
LinearLayout 线性布局
FrameLayout 单帧布局
RelativeLayout 相对布局
AbsoluteLayout 绝对布局
TableLayout 表格布局
layout_width 宽度
layout_height 高度
fill_patent 布满屏幕
wrap_content 适合大小
foreground 前景
background 背景
gravity 位置（center_vertical 居中；top 顶部，默认；bottom 底部）
orientation 定向（horizontal 水平，vertical 垂直）
5 主题（theme）
Theme.Dialog 对话框模式
Theme.NoTitleBar 无标题栏
Theme.NoTitleBar.Fullscreen 无标题栏，全屏
Theme.Light 白色背景
Theme.Light.NoTitleBar 白色背景，无标题栏
Theme.Light.NoTitleBar.Fullscreen 白色背景，无标题栏，全屏
Theme.Black 黑色背景
Theme.Black.NoTitleBar 黑色背景并，无标题栏
Theme.Black.NoTitleBar.Fullscreen 黑色背景，无标题栏，全屏
Theme.Wallpaper 桌面背景
Theme.Wallpaper.NoTitleBar 桌面背景，无标题栏
Theme.Wallpaper.NoTitleBar.Fullscreen 桌面背景，无标题栏，全屏
Theme.Translucent 半透明效果
Theme.Translucent.NoTitleBar 半透明，无标题栏
Theme.Translucent.NoTitleBar.Fullscreen 半透明效果，无标题栏，全屏
Theme.Panel 面板风格显示
Theme.Light.Panel 平板风格显示
6 颜色（color）
1) @color/×× 引用自定义的颜色，一般在res/values/colors.xml中
2) @android:color/×× 引用系统颜色
3) #×××××××× #后一共8个字符，由0~9，a~f组成。前两位是透明度，00表示全透明，ff表示不透明。后6位是RRGGBB，R红G绿B蓝，000000为白，ffffff为黑
7 单位
px 像素点
in 英寸
mm 毫米
pt 磅，1/72 英寸
dp 一个基于density的抽象单位，如果一个160dpi的屏幕，1dp=1px
dip 等同于dp
sp 同dp相似，但还会根据用户的字体大小偏好来缩放。
注意：sp一般是文本的单位，dip最常用

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
5、[美团Android自动化之旅—生成渠道包](http://tech.meituan.com/mt-apk-packaging.html)
6、[美团Android自动化之旅—适配渠道包](http://tech.meituan.com/mt-apk-adaptation.html)