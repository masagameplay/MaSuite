import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    `java-library`
    id("com.github.johnrengelman.shadow")
}

repositories {
    mavenCentral()
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven {
        name = "destroystokyo-repo"
        url = uri("https://repo.destroystokyo.com/repository/maven-public/")
    }
}

dependencies {
    api(project(":masuite-common"))
    compileOnly("io.github.waterfallmc:waterfall-api:1.18-R0.1-SNAPSHOT")

    implementation("net.kyori:adventure-platform-bungeecord:4.0.1")
    implementation("org.spongepowered:configurate-yaml:4.1.2")

    implementation("net.kyori:adventure-serializer-configurate4:4.9.3")

    compileOnly("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")
}

tasks.withType<ShadowJar> {
    relocate("net.kyori:adventure-platform-bungeecord", "dev.masa.masuite.libs.adventure-platform-bungeecord")
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    /*
    from(sourceSets.main.get().resources.srcDirs) {
        expand(mutableMapOf("version" to project.version))
    }*/
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.build {
    dependsOn("shadowJar")
}

artifacts {
    val version = System.getenv("BUILD_NUMBER") ?: "0"
    base.archivesName.set("${project.name}-${parent!!.version}-RC${version}")
}