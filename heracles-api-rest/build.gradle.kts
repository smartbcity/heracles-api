plugins {
	id("io.spring.dependency-management")
	kotlin("jvm")
	kotlin("plugin.spring")
	id("org.springframework.boot")
	id("com.google.cloud.tools.jib")
}

dependencies {
	implementation(project(":heracles-api-fabric"))
	implementation(project(":heracles-api-rest-i2-keycloak"))

	implementation("org.hyperledger.fabric-sdk-java:fabric-sdk-java:${Versions.fabric}")

    implementation ("org.springframework.boot:spring-boot-autoconfigure")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	implementation("io.springfox:springfox-swagger2:${Versions.springfox}")
	implementation("io.springfox:springfox-swagger-ui:${Versions.springfox}")
	implementation("io.springfox:springfox-spring-webflux:${Versions.springfox}")


	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
}

//jib.to.image = "${dockerHost}/${project.name}:${version}"