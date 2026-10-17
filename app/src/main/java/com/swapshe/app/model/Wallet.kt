package com.swapshe.app.model

enum class WalletTransactionType(val label: String) {
    ESCROW_HOLD("Ký quỹ giữ tiền đơn hàng"),
    RELEASE_EARNING("Doanh thu giải ngân"),
    PLATFORM_FEE("Phí dịch vụ sàn SwapShe (5%)"),
    WITHDRAW_BANK("Rút tiền về ngân hàng"),
    DISPUTE_REFUND("Hoàn tiền khiếu nại"),
    SWAP_DEPOSIT("Đặt cọc bảo đảm đổi đồ 1-1")
}

data class WalletTransaction(
    val id: String,
    val type: WalletTransactionType,
    val amount: Long,
    val title: String,
    val description: String,
    val timestamp: String,
    val isPositive: Boolean,
    val orderCode: String? = null
)

data class BankPayoutAccount(
    val bankName: String,
    val accountNumber: String,
    val accountHolderName: String
)

data class Wallet(
    val availableBalance: Long = 1850000L,
    val escrowHoldBalance: Long = 420000L,
    val pendingWithdrawal: Long = 0L,
    val bankAccount: BankPayoutAccount? = BankPayoutAccount(
        bankName = "Vietcombank",
        accountNumber = "9988112233",
        accountHolderName = "NGUYEN HOANG MY"
    ),
    val transactions: List<WalletTransaction> = emptyList()
) {
    val escrowHoldingBalance: Long get() = escrowHoldBalance
}
