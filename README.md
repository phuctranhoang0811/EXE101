# SWAPSHE — Mobile Application (Kotlin & Jetpack Compose)

> **Brand:** SWAPSHE — *Loved Before, Styled Again.*  
> **Tech Stack:** Kotlin 2.0.21, Jetpack Compose (Material 3), Navigation Compose, Coil Compose, StateFlow Architecture.  
> **Style:** Soft Vintage Chic (Warm Cream `#FFF8F2`, Vintage Plum `#754B61`, Dusty Rose `#D9A8B8`, Deep Cocoa `#44343C`, Sage Green `#799078`).  
> **Reference Viewport:** 390 × 844 px (iPhone reference & safe area insets).

---

## 📱 Cấu trúc Screen Inventory (F01 – F21)

| Screen ID | Tên màn hình | File Compose | Route |
|---|---|---|---|
| **F01** | Splash Screen | [`SplashScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/SplashScreen.kt) | `splash` |
| **F02** | Welcome Screen | [`WelcomeScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/WelcomeScreen.kt) | `welcome` |
| **F03** | Login / Register / Forgot Password | [`AuthScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/AuthScreen.kt) | `auth` |
| **F04** | Style Preference (Step 1/3) | [`OnboardingScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/OnboardingScreens.kt) | `onboarding/style` |
| **F05** | Size Profile (Step 2/3) | [`OnboardingScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/OnboardingScreens.kt) | `onboarding/size` |
| **F06** | Color Preference (Step 3/3) | [`OnboardingScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/OnboardingScreens.kt) | `onboarding/color` |
| **F07** | Setup Completed | [`OnboardingScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/OnboardingScreens.kt) | `onboarding/complete` |
| **F08** | Home Marketplace | [`HomeScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/HomeScreen.kt) | `home` |
| **F09** | Explore & Live Search | [`ExploreScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/ExploreScreen.kt) | `explore` |
| **F10** | Filter & Sort (Modal Bottom Sheet) | [`ExploreScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/ExploreScreen.kt) | `explore` + sheet |
| **F11** | Product Detail & 3:4 Carousel | [`ProductDetailScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/ProductDetailScreen.kt) | `product/{id}` |
| **F12** | Seller Profile | [`SellerProfileScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/SellerProfileScreen.kt) | `seller/{id}` |
| **F13** | Wishlist | [`WishlistScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/WishlistScreen.kt) | `wishlist` |
| **F14** | Shipping Address | [`CheckoutScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/CheckoutScreens.kt) | `checkout/address` |
| **F15** | Review & Payment | [`CheckoutScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/CheckoutScreens.kt) | `checkout/review` |
| **F16** | Mock Payment Simulation | [`CheckoutScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/CheckoutScreens.kt) | `checkout/mock-payment` |
| **F17** | Order Confirmation | [`CheckoutScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/CheckoutScreens.kt) | `checkout/success/{orderId}` |
| **F18** | My Orders (Tabs: All/Active/Completed) | [`OrderScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/OrderScreens.kt) | `orders` |
| **F19** | Order Detail & Tracking Timeline | [`OrderScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/OrderScreens.kt) | `orders/{id}` |
| **F20** | Review Order Dialog (1–5 Stars) | [`OrderScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/OrderScreens.kt) | `orders/{id}` + dialog |
| **F21** | My Profile & Demo Switcher | [`ProfileScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/ProfileScreen.kt) | `profile` |
| **F22** | My Closet (Seller Dashboard & Tabs) | [`MyClosetScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/MyClosetScreen.kt) | `sell/closet` |
| **F23** | Create / Edit Listing Wizard (4 Steps) | [`CreateEditListingScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/CreateEditListingScreen.kt) | `sell/listing/editor` |
| **F24** | Seller Orders (Quản lý đơn bán) | [`SellerOrdersScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/SellerOrdersScreen.kt) | `sell/orders` |
| **F25** | Seller Order Detail & Fulfillment | [`SellerOrderDetailScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/SellerOrderDetailScreen.kt) | `sell/orders/{id}` |
| **F26** | Swap Discovery (Catalog đồ đổi 1-1) | [`SwapDiscoveryScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/SwapDiscoveryScreen.kt) | `swap` |
| **F27** | Create Swap Offer (Ghép cặp trao đổi) | [`CreateSwapOfferScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/CreateSwapOfferScreen.kt) | `swap/offer/new` |
| **F28** | My Swap Requests (Hộp thư nhận & gửi) | [`SwapRequestsScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/SwapRequestsScreen.kt) | `swap/requests` |
| **F29** | Swap Detail & Two-Way Delivery Tracking | [`SwapDetailScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/SwapDetailScreen.kt) | `swap/{id}` |
| **F30** | Report / Dispute Form (Trust & Safety) | [`ReportDisputeScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/ReportDisputeScreen.kt) | `report` |
| **F31** | Notification Center | [`NotificationCenterScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/NotificationCenterScreen.kt) | `notifications` |

---

## 🛠️ Hướng dẫn Chạy & Biên dịch

### 1. Mở trong Android Studio
- Khởi động **Android Studio** và chọn **Open...**
- Trỏ tới thư mục `c:\Users\phuct\Desktop\SWAPSHE`
- Android Studio sẽ tự động đồng bộ Gradle và sẵn sàng chạy trên Emulator hoặc thiết bị thật.

### 2. Biên dịch qua dòng lệnh (CLI)
```bash
# Biên dịch mã nguồn Kotlin & Jetpack Compose
.\gradlew.bat compileDebugKotlin

# Đóng gói file APK hoàn chỉnh
.\gradlew.bat assembleDebug
```

File APK đầu ra nằm tại:
`app/build/outputs/apk/debug/app-debug.apk`

---

## 🎯 3 Luồng Trải nghiệm Cốt lõi (BUY — SELL — SWAP)

### 🛍️ Luồng 1: Buyer Journey (F01 – F21)
1. Khám phá kho đồ secondhand qua [`HomeScreen`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/HomeScreen.kt) và [`ExploreScreen`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/ExploreScreen.kt) (Live Search & Filter Sheet).
2. Xem chi tiết [`ProductDetailScreen`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/ProductDetailScreen.kt): ảnh 3:4 chân thực, số đo đo tay đối chiếu vóc dáng.
3. Thanh toán giả lập qua [`MockPaymentScreen`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/CheckoutScreens.kt) với cả 2 nhánh Success / Failure (Retry).
4. Theo dõi vận đơn, bấm xác nhận nhận hàng và đánh giá 5 sao tại [`OrderDetailScreen`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/OrderScreens.kt).

### 🏷️ Luồng 2: Seller Journey (F22 – F25)
1. **My Closet ([`MyClosetScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/MyClosetScreen.kt))**: Quản lý tủ đồ theo các tab *Tất cả / Đang bán / Đang giữ chỗ / Đã bán / Bản nháp*.
2. **Đăng món Wizard ([`CreateEditListingScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/CreateEditListingScreen.kt))**: 4 bước chuẩn chỉ (Tải ảnh $\rightarrow$ Chi tiết $\rightarrow$ Tình trạng & Số đo $\rightarrow$ Giá & Bật chế độ Swap), hỗ trợ lưu nháp.
3. **Quản lý đơn bán ([`SellerOrdersScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/SellerOrdersScreen.kt))**: Theo dõi đơn từ người mua, nhận danh sách đóng gói checklist tại [`SellerOrderDetailScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/SellerOrderDetailScreen.kt), xác nhận chuẩn bị và mô phỏng giao cho shipper.

### 🔄 Luồng 3: Direct Swap 1-1 (F26 – F29)
1. **Swap Discovery ([`SwapDiscoveryScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/SwapDiscoveryScreen.kt))**: Khám phá danh mục các món đồ sẵn sàng trao đổi ngang giá trị.
2. **Tạo đề nghị đổi ([`CreateSwapOfferScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/CreateSwapOfferScreen.kt))**: Chọn 1 món từ Tủ đồ cá nhân ghép đôi với món của người bán, gửi kèm lời nhắn.
3. **Hộp thư Swap ([`SwapRequestsScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/SwapRequestsScreen.kt))**: Người nhận duyệt Accept hoặc Reject. Khi Accept, hệ thống tự động khóa cả 2 món đồ (Atomic Lock).
4. **Theo dõi 2 chiều ([`SwapDetailScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/SwapDetailScreen.kt))**: Theo dõi 2 mã vận đơn riêng biệt (Bạn gửi $\leftrightarrow$ Đối tác gửi). Giao dịch chỉ hoàn tất khi cả 2 bên cùng xác nhận đã nhận đồ.

### 🛡️ Luồng 4: Trust, Safety & Notifications (F30 – F31)
1. **Khiếu nại & Báo cáo ([`ReportDisputeScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/ReportDisputeScreen.kt))**: Gửi báo cáo món đồ, đơn mua hoặc giao dịch swap gặp vấn đề để CSKH xử lý và tạm dừng giải ngân.
2. **Trung tâm thông báo ([`NotificationCenterScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/NotificationCenterScreen.kt))**: Nhận thông báo thời gian thực về tiến độ đơn hàng, lời mời đổi đồ mới và biến động giá.

