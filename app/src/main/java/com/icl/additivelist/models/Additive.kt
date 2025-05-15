package com.icl.additivelist.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Additive(@SerializedName("id") val id :String,@SerializedName("numb") val numb: String,@SerializedName("name") val name: String,@SerializedName("description") val description : String,@SerializedName("uses") val func : String,@SerializedName("sideEffects") val sideEffects : String,@SerializedName("origin") val origin :String): Parcelable
{
    override fun toString(): String = id + "|"+ numb + "|" + name + "|" + description + "|" + func + "|" + sideEffects + "|" + origin
}
