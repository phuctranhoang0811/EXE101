package com.swapshe.app.data

import com.swapshe.app.model.AppNotification
import com.swapshe.app.model.ConditionType
import com.swapshe.app.model.Order
import com.swapshe.app.model.OrderStatus
import com.swapshe.app.model.Product
import com.swapshe.app.model.ProductMeasurements
import com.swapshe.app.model.ProductStatus
import com.swapshe.app.model.Seller
import com.swapshe.app.model.SellerReview
import com.swapshe.app.model.ShippingAddress
import com.swapshe.app.model.TimelineEvent
import com.swapshe.app.model.User
import com.swapshe.app.model.UserRole
import com.swapshe.app.model.SheTrustProfile
import com.swapshe.app.model.TrustBadgeType
import com.swapshe.app.model.EkycStatus
import com.swapshe.app.model.Wallet
import com.swapshe.app.model.WalletTransaction
import com.swapshe.app.model.WalletTransactionType

data class StyleOption(val id: String, val name: String, val imageUrl: String)
data class ColorOption(val id: String, val name: String, val hex: Long)

object MockData {
    val linhReviews = listOf(
        SellerReview(
            id = "rev_01",
            reviewerName = "Hương Giang",
            reviewerAvatar = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150&q=80",
            rating = 5,
            comment = "Áo cardigan thơm phức, đóng gói kĩ và có thư tay cảm ơn siêu dễ thương. Đúng như miêu tả 99% like new!",
            productName = "Soft Pink Knit Cardigan",
            date = "Hôm qua"
        ),
        SellerReview(
            id = "rev_02",
            reviewerName = "Khánh Linh",
            reviewerAvatar = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=150&q=80",
            rating = 5,
            comment = "Chân váy xếp ly dáng vintage cực đẹp, mặc lên bồng bềnh rất tôn dáng. Giao hàng hoả tốc.",
            productName = "Vintage Pleated A-Line Midi Skirt",
            date = "3 ngày trước"
        ),
        SellerReview(
            id = "rev_03",
            reviewerName = "Minh Thư",
            reviewerAvatar = "https://images.unsplash.com/photo-1508214751196-bcfd4ca60f91?w=150&q=80",
            rating = 5,
            comment = "Shop tư vấn số đo kĩ càng, mình mặc vừa in. Sẽ ủng hộ tủ đồ của bạn dài dài!",
            productName = "Victorian Lace Collar Blouse",
            date = "1 tuần trước"
        )
    )

    val anReviews = listOf(
        SellerReview(
            id = "rev_04",
            reviewerName = "Thanh Trúc",
            reviewerAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&q=80",
            rating = 5,
            comment = "Blazer linen đẹp mê li, vải dày dặn mát rượi chuẩn form Hàn Quốc.",
            productName = "Oversized Oat Linen Blazer",
            date = "4 ngày trước"
        ),
        SellerReview(
            id = "rev_05",
            reviewerName = "Bảo Ngọc",
            reviewerAvatar = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=150&q=80",
            rating = 4,
            comment = "Áo khoác bouclé tweed rất xịn, khuy kim loại sáng bóng không trầy xước.",
            productName = "Parisian Bouclé Cropped Tweed",
            date = "2 tuần trước"
        )
    )

    val defaultSellerLinh = Seller(
        id = "seller_linh",
        name = "Thùy Linh",
        shopName = "Linh's Closet",
        avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300&q=80",
        rating = 4.9f,
        reviewCount = 84,
        successfulTransactions = 112,
        memberSince = "Tháng 3, 2024",
        location = "Quận 1, TP. Hồ Chí Minh",
        isVerified = true,
        bio = "Đam mê thời trang vintage & second-hand. Tất cả đồ đều được giặt ủi thơm tho và kiểm tra cẩn thận trước khi gửi.",
        reviews = linhReviews
    )

    val defaultSellerAn = Seller(
        id = "seller_an",
        name = "Phương An",
        shopName = "An's Wardrobe",
        avatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=300&q=80",
        rating = 4.8f,
        reviewCount = 42,
        successfulTransactions = 65,
        memberSince = "Tháng 8, 2024",
        location = "Đống Đa, Hà Nội",
        isVerified = true,
        bio = "Minimalist & Clean aesthetics. Đồ mặc 1-2 lần để chụp lookbook studio, pass lại cho các bạn cùng gu.",
        reviews = anReviews
    )

