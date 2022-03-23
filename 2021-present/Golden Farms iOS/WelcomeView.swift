//
//  ContentView.swift
//  GoldenFarms
//
//  Created by Jacob Gardiner on 10/15/21.
//

import SwiftUI

struct WelcomeView: View {
    var body: some View {
        NavigationView {
            VStack {
                Spacer()
                Image("logo")
    //            Spacer()
                Text("Welcome to Golden Farms")
                Spacer()
                NavigationLink(destination: LoginView()) {
                    Text("Login")
                }
    //            Spacer()
                NavigationLink(destination: SignupView()) {
                    Text("Signup")
                }
                Spacer()
            }
//            .navigationTitle("Welcome")
        }
    }
    
    func login() {
        
    }
    
    func signup() {
        
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        WelcomeView()
    }
}
