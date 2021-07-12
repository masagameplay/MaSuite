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
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.assemble {
    dependsOn("shadowJar")
}
