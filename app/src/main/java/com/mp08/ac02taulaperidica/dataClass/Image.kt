package com.mp08.ac02taulaperidica.dataClass

import com.google.gson.annotations.SerializedName


data class Image (

  @SerializedName("title"       ) var title       : String? = null,
  @SerializedName("url"         ) var url         : String? = null,
  @SerializedName("attribution" ) var attribution : String? = null

)