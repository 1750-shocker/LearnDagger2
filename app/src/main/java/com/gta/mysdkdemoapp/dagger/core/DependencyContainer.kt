package com.gta.mysdkdemoapp.dagger.core

import com.gta.mysdkdemoapp.dagger.annotations.Inject
import com.gta.mysdkdemoapp.dagger.annotations.Module
import com.gta.mysdkdemoapp.dagger.annotations.Provides
import com.gta.mysdkdemoapp.dagger.annotations.Singleton
import kotlin.reflect.KClass
import java.lang.reflect.Method

/**
 * 简化版依赖容器
 * 负责管理和提供依赖实例
 */
class DependencyContainer {
    
    // 存储单例实例
    private val singletonInstances = mutableMapOf<KClass<*>, Any>()
    
    // 存储模块实例
    private val modules = mutableMapOf<KClass<*>, Any>()
    
    // 存储提供者方法
    private val providers = mutableMapOf<KClass<*>, Method>()
    
    /**
     * 注册模块
     */
    fun registerModule(moduleClass: KClass<*>) {
        if (!moduleClass.java.isAnnotationPresent(Module::class.java)) {
            throw IllegalArgumentException("${moduleClass.simpleName} 不是一个有效的 @Module")
        }
        
        val moduleInstance = try {
            moduleClass.java.getDeclaredConstructor().newInstance()
        } catch (e: Exception) {
            throw IllegalArgumentException("无法创建模块实例: ${moduleClass.simpleName}", e)
        }
        modules[moduleClass] = moduleInstance
        
        // 扫描 @Provides 方法
        moduleClass.java.declaredMethods.forEach { method ->
            if (method.isAnnotationPresent(Provides::class.java)) {
                val returnType = method.returnType.kotlin
                // 存储提供者方法，以便后续使用
                providers[returnType] = method
            }
        }
    }
    
    /**
     * 获取依赖实例
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getInstance(clazz: KClass<T>): T {
        // 检查是否已有单例实例
        if (singletonInstances.containsKey(clazz)) {
            return singletonInstances[clazz] as T
        }
        
        // 优先检查是否有 @Provides 方法提供此类型
        if (providers.containsKey(clazz)) {
            return createInstanceFromProvider(clazz)
        }
        
        // 尝试通过构造函数注入创建实例
        return createInstanceWithConstructorInjection(clazz)
    }
    
    /**
     * 通过构造函数注入创建实例
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> createInstanceWithConstructorInjection(clazz: KClass<T>): T {
        val constructors = clazz.java.declaredConstructors
        
        // 寻找有 @Inject 注解的构造函数
        val injectConstructor = constructors.find { 
            it.isAnnotationPresent(Inject::class.java) 
        } ?: throw IllegalArgumentException("${clazz.simpleName} 没有带 @Inject 注解的构造函数")
        
        // 获取构造函数参数的依赖
        val parameterTypes = injectConstructor.parameterTypes
        val parameters = parameterTypes.map { paramType ->
            getInstance(paramType.kotlin)
        }
        
        val instance = injectConstructor.newInstance(*parameters.toTypedArray()) as T
        
        // 如果是单例，缓存实例
        if (clazz.java.isAnnotationPresent(Singleton::class.java)) {
            singletonInstances[clazz] = instance
        }
        
        return instance
    }
    
    /**
     * 通过 @Provides 方法创建实例
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> createInstanceFromProvider(clazz: KClass<T>): T {
        val providerMethod = providers[clazz]
            ?: throw IllegalArgumentException("没有找到 ${clazz.simpleName} 的 @Provides 方法")
        
        // 找到对应的模块实例
        val moduleClass = providerMethod.declaringClass.kotlin
        val moduleInstance = modules[moduleClass]
            ?: throw IllegalArgumentException("模块 ${moduleClass.simpleName} 未注册")
        
        // 获取方法参数的依赖
        val parameterTypes = providerMethod.parameterTypes
        val parameters = parameterTypes.map { paramType: Class<*> ->
            getInstance(paramType.kotlin)
        }
        
        // 调用 provider 方法
        val instance = providerMethod.invoke(moduleInstance, *parameters.toTypedArray()) as T
        
        // 检查是否是单例
        if (providerMethod.isAnnotationPresent(Singleton::class.java)) {
            singletonInstances[clazz] = instance
        }
        
        return instance
    }
    
    /**
     * 执行字段注入
     */
    fun injectFields(target: Any) {
        val targetClass = target::class
        
        targetClass.java.declaredFields.forEach { field ->
            if (field.isAnnotationPresent(Inject::class.java)) {
                field.isAccessible = true
                val fieldType = field.type.kotlin
                val dependency = getInstance(fieldType)
                field.set(target, dependency)
            }
        }
    }
}