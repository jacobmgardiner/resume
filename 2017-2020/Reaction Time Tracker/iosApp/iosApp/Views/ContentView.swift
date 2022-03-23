import SwiftUI
import shared

let app = Application()

struct ContentView: View {
    @Binding var currentView: Int
    
    @State var quality: Double = Double(mainController.currentLog?.quality ?? -1)
    @State var hours: Double = Double(mainController.currentLog?.hours ?? -1)
    @State var notes: String = mainController.currentLog?.note ?? "Ex: Started CPAP treatment..."

    var body: some View {
        ScrollView {
            VStack(alignment: .leading) {
                VStack(alignment: .leading) {
                    Text("Reaction Test")
                        .font(.title2)
                        .bold()
                        .padding(.bottom)
                        .padding(.top)
                    Text("Tap the screen as quickly as you can when it changes color.")
                        .padding(.bottom)
                    HStack {
                        Button(action: {
                            onMoreInfo()
                        }) {
                            Text("More Info")
                        }
                        Spacer()
                        Button(action: {
                            onStart()
                        }) {
                            Text("Start")
                        }
                    }
                }
                Divider()
                VStack(alignment: .leading) {
                    Text("Sleep Log")
                        .font(.title2)
                        .bold()
                        .padding(.bottom)
                        .padding(.top)
                    Text("Sleep Quality")
                    HStack {
                        Slider(value: $quality, in: 0...10)
                            .padding(.bottom)
                        Text("\(Int(quality))/10")
                            .padding(.leading)
                    }
                    Text("Hours of Sleep")
                    HStack {
                        Slider(value: $hours, in: 0...15)
                            .padding(.bottom)
                        Text("\(Int(hours)) hrs")
                            .padding(.leading)
                    }
                    Text("Notes")
                    TextEditor(text: $notes)
//                        .background(Color.black)
                    HStack {
                        Spacer()
                        Button(action: {
                                mainController.onRecord(
                            quality: Int32(quality),
                            hours: Int32(hours),
                            note: notes
                        )
                        }) {
                            Text("Record")
                        }
                    }
                }
                Divider()
                VStack(alignment: .leading) {
                    Text("Data")
                        .font(.title2)
                        .bold()
                        .padding(.bottom)
                        .padding(.top)
                    Text("Data view not yet implemented")
                        .padding(.bottom)
                    HStack {
                        Button(action: {onPickStartDate()}) {
                            Text("Pick start date")
                        }
                        Spacer()
                        Button(action: {onPickEndDate()}) {
                            Text("Pick end date")
                        }
                    }
                }
                Divider()
                VStack(alignment: .leading) {
                    Text("Send/Export Your Data")
                        .font(.title2)
                        .bold()
                        .padding(.bottom)
                        .padding(.top)
                    Text("Send all of your data in one of the following formats to yourself or someone else.")
                        .padding(.bottom)
                    HStack {
                        Spacer()
                        Button(action: {
                            print("SENDING DATA...")
                            onSendData()
                        }) {
                            Text("Send")
                        }
                    }
                }
            }
            .padding()
        }
    }
    
    func onStart() -> Void {
        self.currentView = TEST
    }
    
    func onMoreInfo() -> Void {
        if app.developer {
            DeveloperUtils().fakeMonth()
        }
    }

    func onPickStartDate() -> Void {
        
    }

    func onPickEndDate() -> Void {
        
    }

    func onSendData() -> Void {
        mainController.onSend()
    }
}

//struct ContentView_Previews: PreviewProvider {
//    static var previews: some View {
//        Group {
//            ContentView(startTest: $false)
//        }
//    }
//}
