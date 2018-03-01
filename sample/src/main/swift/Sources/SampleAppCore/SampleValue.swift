import Foundation

public struct SampleValue: Codable {

	public var str1: String
	public var str2: String
	public var str3: String
    
    public static func getRandomValue() -> SampleValue {
    	return SampleValue(str1: "1", str2: "2", str3: "3")
    }

    public func saveValue()  {
    	NSLog("SampleValue: \(str1), \(str2), \(str3)")
    }

    public func isSame(other: SampleValue) -> Bool {
    	return str1 == other.str1 && str2 == other.str2 && str3 == other.str3
    }

    public static func funcThrows() throws {
    	throw NSError(domain: "Error", code: 1)
    }
}
