import Foundation

public struct Issue31TestProgress: Codable {
    public let elapsed: Int
    public let total: Int

    public var percentage: Double {
        return 1.0
    }

    public init(elapsed: Int, total: Int) {
        self.elapsed = elapsed
        self.total = total
    }

    public func calculatePercentage() -> Double {
        return percentage
    }
}

public class Issue31ReferenceTestProgress: Codable {
    public let elapsed: Int
    public let total: Int

    public var percentage: Double {
        return 1.0
    }

    public init(elapsed: Int, total: Int) {
        self.elapsed = elapsed
        self.total = total
    }

    public func calculatePercentage() -> Double {
        return percentage
    }
}