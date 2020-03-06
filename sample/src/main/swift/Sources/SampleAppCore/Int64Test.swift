//
// Created by Andrew on 2/4/18.
//

import Foundation

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


}