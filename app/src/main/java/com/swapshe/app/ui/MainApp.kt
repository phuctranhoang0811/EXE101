package com.swapshe.app.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.foundation.layout.Row
import com.swapshe.app.data.SwapSheRepository
import com.swapshe.app.theme.Primary
import com.swapshe.app.ui.navigation.AppNavigationRail
import com.swapshe.app.ui.navigation.BottomBar
import com.swapshe.app.ui.navigation.Screen
import com.swapshe.app.ui.responsive.ProvideWindowSizeInfo
import com.swapshe.app.ui.responsive.rememberWindowSizeInfo
import com.swapshe.app.ui.components.FloatingDemoControlBar
import com.swapshe.app.ui.screens.AdminDashboardScreen
import com.swapshe.app.ui.screens.AuthScreen
import com.swapshe.app.ui.screens.ChatScreen
import com.swapshe.app.ui.screens.ColorPreferenceScreen
import com.swapshe.app.ui.screens.CreateEditListingScreen
import com.swapshe.app.ui.screens.CreateSwapOfferScreen
import com.swapshe.app.ui.screens.DisputeDetailScreen
import com.swapshe.app.ui.screens.EkycScreen
import com.swapshe.app.ui.screens.ExploreScreen
import com.swapshe.app.ui.screens.HomeScreen
import com.swapshe.app.ui.screens.MockPaymentScreen
import com.swapshe.app.ui.screens.MyClosetScreen
import com.swapshe.app.ui.screens.MyOrdersScreen
import com.swapshe.app.ui.screens.NotificationCenterScreen
import com.swapshe.app.ui.screens.OrderConfirmationScreen
import com.swapshe.app.ui.screens.OrderDetailScreen
import com.swapshe.app.ui.screens.ProductDetailScreen
import com.swapshe.app.ui.screens.ProfileScreen
import com.swapshe.app.ui.screens.ReportDisputeScreen
import com.swapshe.app.ui.screens.ReviewPaymentScreen
import com.swapshe.app.ui.screens.SellerOrderDetailScreen
import com.swapshe.app.ui.screens.SellerOrdersScreen
import com.swapshe.app.ui.screens.SellerProfileScreen
import com.swapshe.app.ui.screens.SetupCompleteScreen
import com.swapshe.app.ui.screens.ShippingAddressScreen
import com.swapshe.app.ui.screens.SizeProfileScreen
import com.swapshe.app.ui.screens.SplashScreen
import com.swapshe.app.ui.screens.StylePreferenceScreen
import com.swapshe.app.ui.screens.SwapDetailScreen
import com.swapshe.app.ui.screens.SwapDiscoveryScreen
import com.swapshe.app.ui.screens.SwapRequestsScreen
import com.swapshe.app.ui.screens.WalletScreen
import com.swapshe.app.ui.screens.WelcomeScreen
import com.swapshe.app.ui.screens.WishlistScreen
import kotlinx.coroutines.delay

@Composable
fun MainApp() {
    ProvideWindowSizeInfo {
        MainAppContent()
    }
}

