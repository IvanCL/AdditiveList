package com.icl.additivelist.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(val id:String, val name:String, val province:Province): Parcelable
{
    override fun toString(): String = id + "|"+ name + "|" + province
}