package com.yoloapps.backyardmarket.utils.sort

import com.yoloapps.backyardmarket.data.classes.Product

class SortByTime() : Comparator<Product>{
    override fun compare(o1: Product?, o2: Product?): Int {
        return o1!!.time!!.compareTo(o2!!.time!!)
    }
}