package com.prikshit.local.database

import androidx.paging.DataSource
import androidx.room.*
import com.prikshit.local.model.DeliveryLocal
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface DeliveryDao {

    @Query("SELECT * FROM delivery where id = :deliveryId")
    fun getDeliveryById(deliveryId: String): Observable<DeliveryLocal>

    @Query("SELECT * FROM delivery")
    fun getDeliveries(): DataSource.Factory<Int, DeliveryLocal>

    @Query("SELECT count(*) FROM delivery")
    fun getDeliveryCount(): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveDeliveries(deliveries: List<DeliveryLocal>)

    @Update
    fun updateDelivery(delivery: DeliveryLocal): Completable

    @Query("DELETE FROM delivery")
    fun clear(): Completable
}