plugins {
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    implementation ("org.jetbrains.kotlin:kotlin-reflect")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    api ("org.springframework.boot:spring-boot-starter-security:2.1.3.RELEASE")
    implementation ("org.springframework.security:spring-security-oauth2-resource-server:5.4.2")
    implementation( "org.springframework.security:spring-security-oauth2-jose:5.4.2")

    implementation ("org.springframework.boot:spring-boot-autoconfigure")

    implementation ("io.projectreactor:reactor-core:3.4.2")
}
