//
// Created by Andrew on 2/4/18.
//

import Foundation

public protocol UIntTestProtocol {
    func uintZero() -> UInt
    func uintMax() -> UInt
    func uintMin() -> UInt
    func uintOptional() -> UInt?
    func uintOptionalNil() -> UInt?
}

public struct UIntTestStruct: Codable, Hashable, UIntTestProtocol {

    let zero = UInt.zero
    let max = UInt.max
    let min = UInt.min
    let optional: UInt? = UInt.zero
    let optionalNil: UInt? = nil

    public init() {}

    public func uintZero() -> UInt {
        return zero
    }

    public func uintMax() -> UInt {
        return max
    }

    public func uintMin() -> UInt {
        return min
    }

    public func uintOptional() -> UInt? {
        return optional
    }

    public func uintOptionalNil() -> UInt? {
        return optionalNil
    }

    public func testUIntTestProtocol(testProtocol: UIntTestProtocol) -> Bool {
        return testProtocol.uintZero() == zero &&
                testProtocol.uintMax() == max &&
                testProtocol.uintMin() == min &&
                testProtocol.uintOptional() == optional &&
                testProtocol.uintOptionalNil() == optionalNil
    }
}