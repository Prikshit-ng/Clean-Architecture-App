package com.prikshit.remote.mapper

import com.prikshit.data.model.DeliveryData
import com.prikshit.data.model.RouteData
import com.prikshit.data.model.SenderData
import com.prikshit.remote.model.DeliveryNetwork
import com.prikshit.remote.model.RouteNetwork
import com.prikshit.remote.model.SenderNetwork
import javax.inject.Inject

class DeliveryNetworkMapper @Inject constructor() : Mapper<DeliveryData, DeliveryNetwork> {
    override fun from(e: DeliveryNetwork): DeliveryData {
        return DeliveryData(
            e.id,
            e.deliveryFee,
            e.goodsPicture,
            e.pickupTime,
            e.remarks,
            RouteData(e.routeNetwork.end, e.routeNetwork.start),
            SenderData(e.senderNetwork.email, e.senderNetwork.name, e.senderNetwork.phone),
            e.surcharge,
            false
        )
    }

    override fun to(t: DeliveryData): DeliveryNetwork {
        return DeliveryNetwork(
            t.id,
            t.deliveryFee,
            t.goodsPicture,
            t.pickupTime,
            t.remarks,
            RouteNetwork(t.routeData.end, t.routeData.start),
            SenderNetwork(t.senderData.email, t.senderData.name, t.senderData.phone),
            t.surcharge
        )
    }
}