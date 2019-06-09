package com.andoverrobotics.inventory.cli

import com.andoverrobotics.inventory.query.SearchQuery
import com.andoverrobotics.inventory.security.GoogleAccountVerifier

fun main() {
    val sq = SearchQuery("axles")
    GoogleAccountVerifier.verifyIdToken("hello world")
}