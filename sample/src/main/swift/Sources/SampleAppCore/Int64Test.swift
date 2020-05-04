//
// Created by Andrew on 2/4/18.
//

import Foundation

public enum Int64Enum: Int64, Codable {
    case one
    case two
    case three
}

public struct Int64OptionsSet: OptionSet, Codable {

    public let rawValue: Int64

    public init(rawValue: Int64) {
        self.rawValue = rawValue
    }

    static let one = Int64OptionsSet(rawValue: 1 << 0)
    static let two = Int64OptionsSet(rawValue: 1 << 1)
    static let three = Int64OptionsSet(rawValue: 1 << 2)
}

public struct Int64TestStruct: Codable, Hashable {
    public var zero: Int64 = Int64.zero
    public var max: Int64 = Int64.max
    public var min: Int64 = Int64.min
    public var optional: Int64? = Int64.zero
    public var optionalNil: Int64? = nil
}

public protocol Int64TestParamProtocol {
    func testParam(_ param: Int64) -> Bool
}

public protocol Int64TestReturnTypeProtocol {
    func testReturnType() -> Int64
}

public protocol Int64TestOptionalParamProtocol {
    func testOptionalParam(_ param: Int64?) -> Bool
}

public protocol Int64TestOptionalReturnTypeProtocol {
    func testOptionalReturnType() -> Int64?
}

public typealias Int64Block = (_ value: Int64) -> Int64
public typealias OptionalInt64Block = (_ value: Int64?) -> Int64?

public class Int64Test {

    public static func testZero() -> Int64 {
        return 0
    }

    public static func testMin() -> Int64 {
        return Int64.min
    }

    public static func testMax() -> Int64 {
        return Int64.max
    }

    public static func testParam(_ param: Int64) -> Bool {
        return param == Int64.max
    }

    public static func testReturnType() -> Int64 {
        return Int64.max
    }

    public static func testOptionalParam(_ param: Int64?) -> Bool {
        return param == Int64.max
    }

    public static func testOptionalReturnType() -> Int64? {
        return Int64.max
    }

    public static func testProtocolParam(_ callback: Int64TestParamProtocol) -> Bool {
        return callback.testParam(Int64.max)
    }

    public static func testProtocolReturnType(_ callback: Int64TestReturnTypeProtocol) -> Int64 {
        return callback.testReturnType()
    }

    public static func testProtocolOptionalParam(_ callback: Int64TestOptionalParamProtocol) -> Bool {
        return callback.testOptionalParam(Int64.max)
    }

    public static func testProtocolOptionalReturnType(_ callback: Int64TestOptionalReturnTypeProtocol) -> Int64? {
        return callback.testOptionalReturnType()
    }

    public static func testEncode() ->  Int64TestStruct {
        return Int64TestStruct()
    }

    public static func testDecode(_ value: Int64TestStruct) -> Bool {
        return value == Int64TestStruct()
    }

    public static func testEnumEncode(_ rawValue: Int64) -> Int64Enum {
        switch rawValue {
        case Int64Enum.one.rawValue: return Int64Enum.one
        case Int64Enum.two.rawValue: return Int64Enum.two
        case Int64Enum.three.rawValue: return Int64Enum.three
        default: fatalError("Can't find enum with rawValue \(rawValue)")
        }
    }

    public static func testEnumDecode(_ enumValue: Int64Enum) -> Int64 {
        return enumValue.rawValue
    }

    public static func testOptionSetEncode(_ rawValue: Int64) -> Int64OptionsSet {
        return Int64OptionsSet(rawValue: rawValue)
    }

    public static func testOptionSetDecode(_ optionSet: Int64OptionsSet) -> Int64 {
        return optionSet.rawValue
    }

    public static func testBlock(_ block: Int64Block) -> Bool {
        let value = block(0)
        return value == 0
    }

    public static func testOptionalBlock(_ block: OptionalInt64Block) -> Bool {
        let value = block(nil)
        return value == nil
    }

}