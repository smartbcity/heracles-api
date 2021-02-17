pluginManagement {
    repositories {
        gradlePluginPortal()
        jcenter()
    }
}

rootProject.name = "heracles-api"

enableFeaturePreview("GRADLE_METADATA")

include(
    "heracles-api-fabric",
    "heracles-api-rest",
    "heracles-api-rest-i2-keycloak"
)