package com.grumpy.pestcontrol.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Job(
    val name : String?=null,
    val phone : String?=null,
    val street : String?=null,
    val city : String?=null,
    val state : String?=null,
    val zipCode: String?=null,
    val date : String?=null,
    val time : String?=null,
    val price: String?=null,
    val isCompleted : Boolean?=null
) : Parcelable