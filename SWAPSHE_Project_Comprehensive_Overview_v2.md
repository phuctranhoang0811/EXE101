# TỔNG HỢP KIẾN TRÚC & TÍNH NĂNG DỰ ÁN SWAPSHE (v2.0)

> **Tên thương hiệu:** SWAPSHE — *Loved Before, Styled Again.*  
> **Nền tảng:** Android (Kotlin 2.0.21, Jetpack Compose Material 3, Navigation Compose, StateFlow Architecture).  
> **Định vị:** Nền tảng thời trang C2C dành cho nữ — tích hợp trọn vẹn 3 trụ cột **BUY — SELL — SWAP** trên cùng một tài khoản.  
> **Phong cách thiết kế:** Soft Vintage Chic (Warm Cream `#FFF8F2`, Vintage Plum `#754B61`, Dusty Rose `#D9A8B8`, Deep Cocoa `#44343C`, Sage Green `#799078`).  
> **Trạng thái biên dịch:** `BUILD SUCCESSFUL` (100% Kotlin Compose, không lỗi biên dịch).

---

## 1. Bản Đồ Danh Mục Màn Hình Đã Hoàn Thiện (F01 – F31)

Dự án hiện có tổng cộng **31 màn hình & component nghiệp vụ hoàn chỉnh**, liên kết đồng bộ qua `NavHost` tại [`MainApp.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/MainApp.kt):

| Nhóm | ID | Tên màn hình | File Compose | Route điều hướng |
|---|---|---|---|---|
| **Entry & Onboarding** | **F01** | Splash Screen | [`SplashScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/SplashScreen.kt) | `splash` |
| | **F02** | Welcome Screen | [`WelcomeScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/WelcomeScreen.kt) | `welcome` |
| | **F03** | Auth (Login / Register / Forgot) | [`AuthScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/AuthScreen.kt) | `auth` |
| | **F04** | Style Preference (Bước 1/3) | [`OnboardingScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/OnboardingScreens.kt) | `onboarding/style` |
| | **F05** | Size Profile (Bước 2/3) | [`OnboardingScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/OnboardingScreens.kt) | `onboarding/size` |
| | **F06** | Color Preference (Bước 3/3) | [`OnboardingScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/OnboardingScreens.kt) | `onboarding/color` |
| | **F07** | Setup Completed | [`OnboardingScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/OnboardingScreens.kt) | `onboarding/complete` |
| **Buyer Journey** | **F08** | Home Marketplace | [`HomeScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/HomeScreen.kt) | `home` *(Tab 1)* |
| | **F09** | Explore & Live Search | [`ExploreScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/ExploreScreen.kt) | `explore` *(Tab 2)* |
| | **F10** | Filter & Sort (Modal Bottom Sheet) | [`ExploreScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/ExploreScreen.kt) | Sheet trong Explore |
| | **F11** | Product Detail & Ảnh 3:4 | [`ProductDetailScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/ProductDetailScreen.kt) | `product/{id}` |
| | **F12** | Seller Profile & Closet | [`SellerProfileScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/SellerProfileScreen.kt) | `seller/{id}` |
| | **F13** | Wishlist (Đồ yêu thích) | [`WishlistScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/WishlistScreen.kt) | `wishlist` |
| | **F14** | Shipping Address | [`CheckoutScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/CheckoutScreens.kt) | `checkout/address` |
| | **F15** | Review & Payment | [`CheckoutScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/CheckoutScreens.kt) | `checkout/review` |
| | **F16** | Mock Payment Gateway Simulation | [`CheckoutScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/CheckoutScreens.kt) | `checkout/mock-payment` |
| | **F17** | Order Confirmation | [`CheckoutScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/CheckoutScreens.kt) | `checkout/success/{id}` |
| | **F18** | My Orders (Đơn mua của tôi) | [`OrderScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/OrderScreens.kt) | `orders` |
| | **F19** | Order Detail & Tracking Timeline | [`OrderScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/OrderScreens.kt) | `orders/{id}` |
| | **F20** | Review Order Dialog (1–5 Sao) | [`OrderScreens.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/OrderScreens.kt) | Dialog trong OrderDetail |
| | **F21** | My Profile & Demo Switcher | [`ProfileScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/ProfileScreen.kt) | `profile` *(Tab 5)* |
| **Seller Journey** | **F22** | My Closet (Quản lý tủ đồ cá nhân) | [`MyClosetScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/MyClosetScreen.kt) | `sell/closet` *(Tab 3 - Hero)* |
| | **F23** | Create/Edit Listing Wizard (4 Bước) | [`CreateEditListingScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/CreateEditListingScreen.kt) | `sell/listing/editor` |
| | **F24** | Seller Orders (Quản lý đơn bán) | [`SellerOrdersScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/SellerOrdersScreen.kt) | `sell/orders` |
| | **F25** | Seller Order Detail & Fulfillment | [`SellerOrderDetailScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/SellerOrderDetailScreen.kt) | `sell/orders/{id}` |
| **Direct Swap 1-1** | **F26** | Swap Discovery (Catalog đồ đổi) | [`SwapDiscoveryScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/SwapDiscoveryScreen.kt) | `swap` *(Tab 4)* |
| | **F27** | Create Swap Offer (Ghép đôi đổi đồ) | [`CreateSwapOfferScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/CreateSwapOfferScreen.kt) | `swap/offer/new` |
| | **F28** | My Swap Requests (Hộp thư Inbox/Outbox) | [`SwapRequestsScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/SwapRequestsScreen.kt) | `swap/requests` |
| | **F29** | Swap Detail & Two-Way Tracking | [`SwapDetailScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/SwapDetailScreen.kt) | `swap/{id}` |
| **Trust & Engagement** | **F30** | Report / Dispute Form (Khiếu nại) | [`ReportDisputeScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/ReportDisputeScreen.kt) | `report` |
| | **F31** | Notification Center (Thông báo) | [`NotificationCenterScreen.kt`](file:///c:/Users/phuct/Desktop/SWAPSHE/app/src/main/java/com/swapshe/app/ui/screens/NotificationCenterScreen.kt) | `notifications` |

---

## 2. Chi Tiết 4 Luồng Người Dùng Chính (Core User Journeys)

### 🛍️ 2.1. Buyer Journey (Mua hàng & Nhận hàng)
1. **Khám phá:** Người dùng (hoặc khách Guest) lướt xem đồ secondhand tại `Home` hoặc tìm kiếm lọc theo mức giá, size, độ mới (*Like New, Excellent, Good, Fair*) tại `Explore`.
2. **Chi tiết & Vừa vặn:** Màn hình `ProductDetail` hiển thị carousel ảnh tỉ lệ chuẩn **3:4**, bảng số đo tay thực tế (ngực, eo, dài áo) đối chiếu trực tiếp với hồ sơ số đo của người dùng.
3. **Auth Gate thông minh:** Khách Guest nhấn **Buy Now** $\rightarrow$ Hệ thống lưu tạm ý định mua $\rightarrow$ Hiện modal đăng nhập $\rightarrow$ Đăng nhập xong tự động đưa thẳng vào thanh toán món đó mà không bắt chọn lại.
4. **Thanh toán mô phỏng (Mock Payment):** Hỗ trợ đầy đủ 2 nhánh kịch bản:
   - **Simulate Failure:** Giả lập lỗi ngân hàng/hết hạn $\rightarrow$ Cung cấp nút Retry thử lại.
   - **Simulate Success:** Chốt đơn hợp lệ $\rightarrow$ Tạo mã đơn hàng dạng `#SW-YYYYMMDD-XXX`.
5. **Tracking & Đánh giá:** Màn hình `OrderDetail` hiển thị timeline vận đơn giao nhận, nút xác nhận *"Đã nhận được hàng"* và mở dialog chấm điểm 1–5 sao kèm nhận xét gửi đến người bán.

---

### 🏷️ 2.2. Seller Journey (Đăng bán & Xử lý đơn)
1. **Quản lý Tủ đồ (`My Closet`):**
   - Phân loại rõ các tab: *Tất cả, Đang bán, Đang giữ chỗ (Reserved), Đã bán, Bản nháp*.
   - Cho phép tạm ẩn món đồ, chỉnh sửa hoặc xóa vĩnh viễn các bản nháp.
2. **Quy trình Đăng món Wizard 4 bước (`Create/Edit Listing`):**
   - *Bước 1 (Photos):* Tải ảnh thật (tối thiểu 2 ảnh, kiểm tra rõ khuyết điểm/nhãn mác).
   - *Bước 2 (Details):* Nhập tên, danh mục, phong cách, mô tả nguồn gốc.
   - *Bước 3 (Measurements):* Chọn size, tình trạng, nhập số đo đo tay thực tế.
   - *Bước 4 (Pricing & Options):* Đặt giá bán VND, công tắc bật/tắt quyền mua và quyền **Đổi đồ 1-1 (Direct Swap)**.
   - Cung cấp nút **Lưu nháp** bất kỳ lúc nào để tránh mất dữ liệu khi đang nhập dở.
3. **Fulfillment Đơn bán (`Seller Orders` & `Detail`):**
   - Xem danh sách đơn do người khác đặt mua từ tủ của mình.
   - Checklist đóng gói chuẩn: Giặt ủi sạch sẽ, bọc chống nước, ghi rõ mã đơn lên bao bì.
   - Nút hành động: *Xác nhận đã chuẩn bị xong hàng* $\rightarrow$ *Mô phỏng bàn giao bưu tá GHN* $\rightarrow$ *Mô phỏng giao thành công*.

---

### 🔄 2.3. Direct Swap Journey (Trao đổi trực tiếp 1 món ↔ 1 món)
1. **Danh mục Swap (`Swap Discovery`):** Lọc hiển thị toàn bộ những món đồ công khai có bật `canSwap = true` (không hiển thị đồ của chính mình).
2. **Ghép đôi trao đổi (`Create Swap Offer`):**
   - Chọn món mong muốn từ đối tác $\leftrightarrow$ Chọn 1 món khả dụng từ Tủ đồ cá nhân của mình.
   - Nếu tủ đồ đang trống $\rightarrow$ Có CTA gợi ý đăng món mới ngay.
   - Ghi lời nhắn gửi đến đối tác. Nguyên tắc MVP: Đổi ngang giá trị, không bù tiền mặt.
3. **Hộp thư Swap (`Swap Requests`):**
   - *Tab Lời mời nhận được:* Người nhận có thể bấm **Đồng ý đổi** hoặc **Từ chối**.
   - *Cơ chế Atomic Lock chống xung đột (Rule 409):* Khi bấm Chấp thuận, hệ thống tự động kiểm tra xem 2 món còn `AVAILABLE` hay không và đồng thời khóa cả 2 món chuyển sang `RESERVED`, ngăn chặn việc bị người khác mua mất trong lúc thỏa thuận.
4. **Theo dõi vận chuyển 2 chiều (`Swap Detail`):**
   - Quản lý 2 mã vận đơn riêng biệt: Hướng 1 (Bạn gửi cho đối tác) và Hướng 2 (Đối tác gửi cho bạn).
   - Mỗi bên độc lập xác nhận đã gửi kiện hàng của mình và độc lập bấm *"Tôi đã nhận được đồ & kiểm tra ưng ý"*.
   - Chỉ khi cả 2 bên cùng xác nhận nhận hàng, giao dịch Swap mới chuyển sang `COMPLETED` và 2 món đồ được đánh dấu là `SOLD`.

---

### 🛡️ 2.4. Trust, Safety & Notifications (An toàn & Thông báo)
1. **Báo cáo & Khiếu nại (`Report Dispute`):**
   - Có thể báo cáo từ Trang chi tiết sản phẩm, Hồ sơ người bán, Đơn mua hoặc Giao dịch Swap.
   - Lý do chuẩn: *Không đúng mô tả, rách/dơ không báo trước, người bán không gửi hàng, nghi vấn gian lận...*
   - Sau khi gửi khiếu nại, hệ thống **tạm ngừng tự động hoàn tất/giải ngân** của giao dịch liên quan để đảm bảo quyền lợi đôi bên.
2. **Trung tâm thông báo (`Notification Center`):**
   - Chia nhóm: Đơn hàng, Đổi đồ, Cảnh báo giảm giá.
   - Hỗ trợ đánh dấu đọc từng tin hoặc đọc tất cả, click vào thông báo tự động chuyển hướng đến màn hình chi tiết tương ứng.

---

## 3. Cấu Trúc Bottom Navigation (Sitemap v2)

Thanh BottomBar hiển thị ở 5 tab cấp cao và tự động ẩn khi người dùng đi vào các màn hình chi tiết, wizard hoặc thanh toán:

$$\boxed{\text{Home}} \quad \boxed{\text{Explore}} \quad \boxed{\begin{matrix} \mathbf{+ \text{ Sell}} \\ \text{(Hero Button)} \end{matrix}} \quad \boxed{\text{Swap}} \quad \boxed{\text{Profile}}$$

* **Sell:** Nút nổi Hero chính giữa, dẫn thẳng vào Tủ đồ cá nhân `My Closet` (F22) và Wizard đăng đồ (F23).
* **Swap:** Dẫn thẳng vào Sàn đồ trao đổi 1-1 `Swap Discovery` (F26).
* **Profile:** Chứa lối tắt vào Đơn mua (F18), Đơn bán (F24), Yêu cầu đổi đồ (F28), Món yêu thích (F13), Thông báo (F31) và bộ chuyển tài khoản demo (*Demo Switcher: Guest $\leftrightarrow$ User*).

---

## 4. Kiến Trúc Mã Nguồn & Lưu Trữ Dữ Liệu

```text
app/src/main/java/com/swapshe/app/
├── MainActivity.kt                      # Điểm khởi chạy Compose, edge-to-edge
├── data/
│   ├── MockData.kt                      # Kho dữ liệu mẫu chuẩn (sản phẩm, đơn mua, đơn bán, swap, user)
│   └── SwapSheRepository.kt             # Singleton Repository quản lý StateFlow cho toàn app
├── model/
│   ├── Product.kt                       # Product, ConditionType, ProductStatus, ProductMeasurements
│   ├── Order.kt                         # Order, OrderStatus, PaymentStatus, TimelineEvent, ShippingAddress
│   ├── Seller.kt                        # Seller, SellerReview
│   ├── User.kt                          # User, BodyMeasurements, AppNotification
│   └── SwapAndDispute.kt                # SwapOffer, SwapTransaction, TwoWayShipment, DisputeReport
├── theme/
│   ├── Color.kt                         # Bảng màu Soft Vintage Chic
│   ├── Theme.kt                         # Typography, shapes, Material3 theme
│   └── Type.kt
└── ui/
    ├── MainApp.kt                       # NavHost 31 màn hình + Animated Toast toàn cục
    ├── components/
    │   ├── AnimatedLikeButton.kt        # Nút like nảy trái tim
    │   ├── AuthGateModal.kt             # Modal chặn Guest chuyển hướng login
    │   ├── CommonButtons.kt             # PrimaryButton, SecondaryButton
    │   ├── CommonInputs.kt              # VintageTextField
    │   ├── ProductCard.kt               # Thẻ sản phẩm chuẩn 3:4
    │   ├── TimelineView.kt              # OrderTimelineView hiển thị timeline vận đơn
    │   └── ShimmerEffect.kt             # Hiệu ứng tải shimmer
    ├── navigation/
    │   ├── BottomBar.kt                 # Thanh điều hướng 5 tab chuẩn Sitemap v2
    │   └── Screen.kt                    # Sealed class định nghĩa các Route và đối số
    └── screens/
        ├── SplashScreen.kt (F01)
        ├── WelcomeScreen.kt (F02)
        ├── AuthScreen.kt (F03)
        ├── OnboardingScreens.kt (F04-F07)
        ├── HomeScreen.kt (F08)
        ├── ExploreScreen.kt (F09, F10)
        ├── ProductDetailScreen.kt (F11)
        ├── SellerProfileScreen.kt (F12)
        ├── WishlistScreen.kt (F13)
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

## 5. Hướng Dẫn Vận Hành & Khởi Chạy Ứng Dụng

### 5.1. Chạy trên Android Emulator hoặc Máy Thật
1. Chạy file batch khởi động nhanh emulator:
   ```cmd
   .\start_emulator.bat
   ```
2. Hoặc chạy toàn bộ quy trình biên dịch & khởi chạy app lên thiết bị:
   ```cmd
   .\Run_SWAPSHE.bat
   ```

### 5.2. Lệnh Biên Dịch Qua Terminal
```powershell
# Kiểm tra biên dịch Kotlin Compose:
.\gradlew.bat compileDebugKotlin

# Đóng gói file APK hoàn chỉnh:
.\gradlew.bat assembleDebug
```
*File APK đầu ra sau khi build:*  
`app/build/outputs/apk/debug/app-debug.apk`
