package com.andoverrobotics.inventory.cli

import com.andoverrobotics.inventory.PartType
import java.net.URL
import java.util.stream.Collectors

object Reader {
    fun readPartType(): PartType = PartType(
            prompt("Name"),
            prompt("Part Number"),
            prompt("Brand"),
            prompt("Category"),
            prompt("Location"),
            prompt("Team"),
            prompt("Quantity").toInt(),
            URL(prompt("URL")),
            URL(prompt("Photo URL")),
            *prompt("Keywords, separated by spaces").split(" ").toTypedArray()
    )

    fun selectPart(): PartType {
        val parts = Runner.foundation.allParts().collect(Collectors.toList())

        if (parts.isEmpty()) {
            println("There are no parts, but you were going to select one...")
            throw IllegalStateException("Missing parts")
        }

        println("Parts:")
        for ((index, part) in parts.withIndex())
            println("${index + 1}: ${part.name}")

        while (true) {
            print("Pick one >>")
            val input = readLine()?.toIntOrNull()

            if (input == null || input < 1 || input > parts.size) {
                println("Ouch. Stop it")
            } else {
                return parts[input - 1]
            }
        }
    }

    fun promptRequireNonEmpty(msg: String): String {
        while (true) {
            val query = prompt(msg)

            if (query.isEmpty())
                System.err.println("Say something I'm giving up on you")
            else
                return query
        }
    }

    fun prompt(msg: String): String {
        print("$msg >>")
        return readLine()!!
    }
}