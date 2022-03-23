//
//  LoginView.swift
//  GoldenFarms
//
//  Created by Jacob Gardiner on 10/15/21.
//

import SwiftUI

struct LoginView: View {
    @State private var email: String = ""
    @State private var password: String = ""
    var body: some View {
//        NavigationView {
            VStack {
                Spacer()
                TextField("email", text: $email)
                    .padding(Edge.Set.horizontal)
                TextField("password", text: $password)
                    .padding(Edge.Set.horizontal)
                Spacer()
                NavigationLink(destination: MainView()) {
                    Text("Login")
                }
                Spacer()
            }.navigationTitle("Login")
//        }
    }
}

struct LoginView_Previews: PreviewProvider {
    static var previews: some View {
        LoginView()
    }
}
