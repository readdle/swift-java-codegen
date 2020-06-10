//
// Created by Andrew on 2/4/18.
//

import Foundation

public enum UInt32Enum: UInt32, Codable {
    case one
    case two
    case three
}

public struct UInt32OptionsSet: OptionSet, Codable {

    public let rawValue: UInt32

    public init(rawValue: UInt32) {
        self.rawValue = rawValue
    }

    static let one = UInt32OptionsSet(rawValue: 1 << 0)
    static let two = UInt32OptionsSet(rawValue: 1 << 1)
    static let three = UInt32OptionsSet(rawValue: 1 << 2)
}

public struct UInt32TestStruct: Codable, Hashable {
    public var zero: UInt32 = UInt32.zero
    public var max: UInt32 = UInt32.max
    public var min: UInt32 = UInt32.min
    public var optional: UInt32? = UInt32.zero
    public var optionalNil: UInt32? = nil
}

public protocol UInt32TestParamProtocol {
    func testParam(_ param: UInt32) -> Bool
}

public protocol UInt32TestReturnTypeProtocol {
    func testReturnType() -> UInt32
}

public protocol UInt32TestOptionalParamProtocol {
    func testOptionalParam(_ param: UInt32?) -> Bool
}

public protocol UInt32TestOptionalReturnTypeProtocol {
    func testOptionalReturnType() -> UInt32?
}

public typealias UInt32Block = (_ value: UInt32) -> UInt32
public typealias OptionalUInt32Block = (_ value: UInt32?) -> UInt32?

public class UInt32Test {

    public static func testZero() -> UInt32 {
        return 0
    }

    public static func testMin() -> UInt32 {
        return UInt32.min
    }

    public static func testMax() -> UInt32 {
        return UInt32.max
    }

    public static func testParam(_ param: UInt32) -> Bool {
        return param == UInt32.max
    }

    public static func testReturnType() -> UInt32 {
        return UInt32.max
    }

    public static func testOptionalParam(_ param: UInt32?) -> Bool {
        return param == UInt32.max
    }

    public static func testOptionalReturnType() -> UInt32? {
        return UInt32.max
    }

    public static func testProtocolParam(_ callback: UInt32TestParamProtocol) -> Bool {
        return callback.testParam(UInt32.max)
    }

    public static func testProtocolReturnType(_ callback: UInt32TestReturnTypeProtocol) -> UInt32 {
        return callback.testReturnType()
    }

    public static func testProtocolOptionalParam(_ callback: UInt32TestOptionalParamProtocol) -> Bool {
        return callback.testOptionalParam(UInt32.max)
    }

    public static func testProtocolOptionalReturnType(_ callback: UInt32TestOptionalReturnTypeProtocol) -> UInt32? {
        return callback.testOptionalReturnType()
    }

    public static func testEncode() ->  UInt32TestStruct {
        return UInt32TestStruct()
    }

    public static func testDecode(_ value: UInt32TestStruct) -> Bool {
        return value == UInt32TestStruct()
    }

    public static func testEnumEncode(_ rawValue: UInt) -> UInt32Enum {
        switch UInt32(rawValue) {
        case UInt32Enum.one.rawValue: return UInt32Enum.one
        case UInt32Enum.two.rawValue: return UInt32Enum.two
        case UInt32Enum.three.rawValue: return UInt32Enum.three
        default: fatalError("Can't find enum with rawValue \(rawValue)")
        }
    }

    public static func testEnumDecode(_ enumValue: UInt32Enum) -> UInt {
        return UInt(enumValue.rawValue)
    }

    public static func testOptionSetEncode(_ rawValue: UInt) -> UInt32OptionsSet {
        return UInt32OptionsSet(rawValue: UInt32(rawValue))
    }

    public static func testOptionSetDecode(_ optionSet: UInt32OptionsSet) -> UInt {
        return UInt(optionSet.rawValue)
    }

    public static func testBlock(_ block: UInt32Block) -> Bool {
        let value = block(0)
        return value == 0
    }

    public static func testOptionalBlock(_ block: OptionalUInt32Block) -> Bool {
        let value = block(nil)
        return value == nil
    }

}