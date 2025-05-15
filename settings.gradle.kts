// Eklenti yönetimi ayarları
pluginManagement {
    repositories {
        // Google'ın Maven deposu - Android ve Google eklentilerini içerir
        google {
            content {
                // Android ile ilgili tüm grupları dahil et
                includeGroupByRegex("com\\.android.*")
                // Google ile ilgili tüm grupları dahil et
                includeGroupByRegex("com\\.google.*")
                // AndroidX kütüphanelerini dahil et
                includeGroupByRegex("androidx.*")
            }
        }
        // Maven Central deposu - açık kaynak kütüphaneleri içerir
        mavenCentral()
        // Gradle eklenti portalı
        gradlePluginPortal()
    }
}

// Bağımlılık çözümleme yönetimi
dependencyResolutionManagement {
    // Proje seviyesindeki repository tanımlamalarını kullanma
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // Google ve Maven Central repository'lerini ekle
        google()
        mavenCentral()
    }
}

// Proje adını belirle
rootProject.name = "Do Today"
// Uygulama modülünü projeye dahil et
include(":app")
