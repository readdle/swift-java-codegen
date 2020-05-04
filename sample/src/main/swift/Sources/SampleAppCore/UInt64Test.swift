//
// Created by Andrew on 2/4/18.
//

import Foundation

public enum UInt64Enum: UInt64, Codable {
    case one
    case two
    case three
}

public struct UInt64OptionsSet: OptionSet, Codable {

    public let rawValue: UInt64

    public init(rawValue: UInt64) {
        self.rawValue = rawValue
    }

    static let one = UInt64OptionsSet(rawValue: 1 << 0)
    static let two = UInt64OptionsSet(rawValue: 1 << 1)
    static let three = UInt64OptionsSet(rawValue: 1 << 2)
}

public struct UInt64TestStruct: Codable, Hashable {
    public var zero: UInt64 = UInt64.zero
    public var max: UInt64 = UInt64.max
    public var min: UInt64 = UInt64.min
    public var optional: UInt64? = UInt64.zero
    public var optionalNil: UInt64? = nil
}

public protocol UInt64TestParamProtocol {
    func testParam(_ param: UInt64) -> Bool
}

public protocol UInt64TestReturnTypeProtocol {
    func testReturnType() -> UInt64
}

public protocol UInt64TestOptionalParamProtocol {
    func testOptionalParam(_ param: UInt64?) -> Bool
}

public protocol UInt64TestOptionalReturnTypeProtocol {
    func testOptionalReturnType() -> UInt64?
}

public typealias UInt64Block = (_ value: UInt64) -> UInt64
public typealias OptionalUInt64Block = (_ value: UInt64?) -> UInt64?

public class UInt64Test {

    public static func testZero() -> UInt64 {
        return 0
    }

    public static func testMin() -> UInt64 {
        return UInt64.min
    }

    public static func testMax() -> UInt64 {
        return UInt64.max
    }

    public static func testParam(_ param: UInt64) -> Bool {
        return param == UInt64.max
    }

    public static func testReturnType() -> UInt64 {
        return UInt64.max
    }

    public static func testOptionalParam(_ param: UInt64?) -> Bool {
        return param == UInt64.max
    }

    public static func testOptionalReturnType() -> UInt64? {
        return UInt64.max
    }

    public static func testProtocolParam(_ callback: UInt64TestParamProtocol) -> Bool {
        return callback.testParam(UInt64.max)
    }

    public static func testProtocolReturnType(_ callback: UInt64TestReturnTypeProtocol) -> UInt64 {
        return callback.testReturnType()
    }

    public static func testProtocolOptionalParam(_ callback: UInt64TestOptionalParamProtocol) -> Bool {
        return callback.testOptionalParam(UInt64.max)
    }

    public static func testProtocolOptionalReturnType(_ callback: UInt64TestOptionalReturnTypeProtocol) -> UInt64? {
        return callback.testOptionalReturnType()
    }

    public static func testEncode() ->  UInt64TestStruct {
        return UInt64TestStruct()
    }

    public static func testDecode(_ value: UInt64TestStruct) -> Bool {
        return value == UInt64TestStruct()
    }

    public static func testEnumEncode(_ rawValue: UInt64) -> UInt64Enum {
        switch rawValue {
        case UInt64Enum.one.rawValue: return UInt64Enum.one
        case UInt64Enum.two.rawValue: return UInt64Enum.two
        case UInt64Enum.three.rawValue: return UInt64Enum.three
        default: fatalError("Can't find enum with rawValue \(rawValue)")
        }
    }

    public static func testEnumDecode(_ enumValue: UInt64Enum) -> UInt64 {
        return enumValue.rawValue
    }

    public static func testOptionSetEncode(_ rawValue: UInt64) -> UInt64OptionsSet {
        return UInt64OptionsSet(rawValue: rawValue)
    }

    public static func testOptionSetDecode(_ optionSet: UInt64OptionsSet) -> UInt64 {
        return optionSet.rawValue
    }

    public static func testBlock(_ block: UInt64Block) -> Bool {
        let value = block(0)
        return value == 0
    }

    public static func testOptionalBlock(_ block: OptionalUInt64Block) -> Bool {
        let value = block(nil)
        return value == nil
    }

}