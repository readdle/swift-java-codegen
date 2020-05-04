//
// Created by Andrew on 2/4/18.
//

import Foundation

public struct FloatTestStruct: Codable, Hashable {
    public var zero: Float = Float.zero
    public var infinity: Float = Float.infinity
    public var negativeInfinity: Float = -1 * Float.infinity
    public var optional: Float? = Float.zero
    public var optionalNil: Float? = nil
}

public protocol FloatTestParamProtocol {
    func testParam(_ param: Float) -> Bool
}

public protocol FloatTestReturnTypeProtocol {
    func testReturnType() -> Float
}

public protocol FloatTestOptionalParamProtocol {
    func testOptionalParam(_ param: Float?) -> Bool
}

public protocol FloatTestOptionalReturnTypeProtocol {
    func testOptionalReturnType() -> Float?
}

public typealias FloatBlock = (_ value: Float) -> Float
public typealias OptionalFloatBlock = (_ value: Float?) -> Float?

public class FloatTest {

    public static func testZero() -> Float {
        return 0
    }

    public static func testInfinite() -> Float {
        return Float.infinity
    }

    public static func testNan() -> Float {
        return Float.nan
    }

    public static func testParam(_ param: Float) -> Bool {
        return param == Float.infinity
    }

    public static func testReturnType() -> Float {
        return Float.infinity
    }

    public static func testOptionalParam(_ param: Float?) -> Bool {
        return param == Float.infinity
    }

    public static func testOptionalReturnType() -> Float? {
        return Float.infinity
    }

    public static func testProtocolParam(_ callback: FloatTestParamProtocol) -> Bool {
        return callback.testParam(Float.infinity)
    }

    public static func testProtocolReturnType(_ callback: FloatTestReturnTypeProtocol) -> Float {
        return callback.testReturnType()
    }

    public static func testProtocolOptionalParam(_ callback: FloatTestOptionalParamProtocol) -> Bool {
        return callback.testOptionalParam(Float.infinity)
    }

    public static func testProtocolOptionalReturnType(_ callback: FloatTestOptionalReturnTypeProtocol) -> Float? {
        return callback.testOptionalReturnType()
    }

    public static func testEncode() ->  FloatTestStruct {
        return FloatTestStruct()
    }

    public static func testDecode(_ value: FloatTestStruct) -> Bool {
        return value == FloatTestStruct()
    }

    public static func testBlock(_ block: FloatBlock) -> Bool {
        let value = block(0)
        return value == 0
    }

    public static func testOptionalBlock(_ block: OptionalFloatBlock) -> Bool {
        let value = block(nil)
        return value == nil
    }

}