    val products = listOf(
        Product(
            id = "prod_offer_01",
            title = "Tape Winter Coat",
            brand = "Tape Outerwear",
            price = 235000L,
            originalPrice = 490000L,
            condition = ConditionType.LIKE_NEW,
            size = "M",
            category = "Outerwear",
            style = "Streetwear",
            color = "Amber Mustard",
            images = listOf(
                "https://images.unsplash.com/photo-1544441893-675973e31985?w=800&q=80",
                "https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=800&q=80"
            ),
            measurements = ProductMeasurements(bust = 96, length = 68, shoulder = 42),
            description = "Áo phao mùa đông Tape Winter Coat chần bông dày dặn, chống thấm nước, phom chuẩn M ấm áp.",
            seller = defaultSellerAn,
            canSwap = true,
            status = ProductStatus.AVAILABLE,
            isLiked = true
        ),
        Product(
            id = "prod_offer_02",
            title = "Arctic Shield Jacket",
            brand = "Arctic Tech",
            price = 219000L,
            originalPrice = 450000L,
            condition = ConditionType.LIKE_NEW,
            size = "XL",
            category = "Outerwear",
            style = "Sporty",
            color = "Crimson Red",
            images = listOf(
                "https://images.unsplash.com/photo-1548883354-7622d03aca27?w=800&q=80",
                "https://images.unsplash.com/photo-1512436991641-6745cdb1723f?w=800&q=80"
            ),
            measurements = ProductMeasurements(bust = 104, length = 72, shoulder = 46),
            description = "Áo khoác phao đỏ bóng Arctic Shield siêu ấm, mũ trùm chống tuyết, khóa kéo chống gió kép.",
            seller = defaultSellerLinh,
            canSwap = true,
            status = ProductStatus.AVAILABLE,
            isLiked = false
        ),
        Product(
            id = "prod_offer_03",
            title = "FrostGuard Puffer",
            brand = "FrostGuard",
            price = 199000L,
            originalPrice = 390000L,
            condition = ConditionType.EXCELLENT,
            size = "XXL",
            category = "Outerwear",
            style = "Minimal",
            color = "Jet Black",
            images = listOf(
                "https://images.unsplash.com/photo-1578587018452-892bacefd3f2?w=800&q=80",
                "https://images.unsplash.com/photo-1551488831-00ddcb6c6bd3?w=800&q=80"
            ),
            measurements = ProductMeasurements(bust = 110, length = 74, shoulder = 48),
            description = "Áo phao đen tuyền phom hộp oversize thời thượng, giữ nhiệt cực tốt dưới thời tiết lạnh giá.",
            seller = defaultSellerAn,
            canSwap = true,
            status = ProductStatus.AVAILABLE,
            isLiked = false
        ),
        Product(
            id = "prod_offer_04",
            title = "Alpine Thermal Down Jacket",
            brand = "Alpine Pro",
            price = 179000L,
            originalPrice = 350000L,
            condition = ConditionType.LIKE_NEW,
            size = "M",
            category = "Outerwear",
            style = "Outdoor",
            color = "Ochre Yellow",
            images = listOf(
                "https://images.unsplash.com/photo-1544441893-675973e31985?w=800&q=80",
                "https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=800&q=80"
            ),
            measurements = ProductMeasurements(bust = 98, length = 70, shoulder = 43),
            description = "Áo lông vũ Alpine Thermal siêu nhẹ nhưng cản gió xuất sắc, phối màu vàng đen năng động.",
            seller = defaultSellerLinh,
            canSwap = true,
            status = ProductStatus.AVAILABLE,
            isLiked = false
        ),
        Product(
            id = "demo_dress_01",
            title = "Đầm Maxi Hoa Đi Biển (Cottagecore)",
            brand = "Cottagecore Bloom",
            price = 320000L,
            originalPrice = 750000L,
            condition = ConditionType.LIKE_NEW,
            size = "M",
            category = "Dresses",
            style = "Vintage",
            color = "Beige Floral",
            images = listOf(
                "https://images.unsplash.com/photo-1496747611176-843222e1e57c?w=800&q=80",
                "https://images.unsplash.com/photo-1572804013309-59a88b7e92f1?w=800&q=80"
            ),
            measurements = ProductMeasurements(bust = 86, waist = 68, hips = 94, length = 115),
            fabricStretch = "Không co giãn (Non-stretch)",
            description = "Váy tơ hoa nhí dáng maxi vintage, tôn ngực và chiết eo thon. Dáng dài 115cm từ vai xuống, phù hợp đi biển, chụp ảnh ngoại cảnh.",
            seller = defaultSellerLinh,
            canSwap = true,
            status = ProductStatus.AVAILABLE,
            isLiked = true
        ),
        Product(
            id = "demo_coat_02",
            title = "Áo Khoác Trench Coat Xanh (Autumn)",
            brand = "Classic Atelier",
            price = 480000L,
            originalPrice = 1200000L,
            condition = ConditionType.LIKE_NEW,
            size = "L",
            category = "Outerwear",
            style = "Elegant",
            color = "Sage Green",
            images = listOf(
                "https://images.unsplash.com/photo-1544441893-675973e31985?w=800&q=80",
                "https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=800&q=80"
            ),
            measurements = ProductMeasurements(bust = 98, waist = 92, hips = 100, length = 95),
            fabricStretch = "Form rộng / Khoác ngoài",
            description = "Chất kaki tuyết mưa cao cấp màu xanh rêu pastel, phom suông thanh lịch chống gió nhẹ, dáng khoác ngoài dễ mặc layer.",
            seller = defaultSellerAn,
            canSwap = true,
            status = ProductStatus.AVAILABLE,
            isLiked = true
        ),
        Product(
            id = "demo_skirt_03",
            title = "Chân Váy Chữ A Công Sở (Midi Skirt)",
            brand = "Modern Office",
            price = 210000L,
            originalPrice = 450000L,
            condition = ConditionType.EXCELLENT,
            size = "S",
            category = "Skirts",
            style = "Minimal",
            color = "Camel",
            images = listOf(
                "https://images.unsplash.com/photo-1583496661160-fb5886a0aaaa?w=800&q=80",
                "https://images.unsplash.com/photo-1552902865-b72c031ac5ea?w=800&q=80"
            ),
            measurements = ProductMeasurements(bust = null, waist = 64, hips = 90, length = 65),
            fabricStretch = "Co giãn nhẹ",
            description = "Chân váy chữ A cạp cao tôn dáng, chất vải tây đứng phom ít nhăn, độ dài 65cm từ cạp eo xuống che khuyết điểm đùi cực tốt.",
            seller = defaultSellerLinh,
            canSwap = false,
            status = ProductStatus.AVAILABLE,
            isLiked = false
        ),
        Product(
            id = "prod_001",
            title = "Soft Pink Knit Cardigan",
            brand = "Vintage Chic",
            price = 120000L,
            originalPrice = 280000L,
            condition = ConditionType.LIKE_NEW,
            size = "S",
            category = "Tops",
            style = "Vintage",
            color = "Dusty Pink",
            images = listOf(
                "https://images.unsplash.com/photo-1434389677669-e08b4cac3105?w=800&q=80",
                "https://images.unsplash.com/photo-1576566588028-4147f3842f27?w=800&q=80"
            ),
            measurements = ProductMeasurements(bust = 84, length = 48, waist = 72, shoulder = 36),
            description = "Áo len cardigan dệt kim mỏng nhẹ màu hồng tro vintage, cúc ngọc trai nữ tính. Phù hợp mặc khoác ngoài nhẹ nhàng mùa thu hoặc trong phòng máy lạnh. Tình trạng như mới, không sờn rách.",
            seller = defaultSellerLinh,
            canSwap = true,
            status = ProductStatus.AVAILABLE,
            isLiked = true
        ),
        Product(
            id = "prod_002",
            title = "Oversized Oat Linen Blazer",
            brand = "Minimal Studio",
            price = 245000L,
            originalPrice = 520000L,
            condition = ConditionType.EXCELLENT,
            size = "M",
            category = "Outerwear",
            style = "Minimal",
            color = "Beige",
            images = listOf(
                "https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=800&q=80",
                "https://images.unsplash.com/photo-1551488831-00ddcb6c6bd3?w=800&q=80"
            ),
            measurements = ProductMeasurements(bust = 96, length = 68, shoulder = 40),
            description = "Blazer vải đũi linen màu yến mạch tự nhiên, phom suông rộng thanh lịch phong cách Hàn Quốc. Đã giặt hấp sạch sẽ.",
            seller = defaultSellerAn,
            canSwap = true,
            status = ProductStatus.AVAILABLE,
            isLiked = true
        ),
        Product(
            id = "prod_003",
            title = "Vintage Pleated A-Line Midi Skirt",
            brand = "Retro Elegance",
            price = 110000L,
            originalPrice = 250000L,
            condition = ConditionType.GOOD,
            size = "S",
            category = "Skirts",
            style = "Vintage",
            color = "Cream",
            images = listOf(
                "https://images.unsplash.com/photo-1583496661160-fb5886a0aaaa?w=800&q=80"
            ),
            measurements = ProductMeasurements(waist = 66, hips = 90, length = 74),
            description = "Chân váy xếp ly dáng dài màu kem bơ nhẹ nhàng, lưng thun co giãn thoải mái, vạt váy bồng bềnh nữ tính.",
            seller = defaultSellerLinh,
            canSwap = false,
            status = ProductStatus.AVAILABLE
        ),
        Product(
            id = "prod_004",
            title = "Parisian Bouclé Cropped Tweed",
            brand = "Chic Atelier",
            price = 310000L,
            originalPrice = 750000L,
            condition = ConditionType.LIKE_NEW,
            size = "S",
            category = "Outerwear",
            style = "Elegant",
            color = "Black",
            images = listOf(
                "https://images.unsplash.com/photo-1544441893-675973e31985?w=800&q=80"
            ),
            measurements = ProductMeasurements(bust = 86, length = 46, shoulder = 37),
            description = "Áo khoác dạ tweed dáng lửng phối viền ánh kim, phom chuẩn quý cô Paris. Mặc đúng 1 lần đi tiệc sinh nhật.",
            seller = defaultSellerAn,
            canSwap = false,
            status = ProductStatus.AVAILABLE
        ),
        Product(
            id = "prod_005",
            title = "Y2K Denim Cargo Skirt with Belt",
            brand = "Retro 2000s",
            price = 160000L,
            originalPrice = 350000L,
            condition = ConditionType.EXCELLENT,
            size = "M",
            category = "Skirts",
            style = "Y2K",
            color = "Blue",
            images = listOf(
                "https://images.unsplash.com/photo-1552902865-b72c031ac5ea?w=800&q=80"
            ),
            measurements = ProductMeasurements(waist = 70, hips = 92, length = 42),
            description = "Chân váy jean túi hộp phong cách Y2K kèm thắt lưng vintage cá tính, chất denim dày dặn không bai dão.",
            seller = defaultSellerLinh,
            canSwap = true,
            status = ProductStatus.AVAILABLE
        ),
        Product(
            id = "prod_006",
            title = "French Floral Silk Midi Dress",
            brand = "Parisian Garden",
            price = 195000L,
            originalPrice = 420000L,
            condition = ConditionType.LIKE_NEW,
            size = "S",
            category = "Dresses",
            style = "Vintage",
            color = "Dusty Pink",
            images = listOf(
                "https://images.unsplash.com/photo-1572804013309-59a88b7e92f1?w=800&q=80"
            ),
            measurements = ProductMeasurements(bust = 84, waist = 66, hips = 92, length = 105),
            description = "Váy hoa nhí lụa tơ tằm cổ tim nhẹ nhàng, họa tiết vintage hoa hồng thanh tao. Có khách đang giữ đơn thanh toán.",
            seller = defaultSellerAn,
            canSwap = true,
            status = ProductStatus.RESERVED
        ),
        Product(
            id = "prod_007",
            title = "Travertine Leather Baguette Bag",
            brand = "Heritage Leather",
            price = 180000L,
            originalPrice = 400000L,
            condition = ConditionType.GOOD,
            size = "Free",
            category = "Bags",
            style = "Minimal",
            color = "Beige",
            images = listOf(
                "https://images.unsplash.com/photo-1548036328-c9fa89d128fa?w=800&q=80"
            ),
            measurements = ProductMeasurements(length = 26, bust = 15),
            description = "Túi kẹp nách da bò thật màu nâu caramel, khóa đồng cổ điển. Đã bán cho khách hàng quen.",
            seller = defaultSellerLinh,
            canSwap = false,
            status = ProductStatus.SOLD
        ),
        Product(
            id = "prod_008",
            title = "High-Waist Wide Leg Linen Trousers",
            brand = "Oatmeal Studio",
            price = 180000L,
            originalPrice = 390000L,
            condition = ConditionType.LIKE_NEW,
            size = "M",
            category = "Pants",
            style = "Minimal",
            color = "Beige",
            images = listOf(
                "https://images.unsplash.com/photo-1509631179647-0177331693ae?w=800&q=80"
            ),
            measurements = ProductMeasurements(waist = 68, hips = 96, length = 102),
            description = "Quần tây ống rộng cạp cao tôn dáng chất vải đũi linen tự nhiên, không nhăn nhúm, phong cách tối giản thanh lịch.",
            seller = defaultSellerAn,
            canSwap = true,
            status = ProductStatus.AVAILABLE
        ),
        Product(
            id = "prod_009",
            title = "Vintage Cable Knit Cream Sweater",
            brand = "Nordic Wool",
            price = 165000L,
            originalPrice = 420000L,
            condition = ConditionType.EXCELLENT,
            size = "L",
            category = "Tops",
            style = "Vintage",
            color = "Cream",
            images = listOf(
                "https://images.unsplash.com/photo-1576566588028-4147f3842f27?w=800&q=80"
            ),
            measurements = ProductMeasurements(bust = 102, length = 62, shoulder = 44),
            description = "Áo len vặn thừng dày dặn màu kem sữa phong cách Bắc Âu cổ điển, giữ ấm tốt và phối đồ cực xinh.",
            seller = defaultSellerLinh,
            canSwap = true,
            status = ProductStatus.AVAILABLE
        ),
        Product(
            id = "prod_010",
            title = "Silk Satin Camisole Top",
            brand = "L'Amour Vintage",
            price = 95000L,
            originalPrice = 220000L,
            condition = ConditionType.LIKE_NEW,
            size = "S",
            category = "Tops",
            style = "Minimal",
            color = "Sage",
            images = listOf(
                "https://images.unsplash.com/photo-1503342217505-b0a15ec3261c?w=800&q=80"
            ),
            measurements = ProductMeasurements(bust = 82, length = 45),
            description = "Áo hai dây lụa satin màu xanh xô thơm bóng mịn, viền ren tinh tế, mặc phối trong blazer rất sang.",
            seller = defaultSellerAn,
            canSwap = false,
            status = ProductStatus.AVAILABLE
        ),
        Product(
            id = "prod_011",
            title = "Retro High-Rise Flared Corduroy Pants",
            brand = "Autumn 90s",
            price = 210000L,
            originalPrice = 480000L,
            condition = ConditionType.EXCELLENT,
            size = "S",
            category = "Pants",
            style = "Vintage",
            color = "Sage",
            images = listOf(
                "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=800&q=80"
            ),
            measurements = ProductMeasurements(waist = 64, hips = 90, length = 100),
            description = "Quần nhung tăm ống loe cạp cao phong cách thập niên 90, chất nhung gân mịn co giãn nhẹ.",
            seller = defaultSellerLinh,
            canSwap = true,
            status = ProductStatus.AVAILABLE
        ),
        Product(
            id = "prod_012",
            title = "Victorian Lace Collar Blouse",
            brand = "Antique Charm",
            price = 135000L,
            originalPrice = 310000L,
            condition = ConditionType.GOOD,
            size = "M",
            category = "Tops",
            style = "Vintage",
            color = "Cream",
            images = listOf(
                "https://images.unsplash.com/photo-1551488831-00ddcb6c6bd3?w=800&q=80"
            ),
            measurements = ProductMeasurements(bust = 90, length = 52, shoulder = 38),
            description = "Áo sơ mi cổ ren hoàng gia Anh, tay phồng bo chun điệu đà, cúc ngọc trai nguyên vẹn.",
            seller = defaultSellerLinh,
            canSwap = false,
            status = ProductStatus.AVAILABLE
        ),
        Product(
            id = "prod_013",
            title = "Bohemian Tiered Prairie Maxi Dress",
            brand = "Cottagecore Folk",
            price = 260000L,
            originalPrice = 580000L,
            condition = ConditionType.LIKE_NEW,
            size = "Free",
            category = "Dresses",
            style = "Vintage",
            color = "Dusty Pink",
            images = listOf(
                "https://images.unsplash.com/photo-1496747611176-843222e1e57c?w=800&q=80"
            ),
            measurements = ProductMeasurements(bust = 92, waist = 74, hips = 104, length = 120),
            description = "Đầm maxi tầng phong cách đồng nội bohemian, vải thô cotton tự nhiên thoáng mát, thắt nơ lưng duyên dáng.",
            seller = defaultSellerAn,
            canSwap = true,
            status = ProductStatus.AVAILABLE
        ),
        Product(
            id = "prod_014",
            title = "Structured Boxy Wool Trench Coat",
            brand = "Minimal Tailor",
            price = 340000L,
            originalPrice = 850000L,
            condition = ConditionType.LIKE_NEW,
            size = "L",
            category = "Outerwear",
            style = "Minimal",
            color = "Black",
            images = listOf(
                "https://images.unsplash.com/photo-1539109136881-3be0616acf4b?w=800&q=80"
            ),
            measurements = ProductMeasurements(bust = 104, length = 108, shoulder = 46),
            description = "Áo măng tô dạ dáng dài phom boxy hiện đại, lót lụa mềm mại, ve áo sắc sảo chuẩn gu thanh lịch.",
            seller = defaultSellerAn,
            canSwap = false,
            status = ProductStatus.AVAILABLE
        ),
        Product(
            id = "prod_015",
            title = "Vintage Quilted Chain Shoulder Bag",
            brand = "Retro Glamour",
            price = 220000L,
            originalPrice = 590000L,
            condition = ConditionType.EXCELLENT,
            size = "Free",
            category = "Bags",
            style = "Elegant",
            color = "Black",
            images = listOf(
                "https://images.unsplash.com/photo-1566150905458-1bf1fc113f0d?w=800&q=80"
            ),
            measurements = ProductMeasurements(length = 24, bust = 16),
            description = "Túi chần bông quả trám dây xích vàng ánh kim, da mềm bóng đẹp, khoá bấm chắc chắn.",
            seller = defaultSellerLinh,
            canSwap = true,
            status = ProductStatus.AVAILABLE
        ),
        Product(
            id = "prod_016",
            title = "90s Raw Hem Denim Jacket",
            brand = "Classic Denim Co.",
            price = 190000L,
            originalPrice = 450000L,
            condition = ConditionType.GOOD,
            size = "M",
            category = "Outerwear",
            style = "Streetwear",
            color = "Blue",
            images = listOf(
                "https://images.unsplash.com/photo-1543087903-1ac2ec7aa8c5?w=800&q=80"
            ),
            measurements = ProductMeasurements(bust = 98, length = 56, shoulder = 42),
            description = "Áo khoác bò viền tua rua phong cách grunge thập niên 90, wash màu bụi bặm cá tính.",
            seller = defaultSellerAn,
            canSwap = true,
            status = ProductStatus.AVAILABLE
        ),
        Product(
            id = "prod_017",
            title = "Petite Linen Button-Down Crop Top",
            brand = "French Minimal",
            price = 145000L,
            originalPrice = 320000L,
            condition = ConditionType.LIKE_NEW,
            size = "XS",
            category = "Tops",
            style = "Minimal",
            color = "Cream",
            images = listOf(
                "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800&q=80"
            ),
            measurements = ProductMeasurements(bust = 80, length = 42, waist = 62, shoulder = 34),
            description = "Áo crop top vải đũi linen nhẹ mát, phom petite ôm gọn cho bạn gái nhỏ nhắn. Cúc gỗ vintage tinh xảo.",
            seller = defaultSellerLinh,
            canSwap = true,
            status = ProductStatus.AVAILABLE
        ),
        Product(
            id = "prod_018",
            title = "Relaxed Vintage Houndstooth Blazer",
            brand = "Classic Heritage",
            price = 285000L,
            originalPrice = 650000L,
            condition = ConditionType.EXCELLENT,
            size = "XL",
            category = "Outerwear",
            style = "Vintage",
            color = "Beige",
            images = listOf(
                "https://images.unsplash.com/photo-1551488831-00ddcb6c6bd3?w=800&q=80"
            ),
            measurements = ProductMeasurements(bust = 108, length = 74, shoulder = 46),
            description = "Blazer dạ họa tiết răng sói dáng suông oversize rộng rãi, mặc layer cực ấm và thời thượng.",
            seller = defaultSellerAn,
            canSwap = true,
            status = ProductStatus.AVAILABLE
        )
    )

