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

}

java {
    sourceCompatibility = JavaVersion.toVersion(16)
    targetCompatibility = JavaVersion.toVersion(16)
}

tasks.withType<Wrapper> {
    gradleVersion = "7.1.1"
}