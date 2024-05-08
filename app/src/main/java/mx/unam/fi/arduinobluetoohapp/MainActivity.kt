package mx.unam.fi.arduinobluetoohapp

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import mx.unam.fi.arduinobluetoohapp.controller.connectHC05
import mx.unam.fi.arduinobluetoohapp.data.DataExchange
import mx.unam.fi.arduinobluetoohapp.ui.theme.ArduinoBluetoohAppTheme
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import mx.unam.fi.arduinobluetoohapp.ui.views.BluetoohUI

const val CONNECTION_FAILED : Int = 0
const val CONNECTION_SUCCESS : Int = 1
var dataExchangeInstance: DataExchange? = null

class MainActivity : ComponentActivity() {
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter

        val status = mutableStateOf("Bluetooth & Arduino \n")
        val handler = Handler(
            Looper.getMainLooper()
        ){
            msg ->
            when(msg.what){
                CONNECTION_FAILED -> {
                    status.value+= "Conexión Rechazada"
                    true
                }
                CONNECTION_SUCCESS->{
                    status.value+= "Conexion Exitosa"
                    true
                }
                else -> false

            }
        }
        
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ){
            isGranted ->
            if(isGranted){
                status.value += "Permisos Aceptados \n Intentando Conexión\n"
                status.value += connectHC05(bluetoothAdapter,handler)
            }else{
                status.value += "=======> Permiso Denegado"
            }
        }
        when{
            ContextCompat.checkSelfPermission(
                applicationContext,Manifest.permission.BLUETOOTH) ==
                PackageManager.PERMISSION_GRANTED -> {
                    status.value += connectHC05(bluetoothAdapter,handler)
                }
                ActivityCompat.shouldShowRequestPermissionRationale(
                    this,Manifest.permission.BLUETOOTH
                
            ) -> {
                Toast.makeText(applicationContext,"Debes de dar permisos",Toast.LENGTH_LONG)
            }
            else->{
                requestPermissionLauncher.launch(
                    Manifest.permission.BLUETOOTH
                )
            }
        }
        setContent {
            ArduinoBluetoohAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BluetoohUI(connectStatus = status)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ArduinoBluetoohAppTheme {
        Greeting("Android")
    }
}