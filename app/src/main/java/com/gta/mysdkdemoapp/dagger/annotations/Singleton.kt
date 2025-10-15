package com.gta.mysdkdemoapp.dagger.annotations

/**
 * 简化版 @Singleton 注解
 * 用于标记单例依赖
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Singleton