package com.gta.mysdkdemoapp.example

import com.gta.mysdkdemoapp.dagger.annotations.Inject
import com.gta.mysdkdemoapp.dagger.annotations.Singleton

/**
 * 示例网络服务类
 */
@Singleton
class NetworkService @Inject constructor() {
    
    fun fetchData(): String {
        // 模拟网络请求
        return "从网络获取的数据: Hello from NetworkService"
    }
    
    fun uploadData(data: String): Boolean {
        // 模拟数据上传
        println("上传数据: $data")
        return true
    }
}