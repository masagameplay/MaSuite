import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    id("com.github.johnrengelman.shadow")
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":masuite-api"))

    implementation("mysql:mysql-connector-java:8.0.27")

    compileOnly("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")
    implementation("com.j256.ormlite:ormlite-jdbc:6.1")
    implementation("javax.persistence:javax.persistence-api:2.2")

    implementation("org.spongepowered:configurate-yaml:4.1.2")
    implementation("com.google.code.gson:gson:2.8.9")

    compileOnly("net.kyori:adventure-api:4.9.3")
}

java {
    sourceCompatibility = JavaVersion.toVersion(16)
    targetCompatibility = JavaVersion.toVersion(16)
}

tasks.withType(AbstractArchiveTask::class).configureEach {
    isReproducibleFileOrder = true
    isPreserveFileTimestamps = false
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    val prefix = "dev.masa.masuite.libs.gson."

    relocate("com.google.gson", prefix + "gson")
    relocate("com.j256.ormlite", prefix + "ormlite")
    relocate("javax.persistence", prefix + "persistence")
    relocate("org.spongepowered", prefix + "spongepowered")
    relocate("mysql", prefix + "mysql")
}

tasks.named("assemble").configure {
    dependsOn("shadowJar")
}

