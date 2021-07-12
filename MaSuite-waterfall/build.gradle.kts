plugins {
    `java`
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
    implementation(project(":masuite-common"))
    compileOnly("io.github.waterfallmc:waterfall-api:1.17-R0.1-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.20")
    annotationProcessor("org.projectlombok:lombok:1.18.20")

    implementation("com.j256.ormlite:ormlite-core:5.6")
    implementation("com.j256.ormlite:ormlite-jdbc:5.6")
    implementation("javax.persistence:javax.persistence-api:2.2")
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    from(sourceSets.main.get().resources.srcDirs) {
       // expand("version")
    }
}

tasks.shadowJar {
    dependencies {
        include(dependency(":masuite-api"))
        include(dependency(":masuite-common"))
    }
    relocate("com.j256.ormlite", "dev.masa.masuite.libs.ormlite")
}

tasks.build {
    dependsOn("shadowJar")
}
