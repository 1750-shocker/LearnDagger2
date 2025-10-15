# SimpleDagger - 轻量级依赖注入框架

SimpleDagger是一个轻量级的依赖注入框架，inspired by Dagger2，但更简单易用。

## 特性

✅ **构造函数注入** - 使用@Inject标记构造函数  
✅ **字段注入** - 使用@Inject标记字段  
✅ **模块化提供** - 使用@Module和@Provides  
✅ **单例管理** - 使用@Singleton注解  
✅ **组件管理** - 使用@Component定义依赖图  
✅ **类型安全** - 编译时和运行时类型检查  
✅ **零配置** - 无需复杂的配置文件  

## 快速开始

### 1. 添加依赖

```gradle
dependencies {
    implementation 'com.gta:simple-dagger:1.0.0'
}
```

### 2. 定义依赖类

```kotlin
// 服务类
class NetworkService @Inject constructor() {
    fun fetchData(): String = "网络数据"
}

class DatabaseService @Inject constructor() {
    fun saveData(data: String) = println("保存: $data")
}

// 业务类
class UserRepository @Inject constructor(
    private val networkService: NetworkService,
    private val databaseService: DatabaseService
) {
    fun getUserData(): String {
        val data = networkService.fetchData()
        databaseService.saveData(data)
        return data
    }
}
```

### 3. 定义模块（可选）

```kotlin
@Module
class AppModule {
    @Provides
    @Singleton
    fun provideAppConfig(): AppConfig {
        return AppConfig(apiUrl = "https://api.example.com")
    }
}
```

### 4. 定义组件

```kotlin
@Component(modules = [AppModule::class])
interface AppComponent {
    fun userRepository(): UserRepository
    fun inject(activity: MainActivity)
}
```

### 5. 使用依赖注入

```kotlin
class MainActivity : AppCompatActivity() {
    @Inject lateinit var userRepository: UserRepository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 创建组件并执行注入
        val appComponent = SimpleDagger.create<AppComponent>()
        appComponent.inject(this)
        
        // 现在可以使用注入的依赖
        val userData = userRepository.getUserData()
    }
}
```

## 注解说明

### @Inject
标记构造函数或字段需要依赖注入

```kotlin
class Service @Inject constructor(dependency: Dependency)

class Activity {
    @Inject lateinit var service: Service
}
```

### @Module
标记模块类，提供复杂依赖的创建逻辑

```kotlin
@Module
class NetworkModule {
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient()
}
```

### @Provides
标记模块中的依赖提供方法

```kotlin
@Provides
@Singleton
fun provideDatabase(): Database = Room.database(...)
```

### @Singleton
标记单例依赖，整个应用生命周期只创建一次

```kotlin
@Singleton
class ApiService @Inject constructor()
```

### @Component
定义依赖注入组件，连接模块和使用方

```kotlin
@Component(modules = [NetworkModule::class, DatabaseModule::class])
interface AppComponent {
    fun apiService(): ApiService
    fun inject(activity: MainActivity)
}
```

## 与Dagger2对比

| 特性 | SimpleDagger | Dagger2 |
|------|-------------|---------|
| 学习成本 | 低 | 高 |
| 配置复杂度 | 简单 | 复杂 |
| 编译时生成 | 否 | 是 |
| 运行时性能 | 良好 | 优秀 |
| 包大小 | 小 | 大 |
| 适用场景 | 中小型项目 | 大型项目 |

## 最佳实践

1. **优先使用构造函数注入**，避免字段注入
2. **合理使用@Singleton**，避免内存泄漏
3. **模块化组织依赖**，提高可维护性
4. **接口分离**，依赖抽象而非具体实现

## 示例项目

查看完整示例：[MySdkDemoApp](../app/)

## 许可证

MIT License - 详见 [LICENSE](LICENSE) 文件