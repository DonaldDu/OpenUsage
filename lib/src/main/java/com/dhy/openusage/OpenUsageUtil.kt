package com.dhy.openusage

import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.util.*

object OpenUsageUtil {
    var enqueue = true
    var useBuffer = true
    var storeFile: File? = null

    private const val url = "https://openusage.avosapps.us/report"

    //    private const val url = "http://localhost:3000/report"
    private val usages: MutableMap<UsingApp, MutableList<Honor>> = mutableMapOf()
    private val gson by lazy { Gson() }

    @JvmStatic
    fun report(app: UsingApp, honors: List<Honor>) {
        val _honors = usages.getOrPut(app) { honors.toMutableList() }
        if (!_honors.containsAll(honors)) {
            _honors.removeAll(honors)
            _honors.addAll(honors)
        }
        delayReport()
    }

    private fun delayReport() {
        val reports = usages.map {
            val report = Report()
            report.appId = it.key.appId
            report.name = it.key.name
            report.url = it.key.url
            report.honors = it.value
            report.honors.sortBy { i -> i.url }
            report
        }
        reports.sortedBy { it.appId }
        val stored = if (storeFile != null && storeFile!!.exists() && storeFile!!.length() > 0) {
            val json = storeFile!!.readText()
            gson.fromJson(json, Array<Report>::class.java).toList()
        } else emptyList()

        if ((useBuffer && stored.containsAll(reports)) || reports.isEmpty()) return
        if (enqueue) delayReport(reports)
        else report(reports)
    }

    private var timer: Timer? = null
    private fun delayReport(reports: List<Report>) {
        timer?.cancel()
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                report(reports)
            }
        }, 15 * 1000)
        this.timer = timer
    }

    private val httpClient by lazy { OkHttpClient() }
    private fun report(reports: List<Report>) {
        reports.forEach {
            val json = gson.toJson(it)
            val request = Request.Builder()
                .url(url)
                .post(json.toRequestBody("application/json".toMediaType()))
                .build()
            if (enqueue) httpClient.newCall(request).enqueue(callback)
            else {
                val call = httpClient.newCall(request)
                try {
                    val response = call.execute()
                    callback.onResponse(call, response)
                } catch (e: IOException) {
                    callback.onFailure(call, e)
                }
            }
        }
        diskBuffer = gson.toJson(reports)
    }

    private val callback = object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            println("onFailure:  ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            println("onResponse: ${response.code} => ${response.message}")
            if (response.isSuccessful) saveToDisk()
        }
    }
    private var diskBuffer: String? = null
    private fun saveToDisk() {
        if (storeFile != null && diskBuffer != null) {
            FileUtils.writeByteArrayToFile(storeFile, diskBuffer!!.toByteArray())
        }
    }
}