package com.mp08.ac02taulaperidica.dataClass

import com.google.gson.annotations.SerializedName


data class PeriodicTable (

  @SerializedName("elements" ) var elements : MutableList<Element> = mutableListOf()

)