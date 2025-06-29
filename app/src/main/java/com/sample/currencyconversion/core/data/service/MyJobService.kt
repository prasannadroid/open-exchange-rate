package com.sample.currencyconversion.core.data.service

import android.annotation.SuppressLint
import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MyJobService : JobService() {

    private val jobRunner: ExchangeRateJobRunner by inject()

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("MissingPermission") // Required for network access (if applicable)
    override fun onStartJob(params: JobParameters): Boolean {
        // Perform your background task here (e.g., network call)

        CoroutineScope(Dispatchers.IO).launch {
            jobRunner.runJob()
            jobFinished(params, false)
        }

        Log.d(
            "MyJobService",
            "Background task started!"
        )
        // Job finished
        jobFinished(params, false) // Set needsReschedule to false as it's a periodic job

        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        // If the job was cancelled before finishing, perform any cleanup here
        return false
    }

}
