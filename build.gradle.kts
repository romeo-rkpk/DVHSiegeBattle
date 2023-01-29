import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    id("co.uzzu.dotenv.gradle") version "2.0.0"
    id("maven-publish")
}

group = "com.danvhae.minecraft.siege.battle"
version = "1.2.8-a1"

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

    compileOnly(files(env.fetch("WORLD_EDIT_FILE")))
    compileOnly(files(env.fetch("WORLD_GUARD_FILE")))
    compileOnly(files(env.fetch("DVH_SIEGE_CORE")))
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

tasks.register("단츄", Jar::class){
    group = "!danvhae"
    description = "의존성 모음집에 버전명 없이 파일을 내보냅니다"
    //dependsOn(listOf("jar", "build"))
    //dependsOn("clean")
    dependsOn("build")//.mustRunAfter("clean")

    from("build/classes/kotlin/main")
    from("build/resources/main")

    println("this is danchu task")
    doFirst{
        destinationDirectory.set(file(env.fetch("DEPEND_DIR")))
        archiveFileName.set(rootProject.name + ".jar")
    }

}

tasks.register("보라비", Jar::class){
    group = "!danvhae"
    description = "테스트 서버에 파일을 내보냅니다. 버전명 없이 내보냅니다."
    //dependsOn(listOf("jar", "build"))
    //dependsOn("clean")
    dependsOn("build")//.mustRunAfter("clean")

    from("build/classes/kotlin/main")
    from("build/resources/main")

    println("this is borav task")
    doFirst{

        destinationDirectory.set(file(env.fetch("PLUGIN_DIR")))
        archiveFileName.set(rootProject.name + ".jar")

    }

}

tasks.register("해야", Jar::class){
    group = "!danvhae"
    description = "어딘가에 있는 일시 폴더에 버전명을 포함하여 내보냅니다."
    //dependsOn(listOf("jar", "build"))
    //dependsOn("clean")
    dependsOn("build")//.mustRunAfter("clean")

    from("build/classes/kotlin/main")
    from("build/resources/main")

    println("this is haeya task")
    doFirst{
        destinationDirectory.set(file(env.fetch("ARCHIVE_DIR")))
    }

}
