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

        def app = new UsingApp()
        app.appId = project.extensions.android.defaultConfig.applicationId
        app.name = rootProject.findProperty('OPEN_USAGE_APP_NAME')
        app.url = rootProject.findProperty('OPEN_USAGE_APP_URL')
        OpenUsageUtil.report(app, honors)
    }
}