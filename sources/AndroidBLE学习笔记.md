#### 前言：
本文主要描述Android BLE的一些基础知识及相关操作流程，不牵扯具体的业务实现，其中提供了针对广播包及响应包的解析思路，希望对正在或即将面临Android BLE开发的伙伴们有所引导。
注：其中的单模、双模、BR、BT、BLE、蓝牙3.0、蓝牙4.0等概念混在一起可能比较难理解，不知下文描述是否清晰，如果啥问题，欢迎留言交流！
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
它在用来控制设备连接和广播，GAP使你的设备被其他设备可见，并决定了你的设备是否可以或者怎样与合同设备进行交互。
##### 2、Generic Attribute Profile(GATT)
通过BLE连接，读写属性类小数据的Profile通用规范，现在所有的BLE应用Profile都是基于GATT的。
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

#### 三、操作流程

4、启动蓝牙：

在使用蓝牙BLE之前，需要确认Android设备是否支持BLE feature(required为false时)，另外要需要确认蓝牙是否打开。

如果发现不支持BLE，则不能使用BLE相关的功能。如果支持BLE，但是蓝牙没打开，则需要打开蓝牙。

打开蓝牙的步骤：

1、获取BluetoothAdapter

BluetoothAdapter是Android系统中所有蓝牙操作都需要的，它对应本地Android设备的蓝牙模块，在整个系统中BluetoothAdapter是单例的。当你获取到它的示例之后，就能进行相关的蓝牙操作了。

获取BluetoothAdapter代码示例如下：

// Initializes Bluetooth adapter.

final BluetoothManager bluetoothManager =

(BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

mBluetoothAdapter = bluetoothManager.getAdapter();

注：这里通过getSystemService获取BluetoothManager，再通过BluetoothManager获取BluetoothAdapter。BluetoothManager在Android4.3以上支持(API level 18)。

2、判断是否支持蓝牙，并打开蓝牙

获取到BluetoothAdapter之后，还需要判断是否支持蓝牙，以及蓝牙是否打开。

如果没打开，需要让用户打开蓝牙：

private BluetoothAdapter mBluetoothAdapter;

...

// Ensures Bluetooth is available on the device and it is enabled. If not,

// displays a dialog requesting user permission to enable Bluetooth.

if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {

Intent enableBtIntent = newIntent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

}

5、搜索BLE设备：

通过调用BluetoothAdapter的 startLeScan() 搜索BLE设备。调用此方法时需要传入  BluetoothAdapter.LeScanCallback 参数。

因此你需要实现 BluetoothAdapter.LeScanCallback 接口，BLE设备的搜索结果将通过这个callback返回。

由于搜索需要尽量减少功耗，因此在实际使用时需要注意：

1、当找到对应的设备后，立即停止扫描；

2、不要循环搜索设备，为每次搜索设置适合的时间限制。避免设备不在可用范围的时候持续不停扫描，消耗电量。

搜索的示例代码如下：

/**
 * Activity for scanning and displaying available BLE devices.
 */
public class DeviceScanActivity extends ListActivity {

  private BluetoothAdapter mBluetoothAdapter;
  private boolean mScanning;
  private Handler mHandler;

  // Stops scanning after 10 seconds.
  private static final long SCAN_PERIOD = 10000;
  ...
  private void scanLeDevice(final boolean enable) {
    if (enable) {
      // Stops scanning after a pre-defined scan period.
      mHandler.postDelayed(new Runnable() {
        @Override
        public void run() {
          mScanning = false;
          mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
      }, SCAN_PERIOD);

      mScanning = true;
      mBluetoothAdapter.startLeScan(mLeScanCallback);
    } else {
      mScanning = false;
      mBluetoothAdapter.stopLeScan(mLeScanCallback);
    }
    
  }

}
如果你只需要搜索指定UUID的外设，你可以调用 startLeScan(UUID[], BluetoothAdapter.LeScanCallback) 方法。

其中UUID数组指定你的应用程序所支持的GATT Services的UUID。

BluetoothAdapter.LeScanCallback 的实现示例如下：

private LeDeviceListAdapter mLeDeviceListAdapter;
...
// Device scan callback.
private BluetoothAdapter.LeScanCallback mLeScanCallback =
  new BluetoothAdapter.LeScanCallback() {
    @Override
    public void onLeScan(final BluetoothDevice device, int rssi,
      byte[] scanRecord) {
  runOnUiThread(new Runnable() {
     @Override
     public void run() {
         mLeDeviceListAdapter.addDevice(device);
         mLeDeviceListAdapter.notifyDataSetChanged();
     }
       });
   }
};
注意：搜索时，你只能搜索传统蓝牙设备或者BLE设备，两者完全独立，不可同时被搜索。

6、连接GATTServer：

两个设备通过BLE通信，首先需要建立GATT连接。这里我们讲的是Android设备作为client端，连接GATT Server。

连接GATT Server，你需要调用BluetoothDevice的 connectGatt() 方法。此函数带三个参数：Context、autoConnect(boolean)和 BluetoothGattCallback 对象。调用示例：

mBluetoothGatt = device.connectGatt(this, false,mGattCallback);

函数成功，返回 BluetoothGatt 对象，它是GATT profile的封装。通过这个对象，我们就能进行GATT Client端的相关操作。 BluetoothGattCallback 用于传递一些连接状态及结果。

BluetoothGatt常规用到的几个操作示例:

connect() ：连接远程设备。

discoverServices() : 搜索连接设备所支持的service。

disconnect()：断开与远程设备的GATT连接。

close()：关闭GATTClient端。

readCharacteristic(characteristic) ：读取指定的characteristic。

setCharacteristicNotification(characteristic, enabled)：设置当指定characteristic值变化时，发出通知。

getServices() ：获取远程设备所支持的services。

等等。

#### 四、数据解析

GAP 给设备定义了若干角色，其中主要的两个是：外围设备（Peripheral）和中心设备（Central）。

外围设备：这一般就是非常小或者简单的低功耗设备，用来提供数据，并连接到一个更加相对强大的中心设备。例如小米手环。
中心设备：中心设备相对比较强大，用来连接其他外围设备。例如手机等。
在 GAP 中外围设备通过两种方式向外广播数据： Advertising Data Payload（广播数据）和 Scan Response Data Payload（扫描回复），每种数据最长可以包含 31 byte。这里广播数据是必需的，因为外设必需不停的向外广播，让中心设备知道它的存在。扫描回复是可选的，中心设备可以向外设请求扫描回复，这里包含一些设备额外的信息，例如设备的名字。

BLE 中有两种角色 Central 和 Peripheral，也就是中心设备和外围设备。中心设备可以主动连接外围设备，外围设备发送广播或者被中心设备连接。外围通过广播被中心设备发现，广播中带有外围设备自身的相关信息。
![](http://jlog.qiniudn.com/ble-adv-data-fromat.jpg)
广播包有两种：广播包（Advertising Data）和响应包（Scan Response），其中广播包是每个设备必须广播的，而响应包是可选的。 数据包的格式如下图所示（图片来自官方 Spec）： data format 每个包都是 31 字节，数据包中分为有效数据（significant）和无效数据（non-significant）两部分。

有效数据部分：包含若干个广播数据单元，称为 AD Structure。如图中所示，AD Structure 的组成是：第一个字节是长度值 Len，表示接下来的 Len 个字节是数据部分。数据部分的第一个字节表示数据的类型 AD Type，剩下的 Len - 1 个字节是真正的数据 AD data。其中 AD type 非常关键，决定了 AD Data 的数据代表的是什么和怎么解析，这个在后面会详细讲；
无效数据部分：因为广播包的长度必须是 31 个 byte，如果有效数据部分不到 31 自己，剩下的就用 0 补全。这部分的数据是无效的，解释的时候，忽略即可。
广播数据格式
所有的 AD type 的定义在文档 ​Core Specification Supplement 中。 AD Type 包括如下类型：

Flags: TYPE = 0x01。这个数据用来标识设备 LE 物理连接的功能。DATA 是 0 到多个字节的 Flag 值，每个 bit 上用 0 或者 1 来表示是否为 True。如果有任何一个 bit 不为 0，并且广播包是可连接的，就必须包含此数据。各 bit 的定义如下：

bit 0: LE 有限发现模式
bit 1: LE 普通发现模式
bit 2: 不支持 BR/EDR
bit 3: 对 Same Device Capable(Controller) 同时支持 BLE 和 BR/EDR
bit 4: 对 Same Device Capable(Host) 同时支持 BLE 和 BR/EDR
bit 5..7: 预留
Service UUID: 广播数据中一般都会把设备支持的 GATT Service 广播出来，用来告诉外面本设备所支持的 Service。有三种类型的 UUID：16 bit, 32bit, 128 bit。广播中，每种类型类型有有两个类别：完整和非完整的。这样就共有 6 种 AD Type。

非完整的 16 bit UUID 列表： TYPE = 0x02;
完整的 16 bit UUID 列表： TYPE = 0x03;
非完整的 32 bit UUID 列表： TYPE = 0x04;
完整的 32 bit UUID 列表： TYPE = 0x05;
非完整的 128 bit UUID 列表： TYPE = 0x06;
完整的 128 bit UUID 列表： TYPE = 0x07;
Local Name: 设备名字，DATA 是名字的字符串。Local Name 可以是设备的全名，也可以是设备名字的缩写，其中缩写必须是全名的前面的若干字符。

设备全名： TYPE = 0x08
设备简称： TYPE = 0x09
TX Power Level: TYPE = 0x0A，表示设备发送广播包的信号强度。DATA 部分是一个字节，表示 -127 到 + 127 dBm。

带外安全管理（Security Manager Out of Band）：TYPE = 0x11。DATA 也是 Flag，每个 bit 表示一个功能：

bit 0: OOB Flag，0 表示没有 OOB 数据，1 表示有
bit 1: 支持 LE
bit 2: 对 Same Device Capable(Host) 同时支持 BLE 和 BR/EDR
bit 3: 地址类型，0 表示公开地址，1 表示随机地址
外设（Slave）连接间隔范围：TYPE = 0x12。数据中定义了 Slave 最大和最小连接间隔，数据包含 4 个字节：

前 2 字节：定义最小连接间隔，取值范围：0x0006 ~ 0x0C80，而 0xFFFF 表示未定义；
后 2 字节：定义最大连接间隔，同上，不过需要保证最大连接间隔大于或者等于最小连接间隔。
服务搜寻：外围设备可以要请中心设备提供相应的 Service。其数据定义和前面的 Service UUID 类似：

16 bit UUID 列表： TYPE = 0x14
32 bit UUID 列表： TYPE = 0x??
128 bit UUID 列表： TYPE = 0x15
Service Data: Service 对应的数据。

16 bit UUID Service: TYPE = 0x16, 前 2 字节是 UUID，后面是 Service 的数据；
32 bit UUID Service: TYPE = 0x??, 前 4 字节是 UUID，后面是 Service 的数据；
128 bit UUID Service: TYPE = 0x??, 前 16 字节是 UUID，后面是 Service 的数据；
公开目标地址：TYPE = 0x17，表示希望这个广播包被指定的目标设备处理，此设备绑定了公开地址，DATA 是目标地址列表，每个地址 6 字节。

随机目标地址：TYPE = 0x18，定义和前一个类似，表示希望这个广播包被指定的目标设备处理，此设备绑定了随机地址，DATA 是目标地址列表，每个地址 6 字节。

Appearance：TYPE = 0x19，DATA 是表示了设备的外观。

厂商自定义数据: TYPE = 0xFF，厂商自定义的数据中，前两个字节表示厂商 ID，剩下的是厂商自己按照需求添加，里面的数据内容自己定义。

还有一些其他的数据，我这里就不一一列举了，有需要的可以从这个文档查阅 Core Specification Supplement。

#### 五、参考链接
1、[蓝牙Bluetooth BR/EDR 和 Bluetooth Smart 必需要知道的十个不同点](http://www.mr-wu.cn/ten-important-differences-between-bluetooth-bredr-and-bluetooth-smart/)
2、[BLE简介和Android BLE编程](http://blog.csdn.net/cnbloger/article/details/41382653?utm_source=tuicool&utm_medium=referral)
3、[BLE广播数据解析](http://www.tuicool.com/articles/3EZjYvv)