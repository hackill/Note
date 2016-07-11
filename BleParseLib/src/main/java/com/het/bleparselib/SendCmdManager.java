package com.het.bleparselib;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Context;

import com.het.bleparselib.inner.CmdBuilder;
import com.vise.common_bluetooth.LiteBluetooth;
import com.vise.common_bluetooth.conn.BluetoothHelper;
import com.vise.common_bluetooth.conn.ConnectListener;
import com.vise.common_bluetooth.conn.TimeoutCallback;
import com.vise.common_bluetooth.scan.PeriodScanCallback;

/**
 * @Description:
 * @author: <a href="http://xiaoyaoyou1212.360doc.com">DAWI</a>
 * @date: 2016-07-11 11:11
 */
public class SendCmdManager {

    private LiteBluetooth liteBluetooth;
    private BluetoothHelper bluetoothHelper;
    private BluetoothGatt bluetoothGatt;
    //服务对应的UUID
    private String serviceUUID;
    //写入能力对应的UUID
    private String writeUUID;
    //通知能力对应的UUID
    private String notifyUUID;

    public SendCmdManager(Context context, String serviceUUID, String writeUUID){
        if(liteBluetooth == null){
            liteBluetooth = new LiteBluetooth(context);
        }
        this.serviceUUID = serviceUUID;
        this.writeUUID = writeUUID;
    }

    public SendCmdManager(Context context, String serviceUUID, String writeUUID, String notifyUUID){
        if(liteBluetooth == null){
            liteBluetooth = new LiteBluetooth(context);
        }
        this.serviceUUID = serviceUUID;
        this.writeUUID = writeUUID;
        this.notifyUUID = notifyUUID;
    }

    public LiteBluetooth getLiteBluetooth() {
        return liteBluetooth;
    }

    public BluetoothHelper getBluetoothHelper() {
        return bluetoothHelper;
    }

    public void setBluetoothHelper(BluetoothHelper bluetoothHelper) {
        this.bluetoothHelper = bluetoothHelper;
    }

    public void setBluetoothGatt(BluetoothGatt bluetoothGatt) {
        this.bluetoothGatt = bluetoothGatt;
    }

    public void scanDevice(PeriodScanCallback periodScanCallback){
        if(liteBluetooth != null){
            liteBluetooth.startScan(periodScanCallback);
        }
    }

    public void connectDevice(String mac, boolean autoConnect, ConnectListener listener){
        if(liteBluetooth != null){
            liteBluetooth.connect(mac, autoConnect, listener);
        }
    }

    public void connectDevice(BluetoothDevice device, boolean autoConnect, ConnectListener listener){
        if(liteBluetooth != null){
            liteBluetooth.connect(device, autoConnect, listener);
        }
    }

    public void sendCommand(byte[] cmdFlag, byte[] data, TimeoutCallback callback){
        byte[] sendData = new CmdBuilder().setCommandFlag(cmdFlag).setData(data).assembleCommand();
        SendCommandThread sendCommandThread = new SendCommandThread(sendData, callback);
        sendCommandThread.start();
    }

    public void sendCommand(byte[] sendData, TimeoutCallback callback){
        SendCommandThread sendCommandThread = new SendCommandThread(sendData, callback);
        sendCommandThread.start();
    }

    class SendCommandThread extends Thread{

        private byte[] sendData;
        private TimeoutCallback callback;

        public SendCommandThread(byte[] sendData, TimeoutCallback callback){
            this.sendData = sendData;
            this.callback = callback;
        }

        @Override
        public void run() {
            if(sendData != null && callback != null){
                try {
                    send(sendData, callback);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 发送命令 超过20个字节分包发送
     * @param sendData
     * @param callback
     */
    private synchronized void send(byte[] sendData, TimeoutCallback callback) throws InterruptedException {
        int index = 0;
        do {
            byte[] surplusData = new byte[sendData.length - index];
            byte[] currentData = new byte[20];
            System.arraycopy(sendData, index, surplusData, 0, sendData.length - index);
            if(surplusData.length <= 20){
                System.arraycopy(surplusData, 0, currentData, 0, surplusData.length);
                index += surplusData.length;
            } else{
                System.arraycopy(sendData, index, currentData, 0, 20);
                index += 20;
            }
            if(bluetoothHelper != null && bluetoothGatt != null){
                bluetoothHelper.characteristicWrite(bluetoothGatt, serviceUUID, writeUUID, currentData, callback);
                Thread.sleep(80);
            }
        }while (index < sendData.length);
    }

}
