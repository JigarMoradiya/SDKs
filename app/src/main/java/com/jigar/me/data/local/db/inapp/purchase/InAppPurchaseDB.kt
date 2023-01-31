package com.jigar.me.data.local.db.inapp.purchase

import androidx.lifecycle.LiveData
import com.android.billingclient.api.Purchase
import com.jigar.me.data.model.dbtable.inapp.InAppPurchaseDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InAppPurchaseDB @Inject constructor(private val dao: InAppPurchaseDao) {
    suspend fun saveInAppPurchase(vararg data: Purchase) = withContext(Dispatchers.IO) {
        dao.insert(*data)
    }

    fun getInAppPurchase(): LiveData<List<InAppPurchaseDetails>> {
        return dao.getPurchases()
    }

}