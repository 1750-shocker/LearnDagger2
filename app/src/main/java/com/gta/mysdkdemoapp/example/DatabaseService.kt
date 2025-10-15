package com.gta.mysdkdemoapp.example

import com.gta.mysdkdemoapp.dagger.annotations.Inject

/**
 * 示例数据库服务类
 */
class DatabaseService @Inject constructor() {
    
    fun saveData(data: String): Boolean {
        // 模拟数据保存
        println("保存数据到数据库: $data")
        return true
    }
    
    fun loadData(): String {
        // 模拟数据加载
        return "从数据库加载的数据"
    }
}