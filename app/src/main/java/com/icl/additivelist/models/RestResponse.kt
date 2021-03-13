package com.icl.additivelist.models

import com.google.gson.annotations.SerializedName

data class RestResponse<TypeToken>(@SerializedName("data") val data : TypeToken)
{
    override fun toString(): String = data.toString()

}