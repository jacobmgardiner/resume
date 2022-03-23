//
//  CartViewState.swift
//  GoldenFarms
//
//  Created by Jacob Gardiner on 11/10/21.
//

import Foundation

class CartViewState: ObservableObject, CartObserver {
    @Published var products: [String : Product] = CartState.products
    @Published var quantities: [String : Int] = CartState.quantities
    @Published var empty = CartState.isEmpty()
    @Published var totalPrice = CartState.calculateTotalPrice()
    
    init() {
        CartState.addObserver(observer: self)
    }
    
    func onCartUpdate() {
        print("onCartUpdate")
        products = CartState.products
        quantities = CartState.quantities
        empty = CartState.isEmpty()
        totalPrice = CartState.calculateTotalPrice()
    }
}
