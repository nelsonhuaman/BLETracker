package com.danp.bletracker.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.danp.bletracker.domain.usecase.SincronizarContactosUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class SincronizarContactosWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val sincronizarContactosUseCase: SincronizarContactosUseCase
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {
            sincronizarContactosUseCase()
            Timber.d("Sincronización completada")
            Result.success()
        } catch (e: Exception) {
            Timber.e(e, "Fallo al sincronizar contactos")
            Result.retry()
        }
    }
}