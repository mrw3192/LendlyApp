package com.example.lendlyapp.navigation

/**
 * App-wide Navigation Route Keys for Navigation Compose.
 */
sealed class AppDestination(val route: String) {
    data object Splash : AppDestination("splash")
    data object Onboarding : AppDestination("onboarding")
    data object Login : AppDestination("login")
    data object Register : AppDestination("register")
    data object Home : AppDestination("home")
    data object VerifyPhone : AppDestination("verifyPhone")
    data object SmsVerification : AppDestination("smsVerification")
    data object ProfileDetail : AppDestination("profileDetail")
    data object CreatePassword : AppDestination("createPassword")
    data object Done : AppDestination("done")
    
    // KYC / Optional Verification Routes
    data object IdVerification : AppDestination("idVerification")
    data object FaceRecognition : AppDestination("faceRecognition")
    data object Signature : AppDestination("signature")
    data object Verified : AppDestination("verified")
}
