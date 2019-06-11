package com.andoverrobotics.inventory.cli

import com.andoverrobotics.inventory.Foundation
import com.andoverrobotics.inventory.FoundationGateway
import com.andoverrobotics.inventory.json.JsonPersistence
import com.andoverrobotics.inventory.security.AnonymousIdentity
import com.andoverrobotics.inventory.security.GoogleAccountVerifier
import com.andoverrobotics.inventory.security.Identity
import kotlin.system.exitProcess

object Runner {
    val google = GoogleAccountVerifier()
    val foundation = Foundation(JsonPersistence("persistence.json"), google)
    val identity = authenticate(foundation)

    fun authenticate(foundation: FoundationGateway): Identity {
        print("Enter ID Token or nothing >>")
        val input = readLine()!!

        if (input.isEmpty()) {
            println("You are Anonymous.")
            return AnonymousIdentity()
        }

        try {
            return foundation.identify(input)
        } catch (e: Exception) {
            println(e.message)
            exitProcess(1)
        }
    }
}

fun main() {
    Runner.identity
    runSubMenu("Main menu",
            ChangeSub(),
            SearchSub(),
            ReflectionSub(),
            AuditSub(),
            WhitelistSub())
}
