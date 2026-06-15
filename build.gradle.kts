plugins {
    java
    id("com.gradleup.shadow") version "9.4.0"
}

group = "dev.brominemc"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.minestom:minestom:2026.06.05-26.1.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.22.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.22.0")
}

tasks.shadowJar {
    archiveBaseName.set("Bromine")
    archiveVersion.set(project.version.toString())
    archiveClassifier.set("")

    manifest {
        attributes(
            "Main-Class" to "dev.brominemc.bromine.Main"
        )
    }
}


tasks.build {
    dependsOn(tasks.shadowJar)
}
