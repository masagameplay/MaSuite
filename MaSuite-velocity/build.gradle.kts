plugins {
    `java-library`
    id("com.github.johnrengelman.shadow")
}

repositories {
    mavenCentral()
    maven {
        name = "velocitypowered-repo"
        url = uri("https://nexus.velocitypowered.com/repository/maven-public/")
    }
    maven {
        name = "minecraft-libraries"
        url = uri("https://libraries.minecraft.net/")
    }
    maven {
        name = "spongepowered-repo"
        url = uri("https://repo.spongepowered.org/maven")
    }
}

dependencies {
    implementation(project(":masuite-common"))
    implementation("com.velocitypowered:velocity-api:3.0.0")
    annotationProcessor("com.velocitypowered:velocity-api:3.0.0")
}

tasks.shadowJar {
    dependencies {
        include(dependency(":masuite-common"))
    }
}

// TODO: Solve
/*
tasks.processSources(type: Sync) {
    from sourceSets.main.java.srcDirs
    inputs.property "version", version
    filter ReplaceTokens, tokens: [version: version]
    into "$buildDir/src"
}*/

//compileJava.source = processSources.outputs

tasks.build {
    dependsOn("shadowJar")
}
