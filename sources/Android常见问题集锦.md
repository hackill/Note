## Android常见问题集锦
**前言：**在开发中，每个人或多或少会遇到各种各样的问题，有些问题依据代码思路调试就可以定位出来，而大部分的问题都是经验性问题，遇到过就很容易解决，但在第一次遇到时往往会花费大量时间来定位问题。针对此种情况，下文把做开发以来遇到的经典问题整理出来，希望对有需要的朋友有所帮助！

*注：此文后续会同步更新最新的问题哦！*

**最近一次更新时间：** October 10, 2016 10:42 AM

### 代码类
#### 1、Java工程中调用Android库出现“Stub!”错误
**描述：**控制台显示错误：Exception in thread "main" java.lang.RuntimeException: Stub!
**原因：**在Java工程中尝试使用Android库中的org.json.JSONObject类，在执行时出现“Stub！”错误，Android工程中无法执行java的main函数相似，Android工程和Java工程还有一定的差异，不能混用他们的库，和函数入口方法。
**解决：**将执行的代码，移植到在Android工程可以正确执行！

#### 2、使用shape的同时能通过代码修改shape的颜色属性
**描述：**有时会遇到这种需求：不同状态的背景标识不同，并且背景有特定的shape样式。
**原因：**一般的shape文件都是在xml中固定好颜色，从而需要在代码中修改shape文件中的颜色值。
**解决：**直接通过控件获取该控件的背景，通过更改背景颜色从而更改shape文件中的颜色，代码如下所示：
```
GradientDrawable gradientDrawable = (GradientDrawable)view.getBackground();
gradientDrawable.setColor(color);
```

#### 3、Failure [INSTALL_FAILED_OLDER_SDK]
**描述：**编译的时候，报Failure [INSTALL_FAILED_OLDER_SDK]错误。
**原因：**一般是系统自动帮你设置了compileSdkVersion，且版本过高导致的错误。
**解决：**修改build.gradle下的compileSdkVersion xxx为compileSdkVersion 19（或者你本机已有的SDK即可）.

#### 4、Popupwindow使用异常：unable to add window--token null is not valid
**描述：** Popupwindow必须依赖一个view进行弹窗，`void android.widget.PopupWindow.showAtLocation(View parent, int gravity, int x, int y)`
调用这个方法就能显示Popupwindow了，但是有时会碰到这样一个异常：unable to add window -- token null is not valid;is your activity running?
**原因：**导致这个的原因一般是Activity的onCreate()函数里面调用了showAtLocation，由于你的popupwindow要依附于一个activity，而activity的onCreate()还没执行完就需要弹窗肯定会出问题的。
**解决：**在Handler中进行弹窗，在onCreate中通过延时调用就OK了，具体代码如下：
```
	private Handler popupHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				popupWindow.showAtLocation(findViewById(R.id.rlShowImage), Gravity.CENTER|Gravity.CENTER, 0, 0);
				popupWindow.update();
				break;
			}
		}
	};
```
```
popupHandler.sendEmptyMessageDelayed(0, 1000);
```

#### 5、使用shape绘制虚线时，在4.0以上机型上显示实线
**描述：**在利用shape绘制虚线时，在Graphical Layout中能正常显示，但在Android4.0上的机型显示成了实线。
**原因：** 4.0以上默认把Activity的硬件加速打开了，所以我们在Manifest.xml中关掉即可。
**解决：**在需要显示的activity中增加如下属性：android:hardwareAccelerated="false",也可以通过从View层级上把硬件加速关掉  view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)。

#### 6、Application does not specify an API level requirement
**描述：**编译时报警告：Application does not specify an API level requirement!
**原因：**在AndroidManifest.xml或者build.gradle文件中没有添加API版本号，不影响运行。
**解决：**在对应的地方添加上minSdkVersion和targetSdkVersion的版本号就行。

#### 7、Installation error: INSTALL_FAILED_INSUFFICIENT_STORAGE
**描述：**运行时报错： Installation error: INSTALL_FAILED_INSUFFICIENT_STORAGE。
**原因：**一般应用默认安装都是手机存储空间，而该设备没有足够的存储空间来安装应用程序。
**解决：**一般手机都有SD卡，可以在AndroidManifest.xml文件中设置属性android:installLocation="auto"就行了。

