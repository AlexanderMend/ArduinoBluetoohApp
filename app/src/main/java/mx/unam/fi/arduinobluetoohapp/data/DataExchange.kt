package mx.unam.fi.arduinobluetoohapp.data

import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException

class DataExchange(mmSocket: BluetoothSocket): Thread() {
    private val length = 2
    private val mmInStream = mmSocket.inputStream
    private val mmOutStream = mmSocket.outputStream
    private val mmBuffer: ByteArray = ByteArray(length)

    fun write(bytes: ByteArray){
        try{
            mmOutStream.write(bytes)

        }catch (error: Exception){
            Log.e("Byte Error","Message did'nt send")

        }
    }

    fun read():String{
        var numBytesReaded = 0
        try {
            while(numBytesReaded<length){
                val number = mmInStream.read(
                    mmBuffer,numBytesReaded,length-numBytesReaded
                )
                if(number == -1){
                    break
                }
                numBytesReaded += number
            }
            Log.d("TagByte","Number of readigns bytes: " + numBytesReaded + "\n Dato: " + mmBuffer[0])
            return mmBuffer[0].toString()
        }catch (e: IOException){
            return "error" + e.toString()
        }
    }
}