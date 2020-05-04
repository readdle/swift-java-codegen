//
// Created by Andrew on 2/4/18.
//

import Foundation

public enum UIntEnum: UInt, Codable {
    case one
    case two
    case three
}

public struct UIntOptionsSet: OptionSet, Codable {

    public let rawValue: UInt

    public init(rawValue: UInt) {
        self.rawValue = rawValue
    }

    static let one = UIntOptionsSet(rawValue: 1 << 0)
    static let two = UIntOptionsSet(rawValue: 1 << 1)
    static let three = UIntOptionsSet(rawValue: 1 << 2)
}

public struct UIntTestStruct: Codable, Hashable {
    public var zero: UInt = UInt.zero
    public var max: UInt = UInt(UInt32.max)
    public var min: UInt = UInt(UInt32.min)
    public var optional: UInt? = UInt.zero
    public var optionalNil: UInt? = nil
}

public protocol UIntTestParamProtocol {
    func testParam(_ param: UInt) -> Bool
}

public protocol UIntTestReturnTypeProtocol {
    func testReturnType() -> UInt
}

public protocol UIntTestOptionalParamProtocol {
    func testOptionalParam(_ param: UInt?) -> Bool
}

public protocol UIntTestOptionalReturnTypeProtocol {
    func testOptionalReturnType() -> UInt?
}

public typealias UIntBlock = (_ value: UInt) -> UInt
public typealias OptionalUIntBlock = (_ value: UInt?) -> UInt?

public class UIntTest {

    public static func testZero() -> UInt {
        return 0
    }

    public static func testMin() -> UInt {
        return UInt.min
    }

    public static func testMax() -> UInt {
        return UInt.max
    }

    public static func testMin32() -> UInt {
        return UInt(UInt32.min)
    }

    public static func testMax32() -> UInt {
        return UInt(UInt32.max)
    }

    public static func testParam(_ param: UInt) -> Bool {
        return param == UInt(UInt32.max)
    }

    public static func testReturnType() -> UInt {
        return UInt(UInt32.max)
    }

    public static func testOptionalParam(_ param: UInt?) -> Bool {
        return param == UInt(UInt32.max)
    }

    public static func testOptionalReturnType() -> UInt? {
        return UInt.max
    }

    public static func testOptional32ReturnType() -> UInt? {
        return UInt(UInt32.max)
    }

    public static func testProtocolParam(_ callback: UIntTestParamProtocol) -> Bool {
        return callback.testParam(UInt(UInt32.max))
    }

    public static func testProtocolReturnType(_ callback: UIntTestReturnTypeProtocol) -> UInt {
        return callback.testReturnType()
    }

    public static func testProtocolOptionalParam(_ callback: UIntTestOptionalParamProtocol) -> Bool {
        return callback.testOptionalParam(UInt(UInt32.max))
    }

    public static func testProtocolOptionalReturnType(_ callback: UIntTestOptionalReturnTypeProtocol) -> UInt? {
        return callback.testOptionalReturnType()
    }

    public static func testEncode() ->  UIntTestStruct {
        return UIntTestStruct()
    }

    public static func testDecode(_ value: UIntTestStruct) -> Bool {
        return value == UIntTestStruct()
    }

    public static func testEnumEncode(_ rawValue: UInt) -> UIntEnum {
        switch rawValue {
        case UIntEnum.one.rawValue: return UIntEnum.one
        case UIntEnum.two.rawValue: return UIntEnum.two
        case UIntEnum.three.rawValue: return UIntEnum.three
        default: fatalError("Can't find enum with rawValue \(rawValue)")
        }
    }

    public static func testEnumDecode(_ enumValue: UIntEnum) -> UInt {
        return enumValue.rawValue
    }

    public static func testOptionSetEncode(_ rawValue: UInt) -> UIntOptionsSet {
        return UIntOptionsSet(rawValue: rawValue)
    }

    public static func testOptionSetDecode(_ optionSet: UIntOptionsSet) -> UInt {
        return optionSet.rawValue
    }

    public static func testBlock(_ block: UIntBlock) -> Bool {
        let value = block(0)
        return value == 0
    }

    public static func testOptionalBlock(_ block: OptionalUIntBlock) -> Bool {
        let value = block(nil)
        return value == nil
    }

}