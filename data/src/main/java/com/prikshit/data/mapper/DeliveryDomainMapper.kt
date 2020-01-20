package com.prikshit.data.mapper

import com.prikshit.data.model.DeliveryData
import com.prikshit.data.model.RouteData
import com.prikshit.data.model.SenderData
import com.prikshit.domain.entities.DeliveryEntity
import com.prikshit.domain.entities.RouteEntity
import com.prikshit.domain.entities.SenderEntity
import javax.inject.Inject

class DeliveryDomainMapper @Inject constructor() : Mapper<DeliveryEntity, DeliveryData> {
    override fun from(e: DeliveryData): DeliveryEntity {
        return DeliveryEntity(
            e.id,
            e.deliveryFee,
            e.goodsPicture,
            e.pickupTime,
            e.remarks,
            RouteEntity(e.routeData.end, e.routeData.start),
            SenderEntity(e.senderData.email, e.senderData.name, e.senderData.phone),
            e.surcharge,
            e.isFav
        )
    }

    override fun to(t: DeliveryEntity): DeliveryData {
        return DeliveryData(
            t.id,
            t.deliveryFee,
            t.goodsPicture,
            t.pickupTime,
            t.remarks,
            RouteData(t.routeEntity.end, t.routeEntity.start),
            SenderData(t.senderEntity.email, t.senderEntity.name, t.senderEntity.phone),
            t.surcharge,
            t.isFav
        )
    }
}