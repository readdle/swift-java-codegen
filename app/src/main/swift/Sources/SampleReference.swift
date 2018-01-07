import Foundation

public class SampleReference {

	public init() {
		
	}

    public func getRandomValue() -> SampleValue {
    	return SampleValue(str1: "1", str2: "2", str3: "3")
    }

    public func saveValue(_ value: SampleValue)  {
    	NSLog("SampleValue: \(value.str1), \(value.str2), \(value.str3)")
    }

    public func funcThrows() throws {
    	throw NSError(domain: "Error", code: 1)
    }
}
