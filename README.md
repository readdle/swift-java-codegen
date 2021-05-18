
# Swift Java Codegeneration annotation processor ![Maven Central](https://img.shields.io/maven-central/v/com.readdle.swift.java.codegen/compiler)

Annotation processor that generate Swift JNI code for Kotlin headers

## Pre-requirements

This annotation process only generate Swift JNI code. For compiling swift code for Android please use [Swift Android Gradle plugin](https://github.com/readdle/swift-android-gradle)

## Setup

1. Add the following to your root build.gradle

```gradle
allprojects {
    repositories {
        mavenCentral()
    }
}
```

2. Add the following to your project build.gradle

```gradle
dependencies {
    kapt "com.readdle.swift.java.codegen:compiler:$swift-java-codegen-version"
    implementation "com.readdle.swift.java.codegen:annotations:$swift-java-codegen-version"
}
```


## Usage

Documentation in progress...


## Samples

There are few samples that use Swift Java Codegen:

1. [Sample in this repo](https://github.com/readdle/swift-java-codegen/tree/master/sample)
2. [Kotlin sample for Desktop](https://github.com/andriydruk/swift-kotlin-sample)
3. [Cross-platform swift weather app](https://github.com/andriydruk/swift-weather-app)

## FAQ

### Why this project has Java in it name but doesn't support Java?

When this project was started Kotlin was in beta development. That's why we decide to move on with Java as primary language. After 4 year of development we moved to Kotlin and break Java support. If for some reason you would like to use this project with Java, latest Java support version us stil available on Maven Central.

### Could I generate Swift JNI without writing Kotlin headers manually?

Very good question. Probably it's possible but it's quite complicated. We invest time to research code generation based on Swift and have a small prototype. But the development of this new tool is still at a very early stage.
