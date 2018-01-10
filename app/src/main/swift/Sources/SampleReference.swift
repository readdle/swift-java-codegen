import Foundation

public protocol SampleDelegate: class {

    func setSampleValue(value: SampleValue?)

    func getSampleValue() -> SampleValue

    func funcWithNil() -> SampleValue?

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

    // TODO: need some extra work for returning abstract type
//    public func getDelegate() -> SampleDelegate?  {
//        return self.delegate
//    }

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
            delegate.setSampleValue(value: value)
            return delegate.timeStampTest()
        }
        return  -1
    }

    public func funcWithNil() -> SampleValue? {
        delegate?.setSampleValue(value: nil)
        return delegate?.funcWithNil()
    }
}