    val styleOptions = listOf(
        StyleOption("vintage", "Vintage", "https://images.unsplash.com/photo-1434389677669-e08b4cac3105?w=500&q=80"),
        StyleOption("casual", "Casual", "https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?w=500&q=80"),
        StyleOption("minimal", "Minimal", "https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=500&q=80"),
        StyleOption("y2k", "Y2K", "https://images.unsplash.com/photo-1552902865-b72c031ac5ea?w=500&q=80"),
        StyleOption("elegant", "Elegant", "https://images.unsplash.com/photo-1544441893-675973e31985?w=500&q=80"),
        StyleOption("streetwear", "Streetwear", "https://images.unsplash.com/photo-1509631179647-0177331693ae?w=500&q=80")
    )

    val colorOptions = listOf(
        ColorOption("cream", "Cream", 0xFFFFFDD0),
        ColorOption("black", "Black", 0xFF222222),
        ColorOption("pink", "Dusty Pink", 0xFFD9A8B8),
        ColorOption("lavender", "Lavender", 0xFFD4C2FC),
        ColorOption("sage", "Sage", 0xFF9CAF88),
        ColorOption("blue", "Blue", 0xFF8AA8C4),
        ColorOption("beige", "Beige", 0xFFE5D5C5)
    )

    val categories = listOf("All", "Tops", "Skirts", "Pants", "Dresses", "Outerwear", "Bags")

