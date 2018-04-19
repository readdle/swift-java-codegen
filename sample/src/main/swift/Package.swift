// swift-tools-version:4.0
import PackageDescription
import Foundation

let packageName = "SampleAppCore"

// generated sources integration
let generatedName = "Generated"
let generatedPath = ".build/\(generatedName.lowercased())"

let isSourcesGenerated: Bool = {
    let basePath = URL(fileURLWithPath: #file)
            .deletingLastPathComponent()
            .path

    let fileManager = FileManager()
    fileManager.changeCurrentDirectoryPath(basePath)

    var isDirectory: ObjCBool = false
    let exists = fileManager.fileExists(atPath: generatedPath, isDirectory: &isDirectory)

    return exists && isDirectory.boolValue
}()

func addGenerated(_ products: [Product]) -> [Product] {
    if isSourcesGenerated == false {
        return products
    }

    return products + [
        .library(name: packageName, type: .dynamic, targets: [generatedName])
    ]
}

func addGenerated(_ targets: [Target]) -> [Target] {
    if isSourcesGenerated == false {
        return targets
    }

    return targets + [
        .target(
            name: generatedName,
            dependencies: [
                .byNameItem(name: packageName),
                "java_swift",
                "Java",
                "JavaCoder",
            ],
            path: generatedPath
        )
    ]
}
// generated sources integration end

let package = Package(
    name: packageName,
    products: addGenerated([
    ]),
    dependencies: [
        .package(url: "https://github.com/andriydruk/java_swift.git", .branch("master")),
        .package(url: "https://github.com/andriydruk/swift-java.git", .branch("master")),
        .package(url: "https://github.com/andriydruk/swift-java-coder.git", .branch("master")),
        .package(url: "https://github.com/andriydruk/swift-anycodable.git", .branch("master")),
    ],
    targets: addGenerated([
        .target(name: packageName, dependencies: ["AnyCodable"])
    ]),
    swiftLanguageVersions: [4]
)