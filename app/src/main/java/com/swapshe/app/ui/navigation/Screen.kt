package com.swapshe.app.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Welcome : Screen("welcome")
    object Auth : Screen("auth")
    object StylePreference : Screen("onboarding/style")
    object SizeProfile : Screen("onboarding/size")
    object ColorPreference : Screen("onboarding/color")
    object SetupComplete : Screen("onboarding/complete")
    object Home : Screen("home")
    object Explore : Screen("explore")
    object ProductDetail : Screen("product/{id}") {
        fun createRoute(id: String) = "product/$id"
    }
    object SellerProfile : Screen("seller/{id}") {
        fun createRoute(id: String) = "seller/$id"
    }
    object Wishlist : Screen("wishlist")
    object CheckoutAddress : Screen("checkout/address")
    object CheckoutReview : Screen("checkout/review")
    object MockPayment : Screen("checkout/mock-payment")
    object OrderConfirmation : Screen("checkout/success/{orderId}") {
        fun createRoute(orderId: String) = "checkout/success/$orderId"
    }
    object MyOrders : Screen("orders")
    object OrderDetail : Screen("orders/{id}") {
        fun createRoute(id: String) = "orders/$id"
    }
    object Profile : Screen("profile")

    // Seller Journey (F22 - F25)
    object MyCloset : Screen("sell/closet")
    object CreateEditListing : Screen("sell/listing/editor?id={id}") {
        fun createRoute(id: String? = null) = if (id != null) "sell/listing/editor?id=$id" else "sell/listing/editor"
    }
    object SellerOrders : Screen("sell/orders")
    object SellerOrderDetail : Screen("sell/orders/{id}") {
        fun createRoute(id: String) = "sell/orders/$id"
    }

    // Direct Swap Journey (F26 - F29)
    object SwapDiscovery : Screen("swap")
    object CreateSwapOffer : Screen("swap/offer/new?targetId={targetId}") {
        fun createRoute(targetId: String) = "swap/offer/new?targetId=$targetId"
    }
    object SwapRequests : Screen("swap/requests?tab={tab}") {
        fun createRoute(initialTab: Int = 0) = "swap/requests?tab=$initialTab"
    }
    object SwapDetail : Screen("swap/{id}") {
        fun createRoute(id: String) = "swap/$id"
    }

    // Trust & Safety and Notifications (F30 - F31)
    object ReportDispute : Screen("report?targetType={targetType}&targetId={targetId}&targetTitle={targetTitle}") {
        fun createRoute(targetType: String, targetId: String, targetTitle: String) =
            "report?targetType=$targetType&targetId=$targetId&targetTitle=${java.net.URLEncoder.encode(targetTitle, "UTF-8")}"
    }
    object Notifications : Screen("notifications")

    // Flow G: Cashflow & Escrow Wallet
    object Wallet : Screen("wallet")

    // Flow H: eKYC & She-Trust Verification
    object Ekyc : Screen("ekyc")

    // Flow D: Extended Dispute Details & Negotiation
    object DisputeDetail : Screen("dispute/{id}") {
        fun createRoute(id: String) = "dispute/$id"
    }

    // Flow C & Flow H: Direct Chat & Anti-Scam Protection
    object Chat : Screen("chat?targetName={targetName}&productId={productId}") {
        fun createRoute(targetName: String = "Người bán", productId: String? = null) =
            "chat?targetName=${java.net.URLEncoder.encode(targetName, "UTF-8")}&productId=${productId ?: ""}"
    }

    // Flow D: Admin Arbitration & Platform Governance
    object AdminDashboard : Screen("admin/dashboard")
}
