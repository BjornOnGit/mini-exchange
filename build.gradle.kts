import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    alias(libs.plugins.spotless) apply false
}

// Shared Java conventions for every subproject that applies the Java plugin.
subprojects {
    plugins.withType<JavaPlugin> {
        pluginManager.apply("com.diffplug.spotless")

        extensions.configure<SpotlessExtension> {
            java {
                googleJavaFormat("1.24.0")
            }
        }

        repositories {
            mavenCentral()
        }

        extensions.configure<JavaPluginExtension> {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(21))
            }
        }

        tasks.withType<JavaCompile>().configureEach {
            options.encoding = "UTF-8"
            options.compilerArgs.add("-Xlint:all")
        }

        tasks.withType<Test>().configureEach {
            useJUnitPlatform()
            testLogging {
                events("failed")
                exceptionFormat = TestExceptionFormat.FULL
            }
        }
    }
}