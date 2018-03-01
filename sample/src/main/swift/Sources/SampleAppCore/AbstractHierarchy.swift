//
// Created by Andrew on 2/4/18.
//

import Foundation

public class AbstractType: Codable {

    // Mandatory field: every SwiftValue should have at least 1 filed
    public let str: String = ""

    public func basicMethod() -> String {
        return "AbstractType"
    }

}

public class FirstChild: AbstractType {

    public override func basicMethod() -> String {
        return "FirstChild"
    }

}

public class SecondChild: AbstractType {

    public override func basicMethod() -> String {
        return "SecondChild"
    }

}

public class ThirdChild: FirstChild {

    public override func basicMethod() -> String {
        return "ThirdChild"
    }

}

public class FourthChild: SecondChild {

    public override func basicMethod() -> String {
        return super.basicMethod()
    }

}

