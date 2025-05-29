plugins {
    kotlin("jvm") version "1.9.25"
    id("org.jetbrains.intellij") version "1.17.0"
}

group = "com.namingPlugin"
version = "1.2"

repositories {
    mavenCentral()
    maven { url = uri("https://maven.aliyun.com/repository/public") }
    maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
    maven { url = uri("https://maven.aliyun.com/repository/jetbrains") }
}

dependencies {
    implementation("com.alibaba:dashscope-sdk-java:2.20.1")
}

intellij {
    version.set("2023.1.1")  // 使用最新版本进行开发和测试
    type.set("IC")
    updateSinceUntilBuild.set(true)
    plugins.set(listOf("com.intellij.java"))
}

tasks {
    patchPluginXml {
        sinceBuild.set("231")      // 支持从IDEA 2024.1开始
        untilBuild.set("251.*")    // 支持到IDEA 2025.1.x所有小版本
    }

    runIde {
        // 增加JVM内存
        jvmArgs("-Xmx2g")
        // 添加调试选项
        jvmArgs("-XX:+UnlockDiagnosticVMOptions")
        // 启用断言
        jvmArgs("-ea")
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
        kotlinOptions.freeCompilerArgs += listOf("-Xjvm-default=all")
    }

    withType<Jar> {
        from("src/main/resources") {
            include("**/*")
            includeEmptyDirs = false
        }
    }

    // 处理资源文件编码
    processResources {
        filteringCharset = "UTF-8"
    }

}