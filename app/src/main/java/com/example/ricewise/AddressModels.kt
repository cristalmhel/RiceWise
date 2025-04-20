package com.example.ricewise

import com.google.gson.annotations.SerializedName

data class Region(
    @SerializedName("region_name")
    val regionName: String? = null,

    @SerializedName("province_list")
    val provinceList: Map<String, Province>
)

data class Province(
    val provinceName: String? = null,

    @SerializedName("municipality_list")
    val municipalityList: Map<String, Municipality>? = null
)

data class Municipality(
    val cityName: String? = null,

    @SerializedName("barangay_list")
    val barangayList: List<String>? = null
)
