//
//  FirebaseStorageImageView.swift
//  GoldenFarms
//
//  Created by Jacob Gardiner on 11/10/21.
//

import SwiftUI

let productImagePlaceholder = UIImage(named: "product-placeholder")!

struct FirebaseStorageImageView: View {
    init(imagePath: String) {
        self.imageLoader = FirebaseStorageImageLoader(imagePath)
    }

    @ObservedObject private var imageLoader : FirebaseStorageImageLoader
    
    var image: UIImage? {
        UIImage(data: imageLoader.image ?? Data())
    }
    
    var body: some View {
        if imageLoader.loaded {
            Image(uiImage: image ?? productImagePlaceholder)
                .resizable()
                .aspectRatio(contentMode: .fit)
        } else {
            ProgressView()
        }
    }
}

struct FirebaseStorageImageView_Previews: PreviewProvider {
    static var previews: some View {
        FirebaseStorageImageView(imagePath: "")
    }
}
