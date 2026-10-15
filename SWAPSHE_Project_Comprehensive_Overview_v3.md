# TỔNG HỢP TOÀN BỘ DỰ ÁN SWAPSHE — KIẾN TRÚC, TÍNH NĂNG & REVIEW BỔ SUNG (v3.0)

> **Tên thương hiệu:** SWAPSHE — *Loved Before, Styled Again.*  
> **Nền tảng:** Android Native (Kotlin 2.0.21, Jetpack Compose Material 3, Navigation Compose, Reactive StateFlow Architecture).  
> **Định vị & Mô hình:** C2C Secondhand Fashion Marketplace dành cho nữ giới — Tích hợp trọn vẹn 3 trụ cột **BUY — SELL — DIRECT SWAP 1:1** trên cùng 1 tài khoản duy nhất.  
> **Phong cách nhận diện:** Soft Vintage Chic (Warm Cream `#FFF8F2`, Vintage Plum `#754B61`, Dusty Rose `#D9A8B8`, Deep Cocoa `#44343C`, Sage Green `#799078`).  
> **Trạng thái kiểm thử:** Biên dịch thành công 100% (`BUILD SUCCESSFUL in 17s`), không lỗi cú pháp hay thiếu import.

---

## 1. Bảng Tổng Hợp 31 Màn Hình & Thành Phần Nghiệp Vụ (F01 – F31)

