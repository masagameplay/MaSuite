import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
plugins {
    `java`
    id("com.github.johnrengelman.shadow")
}

java {
    sourceCompatibility = JavaVersion.toVersion(16)
    targetCompatibility = JavaVersion.toVersion(16)
}

repositories {
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven { url = uri("https://hub.spigotmc.org/nexus/content/groups/public/") }
}

dependencies {
    implementation(project(":masuite-common"))
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
    implementation("co.aikar:acf-paper:0.5.0-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.20")
    annotationProcessor("org.projectlombok:lombok:1.18.20")
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE


    // TODO: Solve
    /*
    from(sourceSets.main.resources.srcDirs) {
        expand "version": project.version
    }*/
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
    options.isFork = true
    options.forkOptions.executable = "javac"
    options.encoding = "UTF-8"
}

tasks.withType<ShadowJar> {
    relocate("co.aikar.commands", "dev.masa.masuite.libs.acf")
    relocate("co.aikar.locales", "dev.masa.masuite.libs.locales")
}

tasks.build {
    dependsOn("shadowJar");
}
