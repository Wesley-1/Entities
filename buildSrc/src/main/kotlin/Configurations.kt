import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.withType

fun Project.setupShadowJar() {
    apply<ShadowPlugin>()

    tasks {
        withType<ShadowJar> {
            archiveFileName.set(project.name + ".jar")
            minimize()
        }

        getByName("build") {
            dependsOn(withType<ShadowJar>())
        }
    }
}

private fun ShadowJar.relocate(group: Any, vararg dependencies: String) {
    dependencies.forEach {
        val split = it.split(".")
        val name = split.last()
        relocate(it, "$group.dependencies.$name")
    }
}