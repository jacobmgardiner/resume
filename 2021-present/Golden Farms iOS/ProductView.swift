//
//  ProductView.swift
//  GoldenFarms
//
//  Created by Jacob Gardiner on 10/15/21.
//

import SwiftUI
import Firebase

class ProductViewState: ObservableObject {
    @Published var reviews: [Review] = []
    
    init(product: Product) {
        getReviews(product: product)
        print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        print(reviews)
    }
    
    func getReviews(product: Product) {
        let db = Firestore.firestore()
        db.collection("reviews").whereField("productId", isEqualTo: product.id).getDocuments() { (querySnapshot, err) in
            if let err = err {
                print("Error getting documents: \(err)")
            } else {
                for document in querySnapshot!.documents {
                    print("\(document.documentID) => \(document.data())")
                    self.reviews.append(Review(
                        id: document.documentID,
                        userId: document.data()["userId"] as! String,
                        productId: document.data()["productId"] as! String,
                        username: document.data()["username"] as! String,
                        title: document.data()["title"] as! String,
                        content: document.data()["content"] as! String,
                        image: document.data()["image"] as! String
                    ))
                }
                self.reviews = self.reviews
            }
        }
    }
}

struct ProductView: View {
    
    @Binding var showProductView: Bool
    
    @State var showCreateReviewView: Bool = false
    
    var product: Product
    
    @State var quantity: Int = 1
    
    @ObservedObject var state: ProductViewState
    
    init(showProductView: Binding<Bool>, product: Product) {
        self._showProductView = showProductView
        self.product = product
        state = ProductViewState(product: product)
    }
    
    var body: some View {
        VStack {
            ScrollView {
                VStack(alignment: .leading) {
                    FirebaseStorageImageView(imagePath: product.image)
        //                .frame(width: 300, height: 300)
                    HStack {
                        Text(product.title).bold().lineLimit(1)
                        Spacer()
                        Text(product.price.asPrice()).bold().lineLimit(1)
                    }
        //            .padding(.leading, 8).padding(.trailing, 8)
                    Text(product.description)
                }
                Spacer()
                HStack {
                    Picker("quantity", selection: $quantity) {
                        ForEach(0 ..< 10) {
                            Text("X \($0)")
                        }
                    }
                    Button(action: {
                        CartState.addProduct(product: product, quantity: quantity)
                        //TODO("show message about products being added to cart")
                        //TODO("message shows option to go to cart")
                    }) {
                        Text("Add to Cart")
                    }
                }
                
                Spacer(minLength: 30)
                
//                NavigationLink(destination: CreateReview(showView: $showView, product: product), isActive: $showView) {
//                    Text("Add Review")
//                }.isDetailLink(false)
                Button(action: { showCreateReviewView = true }) {
                    Text("Add Review")
                }
                
                Spacer(minLength: 90)
                
                VStack(alignment: .leading) {
                    Reviews(reviews: state.reviews)
                }.frame(minWidth: 0, maxWidth: .infinity)
            }
        }
        .padding(32)
        .navigationTitle(product.title)
        .sheet(isPresented: $showCreateReviewView) {
            CreateReview(showView: $showCreateReviewView, product: product)
        }
    }
    
    
}

//func addReview() {
//
//}

struct Reviews: View {
    var reviews: [Review]
    
    var body: some View {
        VStack(alignment: .leading) {
            Text("Reviews").bold()
                .frame(minWidth: 0, maxWidth: .infinity)
            Spacer()
            if reviews.isEmpty {
                Text("No Reviews Yet")
            } else {
                ForEach(reviews) { review in
                    ReviewView(review: review)
                }.padding(0)
            }
        }.frame(minWidth: 0, maxWidth: .infinity)
    }
}

struct ReviewView: View {
    var review: Review
    
    var body: some View {
        VStack(alignment: .leading) {
            Divider()
            Text(review.username).bold()
            Text(review.title)
            FirebaseStorageImageView(imagePath: review.image).padding(32)
            Text(review.content)
            Divider()
        }
    }
}

struct ProductView_Previews: PreviewProvider {
    static var previews: some View {
        ProductView(showProductView: .constant(false), product: testProduct)
    }
}
