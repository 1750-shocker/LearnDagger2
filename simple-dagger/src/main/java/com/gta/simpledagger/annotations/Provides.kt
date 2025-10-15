package com.gta.simpledagger.annotations

/**
 * 简化版 @Provides 注解
 * 用于标记模块中的依赖提供方法
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Provides