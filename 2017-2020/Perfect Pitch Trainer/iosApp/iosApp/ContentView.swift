import SwiftUI
import shared

let controller = MainController()

//var presentationImage: String? = nil
//var presentationSound: AudioController? = nil

//let presentation: Presentation = Presentation()

class Presentation: ObservableObject {
    
//    static let shared: Presentation = Presentation()
    
    @Published var visible = false
    var presentationImage: String? = "x"
    var presentationSound: AudioController? = nil
    
    init() {
        print("STARTING PRESENTATION!!!!!!")
        
        self.presentationSound = AudioController(
        fileUrl: AssetUtils().getNoteSoundUrl(note: NoteUtils.NoteLetter.c),
        listener: NoteListener())
        
        controller.observePresentationVisible(callback: {visible in
            self.visible = visible as! Bool
            //TODO play sound
            print("XXX [CONTENT VIEW][OBSERVER] PRESENTATION VISIBILITY: \(visible)")
            
            //TODO
//            if(visible) as! Bool {
//                print("XXX [CONTENT VIEW][OBSERVER] PLAYING PRESENTATION SOUND")
//                self.presentationSound?.play()
//            }
        }, completionHandler: {_,_ in print("COMPLETION HANDLER")})
        controller.observeNote {note in
            print("XXX [CONTENT VIEW] new note: \(note)")
            self.presentationImage = AssetUtils().getUnknown()
            self.presentationSound = AudioController(
                fileUrl: AssetUtils().getNoteSoundUrl(note: note),
                listener: NoteListener()
            )
        }
        controller.observeSucceeded {succeeded in
            print("XXX [CONTENT VIEW] result: \(succeeded)")
            var audioUrl = AssetUtils().getFailedSoundUrl()
            //TODO
            if succeeded as! Bool {
                audioUrl = AssetUtils().getSucceededSoundUrl()
                self.presentationImage = AssetUtils().getCheck()
            } else {
                self.presentationImage = AssetUtils().getX()
            }
            print("fileUrl: \(audioUrl)")
            self.presentationSound = AudioController(
                fileUrl: audioUrl,
                listener: ResultListener()
            )
        }
        
        //TODO
        controller.start()
    }
}

struct ContentView: View {
    @StateObject var presentation: Presentation = Presentation()
    
    init() {
        //TODO
        
//        controller.start()
    }
    
	var body: some View {
        GeometryReader { geo in
            Image("sky")
                .resizable()
                .scaledToFill()
                .frame(width: geo.size.width, height: geo.size.height)
                .clipped()
                .overlay(
                    LazyHStack(alignment: .bottom) {
                        ForEach(0...6, id: \.self) { count in
                                let imageName = AssetUtils().getButtonName(noteNumber: Int32(count))
//                            let imageName = "button_cat"
                            Image(imageName)
                                .resizable()
                                .frame(width: geo.size.width/7.5, height: geo.size.width/7.5, alignment: .center)
                                .offset(x: 0, y: geo.size.height/10 * -CGFloat(count))
                                .onTapGesture {
                                    onButtonClick(button: count)
                                }
                        }
                    }
                )
                .overlay(
                    PresentationOverlay(presentation: presentation)
                )
        }.edgesIgnoringSafeArea(.all)
	}
    
    private func onButtonClick(button: Int) {
        print("[CONTENT VIEW] button clicked")
        if (!controller.inputAllowed) {print("[CONTENT VIEW] input not allowed!");return}
        
        controller.onResponse(
            response: Int32(button),
            audioController: AudioController(
                fileUrl: AssetUtils().getNoteSoundUrl(note: NoteUtils().fromInt(note: Int32(button))),
                listener: nil
            )
        )
    }
}

struct PresentationOverlay: View {
    @ObservedObject var presentation: Presentation
//    @StateObject var p = presentation
    
    var body: some View {
//        if controller.showPresentation.value != nil {
        if presentation.visible {
            ZStack {
//                Image("background")
//                    .resizable()
//                    .scaledToFill()
//    //                .frame(width: geo.size.width, height: geo.size.height)
//                    .clipped()
                
                Image(presentation.presentationImage ?? "x")
                    .resizable()
                    .scaledToFit()
    //                .frame(width: geo.size.width, height: geo.size.height)
                    .clipped()
            }
            
            
            //TODO
            let _ = print("XXX [CONTENT VIEW][OBSERVER] PLAYING PRESENTATION SOUND")
            let _ = presentation.presentationSound?.play()
        } else {

        }
    }
}

class ResultListener: AudioManagerAudioEventListener {
    func onCancel() {
    }
    func onComplete() {
        print("XXX [CONTENT VIEW] finished result sound")
        controller.onResultComplete()
    }
    func onEnd() {
    }
    func onPause() {
    }
    func onStart() {
    }
}

class NoteListener: AudioManagerAudioEventListener {
    func onCancel() {
    }
    func onComplete() {
        print("XXX [CONTENT VIEW] finished note presentation")
        controller.onNotePresentationComplete()
    }
    func onEnd() {
    }
    func onPause() {
    }
    func onStart() {
    }
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
	ContentView()
	}
}

#if DEBUG
struct ContentView_Previews_2 : PreviewProvider {
    static var previews: some View {
        return ContentView().previewLayout(.fixed(width: 1792, height: 828))
    }
}
#endif
