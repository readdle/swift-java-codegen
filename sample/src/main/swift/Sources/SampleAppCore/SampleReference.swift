import Foundation

public protocol SampleDelegate: class {

    func setSampleValue(value: SampleValue?)

    func getSampleValue() -> SampleValue

    func funcWithNil() -> SampleValue?

    static func getTimestamp() -> Int64

    static func setTimestamp(_ value: Int64)

    func throwableFunc(_ flag: Bool) throws

    func throwableFuncWithReturnType(_ flag: Bool) throws -> String
}

public protocol SampleBlockDelegate {

    func onCall(_ pr1: Int, _ pr2: Int, _ pr3: Double, _ pr4: Double)

}

private extension SampleDelegate {

    func timeStampTest() -> Int64 {
        let timestamp = Self.getTimestamp()
        Self.setTimestamp(timestamp)
        return timestamp
    }
}

public class SampleReferenceEnclose {

    public init() {

    }
}

public class SampleReference {

    // TODO: need some extra work for returning abstract type
    public weak var delegate: SampleDelegate?
    public var string: String = "str1"
    public static var staticString: String = "str1"

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

    public func tick() -> Int64 {
        if let delegate = self.delegate {
            let value = getRandomValue()
            delegate.setSampleValue(value: value)
            return delegate.timeStampTest()
        }
        return  -1
    }

    public func tickWithBlock(_ block: SampleBlockDelegate) {
        // For table overflow test
        for i in 0 ..< 129 {
            block.onCall(i, i * 100, Double(i) * 10000.0, Double(i) * 1000000.0)
        }
    }

    public func funcWithNil() -> SampleValue? {
        delegate?.setSampleValue(value: nil)
        return try! delegate?.funcWithNil()
    }

    public func floatCheck(_ float: Float) -> Float {
        return float + 2.0
    }

    public func doubleCheck(_ double: Double) -> Double {
        return double + 2.0
    }

    public func exceptionCheck(_ var1: Error) -> Error {
        let domain = (var1 as! NSError).domain
        let code = (var1 as! NSError).code
        NSLog("Error: \(domain) \(code)")

        return var1
    }

    public func enclose(_ var1: SampleReferenceEnclose) -> SampleReferenceEnclose {
        return var1
    }

    public func funcWithBlock(completion: ((Error?, String?) -> String?)?) -> String? {
        return completion?(nil, "123") ?? "null"
    }

    public func getAbstractTypeList() -> [AbstractType] {
        return [FirstChild(), SecondChild(), ThirdChild(), FourthChild()]
    }

    public func getAbstractType() -> AbstractType {
        return FirstChild()
    }

    public func getFirstChild() -> FirstChild {
        return FirstChild()
    }

    public func getSecondChild() -> SecondChild {
        return SecondChild()
    }

    public func getThirdChild() -> ThirdChild {
        return ThirdChild()
    }

    public func getFourthChild() -> FourthChild {
        return FourthChild()
    }

    public func throwableFunc(_ delegate: SampleDelegate, _ flag: Bool) throws {
        return try delegate.throwableFunc(flag)
    }

    public func throwableFuncWithReturnType(_ delegate: SampleDelegate, _ flag: Bool) throws -> String {
        return try delegate.throwableFuncWithReturnType(flag)
    }

}
