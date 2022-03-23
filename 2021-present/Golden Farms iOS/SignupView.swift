//
//  SignupView.swift
//  GoldenFarms
//
//  Created by Jacob Gardiner on 10/15/21.
//

import SwiftUI

struct SignupView: View {
    @State private var email: String = ""
    @State private var first: String = ""
    @State private var last: String = ""
    @State private var password: String = ""
    @State private var password2: String = ""
    
    var body: some View {
//        NavigationView {
            VStack {
                Spacer()
//                Text("Signup")
//                Spacer()
                TextField("email", text: $email)
                    .padding(Edge.Set.horizontal)
                TextField("first", text: $first)
                    .padding(Edge.Set.horizontal)
                TextField("last", text: $last)
                    .padding(Edge.Set.horizontal)
                TextField("password", text: $password)
                    .padding(Edge.Set.horizontal)
                TextField("confirm password", text: $password2)
                    .padding(Edge.Set.horizontal)
                Spacer()
                NavigationLink(destination: MainView()) {
                    Text("Signup")
                }
                Spacer()
            }.navigationTitle("Signup")
//        }
    }
}

struct SignupView_Previews: PreviewProvider {
    static var previews: some View {
        SignupView()
    }
}
