plugins {
    kotlin("jvm")
    kotlin("kapt")
}

kotlin {
    jvmToolchain(11)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    testImplementation(kotlin("test"))
    testImplementation(libs.junit)
}