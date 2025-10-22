package org.rsmod.content.areas.city.lumbridge.npcs

import jakarta.inject.Inject
import org.rsmod.api.config.refs.objs
import org.rsmod.api.invtx.invAdd
import org.rsmod.api.invtx.invAddOrDrop
import org.rsmod.api.invtx.invTakeFee
import org.rsmod.api.player.dialogue.Dialogue
import org.rsmod.api.player.output.spam
import org.rsmod.api.player.protect.ProtectedAccess
import org.rsmod.api.player.stat.baseFishingLvl
import org.rsmod.api.repo.obj.ObjRepository
import org.rsmod.api.script.advanced.onUnimplementedOpNpc1
import org.rsmod.content.areas.city.lumbridge.configs.lumbridge_npcs
import org.rsmod.game.entity.Npc
import org.rsmod.plugin.scripts.PluginScript
import org.rsmod.plugin.scripts.ScriptContext

class FishingTutor @Inject constructor(private val objRepo: ObjRepository) : PluginScript() {
    override fun ScriptContext.startup() {
        onUnimplementedOpNpc1(lumbridge_npcs.fishing_tutor) { startDialogue(it.npc) }
    }

    private suspend fun ProtectedAccess.startDialogue(npc: Npc) {
        startDialogue(npc) { fishingDialogue() }
    }

    private suspend fun Dialogue.fishingDialogue() {
        when {
            player.baseFishingLvl >= 99 -> fishingMasteryDialogue()
            player.baseFishingLvl in 29..98 -> fishingHighLevelMenu()
            player.baseFishingLvl in 20..28 -> fishingIntermediateLevelMenu()
            else -> fishingLowLevelMenu()
        }
    }

    private suspend fun Dialogue.fishingLowLevelMenu() {
        val choice =
            choice2(
                "Can you teach me the basics of Fishing, please?",
                1,
                "What is that cape you're wearing?",
                2,
            )
        if (choice == 1) {
            fishingNoviceBasics()
        } else if (choice == 2) {
            fishingCapeExplanationNonMastery()
        }
    }

    private suspend fun Dialogue.fishingNoviceBasics() {
        chatPlayer(quiz, "Can you teach me the basics of Fishing, please?")
        objbox(
            objs.fishing_icon,
            "Of course... look for this icon on your minimap to find areas of fish.",
        )
        giveNetIfRequired()
        chatNpc(neutral, "When you see a likely looking fishing spot, simply click on it to fish.")
        chatNpc(
            happy,
            "When you have a full inventory of fish, you have a " +
                "choice. You can take it to the bank, there's one on the " +
                "roof of the castle in Lumbridge, or you can cook them.",
        )
        objbox(
            objs.bank_icon,
            "To find a bank, look for this symbol on your minimap " +
                "after climbing the stairs of the Lumbridge Castle to the " +
                "top. There are banks all over the world with this symbol.",
        )
        chatNpc(
            happy,
            "To cook your fish, you can use a range, " +
                "or light a fire with a tinderbox and some logs." +
                "Then use the raw fish on the fire or range.",
        )
    }

    private suspend fun Dialogue.fishingIntermediateLevelMenu() {
        val choice =
            choice4(
                "I already know a bit about Fishing, any tips?",
                1,
                "Tell me about different fish and tools.",
                2,
                "What is that cape you're wearing?",
                3,
                "Goodbye.",
                4,
            )
        when (choice) {
            1 -> fishingIntermediateAdvice()
            2 -> fishingToolInquiry()
            3 -> fishingCapeExplanationNonMastery()
            4 -> goodbye()
        }
    }

    private suspend fun Dialogue.fishingIntermediateAdvice() {
        chatPlayer(quiz, "I already know about the basics of fishing, got any tips?")
        chatNpc(
            happy,
            "Choose carefully where and what you fish, you can get " +
                "different fish in different places throughout the land.",
        )
        chatNpc(
            happy,
            "Make sure you hang on to your fish, don't throw them " +
                "away as you can get valuable cooking experience " +
                "from them! Look out for other things too...",
        )
        chatNpc(
            shifty,
            "Watch out for the sea creatures, they really don't like you " +
                "fishing, they can really do some damage if " +
                "you're not paying attention...",
        )
    }

    private suspend fun Dialogue.fishingHighLevelMenu() {
        val choice =
            choice4(
                "Any advice for an advanced fisher?",
                1,
                "Tell me about different fish and tools.",
                2,
                "What is that cape you're wearing?",
                3,
                "Goodbye.",
                4,
            )
        when (choice) {
            1 -> fishingAdvancedAdvice()
            2 -> fishingToolInquiry()
            3 -> fishingCapeExplanationNonMastery()
            4 -> goodbye()
        }
    }

    private suspend fun Dialogue.fishingAdvancedAdvice() {
        chatPlayer(quiz, "Any advice for an advanced fisher?")
        giveNetIfRequired()
        chatNpc(
            happy,
            "As you get better and better you'll find that you can " +
                "catch fish such as Salmon and Tuna! These are very " +
                "good for cooking.",
        )
        chatNpc(
            happy,
            "Also, look out for caskets, you never know what our " +
                "watery friends have been collecting. Clue scrolls " +
                "maybe?",
        )
        fishingHighLevelMenu()
    }

    private suspend fun Dialogue.fishingToolInquiry() {
        chatPlayer(quiz, "Tell me about different fish and tools.")
        fishingToolMenu()
    }

