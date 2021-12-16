import org.spongepowered.gradle.plugin.config.PluginLoaders
import org.spongepowered.plugin.metadata.PluginDependency

plugins {
    `java`
    `java-library`
    id("com.github.johnrengelman.shadow")
    id("org.spongepowered.gradle.plugin") version "1.1.1"
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":masuite-common"))
}

sponge {
    apiVersion("8.0.0")
    plugin("MaSuite") {
        loader(PluginLoaders.JAVA_PLAIN)
        displayName("MaSuite")
        mainClass("dev.masa.masuite.sponge.MaSuiteSponge")
        description("Proxy wide homes, teleportations and warps")
        links {
            homepage("https://masa.dev")
            // source("https://spongepowered.org/source")
            // issues("https://spongepowered.org/issues")
        }
        contributor("Masa") {
            description("Author")
        }
        dependency("spongeapi") {
            loadOrder(PluginDependency.LoadOrder.AFTER)
            optional(false)
        }
    }
}

val javaTarget = 16 // Sponge targets a minimum of Java 8
java {
    sourceCompatibility = JavaVersion.toVersion(javaTarget)
    targetCompatibility = JavaVersion.toVersion(javaTarget)
}

tasks.withType(JavaCompile::class).configureEach {
    options.apply {
        encoding = "utf-8" // Consistent source file encoding
        if (JavaVersion.current().isJava10Compatible) {
            release.set(javaTarget)
        }
    }
}

// Make sure all tasks which produce archives (jar, sources jar, javadoc jar, etc) produce more consistent output
tasks.withType(AbstractArchiveTask::class).configureEach {
    isReproducibleFileOrder = true
    isPreserveFileTimestamps = false
}

tasks {
    shadowJar {
        dependencies {
            include(dependency(":masuite-common"))
        }
    }

    build {
        dependsOn(shadowJar)
    }
}