#### 8、Android 图片加载Bitmap OOM错误解决办法
**描述：** Android加载资源图片时，很容易出现OOM的错误，因为Android系统对内存有一个限制，如果超出该限制，就会出现OOM，为了避免这个问题，需要在加载资源时尽量考虑如何节约内存，尽快释放资源等等。 
**原因：** Android系统版本对图片加载回收的不同： 
1、在Android 2.3以及之后，采用的是并发回收机制，避免在回收内存时的卡顿现象； 
2、在Android 2.3.3(API Level 10)以及之前，Bitmap的backing pixel 数据存储在native memory, 与Bitmap本身是分开的，Bitmap本身存储在dalvik heap 中，导致其pixel数据不能判断是否还需要使用，不能及时释放，容易引起OOM错误，从Android 3.0(API 11)开始，pixel数据与Bitmap一起存储在Dalvik heap中。
**解决：**在加载图片资源时，可采用以下一些方法来避免OOM的问题： 
1、在Android 2.3.3以及之前，建议使用Bitmap.recycle()方法，及时释放资源；
2、在Android 3.0开始，可设置BitmapFactory.options.inBitmap值，(从缓存中获取)达到重用Bitmap的目的，如果设置，则inPreferredConfig属性值会被重用的Bitmap该属性值覆盖；
3、通过设置Options.inPreferredConfig值来降低内存消耗： 
默认为ARGB_8888: 每个像素4字节. 共32位。 
Alpha_8: 只保存透明度，共8位，1字节。 
ARGB_4444: 共16位，2字节。 
RGB_565:共16位，2字节。 
如果不需要透明度，可把默认值ARGB_8888改为RGB_565,节约一半内存。
4、通过设置Options.inSampleSize 对大图片进行压缩，可先设置Options.inJustDecodeBounds，获取Bitmap的外围数据，宽和高等。然后计算压缩比例，进行压缩；
5、设置Options.inPurgeable和inInputShareable：让系统能及时回收内存。 
inPurgeable:设置为True,则使用BitmapFactory创建的Bitmap用于存储Pixel的内存空间，在系统内存不足时可以被回收，当应用需要再次访问该Bitmap的Pixel时，系统会再次调用BitmapFactory 的decode方法重新生成Bitmap的Pixel数组；设置为False时，表示不能被回收。 
inInputShareable：设置是否深拷贝，与inPurgeable结合使用，inPurgeable为false时，该参数无意义；True：  share  a reference to the input data(inputStream, array,etc) 。 False ：a deep copy。 
6、使用decodeStream代替其他decodeResource,setImageResource,setImageBitmap等方法来加载图片。 
区别：
decodeStream直接读取图片字节码，调用nativeDecodeAsset/nativeDecodeStream来完成decode，无需使用Java空间的一些额外处理过程，节省dalvik内存。但是由于直接读取字节码，没有处理过程，因此不会根据机器的各种分辨率来自动适应，需要在hdpi,mdpi和ldpi中分别配置相应的图片资源，否则在不同分辨率机器上都是同样的大小(像素点数量)，显示的实际大小不对；
decodeResource会在读取完图片数据后，根据机器的分辨率，进行图片的适配处理，导致增大了很多dalvik内存消耗；
decodeStream调用过程：decodeStream(InputStream,Rect,Options) -> nativeDecodeAsset/nativeDecodeStream；
decodeResource调用过程：即finishDecode之后，调用额外的Java层的createBitmap方法，消耗更多dalvik内存；
decodeResource(Resource,resId,Options)  -> decodeResourceStream (设置Options的inDensity和inTargetDensity参数)  -> decodeStream() (在完成Decode后，进行finishDecode操作)finishDecode() -> Bitmap.createScaleBitmap()(根据inDensity和inTargetDensity计算scale) -> Bitmap.createBitmap()。
以上方法的组合使用，合理避免OOM错误。

