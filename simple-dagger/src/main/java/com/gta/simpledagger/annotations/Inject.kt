package com.gta.simpledagger.annotations

/**
 * 简化版 @Inject 注解
 * 用于标记需要依赖注入的构造函数或字段
 */
@Target(
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Inject