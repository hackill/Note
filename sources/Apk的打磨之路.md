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

### 五、打包

### 六、反编译

### 七、加固

### 八、APK瘦身

### 九、上传市场