#### 9、解决ScrollView嵌套GridView
**描述：**在开发中用到了需要ScrollView嵌套GridView的情况，由于这两款控件都自带滚动条，当他们碰到一起的时候便会出问题，即GridView会显示不全。
**原因：**由于父控件是自动根据子控件的大小展示的，所以需要对子控件进行最大化显示处理。
**解决：**解决办法，自定义一个GridView控件，代码如下：
```
public class MyGridView extends GridView { 
        public MyGridView(Context context, AttributeSet attrs) { 
            super(context, attrs); 
        } 
        public MyGridView(Context context) { 
            super(context); 
        } 
        public MyGridView(Context context, AttributeSet attrs, int defStyle) { 
            super(context, attrs, defStyle); 
        }     
        @Override 
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {      
            int expandSpec = MeasureSpec.makeMeasureSpec( 
                    Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST); 
            super.onMeasure(widthMeasureSpec, expandSpec); 
        } 
} 
```
代码中主要是修改了onMeasure()方法，将大小设置为int类型的最大值，至于为啥需要右移两位，是因为前两位表示的是如AT_MOST类型的值。

#### 10、Android实现ListView或GridView首行/尾行距离屏幕边缘距离
**描述：** ListView或GridView首行/尾行距离失效。
**原因：** Android上ListView&GridView默认行都是置顶的。
**解决：**设置ListView或GridView的android:clipToPadding ＝ true，然后通过paddingTop和paddingBottom设置距离就好了。

#### 11、Manifest merger failed : uses-sdk:minSdkVersion 15 cannot be smaller than version 18 declared in library
**描述：** Manifest merger failed : uses-sdk:minSdkVersion 15 cannot be smaller than version 18 declared in library;Suggestion:use tools:overrideLibrary="xxx.xxx.xxx" to force usage.
**原因：**出现这个错误一般是引用库的最低版本高于该工程最低版本；
**解决：**在AndroidManifest.xml文件中 标签中添加<uses-sdk tools:overrideLibrary="xxx.xxx.xxx"/>，其中的xxx.xxx.xxx为第三方库包名，如果存在多个库有此异常，则用逗号分割它们，例如：<uses-sdk tools:overrideLibrary="xxx.xxx.aaa, xxx.xxx.bbb"/>，这样做是为了项目中的AndroidManifest.xml和第三方库的AndroidManifest.xml合并时可以忽略最低版本限制。

#### 12、跳转到指定淘宝店铺出现Getting net::ERR_UNKNOWN_URL_SCHEME while calling telephone number from HTML page in Android
**描述：**跳转到指定淘宝店铺出现如下错误：Getting net::ERR_UNKNOWN_URL_SCHEME while calling telephone number from HTML page in Android，提示未知的协议。
**原因：**淘宝的协议是`taobao://`，在跳转前应该做对应判定，如果是网页需要换成`https://`协议。
**解决：**在跳转淘宝店铺前需要进行判定，然后使用隐式跳转到指定店铺。如果用户已经安装了淘宝客户端，那么URL前缀为`taobao://`，如果没安装淘宝客户端，URL前缀为`https://`，具体使用代码如下：
```
Uri uri;
if(ApkUpdateUtil.checkPackage(mContext, "com.taobao.taobao")){
    uri = Uri.parse(AppConstant.BEAUTY_NOW_BUY_SCHEME);
} else{
    uri = Uri.parse(AppConstant.BEAUTY_NOW_BUY_URL);
}
Intent intent = new  Intent(Intent.ACTION_VIEW, uri);
startActivity(intent);
```
其中路径常量如下所示，替换为自己店铺的地址就行，
```
public static final String BEAUTY_NOW_BUY_URL = "https://shop***";
public static final String BEAUTY_NOW_BUY_SCHEME = "taobao://shop***";
```
下面则是检测应用是否安装的静态方法。
```
/**
 * 检测该包名所对应的应用是否存在
 * @param packageName
 * @return
 */
public static boolean checkPackage(Context mContext, String packageName) {
    if (packageName == null || "".equals(packageName)){
        return false;
    }
    try {
        mContext.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
        return true;
    } catch (PackageManager.NameNotFoundException e) {
        return false;
    }
}
```

