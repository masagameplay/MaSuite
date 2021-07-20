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
    compileOnly("org.projectlombok:lombok:1.18.20")
    annotationProcessor("org.projectlombok:lombok:1.18.20")
    implementation("com.j256.ormlite:ormlite-core:5.6")
    implementation("com.j256.ormlite:ormlite-jdbc:5.6")
    implementation("javax.persistence:javax.persistence-api:2.2")

    implementation("org.spongepowered:configurate-yaml:4.1.1")
    implementation("com.google.code.gson:gson:2.8.7")

    implementation("net.kyori:adventure-api:4.8.1")
}

java {
    sourceCompatibility = JavaVersion.toVersion(16)
    targetCompatibility = JavaVersion.toVersion(16)
}

tasks.withType<ShadowJar>() {
    relocate("com.j256.ormlite", "dev.masa.masuite.libs.ormlite")
    relocate("org.spongepowered", "dev.masa.masuite.libs.spongepowered")
    relocate("org.yaml.snakeyaml", "dev.masa.masuite.libs.snakeyaml")
    relocate("io.leangen.geantyref", "dev.masa.masuite.libs.geantyref")
    relocate("javax", "dev.masa.masuite.libs.javax")
    relocate("com.google.code.gson", "dev.masa.masuite.libs.gson")
    relocate("net.kyori.adventure-api", "dev.masa.masuite.libs.adventure-api")
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.jar {
    dependsOn("shadowJar")
}


tasks.withType<Wrapper> {
    gradleVersion = "7.1.1"
}

