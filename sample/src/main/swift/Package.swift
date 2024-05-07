// swift-tools-version:5.0
import Foundation
import PackageDescription

let packageName = "SampleAppCore"

let generatedName = "Generated"
let generatedPath = ".build/\(generatedName.lowercased())"

let package = Package(
    name: packageName,
    products: [
        .library(name: packageName, type: .dynamic, targets: [generatedName])
    ],
    dependencies: [
        .package(url: "https://github.com/readdle/java_swift.git", .upToNextMinor(from: "2.2.0")),
        .package(url: "https://github.com/readdle/swift-java.git", .upToNextMinor(from: "0.3.0")),
        .package(url: "https://github.com/readdle/swift-java-coder.git", .upToNextMinor(from: "1.1.1")),
        .package(url: "https://github.com/readdle/swift-anycodable.git", .upToNextMinor(from: "1.0.3")),
    ],
    targets: [
        .target(name: packageName, dependencies: ["AnyCodable", "java_swift", "JavaCoder"]),
        .target(name: generatedName, dependencies: [
                .byName(name: packageName),
                "java_swift",
                "Java",
                "JavaCoder",
            ],
            path: generatedPath
        )
    ]
)
