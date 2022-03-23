//
//  FirebaseSorageImageLoader.swift
//  GoldenFarms
//
//  Created by Jacob Gardiner on 11/10/21.
//

import SwiftUI
import Combine
import FirebaseStorage

class FirebaseStorageImageLoader : ObservableObject {
    @Published var image: Data? = nil
    @Published var loaded: Bool = false
    @Published var error: Error? = nil

    init(_ imagePath: String){
        let ref = Storage.storage().reference().child("\(imagePath)")
        ref.getData(maxSize: 20 * 1024 * 1024) { data, error in
            self.loaded = true
            if let error = error {
                self.error = error
                print(error)
            } else {
                self.image = data!
            }
        }
    }
}
