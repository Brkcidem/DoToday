// Ana build dosyası - Tüm alt projeler/modüller için ortak yapılandırma seçeneklerini içerir
plugins {
    // Android uygulama eklentisi - Android projesi için gerekli temel yapılandırmayı sağlar
    alias(libs.plugins.android.application) apply false
    // Kotlin Android eklentisi - Kotlin dil desteği ve Android ile entegrasyonu sağlar
    alias(libs.plugins.kotlin.android) apply false

    id("androidx.navigation.safeargs.kotlin") version "2.6.0" apply false
}