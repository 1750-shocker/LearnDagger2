package com.gta.mysdkdemoapp.example

import com.gta.mysdkdemoapp.dagger.annotations.Component
import com.gta.mysdkdemoapp.dagger.annotations.Inject
import com.gta.mysdkdemoapp.dagger.annotations.Module
import com.gta.mysdkdemoapp.dagger.annotations.Provides
import com.gta.mysdkdemoapp.dagger.SimpleDagger

/**
 * 超级简单的例子 - 用做咖啡来理解依赖注入
 */

// 1. 基础材料类（不需要依赖别的东西）
class 咖啡豆 {
    fun 描述() = "优质阿拉比卡咖啡豆"
}

class 牛奶 {
    fun 描述() = "新鲜全脂牛奶"
}

class 糖 {
    fun 描述() = "白砂糖"
}

// 2. 复杂产品类（需要依赖基础材料）
class 咖啡机 @Inject constructor(
    private val 咖啡豆: 咖啡豆
) {
    fun 制作咖啡(): String {
        return "用${咖啡豆.描述()}制作的浓缩咖啡"
    }
}

class 拿铁 @Inject constructor(
    private val 咖啡机: 咖啡机,
    private val 牛奶: 牛奶
) {
    fun 制作(): String {
        val 咖啡 = 咖啡机.制作咖啡()
        return "$咖啡 + ${牛奶.描述()} = 香浓拿铁"
    }
}

// 3. 模块 = 材料供应商
@Module
class 咖啡店材料供应商 {
    
    @Provides
    fun 提供咖啡豆(): 咖啡豆 {
        println("🫘 供应商：准备咖啡豆")
        return 咖啡豆()
    }
    
    @Provides
    fun 提供牛奶(): 牛奶 {
        println("🥛 供应商：准备牛奶")
        return 牛奶()
    }
    
    @Provides
    fun 提供糖(): 糖 {
        println("🍯 供应商：准备糖")
        return 糖()
    }
}

// 4. 组件 = 咖啡店老板（总协调）
@Component(modules = [咖啡店材料供应商::class])
interface 咖啡店老板 {
    // 我可以给你做拿铁
    fun 制作拿铁(): 拿铁
    
    // 我也可以单独提供咖啡机
    fun 提供咖啡机(): 咖啡机
}

// 5. 演示类
class 简单咖啡演示 {
    fun 开始演示(): String {
        println("\n=== 简单咖啡店演示 ===")
        println("客户：我要一杯拿铁")
        
        // 找到咖啡店老板
        val 老板 = SimpleDagger.create<咖啡店老板>()
        println("老板：好的，我来安排制作")
        
        // 老板协调制作
        val 拿铁 = 老板.制作拿铁()
        val 结果 = 拿铁.制作()
        
        println("老板：您的拿铁做好了！")
        println("结果：$结果")
        
        return """
            演示完成！
            
            🎯 关键理解：
            1. 客户只需要说"我要拿铁"
            2. 老板(Component)负责协调整个制作过程
            3. 供应商(Module)负责提供各种材料
            4. 拿铁不用知道咖啡豆从哪来，牛奶怎么准备
            5. 所有依赖都自动注入，完全解耦！
            
            这就是依赖注入的魅力：
            - 拿铁类：专心做拿铁，不管材料从哪来
            - 咖啡机类：专心做咖啡，不管咖啡豆从哪来  
            - 供应商：专门负责提供材料
            - 老板：协调一切，对外提供服务
        """.trimIndent()
    }
}