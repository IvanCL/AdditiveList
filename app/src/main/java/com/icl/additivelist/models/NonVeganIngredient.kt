package com.icl.additivelist.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NonVeganIngredient(
    val term: String,
    val origin: String
) : Parcelable
