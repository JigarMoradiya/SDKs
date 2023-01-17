package com.jigar.me.data.model.dbtable.inapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tableInAppSKU")
data class InAppSkuDetails(
    @PrimaryKey val sku: String,
    val type: String?,
    val price: String?,
    val price_amount_micros: Long?,
    val price_currency_code: String?,
    val title: String?,
    val description: String?,
    val originalJson: String?,
    val isPurchase: Boolean = false, /* Not in SkuDetails; it's the augmentation */
    val orderId: String = "", /* Not in SkuDetails; it's the augmentation */
    val purchaseTime: Long = 0L /* Not in SkuDetails; it's the augmentation */
)