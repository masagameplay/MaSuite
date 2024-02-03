plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.masa.masuite"
version = "4.0.0-SNAPSHOT"

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
