plugins {
    `java`
    id("com.github.johnrengelman.shadow")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":masuite-api"))
}

java {
    sourceCompatibility = JavaVersion.toVersion(16)
    targetCompatibility = JavaVersion.toVersion(16)
}