//
// Created by Andrew on 2/4/18.
//

import Foundation

public struct IntTestStruct: Codable, Hashable {
    public var zero: Int = Int.zero
    public var max: Int = Int(Int32.max)
    public var min: Int = Int(Int32.min)
    public var optional: Int? = Int.zero
    public var optionalNil: Int? = nil
}

public protocol IntTestParamProtocol {
    func testParam(_ param: Int) -> Bool
}

public protocol IntTestReturnTypeProtocol {
    func testReturnType() -> Int
}

public protocol IntTestOptionalParamProtocol {
    func testOptionalParam(_ param: Int?) -> Bool
}

public protocol IntTestOptionalReturnTypeProtocol {
    func testOptionalReturnType() -> Int?
}

public class IntTest {

    public static func testZero() -> Int {
        return 0
    }

    public static func testMin() -> Int {
        return Int.min
    }

    public static func testMax() -> Int {
        return Int.max
    }

    public static func testMin32() -> Int {
        return Int(Int32.min)
    }

    public static func testMax32() -> Int {
        return Int(Int32.max)
    }

    public static func testParam(_ param: Int) -> Bool {
        return param == Int(Int32.max)
    }

    public static func testReturnType() -> Int {
        return Int(Int32.max)
    }

    public static func testOptionalParam(_ param: Int?) -> Bool {
        return param == Int(Int32.max)
    }

    public static func testOptionalReturnType() -> Int? {
        return Int(Int32.max)
    }

    public static func testProtocolParam(_ callback: IntTestParamProtocol) -> Bool {
        return callback.testParam(Int(Int32.max))
    }

    public static func testProtocolReturnType(_ callback: IntTestReturnTypeProtocol) -> Int {
        return callback.testReturnType()
    }

    public static func testProtocolOptionalParam(_ callback: IntTestOptionalParamProtocol) -> Bool {
        return callback.testOptionalParam(Int(Int32.max))
    }

    public static func testProtocolOptionalReturnType(_ callback: IntTestOptionalReturnTypeProtocol) -> Int? {
        return callback.testOptionalReturnType()
    }

    public static func testEncode() ->  IntTestStruct {
        return IntTestStruct()
    }

    public static func testDecode(_ value: IntTestStruct) -> Bool {
        return value == IntTestStruct()
    }


}