import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`

    id("com.github.johnrengelman.shadow")
}

repositories {
    maven {
        name = "velocity"
        url = uri("https://repo.velocitypowered.com/snapshots/")
    }
}

dependencies {
    implementation(project(":masuite-common"))

    compileOnly("com.velocitypowered:velocity-api:3.0.1-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.0.1-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")

    implementation("org.spongepowered:configurate-yaml:4.1.2")
    implementation("net.kyori:adventure-serializer-configurate4:4.9.3")
}

tasks.create<ConfigureShadowRelocation>("relocateShadowJar") {
    target = tasks["shadowJar"] as ShadowJar
    prefix = "dev.masa.masuite.libs"
}

tasks.withType<ShadowJar> {
    dependsOn(tasks["relocateShadowJar"])

    mergeServiceFiles()
}

tasks.named("assemble").configure {
    dependsOn("shadowJar")
}