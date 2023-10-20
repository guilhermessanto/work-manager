package br.com.dio.work.manager.worker

import android.content.Context
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import br.com.dio.work.manager.data.datasource.VideosDataSource
import br.com.dio.work.manager.ui.extensions.showBigPictureNotification
import java.util.concurrent.TimeUnit

class NotificationWorker(
    private val context: Context, workerParams: WorkerParameters
) : Worker(context, workerParams) {
    override fun doWork(): Result {
        Log.i(TAG, "doWork")
        val video = VideosDataSource.getRandomVideo()
        context.showBigPictureNotification(video)
        return Result.success()
    }

    companion object {
        private const val TAG = "notificationWorker"
        private const val WORKER_NAME = "worker_name"

        fun start(context: Context) {
            Log.i(TAG, "starting the worker")
            val workRequest = createRequest()
            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    WORKER_NAME,
                    ExistingWorkPolicy.KEEP,
                    workRequest
                )
        }

        private fun createRequest() =
            OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(2, TimeUnit.MINUTES)
                .build()

        fun startPeriodic(context: Context) {
            val workRequest = createPeriodicRequest()

            Log.i(TAG, "starting the worker with period")
            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    WORKER_NAME,
                    ExistingPeriodicWorkPolicy.KEEP,
                    workRequest
                )

        }

        private fun createPeriodicRequest() =
            PeriodicWorkRequestBuilder<NotificationWorker>(
                1,
                TimeUnit.MINUTES
            ).build()

    }

}


