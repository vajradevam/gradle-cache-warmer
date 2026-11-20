plugins {
    java
    application
    jacoco
}

repositories {
    mavenCentral()
}

dependencies {
    // Pin EXACT versions. Student/exam projects must use these same versions,
    // otherwise Gradle will try to download them (and fail offline).
    testImplementation(platform("org.junit:junit-bom:5.13.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    // Required explicitly since Gradle 9
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

application {
    mainClass = "warmup.Main"
}

tasks.named<JavaExec>("run") {
    args("--no-gui")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "failed", "skipped")
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
}

// Sample code has no Javadoc comments; silence the "no comment" warnings.
tasks.javadoc {
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:-missing", "-quiet")
}
