package com.icl.additivelist.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Province(val id:String, val name:String, val country: Country):Parcelable
{
    override fun toString(): String = id + "|"+ name + "|" + country
}