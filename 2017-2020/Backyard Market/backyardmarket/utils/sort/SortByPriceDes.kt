package com.yoloapps.backyardmarket.utils.sort

import com.yoloapps.backyardmarket.data.classes.Product

class SortByPriceDes() : Comparator<Product> {
    override fun compare(o1: Product?, o2: Product?): Int {
        return (o2!!.price!! - o1!!.price!!).toInt();
    }
}