package com.gta.mysdkdemoapp.dagger.annotations

/**
 * 简化版 @Inject 注解
 * 用于标记需要依赖注入的构造函数、字段或方法
 */
@Target(
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Inject