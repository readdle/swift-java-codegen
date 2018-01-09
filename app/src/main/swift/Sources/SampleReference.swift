import Foundation

public protocol SampleDelegate: class {

    func setSampleValue(_ value: SampleValue)

    func getSampleValue() -> SampleValue

    static func getTimestamp() -> Int64

    static func setTimestamp(_ value: Int64)
    
}

private extension SampleDelegate {

    func timeStampTest() -> Int64 {
        let timestamp = Self.getTimestamp()
        Self.setTimestamp(timestamp)
        return timestamp
    }
}

public class SampleReference {

    weak var delegate: SampleDelegate?

	public init() {
		
	}

    public func setDelegate(_ delegate: SampleDelegate?) {
        self.delegate = delegate
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

    public func tick() -> Int64 {
        if let delegate = self.delegate {
            let value = getRandomValue()
            delegate.setSampleValue(value)
            delegate.getSampleValue()
            return delegate.timeStampTest()
        }
        return  -1
    }
}
