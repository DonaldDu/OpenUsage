package com.dhy.openusage.demo

import com.dhy.openusage.Honor
import com.dhy.openusage.OpenUsageUtil
import com.dhy.openusage.UsingApp
import org.junit.Test
import java.io.File

class OpenUsageTest {
    @Test
    fun test() {
        OpenUsageUtil.enqueue = false
        OpenUsageUtil.storeFile = File.createTempFile("OpenUsage-", ".json")
        OpenUsageUtil.storeFile?.deleteOnExit()
        val app = UsingApp()
        app.appId = "appid"
        val honor = Honor()
        honor.name = "name"
        honor.url = "url"
//        OpenUsageUtil.report(app, listOf(honor))
    }
}