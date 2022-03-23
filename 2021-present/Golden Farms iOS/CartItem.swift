//
//  CartItem.swift
//  GoldenFarms
//
//  Created by Jacob Gardiner on 11/11/21.
//

import SwiftUI

struct CartItem: View {
    var product: Product
    
    @State var quantity: Int
    
    @State var showProductView = false
    
    var body: some View {
        VStack {
            Divider()
            HStack {
                FirebaseStorageImageView(imagePath: product.image)
                    .frame(width: 50, height: 50)
                VStack(alignment: .leading) {
                    Text(product.title).bold()
                    Text(product.price.asPrice())
                    NavigationLink(destination: ProductView(showProductView: $showProductView, product: product)) {
                        Text("View Product")
                    }
                }
                
                Spacer()
                
                VStack {
                    Picker("quantity", selection: $quantity) {
                        ForEach(0 ..< 10) {
                            Text("X \($0)")
                        }
                    }
                    .onReceive([self.quantity].publisher.first()) { (value) in
                        CartState.setQuantity(product: product, value: quantity)
                    }
                    Text((Double(quantity) * product.price).asPrice()).bold()
                }
                
                Spacer()
                
                VStack {
                    Button(action: {
                        print("remove!!!!!!!!!!!!!!!!!")
                        CartState.removeProduct(id: product.id)
                    }) {
                        Image(systemName: "x.square")
                    }
                    
//                    Spacer()
                }
            }
            .padding()
//            .padding(.bottom).padding(.top)
            Divider()
        }.padding(0)
    }
}

struct CartItem_Previews: PreviewProvider {
    static var previews: some View {
        CartItem(product: testProduct, quantity: 2)
    }
}