    val defaultAddress = ShippingAddress(
        fullName = "Nguyễn Hoàng My",
        phone = "0987654321",
        province = "TP. Hồ Chí Minh",
        ward = "Phường Bến Nghé, Quận 1",
        detailAddress = "120 Nguyễn Huệ, Căn hộ 4B"
    )

    val demoBuyerUser = User(
        id = "user_my",
        name = "Hoàng My (Buyer 👩)",
        email = "buyer.my@swapshe.vn",
        isGuest = false,
        avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=300&q=80",
        stylePreferences = setOf("Vintage", "Minimal"),
        sizePreference = "S",
        colorPreferences = setOf("Dusty Pink", "Cream"),
        savedAddresses = listOf(defaultAddress),
        role = UserRole.BUYER,
        bio = "Người mua chính hiệu • Săn đồ vintage secondhand chuẩn Size S • Ký quỹ Escrow bảo đảm 100%",
        trustProfile = SheTrustProfile(
            score = 95,
            badge = TrustBadgeType.QUEEN_CLOSET,
            status = EkycStatus.VERIFIED,
            completedOrdersCount = 18,
            disputeFreeStreakMonths = 6
        ),
        wallet = Wallet(
            availableBalance = 1850000L,
            escrowHoldBalance = 420000L,
            pendingWithdrawal = 0L,
            transactions = listOf(
                WalletTransaction(
                    id = "tx_b01",
                    type = WalletTransactionType.ESCROW_HOLD,
                    amount = 420000L,
                    title = "Ký quỹ Escrow mua đồ",
                    description = "Tạm giữ an toàn tiền mua hàng đơn #SW-20261014-001",
                    timestamp = "14/10/2026 20:30",
                    isPositive = false,
                    orderCode = "SW-20261014-001"
                ),
                WalletTransaction(
                    id = "tx_b02",
                    type = WalletTransactionType.SWAP_DEPOSIT,
                    amount = 50000L,
                    title = "Cọc bảo đảm đổi đồ 1-1",
                    description = "Ký quỹ Escrow bảo đảm giao dịch đổi với Linh's Closet",
                    timestamp = "14/10/2026 20:00",
                    isPositive = false
                )
            )
        )
    )

