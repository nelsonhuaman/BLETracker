package com.danp.bletracker

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.danp.bletracker.worker.SincronizarContactosWorker
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class BLETrackerApp : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        programarSincronizacionPeriodica()
        observarEstadoDelWorker()
    }

    private fun programarSincronizacionPeriodica() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // Solo si hay internet
            .build()

        val request = PeriodicWorkRequestBuilder<SincronizarContactosWorker>(
            15, TimeUnit.MINUTES // Mínimo permitido oficialmente por WorkManager
        )
            .setConstraints(constraints) // Aplica las restricciones
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "sync_contactos",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
    private fun observarEstadoDelWorker() {
        val prefs = getSharedPreferences("worker_prefs", Context.MODE_PRIVATE)
        val nextTime = prefs.getLong("next_worker_time", 0L)
        val diff = nextTime - System.currentTimeMillis()

        if (diff > 0) {
            val minutosRestantes = TimeUnit.MILLISECONDS.toMinutes(diff)
            Timber.d("Faltan $minutosRestantes minutos para que se ejecute el Worker")
        }

        WorkManager.getInstance(this)
            .getWorkInfosForUniqueWorkLiveData("sync_contactos")
            .observeForever { workInfos ->
                val info = workInfos.firstOrNull()
                val estado = info?.state
                Timber.d("Estado actual del Worker: $estado")
            }
    }
}