@Composable
private fun MainAppContent() {
    val windowSizeInfo = rememberWindowSizeInfo()
    val navController = rememberNavController()
    val repository = com.swapshe.app.di.AppContainer.repository
    val toastMessage by repository.toastMessage.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Show Bottom Navigation on main exploration & top-level tabs (Sitemap v2: Home, Explore, Sell, Swap, Profile)
    val showNav = currentRoute in listOf(
        Screen.Home.route,
        Screen.Explore.route,
        Screen.MyCloset.route,
        Screen.SwapDiscovery.route,
        Screen.Profile.route
    )
    val isWideScreen = windowSizeInfo.isWideScreen

    val onNavigateTab: (String) -> Unit = { route ->
        if (route != currentRoute) {
            if (route == Screen.Home.route) {
                val popped = navController.popBackStack(Screen.Home.route, inclusive = false)
                if (!popped) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            } else {
                navController.navigate(route) {
                    popUpTo(Screen.Home.route) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    }

    // Auto-clear toast
    LaunchedEffect(toastMessage) {
        if (toastMessage != null) {
            delay(3500)
            repository.clearToast()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxSize()) {
            if (showNav && isWideScreen) {
                AppNavigationRail(
                    currentRoute = currentRoute,
                    onNavigate = onNavigateTab,
                    onShowPlaceholderToast = { msg -> repository.showToast(msg) }
                )
            }

            Scaffold(
                modifier = Modifier.weight(1f),
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                bottomBar = {}
            ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(320, easing = FastOutSlowInEasing)
                    ) + fadeIn(tween(250))
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(320, easing = FastOutSlowInEasing)
                    ) + fadeOut(tween(250))
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(320, easing = FastOutSlowInEasing)
                    ) + fadeIn(tween(250))
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(320, easing = FastOutSlowInEasing)
                    ) + fadeOut(tween(250))
                },
                modifier = Modifier.fillMaxSize()
            ) {
                // F01: Splash
                composable(Screen.Splash.route) {
                    SplashScreen(
                        onNavigateNext = { isLoggedIn ->
                            if (isLoggedIn) {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Splash.route) { inclusive = true }
                                }
                            } else {
                                navController.navigate(Screen.Welcome.route) {
                                    popUpTo(Screen.Splash.route) { inclusive = true }
                                }
                            }
                        }
                    )
                }

                // F02: Welcome
                composable(Screen.Welcome.route) {
                    WelcomeScreen(
                        onNavigateToAuth = { _ ->
                            navController.navigate(Screen.Auth.route)
                        },
                        onNavigateToGuestHome = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Welcome.route) { inclusive = true }
                            }
                        }
                    )
                }

                // F03: Auth
                composable(Screen.Auth.route) {
                    AuthScreen(
                        initialIsRegister = false,
                        onAuthSuccess = { isNewUser, hasPendingCheckout ->
                            if (hasPendingCheckout) {
                                navController.navigate(Screen.CheckoutAddress.route) {
                                    popUpTo(Screen.Auth.route) { inclusive = true }
                                }
                            } else if (isNewUser) {
                                navController.navigate(Screen.StylePreference.route) {
                                    popUpTo(Screen.Auth.route) { inclusive = true }
                                }
                            } else {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Auth.route) { inclusive = true }
                                }
                            }
                        },
                        onBack = { navController.popBackStack() }
                    )
                }

                // F04: Style Preference
                composable(Screen.StylePreference.route) {
                    StylePreferenceScreen(
                        onContinue = { navController.navigate(Screen.SizeProfile.route) },
                        onSkip = { navController.navigate(Screen.SizeProfile.route) },
                        onBack = { navController.popBackStack() }
                    )
                }

                // F05: Size Profile
                composable(Screen.SizeProfile.route) {
                    SizeProfileScreen(
                        onContinue = { navController.navigate(Screen.ColorPreference.route) },
                        onSkip = { navController.navigate(Screen.ColorPreference.route) },
                        onBack = { navController.popBackStack() }
                    )
                }

                // F06: Color Preference
                composable(Screen.ColorPreference.route) {
                    ColorPreferenceScreen(
                        onFinish = { navController.navigate(Screen.SetupComplete.route) },
                        onSkip = { navController.navigate(Screen.Home.route) },
                        onBack = { navController.popBackStack() }
                    )
                }

                // F07: Setup Completed
                composable(Screen.SetupComplete.route) {
                    SetupCompleteScreen(
                        onExplorePicks = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Welcome.route) { inclusive = true }
                            }
                        }
                    )
                }

                // F08: Home
                composable(Screen.Home.route) {
                    HomeScreen(
                        onNavigateToProduct = { id ->
                            navController.navigate(Screen.ProductDetail.createRoute(id))
                        },
                        onNavigateToExplore = {
                            navController.navigate(Screen.Explore.route)
                        },
                        onNavigateToWishlist = {
                            navController.navigate(Screen.Wishlist.route)
                        }
                    )
                }

                // F09 & F10: Explore
                composable(Screen.Explore.route) {
                    ExploreScreen(
                        onNavigateToProduct = { id ->
                            navController.navigate(Screen.ProductDetail.createRoute(id))
                        },
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }

                // F11: Product Detail
                composable(
                    route = Screen.ProductDetail.route,
                    arguments = listOf(navArgument("id") { type = NavType.StringType })
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("id") ?: ""
                    ProductDetailScreen(
                        productId = productId,
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToCheckout = {
                            navController.navigate(Screen.CheckoutAddress.route)
                        },
                        onNavigateToSeller = { sellerId ->
                            navController.navigate(Screen.SellerProfile.createRoute(sellerId))
                        },
                        onNavigateToAuth = {
                            navController.navigate(Screen.Auth.route)
                        },
                        onNavigateToSwapOffer = { id ->
                            navController.navigate(Screen.CreateSwapOffer.createRoute(id))
                        },
                        onNavigateToReport = { type, id ->
                            navController.navigate(Screen.ReportDispute.createRoute(type, id, "Sản phẩm #$id"))
                        },
                        onNavigateToChat = { sellerName, id ->
                            navController.navigate(Screen.Chat.createRoute(sellerName, id))
                        },
                        onNavigateToEkyc = {
                            navController.navigate(Screen.Ekyc.route)
                        }
                    )
                }

                // F12: Seller Profile
                composable(
                    route = Screen.SellerProfile.route,
                    arguments = listOf(navArgument("id") { type = NavType.StringType })
                ) { backStackEntry ->
                    val sellerId = backStackEntry.arguments?.getString("id") ?: ""
                    SellerProfileScreen(
                        sellerId = sellerId,
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToProduct = { id ->
                            navController.navigate(Screen.ProductDetail.createRoute(id))
                        }
                    )
                }

                // F13: Wishlist
                composable(Screen.Wishlist.route) {
                    WishlistScreen(
                        onNavigateToProduct = { id ->
                            navController.navigate(Screen.ProductDetail.createRoute(id))
                        },
                        onExploreClick = {
                            navController.navigate(Screen.Explore.route)
                        }
                    )
                }

                // F14: Checkout Shipping Address
                composable(Screen.CheckoutAddress.route) {
                    ShippingAddressScreen(
                        onNavigateBack = { navController.popBackStack() },
                        onContinueToReview = {
                            navController.navigate(Screen.CheckoutReview.route)
                        }
                    )
                }

                // F15: Checkout Review & Payment
                composable(Screen.CheckoutReview.route) {
                    ReviewPaymentScreen(
                        onNavigateBack = { navController.popBackStack() },
                        onEditAddress = { navController.popBackStack() },
                        onProceedToMockPayment = {
                            navController.navigate(Screen.MockPayment.route)
                        }
                    )
                }

                // F16: Mock Payment
                composable(Screen.MockPayment.route) {
                    MockPaymentScreen(
                        onPaymentSuccess = { orderId ->
                            navController.navigate(Screen.OrderConfirmation.createRoute(orderId)) {
                                popUpTo(Screen.CheckoutAddress.route) { inclusive = true }
                            }
                        },
                        onNavigateBackToReview = { navController.popBackStack() }
                    )
                }

                // F17: Order Confirmation
                composable(
                    route = Screen.OrderConfirmation.route,
                    arguments = listOf(navArgument("orderId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
                    OrderConfirmationScreen(
                        orderId = orderId,
                        onViewOrder = { id ->
                            navController.navigate(Screen.OrderDetail.createRoute(id)) {
                                popUpTo(Screen.Home.route)
                            }
                        },
                        onContinueShopping = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        }
                    )
                }

                // F18: My Orders
                composable(Screen.MyOrders.route) {
                    MyOrdersScreen(
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToOrderDetail = { id ->
                            navController.navigate(Screen.OrderDetail.createRoute(id))
                        }
                    )
                }

                // F19: Order Detail & Tracking
                composable(
                    route = Screen.OrderDetail.route,
                    arguments = listOf(navArgument("id") { type = NavType.StringType })
                ) { backStackEntry ->
                    val orderId = backStackEntry.arguments?.getString("id") ?: ""
                    OrderDetailScreen(
                        orderId = orderId,
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToReport = { type, id ->
                            navController.navigate(Screen.ReportDispute.createRoute(type, id, "Đơn mua #$id"))
                        },
                        onNavigateToDisputeDetail = { disputeId ->
                            navController.navigate(Screen.DisputeDetail.createRoute(disputeId))
                        }
                    )
                }

                // F21: Profile
                composable(Screen.Profile.route) {
                    ProfileScreen(
                        onNavigateToOrders = { navController.navigate(Screen.MyOrders.route) },
                        onNavigateToWishlist = { navController.navigate(Screen.Wishlist.route) },
                        onNavigateToStyleOnboarding = { navController.navigate(Screen.StylePreference.route) },
                        onNavigateToAuth = { navController.navigate(Screen.Auth.route) },
                        onNavigateToCloset = { navController.navigate(Screen.MyCloset.route) },
                        onNavigateToSwapRequests = { navController.navigate(Screen.SwapRequests.route) },
                        onNavigateToNotifications = { navController.navigate(Screen.Notifications.route) },
                        onNavigateToSellerOrders = { navController.navigate(Screen.SellerOrders.route) },
                        onNavigateToWallet = { navController.navigate(Screen.Wallet.route) },
                        onNavigateToEkyc = { navController.navigate(Screen.Ekyc.route) }
                    )
                }

                // --- SELLER JOURNEY (F22 - F25) ---
                // F22: My Closet
                composable(Screen.MyCloset.route) {
                    MyClosetScreen(
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToCreateListing = { id ->
                            navController.navigate(Screen.CreateEditListing.createRoute(id))
                        },
                        onNavigateToSellerOrders = {
                            navController.navigate(Screen.SellerOrders.route)
                        },
                        onPreviewProduct = { id ->
                            navController.navigate(Screen.ProductDetail.createRoute(id))
                        },
                        onNavigateToEkyc = {
                            navController.navigate(Screen.Ekyc.route)
                        }
                    )
                }

                // F23: Create / Edit Listing
                composable(
                    route = Screen.CreateEditListing.route,
                    arguments = listOf(navArgument("id") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    })
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("id")
                    CreateEditListingScreen(
                        productId = productId,
                        onNavigateBack = { navController.popBackStack() },
                        onSuccess = { navController.popBackStack() }
                    )
                }

                // F24: Seller Orders
                composable(Screen.SellerOrders.route) {
                    SellerOrdersScreen(
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToOrderDetail = { id ->
                            navController.navigate(Screen.SellerOrderDetail.createRoute(id))
                        }
                    )
                }

                // F25: Seller Order Detail
                composable(
                    route = Screen.SellerOrderDetail.route,
                    arguments = listOf(navArgument("id") { type = NavType.StringType })
                ) { backStackEntry ->
                    val orderId = backStackEntry.arguments?.getString("id") ?: ""
                    SellerOrderDetailScreen(
                        orderId = orderId,
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToReport = { type, id ->
                            navController.navigate(Screen.ReportDispute.createRoute(type, id, "Đơn bán #$id"))
                        }
                    )
                }

                // --- DIRECT SWAP JOURNEY (F26 - F29) ---
                // F26: Swap Discovery
                composable(Screen.SwapDiscovery.route) {
                    SwapDiscoveryScreen(
                        onNavigateToProduct = { id ->
                            navController.navigate(Screen.ProductDetail.createRoute(id))
                        },
                        onNavigateToSwapRequests = {
                            navController.navigate(Screen.SwapRequests.route)
                        }
                    )
                }

                // F27: Create Swap Offer
                composable(
                    route = Screen.CreateSwapOffer.route,
                    arguments = listOf(navArgument("targetId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val targetId = backStackEntry.arguments?.getString("targetId") ?: ""
                    CreateSwapOfferScreen(
                        targetProductId = targetId,
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToCreateListing = {
                            navController.navigate(Screen.CreateEditListing.createRoute(null))
                        },
                        onOfferSent = {
                            navController.navigate(Screen.SwapRequests.createRoute(1)) {
                                popUpTo(Screen.SwapDiscovery.route)
                            }
                        }
                    )
                }

                // F28: My Swap Requests (Inbox / Outbox)
                composable(
                    route = Screen.SwapRequests.route,
                    arguments = listOf(navArgument("tab") {
                        type = NavType.IntType
                        defaultValue = 0
                    })
                ) { backStackEntry ->
                    val initialTab = backStackEntry.arguments?.getInt("tab") ?: 0
                    SwapRequestsScreen(
                        initialTab = initialTab,
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToSwapDetail = { id ->
                            navController.navigate(Screen.SwapDetail.createRoute(id))
                        }
                    )
                }

                // F29: Swap Detail & Two-Way Tracking
                composable(
                    route = Screen.SwapDetail.route,
                    arguments = listOf(navArgument("id") { type = NavType.StringType })
                ) { backStackEntry ->
                    val txId = backStackEntry.arguments?.getString("id") ?: ""
                    SwapDetailScreen(
                        transactionId = txId,
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToReport = { type, id ->
                            navController.navigate(Screen.ReportDispute.createRoute(type, id, "Giao dịch đổi #$id"))
                        }
                    )
                }

                // --- TRUST & SAFETY AND NOTIFICATIONS (F30 - F31) ---
                // F30: Report / Dispute
                composable(
                    route = Screen.ReportDispute.route,
                    arguments = listOf(
                        navArgument("targetType") { type = NavType.StringType; defaultValue = "LISTING" },
                        navArgument("targetId") { type = NavType.StringType; defaultValue = "" },
                        navArgument("targetTitle") { type = NavType.StringType; defaultValue = "" }
                    )
                ) { backStackEntry ->
                    val targetType = backStackEntry.arguments?.getString("targetType") ?: "LISTING"
                    val targetId = backStackEntry.arguments?.getString("targetId") ?: ""
                    val targetTitle = backStackEntry.arguments?.getString("targetTitle") ?: ""
                    ReportDisputeScreen(
                        targetTypeStr = targetType,
                        targetId = targetId,
                        targetTitle = targetTitle,
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToDisputeDetail = { dispId ->
                            navController.navigate(Screen.DisputeDetail.createRoute(dispId)) {
                                popUpTo(Screen.Home.route)
                            }
                        }
                    )
                }

                // F31: Notifications
                composable(Screen.Notifications.route) {
                    NotificationCenterScreen(
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToOrders = { navController.navigate(Screen.MyOrders.route) },
                        onNavigateToSwap = { navController.navigate(Screen.SwapRequests.route) },
                        onNavigateToProduct = { prodId ->
                            navController.navigate(Screen.ProductDetail.createRoute(prodId))
                        }
                    )
                }

                // --- FLOW G: ESCROW CASHFLOW & WALLET ---
                composable(Screen.Wallet.route) {
                    WalletScreen(
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToExplore = { navController.navigate(Screen.Explore.route) }
                    )
                }

                // --- FLOW H: eKYC & SHE-TRUST VERIFICATION ---
                composable(Screen.Ekyc.route) {
                    EkycScreen(
                        onNavigateBack = { navController.popBackStack() },
                        onSuccess = { navController.popBackStack() }
                    )
                }

                // --- FLOW D: EXTENDED DISPUTE NEGOTIATION & ARBITRATION ---
                composable(
                    route = Screen.DisputeDetail.route,
                    arguments = listOf(navArgument("id") { type = NavType.StringType })
                ) { backStackEntry ->
                    val disputeId = backStackEntry.arguments?.getString("id") ?: ""
                    DisputeDetailScreen(
                        disputeId = disputeId,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                // --- FLOW C & FLOW H: DIRECT CHAT & ANTI-SCAM ESCROW PROTECTION ---
                composable(
                    route = Screen.Chat.route,
                    arguments = listOf(
                        navArgument("targetName") { type = NavType.StringType; defaultValue = "Người bán" },
                        navArgument("productId") { type = NavType.StringType; nullable = true; defaultValue = null }
                    )
                ) { backStackEntry ->
                    val targetName = backStackEntry.arguments?.getString("targetName") ?: "Người bán"
                    val productId = backStackEntry.arguments?.getString("productId")
                    ChatScreen(
                        recipientName = targetName,
                        productId = productId,
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToSwapOffer = { prodId ->
                            navController.navigate(Screen.CreateSwapOffer.createRoute(prodId))
                        }
                    )
                }

                // Flow D & Admin Governance
                composable(Screen.AdminDashboard.route) {
                    AdminDashboardScreen(
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToDisputeDetail = { dispId ->
                            navController.navigate(Screen.DisputeDetail.createRoute(dispId))
                        }
                    )
                }
            }
        }
    }

        // Floating Dev Simulator & Role Switcher (Buyer <-> Seller <-> Admin)
        Box(
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            val demoBottomPadding = if (showNav && !isWideScreen) 76.dp else 16.dp
            FloatingDemoControlBar(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToSellerCloset = {
                    navController.navigate(Screen.MyCloset.route)
                },
                onNavigateToDisputeDetail = { dispId ->
                    navController.navigate(Screen.DisputeDetail.createRoute(dispId))
                },
                onNavigateToAdminDashboard = {
                    navController.navigate(Screen.AdminDashboard.route)
                },
                bottomPadding = demoBottomPadding
            )
        }

        // Floating Bottom Navigation Bar (Centered Island Pill Dock matching design screenshot)
        if (showNav && !isWideScreen) {
            BottomBar(
                currentRoute = currentRoute,
                onNavigate = onNavigateTab,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        // Global Animated Toast Notification Banner
        AnimatedVisibility(
            visible = toastMessage != null,
            enter = fadeIn() + slideInVertically { -it },
            exit = fadeOut() + slideOutVertically { -it },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Primary,
                shadowElevation = 8.dp
            ) {
                Text(
                    text = toastMessage ?: "",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }
    }
}
