package com.gta.mysdkdemoapp.example

import com.gta.mysdkdemoapp.dagger.SimpleDagger
import com.gta.mysdkdemoapp.explanation.逐步解释演示
import com.gta.mysdkdemoapp.explanation.核心实现解释

/**
 * 组合演示类 - 包含简单和复杂两个例子
 */
class CombinedDemo {
    
    fun runBothDemos(): String {
        return try {
            val 简单演示结果 = runSimpleDemo()
            val 复杂演示结果 = runComplexDemo()
            val 逐步解释结果 = run逐步解释Demo()
            val 核心实现解释结果 = run核心实现解释()
            
            """
                🎯 完整演示都完成了！
                
                === 📍 简单演示（咖啡店例子） ===
                $简单演示结果
                
                === 📍 复杂演示（用户系统例子） ===  
                $复杂演示结果
                
                === 📍 逐步解释（为什么要这样写） ===
                $逐步解释结果
                
                === 📍 核心实现解释（底层原理） ===
                $核心实现解释结果
            """.trimIndent()
            
        } catch (e: Exception) {
            "演示出错: ${e.message}"
        }
    }
    
    private fun runSimpleDemo(): String {
        val 简单演示 = 简单咖啡演示()
        return 简单演示.开始演示()
    }
    
    private fun runComplexDemo(): String {
        // 创建应用组件
        val appComponent = SimpleDagger.create<AppComponent>()
        
        // 获取UserRepository实例
        val userRepository = appComponent.userRepository()
        
        // 使用UserRepository
        val userData = userRepository.getUserData()
        
        // 更新用户数据
        userRepository.updateUser("新的用户数据")
        
        return """
            复杂系统演示完成！
            
            📊 获取到的数据: $userData
            
            🔗 依赖链展示：
            MainActivity → UserRepository → NetworkService + DatabaseService
            
            ✨ 关键点：
            - UserRepository不知道NetworkService怎么来的
            - NetworkService不知道自己是单例
            - 所有依赖都由Component自动协调
            - 完全符合"依赖倒置"原则！
        """.trimIndent()
    }
    
    private fun run逐步解释Demo(): String {
        val 演示 = 逐步解释演示()
        return 演示.运行演示()
    }
    
    private fun run核心实现解释(): String {
        val 解释 = 核心实现解释()
        return 解释.解释为什么这样写()
    }
}