    val demoSellerUser = User(
        id = "seller_linh",
        name = "Thùy Linh - Linh's Closet (Seller 👗)",
        email = "seller.linh@swapshe.vn",
        isGuest = false,
        avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300&q=80",
        stylePreferences = setOf("Vintage", "Casual", "Elegant"),
        sizePreference = "M",
        colorPreferences = setOf("Cream", "Pastel"),
        savedAddresses = listOf(
            ShippingAddress(
                fullName = "Thùy Linh (Linh's Closet)",
                phone = "0908889999",
                province = "TP. Hồ Chí Minh",
                ward = "Phường Bến Nghé, Quận 1",
                detailAddress = "Kho đồ: 28 Lê Lợi"
            )
        ),
        role = UserRole.SELLER,
        bio = "Top Seller Linh's Closet • 112 giao dịch thành công • Chuyên đồ Like New 99% tuyển chọn Nhật & Hàn",
        trustProfile = SheTrustProfile(
            score = 99,
            badge = TrustBadgeType.QUEEN_CLOSET,
            status = EkycStatus.VERIFIED,
            completedOrdersCount = 112,
            disputeFreeStreakMonths = 12
        ),
        wallet = Wallet(
            availableBalance = 5420000L,
            escrowHoldBalance = 640000L,
            pendingWithdrawal = 1200000L,
            transactions = listOf(
                WalletTransaction(
                    id = "tx_s01",
                    type = WalletTransactionType.RELEASE_EARNING,
                    amount = 304000L,
                    title = "Doanh thu bán đồ (Đã trừ 5% phí sàn)",
                    description = "Đơn #SW-20261014-002 đã hoàn tất giải ngân",
                    timestamp = "14/10/2026 18:00",
                    isPositive = true,
                    orderCode = "SW-20261014-002"
                ),
                WalletTransaction(
                    id = "tx_s02",
                    type = WalletTransactionType.WITHDRAW_BANK,
                    amount = 1200000L,
                    title = "Rút tiền về Vietcombank (*9812)",
                    description = "Lệnh chuyển tiền tài khoản ngân hàng đang xử lý",
                    timestamp = "15/10/2026 09:15",
                    isPositive = false
                )
            )
        )
    )

