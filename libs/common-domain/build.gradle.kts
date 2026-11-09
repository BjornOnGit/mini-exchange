plugins {
    `java-library`
}

dependencies {
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj.core)
    testImplementation(libs.jqwik)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}