package com.prikshit.data.repository.utils

import com.prikshit.data.model.DeliveryData
import com.prikshit.data.model.RouteData
import com.prikshit.data.model.SenderData

object TestDataGenerator {
    fun generateDeliveryList(): List<DeliveryData> {
        val d1 = DeliveryData("1", "$2.20",
            "https://loremflickr.com/320/240/cat?lock=9953",
            "2014-10-06T10:45:38-08:00",
            "Minim veniam minim nisi ullamco consequat anim reprehenderit laboris aliquip voluptate sit.",
            RouteData("Noble Court","Noble Street"),
            SenderData("hardingwelch@comdom.com","Harding Welch","+1 (899) 523-3905"),
            "$1.23",true)
        val d2 = DeliveryData("3", "$12.20",
            "https://loremflickr.com/320/240/cat?lock=9953",
            "2014-11-06T10:45:38-08:00",
            "Minim veniam minim nisi ullamco consequat anim reprehenderit laboris aliquip voluptate sit.",
            RouteData("Noble Court","Noble Street"),
            SenderData("hardingwelch@comdom.com","Harding Welch","+1 (899) 523-3905"),
            "$3.23",true)
        val d3 = DeliveryData("sdfvdf343", "$42.20",
            "https://loremflickr.com/320/240/cat?lock=9953",
            "2015-11-06T10:45:38-08:00",
            "Minim veniam minim nisi ullamco consequat anim reprehenderit laboris aliquip voluptate sit.",
            RouteData("Noble Court","Noble Street"),
            SenderData("hardingwelch@comdom.com","Harding Welch","+1 (899) 523-3905"),
            "$34.23",true)
        return listOf(d1, d2, d3)
    }
}