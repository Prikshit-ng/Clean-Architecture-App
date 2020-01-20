package com.prikshit.local.mapper

import com.prikshit.data.model.DeliveryData
import com.prikshit.data.model.RouteData
import com.prikshit.data.model.SenderData
import com.prikshit.local.model.DeliveryLocal
import javax.inject.Inject

class DeliveryDataMapper @Inject constructor() : Mapper<DeliveryData, DeliveryLocal> {

    override fun from(e: DeliveryLocal): DeliveryData {
        return DeliveryData(
            e.id, e.deliveryFee, e.goodsPicture, e.pickupTime, e.remarks,
            RouteData(e.routeEnd, e.routeStart),
            SenderData(e.senderEmail, e.senderName, e.senderPhone), e.surcharge, e.isFav
        )
    }

    override fun to(t: DeliveryData): DeliveryLocal {
        return DeliveryLocal(
            t.id, t.deliveryFee, t.goodsPicture, t.pickupTime, t.remarks,
            t.routeData.end, t.routeData.start,
            t.senderData.email, t.senderData.name, t.senderData.phone, t.surcharge, t.isFav
        )
    }
}