package com.gta.mysdkdemoapp.example

import com.gta.mysdkdemoapp.dagger.SimpleDagger
import com.gta.mysdkdemoapp.dagger.annotations.Inject

/**
 * 演示字段注入功能的示例类
 * 模拟一个Activity使用字段注入
 */
class InjectDemoActivity {
    
    // 使用@Inject注解标记需要注入的字段
    @Inject
    lateinit var userRepository: UserRepository
    
    @Inject
    lateinit var networkService: NetworkService
    
    @Inject
    lateinit var databaseService: DatabaseService
    
    // 没有@Inject注解的字段，不会被注入
    private var activityName: String = "InjectDemoActivity"
    
    /**
     * 模拟Activity的onCreate方法
     */
    fun onCreate(): String {
        val result = StringBuilder()
        result.append("📱 模拟Activity生命周期\n")
        result.append("=".repeat(40) + "\n")
        
        try {
            // 1. 创建Component（通常在Application中创建）
            result.append("1️⃣ 创建AppComponent\n")
            val appComponent = SimpleDagger.create<AppComponent>()
            result.append("✅ AppComponent创建成功\n\n")
            
            // 2. 检查注入前的状态
            result.append("2️⃣ 注入前状态检查\n")
            result.append("- userRepository 已初始化: ${::userRepository.isInitialized}\n")
            result.append("- networkService 已初始化: ${::networkService.isInitialized}\n")
            result.append("- databaseService 已初始化: ${::databaseService.isInitialized}\n")
            result.append("- activityName: $activityName\n\n")
            
            // 3. 执行依赖注入
            result.append("3️⃣ 执行依赖注入\n")
            result.append("调用: appComponent.inject(this)\n")
            appComponent.inject(this)
            result.append("✅ 注入完成\n\n")
            
            // 4. 检查注入后的状态
            result.append("4️⃣ 注入后状态检查\n")
            result.append("- userRepository 已初始化: ${::userRepository.isInitialized}\n")
            result.append("- networkService 已初始化: ${::networkService.isInitialized}\n")  
            result.append("- databaseService 已初始化: ${::databaseService.isInitialized}\n")
            result.append("- activityName: $activityName (未改变，正确)\n\n")
            
            // 5. 使用注入的依赖
            result.append("5️⃣ 使用注入的依赖\n")
            if (::userRepository.isInitialized) {
                val userData = userRepository.getUserData()
                result.append("📊 获取用户数据: $userData\n")
                
                userRepository.updateUser("新的用户信息")
                result.append("💾 更新用户数据: 成功\n")
            }
            
            // 6. 验证依赖链
            result.append("\n6️⃣ 验证依赖链\n")
            result.append("UserRepository 内部使用的服务:\n")
            result.append("- NetworkService: ${if (::networkService.isInitialized) "✅ 可用" else "❌ 不可用"}\n")
            result.append("- DatabaseService: ${if (::databaseService.isInitialized) "✅ 可用" else "❌ 不可用"}\n")
            
            // 7. 验证单例特性
            result.append("\n7️⃣ 验证单例特性\n")
            val networkService1 = appComponent.networkService()
            val networkService2 = appComponent.networkService()
            val isSingleton = networkService1 === networkService2
            result.append("NetworkService单例测试: ${if (isSingleton) "✅ 是单例" else "❌ 不是单例"}\n")
            result.append("注入的实例与直接获取的实例相同: ${if (networkService === networkService1) "✅ 相同" else "❌ 不同"}\n")
            
            result.append("\n🎉 字段注入演示完成！\n")
            result.append("=".repeat(40))
            
        } catch (e: Exception) {
            result.append("❌ 演示过程中出错: ${e.message}\n")
            result.append("错误详情:\n${e.stackTrace.joinToString("\n")}")
        }
        
        return result.toString()
    }
    
    /**
     * 获取当前Activity的状态信息
     */
    fun getStatusInfo(): String {
        return """
            📋 Activity状态信息:
            - Activity名称: $activityName
            - UserRepository: ${if (::userRepository.isInitialized) "已注入" else "未注入"}
            - NetworkService: ${if (::networkService.isInitialized) "已注入" else "未注入"}
            - DatabaseService: ${if (::databaseService.isInitialized) "已注入" else "未注入"}
        """.trimIndent()
    }
}