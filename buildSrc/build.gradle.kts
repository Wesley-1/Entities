plugins {
    `kotlin-dsl`
}
repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // Ae - start
    // When updating this plugin look at the bottom "Using legacy plugin application"
    // in that block you will see.
    //    dependencies {
    //        classpath "gradle.plugin.com.github.johnrengelman:shadow:7.1.2"
    //    }
    // then you will want to copy out the information from that block.
    // eg.. "gradle.plugin.com.github.johnrengelman:shadow:7.1.2"
    // https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow
    implementation("gradle.plugin.com.github.johnrengelman", "shadow", "7.1.2")
    // Ae - end
}