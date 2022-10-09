setupShadowJar()

dependencies {
    implementation("org.jetbrains:annotations:19.0.0")
    compileOnly(Dependencies.LOMBOK)
    compileOnly(Dependencies.SPIGOT)
    annotationProcessor(Dependencies.LOMBOK)
    compileOnly(Dependencies.REFLECTIONS)
    implementation(Dependencies.LUCKO_HELPER)
    implementation(Dependencies.LUCKO_SQL)
    implementation(project(":entities-api"))
    compileOnly(Dependencies._SPIGOT)
    compileOnly(Dependencies.GUICE)
}