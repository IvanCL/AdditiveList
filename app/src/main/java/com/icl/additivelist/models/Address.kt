package com.icl.additivelist.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(val id : String,val type: String, val name: String, val cp: String, val numb:String, val province: Province, val city: City, val country: Country): Parcelable
{
    override fun toString(): String = id + "|"+ type + "|" + name + "|" + cp + "|" + numb + "|" + province  + "|" + city  + "|" + country
}