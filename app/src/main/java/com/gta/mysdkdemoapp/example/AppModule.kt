package com.gta.mysdkdemoapp.example

import com.gta.mysdkdemoapp.dagger.annotations.Module
import com.gta.mysdkdemoapp.dagger.annotations.Provides
import com.gta.mysdkdemoapp.dagger.annotations.Singleton

/**
 * 示例应用模块
 * 提供一些复杂的依赖创建逻辑
 */
@Module
class AppModule {
    
    @Provides
    @Singleton
    fun provideAppConfig(): AppConfig {
        return AppConfig(
            apiUrl = "https://api.example.com",
            timeout = 30000,
            retryCount = 3
        )
    }
    
    @Provides
    fun provideLogger(): Logger {
        return Logger("MyApp")
    }
}

/**
 * 应用配置类
 */
data class AppConfig(
    val apiUrl: String,
    val timeout: Long,
    val retryCount: Int
)

/**
 * 日志类
 */
class Logger(private val tag: String) {
    fun log(message: String) {
        println("[$tag] $message")
    }
}