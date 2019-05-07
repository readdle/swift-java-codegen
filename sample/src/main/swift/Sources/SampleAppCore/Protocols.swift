public protocol Provider {
    func fill(storage: Storage)
}

public protocol Storage {
    func add(_ str: String)
}

class StorageImpl: Storage {
    func add(_ str: String) {
        print(str)
    }
}

public class Service {
    public class func register(provider: Provider) {
        provider.fill(storage: StorageImpl())
    }
}