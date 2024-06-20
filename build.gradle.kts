plugins {
    kotlin("jvm")
    id("babric-loom") version "1.4.2"
    id("maven-publish")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
        vendor = JvmVendorSpec.ADOPTIUM
    }
    withSourcesJar()
}

kotlin {
    jvmToolchain {
        languageVersion = JavaLanguageVersion.of(17)
        vendor = JvmVendorSpec.ADOPTIUM
    }
}

base.archivesName = property("archives_base_name") as String
group = property("maven_group")!!
version = property("mod_version")!!

loom {
    @Suppress("UnstableApiUsage")
    mixin {
        useLegacyMixinAp.set(true)
    }
    gluedMinecraftJar()
    customMinecraftManifest.set("https://babric.github.io/manifest-polyfill/${property("minecraft_version")}.json")
    intermediaryUrl.set("https://maven.glass-launcher.net/babric/babric/intermediary/%1\$s/intermediary-%1\$s-v2.jar")
}

repositories {
    // Add repositories to retrieve artifacts from in here.
    // You should only use this when depending on other mods because
    // Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
    // See https://docs.gradle.org/current/userguide/declaring_repositories.html
    // for more information about repositories.

    // Used for the fabric toolchain in this project.
    maven("https://maven.glass-launcher.net/babric") {
        name = "Babric"
    }
    // Used for mappings.
    maven("https://maven.glass-launcher.net/releases") {
        name = "Glass Releases"
    }
    // Used for StationAPI and HowManyItems.
    maven("https://maven.glass-launcher.net/snapshots") {
        name = "Glass Snapshots"
    }
    // Used for a StationAPI dependency.
    maven("https://maven.minecraftforge.net/") {
        name = "Froge"
    }
    maven("https://repo.viaversion.com") {
        name = "ViaVersion"
    }
    // Used for projects that do not have a maven repository, but do have a GitHub repository with working build scripts.
    maven("https://jitpack.io") {
        name = "Jitpack"
    }
    // Used for another StationAPI dependency
    exclusiveContent {
        forRepository {
            maven("https://api.modrinth.com/maven") {
                name = "Modrinth"
            }
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }
    mavenCentral()
}

dependencies {
    // To change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings("net.glasslauncher:biny:${property("mappings")}:v2")
    modImplementation("babric:fabric-loader:${property("loader_version")}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${property("fabric_kotlin_version")}") {
        exclude(module = "fabric-loader")
    }

    implementation("org.slf4j:slf4j-api:1.8.0-beta4")
    implementation("org.apache.logging.log4j:log4j-slf4j18-impl:2.17.2")
    implementation("blue.endless:jankson:1.2.1")

    modImplementation("net.modificationstation:StationAPI:${property("stapi_version")}")
    modImplementation("net.glasslauncher.mods:GlassConfigAPI:${property("gcapi_version")}")
    // Optional dependencies
    modLocalRuntime("com.github.calmilamsy:ModMenu:${property("modmenu_version")}") {
        isTransitive = false
    }
    modImplementation("net.glasslauncher:HowManyItems-Fabric-Unofficial:${property("hmifabric_version")}") {
        isTransitive = false
    }
    modImplementation("maven.modrinth:mojangfix-stationapi-edition:${property("mojangfixstationapi_version")}") {
        isTransitive = false
    }
    implementation("com.github.GeyserMC:MCAuthLib:d9d773e5d50327c33898c65cd545a4f6ef3ba1b5") {
        isTransitive = false
    }
}

tasks {
    processResources {
        inputs.property("version", version)

        filesMatching("fabric.mod.json") {
            expand(getProperties())
            expand(mapOf("version" to version))
        }
    }

    jar {
        from("LICENSE") {
            rename { "${it}_${base.archivesName}" }
        }
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                artifact(remapJar) {
                    builtBy(remapJar)
                }
                artifact(kotlinSourcesJar) {
                    builtBy(remapSourcesJar)
                }
            }
        }

        // select the repositories you want to publish to
        repositories {
            // uncomment to publish to the local maven
            // mavenLocal()
        }
    }
}
