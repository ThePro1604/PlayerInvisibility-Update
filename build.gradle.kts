import java.net.URI

plugins {
    id("net.fabricmc.fabric-loom")
}

base {
    archivesName.set("playervisibility")
}

version = "26.1-2.1.0"
group = "win.transgirls"

repositories {
    mavenCentral()
}

loom {
    accessWidenerPath.set(file("src/main/resources/playervisibility.aw"))
}

dependencies {
    val minecraftVersion: String by project
    minecraft("com.mojang:minecraft:$minecraftVersion")
    // No Yarn mappings — Minecraft 26.1 uses Mojang official (unobfuscated)
    val loaderVersion: String by project
    implementation("net.fabricmc:fabric-loader:$loaderVersion")
    val fabricVersion: String by project
    implementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")

    // Gson for config serialization
    implementation("com.google.code.gson:gson:2.11.0")

    // Mixin extras
    implementation(annotationProcessor("io.github.llamalad7:mixinextras-fabric:0.4.0")!!)
}

tasks {
    val javaVersion = JavaVersion.VERSION_25
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
        options.release.set(25)
    }

    jar {
        from("LICENSE") {
            rename { "${it}_${base.archivesName.get()}" }
        }
    }

    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(mutableMapOf("version" to project.version))
        }
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
        }
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        withSourcesJar()
    }
}
