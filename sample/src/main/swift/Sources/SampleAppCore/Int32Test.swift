//
// Created by Andrew on 2/4/18.
//

import Foundation

public struct Int32TestStruct: Codable, Hashable {
    public var zero: Int32 = Int32.zero
    public var max: Int32 = Int32.max
    public var min: Int32 = Int32.min
    public var optional: Int32? = Int32.zero
    public var optionalNil: Int32? = nil
}

public protocol Int32TestParamProtocol {
    func testParam(_ param: Int32) -> Bool
}

public protocol Int32TestReturnTypeProtocol {
    func testReturnType() -> Int32
}

public protocol Int32TestOptionalParamProtocol {
    func testOptionalParam(_ param: Int32?) -> Bool
}

public protocol Int32TestOptionalReturnTypeProtocol {
    func testOptionalReturnType() -> Int32?
}

public class Int32Test {

    public static func testZero() -> Int32 {
        return 0
    }

    public static func testMin() -> Int32 {
        return Int32.min
    }

    public static func testMax() -> Int32 {
        return Int32.max
    }

    public static func testParam(_ param: Int32) -> Bool {
        return param == Int32.max
    }

    public static func testReturnType() -> Int32 {
        return Int32.max
    }

    public static func testOptionalParam(_ param: Int32?) -> Bool {
        return param == Int32.max
    }

    public static func testOptionalReturnType() -> Int32? {
        return Int32.max
    }

    public static func testProtocolParam(_ callback: Int32TestParamProtocol) -> Bool {
        return callback.testParam(Int32.max)
    }

    public static func testProtocolReturnType(_ callback: Int32TestReturnTypeProtocol) -> Int32 {
        return callback.testReturnType()
    }

    public static func testProtocolOptionalParam(_ callback: Int32TestOptionalParamProtocol) -> Bool {
        return callback.testOptionalParam(Int32.max)
    }

    public static func testProtocolOptionalReturnType(_ callback: Int32TestOptionalReturnTypeProtocol) -> Int32? {
        return callback.testOptionalReturnType()
    }

    public static func testEncode() ->  Int32TestStruct {
        return Int32TestStruct()
    }

    public static func testDecode(_ value: Int32TestStruct) -> Bool {
        return value == Int32TestStruct()
    }


}