### 开发工具类
#### 1、SVN：Commit failed(details follow):svn: xxx is scheduled for addition, but is missing
**描述：**删除文件夹后点commit提交，但是提示以下错误： "svn: Commit failed (details follow): svn: 'xxx' is scheduled for addition, but is missing"。
**原因：**之前用SVN提交过的文件，被标记为"add"状态，等待被加入到仓库。若此时你把这个文件删除了，SVN提交的时候还是会尝试提交这个文件，虽然它的状态已经是 "missing"了。
**解决：**在命令行下用 "svn revert xxx --depth infinity"，在图形界面下，右键--Revert，选中那个文件或文件夹。这样就告诉SVN把这个文件退回到之前的状态 "unversioned"，也就是不对这个文件做任何修改。

#### 2、Error:Cause: peer not authenticated
**描述：** Android studio 导入项目报 Error:Cause: peer not authenticated 异常
**原因：**主要是gradle版本对应不上导致的
**解决：**在project下的build.gradle文件中，将dependencies中的classpath对应的gradle版本改为1.3.0，再将repositories中的jcenter()改为jcenter{url "http://jcenter.bintray.com/"}

#### 3、The connection to adb is down and a severe error has occured解决方案
**描述：**编译运行时报错：The connection to adb is down and a severe error has occured。
**原因：**其他应用占用了adb的进程端口，只要关闭相关进程就行。
**解决：**进入dos窗口，输入netstat -ano|findstr "5037"查看所有占用5037端口的进程ID，再输入tasklist|findstr "进程ID"，查找出对应的进程名称，进入任务管理器中关闭就行。

#### 4、SVN无法读取current修复方法
**描述：** SVN提交记录时出现Can't read file : End of file found，文件：repository/db/txn_current、repository/db/current，其中current记录当前最新版本号，txn_current记录版本库中版本号文件所存放文件夹。
**原因：**在提交文件时，svn服务器被强行关闭了，导致版本信息文件写入不成功，版本记录文件txn_current、current成了乱码。
**解决：**重新将正确的版本信息写入到current、txn-current文件，一般最新的那个版本会是错误的，只能回滚到上一版本。找到最新的版本，一般就是出错的那个版本，假设出错的是9010，一般可以从（\Repositories\ProjectName\db\revprops\X），其中的X是里面的文件夹名，几乎所有的版本号都能在这些目录里找到对应的文件名，找到最大的版本号9010，如果用记录本打开该文件是乱码，应该就是出错了，那就删除该文件，相应的，上一版本的版本号就是9009，对应的X一般就是9的文件夹。更新txn-current，里面写上X文件夹名"9"，然后回车换行并保存。更新current，里面写上9009，然后回车换行并保存。

#### 5、android You may want to manually restart adb from the Devices view.
**描述：**编译运行时报错：You may want to manually restart adb from the Devices view.
**原因：** adb服务出问题导致的，一般需要重启该服务。
**解决：**在命令窗口中输入如下指令：
```
adb kill-server
adb start-server
```

#### 6、Eclipse连接手机后DDMS一直显示connect attempts的问题
**描述：** Eclipse连接手机以后DDMS一直显示connect attempts，报Adb connection Error:An existing connection was forcibly closed by the remote host错误。
**原因：** Eclipse连接问题导致。
**解决：**添加系统环境变量：进入计算机属性，点击高级设置环境变量，新加变量ANDROID_SDK_HOME=D:\android\sdk（D:\android\sdk是android-sdk-windows的位置），Path追加%ANDROID_SDK_HOME%\tools。

#### 7、Aborting commit:’XXXXXX’remains in conflict
**描述：**提交SVN代码时报冲突错误：Aborting commit:’XXXXXX’remains in conflict！
**原因：**在使用SVN时不可避免会出现代码冲突的问题，如果在更新代码时出现本地文件已经删除，而在SVN上却被别人修改导致更新出现冲突提交代码失败。
**解决：** Eclipse解决方式如下：
1、右击工程目录； 
2、选择Team； 
3、选择Show Tree Conflict(冲突树)； 
4、查看冲突列表，右击冲突文件； 
5、标记为解决。
Android stuio解决方式如下：
1、右击工程目录； 
2、选择Sunversion； 
3、选择Commit Files； 
4、显示提交文件列表，勾选需提交文件，如果文件标志位红色方框，则表示文件冲突，直接双击冲突文件进入下一步操作； 
5、有两个选择，一个为文件以SVN为主，一个为文件以本地编写为主，如果该文件是需要被删除的，而本地已删除，则选择Accept Yours以本地为主，这样再提交就不会出现冲突。

