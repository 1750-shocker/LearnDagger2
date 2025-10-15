package com.gta.simpledagger

import com.gta.simpledagger.core.ComponentBuilder
import kotlin.reflect.KClass

/**
 * 简化版Dagger主入口
 * 提供便捷的API来使用依赖注入框架
 */
object SimpleDagger {
    
    /**
     * 创建组件
     */
    fun <T : Any> create(componentClass: KClass<T>): T {
        return ComponentBuilder.create(componentClass)
    }
    
    /**
     * 创建组件（内联版本）
     */
    inline fun <reified T : Any> create(): T {
        return create(T::class)
    }
}