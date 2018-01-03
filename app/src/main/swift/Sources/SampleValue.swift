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
}