#### 8、编译报错：com.android.dex.DexIndexOverflowException:method ID not in [0, 0xffff]:65536
**描述：**编译时报如下错误：com.android.dex.DexIndexOverflowException:method ID not in [0, 0xffff]:65536，或者在安装时失败，并报错：dexopt failed on '/data/dalvik-cache/data@app@应用包名@classes.dex' res = 65280（方法数）。
**原因：**这是由于编译时方法数越界和安装时的dexopt缓冲区大小不够存储该方法数导致的。
**解决：**在Gradle中的build文件中设置Android SDK Build Tools 21.1及以上版本，再在defaultConfig中设置multiDexEnabled true，接着还需要在dependencies中添加multidex的依赖：compile 'com.android.support:multidex:1.0.0'，最后还需要在代码中加入支持multidex的功能，最终配置文件如下所示：

```
apply plugin: 'com.android.application'

android {
    compileSdkVersion 22
    buildToolsVersion "22.0.1"//第一步
    
    defaultConfig {
        minSdkVersion 15
        targetSdkVersion 22
        versionCode 1
        versionName "1.0"
        
        multiDexEnabled true//第二步
    }
    
    lintOptions {
        abortOnError false
    }
}

dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile com.android.support:multidex:1.0.0//第三步
}
```
其中加注释部分为该设置所需，在代码中加入支持multidex功能的方法有如下三种方式：
方式一、在manifest文件中指定Application为MultiDexApplication；
方式二、让自定义的Application继承自MultiDexApplication；
方式三、在自定义的Application中实现attachBaseContext方法，在其中添加如下代码：`MultiDex.install(this)`，该方法比onCreate方法先执行。

#### 9、Gradle进行dex遇到内存不够用的情况
**描述：** java.lang.OutOfMemoryError: GC overhead limit exceeded；
**原因：**在Gradle进行dex的可能会遇到内存不够用的情况；
**解决：**在build文件的android属性下配置dexOptions下的javaMaxHeapSize大小即可，我这里配置2g；
```
dexOptions {
        javaMaxHeapSize "2g"
    }
```

