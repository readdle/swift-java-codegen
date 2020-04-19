//
// Created by Andrew on 2/4/18.
//

import Foundation

public struct BoolTestStruct: Codable, Hashable {
    public var yes: Bool = true
    public var no: Bool = false
    public var optional: Bool? = true
    public var optionalNil: Bool? = nil
}

public protocol BoolTestParamProtocol {
    func testParam(_ param: Bool) -> Bool
}

public protocol BoolTestReturnTypeProtocol {
    func testReturnType() -> Bool
}

public protocol BoolTestOptionalParamProtocol {
    func testOptionalParam(_ param: Bool?) -> Bool
}

public protocol BoolTestOptionalReturnTypeProtocol {
    func testOptionalReturnType() -> Bool?
}

public class BoolTest {

    public static func testYes() -> Bool {
        return true
    }

    public static func testNo() -> Bool {
        return false
    }

    public static func testParam(_ param: Bool) -> Bool {
        return param == true
    }

    public static func testReturnType() -> Bool {
        return true
    }

    public static func testOptionalParam(_ param: Bool?) -> Bool {
        return param == true
    }

    public static func testOptionalReturnType() -> Bool? {
        return true
    }

    public static func testProtocolParam(_ callback: BoolTestParamProtocol) -> Bool {
        return callback.testParam(true)
    }

    public static func testProtocolReturnType(_ callback: BoolTestReturnTypeProtocol) -> Bool {
        return callback.testReturnType()
    }

    public static func testProtocolOptionalParam(_ callback: BoolTestOptionalParamProtocol) -> Bool {
        return callback.testOptionalParam(true)
    }

    public static func testProtocolOptionalReturnType(_ callback: BoolTestOptionalReturnTypeProtocol) -> Bool? {
        return callback.testOptionalReturnType()
    }

    public static func testEncode() ->  BoolTestStruct {
        return BoolTestStruct()
    }

    public static func testDecode(_ value: BoolTestStruct) -> Bool {
        return value == BoolTestStruct()
    }

}