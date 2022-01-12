import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`

    id("com.github.johnrengelman.shadow")
}

repositories {
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
}

dependencies {
    implementation(project(":masuite-common"))

    compileOnly("com.velocitypowered:velocity-api:3.1.1")
    annotationProcessor("com.velocitypowered:velocity-api:3.1.1")

    compileOnly("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")

    implementation("org.spongepowered:configurate-yaml:4.1.2")
    implementation("net.kyori:adventure-serializer-configurate4:4.9.3")
}

tasks.withType(AbstractArchiveTask::class).configureEach {
    isReproducibleFileOrder = true
    isPreserveFileTimestamps = false
}

tasks.withType<ShadowJar> {
    val prefix = "dev.masa.masuite.libs."

    relocate("org.spongepowered", prefix + "spongepowered")
}

tasks.named("assemble").configure {
    dependsOn("shadowJar")
}