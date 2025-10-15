package com.gta.mysdkdemoapp.dagger.annotations

/**
 * 简化版 @Provides 注解
 * 用于标记在Module中提供依赖的方法
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Provides