package com.jigar.me.data.repositories

import androidx.lifecycle.LiveData
import com.jigar.me.data.api.SafeApiCall
import com.jigar.me.data.local.db.inapp.purchase.InAppPurchaseDB
import com.jigar.me.data.local.db.inapp.sku.InAppSKUDB
import com.jigar.me.data.model.dbtable.inapp.InAppPurchaseDetails
import com.jigar.me.data.model.dbtable.inapp.InAppSkuDetails
import javax.inject.Inject

class DBRepository @Inject constructor(
    private val inAppPurchaseDB: InAppPurchaseDB,
    private val inAppSKUDB: InAppSKUDB
) : SafeApiCall {

    fun getInAppPurchase() : LiveData<List<InAppPurchaseDetails>> {
        return inAppPurchaseDB.getInAppPurchase()
    }

    fun getInAppSKUDetailLive(sku : String): LiveData<List<InAppSkuDetails>>{
        return inAppSKUDB.getInAppSKUDetailLive(sku)
    }

    fun getInAppSKU(): LiveData<List<InAppSkuDetails>>{
        return inAppSKUDB.getInAppSKU()
    }
}