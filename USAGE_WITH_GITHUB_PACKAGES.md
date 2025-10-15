# 🚀 使用 GitHub Packages 中的 Simple Dagger SDK

## 📋 快速开始

### 1. 添加仓库和依赖

#### settings.gradle.kts
```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/freddywang/MySdkDemoApp")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_USERNAME")
                password = project.findProperty("gpr.token") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
        google()
        mavenCentral()
    }
}
```

#### app/build.gradle.kts
```kotlin
dependencies {
    // 添加 Simple Dagger SDK
    implementation("com.gta:simple-dagger:1.0.0")
    
    // Android 基础依赖
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.material3:material3:1.1.2")
}
```

### 2. 配置认证

#### 方式一：环境变量（推荐）
```bash
# Windows
set GITHUB_USERNAME=你的GitHub用户名
set GITHUB_TOKEN=你的GitHub_Personal_Access_Token

# Linux/Mac
export GITHUB_USERNAME=你的GitHub用户名
export GITHUB_TOKEN=你的GitHub_Personal_Access_Token
```

#### 方式二：gradle.properties
```properties
# 在项目根目录的 gradle.properties 中添加
gpr.user=你的GitHub用户名
gpr.token=你的GitHub_Personal_Access_Token
```

## 💡 完整使用示例

### 项目结构
```
YourApp/
├── app/
│   ├── src/main/java/com/example/yourapp/
│   │   ├── MainActivity.kt
│   │   ├── UserService.kt
│   │   ├── NetworkService.kt
│   │   ├── AppModule.kt
│   │   └── AppComponent.kt
│   └── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

### UserService.kt
```kotlin
package com.example.yourapp

class UserService {
    fun getCurrentUser(): String = "张三"
    fun login(username: String): Boolean {
        println("用户 $username 登录成功")
        return true
    }
}
```

### NetworkService.kt
```kotlin
package com.example.yourapp

class NetworkService {
    fun fetchData(): String = "网络数据获取成功"
    fun uploadFile(filename: String): Boolean {
        println("上传文件: $filename")
        return true
    }
}
```

### AppModule.kt
```kotlin
package com.example.yourapp

import com.gta.simpledagger.annotations.Module
import com.gta.simpledagger.annotations.Provides
import com.gta.simpledagger.annotations.Singleton

@Module
class AppModule {
    
    @Provides
    @Singleton
    fun provideUserService(): UserService = UserService()
    
    @Provides
    @Singleton
    fun provideNetworkService(): NetworkService = NetworkService()
}
```

### AppComponent.kt
```kotlin
package com.example.yourapp

import com.gta.simpledagger.annotations.Component

@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    
    // 提供依赖的方法
    fun getUserService(): UserService
    fun getNetworkService(): NetworkService
}
```

### MainActivity.kt
```kotlin
package com.example.yourapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gta.simpledagger.annotations.Inject
import com.gta.simpledagger.core.DaggerComponentBuilder

class MainActivity : ComponentActivity() {
    
    // 使用 @Inject 注解进行字段注入
    @Inject
    lateinit var userService: UserService
    
    @Inject
    lateinit var networkService: NetworkService
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 创建依赖注入组件
        val appComponent = DaggerComponentBuilder.create<AppComponent>(AppModule::class)
        
        // 注入依赖到当前 Activity
        appComponent.inject(this)
        
        setContent {
            MaterialTheme {
                MainScreen()
            }
        }
    }
    
    @Composable
    private fun MainScreen() {
        var message by remember { mutableStateOf("点击按钮测试依赖注入") }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val currentUser = userService.getCurrentUser()
                                userService.login(currentUser)
                                message = "当前用户: $currentUser"
                            }
                        ) {
                            Text("测试 UserService")
                        }
                        
                        Button(
                            onClick = {
                                val data = networkService.fetchData()
                                networkService.uploadFile("test.txt")
                                message = data
                            }
                        ) {
                            Text("测试 NetworkService")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Button(
                        onClick = {
                            val user = userService.getCurrentUser()
                            val data = networkService.fetchData()
                            message = "用户: $user\n数据: $data"
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("测试所有服务")
                    }
                }
            }
        }
    }
}
```

## 🎯 与原生 Dagger2 对比

### Simple Dagger (我们的 SDK)
```kotlin
// 1. 添加依赖
implementation("com.gta:simple-dagger:1.0.0")

// 2. 创建组件
val component = DaggerComponentBuilder.create<AppComponent>(AppModule::class)

// 3. 注入依赖
component.inject(this)
```

### 原生 Dagger2
```kotlin
// 1. 添加依赖
implementation("com.google.dagger:dagger:2.48")
kapt("com.google.dagger:dagger-compiler:2.48")

// 2. 创建组件（需要编译时生成）
val component = DaggerAppComponent.create()

// 3. 注入依赖
component.inject(this)
```

## ✨ 优势对比

| 特性 | Simple Dagger | 原生 Dagger2 |
|------|---------------|---------------|
| **学习曲线** | 🟢 简单易懂 | 🔴 复杂，需要深入理解 |
| **编译时间** | 🟢 无额外编译 | 🔴 需要注解处理器 |
| **运行时性能** | 🟡 略慢（反射） | 🟢 最快（编译时生成） |
| **调试难度** | 🟢 容易调试 | 🔴 生成代码难调试 |
| **项目大小** | 🟢 轻量级 | 🔴 较重 |
| **功能完整性** | 🟡 基础功能 | 🟢 功能全面 |

## 🚀 开始使用

1. **设置 GitHub 认证**
2. **添加仓库和依赖**
3. **创建模块和组件**
4. **在 Activity 中注入依赖**
5. **享受简单的依赖注入！**

现在你就可以像使用 Dagger2 一样，通过远程依赖使用 Simple Dagger SDK 了！🎉