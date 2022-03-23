//
//  product.swift
//  GoldenFarms
//
//  Created by Jacob Gardiner on 11/10/21.
//

import Foundation
import Firebase
import SwiftUI

class ShopData: ObservableObject {
    @Published var products: [Product] = []
    
    init() {
        let db = Firestore.firestore()
        
        db.collection("products").getDocuments() { (querySnapshot, err) in
            if let err = err {
                print("Error getting documents: \(err)")
            } else {
                for document in querySnapshot!.documents {
                    print("\(document.documentID) => \(document.data())")
                    self.products.append(Product(
                        id: document.documentID,
                        category: document.data()["categoryNum"] as! Int,
                        description: document.data()["description"] as! String,
                        image: document.data()["image"] as! String,
                        price: document.data()["price"] as! Double,
                        title: document.data()["title"] as! String
                    ))
                }
                self.products = self.products
            }
        }
    }
}

//func getReviews(arr: [[String : String]]) -> [Review] {
//    var reviews: [Review] = []
//    arr.forEach { review in
//        reviews.append(
//            Review(id: review["id"] ?? "", userId: review["userId"] ?? "", username: review["username"] ?? "", title: review["title"] ?? "", content: review["content"] ?? "")
//        )
//    }
//    return reviews
//}

struct Product: Hashable, Identifiable {
    var id: String
    var category: Int
    var description: String
    var image: String
    var price: Double
    var title: String
}

struct Review: Hashable, Identifiable {
    var id: String
    var userId: String
    var productId: String
    var username: String
    var title: String
    var content: String
    var image: String
}

var testReview: Review = Review(
    id: "",
    userId: "",
    productId: "",
    username: "John Smith",
    title: "Test Review",
    content: "This is a test Review",
    image: ""
)

var testProduct: Product = Product(
    id: "",
    category: 0,
    description: "This is a test description.",
    image: "",
    price: 25.99,
    title: "Test Product"
)

extension Double {
    func asPrice() -> String {
        var price = ""
        
        let formatter = NumberFormatter()
        formatter.locale = Locale.current // Change this to another locale if you want to force a specific locale, otherwise this is redundant as the current locale is the default already
        formatter.numberStyle = .currency
        if let formattedDouble = formatter.string(from: self as NSNumber) {
            price = formattedDouble
        }
        
        return price
    }
}
