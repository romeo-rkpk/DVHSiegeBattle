import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    id("co.uzzu.dotenv.gradle") version "2.0.0"
    id("maven-publish")
}

group = "com.danvhae.minecraft.siege"
version = "0.0.0-a1"

repositories {
    mavenCentral()

    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven {
        name = "spigotmc-repo"
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    mavenLocal()
}

dependencies {
    testImplementation(kotlin("test"))
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly("com.danvhae.minecraft.siege:SiegeCore:0.3.0-a1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<ProcessResources>{
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml"){
        expand(project.properties)
    }
}

publishing{

    publications{
        create<MavenPublication>("maven"){
            groupId = group.toString()
            artifactId = "SiegeBattle"
            version = project.version.toString()

        }
    }

    repositories{
        maven{
            name ="GitHubPackages"
            url = uri("https://maven.pkg.github.com/romeo-rkpk/DVHSiegeBattle")
            credentials{
                username = env.fetch("GITHUB_NAME")
                password = env.fetch("GITHUB_TOKEN")
            }

        }
    }
}