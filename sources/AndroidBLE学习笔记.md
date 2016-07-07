#### 前言：
本文主要描述Android BLE的一些基础知识及相关操作流程，不牵扯具体的业务实现，其中提供了针对广播包及响应包的解析思路，希望对正在或即将面临Android BLE开发的伙伴们有所引导。
注：其中的单模、双模、BR、BT、BLE、蓝牙3.0、蓝牙4.0等概念混在一起可能比较难理解，不知下文描述是否清晰，如果有不理解的地方，欢迎留言交流！
#### 一、相关介绍
##### 1、概述
蓝牙无线技术是一种全球通用的短距离无线技术，通过蓝牙技术能够实现多种电子设备间的相互连接，特别是在小型无线电、耗电量低、成本低、安全性、稳定性、易用性以及特别的联网能力等固有的优势上，蓝牙无线技术发展迅速。
##### 2、分类
蓝牙分为三种：Bluetooth Smart Ready、Bluetooth Smart（Smart是低功耗蓝牙的标识）、以及标准 Bluetooth。根据 Bluetooth SIG的说法，这样是为了要分辨装置间的相容性以及标识各版本的传输频率。基本上来说，Bluetooth Smart Ready适用于任何双模蓝牙4.0的电子产品,而Bluetooth Smart是应用在心率监视器或计步器等使用扭扣式电池并传输单一的装置。Bluetooth Smart Ready的相容性最高，可与Bluetooth Smart及标准蓝牙相通。标准蓝牙则无法与Bluetooth Smart相通。
![](http://i1.hexunimg.cn/2015-12-09/181086199.jpg)
##### 3、BLE介绍
BLE是Bluetooth Low Energy的缩写，又叫蓝牙4.0，区别于蓝牙3.0和之前的技术。BLE前身是NOKIA开发的Wibree技术，主要用于实现移动智能终端与周边配件之间的持续连接，是功耗极低的短距离无线通信技术，并且有效传输距离被提升到了100米以上，同时只需要一颗纽扣电池就可以工作数年之久。BLE是在蓝牙技术的基础上发展起来的，既同于蓝牙，又区别于传统蓝牙。BLE设备分单模和双模两种，双模简称BR，商标为Bluetooth Smart Ready，单模简称BLE或者LE,商标为Bluetooth Smart。Android是在4.3后才支持BLE，这说明不是所有蓝牙手机都支持BLE，而且支持BLE的蓝牙手机一般是双模的。双模兼容传统蓝牙，可以和传统蓝牙通信，也可以和BLE通信，常用在手机上，android4.3和IOS4.0之后版本都支持BR，也就是双模设备。单模只能和BR和单模的设备通信，不能和传统蓝牙通信，由于功耗低，待机长，所以常用在手环的智能设备上。
 
#### 二、基本概念
##### 1、Generic Access Profile(GAP)
用来控制设备连接和广播，GAP使你的设备被其他设备可见，并决定了你的设备是否可以或者怎样与合同设备进行交互。
##### 2、Generic Attribute Profile(GATT)
通过BLE连接，读写属性类数据的Profile通用规范，现在所有的BLE应用Profile都是基于GATT的。
##### 3、Attribute Protocol (ATT)
GATT是基于ATTProtocol的，ATT针对BLE设备做了专门的优化，具体就是在传输过程中使用尽量少的数据，每个属性都有一个唯一的UUID，属性将以characteristics and services的形式传输。
##### 4、Characteristic
Characteristic可以理解为一个数据类型，它包括一个value和0至多个对次value的描述（Descriptor）。
##### 5、Descriptor
对Characteristic的描述，例如范围、计量单位等。
##### 6、Service
Characteristic的集合。例如一个service叫做“Heart Rate Monitor”，它可能包含多个Characteristics，其中可能包含一个叫做“heart ratemeasurement"的Characteristic。
##### 7、UUID
唯一标示符，每个Service，Characteristic，Descriptor，都是由一个UUID定义。

#### 三、Android BLE API
##### 1、BluetoothGatt
继承BluetoothProfile，通过BluetoothGatt可以连接设备（connect）,发现服务（discoverServices），并把相应地属性返回到BluetoothGattCallback，可以看成蓝牙设备从连接到断开的生命周期。
##### 2、BluetoothGattCharacteristic
相当于一个数据类型，可以看成一个特征或能力，它包括一个value和0~n个value的描述（BluetoothGattDescriptor）。
##### 3、BluetoothGattDescriptor
描述符，对Characteristic的描述，包括范围、计量单位等。
##### 4、BluetoothGattService
服务，Characteristic的集合。
##### 5、BluetoothProfile
一个通用的规范，按照这个规范来收发数据。
##### 6、BluetoothManager
通过BluetoothManager来获取BluetoothAdapter。
`BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);`
##### 7、BluetoothAdapter
代表了移动设备的本地的蓝牙适配器, 通过该蓝牙适配器可以对蓝牙进行基本操作，一个Android系统只有一个BluetoothAdapter，通过BluetoothManager获取。
`BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();`
##### 8、BluetoothDevice
扫描后发现可连接的设备，获取已经连接的设备。
`BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);`
##### 9、BluetoothGattCallback
已经连接上设备，对设备的某些操作后返回的结果。
```
BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback(){
	//实现回调方法，根据业务做相应处理
};
BluetoothGatt bluetoothGatt = bluetoothDevice.connectGatt(this, false, bluetoothGattCallback);

```

#### 三、操作流程
##### 1、蓝牙开启
在使用蓝牙BLE之前，需要确认Android设备是否支持BLE feature(required为false时)，另外要需要确认蓝牙是否打开。如果发现不支持BLE，则不能使用BLE相关的功能；如果支持BLE，但是蓝牙没打开，则需要打开蓝牙。代码示例如下：
```
//是否支持蓝牙模块
@TargetApi(18)
public static boolean isSupportBle(Context context) {
    if(context != null && context.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
        BluetoothManager manager = (BluetoothManager)context.getSystemService("bluetooth");
        return manager.getAdapter() != null;
    } else {
        return false;
    }
}
//是否开启蓝牙
@TargetApi(18)
public static boolean isBleEnable(Context context) {
    if(!isSupportBle(context)) {
        return false;
    } else {
        BluetoothManager manager = (BluetoothManager)context.getSystemService("bluetooth");
        return manager.getAdapter().isEnabled();
    }
}
//开启蓝牙
public static void enableBle(Activity act, int requestCode) {
    Intent mIntent = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
    act.startActivityForResult(mIntent, requestCode);
}
//蓝牙开启过程
if(isSupportBle(mContext)){
	//支持蓝牙模块
    if(!isBleEnable(mContext)){
    	//没开启蓝牙则开启
    	enableBle(mSelfActivity, 1);
    }
} else{
	//不支持蓝牙模块处理
}
//蓝牙开启回调
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	//判断requestCode是否为开启蓝牙时传进去的值，再做相应处理
    if(requestCode == 1){
    	//蓝牙开启成功后的处理
    }
    super.onActivityResult(requestCode, resultCode, data);
}
```

##### 2、设备搜索
- BluetoothAdapter.startDiscovery在大多数手机上是可以同时发现经典蓝牙和Ble的，但是startDiscovery的回调无法返回Ble的广播，所以无法通过广播识别设备，且startDiscovery扫描Ble的效率比StartLeScan低很多。所以在实际应用中，还是StartDiscovery和StartLeScan分开扫，前者扫传统蓝牙，后者扫低功耗蓝牙。

- 由于搜索需要尽量减少功耗，因此在实际使用时需要注意：当找到对应的设备后，立即停止扫描；不要循环搜索设备，为每次搜索设置适合的时间限制，避免设备不在可用范围的时候持续不停扫描，消耗电量。

- 通过调用BluetoothAdapter的 startLeScan() 搜索BLE设备。调用此方法时需要传入  BluetoothAdapter.LeScanCallback 参数。具体代码示例如下：
```
BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
bluetoothAdapter.startLeScan(new BluetoothAdapter.LeScanCallback() {
    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
		//对扫描到的设备进行处理，可以依据BluetoothDevice中的信息、信号强度rssi以及广播包和响应包组成的scanRecord字节数组进行分析
    }
});
```

##### 3、设备通信
两个设备通过BLE通信，首先需要建立GATT连接，这里我们讲的是Android设备作为client端，连接GATT Server。连接GATT Server，需要调用BluetoothDevice的connectGatt()方法，此函数带三个参数：Context、autoConnect(boolean)和 BluetoothGattCallback 对象。调用后返回BluetoothGatt对象，它是GATT profile的封装，通过这个对象，我们就能进行GATT Client端的相关操作。如断开连接`bluetoothGatt.disconnect()`，发现服务`bluetoothGatt.discoverServices()`等等。示例代码如下：

```
BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback(){
    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicRead(gatt, characteristic, status);
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorRead(gatt, descriptor, status);
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorWrite(gatt, descriptor, status);
    }

    @Override
    public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
        super.onReliableWriteCompleted(gatt, status);
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        super.onReadRemoteRssi(gatt, rssi, status);
    }

    @Override
    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
        super.onMtuChanged(gatt, mtu, status);
    }
};
BluetoothGatt bluetoothGatt = bluetoothDevice.connectGatt(this, false, bluetoothGattCallback);

//以下为获得Gatt后的相关操作对应的响应方法
//notification to onCharacteristicChanged；
bluetoothGatt.setCharacteristicNotification(characteristic, true);

//readCharacteristic to onCharacteristicRead；
bluetoothGatt.readCharacteristic(characteristic);

//writeCharacteristic to onCharacteristicWrite；
bluetoothGatt.wirteCharacteristic(mCurrentcharacteristic);

//connect and disconnect to onConnectionStateChange；
bluetoothGatt.connect();
bluetoothGatt.disconnect();

//readDescriptor to onDescriptorRead；
bluetoothGatt.readDescriptor(descriptor);

//writeDescriptor to onDescriptorWrite；
bluetoothGatt.writeDescriptor(descriptor);

//readRemoteRssi to onReadRemoteRssi；
bluetoothGatt.readRemoteRssi();

//executeReliableWrite to onReliableWriteCompleted；
bluetoothGatt.executeReliableWrite();

//discoverServices to onServicesDiscovered;
bluetoothGatt.discoverServices();
```

#### 四、数据解析
- BLE中有两种角色Central和Peripheral，也就是中心设备和外围设备，中心设备可以主动连接外围设备，外围设备发送广播或者被中心设备连接，外围通过广播被中心设备发现，广播中带有外围设备自身的相关信息。

- 数据包有两种：广播包（Advertising Data）和响应包（Scan Response），其中广播包是每个设备必须广播的，而响应包是可选的。数据包的格式如下图所示（图片来自官方 Spec）：
![](http://jlog.qiniudn.com/ble-adv-data-fromat.jpg)
每个包都是 31 字节，数据包中分为有效数据（significant）和无效数据（non-significant）两部分。

- 有效数据部分：包含若干个广播数据单元，称为AD Structure。如图中所示，AD Structure的组成是：第一个字节是长度值Len，表示接下来的Len个字节是数据部分。数据部分的第一个字节表示数据的类型AD Type，剩下的Len - 1个字节是真正的数据AD data。其中AD type非常关键，决定了AD Data的数据代表的是什么和怎么解析，这个在后面会详细讲；

- 无效数据部分：因为广播包的长度必须是31字节，如果有效数据部分不到31字节，剩下的就用0补齐，这部分的数据是无效的，解析的时候，直接忽略即可。

- 查看Nordic的SDK中的定义，AD type的定义在程序的“ble_gap.h”头文件中。定义如下：
```
#define BLE_GAP_AD_TYPE_FLAGS                               0x01 //< Flags for discoverability. 
#define BLE_GAP_AD_TYPE_16BIT_SERVICE_UUID_MORE_AVAILABLE   0x02 //< Partial list of 16 bit service UUIDs. 
#define BLE_GAP_AD_TYPE_16BIT_SERVICE_UUID_COMPLETE         0x03 //< Complete list of 16 bit service UUIDs.  
#define BLE_GAP_AD_TYPE_32BIT_SERVICE_UUID_MORE_AVAILABLE   0x04 //< Partial list of 32 bit service UUIDs.  
#define BLE_GAP_AD_TYPE_32BIT_SERVICE_UUID_COMPLETE         0x05 //< Complete list of 32 bit service UUIDs. 
#define BLE_GAP_AD_TYPE_128BIT_SERVICE_UUID_MORE_AVAILABLE  0x06 //< Partial list of 128 bit service UUIDs.  
#define BLE_GAP_AD_TYPE_128BIT_SERVICE_UUID_COMPLETE        0x07 //< Complete list of 128 bit service UUIDs. 
#define BLE_GAP_AD_TYPE_SHORT_LOCAL_NAME                    0x08 //< Short local device name. 
#define BLE_GAP_AD_TYPE_COMPLETE_LOCAL_NAME                 0x09 //< Complete local device name. 
#define BLE_GAP_AD_TYPE_TX_POWER_LEVEL                      0x0A //< Transmit power level. 
#define BLE_GAP_AD_TYPE_CLASS_OF_DEVICE                     0x0D //< Class of device. 
#define BLE_GAP_AD_TYPE_SIMPLE_PAIRING_HASH_C               0x0E //< Simple Pairing Hash C. 
#define BLE_GAP_AD_TYPE_SIMPLE_PAIRING_RANDOMIZER_R         0x0F //< Simple Pairing Randomizer R. 
#define BLE_GAP_AD_TYPE_SECURITY_MANAGER_TK_VALUE           0x10 //< Security Manager TK Value. 
#define BLE_GAP_AD_TYPE_SECURITY_MANAGER_OOB_FLAGS          0x11 //< Security Manager Out Of Band Flags. 
#define BLE_GAP_AD_TYPE_SLAVE_CONNECTION_INTERVAL_RANGE     0x12 //< Slave Connection Interval Range. 
#define BLE_GAP_AD_TYPE_SOLICITED_SERVICE_UUIDS_16BIT       0x14 //< List of 16-bit Service Solicitation UUIDs. 
#define BLE_GAP_AD_TYPE_SOLICITED_SERVICE_UUIDS_128BIT      0x15 //< List of 128-bit Service Solicitation UUIDs. 
#define BLE_GAP_AD_TYPE_SERVICE_DATA                        0x16 //< Service Data - 16-bit UUID. 
#define BLE_GAP_AD_TYPE_PUBLIC_TARGET_ADDRESS               0x17 //< Public Target Address. 
#define BLE_GAP_AD_TYPE_RANDOM_TARGET_ADDRESS               0x18 //< Random Target Address. 
#define BLE_GAP_AD_TYPE_APPEARANCE                          0x19 //< Appearance. 
#define BLE_GAP_AD_TYPE_ADVERTISING_INTERVAL                0x1A //< Advertising Interval.  
#define BLE_GAP_AD_TYPE_LE_BLUETOOTH_DEVICE_ADDRESS         0x1B //< LE Bluetooth Device Address. 
#define BLE_GAP_AD_TYPE_LE_ROLE                             0x1C //< LE Role. 
#define BLE_GAP_AD_TYPE_SIMPLE_PAIRING_HASH_C256            0x1D //< Simple Pairing Hash C-256. 
#define BLE_GAP_AD_TYPE_SIMPLE_PAIRING_RANDOMIZER_R256      0x1E //< Simple Pairing Randomizer R-256. 
#define BLE_GAP_AD_TYPE_SERVICE_DATA_32BIT_UUID             0x20 //< Service Data - 32-bit UUID. 
#define BLE_GAP_AD_TYPE_SERVICE_DATA_128BIT_UUID            0x21 //< Service Data - 128-bit UUID. 
#define BLE_GAP_AD_TYPE_3D_INFORMATION_DATA                 0x3D //< 3D Information Data.
#define BLE_GAP_AD_TYPE_MANUFACTURER_SPECIFIC_DATA          0xFF //< Manufacturer Specific Data. 
```

- 所有的 AD Type 的定义在文档[Core Specification Supplement](https://www.bluetooth.com/specifications/adopted-specifications)中。根据上面头文件中的定义，AD Type包括如下类型：
1、TYPE = 0x01：标识设备LE物理连接的功能，占一个字节，各bit为1时定义如下：
```
bit 0: LE有限发现模式
bit 1: LE普通发现模式
bit 2: 不支持BR/EDR
bit 3: 对Same Device Capable(Controller)同时支持BLE和BR/EDR
bit 4: 对Same Device Capable(Host)同时支持BLE和BR/EDR
bit 5..7: 预留
```
2、TYPE = 0x02：非完整的16 bit UUID列表
3、TYPE = 0x03：完整的16 bit UUID列表
4、TYPE = 0x04：非完整的32 bit UUID列表
5、TYPE = 0x05：完整的32 bit UUID列表
6、TYPE = 0x06：非完整的128 bit UUID列表
7、TYPE = 0x07：完整的128 bit UUID列表
8、TYPE = 0x08：设备简称
9、TYPE = 0x09：设备全名
10、TYPE = 0x0A：表示设备发送广播包的信号强度
11、TYPE = 0x0D：设备类别
12、TYPE = 0x0E：设备配对的Hash值
13、TYPE = 0x0F：设备配对的随机值
14、TYPE = 0x10：TK安全管理（Security Manager TK Value）
15、TYPE = 0x11：带外安全管理（Security Manager Out of Band），各bit定义如下：
```
bit 0: OOB Flag，0-表示没有OOB数据，1-表示有
bit 1: 支持LE
bit 2: 对Same Device Capable(Host)同时支持BLE和BR/EDR
bit 3: 地址类型，0-表示公开地址，1-表示随机地址
```
16、TYPE = 0x12：外设（Slave）连接间隔范围，数据中定义了Slave最大和最小连接间隔，数据包含4个字节：前两字节定义最小连接间隔，取值范围：0x0006 ~ 0x0C80，而0xFFFF表示未定义；后两字节，定义最大连接间隔，取值范围同上，不过需要保证最大连接间隔大于或者等于最小连接间隔。
17、TYPE = 0x14：服务搜寻16 bit UUID列表
18、TYPE = 0x15：服务搜寻128 bit UUID列表
19、TYPE = 0x16：16 bit UUID Service，前两个字节是UUID，后面是Service的数据
20、TYPE = 0x17：公开目标地址，表示希望这个广播包被指定的目标设备处理，此设备绑定了公开地址
21、TYPE = 0x18：随机目标地址，表示希望这个广播包被指定的目标设备处理，此设备绑定了随机地址
22、TYPE = 0x19：表示设备的外观
23、TYPE = 0x1A：广播区间
24、TYPE = 0x1B：LE设备地址
25、TYPE = 0x1C：LE设备角色
26、TYPE = 0x1D：256位设备配对的Hash值
27、TYPE = 0x1E：256位设备配对的随机值
28、TYPE = 0x20：32 bit UUID Service，前4个字节是UUID，后面是Service的数据
29、TYPE = 0x21：128 bit UUID Service，前16个字节是UUID，后面是Service的数据
30、TYPE = 0x3D：3D信息数据
31、TYPE = 0xFF：厂商自定义数据，厂商自定义的数据中，前两个字节表示厂商ID，剩下的是厂商自己按照需求添加，里面的数据内容自己定义。

- 根据如下数据包，举例说明解析的思路
搜索设备获取的数据包如下：
```
02 01 06 14 FF 11 22 00 00 00 01 00 1F 09 01 00 00 00 CE DD 5E 5A 5D 23 06 08 48 45 54 2D 35 09 03 E7 FE 12 FF 0F 18 0A 18 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
```
根据解析规则，可分成如下部分：
1、广播数据
```
02 01 06 14 FF 11 22 00 00 00 01 00 1F 09 01 00 00 00 CE DD 5E 5A 5D 23 06 08 48 45 54 2D 35 
```
2、响应数据
```
09 03 E7 FE 12 FF 0F 18 0A 18 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
```
3、有效数据
```
02 01 06 14 FF 11 22 00 00 00 01 00 1F 09 01 00 00 00 CE DD 5E 5A 5D 23 06 08 48 45 54 2D 35 09 03 E7 FE 12 FF 0F 18 0A 18
```
4、无效数据
```
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
```
其中的有效数据又可分为如下几个数据单元：
`02 01 06`
`14 FF 11 22 00 00 00 01 00 1F 09 01 00 00 00 CE DD 5E 5A 5D 23`
`06 08 48 45 54 2D 35`
`09 03 E7 FE 12 FF 0F 18 0A 18`
根据上面定义的AD Type分别解析如下：
第一组数据告诉我们该设备属于LE普通发现模式，不支持BR/EDR；
第二组数据告诉我们该数据为厂商自定义数据，一般是必须解析的，可根据协议规则进行解析获取对应的所需信息；
第三组数据告诉我们该设备的简称为HET-5，其中对应的字符是查找[ASSIC表](http://baike.baidu.com/link?url=_TISWJN8T3JSHi5A8OrhuBNyzLLAf006twuPA8wIWtdDG7UbyTfenCFaGWMxJCfXXmZp3SFMTHirjsKx0pTRua)得出的；
第四组数据告诉我们UUID为`E7FE-12FF-0F18-0A18`(此处有疑，类型03表示的是16位的UUID，对应的两个字节，而此处有8个字节，估计是设备烧录时把字节位数理解为了字符位数导致的问题).

#### 五、参考链接
1、[蓝牙Bluetooth BR/EDR 和 Bluetooth Smart 必需要知道的十个不同点](http://www.mr-wu.cn/ten-important-differences-between-bluetooth-bredr-and-bluetooth-smart/)
2、[BLE简介和Android BLE编程](http://blog.csdn.net/cnbloger/article/details/41382653?utm_source=tuicool&utm_medium=referral)
3、[BLE广播数据解析](http://www.tuicool.com/articles/3EZjYvv)