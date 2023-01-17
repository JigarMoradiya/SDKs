package com.jigar.me.data.local.db.inapp.purchase

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.billingclient.api.Purchase
import com.jigar.me.data.model.dbtable.inapp.InAppPurchaseDetails

@Dao
interface InAppPurchaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(inAppPurchaseDetails: InAppPurchaseDetails)

    @Transaction
    fun insert(vararg purchases: Purchase) {
        purchases.map {
            if (it.products.isNotEmpty()){
                insertData(InAppPurchaseDetails(it.orderId,it.products.first(),it.developerPayload,it.purchaseToken,
                    it.purchaseTime,it.purchaseState,it.isAcknowledged,it.signature,it.originalJson))
            }
        }
    }

    @Query("SELECT * FROM tableInAppPurchase WHERE purchaseState = 1")
    fun getPurchases(): LiveData<List<InAppPurchaseDetails>>

}