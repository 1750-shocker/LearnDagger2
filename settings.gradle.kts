import org.gradle.kotlin.dsl.flatDir
import org.gradle.kotlin.dsl.repositories

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // 👇 关键：添加 flatDir 仓库（用于本地 AAR）
        flatDir {
            dirs("app/libs")
        }
    }
}

rootProject.name = "MySdkDemoApp"
include(":app")
 