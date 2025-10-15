package com.gta.simpledagger.core

import com.gta.simpledagger.annotations.Component
import kotlin.reflect.KClass

/**
 * 简化版组件构建器
 * 用于创建和配置依赖注入组件
 */
object ComponentBuilder {
    
    /**
     * 创建组件实例
     */
    fun <T : Any> create(componentInterface: KClass<T>): T {
        val componentAnnotation = componentInterface.java.getAnnotation(Component::class.java)
            ?: throw IllegalArgumentException("${componentInterface.simpleName} 不是一个有效的 @Component")
        
        val container = DependencyContainer()
        
        // 注册所有模块
        componentAnnotation.modules.forEach { moduleClass ->
            container.registerModule(moduleClass)
        }
        
        // 创建组件代理实例
        return createComponentProxy(componentInterface, container)
    }
    
    /**
     * 创建组件代理实例
     * 在真实的Dagger中，这部分是通过注解处理器在编译时生成的
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> createComponentProxy(
        componentInterface: KClass<T>,
        container: DependencyContainer
    ): T {
        return java.lang.reflect.Proxy.newProxyInstance(
            componentInterface.java.classLoader,
            arrayOf(componentInterface.java)
        ) { _, method, args ->
            val returnType = method.returnType.kotlin
            
            when (method.name) {
                "inject" -> {
                    // 处理注入方法
                    if (args == null || args.isEmpty()) {
                        throw IllegalArgumentException("inject 方法需要一个参数")
                    }
                    
                    val targetObject = args[0]
                        ?: throw IllegalArgumentException("inject 方法的参数不能为 null")
                    
                    // 执行字段注入
                    container.injectFields(targetObject)
                    Unit
                }
                else -> {
                    // 处理获取依赖的方法
                    container.getInstance(returnType)
                }
            }
        } as T
    }
}