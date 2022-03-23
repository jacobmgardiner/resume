//
//  CheckoutView.swift
//  GoldenFarms
//
//  Created by Jacob Gardiner on 10/15/21.
//

import SwiftUI

struct CheckoutView: View {
    @ObservedObject var state = CartViewState()
    
    var body: some View {
        ScrollView {
        VStack {
            if state.empty {
                Text("Nothing in your cart!")
            } else {
                ForEach(Array(state.products.values)) { product in
                    CartItem(product: product, quantity: CartState.getQuantity(id: product.id))
                }.padding(0)
                
                Spacer()
                
                Text("Total: \(state.totalPrice.asPrice())").bold().padding()
                
                Button(action: {
                    
                }) {
                    Text("Checkout")
                }
                
                Spacer()
            }
        }
//        .navigationTitle("Your Cart")
        }
    }
}

struct CheckoutView_Previews: PreviewProvider {
    static var previews: some View {
        CheckoutView()
    }
}
