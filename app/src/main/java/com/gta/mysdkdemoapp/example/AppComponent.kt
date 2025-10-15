package com.gta.mysdkdemoapp.example

import com.gta.mysdkdemoapp.dagger.annotations.Component
import com.gta.mysdkdemoapp.dagger.annotations.Singleton
import com.gta.mysdkdemoapp.test.InjectTest

/**
 * 示例应用组件
 * 定义依赖图的入口点
 */
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    
    // 提供依赖的方法
    fun userRepository(): UserRepository
    fun networkService(): NetworkService
    fun databaseService(): DatabaseService
    
    // 注入方法
    fun inject(activity: com.gta.mysdkdemoapp.MainActivity)
    fun inject(test: com.gta.mysdkdemoapp.test.InjectTest)
    fun inject(demoActivity: com.gta.mysdkdemoapp.example.InjectDemoActivity) // 添加这一行以支持 InjectDemoActivity 注入
}