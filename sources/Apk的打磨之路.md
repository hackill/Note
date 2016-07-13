---
title: Apk的打磨之路
date: 2016-06-25 10:24:51
tags: [Android,随笔]
---
#### 前言：
俗话说磨刀不误砍柴工，一个优秀的产品从一个不错的点子直到用户的手中，是需要一个团队不遗余力协同合作不断打磨出来的；同样，一个好的APK除正常的代码编写外，还需要经过其他方面的不断打磨才能正式交互，最终到达用户的手中。该文主要讲述一个应用除开发外还需要进行哪些工作才能合格交互，在此抛砖引玉，希望对有需要的朋友一点启示！
### 一、单元测试
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
### 二、性能分析

### 三、签名
签名的前提得有签名文件，生成签名文件的方式大同小异，IDE基本都有这个功能，这里以Android Studio为列讲述生成签名文件的过程。选择工具栏Build->Generate Signed APK,打开后选择对应的module，点击next，如图所示：

点击Create new，进入如下界面：

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
### 四、混淆
Java 是一种跨平台的、解释型语言，Java 源代码编译成中间”字节码”存储于 class 文件中。由于跨平台的需要，Java 字节码中包括了很多源代码信息，如变量名、方法名，并且通过这些名称来访问变量和方法，这些符号带有许多语义信息，很容易被反编译成 Java 源代码。为了防止这种现象，我们可以使用 Java 混淆器对 Java 字节码进行混淆。
混淆就是对发布出去的程序进行重新组织和处理，使得处理后的代码与处理前代码完成相同的功能，而混淆后的代码很难被反编译，即使反编译成功也很难得出程序的真正语义。被混淆过的程序代码，仍然遵照原来的档案格式和指令集，执行结果也与混淆前一样，只是混淆器将代码中的所有变量、函数、类的名称变为简短的英文字母代号，在缺乏相应的函数名和程序注释的况下，即使被反编译，也将难以阅读。同时混淆是不可逆的，在混淆的过程中一些不影响正常运行的信息将永久丢失，这些信息的丢失使程序变得更加难以理解。
混淆器的作用不仅仅是保护代码，它也有精简编译后程序大小的作用。由于以上介绍的缩短变量和函数名以及丢失部分信息的原因， 编译后 jar 文件体积大约能减少25% ，这对当前费用较贵的无线网络传输是有一定意义的。
混淆是上线前挺重要的一个环节,android使用的是ProGuard，可以起到压缩，混淆，预检，优化的作用。纵观大部分项目的混淆文件，大部分内容都是固定的，从中可以整理出一个通用的模板，模板内容大致分为以下几个部分：基本指令、公共组件、第三方库、实体类、反射相关及JS调用相关。其中前两部分基本不会有太大变化，第三方库网上基本都会提供混淆方式，下文也会依据网上资源整理出大部分的三方库保留方式，而后面几个部分就与具体项目相关了，掌握思路后依照具体项目定制就行。
1、基本指令
```
-optimizationpasses 5	# 指定代码的压缩级别
-dontusemixedcaseclassnames	# 表示混淆时不使用大小写混合类名
-dontskipnonpubliclibraryclasses	# 表示不跳过library中的非public的类
-dontskipnonpubliclibraryclassmembers	# 指定不去忽略包可见的库类的成员
-dontoptimize	# 表示不进行优化，建议使用此选项，因为根据proguard-android-optimize.txt中的描述，优化可能会造成一些潜在风险，不能保证在所有版本的Dalvik上都正常运行
-dontpreverify  # 表示不进行预校验,这个预校验是作用在Java平台上的，Android平台上不需要这项功能，去掉之后还可以加快混淆速度
-verbose	# 表示打印混淆的详细信息
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*	# 混淆时所采用的算法
```

