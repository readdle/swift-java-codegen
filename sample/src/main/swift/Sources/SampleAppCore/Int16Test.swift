//
// Created by Andrew on 2/4/18.
//

import Foundation

public enum Int16Enum: Int16, Codable {
    case one
    case two
    case three
}

public struct Int16OptionsSet: OptionSet, Codable {

    public let rawValue: Int16

    public init(rawValue: Int16) {
        self.rawValue = rawValue
    }

    static let one = Int16OptionsSet(rawValue: 1 << 0)
    static let two = Int16OptionsSet(rawValue: 1 << 1)
    static let three = Int16OptionsSet(rawValue: 1 << 2)
}

public struct Int16TestStruct: Codable, Hashable {
    public var zero: Int16 = Int16.zero
    public var max: Int16 = Int16.max
    public var min: Int16 = Int16.min
    public var optional: Int16? = Int16.zero
    public var optionalNil: Int16? = nil
}

public protocol Int16TestParamProtocol {
    func testParam(_ param: Int16) -> Bool
}

public protocol Int16TestReturnTypeProtocol {
    func testReturnType() -> Int16
}

public protocol Int16TestOptionalParamProtocol {
    func testOptionalParam(_ param: Int16?) -> Bool
}

public protocol Int16TestOptionalReturnTypeProtocol {
    func testOptionalReturnType() -> Int16?
}

public typealias Int16Block = (_ value: Int16) -> Int16
public typealias OptionalInt16Block = (_ value: Int16?) -> Int16?

public class Int16Test {

    public static func testZero() -> Int16 {
        return 0
    }

    public static func testMin() -> Int16 {
        return Int16.min
    }

    public static func testMax() -> Int16 {
        return Int16.max
    }

    public static func testParam(_ param: Int16) -> Bool {
        return param == Int16.max
    }

    public static func testReturnType() -> Int16 {
        return Int16.max
    }

    public static func testOptionalParam(_ param: Int16?) -> Bool {
        return param == Int16.max
    }

    public static func testOptionalReturnType() -> Int16? {
        return Int16.max
    }

    public static func testProtocolParam(_ callback: Int16TestParamProtocol) -> Bool {
        return callback.testParam(Int16.max)
    }

    public static func testProtocolReturnType(_ callback: Int16TestReturnTypeProtocol) -> Int16 {
        return callback.testReturnType()
    }

    public static func testProtocolOptionalParam(_ callback: Int16TestOptionalParamProtocol) -> Bool {
        return callback.testOptionalParam(Int16.max)
    }

    public static func testProtocolOptionalReturnType(_ callback: Int16TestOptionalReturnTypeProtocol) -> Int16? {
        return callback.testOptionalReturnType()
    }

    public static func testEncode() ->  Int16TestStruct {
        return Int16TestStruct()
    }

    public static func testDecode(_ value: Int16TestStruct) -> Bool {
        return value == Int16TestStruct()
    }

    public static func testEnumEncode(_ rawValue: Int16) -> Int16Enum {
        switch rawValue {
        case Int16Enum.one.rawValue: return Int16Enum.one
        case Int16Enum.two.rawValue: return Int16Enum.two
        case Int16Enum.three.rawValue: return Int16Enum.three
        default: fatalError("Can't find enum with rawValue \(rawValue)")
        }
    }

    public static func testEnumDecode(_ enumValue: Int16Enum) -> Int16 {
        return enumValue.rawValue
    }

    public static func testOptionSetEncode(_ rawValue: Int16) -> Int16OptionsSet {
        return Int16OptionsSet(rawValue: rawValue)
    }

    public static func testOptionSetDecode(_ optionSet: Int16OptionsSet) -> Int16 {
        return optionSet.rawValue
    }

    public static func testBlock(_ block: Int16Block) -> Bool {
        let value = block(0)
        return value == 0
    }

    public static func testOptionalBlock(_ block: OptionalInt16Block) -> Bool {
        let value = block(nil)
        return value == nil
    }

}