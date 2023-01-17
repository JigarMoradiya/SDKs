package com.jigar.me.data.local.db.inapp.sku

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.ProductDetails
import com.google.gson.Gson
import com.jigar.me.data.model.dbtable.inapp.InAppSkuDetails


@Dao
interface InAppSKUDao {
    @Query("SELECT SKU.*,CASE WHEN (P.orderId IS NULL) THEN 0 ELSE 1 END as isPurchase,CASE WHEN (P.orderId IS NULL) THEN '' ELSE P.orderId END as orderId,P.purchaseTime FROM tableInAppSKU as SKU LEFT JOIN tableInAppPurchase as P ON (SKU.sku = P.sku AND P.purchaseState = 1) WHERE SKU.type = '${BillingClient.ProductType.INAPP}'  ORDER BY SKU.price_amount_micros DESC")
    fun getInAppSku(): LiveData<List<InAppSkuDetails>>

    @Query("SELECT SKU.*,CASE WHEN (P.orderId IS NULL) THEN 0 ELSE 1 END as isPurchase,CASE WHEN (P.orderId IS NULL) THEN '' ELSE P.orderId END as orderId,P.purchaseTime FROM tableInAppSKU as SKU LEFT JOIN tableInAppPurchase as P ON (SKU.sku = P.sku AND P.purchaseState = 1) WHERE SKU.sku = :sku AND SKU.type = '${BillingClient.ProductType.INAPP}' ORDER BY SKU.price_amount_micros DESC")
    fun getInAppSkuDetail(sku: String): List<InAppSkuDetails>

    @Query("SELECT SKU.*,CASE WHEN (P.orderId IS NULL) THEN 0 ELSE 1 END as isPurchase,CASE WHEN (P.orderId IS NULL) THEN '' ELSE P.orderId END as orderId,P.purchaseTime FROM tableInAppSKU as SKU LEFT JOIN tableInAppPurchase as P ON (SKU.sku = P.sku AND P.purchaseState = 1) WHERE SKU.sku = :sku AND SKU.type = '${BillingClient.ProductType.INAPP}' ORDER BY SKU.price_amount_micros DESC")
    fun getInAppSkuDetailLive(sku: String): LiveData<List<InAppSkuDetails>>

    @Transaction
    fun insertOrUpdate(skuDetails: MutableList<ProductDetails>) = skuDetails.apply {
        skuDetails.map {
            val originalJson = Gson().toJson(it)
            val detail = InAppSkuDetails(it.productId, it.productType, it.oneTimePurchaseOfferDetails?.formattedPrice,
                it.oneTimePurchaseOfferDetails?.priceAmountMicros, it.oneTimePurchaseOfferDetails?.priceCurrencyCode, it.title, it.description, originalJson)
            insert(detail)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(inAppSkuDetails: InAppSkuDetails)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(inAppSkuDetails: List<InAppSkuDetails>)
}