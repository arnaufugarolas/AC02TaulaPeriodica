package com.mp08.ac02taulaperidica.dataClass

import com.google.gson.annotations.SerializedName


data class Element(
  @SerializedName("name") var name: String? = null,
  @SerializedName("appearance") var appearance: String? = null,
  @SerializedName("atomic_mass") var atomicMass: Double? = null,
  @SerializedName("boil") var boil: Double? = null,
  @SerializedName("category") var category: String? = null,
  @SerializedName("density") var density: Double? = null,
  @SerializedName("discovered_by") var discoveredBy: String? = null,
  @SerializedName("melt") var melt: Double? = null,
  @SerializedName("molar_heat") var molarHeat: Double? = null,
  @SerializedName("named_by") var namedBy: String? = null,
  @SerializedName("number") var number: Int? = null,
  @SerializedName("period") var period: Int? = null,
  @SerializedName("phase") var phase: String? = null,
  @SerializedName("source") var source: String? = null,
  @SerializedName("bohr_model_image") var bohrModelImage: String? = null,
  @SerializedName("bohr_model_3d") var bohrModel3d: String? = null,
  @SerializedName("spectral_img") var spectralImg: String? = null,
  @SerializedName("summary") var summary: String? = null,
  @SerializedName("symbol") var symbol: String? = null,
  @SerializedName("xpos") var xpos: Int? = null,
  @SerializedName("ypos") var ypos: Int? = null,
  @SerializedName("shells") var shells: ArrayList<Int> = arrayListOf(),
  @SerializedName("electron_configuration") var electronConfiguration: String? = null,
  @SerializedName("electron_configuration_semantic") var electronConfigurationSemantic: String? = null,
  @SerializedName("electron_affinity") var electronAffinity: Double? = null,
  @SerializedName("electronegativity_pauling") var electronegativityPauling: Double? = null,
  @SerializedName("ionization_energies") var ionizationEnergies: ArrayList<Double> = arrayListOf(),
  @SerializedName("cpk-hex") var cpkHex: String? = null,
  @SerializedName("image") var image: Image? = Image(),
  var favorite: Boolean = false

)