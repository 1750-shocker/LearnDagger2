package com.gta.mysdkdemoapp.example

import com.gta.mysdkdemoapp.dagger.SimpleDagger

/**
 * 依赖注入演示类
 * 展示如何使用简化版Dagger2
 */
class DependencyInjectionDemo {
    
    fun runDemo(): String {
        try {
            // 创建应用组件
            val appComponent = SimpleDagger.create<AppComponent>()
            
            // 获取UserRepository实例
            val userRepository = appComponent.userRepository()
            
            // 使用UserRepository
            val userData = userRepository.getUserData()
            
            // 更新用户数据
            userRepository.updateUser("新的用户数据")
            
            return """
                依赖注入演示完成！
                
                获取到的数据:
                $userData
                
                依赖关系:
                - UserRepository 依赖 NetworkService 和 DatabaseService
                - NetworkService 是单例
                - 所有依赖都通过构造函数注入
                
                这展示了依赖注入的核心概念:
                1. 控制反转 (IoC)
                2. 依赖注入 (DI)
                3. 单例管理
                4. 模块化配置
            """.trimIndent()
            
        } catch (e: Exception) {
            return "依赖注入演示出错: ${e.message}\n${e.stackTrace.joinToString("\n")}"
        }
    }
}