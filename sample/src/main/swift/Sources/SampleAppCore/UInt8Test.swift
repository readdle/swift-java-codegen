//
// Created by Andrew on 2/4/18.
//

import Foundation

public enum UInt8Enum: UInt8, Codable {
    case one
    case two
    case three
}

public struct UInt8OptionsSet: OptionSet, Codable {

    public let rawValue: UInt8

    public init(rawValue: UInt8) {
        self.rawValue = rawValue
    }

    static let one = UInt8OptionsSet(rawValue: 1 << 0)
    static let two = UInt8OptionsSet(rawValue: 1 << 1)
    static let three = UInt8OptionsSet(rawValue: 1 << 2)
}

public struct UInt8TestStruct: Codable, Hashable {
    public var zero: UInt8 = UInt8.zero
    public var max: UInt8 = UInt8.max
    public var min: UInt8 = UInt8.min
    public var optional: UInt8? = UInt8.zero
    public var optionalNil: UInt8? = nil
}

public protocol UInt8TestParamProtocol {
    func testParam(_ param: UInt8) -> Bool
}

public protocol UInt8TestReturnTypeProtocol {
    func testReturnType() -> UInt8
}

public protocol UInt8TestOptionalParamProtocol {
    func testOptionalParam(_ param: UInt8?) -> Bool
}

public protocol UInt8TestOptionalReturnTypeProtocol {
    func testOptionalReturnType() -> UInt8?
}

public typealias UInt8Block = (_ value: UInt8) -> UInt8
public typealias OptionalUInt8Block = (_ value: UInt8?) -> UInt8?

public class UInt8Test {

    public static func testZero() -> UInt8 {
        return 0
    }

    public static func testMin() -> UInt8 {
        return UInt8.min
    }

    public static func testMax() -> UInt8 {
        return UInt8.max
    }

    public static func testParam(_ param: UInt8) -> Bool {
        return param == UInt8.max
    }

    public static func testReturnType() -> UInt8 {
        return UInt8.max
    }

    public static func testOptionalParam(_ param: UInt8?) -> Bool {
        return param == UInt8.max
    }

    public static func testOptionalReturnType() -> UInt8? {
        return UInt8.max
    }

    public static func testProtocolParam(_ callback: UInt8TestParamProtocol) -> Bool {
        return callback.testParam(UInt8.max)
    }

    public static func testProtocolReturnType(_ callback: UInt8TestReturnTypeProtocol) -> UInt8 {
        return callback.testReturnType()
    }

    public static func testProtocolOptionalParam(_ callback: UInt8TestOptionalParamProtocol) -> Bool {
        return callback.testOptionalParam(UInt8.max)
    }

    public static func testProtocolOptionalReturnType(_ callback: UInt8TestOptionalReturnTypeProtocol) -> UInt8? {
        return callback.testOptionalReturnType()
    }

    public static func testEncode() ->  UInt8TestStruct {
        return UInt8TestStruct()
    }

    public static func testDecode(_ value: UInt8TestStruct) -> Bool {
        return value == UInt8TestStruct()
    }

    public static func testEnumEncode(_ rawValue: UInt8) -> UInt8Enum {
        switch rawValue {
        case UInt8Enum.one.rawValue: return UInt8Enum.one
        case UInt8Enum.two.rawValue: return UInt8Enum.two
        case UInt8Enum.three.rawValue: return UInt8Enum.three
        default: fatalError("Can't find enum with rawValue \(rawValue)")
        }
    }

    public static func testEnumDecode(_ enumValue: UInt8Enum) -> UInt8 {
        return enumValue.rawValue
    }

    public static func testOptionSetEncode(_ rawValue: UInt8) -> UInt8OptionsSet {
        return UInt8OptionsSet(rawValue: rawValue)
    }

    public static func testOptionSetDecode(_ optionSet: UInt8OptionsSet) -> UInt8 {
        return optionSet.rawValue
    }

    public static func testBlock(_ block: UInt8Block) -> Bool {
        let value = block(0)
        return value == 0
    }

    public static func testOptionalBlock(_ block: OptionalUInt8Block) -> Bool {
        let value = block(nil)
        return value == nil
    }

}