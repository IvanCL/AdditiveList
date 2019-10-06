package com.icl.additivelist.models

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

data class Additive(@SerializedName("id") val id :String,@SerializedName("numb") val numb: String,@SerializedName("name") val name: String,@SerializedName("description") val description : String,@SerializedName("func") val func : String,@SerializedName("sideEffects") val sideEffects : String,@SerializedName("origin") val origin :String)