2、公共组件
```
-keep public class * extends android.app.Activity	# 保留Activity类不被混淆
-keep public class * extends android.app.Application	# 保留Application类不被混淆
-keep public class * extends android.app.Service	# 保留Service类不被混淆
-keep public class * extends android.content.BroadcastReceiver	# 保留BroadcastReceiver类不被混淆
-keep public class * extends android.content.ContentProvider	# 保留ContentProvider类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper	# 保留BackupAgentHelper类不被混淆
-keep public class * extends android.preference.Preference	# 保留Preference类不被混淆
-keep public class com.google.vending.licensing.ILicensingService	# 保留Google包下ILicensingService类不被混淆
-keep public class com.android.vending.licensing.ILicensingService	# 保留Android包下ILicensingService类不被混淆

-keepattributes *Annotation*,InnerClasses,Signature,SourceFile,LineNumberTable	# 保留相关属性

-keepclasseswithmembernames class * {                                           # 保持native方法不被混淆
    native <methods>;
}

-keepclasseswithmembers class * {                                               # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);     # 保持自定义控件类不被混淆
}

-keepclassmembers class * extends android.app.Activity {                        # 保持自定义控件类不被混淆
   public void *(android.view.View);
}

-keepclassmembers enum * {                                                      # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {                                # 保持 Parcelable 不被混淆
  public static final android.os.Parcelable$Creator *;
}
```
```
-keep class android.support.** {*;}

-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}

-keepclassmembers class * {
    void *(**On*Event);
}

-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}
```

3、第三方库
```
#eventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#log4j
-libraryjars log4j-1.2.17.jar
-dontwarn org.apache.log4j.**
-keep class  org.apache.log4j.** { *;}
```
4、实体类
```
-keep class 你的实体类包名.** { *; }
```

5、反射相关
```
-keep class 你的类所在的包.** { *; }
```

6、JS调用相关
```
-keep class 你的类所在的包.** { *; }
//如果是内部类则使用如下方式
-keepclasseswithmembers class 你的类所在的包.父类$子类 { <methods>; }
```


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

mapping.txt
表示混淆前后代码的对照表，这个文件非常重要。如果你的代码混淆后会产生bug的话，log提示中是混淆后的代码，希望定位到源代码的话就可以根据mapping.txt反推。
每次发布都要保留它方便该版本出现问题时调出日志进行排查，它可以根据版本号或是发布时间命名来保存或是放进代码版本控制中。


dump.txt
描述apk内所有class文件的内部结构。

seeds.txt
列出了没有被混淆的类和成员。

usage.txt
列出了源代码中被删除在apk中不存在的代码。


proguard 原理
Java代码编译成二进制class 文件，这个class 文件也可以反编译成源代码 ，除了注释外，原来的code 基本都可以看到。为了防止重要code 被泄露，我们往往需要混淆（Obfuscation code ， 也就是把方法，字段，包和类这些java 元素的名称改成无意义的名称，这样代码结构没有变化，还可以运行，但是想弄懂代码的架构却很难。 proguard 就是这样的混淆工具，它可以分析一组class 的结构，根据用户的配置，然后把这些class 文件的可以混淆java 元素名混淆掉。在分析class 的同时，他还有其他两个功能，删除无效代码（Shrinking 收缩），和代码进行优化（Optimization Options）。
缺省情况下，proguard 会混淆所有代码，但是下面几种情况是不能改变java 元素的名称，否则就会这样就会导致程序出错。
一， 我们用到反射的地方。
二，我们代码依赖于系统的接口，比如被系统代码调用的回调方法，这种情况最复杂。
三， 是我们的java 元素名称是在配置文件中配置好的。
所以使用proguard时，我们需要有个配置文件告诉proguard 那些java 元素是不能混淆的。

proguard 配置
最常用的配置选项
-dontwarn 缺省proguard 会检查每一个引用是否正确，但是第三方库里面往往有些不会用到的类，没有正确引用。如果不配置的话，系统就会报错。
-keep 指定的类和类成员被保留作为 入口。
-keepclassmembers 指定的类成员被保留。
-keepclasseswithmembers 指定的类和类成员被保留，假如指定的类成员存在的话。

