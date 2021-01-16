import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
  java
  application
  id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "com.benefitj"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
  //maven { url 'http://maven.aliyun.com/nexus/content/groups/public/' }
  //jcenter()
}

val vertxVersion = "4.0.0"
val junitJupiterVersion = "5.7.0"

val mainVerticleName = "com.benefitj.vertxweb.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
  mainClassName = launcherClassName
}

dependencies {
  //compile(fileTree(dir: "libs", include: ["*.jar"]))
  //compile(fileTree(dir: "libs/jar", include: ["*.jar"]))
  //compileOnly(fileTree(dir: "libs/source", include: ["*.jar"]))

  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-web")
  //implementation("io.vertx:vertx-web-client")
  implementation("io.vertx:vertx-mqtt")
  //implementation("io.vertx:vertx-hazelcast")
  implementation("com.alibaba:fastjson:1.2.75")

  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")

}

java {
  sourceCompatibility = JavaVersion.VERSION_15
  targetCompatibility = JavaVersion.VERSION_15
}

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}
