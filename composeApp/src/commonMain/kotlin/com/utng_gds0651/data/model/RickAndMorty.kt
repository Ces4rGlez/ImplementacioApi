package com.utng_gds0651.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterResponse(
    @SerialName("info") val info: Info,
    @SerialName("results") val results: List<Character>
)

@Serializable
data class Info(
    @SerialName("count") val count: Int,
    @SerialName("pages") val pages: Int,
    @SerialName("next") val next: String?,
    @SerialName("prev") val prev: String?
)

@Serializable
data class Character(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("status") val status: String,
    @SerialName("species") val species: String,
    @SerialName("type") val type: String,
    @SerialName("gender") val gender: String,
    @SerialName("origin") val origin: Location,
    @SerialName("location") val location: Location,
    @SerialName("image") val image: String,
    @SerialName("episode") val episode: List<String>,
    @SerialName("url") val url: String,
    @SerialName("created") val created: String
)

@Serializable
data class Location(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
)

@Serializable
data class Episode(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("air_date") val airDate: String,
    @SerialName("episode") val episode: String,
    @SerialName("characters") val characters: List<String>,
    @SerialName("url") val url: String,
    @SerialName("created") val created: String
)
