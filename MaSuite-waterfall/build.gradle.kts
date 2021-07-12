plugins {
    `java`
    id("com.github.johnrengelman.shadow")
}

repositories {
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
    implementation("io.github.waterfallmc:waterfall-api:1.17-R0.1-SNAPSHOT")
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    from(sourceSets.main.get().resources.srcDirs) {
       // TODO expand("version") = project.version
    }
}

tasks.shadowJar {
    dependencies {
        include(dependency(":MaSuite-common"))
    }
}

tasks.build {
    dependsOn("shadowJar")
}
