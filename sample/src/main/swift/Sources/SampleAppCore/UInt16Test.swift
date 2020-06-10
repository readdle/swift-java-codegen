//
// Created by Andrew on 2/4/18.
//

import Foundation

public enum  UInt16Enum:  UInt16, Codable {
    case one
    case two
    case three
}

public struct  UInt16OptionsSet: OptionSet, Codable {

    public let rawValue:  UInt16

    public init(rawValue:  UInt16) {
        self.rawValue = rawValue
    }

    static let one =  UInt16OptionsSet(rawValue: 1 << 0)
    static let two =  UInt16OptionsSet(rawValue: 1 << 1)
    static let three =  UInt16OptionsSet(rawValue: 1 << 2)
}

public struct UInt16TestStruct: Codable, Hashable {
    public var zero: UInt16 = UInt16.zero
    public var max: UInt16 = UInt16.max
    public var min: UInt16 = UInt16.min
    public var optional: UInt16? = UInt16.zero
    public var optionalNil: UInt16? = nil
}

public protocol UInt16TestParamProtocol {
    func testParam(_ param: UInt16) -> Bool
}

public protocol UInt16TestReturnTypeProtocol {
    func testReturnType() -> UInt16
}

public protocol UInt16TestOptionalParamProtocol {
    func testOptionalParam(_ param: UInt16?) -> Bool
}

public protocol UInt16TestOptionalReturnTypeProtocol {
    func testOptionalReturnType() -> UInt16?
}

public typealias UInt16Block = (_ value: UInt16) -> UInt16
public typealias OptionalUInt16Block = (_ value: UInt16?) -> UInt16?

public class UInt16Test {

    public static func testZero() -> UInt16 {
        return 0
    }

    public static func testMin() -> UInt16 {
        return UInt16.min
    }

    public static func testMax() -> UInt16 {
        return UInt16.max
    }

    public static func testParam(_ param: UInt16) -> Bool {
        return param == UInt16.max
    }

    public static func testReturnType() -> UInt16 {
        return UInt16.max
    }

    public static func testOptionalParam(_ param: UInt16?) -> Bool {
        return param == UInt16.max
    }

    public static func testOptionalReturnType() -> UInt16? {
        return UInt16.max
    }

    public static func testProtocolParam(_ callback: UInt16TestParamProtocol) -> Bool {
        return callback.testParam(UInt16.max)
    }

    public static func testProtocolReturnType(_ callback: UInt16TestReturnTypeProtocol) -> UInt16 {
        return callback.testReturnType()
    }

    public static func testProtocolOptionalParam(_ callback: UInt16TestOptionalParamProtocol) -> Bool {
        return callback.testOptionalParam(UInt16.max)
    }

    public static func testProtocolOptionalReturnType(_ callback: UInt16TestOptionalReturnTypeProtocol) -> UInt16? {
        return callback.testOptionalReturnType()
    }

    public static func testEncode() ->  UInt16TestStruct {
        return UInt16TestStruct()
    }

    public static func testDecode(_ value: UInt16TestStruct) -> Bool {
        return value == UInt16TestStruct()
    }

    public static func testEnumEncode(_ rawValue:  UInt16) ->  UInt16Enum {
        switch rawValue {
        case  UInt16Enum.one.rawValue: return  UInt16Enum.one
        case  UInt16Enum.two.rawValue: return  UInt16Enum.two
        case  UInt16Enum.three.rawValue: return  UInt16Enum.three
        default: fatalError("Can't find enum with rawValue \(rawValue)")
        }
    }

    public static func testEnumDecode(_ enumValue:  UInt16Enum) ->  UInt16 {
        return enumValue.rawValue
    }

    public static func testOptionSetEncode(_ rawValue:  UInt16) ->  UInt16OptionsSet {
        return  UInt16OptionsSet(rawValue: rawValue)
    }

    public static func testOptionSetDecode(_ optionSet:  UInt16OptionsSet) ->  UInt16 {
        return optionSet.rawValue
    }

    public static func testBlock(_ block: UInt16Block) -> Bool {
        let value = block(0)
        return value == 0
    }

    public static func testOptionalBlock(_ block: OptionalUInt16Block) -> Bool {
        let value = block(nil)
        return value == nil
    }

}