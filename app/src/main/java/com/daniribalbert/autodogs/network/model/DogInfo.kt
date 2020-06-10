package com.daniribalbert.autodogs.network.model

data class DogInfo(val name: String,
                   val imageUrl: String,
                   val temperament: String,
                   val origin: String
) {
    constructor(imageUrl: String): this("", imageUrl, "", "")
}