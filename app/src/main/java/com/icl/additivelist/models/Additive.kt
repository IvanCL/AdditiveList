package com.icl.additivelist.models


import com.google.gson.annotations.SerializedName


data class Additive(@SerializedName("id") val id :String,@SerializedName("numb") val numb: String,@SerializedName("name") val name: String,@SerializedName("description") val description : String,@SerializedName("uses") val func : String,@SerializedName("sideEffects") val sideEffects : String,@SerializedName("origin") val origin :String)
{
    override fun toString(): String = id + "|"+ numb + "|" + name + "|" + description + "|" + func + "|" + sideEffects + "|" + origin
}
