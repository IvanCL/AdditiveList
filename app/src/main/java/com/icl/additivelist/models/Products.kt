package com.icl.additivelist.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Products(val id : String, val name: String, val description: String, val uses: String, val origin: String): Parcelable
{
    override fun toString(): String = id + "|"+ name + "|" + description + "|" + uses + "|" + origin
}