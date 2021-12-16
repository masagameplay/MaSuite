plugins {
    java
}

group "dev.masa"
version "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.withType<Wrapper> {
    gradleVersion = "7.1.1"
}