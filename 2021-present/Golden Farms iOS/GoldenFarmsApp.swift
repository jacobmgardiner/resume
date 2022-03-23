//
//  GoldenFarmsApp.swift
//  GoldenFarms
//
//  Created by Jacob Gardiner on 10/15/21.
//

import SwiftUI
import Firebase

@main
struct GoldenFarmsApp: App {
    init() {
        FirebaseApp.configure()
    }
    
    var body: some Scene {
        WindowGroup {
            MainView()
        }
    }
}
