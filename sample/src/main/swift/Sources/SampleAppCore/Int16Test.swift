//
// Created by Andrew on 2/4/18.
//

import Foundation

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


}