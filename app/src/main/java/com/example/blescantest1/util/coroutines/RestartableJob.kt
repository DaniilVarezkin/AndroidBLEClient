package com.example.blescantest1.util.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestartableJob(
    private val scope: CoroutineScope
) {
    private var job: Job? = null

    fun restart(block: suspend CoroutineScope.() -> Unit){
        job?.cancel()
        job = scope.launch(block = block)
    }

    fun cancel() {
        job?.cancel()
        job = null
    }
}