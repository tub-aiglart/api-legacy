import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.21"
}

group = "com.tub-aiglart"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    compile("org.mongodb:mongodb-driver-sync:3.10.1")
    compile("com.electronwill.night-config:yaml:3.5.2")
    compile("io.javalin", "javalin", "2.8.0")
    compile ("org.slf4j:slf4j-simple:1.7.26")
    implementation(kotlin("stdlib-jdk8"))
    implementation ("com.github.Carleslc:Simple-YAML:1.4.1")
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