    val demoAdminUser = User(
        id = "admin_swapshe",
        name = "Trọng tài SwapShe (Admin ⚖️)",
        email = "trust.safety@swapshe.vn",
        isGuest = false,
        avatarUrl = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=300&q=80",
        stylePreferences = setOf("Minimal"),
        sizePreference = "Free",
        role = UserRole.ADMIN,
        bio = "Bộ phận Trust & Safety SwapShe • Trọng tài phân xử khiếu nại Escrow 48h • Kiểm soát dòng tiền gian lận",
        trustProfile = SheTrustProfile(
            score = 100,
            badge = TrustBadgeType.QUEEN_CLOSET,
            status = EkycStatus.VERIFIED,
            completedOrdersCount = 999,
            disputeFreeStreakMonths = 24
        ),
        wallet = Wallet(
            availableBalance = 99999000L,
            escrowHoldBalance = 15400000L,
            pendingWithdrawal = 0L,
            transactions = emptyList()
        )
    )

    val initialUser = demoBuyerUser

    val notifications = listOf(
        AppNotification(
            id = "notif_01",
            title = "Đơn hàng đang giao đến bạn 🚚",
            message = "Đơn hàng SW-20261014-002 đã được bàn giao cho tài xế GHN. Dự kiến giao hôm nay.",
            timestamp = "15 phút trước",
            isRead = false,
            type = "order"
        ),
        AppNotification(
            id = "notif_02",
            title = "Kho báu yêu thích giảm giá ✨",
            message = "Chiếc áo Parisian Bouclé Cropped Tweed vừa giảm giá còn 310.000đ!",
            timestamp = "2 giờ trước",
            isRead = false,
            type = "price_drop"
        ),
        AppNotification(
            id = "notif_03",
            title = "Lời mời trao đổi đồ mới ⇄",
            message = "Linh's Closet muốn trao đổi chiếc váy floral silk với blazer của bạn.",
            timestamp = "Hôm qua",
            isRead = true,
            type = "swap"
        )
    )

    val initialOrders = listOf(
        Order(
            id = "order_001",
            internalCode = "SW-20261014-001",
            trackingCode = "GHN-89218342",
            item = products[0],
            shippingAddress = defaultAddress,
            deliveryMethod = "Standard Delivery",
            shippingFee = 25000L,
            subtotal = 120000L,
            total = 145000L,
            status = OrderStatus.PREPARING,
            timeline = listOf(
                TimelineEvent("Payment confirmed", "Mô phỏng thanh toán đã xác thực thành công", "14/10/2026 20:30"),
                TimelineEvent("Seller preparing item", "Người bán đang đóng gói sản phẩm và ghi mã vận đơn", "14/10/2026 21:00")
            )
        ),
        Order(
            id = "order_002",
            internalCode = "SW-20261014-002",
            trackingCode = "GHN-77319201",
            item = products[1],
            shippingAddress = defaultAddress,
            deliveryMethod = "Express Delivery",
            shippingFee = 35000L,
            subtotal = 245000L,
            total = 280000L,
            status = OrderStatus.SHIPPED,
            timeline = listOf(
                TimelineEvent("Payment confirmed", "Thanh toán thành công", "13/10/2026 14:15"),
                TimelineEvent("Seller prepared item", "Người bán đã đóng gói và dán niêm phong", "13/10/2026 16:30"),
                TimelineEvent("Picked up by GHN", "Đơn vị vận chuyển đã lấy hàng từ kho", "14/10/2026 08:30"),
                TimelineEvent("Out for delivery", "Bưu tá đang trên đường giao đến bạn", "14/10/2026 14:00")
            )
        ),
        Order(
            id = "order_003",
            internalCode = "SW-20261011-008",
            trackingCode = "GHN-66120399",
            item = products[2],
            shippingAddress = defaultAddress,
            deliveryMethod = "Standard Delivery",
            shippingFee = 25000L,
            subtotal = 110000L,
            total = 135000L,
            status = OrderStatus.DELIVERED,
            timeline = listOf(
                TimelineEvent("Payment confirmed", "Thanh toán thành công", "11/10/2026 09:00"),
                TimelineEvent("Delivered successfully", "Kiện hàng đã giao thành công và có chữ ký xác nhận", "13/10/2026 15:45")
            )
        ),
        Order(
            id = "order_004",
            internalCode = "SW-20261008-015",
            trackingCode = "GHN-55291032",
            item = products[4],
            shippingAddress = defaultAddress,
            deliveryMethod = "Standard Delivery",
            shippingFee = 25000L,
            subtotal = 160000L,
            total = 185000L,
            status = OrderStatus.CANCELLED,
            timeline = listOf(
                TimelineEvent("Order placed", "Đơn hàng được khởi tạo", "08/10/2026 11:20"),
                TimelineEvent("Order cancelled", "Người mua hủy đơn hàng trước khi người bán chuẩn bị", "08/10/2026 11:35")
            )
        )
    )