proguard 问题和风险
代码混淆后虽然有混淆优化的好处，但是它往往也会带来如下的几点问题
1，混淆错误，用到第三方库的时候，必须告诉 proguard 不要检查，否则proguard 会报错。
2，运行错误，当code 不能混淆的时候，我们必须要正确配置，否则程序会运行出错，这种情况问题最多。
3，调试苦难，出错了，错误堆栈是混淆后的代码，自己也看不懂。


为了防止混淆出问题，你需要熟悉你所有的code ，系统的架构，以及系统和你code的集成的接口，并细心分析。 同时你必须需要一轮全面的测试。 所以混淆也还是有一定风险的。为了避免风险，你可以只是混淆部分关键的代码，但是这样你的混淆的效果也会有所降低。

常见的不能混淆的androidCode
Android 程序，下面这样代码混淆的时候要注意保留。
Android系统组件，系统组件有固定的方法被系统调用。
被Android Resource 文件引用到的。名字已经固定，也不能混淆，比如自定义的View 。
Android Parcelable ，需要使用android 序列化的。
其他Anroid 官方建议不混淆的，如
android.app.backup.BackupAgentHelper
android.preference.Preference
com.android.vending.licensing.ILicensingService
Java序列化方法，系统序列化需要固定的方法。
枚举 ，系统需要处理枚举的固定方法。
本地方法，不能修改本地方法名
annotations 注释
数据库驱动
有些resource 文件
用到反射的地方


如何实施
现在的系统已经配置为混淆时候会保留
Android系统组件
自定义View
Android Parcelable
Android R 文件
Android Parcelable
枚举
各个开发人员必须检查自己的code 是否用到反射，和其他不能混淆的地方。告诉我来修改配置文件（已经保留的就不需要了）


目前系统部检查的第三方库为
-dontwarn android.support.**
-dontwarn com.tencent.**
-dontwarn org.dom4j.**
-dontwarn org.slf4j.**
-dontwarn org.http.mutipart.**
-dontwarn org.apache.**
-dontwarn org.apache.log4j.**
-dontwarn org.apache.commons.logging.**
-dontwarn org.apache.commons.codec.binary.**
-dontwarn weibo4android.**
proguard 参数
-include {filename}    从给定的文件中读取配置参数 

-basedirectory {directoryname}    指定基础目录为以后相对的档案名称 

-injars {class_path}    指定要处理的应用程序jar,war,ear和目录 

-outjars {class_path}    指定处理完后要输出的jar,war,ear和目录的名称 

-libraryjars {classpath}    指定要处理的应用程序jar,war,ear和目录所需要的程序库文件 

-dontskipnonpubliclibraryclasses    指定不去忽略非公共的库类。 

-dontskipnonpubliclibraryclassmembers    指定不去忽略包可见的库类的成员。 


保留选项 
-keep {Modifier} {class_specification}    保护指定的类文件和类的成员 

-keepclassmembers {modifier} {class_specification}    保护指定类的成员，如果此类受到保护他们会保护的更好

-keepclasseswithmembers {class_specification}    保护指定的类和类的成员，但条件是所有指定的类和类成员是要存在。 

-keepnames {class_specification}    保护指定的类和类的成员的名称（如果他们不会压缩步骤中删除） 

-keepclassmembernames {class_specification}    保护指定的类的成员的名称（如果他们不会压缩步骤中删除） 

-keepclasseswithmembernames {class_specification}    保护指定的类和类的成员的名称，如果所有指定的类成员出席（在压缩步骤之后） 

-printseeds {filename}    列出类和类的成员-keep选项的清单，标准输出到给定的文件 

压缩 
-dontshrink    不压缩输入的类文件 

-printusage {filename} 

-whyareyoukeeping {class_specification}     

优化 
-dontoptimize    不优化输入的类文件 

-assumenosideeffects {class_specification}    优化时假设指定的方法，没有任何副作用 

-allowaccessmodification    优化时允许访问并修改有修饰符的类和类的成员 

