# 简化版 Dagger2 依赖注入框架

这是一个简化版的Dagger2实现，用于学习依赖注入框架的基本原理。虽然功能简化，但包含了Dagger2的核心概念。

## 🎯 核心概念

### 1. 依赖注入 (Dependency Injection)
依赖注入是一种设计模式，它将对象的依赖关系由外部容器管理，而不是对象自己创建依赖。

### 2. 控制反转 (Inversion of Control)
传统方式：对象主动创建依赖
```kotlin
class UserRepository {
    private val networkService = NetworkService() // 直接创建依赖
    private val databaseService = DatabaseService()
}
```

依赖注入方式：依赖由外部注入
```kotlin
class UserRepository @Inject constructor(
    private val networkService: NetworkService,  // 依赖注入
    private val databaseService: DatabaseService
)
```

## 📚 核心注解

### @Inject
标记需要依赖注入的构造函数、字段或方法
```kotlin
class NetworkService @Inject constructor() {
    // 构造函数注入
}

class MainActivity {
    @Inject lateinit var userRepository: UserRepository  // 字段注入
}
```

### @Component
定义依赖图的入口点，连接模块和消费者
```kotlin
@Component(modules = [AppModule::class])
interface AppComponent {
    fun userRepository(): UserRepository
    fun inject(activity: MainActivity)
}
```

### @Module
提供复杂依赖的创建逻辑
```kotlin
@Module
class AppModule {
    @Provides
    fun provideAppConfig(): AppConfig {
        return AppConfig("https://api.example.com")
    }
}
```

### @Provides
在Module中标记提供依赖的方法
```kotlin
@Provides
@Singleton
fun provideNetworkService(): NetworkService {
    return NetworkService()
}
```

### @Singleton
标记单例依赖，确保整个应用中只有一个实例
```kotlin
@Singleton
class NetworkService @Inject constructor()
```

## 🏗️ 架构设计

### 1. 注解层 (annotations/)
- 定义所有依赖注入相关的注解
- 使用 `@Retention(AnnotationRetention.RUNTIME)` 确保运行时可访问

### 2. 核心层 (core/)
- `DependencyContainer`: 依赖容器，管理实例创建和缓存
- `ComponentBuilder`: 组件构建器，创建依赖图

### 3. API层
- `SimpleDagger`: 主入口，提供便捷API

## 🔄 工作流程

1. **注册阶段**：扫描@Module注解的类，注册提供者方法
2. **解析阶段**：分析依赖关系，构建依赖图
3. **创建阶段**：根据依赖图创建实例，处理循环依赖
4. **注入阶段**：将依赖注入到目标对象

## 📝 使用示例

### 1. 定义依赖类
```kotlin
@Singleton
class NetworkService @Inject constructor() {
    fun fetchData(): String = "网络数据"
}

class DatabaseService @Inject constructor() {
    fun saveData(data: String): Boolean = true
}

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

### 2. 定义模块
```kotlin
@Module
class AppModule {
    @Provides
    @Singleton
    fun provideAppConfig(): AppConfig {
        return AppConfig("https://api.example.com")
    }
}
```

### 3. 定义组件
```kotlin
@Component(modules = [AppModule::class])
interface AppComponent {
    fun userRepository(): UserRepository
    fun inject(activity: MainActivity)
}
```

### 4. 使用依赖注入
```kotlin
// 创建组件
val appComponent = SimpleDagger.create<AppComponent>()

// 获取依赖
val userRepository = appComponent.userRepository()

// 使用依赖
val userData = userRepository.getUserData()
```

## 🔍 与真实Dagger2的区别

### 简化版特点：
- ✅ 运行时依赖解析（反射）
- ✅ 基本的依赖注入功能
- ✅ 单例支持
- ✅ 模块系统
- ❌ 编译时代码生成
- ❌ 性能优化
- ❌ 复杂的作用域管理
- ❌ 循环依赖检测

### 真实Dagger2特点：
- ✅ 编译时代码生成（注解处理器）
- ✅ 零反射，高性能
- ✅ 完整的作用域系统
- ✅ 循环依赖检测
- ✅ 多级组件依赖
- ✅ 子组件支持

## 🎓 学习价值

通过这个简化版实现，你可以理解：

1. **依赖注入的核心思想**：控制反转，依赖由外部管理
2. **注解驱动开发**：通过注解声明依赖关系
3. **反射机制**：运行时分析类结构和注解
4. **设计模式**：工厂模式、单例模式、代理模式
5. **框架设计思路**：如何设计一个可扩展的依赖注入框架

## 🚀 运行演示

在MainActivity中点击"运行依赖注入演示"按钮，可以看到：
- 依赖自动创建和注入
- 单例实例复用
- 依赖链的正确构建

## 📖 进阶学习

要深入理解Dagger2，建议学习：
1. 注解处理器 (Annotation Processor)
2. 编译时代码生成
3. 作用域管理 (@Scope)
4. 组件依赖 (Component Dependencies)
5. 子组件 (Subcomponent)
6. 多绑定 (Multibinding)

这个简化版为你提供了理解这些高级概念的基础！