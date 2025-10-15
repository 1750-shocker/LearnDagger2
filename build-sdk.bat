@echo off
chcp 65001 >nul

REM SimpleDagger SDK 构建脚本 (Windows版)

echo 🚀 开始构建 SimpleDagger SDK...

REM 清理之前的构建
echo 🧹 清理之前的构建...
call gradlew clean

REM 构建SDK模块
echo 🔨 构建SDK模块...
call gradlew :simple-dagger:build

REM 检查构建是否成功
if %ERRORLEVEL% EQU 0 (
    echo ✅ SDK构建成功！
    
    REM 复制AAR文件到输出目录
    echo 📦 复制AAR文件...
    if not exist output mkdir output
    copy "simple-dagger\build\outputs\aar\simple-dagger-release.aar" "output\"
    
    REM 生成Maven发布  
    echo 📚 发布到本地Maven仓库...
    call gradlew :simple-dagger:publishToMavenLocal
    
    echo 🎉 SDK构建完成！
    echo.
    echo 输出文件：
    echo   📄 AAR文件: output\simple-dagger-release.aar
    echo   📚 本地Maven: %USERPROFILE%\.m2\repository\com\gta\simple-dagger\1.0.0\
    echo.
    echo 使用方法：
    echo 1. 复制AAR文件到目标项目的libs目录
    echo 2. 在build.gradle中添加: implementation^(files^('libs/simple-dagger-release.aar'^)^)
    echo 3. 或使用Maven坐标: implementation^('com.gta:simple-dagger:1.0.0'^)
    
) else (
    echo ❌ SDK构建失败！
    exit /b 1
)

pause