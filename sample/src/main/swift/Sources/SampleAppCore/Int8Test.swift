//
// Created by Andrew on 2/4/18.
//

import Foundation

public enum Int8Enum: Int8, Codable {
    case one
    case two
    case three
}

public struct Int8OptionsSet: OptionSet, Codable {

    public let rawValue: Int8

    public init(rawValue: Int8) {
        self.rawValue = rawValue
    }

    static let one = Int8OptionsSet(rawValue: 1 << 0)
    static let two = Int8OptionsSet(rawValue: 1 << 1)
    static let three = Int8OptionsSet(rawValue: 1 << 2)
}

public struct Int8TestStruct: Codable, Hashable {
    public var zero: Int8 = Int8.zero
    public var max: Int8 = Int8.max
    public var min: Int8 = Int8.min
    public var optional: Int8? = Int8.zero
    public var optionalNil: Int8? = nil
}

public protocol Int8TestParamProtocol {
    func testParam(_ param: Int8) -> Bool
}

public protocol Int8TestReturnTypeProtocol {
    func testReturnType() -> Int8
}

public protocol Int8TestOptionalParamProtocol {
    func testOptionalParam(_ param: Int8?) -> Bool
}

public protocol Int8TestOptionalReturnTypeProtocol {
    func testOptionalReturnType() -> Int8?
}

public typealias Int8Block = (_ value: Int8) -> Int8
public typealias OptionalInt8Block = (_ value: Int8?) -> Int8?

public class Int8Test {

    public static func testZero() -> Int8 {
        return 0
    }

    public static func testMin() -> Int8 {
        return Int8.min
    }

    public static func testMax() -> Int8 {
        return Int8.max
    }

    public static func testParam(_ param: Int8) -> Bool {
        return param == Int8.max
    }

    public static func testReturnType() -> Int8 {
        return Int8.max
    }

    public static func testOptionalParam(_ param: Int8?) -> Bool {
        return param == Int8.max
    }

    public static func testOptionalReturnType() -> Int8? {
        return Int8.max
    }

    public static func testProtocolParam(_ callback: Int8TestParamProtocol) -> Bool {
        return callback.testParam(Int8.max)
    }

    public static func testProtocolReturnType(_ callback: Int8TestReturnTypeProtocol) -> Int8 {
        return callback.testReturnType()
    }

    public static func testProtocolOptionalParam(_ callback: Int8TestOptionalParamProtocol) -> Bool {
        return callback.testOptionalParam(Int8.max)
    }

    public static func testProtocolOptionalReturnType(_ callback: Int8TestOptionalReturnTypeProtocol) -> Int8? {
        return callback.testOptionalReturnType()
    }

    public static func testEncode() ->  Int8TestStruct {
        return Int8TestStruct()
    }

    public static func testDecode(_ value: Int8TestStruct) -> Bool {
        return value == Int8TestStruct()
    }

    public static func testEnumEncode(_ rawValue: Int8) -> Int8Enum {
        switch rawValue {
        case Int8Enum.one.rawValue: return Int8Enum.one
        case Int8Enum.two.rawValue: return Int8Enum.two
        case Int8Enum.three.rawValue: return Int8Enum.three
        default: fatalError("Can't find enum with rawValue \(rawValue)")
        }
    }

    public static func testEnumDecode(_ enumValue: Int8Enum) -> Int8 {
        return enumValue.rawValue
    }

    public static func testOptionSetEncode(_ rawValue: Int8) -> Int8OptionsSet {
        return Int8OptionsSet(rawValue: rawValue)
    }

    public static func testOptionSetDecode(_ optionSet: Int8OptionsSet) -> Int8 {
        return optionSet.rawValue
    }

    public static func testBlock(_ block: Int8Block) -> Bool {
        let value = block(0)
        return value == 0
    }

    public static func testOptionalBlock(_ block: OptionalInt8Block) -> Bool {
        let value = block(nil)
        return value == nil
    }

}