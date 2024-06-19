package com.example.aquaspot.screens.filters

import com.example.aquaspot.model.Beach

fun searchBeachesByDescription(
    beaches: MutableList<Beach>,
    query: String
):List<Beach>{
    val regex = query.split(" ").joinToString(".*"){
        Regex.escape(it)
    }.toRegex(RegexOption.IGNORE_CASE)
    return beaches.filter { beach ->
        regex.containsMatchIn(beach.description)
    }
}
