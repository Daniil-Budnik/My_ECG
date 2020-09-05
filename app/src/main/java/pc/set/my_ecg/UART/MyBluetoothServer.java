package pc.set.my_ecg.UART;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class MyBluetoothServer extends AsyncTask {

    private BluetoothSocket BL_Socket;
    private BluetoothDevice BL_Device;

    private boolean Power = false, Enable = false, ASYNC = true;

    private InputStream BL_INPUT;
    private OutputStream BL_OUTPUT;

    // Создание сокета для сервера Bluetooth
    public MyBluetoothServer(BluetoothDevice Bluetooth_Device, UUID uuid) {

        try {
            BL_Socket = Bluetooth_Device.createRfcommSocketToServiceRecord(uuid);
            BL_Device = Bluetooth_Device;
            Power = true;
        } catch (IOException e) {
            Log.e("FATAL ERROR: ", "Bluetooth Socket and Device - database was not created...");
            Power = false;
        }
    }

    // Возвращает состояние включения Bluetooth Device подключения
    public boolean getPower() {
        return Power;
    }

    // Возвращает состояние включения Bluetooth Socket подключения
    public boolean getEnable() {
        return Enable;
    }


    // Подключение
    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            BL_Socket.connect();
            BL_OUTPUT = BL_Socket.getOutputStream();
            BL_INPUT = BL_Socket.getInputStream();
            Enable = true;
        } catch (IOException connectException) {
            // Невозможно соединиться. Закрываем сокет и выходим.
            Log.e("ERROR: ", "Bluetooth Socket and Device - Unable to connect...");
            Enable = false;
            cancel();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        ASYNC = false;
    }

    // Состояния потока подключения
    public boolean getFinish() {
        return ASYNC;
    }

    // Отключение
    public void cancel() {
        try {
            BL_Socket.close();
            Power = false;
        } catch (IOException e) {
            Log.e("ERROR: ", "Socket - don't close...");
        }
    }


}