    val mySellerProfile = Seller(
        id = "user_my",
        name = "Hoàng My",
        shopName = "My's Chic Closet",
        avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=300&q=80",
        rating = 4.95f,
        reviewCount = 18,
        successfulTransactions = 24,
        memberSince = "Tháng 1, 2024",
        location = "Quận 1, TP. Hồ Chí Minh",
        isVerified = true,
        bio = "Tủ đồ cá nhân của Hoàng My. Đồ mặc giữ gìn rất kĩ, giặt tay thơm tho, sẵn sàng trao đổi 1-1 với các bạn có cùng gu vintage chic!"
    )

    val myClosetInitial = listOf(
        Product(
            id = "my_prod_01",
            title = "Vintage Embroidered Cardigan",
            brand = "Handmade Vintage",
            price = 145000L,
            originalPrice = 320000L,
            condition = ConditionType.LIKE_NEW,
            size = "S",
            category = "Tops",
            style = "Vintage",
            color = "Cream",
            images = listOf("https://images.unsplash.com/photo-1434389677669-e08b4cac3105?w=800&q=80"),
            measurements = ProductMeasurements(bust = 86, length = 50, waist = 70),
            description = "Áo cardigan len thêu hoa nhí cúc gỗ cực xinh, vải dày dặn mềm mịn không ngứa.",
            seller = mySellerProfile,
            canSwap = true,
            status = ProductStatus.AVAILABLE,
            ownerId = "user_my"
        ),
        Product(
            id = "my_prod_02",
            title = "High-Waist Pleated Midi Skirt",
            brand = "Korean Select",
            price = 115000L,
            originalPrice = 280000L,
            condition = ConditionType.EXCELLENT,
            size = "S",
            category = "Skirts",
            style = "Minimal",
            color = "Dusty Pink",
            images = listOf("https://images.unsplash.com/photo-1583496661160-fb5886a0aaaa?w=800&q=80"),
            measurements = ProductMeasurements(waist = 64, hips = 88, length = 72),
            description = "Chân váy dập ly dáng chữ A thanh lịch, màu hồng tro tôn da.",
            seller = mySellerProfile,
            canSwap = true,
            status = ProductStatus.RESERVED,
            ownerId = "user_my"
        ),
        Product(
            id = "my_prod_03",
            title = "French Tweed Crop Top",
            brand = "Parisian Mood",
            price = 190000L,
            originalPrice = 450000L,
            condition = ConditionType.LIKE_NEW,
            size = "S",
            category = "Tops",
            style = "Elegant",
            color = "Beige",
            images = listOf("https://images.unsplash.com/photo-1544441893-675973e31985?w=800&q=80"),
            measurements = ProductMeasurements(bust = 84, length = 42),
            description = "Áo dạ tweed lửng phối nút ngọc trai tinh xảo, diện tiệc hoặc chụp lookbook.",
            seller = mySellerProfile,
            canSwap = false,
            status = ProductStatus.SOLD,
            ownerId = "user_my"
        ),
        Product(
            id = "my_prod_04",
            title = "Retro Oversized Denim Shirt (Bản nháp)",
            brand = "Vintage Denim",
            price = 130000L,
            condition = ConditionType.GOOD,
            size = "M",
            category = "Tops",
            style = "Vintage",
            color = "Blue",
            images = listOf("https://images.unsplash.com/photo-1543087903-1ac2ec7aa8c5?w=800&q=80"),
            measurements = ProductMeasurements(bust = 96, length = 64),
            description = "Áo sơ mi jeans dáng thụng phong cách thập niên 90. Cần chụp thêm ảnh nhãn mác.",
            seller = mySellerProfile,
            canSwap = true,
            status = ProductStatus.DRAFT,
            ownerId = "user_my"
        )
    )

    val initialSellerOrders = listOf(
        Order(
            id = "seller_order_001",
            internalCode = "SW-20261015-081",
            trackingCode = "GHN-66442211",
            item = myClosetInitial[1], // High-Waist Pleated Midi Skirt
            shippingAddress = ShippingAddress(
                fullName = "Lê Thu Trang",
                phone = "0912345678",
                province = "Hà Nội",
                ward = "Phường Hàng Trống, Quận Hoàn Kiếm",
                detailAddress = "45 Phố Nhà Chung"
            ),
            deliveryMethod = "Standard Delivery",
            shippingFee = 25000L,
            subtotal = 115000L,
            total = 140000L,
            status = OrderStatus.PREPARING,
            paymentStatus = com.swapshe.app.model.PaymentStatus.PAID,
            buyerId = "user_trang",
            sellerId = "user_my",
            timeline = listOf(
                TimelineEvent("Payment confirmed", "Người mua đã thanh toán thành công", "15/10/2026 08:30"),
                TimelineEvent("Seller preparing", "Chờ bạn đóng gói và gửi hàng", "Đang xử lý", false)
            )
        )
    )

