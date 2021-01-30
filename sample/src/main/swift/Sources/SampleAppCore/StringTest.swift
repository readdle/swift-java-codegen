//
// Created by Andrew on 2/4/18.
//

import Foundation

public enum StringEnum: String, Codable {
    case ONE
    case TWO
    case THREE
}

public struct StringTestStruct: Codable, Hashable {
    public var zero: String = ""
    public var optional: String? = "42"
    public var optionalNil: String? = nil
}

public protocol StringTestParamProtocol {
    func testParam(_ param: String) -> Bool
}

public protocol StringTestReturnTypeProtocol {
    func testReturnType() -> String
}

public protocol StringTestOptionalParamProtocol {
    func testOptionalParam(_ param: String?) -> Bool
}

public protocol StringTestOptionalReturnTypeProtocol {
    func testOptionalReturnType() -> String?
}

public typealias StringBlock = (_ value: String) -> String
public typealias OptionalStringBlock = (_ value: String?) -> String?

public class StringTest {

    public static func testZero() -> String {
        return ""
    }

    public static func testParam(_ param: String) -> Bool {
        return param == ""
    }

    public static func testReturnType() -> String {
        return ""
    }

    public static func testOptionalParam(_ param: String?) -> Bool {
        return param == ""
    }

    public static func testOptionalReturnType() -> String? {
        return ""
    }

    public static func testOptional32ReturnType() -> String? {
        return ""
    }

    public static func testProtocolParam(_ callback: StringTestParamProtocol) -> Bool {
        return callback.testParam("")
    }

    public static func testProtocolReturnType(_ callback: StringTestReturnTypeProtocol) -> String {
        return callback.testReturnType()
    }

    public static func testProtocolOptionalParam(_ callback: StringTestOptionalParamProtocol) -> Bool {
        return callback.testOptionalParam("")
    }

    public static func testProtocolOptionalReturnType(_ callback: StringTestOptionalReturnTypeProtocol) -> String? {
        return callback.testOptionalReturnType()
    }

    public static func testEncode() ->  StringTestStruct {
        return StringTestStruct()
    }

    public static func testDecode(_ value: StringTestStruct) -> Bool {
        return value == StringTestStruct()
    }

    public static func testEnumEncode(_ rawValue: String) -> StringEnum {
        switch rawValue {
        case StringEnum.ONE.rawValue: return StringEnum.ONE
        case StringEnum.TWO.rawValue: return StringEnum.TWO
        case StringEnum.THREE.rawValue: return StringEnum.THREE
        default: fatalError("Can't find enum with rawValue \(rawValue)")
        }
    }

    public static func testEnumDecode(_ enumValue: StringEnum) -> String {
        return enumValue.rawValue
    }

    public static func testBlock(_ block: StringBlock) -> Bool {
        let value = block("")
        return value == ""
    }

    public static func testOptionalBlock(_ block: OptionalStringBlock) -> Bool {
        let value = block(nil)
        return value == nil
    }

}