    private suspend fun Dialogue.fishingToolMenu() {
        val choice =
            choice4(
                "Shrimp and Anchovies",
                1,
                "Salmon and Tuna",
                2,
                "Tools",
                3,
                "Go back to teaching",
                4,
                title = "Fish",
            )
        when (choice) {
            1 -> netAndBaitExplanation()
            2 -> rodAndLureExplanation()
            3 -> fishingToolsExplanation()
            4 -> fishingHighLevelMenu()
        }
    }

    private suspend fun Dialogue.netAndBaitExplanation() {
        doubleobjbox(
            objs.raw_shrimp,
            objs.raw_anchovies,
            "Almost every body of water can be fished. Small fishing nets " +
                "will catch 'Shrimp' and 'Anchovies'. You can find these " +
                "in most coastal areas.",
        )
        objbox(
            objs.fishing_bait,
            "Fishing bait is used with a fishing rod to catch different types of fish.",
        )
        fishingToolMenu()
    }

    private suspend fun Dialogue.rodAndLureExplanation() {
        objbox(
            objs.raw_salmon,
            "Salmon can be caught with a fishing rod and fishing bait. You'll " +
                "usually find Salmon in rivers and lakes.",
        )
        doubleobjbox(
            objs.raw_tuna,
            objs.fishing_icon,
            "Tuna are found in deeper waters. We do our best to " +
                "cultivate them. Look for the fishing icon on your minimap " +
                "to find rare fishing spots. Try Catherby.",
        )
        fishingToolMenu()
    }

    private suspend fun Dialogue.fishingToolsExplanation() {
        objbox(
            objs.small_fishing_net,
            "Small fishing nets are easy to get, simply go visit a fishing shop, " +
                "or talk to me if you have mislaid yours.",
        )
        chatNpc(
            happy,
            "As you progress in your fishing skill you will find you " +
                "can use better tools to catch fish faster.",
        )
        doubleobjbox(
            objs.fishing_rod,
            objs.fly_fishing_rod,
            "As your fishing skill increases you will find " +
                "yourself able to use better fishing rods.... " +
                "anything up to a fly fishing rod you can buy from a fishing shop.",
        )
        objbox(
            objs.harpoon,
            "Harpoons can be used to catch bigger fish like sharks. They can also " +
                "be obtained through killing certain sea creatures, though this is " +
                "very rare.",
        )
        fishingToolMenu()
    }

    private suspend fun Dialogue.goodbye() {
        chatPlayer(neutral, "Goodbye.")
    }

    private suspend fun Dialogue.giveNetIfRequired() {
        if (objs.small_fishing_net in player.inv) {
            return
        }
        val confirm =
            choice2(
                "Yes, please.",
                1,
                "No, thank you.",
                2,
                title = "Would you like a small fishing net?",
            )
        if (confirm == 2) {
            chatNpc(neutral, "Perhaps another time then.")
            return
        }
        val add = player.invAdd(player.inv, objs.small_fishing_net)
        if (add.success) {
            chatNpc(
                happy,
                "As you're already here, have a net so that you can fish in the waters around me.",
            )
        } else {
            chatNpc(
                sad,
                "I'd give you a net to fish with but you don't have room in your inventory.",
            )
        }
    }

    private suspend fun Dialogue.fishingCapeExplanationNonMastery() {
        chatPlayer(happy, "What is that cape you're wearing?")
        chatNpc(
            neutral,
            "This is a Skillcape of Fishing, wearing one " +
                "increases your chance of catching rare fish. Only a " +
                "person who has achieved the highest possible level in a " +
                "skill can wear one.",
        )
    }

    private suspend fun Dialogue.fishingMasteryDialogue() {
        val choice =
            choice2("What is that cape you're wearing?", 1, "Can I buy a Fishing skillcape?", 2)
        if (choice == 1) {
            fishingCapeExplanationMastery()
        } else if (choice == 2) {
            purchaseFishingSkillcape()
        }
    }

    private suspend fun Dialogue.fishingCapeExplanationMastery() {
        chatPlayer(happy, "What is that cape you're wearing?")
        chatNpc(
            neutral,
            "This is a Skillcape of Fishing, wearing one " +
                "increases your chance of catching rare fish. Only a " +
                "person who has achieved the highest possible level in a " +
                "skill can wear one.",
        )
        fishingMasteryDialogue()
    }

    private suspend fun Dialogue.purchaseFishingSkillcape() {
        chatPlayer(quiz, "Can I buy a Fishing skillcape?")
        val confirm =
            choice2(
                "Yes",
                1,
                "No, thank you.",
                2,
                title = "Are you sure you want to buy a Fishing cape for 99,000 coins?",
            )
        if (confirm == 2) {
            chatNpc(neutral, "Perhaps another time then.")
            return
        }
        if (player.inv.freeSpace() < 2) {
            chatNpc(sad, "You need at least two free inventory spaces to buy a skillcape.")
            return
        }
        if (!player.invTakeFee(99000)) {
            chatNpc(sad, "You don't have enough money to buy a skillcape. They cost 99,000 coins.")
            return
        }
        player.spam("You buy a Fishing skillcape and hood.")
        player.invAddOrDrop(objRepo, objs.fishing_skillcape)
        player.invAddOrDrop(objRepo, objs.fishing_hood)
    }
}
