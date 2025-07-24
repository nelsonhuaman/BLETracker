package com.danp.bletracker.service

import android.Manifest
import android.app.Service
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Context
import android.content.Intent
import android.os.ParcelUuid
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresPermission
import com.danp.bletracker.data.local.UserPreferences
import com.danp.bletracker.domain.model.ContactoCercano
import com.danp.bletracker.domain.repository.LocalContactoRepository
import com.danp.bletracker.utils.NotificationUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import java.nio.ByteBuffer

@AndroidEntryPoint
class ContactTracingService : Service() {

    @Inject lateinit var userPreferences: UserPreferences
    @Inject lateinit var contactoRepository: LocalContactoRepository

    private var advertiser: BluetoothLeAdvertiser? = null
    private var scanner: BluetoothLeScanner? = null
    private var advertiseCallback: AdvertiseCallback? = null
    private var scanCallback: ScanCallback? = null
    private var uuidUsuario: String = ""
    private val mejoresContactos = mutableMapOf<String, ContactoCercano>()

    override fun onCreate() {
        super.onCreate()

        NotificationUtils.createNotificationChannel(this)
        startForeground(1, NotificationUtils.getForegroundNotification(this))

        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        advertiser = bluetoothAdapter.bluetoothLeAdvertiser
        scanner = bluetoothAdapter.bluetoothLeScanner

        CoroutineScope(Dispatchers.IO).launch {
            try {
                uuidUsuario = userPreferences.obtenerUuid.first()

                if (ContextCompat.checkSelfPermission(this@ContactTracingService, Manifest.permission.BLUETOOTH_ADVERTISE) == PackageManager.PERMISSION_GRANTED) {
                    iniciarTransmision()
                }

                if (ContextCompat.checkSelfPermission(this@ContactTracingService, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
                    while (isActive) {
                        Log.d("BLE", "Iniciando escaneo temporal")
                        iniciarEscaneo()
                        delay(10_000)
                        detenerEscaneo()
                        delay(50_000)
                    }
                }

            } catch (e: Exception) {
                Log.e("BLE", "Error en el servicio de contacto: ${e.message}", e)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Asegura que el servicio se reinicie si es terminado por el sistema
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADVERTISE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            detenerTransmision()
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
            == PackageManager.PERMISSION_GRANTED
        ) {
            detenerEscaneo()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // ----------- TRANSMISIÓN BLE --------------
    @RequiresPermission(Manifest.permission.BLUETOOTH_ADVERTISE)
    private fun iniciarTransmision() {
        val serviceUuid = UUID.fromString("0000feed-0000-1000-8000-00805f9b34fb") // UUID fijo de servicio para tu app
        val parcelUuid = ParcelUuid(serviceUuid)

        // Convertir el UUID de string a 16 bytes
        val uuid = UUID.fromString(uuidUsuario)
        val userUuidBytes = ByteBuffer.allocate(16)
            .putLong(uuid.mostSignificantBits)
            .putLong(uuid.leastSignificantBits)
            .array()

        val advertiseData = AdvertiseData.Builder()
            .addServiceUuid(parcelUuid)
            .addServiceData(parcelUuid, userUuidBytes) // Aquí van los 16 bytes
            .setIncludeDeviceName(false)
            .build()

        val settings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
            .setConnectable(false)
            .build()

        advertiseCallback = object : AdvertiseCallback() {
            override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
                Log.d("BLE", "✅ Transmisión iniciada con UUID completo: $uuidUsuario")
            }

            override fun onStartFailure(errorCode: Int) {
                Log.e("BLE", "❌ Error en transmisión BLE: $errorCode")
            }
        }

        advertiser?.startAdvertising(settings, advertiseData, advertiseCallback)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADVERTISE)
    private fun detenerTransmision() {
        advertiser?.stopAdvertising(advertiseCallback)
        Log.d("BLE", "Transmisión detenida")
    }

    // ----------- ESCANEO BLE --------------
    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    private fun iniciarEscaneo() {

        val serviceUuid = ParcelUuid(UUID.fromString("0000feed-0000-1000-8000-00805f9b34fb"))

        val filters = listOf(
            ScanFilter.Builder()
                .setServiceUuid(serviceUuid)
                .build()
        )
        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        fun bytesToUuid(bytes: ByteArray): UUID {
            val buffer = ByteBuffer.wrap(bytes)
            val high = buffer.long
            val low = buffer.long
            return UUID(high, low)
        }

        scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                result?.scanRecord?.getServiceData(serviceUuid)?.let { serviceData ->
                    if (serviceData.size == 16) {
                        val uuidReceptor = bytesToUuid(serviceData).toString() // ✅ Conversión clara y correcta

                        if (uuidReceptor != uuidUsuario) {
                            val fuerzaSenal = result.rssi.toFloat()
                            val timestamp = System.currentTimeMillis()
                            val id = "$uuidUsuario-$uuidReceptor"

                            val contacto = ContactoCercano(
                                id = id,
                                uuidEmisor = uuidUsuario,
                                uuidReceptor = uuidReceptor,
                                fechaHora = timestamp,
                                fuerzaSenal = fuerzaSenal,
                                sincronizado = false
                            )

                            val existente = mejoresContactos[uuidReceptor]
                            if (existente == null || fuerzaSenal > existente.fuerzaSenal) {
                                mejoresContactos[uuidReceptor] = contacto
                                Log.d("BLE", "🆕 Mejor contacto actualizado: $uuidReceptor | RSSI = $fuerzaSenal")
                            }
                        }
                    } else {
                        Log.w("BLE", "⚠️ Datos recibidos no tienen 16 bytes: tamaño = ${serviceData.size}")
                    }
                }
            }

            override fun onScanFailed(errorCode: Int) {
                Log.e("BLE", "Error en escaneo BLE: $errorCode")
            }
        }

        scanner?.startScan(filters, settings, scanCallback)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    private fun detenerEscaneo() {
        scanner?.stopScan(scanCallback)
        Log.d("BLE", "⛔ Escaneo detenido. Guardando mejores contactos...")

        CoroutineScope(Dispatchers.IO).launch {
            for ((_, contacto) in mejoresContactos) {
                contactoRepository.insertarOActualizarContacto(contacto)
            }
            mejoresContactos.clear()
        }
    }
}


