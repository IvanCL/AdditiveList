package com.icl.additivelist.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Company(val id : String, val name: String, val address: Address): Parcelable
{
    override fun toString(): String = id + "|"+ name + "|" + address
}