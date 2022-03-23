//
//  ProductPreviewView.swift
//  GoldenFarms
//
//  Created by Jacob Gardiner on 10/15/21.
//

import SwiftUI
import FirebaseStorage

struct ProductPreviewView: View {
    var product: Product
    
    var dimensions: Double = 128
    
    @State var showProductView: Bool = false
    
    var body: some View {
//        NavigationLink(destination: ProductView(product: product)) {
            VStack(alignment: .leading) {
                FirebaseStorageImageView(imagePath: product.image)
                    .frame(width: dimensions, height: dimensions)
//                    .aspectRatio(contentMode: .fit)
                HStack {
                    Text(product.title).bold().lineLimit(1)
                    Spacer()
                    Text(product.price.asPrice()).bold().lineLimit(1)
                }
                Text(product.description).lineLimit(3)
            }
                .padding(.all)
                .onTapGesture {
                    showProductView = true
                }
                .sheet(isPresented: $showProductView) {
                    ProductView(showProductView: $showProductView, product: product)
                }
//        }.buttonStyle(PlainButtonStyle())
    }
}

struct ProductPreviewView_Previews: PreviewProvider {
    static var previews: some View {
        ProductPreviewView(product: testProduct)
    }
}
