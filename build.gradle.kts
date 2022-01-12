group = "dev.masa"
version = "1.0.0-SNAPSHOT"

plugins {
    `java`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

java {
    sourceCompatibility = JavaVersion.toVersion(17)
    targetCompatibility = JavaVersion.toVersion(17)
}

tasks.wrapper {
    gradleVersion = "7.3.3"
}

allprojects {

    group = project.group
    version = project.version

    repositories {
        mavenLocal()
        mavenCentral()
    }
}
