package com.andoverrobotics.inventory.cli

import com.andoverrobotics.inventory.PartType
import com.andoverrobotics.inventory.ScraperGateway
import com.andoverrobotics.inventory.mutations.Addition
import com.andoverrobotics.inventory.mutations.Consumption
import com.andoverrobotics.inventory.mutations.Deletion
import com.andoverrobotics.inventory.mutations.Mutation
import com.andoverrobotics.inventory.query.SearchQuery
import com.andoverrobotics.inventory.scraper.JsoupScraper
import com.andoverrobotics.inventory.security.Action
import com.andoverrobotics.inventory.security.AuditLogItem
import com.andoverrobotics.inventory.security.GoogleIdentity
import com.andoverrobotics.inventory.security.Identity
import java.net.URL
import java.time.LocalDateTime

interface Subsection : Runnable {
    val name: String
}

fun runSubMenu(title: String, vararg options: Subsection) {


    while (true) {
        println("---> $title <---")
        for ((index, opt) in options.withIndex()) {
            println("${index + 1}: ${opt.name}")
        }
        print("Pick one (or -1 to exit) >>")
        val input = readLine()?.toIntOrNull()

        if (input == -1) {
            break
        }

        if (input == null || input < 1 || input > options.size) {
            println("Ouch. Stop it")
        } else {
            try {
                options[input - 1].run()
            } catch (e: Exception) {

            }
        }
    }

}

class ChangeSub : Subsection {
    override val name = "Make a change"

    override fun run() {
        val myLevel = Runner.foundation.permissionLevelOf(Runner.identity)
        if (!myLevel.canPerform(Action.EDIT_INVENTORY)) {
            System.err.println("Access Prohibited: Your permission level ($myLevel) cannot edit the inventory")
            return
        }

        runSubMenu("Possible Changes",
                AdditionSub(),
                DeletionSub(),
                ConsumptionSub())
    }

    private class AdditionSub : Subsection {
        override val name = "Add a Part"
        override fun run() {
            val part = Reader.readPartType()
            val success = Runner.foundation.change(Runner.identity, Addition(part))

            println(if (success) "Success" else "Failure")
        }
    }

    private class DeletionSub : Subsection {
        override val name = "Remove a Part"
        override fun run() {
            val part = Reader.selectPart()

            val success = Runner.foundation.change(Runner.identity, Deletion(part.uuid))

            print(if (success) "Success" else "Failure")
        }
    }

    private class ConsumptionSub : Subsection {
        override val name = "Consume a Part"
        override fun run() {
            val part = Reader.selectPart()
            val amount = readAmount(part)

            val success = Runner.foundation.change(Runner.identity, Consumption(part.uuid, amount))

            print(if (success) "Success" else "Failure")
        }

        private fun readAmount(part: PartType): Int {
            while (true) {
                print("How many of ${part.name} >>")
                val input = readLine()!!.toIntOrNull()

                if (input == null || input < 1 || input > part.quantity) {
                    println("Sorry, not possible")
                } else {
                    return input
                }
            }
        }
    }
}

class SearchSub : Subsection {
    override val name = "Search for a Part"

    override fun run() {

        val query = Reader.promptRequireNonEmpty("Enter Query")
        var resultCount = 0

        Runner.foundation.filter(Runner.identity, SearchQuery(query)).forEach {
            resultCount++
            println(it.expressPart())
        }

        println("Found $resultCount item(s)")

    }
}

class BrowseSub : Subsection {
    override val name = "Show All Parts"

    override fun run() {
        Runner.foundation.allParts().forEach {
            println(it.expressPart())
        }
        println("-----")
    }
}

class ReflectionSub : Subsection {
    override val name = "Who Am I?"

    override fun run() {
        Runner.identity.run {
            when {
                this is GoogleIdentity -> println("I am $name ($email). My picture is at $pictureUrl")
                else -> println("I am *Anonymous*")
            }
            println("My permission level: ${Runner.foundation.permissionLevelOf(this)}")
        }
    }
}

class WhitelistSub : Subsection {
    override val name = "Manage Whitelist"

    override fun run() {
        val myLevel = Runner.foundation.permissionLevelOf(Runner.identity)
        if (!myLevel.canPerform(Action.MANAGE)) {
            System.err.println("Access Prohibited: Your permission level ($myLevel) cannot manage the whitelist")
            return
        }

        runSubMenu("Whitelist Menu",
                AddToWhitelist(),
                RemoveFromWhitelist(),
                ViewWhitelist())
    }

    class AddToWhitelist : Subsection {
        override val name = "Add new email to whitelist"

        override fun run() {
            val email = Reader.promptRequireNonEmpty("Enter new email")

            Runner.run {
                val result = foundation.addEmailToWhitelist(identity, email)

                println(if (result) {
                    "Success"
                } else {
                    "$email is already whitelisted"
                })
            }
        }
    }

    class RemoveFromWhitelist : Subsection {
        override val name = "Remove email from whitelist"

        override fun run() {
            val email = Reader.promptRequireNonEmpty("Enter email to remove")

            Runner.run {
                val result = foundation.removeEmailFromWhitelist(identity, email)

                println(if (result) {
                    "Success"
                } else {
                    "$email was not already whitelisted"
                })
            }
        }
    }

    class ViewWhitelist : Subsection {
        override val name = "View the whitelist"

        override fun run() {
            Runner.run {
                foundation.whitelist(identity).forEach(::println)
            }
        }
    }
}

class AuditSub : Subsection {
    override val name = "View audit log"

    override fun run() {
        Runner.run {
            foundation.auditLogSince(identity, LocalDateTime.of(2019, 6, 11, 18, 40, 0))
                    .forEach {
                        println(it.express())
                    }
        }
    }

    private fun AuditLogItem.express(): String {
        return "At $time, ${initiator.str} performed a(n) ${change.str}"
    }

    private val Identity.str: String
        get() {
            return if (this is GoogleIdentity)
                "$name ($email)"
            else
                "idk lol"
        }

    private val Mutation.str: String
        get() {
            return when {
                this is Addition -> "Addition of the following part:\n ${newPart.expressPart()}"
                this is Deletion -> "Deletion of the part with UUID $uuid"
                this is Consumption -> "Consumption of $amount of the part with UUID $uuid"
                else -> "idk lol"
            }
        }
}


class ScraperSub : Subsection {
    private val scraper = JsoupScraper()

    override val name = "Try the Scraper"

    override fun run() {
            try {

                val url = URL(Reader.promptRequireNonEmpty("Enter URL of Part"))
                val result = scraper.interpret(url)

                println(result?.express() ?: "failed to scrape")

            } catch (e: Exception) {
                e.printStackTrace()
            }
    }

    private fun ScraperGateway.Interpretation.express(): String =
            "Name: $name\nSKU: $partNumber\nBrand: $brand\nURL: $url\nImage: $imageUrl"
}

fun PartType.expressPart(): String {
    return "$name ($partNumber) by $brand:\n" +
            "  Category: $category\n" +
            "  Location: $location\n" +
            "  Team: $team\n" +
            "  Amount Available: $quantity\n" +
            "  URL: $url\n" +
            "  Image URL: $imageUrl"
}