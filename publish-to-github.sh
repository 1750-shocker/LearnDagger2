#!/bin/bash

echo "🚀 发布 Simple Dagger SDK 到 GitHub Packages"
echo ""

# 检查是否设置了环境变量
if [ -z "$GITHUB_USERNAME" ]; then
    echo "❌ 错误: 请设置 GITHUB_USERNAME 环境变量"
    echo "示例: export GITHUB_USERNAME=freddywang"
    exit 1
fi

if [ -z "$GITHUB_TOKEN" ]; then
    echo "❌ 错误: 请设置 GITHUB_TOKEN 环境变量"
    echo "请到 GitHub Settings > Developer settings > Personal access tokens 创建 token"
    echo "需要 write:packages 权限"
    exit 1
fi

echo "✅ 环境变量检查通过"
echo "📦 开始构建 SDK..."

# 清理项目
./gradlew clean
if [ $? -ne 0 ]; then
    echo "❌ 清理失败"
    exit 1
fi

# 构建 SDK
./gradlew :simple-dagger:build
if [ $? -ne 0 ]; then
    echo "❌ 构建失败"
    exit 1
fi

echo "✅ 构建成功"
echo "📤 发布到 GitHub Packages..."

# 发布到 GitHub Packages
./gradlew :simple-dagger:publish
if [ $? -ne 0 ]; then
    echo "❌ 发布失败"
    exit 1
fi

echo ""
echo "🎉 发布成功！"
echo ""
echo "📋 使用方法:"
echo "其他项目可以通过以下方式使用你的 SDK:"
echo ""
echo "1. 在 settings.gradle.kts 中添加:"
echo "   repositories {"
echo "       maven {"
echo "           url = uri(\"https://maven.pkg.github.com/$GITHUB_USERNAME/MySdkDemoApp\")"
echo "           credentials {"
echo "               username = \"GitHub用户名\""
echo "               password = \"Personal_Access_Token\""
echo "           }"
echo "       }"
echo "   }"
echo ""
echo "2. 在 app/build.gradle.kts 中添加:"
echo "   dependencies {"
echo "       implementation(\"com.gta:simple-dagger:1.0.0\")"
echo "   }"
echo ""