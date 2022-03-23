//
//  ShopView.swift
//  GoldenFarms
//
//  Created by Jacob Gardiner on 10/15/21.
//

import SwiftUI

struct ShopView: View {
    @ObservedObject var shopData: ShopData = ShopData()
    
    var columns = [
            GridItem(.adaptive(minimum: 128))
        ]
    
    var body: some View {
        VStack {
            ScrollView {
                LazyVGrid(columns: columns, spacing: 20) {
                    ForEach(shopData.products, id: \.self) { product in
                        ProductPreviewView(product: product)
                    }
                }
                .padding(.horizontal)
            }
        }
//        .navigationTitle("Shop")
    }
}

struct ShopView_Previews: PreviewProvider {
    static var previews: some View {
        ShopView()
    }
}


