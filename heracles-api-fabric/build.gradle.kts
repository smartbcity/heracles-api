plugins {
    id("io.spring.dependency-management")
    kotlin("jvm")
//    kotlin("plugin.spring")
//    id("org.springframework.boot")
//    id("com.google.cloud.tools.jib")
}

dependencies {
    implementation ("org.slf4j:slf4j-api")
    implementation ("org.hyperledger.fabric-sdk-java:fabric-sdk-java:${Versions.fabric}")
    implementation( "com.fasterxml.jackson.core:jackson-databind")
}