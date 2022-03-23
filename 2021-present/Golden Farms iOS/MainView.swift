//
//  TabView.swift
//  GoldenFarms
//
//  Created by Jacob Gardiner on 10/15/21.
//

import SwiftUI

struct MainView: View {
    @ObservedObject var state = CartViewState()
    
    var body: some View {
        NavigationView {
            VStack {
                TabView {
                    AboutView()
                        .tabItem {
                            Image(systemName: "house.fill")
                            Text("Home")
                        }
                    ShopView()
                        .tabItem {
                            Image(systemName: "bag")
                            Text("Shop")
                        }
                    if #available(iOS 15.0, *) {
                        CheckoutView()
                            .badge(state.products.count)
                            .tabItem {
                                Image(systemName: "cart")
                                Text("Checkout")
                            }
                    } else {
                        CheckoutView()
                            .tabItem {
                                Image(systemName: "cart")
                                Text("Checkout")
                            }
                    }
                }
            }
        }
    }
}

struct TabView_Previews: PreviewProvider {
    static var previews: some View {
        MainView()
    }
}
