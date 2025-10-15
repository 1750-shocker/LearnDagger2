package com.gta.mysdkdemoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
        Button(onClick = onClick) {
            Text("Call My SDK")
        }
        
        Button(
            onClick = {
                val demo = DependencyInjectionDemo()
                val result = demo.runDemo()
                setDemoResult(result)
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("运行复杂演示")
        }
        
        Button(
            onClick = {
                val demo = CombinedDemo()
                val result = demo.runBothDemos()
                setDemoResult(result)
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("🎯 运行完整演示（推荐）")
        }
        
        Button(
            onClick = {
                val injectDemo = InjectDemoActivity()
                val result = injectDemo.onCreate()
                setDemoResult(result)
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("💉 字段注入演示（推荐）")
        }
        
        Button(
            onClick = {
                val injectTest = InjectTest()
                val result = injectTest.runAllTests()
                setDemoResult(result)
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("🧪 详细字段注入测试")
        }
        
        Text(
            text = demoResult,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}
