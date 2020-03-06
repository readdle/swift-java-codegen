//
// Created by Andrew on 2/4/18.
//

import Foundation

public struct Int8TestStruct: Codable, Hashable {
    public var zero: Int8 = Int8.zero
    public var max: Int8 = Int8.max
    public var min: Int8 = Int8.min
    public var optional: Int8? = Int8.zero
    public var optionalNil: Int8? = nil
}

public protocol Int8TestParamProtocol {
    func testParam(_ param: Int8) -> Bool
}

public protocol Int8TestReturnTypeProtocol {
    func testReturnType() -> Int8
}

public protocol Int8TestOptionalParamProtocol {
    func testOptionalParam(_ param: Int8?) -> Bool
}

public protocol Int8TestOptionalReturnTypeProtocol {
    func testOptionalReturnType() -> Int8?
}

public class Int8Test {

    public static func testZero() -> Int8 {
        return 0
    }

    public static func testMin() -> Int8 {
        return Int8.min
    }

    public static func testMax() -> Int8 {
        return Int8.max
    }

    public static func testParam(_ param: Int8) -> Bool {
        return param == Int8.max
    }

    public static func testReturnType() -> Int8 {
        return Int8.max
    }

    public static func testOptionalParam(_ param: Int8?) -> Bool {
        return param == Int8.max
    }

    public static func testOptionalReturnType() -> Int8? {
        return Int8.max
    }

    public static func testProtocolParam(_ callback: Int8TestParamProtocol) -> Bool {
        return callback.testParam(Int8.max)
    }

    public static func testProtocolReturnType(_ callback: Int8TestReturnTypeProtocol) -> Int8 {
        return callback.testReturnType()
    }

    public static func testProtocolOptionalParam(_ callback: Int8TestOptionalParamProtocol) -> Bool {
        return callback.testOptionalParam(Int8.max)
    }

    public static func testProtocolOptionalReturnType(_ callback: Int8TestOptionalReturnTypeProtocol) -> Int8? {
        return callback.testOptionalReturnType()
    }

    public static func testEncode() ->  Int8TestStruct {
        return Int8TestStruct()
    }

    public static func testDecode(_ value: Int8TestStruct) -> Bool {
        NSLog("!!!\(value.max)!!!")
        return value == Int8TestStruct()
    }


}