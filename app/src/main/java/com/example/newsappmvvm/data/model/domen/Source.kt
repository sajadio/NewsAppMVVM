package com.example.newsappmvvm.data.model.domen

import com.google.gson.annotations.SerializedName

data class Source(
    @SerializedName("id")
    val id: Any? = null,
    @SerializedName("name")
    val name: String? = null
)