    val initialSwapOffers = listOf(
        com.swapshe.app.model.SwapOffer(
            id = "swap_offer_01",
            senderId = "seller_linh",
            senderName = "Thùy Linh (Linh's Closet)",
            senderAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300&q=80",
            recipientId = "user_my",
            recipientName = "Hoàng My",
            offeredProduct = products[0], // Soft Pink Knit Cardigan
            requestedProduct = myClosetInitial[0], // Vintage Embroidered Cardigan
            note = "Chào My, mình cực thích họa tiết hoa nhí trên chiếc áo của bạn. Đổi ngang chiếc Cardigan hồng tro của mình nhé, size vừa in luôn!",
            status = com.swapshe.app.model.SwapOfferStatus.PENDING,
            createdAt = "Hôm qua lúc 18:30"
        ),
        com.swapshe.app.model.SwapOffer(
            id = "swap_offer_02",
            senderId = "user_my",
            senderName = "Hoàng My",
            senderAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=300&q=80",
            recipientId = "seller_an",
            recipientName = "Phương An (An's Wardrobe)",
            offeredProduct = myClosetInitial[0],
            requestedProduct = products[1], // Oversized Oat Linen Blazer
            note = "Mình muốn đổi áo cardigan len lấy chiếc blazer đũi linen này của bạn nhé!",
            status = com.swapshe.app.model.SwapOfferStatus.PENDING,
            createdAt = "Hôm nay lúc 09:15"
        )
    )

    val initialSwapTransactions = listOf(
        com.swapshe.app.model.SwapTransaction(
            id = "swap_tx_01",
            offerId = "swap_offer_completed",
            userAId = "user_my",
            userAName = "Hoàng My",
            userBId = "seller_linh",
            userBName = "Thùy Linh",
            productA = myClosetInitial[2],
            productB = products[2],
            shipmentAtoB = com.swapshe.app.model.TwoWayShipment(
                id = "ship_01",
                fromUserId = "user_my",
                toUserId = "seller_linh",
                trackingCodeDemo = "DEMO-SHIP-A2B-9812",
                isDispatched = true,
                dispatchedAt = "12/10/2026 10:00",
                isDelivered = true,
                isReceivedConfirmed = true,
                receivedAt = "14/10/2026 15:30"
            ),
            shipmentBtoA = com.swapshe.app.model.TwoWayShipment(
                id = "ship_02",
                fromUserId = "seller_linh",
                toUserId = "user_my",
                trackingCodeDemo = "DEMO-SHIP-B2A-7731",
                isDispatched = true,
                dispatchedAt = "12/10/2026 11:20",
                isDelivered = true,
                isReceivedConfirmed = true,
                receivedAt = "14/10/2026 16:00"
            ),
            status = com.swapshe.app.model.SwapTransactionStatus.COMPLETED,
            createdAt = "11/10/2026 14:00",
            timeline = listOf(
                TimelineEvent("Đề nghị trao đổi được chấp thuận", "Cả 2 bên đồng ý đổi 1-1", "11/10/2026 14:00"),
                TimelineEvent("Cả 2 bên đã gửi hàng", "Đã khởi tạo 2 vận đơn hai chiều", "12/10/2026 11:30"),
                TimelineEvent("Hoàn tất trao đổi", "Cả 2 bên đều đã bấm xác nhận nhận đồ", "14/10/2026 16:00")
            )
        )
    )

    val initialDisputes = listOf(
        com.swapshe.app.model.DisputeReport(
            id = "disp_001",
            reporterId = "user_my",
            reporterName = "Hoàng My (Buyer 👩)",
            targetType = com.swapshe.app.model.DisputeTargetType.ORDER,
            targetId = "order_001",
            targetTitle = "Đơn hàng #SW-20261014-001 (Đầm Maxi Hoa Bloom)",
            reason = "Hàng bị ố vàng nách áo và rách nhẹ đường chỉ",
            description = "Tôi nhận hàng mở ra kiểm tra thấy vết ố vàng không đúng với cam kết Like New 99% của shop. Đã quay video unbox đầy đủ.",
            status = com.swapshe.app.model.DisputeStatus.UNDER_REVIEW,
            requestedRefundAmount = 145000L,
            sellerOffer = com.swapshe.app.model.DisputeOffer(
                type = com.swapshe.app.model.DisputeOfferType.PARTIAL_REFUND,
                amount = 45000L,
                note = "Shop xin lỗi vì sơ suất của nhân viên soạn đồ, xin gửi hoàn bạn 45.000đ giữ áo."
            ),
            evidencePhotos = listOf(
                "https://images.unsplash.com/photo-1583496661160-fb5886a0aaaa?w=800&q=80",
                "https://images.unsplash.com/photo-1552902865-b72c031ac5ea?w=800&q=80"
            ),
            evidenceVideoUrl = "https://assets.mixkit.co/videos/preview/mixkit-hands-opening-a-clothing-package-41484-large.mp4",
            createdAt = "Hôm nay lúc 10:15",
            resolutionNote = null
        ),
        com.swapshe.app.model.DisputeReport(
            id = "disp_002",
            reporterId = "user_my",
            reporterName = "Hoàng My (Buyer 👩)",
            targetType = com.swapshe.app.model.DisputeTargetType.ORDER,
            targetId = "order_004",
            targetTitle = "Đơn hàng SW-20261008-015",
            reason = "Người bán phản hồi chậm",
            description = "Người bán hẹn gửi hàng nhưng quá 48h chưa gửi và không phản hồi tin nhắn.",
            status = com.swapshe.app.model.DisputeStatus.RESOLVED,
            createdAt = "09/10/2026 10:00",
            resolutionNote = "Đã hỗ trợ người mua hủy đơn và Escrow hoàn tiền 100% thành công."
        )
    )
}

