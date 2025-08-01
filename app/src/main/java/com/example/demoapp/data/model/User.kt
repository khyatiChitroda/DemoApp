package com.example.demoapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int = -1,
    val firstName: String,
    val lastName: String = "",
    val maidenName: String = "",
    val age: Int = 25,
    val gender: String = "",
    val email: String = "",
    val phone: String = "",
    val university: String ="",
    val image: String = ""
) : Parcelable