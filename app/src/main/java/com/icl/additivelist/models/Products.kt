package com.icl.additivelist.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Products(val id : String, val name: String, val category: Category, val company: Company, val price: Double): Parcelable
{
    override fun toString(): String = id + "|"+ name + "|" + category + "|" + company + "|" + price
}