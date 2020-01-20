package com.prikshit.domain.util

import com.prikshit.domain.entities.DeliveryEntity
import com.prikshit.domain.entities.RouteEntity
import com.prikshit.domain.entities.SenderEntity

object TestDataGenerator {
    fun generateDeliveryList(): List<DeliveryEntity> {
        val d1 = DeliveryEntity("1", "$2.20",
            "https://loremflickr.com/320/240/cat?lock=9953",
            "2014-10-06T10:45:38-08:00",
            "Minim veniam minim nisi ullamco consequat anim reprehenderit laboris aliquip voluptate sit.",
            RouteEntity("Noble Court","Noble Street"),
            SenderEntity("hardingwelch@comdom.com","Harding Welch","+1 (899) 523-3905"),
            "$1.23",true)
        val d2 = DeliveryEntity("3", "$12.20",
            "https://loremflickr.com/320/240/cat?lock=9953",
            "2014-11-06T10:45:38-08:00",
            "Minim veniam minim nisi ullamco consequat anim reprehenderit laboris aliquip voluptate sit.",
            RouteEntity("Noble Court","Noble Street"),
            SenderEntity("hardingwelch@comdom.com","Harding Welch","+1 (899) 523-3905"),
            "$3.23",true)
        val d3 = DeliveryEntity("sdfvdf343", "$42.20",
            "https://loremflickr.com/320/240/cat?lock=9953",
            "2015-11-06T10:45:38-08:00",
            "Minim veniam minim nisi ullamco consequat anim reprehenderit laboris aliquip voluptate sit.",
            RouteEntity("Noble Court","Noble Street"),
            SenderEntity("hardingwelch@comdom.com","Harding Welch","+1 (899) 523-3905"),
            "$34.23",true)
        return listOf(d1, d2, d3)
    }


}