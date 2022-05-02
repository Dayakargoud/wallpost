package com.dayakar.wallpost.model

data class PexelAPIResponse(
    val next_page: String,
    val page: Int,
    val per_page: Int,
    val photos: List<Photo>,
    val total_results: Int
)