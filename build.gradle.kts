import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {

	kotlin("multiplatform") version PluginVersions.kotlin apply false
	kotlin("jvm") version PluginVersions.kotlin apply false
	kotlin("plugin.spring") version PluginVersions.kotlin apply false
	id("io.spring.dependency-management") version PluginVersions.springPom apply false
	id("org.springframework.boot") version PluginVersions.spring apply false
	id("com.google.cloud.tools.jib") version PluginVersions.jib apply false


}

allprojects {
	group = "city.smartb.heracles"
	version = System.getenv("VERSION") ?: "latest"
	repositories {
		jcenter()
		mavenCentral()
	}
}

subprojects {
	val coroutines_version: String by project
	plugins.withType(org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper::class.java).whenPluginAdded {
		the<org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension>().apply {
			jvm {
				compilations.all {
					kotlinOptions.jvmTarget = "11"
				}
			}
			js(IR) {
				browser {
				}
				binaries.executable()
			}
			sourceSets {
				val commonMain by getting
				val commonTest by getting {
					dependencies {
						implementation(kotlin("test-common"))
						implementation(kotlin("test-annotations-common"))
					}
				}
				val jvmMain by getting
				val jvmTest by getting {
					dependencies {

						implementation(kotlin("reflect"))
//						api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
//						api("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$coroutines_version")
//						api("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:$coroutines_version")
//						api("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutines_version")

						implementation(kotlin("test-junit"))
					}
				}
				val jsMain by getting
				val jsTest by getting {
					dependencies {
						implementation(kotlin("test-js"))
					}
				}
			}
		}
	}
	plugins.withType(JavaPlugin::class.java).whenPluginAdded {
		tasks.withType<KotlinCompile>().configureEach {
			println("Configuring $name in project ${project.name}...")
			kotlinOptions {
				freeCompilerArgs = listOf("-Xjsr305=strict")
				jvmTarget = "11"
			}

		}
		the<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension>().apply {
			imports {
				mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES) {
					bomProperty("kotlin.version", PluginVersions.kotlin)
				}
			}
		}
		tasks.withType<JavaCompile> {
			sourceCompatibility = JavaVersion.VERSION_11.toString()
			targetCompatibility = JavaVersion.VERSION_11.toString()
		}

		tasks.withType<Test> {
			useJUnitPlatform()
		}

		dependencies {
			val implementation by configurations
			val testImplementation by configurations

			implementation(kotlin("reflect"))

			testImplementation("org.junit.jupiter:junit-jupiter:${Versions.junit}")
			testImplementation("org.junit.jupiter:junit-jupiter-api:${Versions.junit}")

			testImplementation("org.assertj:assertj-core:${Versions.assertj}")
//			implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
//			implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$coroutines_version")
//			implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:$coroutines_version")
//			implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutines_version")
		}
	}
}
