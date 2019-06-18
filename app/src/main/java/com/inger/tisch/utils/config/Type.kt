package com.inger.tisch.utils.config

import android.support.annotation.IdRes
import com.inger.tisch.R

enum class Type(@IdRes val  icon : Int, val value : String) {

    VE(R.mipmap.vegetable, "素食"),
    ST(R.mipmap.staple_food, "主食"),
    FF(R.mipmap.fastfood,"快餐"),
    MEAL(R.mipmap.meal, "肉类"),
    DH(R.mipmap.doughnut, "甜品"),
    DK(R.mipmap.drink, "饮品"),
    HP(R.mipmap.hotpot, "火锅"),
    RT(R.mipmap.restaurant, "饭店");

    companion object {
        @JvmStatic
        fun toList() : List<Type>{
            val list = ArrayList<Type>()
            Type.values().forEach {
                list += it
            }
            return list
        }

        @JvmStatic
        fun toValueList() : List<String>{
            val list = ArrayList<String>()
            Type.values().forEach {
                list += it.value
            }
            return list
        }
    }
}