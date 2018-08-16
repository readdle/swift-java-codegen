//
// Created by Andrew on 2/4/18.
//

import AnyCodable
import Foundation

public class SwiftEnvironment {

    public static func initEnvironment() {
        AnyCodable.RegisterType(AbstractType.self)
        AnyCodable.RegisterType(FirstChild.self)
        AnyCodable.RegisterType(SecondChild.self)
        AnyCodable.RegisterType(ThirdChild.self)
        AnyCodable.RegisterType(FourthChild.self)
    }

}
