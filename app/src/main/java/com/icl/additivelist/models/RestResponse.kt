package com.icl.additivelist.models

import com.google.gson.annotations.SerializedName

data class RestResponse(@SerializedName("data") val data : Array<Additive>)
{
    override fun toString(): String = data.toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RestResponse

        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }
}