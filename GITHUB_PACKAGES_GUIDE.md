# 📦 GitHub Packages 发布和使用指南

## 🚀 发布 SDK 到 GitHub Packages

### 1. 准备工作

#### 创建 GitHub Personal Access Token
1. 访问 GitHub Settings > Developer settings > Personal access tokens > Tokens (classic)
2. 点击 "Generate new token"
3. 选择权限：
   - ✅ `write:packages` - 发布包到 GitHub Packages
   - ✅ `read:packages` - 读取包（如果仓库是私有的）
   - ✅ `repo` - 访问仓库（如果仓库是私有的）
4. 复制生成的 token（只显示一次！）

#### 设置环境变量

**Windows:**
```cmd
set GITHUB_USERNAME=你的GitHub用户名
set GITHUB_TOKEN=你的Personal_Access_Token
```

**Linux/Mac:**
```bash
export GITHUB_USERNAME=你的GitHub用户名
export GITHUB_TOKEN=你的Personal_Access_Token
```

### 2. 发布方式

#### 方式一：使用脚本发布（推荐）

**Windows:**
```cmd
publish-to-github.bat
```

**Linux/Mac:**
```bash
chmod +x publish-to-github.sh
./publish-to-github.sh
```

#### 方式二：手动发布
```bash
# 1. 清理项目
./gradlew clean

# 2. 构建 SDK
./gradlew :simple-dagger:build

# 3. 发布到 GitHub Packages
./gradlew :simple-dagger:publish
```

#### 方式三：通过 Git 标签自动发布
```bash
# 创建并推送标签，会自动触发 GitHub Actions
git tag v1.0.0
git push origin v1.0.0
```

## 📥 其他项目如何使用你的 SDK

### 1. 配置仓库访问

在其他项目的 `settings.gradle.kts` 中添加：

```kotlin
dependencyResolutionManagement {
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

### 2. 添加依赖

在 `app/build.gradle.kts` 中添加：

```kotlin
dependencies {
    implementation("com.gta:simple-dagger:1.0.0")
    
    // 其他依赖...
    implementation("androidx.core:core-ktx:1.12.0")
}
```

### 3. 配置认证信息

#### 方式一：环境变量（推荐）
```bash
# 使用者需要设置这些环境变量
export GITHUB_USERNAME=使用者的GitHub用户名
export GITHUB_TOKEN=使用者的Personal_Access_Token
```

#### 方式二：gradle.properties
在项目根目录的 `gradle.properties` 中添加：
```properties
gpr.user=使用者的GitHub用户名
gpr.token=使用者的Personal_Access_Token
```

### 4. 使用示例

```kotlin
// UserService.kt
class UserService {
    fun getUserName(): String = "John Doe"
    fun doSomething() {
        println("UserService is working!")
    }
}

// AppModule.kt
import com.gta.simpledagger.annotations.*

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideUserService(): UserService = UserService()
}

// AppComponent.kt
import com.gta.simpledagger.annotations.Component

@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun getUserService(): UserService
}

// MainActivity.kt
import com.gta.simpledagger.annotations.Inject
import com.gta.simpledagger.core.DaggerComponentBuilder

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var userService: UserService
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 创建组件并注入
        val component = DaggerComponentBuilder.create<AppComponent>(AppModule::class)
        component.inject(this)
        
        // 使用注入的依赖
        userService.doSomething()
        println("用户名: ${userService.getUserName()}")
    }
}
```

## 🔧 版本管理

### 发布新版本
1. 修改 `simple-dagger/build.gradle.kts` 中的版本号：
   ```kotlin
   version = "1.1.0"  // 更新版本
   ```

2. 使用新版本标签：
   ```bash
   git tag v1.1.0
   git push origin v1.1.0
   ```

### 使用特定版本
```kotlin
dependencies {
    implementation("com.gta:simple-dagger:1.0.0")  // 使用 1.0.0
    implementation("com.gta:simple-dagger:1.1.0")  // 使用 1.1.0
}
```

## 📊 GitHub Packages 特点

### 优势
- ✅ **与 GitHub 深度集成**：代码和包在同一个地方
- ✅ **私有仓库支持**：可以发布私有包
- ✅ **访问控制**：基于 GitHub 的权限系统
- ✅ **版本管理**：完整的版本历史和标签支持

### 限制
- 🔴 **需要认证**：使用者需要 GitHub token
- 🔴 **存储限制**：每月有免费额度限制
- 🔴 **网络要求**：需要访问 GitHub

## 🎯 最佳实践

1. **版本语义化**：使用 `v1.0.0`, `v1.1.0` 格式
2. **文档完善**：提供清晰的使用说明
3. **自动化发布**：使用 GitHub Actions 自动发布
4. **测试充分**：发布前确保 SDK 功能正常
5. **向后兼容**：尽量保持 API 的向后兼容性

## 🆘 常见问题

### Q: 发布失败，提示权限错误？
A: 检查 Personal Access Token 是否有 `write:packages` 权限

### Q: 其他人无法下载包？
A: 确保他们有正确的 GitHub token 和仓库访问权限

### Q: 如何删除已发布的版本？
A: 到 GitHub 仓库的 Packages 页面手动删除

### Q: 可以发布到多个仓库吗？
A: 可以，修改 `build.gradle.kts` 中的 URL 即可

现在你的 Simple Dagger SDK 就可以像真正的 Dagger2 一样通过远程依赖使用了！🚀