#### 10、提交svn时设置忽略文件无效
**描述：**在提交SVN时有时总是提示各种已经在.gitignore文件中设置忽略却无效的文件，如.gradle、.idea、build等文件夹下的文件。
**原因：**在Share Project to Subversion之前没有设置相关文件及文件夹的忽略，导致Share Project时全部添加到SVN库中。
**解决：**这种情况一般出现在本地已经有工程了，想提交到SVN库中，但是在第一次Share Project时没有设置忽略，因为第一次分享到SVN后就相当于把所有文件都提交到SVN库中了，这样不管你后面如何设置忽略都是无效的，因为SVN库中已经有这些文件了，所以只有在Share Project之前设置忽略才会真正忽略，设置忽略的操作步骤如下所示：
打开设置，在下图所示中找到版本控制忽略文件的条目，点击右上角的添加按钮添加忽略就行。最后再点击菜单栏中的VCS，选择Import into Version Control，再选择Share Project(Subversion)分享到SVN库中就可以了。
![设置忽略](http://img.blog.csdn.net/20160927110551414)

#### 11、Error:(2, 0) Plugin with id 'com.github.dcendents.android-maven' not found
**描述：**在上传代码到jCenter库时需要配置
```
apply plugin: 'com.github.dcendents.android-maven'
apply plugin: 'com.jfrog.bintray'
```
而在配置后构建出现`Error:(2, 0) Plugin with id 'com.github.dcendents.android-maven' not found`错误。
**原因：**在使用插件功能时需要配置插件的支持来源及版本。
**解决：**找到project的build.gradle添加如下配置
```
classpath 'com.github.dcendents:android-maven-gradle-plugin:1.3'
classpath "com.jfrog.bintray.gradle:gradle-bintray-plugin:1.0"
```
完整配置如下：
```
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:1.5.0'
        classpath 'com.github.dcendents:android-maven-gradle-plugin:1.3'
        classpath "com.jfrog.bintray.gradle:gradle-bintray-plugin:1.0"
    }
}
```
然后重新构建即可。

#### 12、 Failed to apply plugin [id 'com.github.dcendents.android-maven']，Could not create plugin of type 'AndroidMavenPlugin'.
**描述：**在使用Android Studio2.2时需要将gradle插件版本更新为`classpath 'com.android.tools.build:gradle:2.2.0'`，构建工具版本更新为`distributionUrl=https\://services.gradle.org/distributions/gradle-2.14.1-all.zip`，而在使用上传代码到jCenter功能时报错：`Failed to apply plugin [id 'com.github.dcendents.android-maven']，Could not create plugin of type 'AndroidMavenPlugin'.`
**原因：** AndroidMavenPlugin插件版本配置过低。
**解决：**将原来的配置
```
classpath 'com.github.dcendents:android-maven-gradle-plugin:1.3'
classpath "com.jfrog.bintray.gradle:gradle-bintray-plugin:1.0"
```
改成如下形式就行：
```
classpath 'com.github.dcendents:android-maven-gradle-plugin:1.5'
classpath "com.jfrog.bintray.gradle:gradle-bintray-plugin:1.0"
```


### 操作类
#### 1、Javadoc中产生乱码的解决方法
**描述：**在生成javadoc文档或者在进行打包时出现“编码GBK的不可映射字符”错误。
**原因：**因为代码中有中文注释的缘故，这个还是比较常见的。
**解决：**
方式一：通过Android Studio界面操作生成JavaDoc文档，依次打开Tools->Generate JavaDoc->Other command line arguments设置为：“-encoding UTF-8 -charset UTF-8”；
方式二：通过配置module中的build.gradle文件，添加一个如下任务task：tasks.withType(JavaCompile) {
    options.encoding = "UTF-8"
}，这个构建时不会出现乱码，但是在查看生成的文档时会发现显示会乱码，找到原因是需要设置charset也为UTF-8，至今没找到怎么配置，如果有哪位仁兄知道怎么配置，麻烦评论告知下，万分感谢！

#### 2、虚拟按键影响界面全屏显示
**描述：**在android4.0及其以上的版本中，出现了一个很屌的东西，叫做Navigation Bar，它和Status Bar一上一下相互交映，影响了我们的全屏。
**原因：**全屏显示时有时需要隐藏导航栏和虚拟按键，而虚拟按键是会根据你的触摸显示出来的，所以需要添加监听，在出现时直接强制隐藏。
**解决：**直接在当前Activity中添加如下代码就行：
```
private static Handler sHandler;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            int flags;
            int curApiVersion = android.os.Build.VERSION.SDK_INT;
            if (curApiVersion >= Build.VERSION_CODES.KITKAT) {
                flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE;

            } else {
                flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            getWindow().getDecorView().setSystemUiVisibility(flags);

        }
    };
```
```
@Override
    protected void onResume() {
        super.onResume();
        sHandler = new Handler();
        sHandler.post(mHideRunnable); // hide the navigation bar
        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                sHandler.post(mHideRunnable); // hide the navigation bar
            }
        });
    }
```

#### 3、ListView点击条目无响应
**描述：**开发中很常见的一个问题，项目中的listview不仅仅是简单的文字，常常需要自己定义listview，自己的Adapter去继承BaseAdapter，在adapter中按照需求进行编写，问题就出现了，可能会发生点击每一个item的时候没有反应，无法获取的焦点。
**原因：**原因多半是由于在你自己定义的Item中存在诸如ImageButton，Button，CheckBox等子控件(也可以说是Button或者Checkable的子类控件)，此时这些子控件会将焦点获取到，所以常常当点击item时变化的是子控件，item本身的点击没有响应。
**解决：**使用descendantFocusability来解决，该属性是当一个为view获取焦点时，定义viewGroup和其子控件两者之间的关系。
属性的值有三种：
beforeDescendants：viewgroup会优先其子类控件而获取到焦点
afterDescendants：viewgroup只有当其子类控件不需要获取焦点时才获取焦点
blocksDescendants：viewgroup会覆盖子类控件而直接获得焦点
通常我们用到的是第三种，即在Item布局的根布局加上android:descendantFocusability=”blocksDescendants”的属性就好了。

#### 4、ActivityManager: Warning: Activity not started, its current task has been brought to the front
**描述：**用手机调试运行出现如下错误：ActivityManager: Warning: Activity not started, its current task has been brought to the front。
**原因：**该手机已经启动了相同名字的应用。
**解决：**关闭运行的应用重试就行。

#### 5、Failed to fetch URL https://dl-ssl.google.com/android/repository/repository.xml
**描述：**在打开SDK Manager时，更新SDK时会出现如下错误：Failed to fetch URL https://dl-ssl.google.com/android/repository/repository.xml。
**原因：** dl-ssl.google.com在大陆封掉了。
**解决：**修改C:\Windows\System32\drivers\etc\hosts文件，添加一行：`74.125.237.1  dl-ssl.google.com`,保存并重新自动SDK Manager就OK了。

#### 6、Android程序启动界面的短暂黑屏
**描述：**默认的情况下，android 程序启动时，会有一个黑屏的时期。
**原因：**系统默认的主题背景为黑色导致的。
**解决：**只要在入口activity加上android:theme="@android:style/Theme.Translucent" 就可以解决启动黑屏的问题。

#### 7、curl: (6) Couldn't resolve host 'android.git.kernel.org' 
**描述：**通过Linux系统下载Android源码时提示错误：curl: (6) Couldn't resolve host 'android.git.kernel.org' 。
**原因：**因为android.git.kernel.org网站被黑了，所以无法从该网站下载repo和android源代码了。
**解决：**换个网址下载，从[https://www.codeaurora.org/](http://)网站下载android源码，具体方法如下：
下载repo并设置环境变量
```
$ curl "http://php.webtutor.pl/en/wp-content/uploads/2011/09/repo"> ~/bin/repo
$ chmod a+x ~/bin/repo
$ PATH=~/bin:$PATH
```
下载android源码
```
$ mkdir WORKING_DIRECTORY
$cd WORKING_DIRECTORY
$ repo init -u git://codeaurora.org/platform/manifest.git -b gingerbread
$ repo sync
```

#### 8、ListView取消点击效果
**描述：**用LIstView呈现的数据，类似于静态的那中，但是不要点击效果，默认的就是点击一下显示成橙黄色
**原因：**主要是实现点击没效果，其实是有效果只不过是透明给掩盖了
**解决：**在listview布局属性中添加如下属性android:listSelector="@android:color/transparent"；

#### 9、Didn't find class "com.android.tools.fd.runtime.BootstrapApplication"
**描述：**提供的安装包在部分手机上安装点击后直接崩溃，提示Didn't find class "com.android.tools.fd.runtime.BootstrapApplication"这样的错误
**原因：** Instant Run不支持5.0以下系统
**解决：**
方法一：直接Clean工程，再重新打包
方法二：禁止Instant Run功能，按照如下步骤操作即可：File –> Settings–>Build,Execution,Deployment –>Instant Run —> 不勾选 “Enable instant run”
方法三：修改编译环境，将相关的版本信息调低
将`classpath 'com.android.tools.build:gradle:2.2.0'`修改为`classpath 'com.android.tools.build:gradle:1.2.3'`；将`buildToolsVersion '23.0.2`修改为`buildToolsVersion "21.1.2"`；找到`.idea/gradle.xml`文件，将`<option name="gradleHome" value="$APPLICATION_HOME_DIR$/gradle/gradle-2.8" />`修改为`<option name="gradleHome" value="$APPLICATION_HOME_DIR$/gradle/gradle-2.4" />`
出现如上问题，一般第一种方法都能解决，如果不行，禁用Instant Run功能即可，一般第三种方法不推荐使用。