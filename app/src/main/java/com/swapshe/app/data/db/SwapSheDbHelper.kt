package com.swapshe.app.data.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.swapshe.app.model.ConditionType
import com.swapshe.app.model.Product
import com.swapshe.app.model.ProductMeasurements
import com.swapshe.app.model.ProductStatus
import com.swapshe.app.model.Seller
import com.swapshe.app.model.SwapSheGrade

class SwapSheDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "swapshe_closet.db"
        const val DATABASE_VERSION = 1

        const val TABLE_CLOSET = "user_closet"
        const val COLUMN_ID = "id"
        const val COLUMN_OWNER_ID = "owner_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_BRAND = "brand"
        const val COLUMN_PRICE = "price"
        const val COLUMN_ORIGINAL_PRICE = "original_price"
        const val COLUMN_CONDITION = "condition"
        const val COLUMN_GRADE = "grade"
        const val COLUMN_SIZE = "size"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_STYLE = "style"
        const val COLUMN_COLOR = "color"
        const val COLUMN_IMAGES = "images" // Delimited by ||
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_FABRIC_STRETCH = "fabric_stretch"
        const val COLUMN_CAN_SWAP = "can_swap"
        const val COLUMN_ALLOW_BUY = "allow_buy"
        const val COLUMN_STATUS = "status"
        const val COLUMN_SELLER_ID = "seller_id"
        const val COLUMN_SELLER_NAME = "seller_name"
        const val COLUMN_SELLER_AVATAR = "seller_avatar"
        const val COLUMN_MEASURE_BUST = "measure_bust"
        const val COLUMN_MEASURE_WAIST = "measure_waist"
        const val COLUMN_MEASURE_HIPS = "measure_hips"
        const val COLUMN_MEASURE_LENGTH = "measure_length"
        const val COLUMN_MEASURE_SHOULDER = "measure_shoulder"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_CLOSET (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_OWNER_ID TEXT NOT NULL,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_BRAND TEXT,
                $COLUMN_PRICE INTEGER NOT NULL,
                $COLUMN_ORIGINAL_PRICE INTEGER,
                $COLUMN_CONDITION TEXT,
                $COLUMN_GRADE TEXT,
                $COLUMN_SIZE TEXT,
                $COLUMN_CATEGORY TEXT,
                $COLUMN_STYLE TEXT,
                $COLUMN_COLOR TEXT,
                $COLUMN_IMAGES TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_FABRIC_STRETCH TEXT,
                $COLUMN_CAN_SWAP INTEGER,
                $COLUMN_ALLOW_BUY INTEGER,
                $COLUMN_STATUS TEXT,
                $COLUMN_SELLER_ID TEXT,
                $COLUMN_SELLER_NAME TEXT,
                $COLUMN_SELLER_AVATAR TEXT,
                $COLUMN_MEASURE_BUST INTEGER,
                $COLUMN_MEASURE_WAIST INTEGER,
                $COLUMN_MEASURE_HIPS INTEGER,
                $COLUMN_MEASURE_LENGTH INTEGER,
                $COLUMN_MEASURE_SHOULDER INTEGER
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CLOSET")
        onCreate(db)
    }

    // --- CRUD OPERATIONS ---

    // 1. CREATE / INSERT
    fun insertProduct(product: Product): Long {
        val db = writableDatabase
        val values = productToContentValues(product)
        return db.insertWithOnConflict(TABLE_CLOSET, null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    // 2. READ ALL BY OWNER
    fun getProductsByOwner(ownerId: String): List<Product> {
        val db = readableDatabase
        val productList = mutableListOf<Product>()
        val cursor: Cursor = db.query(
            TABLE_CLOSET,
            null,
            "$COLUMN_OWNER_ID = ?",
            arrayOf(ownerId),
            null,
            null,
            "$COLUMN_PRICE DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                productList.add(cursorToProduct(it))
            }
        }
        return productList
    }

    // 2b. READ SINGLE BY ID
    fun getProductById(id: String): Product? {
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_CLOSET,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id),
            null,
            null,
            null
        )

        cursor.use {
            if (it.moveToFirst()) {
                return cursorToProduct(it)
            }
        }
        return null
    }

    // 3. UPDATE
    fun updateProduct(product: Product): Int {
        val db = writableDatabase
        val values = productToContentValues(product)
        return db.update(TABLE_CLOSET, values, "$COLUMN_ID = ?", arrayOf(product.id))
    }

    // 4. DELETE
    fun deleteProduct(id: String): Int {
        val db = writableDatabase
        return db.delete(TABLE_CLOSET, "$COLUMN_ID = ?", arrayOf(id))
    }

    // Check count
    fun getProductCount(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_CLOSET", null)
        var count = 0
        cursor.use {
            if (it.moveToFirst()) {
                count = it.getInt(0)
            }
        }
        return count
    }

    private fun productToContentValues(product: Product): ContentValues {
        return ContentValues().apply {
            put(COLUMN_ID, product.id)
            put(COLUMN_OWNER_ID, product.ownerId)
            put(COLUMN_TITLE, product.title)
            put(COLUMN_BRAND, product.brand)
            put(COLUMN_PRICE, product.price)
            put(COLUMN_ORIGINAL_PRICE, product.originalPrice)
            put(COLUMN_CONDITION, product.condition.name)
            put(COLUMN_GRADE, product.grade.name)
            put(COLUMN_SIZE, product.size)
            put(COLUMN_CATEGORY, product.category)
            put(COLUMN_STYLE, product.style)
            put(COLUMN_COLOR, product.color)
            put(COLUMN_IMAGES, product.images.joinToString("||"))
            put(COLUMN_DESCRIPTION, product.description)
            put(COLUMN_FABRIC_STRETCH, product.fabricStretch)
            put(COLUMN_CAN_SWAP, if (product.canSwap) 1 else 0)
            put(COLUMN_ALLOW_BUY, if (product.allowBuy) 1 else 0)
            put(COLUMN_STATUS, product.status.name)
            put(COLUMN_SELLER_ID, product.seller.id)
            put(COLUMN_SELLER_NAME, product.seller.name)
            put(COLUMN_SELLER_AVATAR, product.seller.avatarUrl)
            put(COLUMN_MEASURE_BUST, product.measurements.bust)
            put(COLUMN_MEASURE_WAIST, product.measurements.waist)
            put(COLUMN_MEASURE_HIPS, product.measurements.hips)
            put(COLUMN_MEASURE_LENGTH, product.measurements.length)
            put(COLUMN_MEASURE_SHOULDER, product.measurements.shoulder)
        }
    }

    private fun cursorToProduct(cursor: Cursor): Product {
        val id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val ownerId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OWNER_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val brand = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRAND))
        val price = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_PRICE))
        val originalPrice = if (cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_ORIGINAL_PRICE))) null
        else cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ORIGINAL_PRICE))

        val conditionName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONDITION))
        val condition = try { ConditionType.valueOf(conditionName) } catch (_: Exception) { ConditionType.GOOD }

        val gradeName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GRADE))
        val grade = try { SwapSheGrade.valueOf(gradeName) } catch (_: Exception) { SwapSheGrade.GRADE_S }

        val size = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SIZE))
        val category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY))
        val style = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STYLE))
        val color = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COLOR))
        val imagesStr = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGES))
        val images = if (imagesStr.isNullOrBlank()) emptyList() else imagesStr.split("||")

        val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
        val fabricStretch = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FABRIC_STRETCH))
        val canSwap = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CAN_SWAP)) == 1
        val allowBuy = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ALLOW_BUY)) == 1

        val statusName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS))
        val status = try { ProductStatus.valueOf(statusName) } catch (_: Exception) { ProductStatus.AVAILABLE }

        val sellerId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SELLER_ID))
        val sellerName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SELLER_NAME))
        val sellerAvatar = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SELLER_AVATAR))

        val bust = if (cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_MEASURE_BUST))) null else cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MEASURE_BUST))
        val waist = if (cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_MEASURE_WAIST))) null else cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MEASURE_WAIST))
        val hips = if (cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_MEASURE_HIPS))) null else cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MEASURE_HIPS))
        val length = if (cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_MEASURE_LENGTH))) null else cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MEASURE_LENGTH))
        val shoulder = if (cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_MEASURE_SHOULDER))) null else cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MEASURE_SHOULDER))

        return Product(
            id = id,
            title = title,
            brand = brand ?: "Unknown",
            price = price,
            originalPrice = originalPrice,
            condition = condition,
            grade = grade,
            size = size ?: "Free",
            category = category ?: "All",
            style = style ?: "Casual",
            color = color ?: "N/A",
            images = images,
            description = description ?: "",
            fabricStretch = fabricStretch ?: "Co giãn nhẹ",
            measurements = ProductMeasurements(
                bust = bust,
                length = length,
                waist = waist,
                hips = hips,
                shoulder = shoulder
            ),
            seller = Seller(
                id = sellerId,
                name = sellerName ?: "SwapShe Member",
                shopName = "$sellerName's Closet",
                avatarUrl = sellerAvatar ?: "",
                rating = 4.9f,
                reviewCount = 12,
                successfulTransactions = 28,
                memberSince = "2024",
                location = "TP. Hồ Chí Minh"
            ),
            canSwap = canSwap,
            allowBuy = allowBuy,
            status = status,
            ownerId = ownerId
        )
    }
}
