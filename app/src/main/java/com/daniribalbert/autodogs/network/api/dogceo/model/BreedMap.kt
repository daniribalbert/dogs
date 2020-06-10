package com.daniribalbert.autodogs.network.api.dogceo.model

class BreedMap: HashMap<String, List<String>>() {

    fun getAllBreeds(): List<String> {
        val breedList = mutableListOf<String>()
        keys.forEach { breedName ->
            val breed = get(breedName)
            if (breed?.isEmpty() == true) breedList.add(breedName)
            if (breed?.isEmpty() == false) {
                breed.forEach { type ->
                    breedList.add("$breedName $type")
                }
            }
        }
        return breedList
    }
}