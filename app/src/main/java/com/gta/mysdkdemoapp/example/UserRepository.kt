package com.gta.mysdkdemoapp.example

import com.gta.mysdkdemoapp.dagger.annotations.Inject

/**
 * 示例用户仓库类
 * 依赖于NetworkService和DatabaseService
 */
class UserRepository @Inject constructor(
    private val networkService: NetworkService,
    private val databaseService: DatabaseService
) {
    
    fun getUserData(): String {
        // 先从网络获取数据
        val networkData = networkService.fetchData()
        
        // 保存到数据库
        databaseService.saveData(networkData)
        
        // 从数据库加载数据
        val dbData = databaseService.loadData()
        
        return "UserRepository: $networkData, $dbData"
    }
    
    fun updateUser(userData: String): Boolean {
        // 上传到网络
        networkService.uploadData(userData)
        
        // 保存到数据库
        return databaseService.saveData(userData)
    }
}