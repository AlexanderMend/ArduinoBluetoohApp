package mx.unam.fi.arduinobluetoohapp.controller

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Handler
import mx.unam.fi.arduinobluetoohapp.CONNECTION_FAILED
import mx.unam.fi.arduinobluetoohapp.CONNECTION_SUCCESS
import mx.unam.fi.arduinobluetoohapp.data.DataExchange
import mx.unam.fi.arduinobluetoohapp.dataExchangeInstance
import java.util.UUID

private val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9834FB")

@SuppressLint("MissingPermission")
class ConnectThread(
    private val monDevice: BluetoothDevice,
    private val handler: Handler
): Thread() {
    private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE){
        monDevice.createRfcommSocketToServiceRecord(MY_UUID)
    }
    override fun run(){
        mmSocket?.let {
            socket ->
            try {
                socket.connect()
                handler.obtainMessage(CONNECTION_SUCCESS).sendToTarget()

            }
            catch ( e: Exception){
                handler.obtainMessage(CONNECTION_FAILED).sendToTarget()
            }
            dataExchangeInstance = DataExchange(socket)
        }
    }
}