package com.gta.simpledagger.annotations

import kotlin.reflect.KClass

/**
 * 简化版 @Component 注解
 * 用于标记依赖注入组件接口
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Component(
    val modules: Array<KClass<*>> = []
)