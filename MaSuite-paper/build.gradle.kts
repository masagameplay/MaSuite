plugins {
    `java`
    id("com.github.johnrengelman.shadow")
}

repositories {
    maven {
        name = "papermc-repo"
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
}

dependencies {
    implementation(project(":masuite-common"))
    implementation("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE


    // TODO: Solve
    /*
    from(sourceSets.main.resources.srcDirs) {
        expand "version": project.version
    }*/
}


tasks.shadowJar {
    dependencies {
        include(dependency(":MaSuite-common"))
    }
}

tasks.build {
    dependsOn("shadowJar");
}
