package com.prikshit.remote.source.utils

import com.prikshit.remote.model.DeliveryNetwork
import com.prikshit.remote.model.RouteNetwork
import com.prikshit.remote.model.SenderNetwork


object TestDataGenerator {
    fun generateDeliveryList(): List<DeliveryNetwork> {
        var d1 = DeliveryNetwork(
            "1", "$2.20",
            "https://loremflickr.com/320/240/cat?lock=9953",
            "2014-10-06T10:45:38-08:00",
            "Minim veniam minim nisi ullamco consequat anim reprehenderit laboris aliquip voluptate sit.",
            RouteNetwork("Noble Court", "Noble Street"),
            SenderNetwork("hardingwelch@comdom.com", "Harding Welch", "+1 (899) 523-3905"),
            "$1.23"
        )
        var d2 = DeliveryNetwork(
            "3", "$12.20",
            "https://loremflickr.com/320/240/cat?lock=9953",
            "2014-11-06T10:45:38-08:00",
            "Minim veniam minim nisi ullamco consequat anim reprehenderit laboris aliquip voluptate sit.",
            RouteNetwork("Noble Court", "Noble Street"),
            SenderNetwork("hardingwelch@comdom.com", "Harding Welch", "+1 (899) 523-3905"),
            "$3.23"
        )
        var d3 = DeliveryNetwork(
            "sdfvdf343", "$42.20",
            "https://loremflickr.com/320/240/cat?lock=9953",
            "2015-11-06T10:45:38-08:00",
            "Minim veniam minim nisi ullamco consequat anim reprehenderit laboris aliquip voluptate sit.",
            RouteNetwork("Noble Court", "Noble Street"),
            SenderNetwork("hardingwelch@comdom.com", "Harding Welch", "+1 (899) 523-3905"),
            "$34.23"
        )
        return listOf(d1, d2, d3)
    }
}