//
//  MapView.swift
//  GoldenFarms
//
//  Created by Jacob Gardiner on 10/15/21.
//

import SwiftUI
import MapKit
import Firebase

//struct AboutView: View {
//    var body: some View {
//        Text("test")
//    }
//}

struct AboutView: View {
    var body: some View {
        ScrollView {
//            FirebaseStorageImageView(imagePath: "home-slideshow/az.jpg")
            Divider()
            VStack(alignment: .leading) {
                Text("Golden Farms").bold()
                Spacer()
                Text("This will say something really nice about how the business is so local and family, and etc.")
                Spacer()
                Button(action: {  }) {
                    Text("Learn More")
                }
            }.padding()
            Divider()
            PickupMapView()
//            .navigationTitle("Home")
        }
        Spacer()
    }
}

class MapViewState: ObservableObject {
    @Published var region = MKCoordinateRegion(
        center: CLLocationCoordinate2D(latitude: 33.4152,
                                       longitude: -111.8315),
        latitudinalMeters: 750,
        longitudinalMeters: 750
    )
    @Published var pickupLocations: [Marker] = []

    init() {
        getPickupLocations()
    }

    func getPickupLocations() {
        let db = Firestore.firestore()
        
        db.collection("locations").getDocuments() { (querySnapshot, err) in
            if let err = err {
                print("Error getting documents: \(err)")
            } else {
                for document in querySnapshot!.documents {
                    print("\(document.documentID) => \(document.data())")
                    self.pickupLocations.append(
                        Marker(location: MapMarker(coordinate: CLLocationCoordinate2D(latitude: (document.data()["location"] as! GeoPoint).latitude, longitude: (document.data()["location"] as! GeoPoint).longitude)))
                    )
                }
                self.pickupLocations = self.pickupLocations
            }
        }
    }
}

struct PickupLocation {
    var id: String
    var location: GeoPoint
}

struct Marker: Identifiable {
    let id = UUID()
    var location: MapMarker
}

struct PickupMapView: View {
    @ObservedObject var state = MapViewState()

    var body: some View {
        VStack {
            Text("Pickup locations")
            Map(coordinateRegion: $state.region, showsUserLocation: true,
                annotationItems: state.pickupLocations) { marker in
                  marker.location
              }
                .frame(width: 300, height: 300)

        }
    }
}

struct AboutView_Previews: PreviewProvider {
    static var previews: some View {
        AboutView()
    }
}
