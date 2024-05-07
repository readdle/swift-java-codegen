//
// Created by Andrew Druk on 06.05.2024.
//

import Foundation

public enum SampleEnumWithValue: Codable {
    case none
    case stringValue(value: String)
    case integerValue(value: Int)
    case int8Value(value: Int8)
    case int16Value(value: Int16)
    case int32Value(value: Int32)
    case int64Value(value: Int64)
    case uintValue(value: UInt)
    case uint8Value(value: UInt8)
    case uint16Value(value: UInt16)
    case uint32Value(value: UInt32)
    case uint64Value(value: UInt64)
    case optionalIntegerValue(value: Int?)
    case optionalInt8Value(value: Int8?)
    case optionalInt16Value(value: Int16?)
    case optionalInt32Value(value: Int32?)
    case optionalInt64Value(value: Int64?)
    case optionalUintValue(value: UInt?)
    case optionalUint8Value(value: UInt8?)
    case optionalUint16Value(value: UInt16?)
    case optionalUint32Value(value: UInt32?)
    case optionalUint64Value(value: UInt64?)
    case objectArrayValue(value: [SampleValue])
    case stringArrayValue(value: [String])
    case numberArrayValue(value: [Int])
    case arrayInArrayValue(value: [[Int]])
    case dictInArrayValue(value: [[Int: Int]])
    case dictSampleClassValue(value: [String: SampleValue])
    case dictStringsValue(value: [String: String])
    case dictNumbersValue(value: [Int: Int])
    case dict64NumbersValue(value: [Int: Int])
    case dictInDictValue(value: [Int: [Int: Int]])
    case arrayInDictValue(value: [Int: [Int]])
    case setValue(value: Set<Int>)
    case setValuesValue(value: Set<SampleValue>)

    public static func copy(value: SampleEnumWithValue) -> SampleEnumWithValue {
        return value
    }
}