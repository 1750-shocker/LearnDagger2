package com.gta.simpledagger.annotations

/**
 * 简化版 @Module 注解
 * 用于标记依赖提供模块类
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Module