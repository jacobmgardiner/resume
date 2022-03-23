package com.yoloapps.backyardmarket.utils.sort

import com.yoloapps.backyardmarket.data.classes.Product

class SortByPrice() : Comparator<Product>{
    override fun compare(o1: Product?, o2: Product?): Int {
        return (o1!!.price!! - o2!!.price!!).toInt();
    }
}