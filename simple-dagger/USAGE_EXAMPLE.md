# SimpleDagger 使用示例

## 在新项目中使用SimpleDagger SDK

### 1. 添加依赖

#### 方式一：使用本地AAR文件
```gradle
// app/build.gradle.kts
dependencies {
    implementation(files("libs/simple-dagger.aar"))
}
```

#### 方式二：使用模块依赖（开发阶段）
```gradle
// settings.gradle.kts
include(":simple-dagger")

// app/build.gradle.kts
dependencies {
    implementation(project(":simple-dagger"))
}
```

#### 方式三：使用Maven仓库（发布后）
```gradle
// app/build.gradle.kts
dependencies {
    implementation("com.gta:simple-dagger:1.0.0")
}
```

### 2. 完整使用示例

#### 定义业务类
```kotlin
// 数据层
@Singleton
class ApiService @Inject constructor() {
    fun fetchUserData(): String = "用户数据从API获取"
}

class DatabaseService @Inject constructor() {
    fun saveUser(data: String) {
        println("保存用户数据: $data")
    }
}

// 业务层
class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val databaseService: DatabaseService
) {
    fun getUserData(): String {
        val data = apiService.fetchUserData()
        databaseService.saveUser(data)
        return data
    }
}
```

#### 定义模块
```kotlin
@Module
class AppModule {
    @Provides
    @Singleton
    fun provideAppConfig(): AppConfig {
        return AppConfig(
            apiUrl = "https://api.myapp.com",
            timeout = 30000
        )
    }
    
    @Provides
    fun provideLogger(): Logger {
        return Logger("MyApp")
    }
}

data class AppConfig(
    val apiUrl: String,
    val timeout: Long
)

class Logger(private val tag: String) {
    fun log(message: String) {
        println("[$tag] $message")
    }
}
```

#### 定义组件
```kotlin
@Component(modules = [AppModule::class])
interface AppComponent {
    // 提供依赖的方法
    fun userRepository(): UserRepository
    fun apiService(): ApiService
    fun logger(): Logger
    
    // 注入方法
    fun inject(activity: MainActivity)
    fun inject(fragment: UserFragment)
}
```

#### 在Application中初始化
```kotlin
class MyApplication : Application() {
    
    // 全局组件实例
    val appComponent: AppComponent by lazy {
        SimpleDagger.create<AppComponent>()
    }
    
    override fun onCreate() {
        super.onCreate()
    }
}
```

#### 在Activity中使用
```kotlin
class MainActivity : AppCompatActivity() {
    
    // 字段注入方式
    @Inject lateinit var userRepository: UserRepository
    @Inject lateinit var logger: Logger
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // 执行依赖注入
        (application as MyApplication).appComponent.inject(this)
        
        // 使用注入的依赖
        logger.log("MainActivity created")
        val userData = userRepository.getUserData()
        
        // 或者主动获取依赖
        val apiService = (application as MyApplication).appComponent.apiService()
    }
}
```

#### 在Fragment中使用
```kotlin
class UserFragment : Fragment() {
    
    @Inject lateinit var userRepository: UserRepository
    
    override fun onAttach(context: Context) {
        super.onAttach(context)
        
        // 从Application获取组件并注入
        val app = requireActivity().application as MyApplication
        app.appComponent.inject(this)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // 使用注入的依赖
        val userData = userRepository.getUserData()
        // 更新UI...
    }
}
```

### 3. 测试支持

#### 创建测试模块
```kotlin
@Module
class TestAppModule {
    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return MockApiService() // 返回Mock实现
    }
}

@Component(modules = [TestAppModule::class])
interface TestAppComponent : AppComponent
```

#### 单元测试
```kotlin
class UserRepositoryTest {
    
    @Test
    fun testGetUserData() {
        // 创建测试组件
        val testComponent = SimpleDagger.create<TestAppComponent>()
        val userRepository = testComponent.userRepository()
        
        // 执行测试
        val result = userRepository.getUserData()
        assertEquals("Mock数据", result)
    }
}
```

### 4. 最佳实践

#### 组织结构建议
```
com.yourapp/
├── di/                    # 依赖注入相关
│   ├── component/
│   │   ├── AppComponent.kt
│   │   └── TestComponent.kt
│   └── module/
│       ├── AppModule.kt
│       ├── NetworkModule.kt
│       └── DatabaseModule.kt
├── data/                  # 数据层
│   ├── api/
│   ├── database/
│   └── repository/
├── domain/                # 业务层
│   ├── usecase/
│   └── model/
└── presentation/          # 表现层
    ├── ui/
    └── viewmodel/
```

#### 依赖注入原则
1. **优先构造函数注入**，避免字段注入
2. **依赖抽象接口**，而非具体实现
3. **合理使用单例**，避免内存泄漏
4. **模块化组织**，按功能分组
5. **测试友好**，提供Mock实现

这样，其他开发者就可以轻松在自己的项目中使用SimpleDagger了！