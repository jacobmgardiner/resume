import Foundation
import SwiftUI
import shared

let MAIN: Int = 0
let TEST: Int = 1
let POST_TEST: Int = 2

let mainController = MainController()
var testController = TestController()

@main
struct ReactionTimeTrackerApp: App {
    @State var currentView = MAIN
    
    init() {
        onAppStart()
    }
    
    var body: some Scene {
        WindowGroup {
            switch currentView {
            case MAIN: ContentView(currentView: $currentView)
            case TEST: TestView(currentView: $currentView, model: Model())
            case POST_TEST: ContentView(currentView: $currentView)
            default: ContentView(currentView: $currentView)
            }
        }
    }
}

//struct ReactionTimeTrackerApp_Previews: PreviewProvider {
//    static var previews: some View {
//        Group {
//            ContentView()
//        }
//    }
//}

func onAppStart() {
    print("ON APP START")
    Application().onStart(databaseDriverFactory: DatabaseDriverFactory(), fileUtilsFactory: FileUtilsFactory())
}
