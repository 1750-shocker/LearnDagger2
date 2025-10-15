package com.gta.mysdkdemoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Alignment
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gta.mysdk.MySdk
import com.gta.mysdkdemoapp.ui.theme.MySdkDemoAppTheme
import com.gta.mysdkdemoapp.example.DependencyInjectionDemo
import com.gta.mysdkdemoapp.example.CombinedDemo
import com.gta.mysdkdemoapp.example.InjectDemoActivity
import com.gta.mysdkdemoapp.test.InjectTest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MySdkDemoAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        onClick = {
                            // 点击按钮时调用 SDK
                            MySdk.showHello(this@MainActivity)
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Greeting(onClick:()->Unit, modifier: Modifier = Modifier) {
    val (demoResult, setDemoResult) = remember { mutableStateOf("点击按钮开始依赖注入演示") }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 标题
        Text(
            text = "MySdkDemoApp",
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        // 按钮区域 - 瀑布式排列
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onClick,
                modifier = Modifier.padding(4.dp)
            ) {
                Text("Call My SDK")
            }
            
            Button(
                onClick = {
                    val demo = DependencyInjectionDemo()
                    val result = demo.runDemo()
                    setDemoResult(result)
                },
                modifier = Modifier.padding(4.dp)
            ) {
                Text("运行复杂演示")
            }
            
            Button(
                onClick = {
                    val demo = CombinedDemo()
                    val result = demo.runBothDemos()
                    setDemoResult(result)
                },
                modifier = Modifier.padding(4.dp)
            ) {
                Text("🎯 运行完整演示")
            }
            
            Button(
                onClick = {
                    val injectDemo = InjectDemoActivity()
                    val result = injectDemo.onCreate()
                    setDemoResult(result)
                },
                modifier = Modifier.padding(4.dp)
            ) {
                Text("💉 字段注入演示")
            }
            
            Button(
                onClick = {
                    val injectTest = InjectTest()
                    val result = injectTest.runAllTests()
                    setDemoResult(result)
                },
                modifier = Modifier.padding(4.dp)
            ) {
                Text("🧪 详细测试")
            }
        }
        
        // 结果显示区域
        Text(
            text = demoResult,
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
        )
    }
}