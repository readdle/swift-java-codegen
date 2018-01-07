import Foundation
import java_swift
import JavaCoder

public let SwiftErrorClass = JNI.GlobalFindClass("com/readdle/codegen/anotation/SwiftError")
public let SwiftRuntimeErrorClass = JNI.GlobalFindClass("com/readdle/codegen/anotation/SwiftRuntimeError")

// TODO: move to library
public protocol SwiftJava: Codable {

    // Decoding SwiftValue type with JavaCoder
    static func from(javaObject: jobject) throws -> Self

    // Encoding SwiftValue type with JavaCoder
    func javaObject() throws -> jobject
}

extension SwiftJava {

    public static func from(javaObject: jobject) throws -> Self {
        // ignore forPackage for basic impl
        return try JavaDecoder(forPackage: "").decode(Self.self, from: javaObject)
    }

    public func javaObject() throws -> jobject {
        // ignore forPackage for basic impl
        return try JavaEncoder(forPackage: "").encode(self)
    }
    
}

extension Bool: SwiftJava {}
extension Int: SwiftJava {}
extension Int8: SwiftJava {}
extension Int16: SwiftJava {}
extension Int32: SwiftJava {}
extension Int64: SwiftJava {}
extension UInt: SwiftJava {}
extension UInt8: SwiftJava {}
extension UInt16: SwiftJava {}
extension UInt32: SwiftJava {}
extension UInt64: SwiftJava {}
extension Float: SwiftJava {}
extension Double: SwiftJava {}
extension Array: SwiftJava {}
extension Dictionary: SwiftJava {}
extension Set: SwiftJava {}
