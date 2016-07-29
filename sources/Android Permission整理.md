#### 1、权限概述
Android 通过在每台设备上实施了基于权限的安全策略来处理安全问题，采用权限来限制安装应用程序的能力。当某个权限与某个操作和资源对象绑定在一起，我们必须获得这个权限才能在对象上执行操作。由于Android设计本身就是为Android开发人员着想，所以一切权限许可权由用户决定而不是手机制造商和平台提供商，但这不得不带来了开发者滥用权限，黑客通过权限来进行恶意行为的风险，所以作为静态分析一个App是否为恶意软件的第一道关，获取并了解Android Permission权限意义是十分重大的。
#### 2、权限策略
Android 框架提供一套默认的权限存储在android.anifest.permission类中，同时也允许我们自己定义新的权限。我们在写应用程序时声明权限，程序安装时新权限被引入系统，权限授权在应用程序被安装时执行。当在设备上安装应用程序时，程序将请求完成任务必需的权限集合，被请求的权限列单显示在设备屏幕上以待用户审查只有用户同意授权后，程序才会被安装，该应用程序获得所有被请求的权限。所以Android系统实施的主要安全准则是应用程序只有得到权限许可后，才能执行可能会影响到系统其它部分的操作。每个权限被定义成一个字符串，用来传达权限以执行某个特殊的操作。所有权限可以分为两个类别:一种是执行程序时被应用程序所请求的权限，一种是应用程序的组件之间通信时被其它组件请求的权限。开发者通过在AndroidManifest.xml文件中编写权限标签来定义以上两种类别的权限策略。
#### 3、权限声明
应用程序可以用一个`<permission>`元素来声明权限， 用于限制访问特定组件或应用程序 在安装程序时， 这个已声明的权限被加入到系统中，具体可以参看下文的Android自定义权限。
#### 4、权限请求
应用程序列出所有需要用来完成任务的权限，分别用 `<use-permission>`元素标识这些权限。在程序安装时被请求，列表显示在屏幕上用户要么同意安装，要么中止安装。同意安装则意味着授权所有被请求的权限。
#### 5、获取权限
获取权限，是我们静态分析某个App的第一关，通过知道App所具有的权限，我们一般能够基本知道该App或许会有哪些行为。目前已有多个工具可以静态检测Android app所具有的Permissions，这类工具有：aapt、apktool、androguard等等。
#### 6、Android自定义权限
在android系统的安全模型中，应用程序在默认的情况下不可以执行任何对其他应用程序，系统或者用户带来负面影响的操作。如果应用需要执行某些操作，就需要声明使用这个操作对应的权限（在manifest文件中 添加`<uses-permission>`标记）。
android 系统提供了一系列这样的权限，具体可以查看android 权限，另外，android系统在新的版本中会增加一些permission，可以查看android 版本信息。
当然，app也可以自定义属于自己的permission 或属于开发者使用的同一个签名的permission。定义一个permission 就是在menifest文件中添加一个permission标签。
```
<permission android:description="string resource"
    android:icon="drawable resource"
    android:label="string resource"
    android:name="string"
    android:permissionGroup="string"
    android:protectionLevel=["normal" | "dangerous" | "signature" | "signatureOrSystem"] />  
```
- 各属性详解：
**android:description:**对权限的描述，一般是两句话，第一句话描述这个权限所针对的操作，第二句话告诉用户授予app这个权限会带来的后果；
**android:label:**对权限的一个简短描述；
**android:name:**权限的唯一标识，一般都是使用 报名加权限名；
**android:permissionGroup:**权限所属权限组的名称；
**android:protectionLevel:**权限的等级，包含以下几个：
normal是最低的等级，声明次权限的app，系统会默认授予次权限，不会提示用户；
dangerous权限对应的操作有安全风险，系统在安装声明此类权限的app时会提示用户；
signature权限表明的操作只针对使用同一个证书签名的app开放；
signatureOrSystem与signature类似，只是增加了rom中自带的app的声明；
其中android:name属性是必须的，其他的可选，未写的系统会指定默认值。

#### 7、Android权限一览
在Android的设计中，资源的访问或者网络连接，要得到这些服务都需要声明其访问权限，否则将无法正常工作。在Android中这样的权限有很多种，这里将各类访问权限一一罗列出来，供大家使用时参考之用。