Toàn bộ 31 màn hình đã được hiện thực hóa bằng Jetpack Compose, tổ chức module hóa và liên kết qua `NavHost` tại [`MainApp.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/MainApp.kt):

| Nhóm | ID | Tên màn hình / Chức năng | Tệp Compose | Route điều hướng |
|---|---|---|---|---|
| **Entry & Onboarding** | **F01** | Splash Screen | [`SplashScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/SplashScreen.kt) | `splash` |
| | **F02** | Welcome Screen (Guest / Auth CTA) | [`WelcomeScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/WelcomeScreen.kt) | `welcome` |
| | **F03** | Auth Gate (Login / Register / Forgot) | [`AuthScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/AuthScreen.kt) | `auth` |
| | **F04** | Style Preference (Onboarding Bước 1/3) | [`OnboardingScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/OnboardingScreens.kt) | `onboarding/style` |
| | **F05** | Size Profile & Số đo (Onboarding Bước 2/3) | [`OnboardingScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/OnboardingScreens.kt) | `onboarding/size` |
| | **F06** | Color Palette (Onboarding Bước 3/3) | [`OnboardingScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/OnboardingScreens.kt) | `onboarding/color` |
| | **F07** | Setup Completed | [`OnboardingScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/OnboardingScreens.kt) | `onboarding/complete` |
| **Buyer Journey** | **F08** | Home Marketplace & Personal Matches | [`HomeScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/HomeScreen.kt) | `home` *(Tab 1)* |
| | **F09** | Explore & Live Search | [`ExploreScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/ExploreScreen.kt) | `explore` *(Tab 2)* |
| | **F10** | Filter & Sort (Modal Bottom Sheet) | [`ExploreScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/ExploreScreen.kt) | Sheet trong Explore |
| | **F11** | Product Detail & Ảnh 3:4 & Đối chiếu Size | [`ProductDetailScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/ProductDetailScreen.kt) | `product/{id}` |
| | **F12** | Seller Profile & Closet Tủ đồ shop | [`SellerProfileScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/SellerProfileScreen.kt) | `seller/{id}` |
| | **F13** | Smart Wishlist & Saved Search (Flow E) | [`WishlistScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/WishlistScreen.kt) | `wishlist` |
| | **F14** | Shipping Address Selection | [`CheckoutScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/CheckoutScreens.kt) | `checkout/address` |
| | **F15** | Review Order & Pricing Breakdown | [`CheckoutScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/CheckoutScreens.kt) | `checkout/review` |
| | **F16** | Mock Payment Gateway (Success/Failure/Retry) | [`CheckoutScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/CheckoutScreens.kt) | `checkout/mock-payment` |
| | **F17** | Order Confirmation `#SW-YYYYMMDD-XXX` | [`CheckoutScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/CheckoutScreens.kt) | `checkout/success/{id}` |
| | **F18** | My Orders (Đơn mua theo các tab) | [`OrderScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/OrderScreens.kt) | `orders` |
| | **F19** | Order Detail & Vận đơn Timeline | [`OrderScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/OrderScreens.kt) | `orders/{id}` |
| | **F20** | Review Order Dialog (Đánh giá 1–5 sao) | [`OrderScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/OrderScreens.kt) | Dialog trong OrderDetail |
| | **F21** | My Profile & Demo Role Switcher | [`ProfileScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/ProfileScreen.kt) | `profile` *(Tab 5)* |
| **Seller Journey** | **F22** | My Closet (Quản lý tủ đồ cá nhân) | [`MyClosetScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/MyClosetScreen.kt) | `sell/closet` *(Tab 3 - Hero)* |
| | **F23** | Create/Edit Listing Wizard 4 Bước (Lưu nháp) | [`CreateEditListingScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/CreateEditListingScreen.kt) | `sell/listing/editor` |
| | **F24** | Seller Orders (Danh sách đơn bán từ khách) | [`SellerOrdersScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/SellerOrdersScreen.kt) | `sell/orders` |
| | **F25** | Seller Order Fulfillment & Packing Checklist | [`SellerOrderDetailScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/SellerOrderDetailScreen.kt) | `sell/orders/{id}` |
| **Direct Swap 1-1** | **F26** | Swap Discovery (Sàn đồ mở trao đổi 1-1) | [`SwapDiscoveryScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/SwapDiscoveryScreen.kt) | `swap` *(Tab 4)* |
| | **F27** | Create Swap Offer (Ghép đôi 2 món đồ) | [`CreateSwapOfferScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/CreateSwapOfferScreen.kt) | `swap/offer/new` |
| | **F28** | My Swap Requests (Hộp thư nhận & gửi) | [`SwapRequestsScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/SwapRequestsScreen.kt) | `swap/requests` |
| | **F29** | Swap Detail & Two-Way Tracking độc lập | [`SwapDetailScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/SwapDetailScreen.kt) | `swap/{id}` |
| **Trust & Engagement** | **F30** | Report / Dispute Form (Khiếu nại bảo vệ) | [`ReportDisputeScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/ReportDisputeScreen.kt) | `report` |
| | **F31** | Notification Center (Trung tâm thông báo) | [`NotificationCenterScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/NotificationCenterScreen.kt) | `notifications` |

---

## 2. Review Toàn Diện & Điểm Bổ Sung Mới Nhất

### 🌟 2.1. Bổ sung Flow E: Smart Wishlist & Saved Search Notification
- **Tại F13 ([`WishlistScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/WishlistScreen.kt)):**
  - Mở rộng thêm tab **"Bộ lọc đã lưu (Saved Searches)"** bên cạnh tab món đồ đã thả tim.
  - Người dùng có thể lưu các tiêu chí săn đồ: Gu thời trang (Vintage, Minimal...), Size số đo (XS, S, M, L), Mức giá trần.
  - Mỗi bộ lọc có công tắc bật/tắt chuông thông báo thông minh (*Notification Active Toggle*).
- **Cơ chế Matching tự động:** Khi bất kỳ người bán nào hoàn tất đăng đồ (Publish) ở F23, hệ thống trong [`SwapSheRepository.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/data/SwapSheRepository.kt) sẽ tự động rà soát các `savedSearches` đang mở chuông và bắn thông báo In-App:
  > *"✨ Đồ mới hợp gu bạn vừa lên kệ: Món '...' (Size S) vừa khớp với bộ lọc bạn đã lưu!"*

### 🛍️ 2.2. Hoàn thiện Buyer Flow & Auth Gate
- Cho phép khách **Guest** khám phá sản phẩm không rào cản.
- Khi Guest bấm **Buy Now**, `AuthGateModal` lưu tạm `productId`. Sau khi đăng nhập thành công, app đưa thẳng vào luồng thanh toán mà không làm mất món đồ.
- Cổng thanh toán mô phỏng (F16) có đầy đủ cả nhánh thử nghiệm thất bại (để test nút Retry) và nhánh thành công.

### 🏷️ 2.3. Hoàn thiện Seller Flow & Fulfillment Checklist
- **My Closet (F22):** Quản lý trạng thái vòng đời sản phẩm: `DRAFT`, `AVAILABLE`, `RESERVED`, `SOLD`, `HIDDEN`.
- **Wizard đăng bán (F23):** 4 bước minh bạch:
  1. *Ảnh chụp:* Hướng dẫn chụp mặt trước, mặt sau và nhãn mác.
  2. *Thông tin:* Tên, thương hiệu, phong cách, câu chuyện món đồ.
  3. *Số đo đo tay & Tình trạng:* Kiểm tra các lỗi (sờn, xù lông, đứt chỉ) và chọn độ mới (*Like New, Excellent, Good, Fair*).
  4. *Giá & Chế độ Swap:* Bật/tắt bán thẳng hoặc trao đổi ngang. Hỗ trợ nút **Lưu nháp** bất kỳ lúc nào.
- **Seller Order Detail (F25):** Checklist đóng gói chuẩn và nút cập nhật vận đơn (*Xác nhận chuẩn bị $\rightarrow$ Giao bưu tá GHN*).

### 🔄 2.4. Hoàn thiện Direct Swap 1-1 & Chống Xung Đột (Rule 409)
- **Swap Discovery (F26):** Lọc danh mục đồ mở Swap, loại trừ đồ của chính người dùng.
- **Ghép cặp (F27):** Chọn 1 món của đối tác $\leftrightarrow$ Chọn 1 món từ tủ đồ cá nhân.
- **Cơ chế Atomic Lock (F28):** Khi bấm Chấp thuận (Accept), repository kiểm tra tức thì xem cả 2 món có còn `AVAILABLE` không. Nếu một món vừa bị người khác mua, hệ thống trả về thông báo xung đột (`CONFLICT`), không để xảy ra tình trạng mua và đổi đè lên nhau.
- **Tracking 2 chiều độc lập (F29):** Tách bạch 2 mã vận đơn: Hướng 1 (Bạn gửi cho đối tác) và Hướng 2 (Đối tác gửi cho bạn). Cả 2 bên đều phải bấm xác nhận nhận đồ thì giao dịch mới chuyển sang `COMPLETED` và 2 món chuyển thành `SOLD/SWAPPED`.

### 🛡️ 2.5. Hoàn thiện Trust, Safety & Báo Cáo Sự Cố (F30)
- Nút báo cáo (Flag icon) xuất hiện ở Chi tiết sản phẩm, Hồ sơ người bán, Đơn mua và Giao dịch Swap.
- Sau khi gửi khiếu nại, hệ thống chuyển giao dịch sang `DISPUTED`, **tự động đóng băng quy trình giải ngân/hoàn tất** để bộ phận CSKH xử lý.

---

## 3. Cấu Trúc Bottom Navigation (Sitemap v2)

Thanh BottomBar hiển thị ở 5 tab cấp cao và tự động ẩn khi vào màn hình chi tiết, wizard hoặc thanh toán:

$$\boxed{\text{Home}} \quad \boxed{\text{Explore}} \quad \boxed{\begin{matrix} \mathbf{+ \text{ Sell}} \\ \text{(Hero Button)} \end{matrix}} \quad \boxed{\text{Swap}} \quad \boxed{\text{Profile}}$$

* **Home (F08):** Bảng tin cá nhân hóa theo phong cách & size.
* **Explore (F09):** Tìm kiếm và bộ lọc đa chiều.
* **Sell (F22):** Nút Hero trung tâm, dẫn thẳng vào Tủ đồ cá nhân `My Closet` và Wizard đăng đồ `F23`.
* **Swap (F26):** Sàn đồ trao đổi 1-1 `Swap Discovery`.
* **Profile (F21):** Hồ sơ cá nhân, phím tắt vào Đơn mua, Đơn bán, Yêu cầu đổi đồ, Wishlist, Thông báo và bộ chuyển đổi tài khoản demo (*Demo Switcher: Guest $\leftrightarrow$ User*).

---

## 4. Cây Thư Mục Mã Nguồn Dự Án

```text
app/src/main/java/com/swapshe/app/
├── MainActivity.kt                      # Điểm khởi động Compose
├── data/
│   ├── MockData.kt                      # Dữ liệu hạt giống (users, products, orders, swaps, notifs)
│   └── SwapSheRepository.kt             # Singleton StateFlow Repository quản lý toàn bộ luồng nghiệp vụ
├── model/
│   ├── Product.kt                       # Product, ConditionType, ProductStatus, ProductMeasurements
│   ├── Order.kt                         # Order, OrderStatus, PaymentStatus, TimelineEvent, ShippingAddress
│   ├── Seller.kt                        # Seller, SellerReview
│   ├── User.kt                          # User, BodyMeasurements, AppNotification
│   └── SwapAndDispute.kt                # SwapOffer, SwapTransaction, TwoWayShipment, DisputeReport, SavedSearch
├── theme/
│   ├── Color.kt                         # Hệ màu Soft Vintage Chic
│   ├── Theme.kt                         # Cấu hình Material3 Theme
│   └── Type.kt
└── ui/
    ├── MainApp.kt                       # NavHost 31 màn hình + Animated Toast toàn cục
    ├── components/                      # Các thành phần giao diện tái sử dụng
    │   ├── AnimatedLikeButton.kt        # Nút like nảy trái tim
    │   ├── AuthGateModal.kt             # Modal chặn Guest chuyển hướng login
    │   ├── CommonButtons.kt             # PrimaryButton, SecondaryButton
    │   ├── CommonInputs.kt              # Ô nhập liệu chuẩn phong cách vintage
    │   ├── ProductCard.kt               # Thẻ sản phẩm chuẩn 3:4
    │   ├── TimelineView.kt              # OrderTimelineView hiển thị hành trình vận đơn
    │   └── ShimmerEffect.kt             # Hiệu ứng tải trang shimmer
    ├── navigation/
    │   ├── BottomBar.kt                 # Thanh điều hướng 5 tab chuẩn Sitemap v2
    │   └── Screen.kt                    # Định nghĩa Sealed class routes
    └── screens/                         # 31 màn hình giao diện F01 đến F31
        ├── SplashScreen.kt (F01)
        ├── WelcomeScreen.kt (F02)
        ├── AuthScreen.kt (F03)
        ├── OnboardingScreens.kt (F04-F07)
        ├── HomeScreen.kt (F08)
        ├── ExploreScreen.kt (F09, F10)
        ├── ProductDetailScreen.kt (F11)
        ├── SellerProfileScreen.kt (F12)
        ├── WishlistScreen.kt (F13 - Smart Wishlist & Saved Search)
        ├── CheckoutScreens.kt (F14-F17)
        ├── OrderScreens.kt (F18-F20)
        ├── ProfileScreen.kt (F21)
        ├── MyClosetScreen.kt (F22)
        ├── CreateEditListingScreen.kt (F23)
        ├── SellerOrdersScreen.kt (F24)
        ├── SellerOrderDetailScreen.kt (F25)
        ├── SwapDiscoveryScreen.kt (F26)
        ├── CreateSwapOfferScreen.kt (F27)
        ├── SwapRequestsScreen.kt (F28)
        ├── SwapDetailScreen.kt (F29)
        ├── ReportDisputeScreen.kt (F30)
        └── NotificationCenterScreen.kt (F31)
```

---

## 5. Hướng Dẫn Vận Hành & Khởi Chạy

### 5.1. Khởi động ứng dụng trên Android Studio hoặc CLI
```cmd
# Khởi động nhanh Android Emulator:
.\start_emulator.bat

# Biên dịch & chạy cài đặt ứng dụng:
.\Run_SWAPSHE.bat
```

### 5.2. Lệnh kiểm tra biên dịch độc lập
```powershell
.\gradlew.bat compileDebugKotlin
# BUILD SUCCESSFUL
```
File APK debug sau khi build nằm tại:  
`app/build/outputs/apk/debug/app-debug.apk`
