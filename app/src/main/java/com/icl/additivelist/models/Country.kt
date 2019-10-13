package com.icl.additivelist.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Country(val id: String, val name: String): Parcelable
{
    override fun toString(): String = id + "|"+ name
}