/*
 * API - A basic REST API made for tub-aiglart.com
 *
 * Copyright (C) 2019  Oskar Lang
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/.
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.21"
}

group = "com.tub-aiglart"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compile("io.javalin:javalin:2.8.0")
    compile("com.datastax.cassandra:cassandra-driver-core:3.6.0")
    compile("com.datastax.cassandra:cassandra-driver-mapping:3.6.0")
    compile("com.datastax.cassandra:cassandra-driver-extras:3.6.0")
    compile("com.electronwill.night-config:yaml:3.5.2")
    compile("org.json:json:20180813")
    compile("com.fasterxml.jackson.core:jackson-databind:2.9.8")
    compile("commons-cli:commons-cli:1.4")
    compile("org.apache.logging.log4j:log4j-api:2.11.2")
    compile("org.apache.logging.log4j:log4j-core:2.11.2")
    compile("org.apache.logging.log4j:log4j-slf4j-impl:2.11.2")
    compile("xyz.downgoon:snowflake:1.0.0")
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
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
