import groovy.lang.GroovySystem
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Calendar


plugins {
    java
    kotlin("jvm") version "1.7.10"
    jacoco
    eclipse
    idea
    `maven-publish`
    signing
    id("org.jetbrains.dokka") version "1.7.10"
    id("com.github.hierynomus.license") version "0.16.1"
    id("com.diffplug.spotless") version "6.8.0"
}

group = "com.github.rising3"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.7.10")
}

val install by tasks.creating() {
    dependsOn(tasks.publishToMavenLocal)
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Implementation-Title"] = project.name
        attributes["Implementation-Version"] = project.version
        attributes["Implementation-Id"] = "${project.group}:${project.name}:${project.version}"
        attributes["Created-By"] = "${System.getProperty("java.version")}(${System.getProperty("java.vendor")})"
        attributes["Built-With"] = "gradle-${project.gradle.gradleVersion}, groovy-${GroovySystem.getVersion()}"
        attributes["Build-Time"] = "${ZonedDateTime.now(ZoneId.of("UTC"))}"
        attributes["License"] = "The Apache License, Version 2.0"
    }
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
}

val javadocJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    archiveClassifier.set("javadoc")
    from(tasks.dokkaJavadoc)
    dependsOn(tasks.dokkaJavadoc)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}

tasks.dokkaJavadoc.configure {
    outputDirectory.set(buildDir.resolve("javadoc"))
    moduleName.set(rootProject.name)
}

tasks.test {
    useJUnitPlatform()
    finalizedBy("jacocoTestReport")
}

jacoco { toolVersion = "0.8.8" }

tasks.jacocoTestReport {
    finalizedBy("jacocoTestCoverageVerification")
}

spotless {
    java {
        importOrder()
        removeUnusedImports()
        googleJavaFormat("1.8").aosp()
        prettier()
    }
    kotlin {
        // ktlint()
    }
    kotlinGradle {
        // ktlint()
    }
}

license {
    header = rootProject.file("codequality/HEADER")
    strictCheck = true
    license.ext["year"] = Calendar.getInstance().get(Calendar.YEAR)
    license.ext["name"] = project.findProperty("author") ?: System.getenv("ENV_AUTHOR")
    license.ext["email"] = project.findProperty("email") ?: System.getenv("ENV_EMAIL")
}
