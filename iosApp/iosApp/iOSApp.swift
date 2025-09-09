import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init(){
       KoinInitializeKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}