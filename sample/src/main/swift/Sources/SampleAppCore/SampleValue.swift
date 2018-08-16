import Foundation

public struct SampleValue: Codable, Hashable {

	public var string: String

    public var integer: Int = 32
    public var int8: Int8 = 8
    public var int16: Int16 = 16
    public var int32: Int32 = 32
    public var int64: Int64 = 64

    public var uint: UInt = 32
    public var uint8: UInt8 = 8
    public var uint16: UInt16 = 16
    public var uint32: UInt32 = 32
    public var uint64: UInt64 = 64

    public var objectArray: [SampleValue] = []
    public var stringArray: [String] = ["one", "two", "free"]
    public var numberArray: [Int] = [1, 2, 3]
    public var arrayInArray: [[Int]] = [[1, 2, 3]]
    public var dictInArray: [[Int: Int]] = [[1: 1, 2: 2, 3: 3]]

    public var dictSampleClass: [String: SampleValue] = [:]
    public var dictStrings: [String: String] = ["oneKey": "oneValue"]
    public var dictNumbers: [Int: Int] = [123: 2]
    public var dict64Numbers: [UInt64: UInt64] = [123: 2]
    public var dictInDict: [UInt64: [UInt64: UInt64]] = [123: [123: 2]]
    public var arrayInDict: [UInt64: [UInt64]] = [123: [1, 2, 3]]

    public var set = Set<Int>(arrayLiteral: 1, 2, 3)
    public var setValues = Set<SampleValue>()

    public init(string: String) {
        self.string = string
    }
    
    public static func getRandomValue() -> SampleValue {
    	return SampleValue(string: UUID().uuidString)
    }

    public func saveValue() {
    	NSLog("save SampleValue: \(string)")
    }

    public func isSame(other: SampleValue) -> Bool {
    	return self == other
    }

    public static func funcThrows() throws {
    	throw NSError(domain: "Error", code: 1)
    }

    // MARK: - dump hashable impl
    public var hashValue: Int {
        return string.hashValue
    }

    public static func == (lhs: SampleValue, rhs: SampleValue) -> Bool {
        return lhs.string == rhs.string
    }

    public func copy() -> SampleValue {
        var copy = self
        copy.string = UUID().uuidString
        return copy
    }

}
