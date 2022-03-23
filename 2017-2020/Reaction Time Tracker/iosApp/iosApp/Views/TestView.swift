import Foundation
import SwiftUI
import shared

//let handler = { (_: KotlinUnit?, _: Error?) -> Void in
//
// }

class Model: ObservableObject {
    @Published var showPrompt: Bool = false
    @Published var timeText: String = "error"
}

struct TestView: View {
    @Binding var currentView: Int
//    @State var showPrompt: Bool = false
//    @State var timeText: String = "error"
    @ObservedObject var model: Model
    var testController: TestController
    
    init(currentView: Binding<Int>, model: Model) {
        self._currentView = currentView
        self.model = model
        
        testController.addTestEventListener(listener: MTestEventListener(currentView: $currentView, showPrompt: $model.showPrompt))
        testController.observableTimeElapsed.addObserver(observer: TimeObserver(timeText: $model.timeText))
        testController.onStart(completionHandler: handler)
    }
    
    var body: some View {
        if model.showPrompt {
            Text(model.timeText)
                .foregroundColor(Color.red)
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .background(Color.blue)
                .onTapGesture {
                    onTap()
                }
        } else {
            Text(model.timeText)
                .foregroundColor(Color.red)
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .background(Color.white)
                .onTapGesture {
                    onTap()
                }
        }
    }
    
    func onTap() {
        testController.onTap()
//        showPrompt = false
    }
    
    class TimeObserver: DataObserver<KotlinLong> {
        init(timeText: Binding<String>) {
            self._timeText = timeText
        }
        @Binding var timeText: String
        override func onUpdate(data: KotlinLong?) {
            timeText = String(Int64(truncating: data!))
        }
    }
    
    class MTestEventListener: TestEventListener {
        @Binding var currentView: Int
        @Binding var showPrompt: Bool
//        @Binding var timeText: String
        let testController: TestController
        
        init(testController: TestController, currentView: Binding<Int>, showPrompt: Binding<Bool>) {
            self.testController = testController
            self._currentView = currentView
            self._showPrompt = showPrompt
//            self._timeText = timeText
        }
        
        func onPrompt(prompt: Prompt) {
            print("ON PROMPT!!!")
            testController.onPrompt()
            showPrompt = true
        }
        
        func onPromptEnd(prompt: Prompt) {
            showPrompt = false
        }

        func onStart() {
            print("ON START!!!")
//             testController.observableTimeElapsed.addObserver(observer: TimeObserver(timeText: $timeText))
        }

        func onEnd() {
            print("ON END!!!")
            testController.onTestEnd()
            currentView = POST_TEST
        }
    }
}

//struct TestView_Previews: PreviewProvider {
//    static var previews: some View {
//        Group {
//            TestView()
//        }
//    }
//}