混淆 
-dontobfuscate    不混淆输入的类文件 

-printmapping {filename} 

-applymapping {filename}    重用映射增加混淆 

-obfuscationdictionary {filename}    使用给定文件中的关键字作为要混淆方法的名称 

-overloadaggressively    混淆时应用侵入式重载 

-useuniqueclassmembernames    确定统一的混淆类的成员名称来增加混淆 

-flattenpackagehierarchy {package_name}    重新包装所有重命名的包并放在给定的单一包中 

-repackageclass {package_name}    重新包装所有重命名的类文件中放在给定的单一包中 

-dontusemixedcaseclassnames    混淆时不会产生形形色色的类名 

-keepattributes {attribute_name,...}    保护给定的可选属性，例如LineNumberTable, LocalVariableTable, SourceFile, Deprecated, Synthetic, Signature, and InnerClasses. 

-renamesourcefileattribute {string}    设置源文件中给定的字符串常量

解决export打包的报错
这个时候export提示“conversion to Dalvik format failed with error 1”错误，网上说法有好多种，最后我还是把proguard从4.4升级到4.8就解决了。官方地址是http://proguard.sourceforge.net。上面的配置文件参数可以在这里查阅。
升级办法很简单，就是把android sdk目录下的tool/proguard目录覆盖一下即可。

打包出来的程序如何调试
一旦打包出来，就不能用eclipse的logcat去看了，这里可以用android sdk中ddms.bat的tool来看，一用就发现和logcat其实还是一个东西，就是多了个设备的选择。

使用 gson 需要的配置
当Gson用到了泛型就会有报错，这个真给郁闷了半天，提示“Missing type parameter”。最后找到一个资料给了一个解决办法，参考：http://stackoverflow.com/questio ... sing-type-parameter。
另外我又用到了JsonObject，提交的Object里面的members居然被改成了a。所以上面给的东西还不够，还要加上
# 用到自己拼接的JsonObject
-keep class com.google.gson.JsonObject { *; }


个人建议减少这些依赖包混淆带来的麻烦，干脆都全部保留不混淆。例如
-keep class com.badlogic.** { *; }
-keep class * implements com.badlogic.gdx.utils.Json*
-keep class com.google.** { *; }

使用libgdx需要的配置
参考http://code.google.com/p/libgdx-users/wiki/Ant


验证打包效果
利用了apktool的反编译工具，把打包文件又解压了看了一下，如果包路径、类名、变量名、方法名这些变化和你期望一致，那就OK了。命令：


apktool.bat d xxx.apk destdir

配置实例
-injars  androidtest.jar【jar包所在地址】 
-outjars  out【输出地址】
-libraryjars    'D:\android-sdk-windows\platforms\android-9\android.jar' 【引用的库的jar，用于解析injars所指定的jar类】

-optimizationpasses 5
-dontusemixedcaseclassnames 【混淆时不会产生形形色色的类名 】
-dontskipnonpubliclibraryclasses 【指定不去忽略非公共的库类。 】
-dontpreverify 【不预校验】
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/* 【优化】
-keep public class * extends android.app.Activity　　【不进行混淆保持原样】
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public abstract interface com.asqw.android.Listener{
public protected ;  【所有方法不进行混淆】
}
-keep public class com.asqw.android{
public void Start(java.lang.String); 【对该方法不进行混淆】
}
-keepclasseswithmembernames class * { 【保护指定的类和类的成员的名称，如果所有指定的类成员出席（在压缩步骤之后）】
native ;
}
-keepclasseswithmembers class * { 【保护指定的类和类的成员，但条件是所有指定的类和类成员是要存在。】
public (android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
public (android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity {【保护指定类的成员，如果此类受到保护他们会保护的更好】
public void *(android.view.View);
}
-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {【保护指定的类文件和类的成员】
public static final android.os.Parcelable$Creator *;
}
//不混淆指定包下的类 
-keep class com.aspire.**
 

### 五、打包

### 六、反编译

### 七、加固

### 八、APK瘦身

