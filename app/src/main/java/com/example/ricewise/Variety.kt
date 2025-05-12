package com.example.ricewise

class Variety (
    val _id: String? = null,
    val name: String,
    val code: String,
    val type: String? = null,
    val maturity_days: Int? = null,
    val grain_length: String? = null,
    val grain_shape: String? = null,
    val amylose_content: String? = null,
    val yield_per_hectare: Int? = null,
    val resistance: List<String> = emptyList(),
    val recommended_area: String? = null,
    val released_year: Int? = null,
    val description: String? = null
)
