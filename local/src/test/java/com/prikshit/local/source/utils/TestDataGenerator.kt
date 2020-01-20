package com.prikshit.local.source.utils

import com.prikshit.local.model.DeliveryLocal

object TestDataGenerator {
    fun generateDeliveryList(): List<DeliveryLocal> {
        val d1 = DeliveryLocal("1", "$2.20",
            "https://loremflickr.com/320/240/cat?lock=9953",
            "2014-10-06T10:45:38-08:00",
            "Minim veniam minim nisi ullamco consequat anim reprehenderit laboris aliquip voluptate sit.",
            "Noble Court","Noble Street",
           "hardingwelch@comdom.com","Harding Welch","+1 (899) 523-3905",
            "$1.23",false)
        val d2 = DeliveryLocal("3", "$12.20",
            "https://loremflickr.com/320/240/cat?lock=9953",
            "2014-11-06T10:45:38-08:00",
            "Minim veniam minim nisi ullamco consequat anim reprehenderit laboris aliquip voluptate sit.",
            "Noble Court","Noble Street",
            "hardingwelch@comdom.com","Harding Welch","+1 (899) 523-3905",
            "$3.23",false)
        val d3 = DeliveryLocal("sdfvdf343", "$42.20",
            "https://loremflickr.com/320/240/cat?lock=9953",
            "2015-11-06T10:45:38-08:00",
            "Minim veniam minim nisi ullamco consequat anim reprehenderit laboris aliquip voluptate sit.",
            "Noble Court","Noble Street",
            "hardingwelch@comdom.com","Harding Welch","+1 (899) 523-3905",
            "$34.23",false)
        return listOf(d1, d2, d3)
    }
}