//
// Created by Andrew on 2/4/18.
//

import Foundation

public struct DoubleTestStruct: Codable, Hashable {
    public var zero: Double = Double.zero
    public var infinity: Double = Double.infinity
    public var negativeInfinity: Double = -1 * Double.infinity
    public var optional: Double? = Double.zero
    public var optionalNil: Double? = nil
}

public protocol DoubleTestParamProtocol {
    func testParam(_ param: Double) -> Bool
}

public protocol DoubleTestReturnTypeProtocol {
    func testReturnType() -> Double
}

public protocol DoubleTestOptionalParamProtocol {
    func testOptionalParam(_ param: Double?) -> Bool
}

public protocol DoubleTestOptionalReturnTypeProtocol {
    func testOptionalReturnType() -> Double?
}

public class DoubleTest {

    public static func testZero() -> Double {
        return 0
    }

    public static func testInfinite() -> Double {
        return Double.infinity
    }

    public static func testNan() -> Double {
        return Double.nan
    }

    public static func testParam(_ param: Double) -> Bool {
        return param == Double.infinity
    }

    public static func testReturnType() -> Double {
        return Double.infinity
    }

    public static func testOptionalParam(_ param: Double?) -> Bool {
        return param == Double.infinity
    }

    public static func testOptionalReturnType() -> Double? {
        return Double.infinity
    }

    public static func testProtocolParam(_ callback: DoubleTestParamProtocol) -> Bool {
        return callback.testParam(Double.infinity)
    }

    public static func testProtocolReturnType(_ callback: DoubleTestReturnTypeProtocol) -> Double {
        return callback.testReturnType()
    }

    public static func testProtocolOptionalParam(_ callback: DoubleTestOptionalParamProtocol) -> Bool {
        return callback.testOptionalParam(Double.infinity)
    }

    public static func testProtocolOptionalReturnType(_ callback: DoubleTestOptionalReturnTypeProtocol) -> Double? {
        return callback.testOptionalReturnType()
    }

    public static func testEncode() ->  DoubleTestStruct {
        return DoubleTestStruct()
    }

    public static func testDecode(_ value: DoubleTestStruct) -> Bool {
        return value == DoubleTestStruct()
    }

}