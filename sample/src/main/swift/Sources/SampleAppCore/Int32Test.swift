//
// Created by Andrew on 2/4/18.
//

import Foundation

public enum Int32Enum: Int32, Codable {
    case one
    case two
    case three
}

public struct Int32OptionsSet: OptionSet, Codable {

    public let rawValue: Int32

    public init(rawValue: Int32) {
        self.rawValue = rawValue
    }

    static let one = Int32OptionsSet(rawValue: 1 << 0)
    static let two = Int32OptionsSet(rawValue: 1 << 1)
    static let three = Int32OptionsSet(rawValue: 1 << 2)
}

public struct Int32TestStruct: Codable, Hashable {
    public var zero: Int32 = Int32.zero
    public var max: Int32 = Int32.max
    public var min: Int32 = Int32.min
    public var optional: Int32? = Int32.zero
    public var optionalNil: Int32? = nil
}

public protocol Int32TestParamProtocol {
    func testParam(_ param: Int32) -> Bool
}

public protocol Int32TestReturnTypeProtocol {
    func testReturnType() -> Int32
}

public protocol Int32TestOptionalParamProtocol {
    func testOptionalParam(_ param: Int32?) -> Bool
}

public protocol Int32TestOptionalReturnTypeProtocol {
    func testOptionalReturnType() -> Int32?
}

public typealias Int32Block = (_ value: Int32) -> Int32
public typealias OptionalInt32Block = (_ value: Int32?) -> Int32?

public class Int32Test {

    public static func testZero() -> Int32 {
        return 0
    }

    public static func testMin() -> Int32 {
        return Int32.min
    }

    public static func testMax() -> Int32 {
        return Int32.max
    }

    public static func testParam(_ param: Int32) -> Bool {
        return param == Int32.max
    }

    public static func testReturnType() -> Int32 {
        return Int32.max
    }

    public static func testOptionalParam(_ param: Int32?) -> Bool {
        return param == Int32.max
    }

    public static func testOptionalReturnType() -> Int32? {
        return Int32.max
    }

    public static func testProtocolParam(_ callback: Int32TestParamProtocol) -> Bool {
        return callback.testParam(Int32.max)
    }

    public static func testProtocolReturnType(_ callback: Int32TestReturnTypeProtocol) -> Int32 {
        return callback.testReturnType()
    }

    public static func testProtocolOptionalParam(_ callback: Int32TestOptionalParamProtocol) -> Bool {
        return callback.testOptionalParam(Int32.max)
    }

    public static func testProtocolOptionalReturnType(_ callback: Int32TestOptionalReturnTypeProtocol) -> Int32? {
        return callback.testOptionalReturnType()
    }

    public static func testEncode() ->  Int32TestStruct {
        return Int32TestStruct()
    }

    public static func testDecode(_ value: Int32TestStruct) -> Bool {
        return value == Int32TestStruct()
    }

    public static func testEnumEncode(_ rawValue: Int) -> Int32Enum {
        switch Int32(rawValue) {
        case Int32Enum.one.rawValue: return Int32Enum.one
        case Int32Enum.two.rawValue: return Int32Enum.two
        case Int32Enum.three.rawValue: return Int32Enum.three
        default: fatalError("Can't find enum with rawValue \(rawValue)")
        }
    }

    public static func testEnumDecode(_ enumValue: Int32Enum) -> Int {
        return Int(enumValue.rawValue)
    }

    public static func testOptionSetEncode(_ rawValue: Int) -> Int32OptionsSet {
        return Int32OptionsSet(rawValue: Int32(rawValue))
    }

    public static func testOptionSetDecode(_ optionSet: Int32OptionsSet) -> Int {
        return Int(optionSet.rawValue)
    }

    public static func testBlock(_ block: Int32Block) -> Bool {
        let value = block(0)
        return value == 0
    }

    public static func testOptionalBlock(_ block: OptionalInt32Block) -> Bool {
        let value = block(nil)
        return value == nil
    }

}