|权限|描述|
|---|---|
|android.permission.EXPAND_STATUS_BAR|允许一个程序扩展收缩在状态栏,android开发网提示应该是一个类似Windows Mobile中的托盘程序|
|android.permission.FACTORY_TEST|作为一个工厂测试程序，运行在root用户|
|android.permission.FLASHLIGHT|访问闪光灯,android开发网提示HTC Dream不包含闪光灯|
|android.permission.FORCE_BACK|允许程序强行一个后退操作是否在顶层activities|
|android.permission.FOTA_UPDATE|暂时不了解这是做什么使用的，android开发网分析可能是一个预留权限|
|android.permission.GET_ACCOUNTS|访问一个帐户列表在Accounts Service中|
|android.permission.GET_PACKAGE_SIZE|允许一个程序获取任何package占用空间容量|
|android.permission.GET_TASKS|允许一个程序获取信息有关当前或最近运行的任务，一个缩略的任务状态，是否活动等等|
|android.permission.HARDWARE_TEST|允许访问硬件|
|android.permission.INJECT_EVENTS|允许一个程序截获用户事件如按键、触摸、轨迹球等等到一个时间流|
|android.permission.INSTALL_PACKAGES|允许一个程序安装packages|
|android.permission.INTERNAL_SYSTEM_WINDOW|允许打开窗口使用系统用户界面|
|android.permission.ACCESS_CHECKIN_PROPERTIES|允许读写访问“properties”表在checkin数据库中，改值可以修改上传|
|android.permission.ACCESS_COARSE_LOCATION|允许一个程序访问CellID或WiFi热点来获取粗略的位置|
|android.permission.ACCESS_FINE_LOCATION|允许一个程序访问精良位置(如GPS)|
|android.permission.WRITE_CONTACTS|允许程序写入但不读取用户联系人数据|
|android.permission.WRITE_GSERVICES|允许程序修改Google服务地图|
|android.permission.WRITE_OWNER_DATA|允许一个程序写入但不读取所有者数据|
|android.permission.WRITE_SETTINGS|允许程序读取或写入系统设置|
|android.permission.WRITE_SMS|允许程序写短信|
|android.permission.WRITE_SYNC_SETTINGS|允许程序写入同步设置|
|android.permission.ACCESS_LOCATION_EXTRA_COMMANDS|允许应用程序访问额外的位置提供命令|
|android.permission.ACCESS_MOCK_LOCATION|允许程序创建模拟位置提供用于测试|
|android.permission.ACCESS_NETWORK_STATE|允许程序访问有关GSM网络信息|
|android.permission.ACCESS_SURFACE_FLINGER|允许程序使用SurfaceFlinger底层特性|
|android.permission.ACCESS_WIFI_STATE|允许程序访问Wi-Fi网络状态信息|
|android.permission.ADD_SYSTEM_SERVICE|允许程序发布系统级服务|
|android.permission.BATTERY_STATS|允许程序更新手机电池统计信息|
|android.permission.BLUETOOTH|允许程序连接到已配对的蓝牙设备|
|android.permission.BLUETOOTH_ADMIN|允许程序发现和配对蓝牙设备|
|android.permission.BROADCAST_PACKAGE_REMOVED|允许程序广播一个提示消息在一个应用程序包已经移除后|
|android.permission.BROADCAST_STICKY|允许一个程序广播常用intents|
|android.permission.CALL_PHONE|允许一个程序初始化一个电话拨号不需通过拨号用户界面需要用户确认|
|android.permission.DELETE_CACHE_FILES|允许程序删除缓存文件|
|android.permission.DELETE_PACKAGES|允许一个程序删除包|
|android.permission.DEVICE_POWER|允许访问底层电源管理|
|android.permission.DIAGNOSTIC|允许程序RW诊断资源|
|android.permission.DISABLE_KEYGUARD|允许程序禁用键盘锁|
|android.permission.DUMP|允许程序返回状态抓取信息从系统服务|
|android.permission.CALL_PRIVILEGED|允许一个程序拨打任何号码，包含紧急号码无需通过拨号用户界面需要用户确认|
|android.permission.CAMERA|请求访问使用照相设备|
|android.permission.CHANGE_COMPONENT_ENABLED_STATE|允许一个程序是否改变一个组件或其他的启用或禁用|
|android.permission.CHANGE_CONFIGURATION|允许一个程序修改当前设置，如本地化|
|android.permission.CHANGE_NETWORK_STATE|允许程序改变网络连接状态|
|android.permission.CHANGE_WIFI_STATE|允许程序改变Wi-Fi连接状态|
|android.permission.CLEAR_APP_CACHE|允许一个程序清楚缓存从所有安装的程序在设备中|
|android.permission.CLEAR_APP_USER_DATA|允许一个程序清除用户设置|
|android.permission.CONTROL_LOCATION_UPDATES|允许启用禁止位置更新提示从无线模块|
|android.permission.REBOOT|请求能够重新启动设备|
|android.permission.RECEIVE_BOOT_COMPLETED|允许一个程序接收到ACTION_BOOT_COMPLETED广播在系统完成启动|
|android.permission.RECEIVE_MMS|允许一个程序监控将收到MMS彩信,记录或处理|
|android.permission.RECEIVE_SMS|允许程序监控一个将收到短信息，记录或处理|
|android.permission.RECEIVE_WAP_PUSH|允许程序监控将收到WAP PUSH信息|
|android.permission.RECORD_AUDIO|允许程序录制音频|
|android.permission.REORDER_TASKS|允许程序改变Z轴排列任务|
|android.permission.RESTART_PACKAGES|允许程序重新启动其他程序|
|android.permission.SEND_SMS|允许程序发送SMS短信|
|android.permission.INTERNET|允许程序打开网络套接字|
|android.permission.MANAGE_APP_TOKENS|允许程序管理(创建、催后、 z-order默认向z轴推移)程序引用在窗口管理器中|
|android.permission.MASTER_CLEAR|目前还没有明确的解释，android开发网分析可能是清除一切数据，类似硬格机|
|android.permission.MODIFY_AUDIO_SETTINGS|允许程序修改全局音频设置|
|android.permission.MODIFY_PHONE_STATE|允许修改话机状态，如电源，人机接口等|
|android.permission.MOUNT_UNMOUNT_FILESYSTEMS|允许挂载和反挂载文件系统可移动存储|
|android.permission.PERSISTENT_ACTIVITY允许一个程序设置他的activities显示
|android.permission.PROCESS_OUTGOING_CALLS|允许程序监视、修改有关播出电话|
|android.permission.READ_CALENDAR|允许程序读取用户日历数据|
|android.permission.READ_CONTACTS|允许程序读取用户联系人数据|
|android.permission.READ_FRAME_BUFFER|允许程序屏幕波或和更多常规的访问帧缓冲数据|
|android.permission.READ_INPUT_STATE|允许程序返回当前按键状态|
|android.permission.READ_LOGS|允许程序读取底层系统日志文件|
|android.permission.READ_OWNER_DATA|允许程序读取所有者数据|
|android.permission.READ_SMS|允许程序读取短信息|
|android.permission.READ_SYNC_SETTINGS|允许程序读取同步设置|
|android.permission.READ_SYNC_STATS|允许程序读取同步状态|
|android.permission.SET_ACTIVITY_WATCHER|允许程序监控或控制activities已经启动全局系统中|
|android.permission.SET_ALWAYS_FINISH|允许程序控制是否活动间接完成在处于后台时|
|android.permission.SET_ANIMATION_SCALE|修改全局信息比例|
|android.permission.SET_DEBUG_APP|配置一个程序用于调试|
|android.permission.SET_ORIENTATION|允许底层访问设置屏幕方向和实际旋转|
|android.permission.SET_PREFERRED_APPLICATIONS|允许一个程序修改列表参数PackageManager.addPackageToPreferred()和PackageManager.removePackageFromPreferred()方法|
|android.permission.SET_PROCESS_FOREGROUND|允许程序当前运行程序强行到前台|
|android.permission.SET_PROCESS_LIMIT|允许设置最大的运行进程数量|
|android.permission.SET_TIME_ZONE|允许程序设置时间区域|
|android.permission.SET_WALLPAPER|允许程序设置壁纸|
|android.permission.SET_WALLPAPER_HINTS|允许程序设置壁纸hits|
|android.permission.SIGNAL_PERSISTENT_PROCESSES|允许程序请求发送信号到所有显示的进程中|
|android.permission.STATUS_BAR|允许程序打开、关闭或禁用状态栏及图标|
|android.permission.SUBSCRIBED_FEEDS_READ|允许一个程序访问订阅RSS Feed内容提供|
|android.permission.SUBSCRIBED_FEEDS_WRITE|系统暂时保留改设置|
|android.permission.SYSTEM_ALERT_WINDOW|允许一个程序打开窗口使用TYPE_SYSTEM_ALERT，显示在其他所有程序的顶层|
|android.permission.VIBRATE|允许访问振动设备|
|android.permission.WAKE_LOCK|允许使用PowerManager的 WakeLocks保持进程在休眠时从屏幕消失|
|android.permission.WRITE_APN_SETTINGS|允许程序写入API设置|
|android.permission.WRITE_CALENDAR|允许一个程序写入但不读取用户日历数据|


- 参考链接
[Android各种访问权限Permission详解](http://www.cppblog.com/guojingjia2006/archive/2013/02/18/197911.html)