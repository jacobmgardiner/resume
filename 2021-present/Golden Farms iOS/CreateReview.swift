//
//  CreateReview.swift
//  GoldenFarms
//
//  Created by Jacob Gardiner on 11/13/21.
//

import SwiftUI
import Firebase
import FirebaseStorage

struct CreateReview: View {
    @Binding var showView: Bool
    var product: Product
    @State var username: String = ""
    @State var title: String = ""
    @State var content: String = ""
    
    @State private var image: Image?
    @State var inputImage: UIImage?
    @State private var showingImagePicker = false
    
    var canSubmit: Bool {
        !username.isEmpty &&
        !title.isEmpty &&
        !content.isEmpty
    }
    
    var body: some View {
        VStack {
            Form {
                Text("Create Review")
                VStack(alignment: .leading) {
                    TextField("Name", text: $username)
                    TextField("Title", text: $title)
                    TextField("Content", text: $content)
                }.padding()
                
//                NavigationLink(destination: ImagePicker(image: self.$inputImage, showingImagePicker: $showingImagePicker), isActive: $showingImagePicker) {
                    ZStack {
                        if image != nil {
                            image?.resizable().scaledToFit()
                        } else {
                            Image(uiImage: productImagePlaceholder).resizable().scaledToFit()
                        }
                    }
                    .onTapGesture {
                        showingImagePicker = true
                    }
//                }.isDetailLink(false)
                
                Button(action: {
                    let rid = UUID().uuidString
                    storeReview(review: Review(
                        id: rid, userId: UUID().uuidString, productId: product.id, username: username, title: title, content: content, image: rid
                    ), image: inputImage)
                    
                    showView = false
                }) {
                    Text("Submit")
                }.disabled(!canSubmit)
            }
        }
        .sheet(isPresented: $showingImagePicker, onDismiss: loadImage) {
            ImagePicker(image: self.$inputImage, showingImagePicker: $showingImagePicker)
        }
    }
    
    func loadImage() {
        guard let inputImage = inputImage else {
            return
        }
        image = Image(uiImage: inputImage)
    }
}

func storeReview(review: Review, image: UIImage?) {
    let db = Firestore.firestore()
    var img = ""
    if image != nil { img = "reviews/\(review.image).png" }
    db.collection("reviews").document(review.id).setData([
        "id" : review.id,
        "userId" : review.userId,
        "productId" : review.productId,
        "username": review.username,
        "title": review.title,
        "content": review.content,
        "image": img
    ]) { err in
        if let err = err {
            print("Error writing document: \(err)")
        } else {
            print("Document successfully written!")
        }
    }
    
    
    
    
    //TODO("upload image")
    
    if image == nil { return }
    
    // Data in memory
    guard let data = image?.jpegData(compressionQuality: 0.2) else { return }

    // Create a reference to the file you want to upload
    let riversRef = Storage.storage().reference().child("reviews/\(review.image).png")

    // Upload the file to the path
    let uploadTask = riversRef.putData(data, metadata: nil) { (metadata, error) in
        print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        print(error)
        guard let metadata = metadata else {
        // Uh-oh, an error occurred!
          print("ERROR!!!!!!!!!!!!!!!!!!")
        return
        }
//      // Metadata contains file metadata such as size, content-type.
//      let size = metadata.size
//      // You can also access to download URL after upload.
//      riversRef.downloadURL { (url, error) in
//        guard let downloadURL = url else {
//          // Uh-oh, an error occurred!
//            print("ERROR!!!!!!!!!!!!!!!!!!")
//          return
//        }
//      }
    }
    
    uploadTask.enqueue()

}

struct CreateReview_Previews: PreviewProvider {
    static var previews: some View {
        CreateReview(showView:.constant(true), product: testProduct)
    }
}

struct ImagePicker: UIViewControllerRepresentable {
    class Coordinator: NSObject, UINavigationControllerDelegate, UIImagePickerControllerDelegate {
        let parent: ImagePicker

        init(_ parent: ImagePicker) {
            self.parent = parent
        }
        
        func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey: Any]) {
            if let uiImage = info[.originalImage] as? UIImage {
                parent.image = uiImage
            }

            parent.showingImagePicker = false
        }
    }
    
    @Binding var image: UIImage?
    @Binding var showingImagePicker: Bool
    
    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }
    
    func makeUIViewController(context: UIViewControllerRepresentableContext<ImagePicker>) -> UIImagePickerController {
        let picker = UIImagePickerController()
        picker.delegate = context.coordinator
        return picker
    }

    func updateUIViewController(_ uiViewController: UIImagePickerController, context: UIViewControllerRepresentableContext<ImagePicker>) {

    }
}
