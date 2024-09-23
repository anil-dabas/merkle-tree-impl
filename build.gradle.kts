plugins {
    id("java")
}

group = "org.mktree.impl"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation ("org.bouncycastle:bcprov-jdk15on:1.70")
}

tasks.test {
    useJUnitPlatform()
}