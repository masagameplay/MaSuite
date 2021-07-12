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
}

java {
    sourceCompatibility = JavaVersion.toVersion(16)
    targetCompatibility = JavaVersion.toVersion(16)
}

tasks.shadowJar {
    relocate("com.j256.ormlite", "dev.masa.masuite.libs.ormlite")
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

