package com.gta.mysdkdemoapp.dagger.annotations

/**
 * 简化版 @Module 注解
 * 用于标记提供依赖的模块类
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Module