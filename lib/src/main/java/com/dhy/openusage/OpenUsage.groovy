package com.dhy.openusage

import org.gradle.api.Project

class OpenUsage {
    static void report(Project project, String honorName, String honorUrl) {
        def honor = new Honor()
        honor.name = honorName
        honor.url = honorUrl
        report(project, Arrays.asList(honor))
    }

    static void report(Project project, List<Honor> honors) {
        def rootProject = project.rootProject
        OpenUsageUtil.INSTANCE.storeFile = rootProject.file("./.idea/caches/OpenUsage.json")
        OpenUsageUtil.INSTANCE.useBuffer = rootProject.findProperty('OPEN_USAGE_USE_BUFFER') != 'false'
        OpenUsageUtil.INSTANCE.log = rootProject.findProperty('OPEN_USAGE_LOG') == 'true'
        OpenUsageUtil.INSTANCE.newUrl = rootProject.findProperty('OPEN_USAGE_URL')
        def delayTime = rootProject.findProperty('OPEN_USAGE_DELAY_TIME_MS')
        if (delayTime != null) {
            try {
                OpenUsageUtil.INSTANCE.delayTime = delayTime.toString().toLong()
            } catch (ignored) {
            }
        }
        def app = new UsingApp()
        app.appId = project.extensions.android.defaultConfig.applicationId
        app.name = rootProject.findProperty('OPEN_USAGE_APP_NAME')
        app.url = rootProject.findProperty('OPEN_USAGE_APP_URL')
        OpenUsageUtil.report(app, honors)
    }
}