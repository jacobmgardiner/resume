//
//  CartState.swift
//  GoldenFarms
//
//  Created by Jacob Gardiner on 11/9/21.
//

import Foundation

class CartState {
    private static var cartObservers: [CartObserver] = []
    static func addObserver(observer: CartObserver) {
        cartObservers.append(observer)
    }
    private static func notifyObservers() {
        cartObservers.forEach { observer in
            observer.onCartUpdate()
        }
    }
    
    static var products: [String : Product] = [:]
    static var quantities: [String : Int] = [:]
    static func addProduct(product: Product, quantity: Int) {
        if (products[product.id] == nil) {
            products[product.id] = product
        }
        quantities[product.id] = getProductQuantity(id: product.id) + quantity
        
        notifyObservers()
    }
    static func removeProduct(id: String) {
        products.removeValue(forKey: id)
        quantities.removeValue(forKey: id)
        
        notifyObservers()
    }

    static func clear() {
        products.removeAll()
        quantities.removeAll()
        
        notifyObservers()
    }

    static func getProductQuantity(id: String) -> Int {
        if (quantities[id] == nil) {
            quantities[id] = 0
        }
        return quantities[id] ?? 0
    }

    static func calculateTotalPrice() -> Double {
        var total = 0.0
        products.values.forEach { product in
            total += Double(products[product.id]?.price ?? 0) * Double(quantities[product.id] ?? 0)
        }
        return total
    }

    static func getNumberOfProducts() -> Int {
        return products.count
    }

    static func isEmpty() -> Bool {
        return getNumberOfProducts() == 0
    }

    static func setQuantity(product: Product, value: Int) {
        quantities[product.id] = value
        
        notifyObservers()
    }
    static func getQuantity(id: String) -> Int {
        return quantities[id] ?? 0
    }
}

protocol CartObserver: AnyObject {
    func onCartUpdate()
}
