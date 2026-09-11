package com.vesseltutor.app.data.seed

import com.vesseltutor.app.data.local.entity.ScenarioEntity

/**
 * Starter content for a Vessel Manager audience: status updates, calls, meetings, problem
 * explanations, requests and confirmations, all drawn from everyday shipping operations —
 * plus a Daily Life category so practice isn't only about work.
 */
object ScenarioSeeder {

    fun seedScenarios(): List<ScenarioEntity> = listOf(

        // --- Status Updates ---------------------------------------------------------------
        scenario(
            category = ScenarioCategory.STATUS_UPDATES,
            difficulty = Difficulty.BEGINNER,
            context = "You're the Vessel Manager. The port agent just called asking when your vessel will arrive.",
            prompt = "Give me an update on the vessel's arrival status.",
            keyPhrases = listOf("expected to arrive", "on schedule", "delayed due to"),
            vocabulary = listOf("ETA", "berth", "anchorage"),
            sample = "The vessel is on schedule and expected to arrive at the berth by 0600 tomorrow."
        ),
        scenario(
            category = ScenarioCategory.STATUS_UPDATES,
            difficulty = Difficulty.BEGINNER,
            context = "Loading started three hours ago and the terminal supervisor wants a quick update.",
            prompt = "What is the current status of cargo loading?",
            keyPhrases = listOf("loading is in progress", "we have loaded", "completed by"),
            vocabulary = listOf("metric tons", "hold", "stowage"),
            sample = "Loading is in progress, we have loaded eight thousand metric tons so far and expect to be completed by midnight."
        ),
        scenario(
            category = ScenarioCategory.STATUS_UPDATES,
            difficulty = Difficulty.INTERMEDIATE,
            context = "Before departure, the Master radios you asking about bunkers on board.",
            prompt = "How much fuel do we have remaining on board?",
            keyPhrases = listOf("remaining on board", "consumption per day", "sufficient for"),
            vocabulary = listOf("metric tons of fuel", "bunkers", "reserve"),
            sample = "We have four hundred metric tons of fuel remaining on board, with a consumption of twenty tons per day, which is sufficient for the next voyage."
        ),
        scenario(
            category = ScenarioCategory.STATUS_UPDATES,
            difficulty = Difficulty.INTERMEDIATE,
            context = "The office asks for a routine position report during the voyage.",
            prompt = "Report the vessel's current position and speed.",
            keyPhrases = listOf("current position", "we are making", "on course for"),
            vocabulary = listOf("knots", "nautical miles", "course"),
            sample = "Our current position is fifty miles south of the strait, we are making twelve knots and on course for the next waypoint."
        ),
        scenario(
            category = ScenarioCategory.STATUS_UPDATES,
            difficulty = Difficulty.ADVANCED,
            context = "A crew change just finished at the last port and the office wants confirmation.",
            prompt = "Update the office on the crew change status.",
            keyPhrases = listOf("crew change", "has been completed", "signed on", "signed off"),
            vocabulary = listOf("relief crew", "sign-off", "immigration"),
            sample = "The crew change has been completed, three officers signed off and the relief crew signed on without any issues."
        ),

        // --- Phone Calls --------------------------------------------------------------------
        scenario(
            category = ScenarioCategory.PHONE_CALLS,
            difficulty = Difficulty.BEGINNER,
            context = "You need to call the local agent to check if the berth is ready.",
            prompt = "Call the agent and ask about berth availability.",
            keyPhrases = listOf("this is", "calling regarding", "could you confirm"),
            vocabulary = listOf("berth availability", "agent", "schedule"),
            sample = "Good morning, this is the Vessel Manager calling regarding berth availability, could you confirm the expected berthing time?"
        ),
        scenario(
            category = ScenarioCategory.PHONE_CALLS,
            difficulty = Difficulty.INTERMEDIATE,
            context = "The vessel will arrive a day late. You need to call the charterer before they find out another way.",
            prompt = "Call the charterer to explain a delay.",
            keyPhrases = listOf("calling to inform you", "due to", "apologize for the delay"),
            vocabulary = listOf("revised ETA", "charterer", "weather delay"),
            sample = "Hello, I am calling to inform you that, due to heavy weather, the vessel is delayed. I apologize for the delay and the revised ETA is Friday morning."
        ),
        scenario(
            category = ScenarioCategory.PHONE_CALLS,
            difficulty = Difficulty.INTERMEDIATE,
            context = "You're approaching the pilot station and need to radio Port Control.",
            prompt = "Call port control to request the pilot boarding time.",
            keyPhrases = listOf("requesting the pilot boarding time", "this is vessel", "over"),
            vocabulary = listOf("pilot station", "boarding time", "over"),
            sample = "Port Control, this is vessel Ocean Star, requesting the pilot boarding time, over."
        ),
        scenario(
            category = ScenarioCategory.PHONE_CALLS,
            difficulty = Difficulty.BEGINNER,
            context = "The superintendent called while you were busy. You're calling them back.",
            prompt = "Return a missed call from the superintendent.",
            keyPhrases = listOf("returning your call", "sorry I missed your call", "how can I help"),
            vocabulary = listOf("superintendent", "missed call", "available"),
            sample = "Hello, I am returning your call, sorry I missed your call earlier, how can I help you?"
        ),
        scenario(
            category = ScenarioCategory.PHONE_CALLS,
            difficulty = Difficulty.ADVANCED,
            context = "A part just failed and you need a replacement fast. You call the technical department.",
            prompt = "Call to request spare parts urgently.",
            keyPhrases = listOf("we urgently require", "spare part", "as soon as possible"),
            vocabulary = listOf("spare part", "urgent", "advise"),
            sample = "Hello, we urgently require a replacement spare part for the main engine, please arrange delivery as soon as possible and advise the tracking details."
        ),

        // --- Meetings ------------------------------------------------------------------------
        scenario(
            category = ScenarioCategory.MEETINGS,
            difficulty = Difficulty.BEGINNER,
            context = "It's 0900 and the daily operations meeting is about to start.",
            prompt = "Open the daily operations meeting.",
            keyPhrases = listOf("good morning everyone", "let's begin", "first item"),
            vocabulary = listOf("agenda", "operations meeting", "attendance"),
            sample = "Good morning everyone, let's begin the meeting, the first item on the agenda is today's cargo plan."
        ),
        scenario(
            category = ScenarioCategory.MEETINGS,
            difficulty = Difficulty.INTERMEDIATE,
            context = "Before today's cargo work, you need to brief the crew on safety.",
            prompt = "Present the safety briefing to the crew.",
            keyPhrases = listOf("safety briefing", "please be aware", "in case of emergency"),
            vocabulary = listOf("emergency muster", "hazard", "precautions"),
            sample = "This is the safety briefing, please be aware of the hazards on deck today, and in case of emergency proceed to the muster station."
        ),
        scenario(
            category = ScenarioCategory.MEETINGS,
            difficulty = Difficulty.INTERMEDIATE,
            context = "The chief engineer wants to review upcoming maintenance in the meeting.",
            prompt = "Discuss the maintenance schedule at the meeting.",
            keyPhrases = listOf("maintenance schedule", "scheduled for", "top priority"),
            vocabulary = listOf("inspection", "overdue", "priority"),
            sample = "Regarding the maintenance schedule, the main engine overhaul is scheduled for next week, and it is our top priority."
        ),
        scenario(
            category = ScenarioCategory.MEETINGS,
            difficulty = Difficulty.ADVANCED,
            context = "The meeting is wrapping up and someone needs to recap what was agreed.",
            prompt = "Summarize the action items at the end of the meeting.",
            keyPhrases = listOf("to summarize", "action items", "responsible for"),
            vocabulary = listOf("deadline", "follow up", "assigned"),
            sample = "To summarize, we have three action items, the chief officer is responsible for the cargo plan and the deadline is tomorrow morning."
        ),
        scenario(
            category = ScenarioCategory.MEETINGS,
            difficulty = Difficulty.BEGINNER,
            context = "The crew is gathered on deck just before cargo operations begin.",
            prompt = "Give a toolbox talk before cargo operations begin.",
            keyPhrases = listOf("before we start", "safety precautions", "report any hazards"),
            vocabulary = listOf("toolbox talk", "protective equipment", "hazards"),
            sample = "Before we start cargo operations, please review the safety precautions, wear your protective equipment, and report any hazards immediately."
        ),

        // --- Explaining Problems --------------------------------------------------------------
        scenario(
            category = ScenarioCategory.EXPLAINING_PROBLEMS,
            difficulty = Difficulty.INTERMEDIATE,
            context = "The engine room just reported an issue and the chief engineer needs an explanation.",
            prompt = "Explain an engine problem to the chief engineer.",
            keyPhrases = listOf("we have a problem with", "is not functioning properly", "needs repair"),
            vocabulary = listOf("main engine", "malfunction", "repair"),
            sample = "We have a problem with the main engine, the cooling system is not functioning properly and it needs repair before departure."
        ),
        scenario(
            category = ScenarioCategory.EXPLAINING_PROBLEMS,
            difficulty = Difficulty.BEGINNER,
            context = "Bad weather has pushed back your arrival and someone is asking why.",
            prompt = "Explain why the vessel is delayed.",
            keyPhrases = listOf("the vessel is delayed because", "due to bad weather", "we expect to"),
            vocabulary = listOf("delay", "bad weather", "revised schedule"),
            sample = "The vessel is delayed because of bad weather, due to a storm in the area, and we expect to arrive one day later than planned."
        ),
        scenario(
            category = ScenarioCategory.EXPLAINING_PROBLEMS,
            difficulty = Difficulty.ADVANCED,
            context = "A surveyor has come aboard to inspect reported cargo damage.",
            prompt = "Explain a cargo damage issue to the surveyor.",
            keyPhrases = listOf("there is damage to", "caused by", "we have reported"),
            vocabulary = listOf("cargo damage", "surveyor", "claim"),
            sample = "There is damage to a section of the cargo, caused by rough weather during the voyage, and we have reported it to the office."
        ),
        scenario(
            category = ScenarioCategory.EXPLAINING_PROBLEMS,
            difficulty = Difficulty.ADVANCED,
            context = "Port authorities noticed a mismatch in your paperwork.",
            prompt = "Explain a documentation discrepancy to port authorities.",
            keyPhrases = listOf("there is a discrepancy", "in the documents", "we will correct"),
            vocabulary = listOf("discrepancy", "documentation", "port authority"),
            sample = "There is a discrepancy in the documents, the quantity does not match the manifest, and we will correct it as soon as possible."
        ),
        scenario(
            category = ScenarioCategory.EXPLAINING_PROBLEMS,
            difficulty = Difficulty.INTERMEDIATE,
            context = "A crew member is unwell and you need to inform the company.",
            prompt = "Explain a crew medical issue to the company.",
            keyPhrases = listOf("one of the crew members", "is experiencing", "medical assistance"),
            vocabulary = listOf("medical assistance", "symptoms", "next port"),
            sample = "One of the crew members is experiencing severe stomach pain, and we require medical assistance at the next port."
        ),

        // --- Requests --------------------------------------------------------------------------
        scenario(
            category = ScenarioCategory.REQUESTS,
            difficulty = Difficulty.BEGINNER,
            context = "You're planning fuel for the next voyage and need to request more.",
            prompt = "Request additional bunkers for the next voyage.",
            keyPhrases = listOf("I would like to request", "for the next voyage", "please arrange"),
            vocabulary = listOf("bunkers", "next voyage", "arrange"),
            sample = "I would like to request additional bunkers for the next voyage, please arrange delivery before departure."
        ),
        scenario(
            category = ScenarioCategory.REQUESTS,
            difficulty = Difficulty.ADVANCED,
            context = "Severe weather lies ahead on your route and you need to ask for a course change.",
            prompt = "Request permission to deviate from the planned route.",
            keyPhrases = listOf("requesting permission", "to deviate from", "for safety reasons"),
            vocabulary = listOf("deviation", "route", "safety reasons"),
            sample = "We are requesting permission to deviate from the planned route, due to severe weather ahead, for safety reasons."
        ),
        scenario(
            category = ScenarioCategory.REQUESTS,
            difficulty = Difficulty.INTERMEDIATE,
            context = "Equipment needs a replacement part before you can continue.",
            prompt = "Request spare parts from the technical department.",
            keyPhrases = listOf("we require", "spare parts", "at the earliest"),
            vocabulary = listOf("spare parts", "technical department", "earliest"),
            sample = "We require spare parts for the generator, could you please send them at the earliest opportunity."
        ),
        scenario(
            category = ScenarioCategory.REQUESTS,
            difficulty = Difficulty.INTERMEDIATE,
            context = "Cargo work is running long and you may need extra time in port.",
            prompt = "Request an extension of the port stay.",
            keyPhrases = listOf("requesting an extension", "of our port stay", "because"),
            vocabulary = listOf("extension", "port stay", "additional time"),
            sample = "We are requesting an extension of our port stay, because cargo operations are taking longer than planned."
        ),
        scenario(
            category = ScenarioCategory.REQUESTS,
            difficulty = Difficulty.BEGINNER,
            context = "Paperwork for customs is proving tricky and you need the agent's help.",
            prompt = "Request the agent's assistance with customs clearance.",
            keyPhrases = listOf("we need assistance with", "customs clearance", "could you help"),
            vocabulary = listOf("customs clearance", "documents", "assistance"),
            sample = "We need assistance with customs clearance, could you help us prepare the required documents."
        ),

        // --- Confirmations -----------------------------------------------------------------------
        scenario(
            category = ScenarioCategory.CONFIRMATIONS,
            difficulty = Difficulty.BEGINNER,
            context = "The agent needs your confirmed arrival time to prepare the berth.",
            prompt = "Confirm the vessel's ETA to the agent.",
            keyPhrases = listOf("I confirm", "our ETA is", "please prepare"),
            vocabulary = listOf("ETA", "berth number", "confirm"),
            sample = "I confirm our ETA is 0800 tomorrow at berth number four, please prepare for our arrival."
        ),
        scenario(
            category = ScenarioCategory.CONFIRMATIONS,
            difficulty = Difficulty.INTERMEDIATE,
            context = "The office just sent new instructions and wants acknowledgment.",
            prompt = "Confirm receipt of instructions from the office.",
            keyPhrases = listOf("I confirm receipt of", "your instructions", "we will proceed"),
            vocabulary = listOf("receipt", "instructions", "proceed"),
            sample = "I confirm receipt of your instructions, and we will proceed as advised."
        ),
        scenario(
            category = ScenarioCategory.CONFIRMATIONS,
            difficulty = Difficulty.INTERMEDIATE,
            context = "Cargo operations just finished and someone needs official confirmation.",
            prompt = "Confirm completion of cargo operations.",
            keyPhrases = listOf("I confirm that", "cargo operations", "have been completed"),
            vocabulary = listOf("cargo operations", "completed", "documents ready"),
            sample = "I confirm that cargo operations have been completed, and all documents are ready for signature."
        ),
        scenario(
            category = ScenarioCategory.CONFIRMATIONS,
            difficulty = Difficulty.ADVANCED,
            context = "The pilot is due soon and Port Control wants confirmation you're ready.",
            prompt = "Confirm the pilot boarding arrangements.",
            keyPhrases = listOf("I confirm", "pilot boarding", "the vessel is ready"),
            vocabulary = listOf("pilot boarding", "arrangements", "ready"),
            sample = "I confirm the pilot boarding arrangements, the pilot ladder is rigged and the vessel is ready."
        ),
        scenario(
            category = ScenarioCategory.CONFIRMATIONS,
            difficulty = Difficulty.BEGINNER,
            context = "You're about to leave port and need to confirm pre-departure checks.",
            prompt = "Confirm safety checks before departure.",
            keyPhrases = listOf("I confirm", "all safety checks", "ready for departure"),
            vocabulary = listOf("safety checks", "departure", "ready"),
            sample = "I confirm all safety checks have been completed, and the vessel is ready for departure."
        ),

        // --- Daily Life --------------------------------------------------------------------------
        scenario(
            category = ScenarioCategory.DAILY_LIFE,
            difficulty = Difficulty.BEGINNER,
            context = "It's Monday morning and a coworker asks what you did over the weekend.",
            prompt = "Tell a colleague about your weekend.",
            keyPhrases = listOf("over the weekend", "I went to", "it was"),
            vocabulary = listOf("weekend", "relax", "family"),
            sample = "Over the weekend I went to visit my family, it was really nice to relax."
        ),
        scenario(
            category = ScenarioCategory.DAILY_LIFE,
            difficulty = Difficulty.BEGINNER,
            context = "You're at a restaurant and the waiter comes to take your order.",
            prompt = "Order food at a restaurant.",
            keyPhrases = listOf("I would like", "could I have", "for me please"),
            vocabulary = listOf("order", "menu", "waiter"),
            sample = "I would like the chicken curry, could I have some rice with that too, for me please."
        ),
        scenario(
            category = ScenarioCategory.DAILY_LIFE,
            difficulty = Difficulty.BEGINNER,
            context = "You're waiting with a neighbor and want to start a friendly conversation.",
            prompt = "Make small talk about the weather.",
            keyPhrases = listOf("it's quite", "today", "don't you think"),
            vocabulary = listOf("weather", "hot", "forecast"),
            sample = "It's quite hot today, don't you think? I heard it might rain later."
        ),
        scenario(
            category = ScenarioCategory.DAILY_LIFE,
            difficulty = Difficulty.INTERMEDIATE,
            context = "You're lost in an unfamiliar city and need to ask a stranger for help.",
            prompt = "Ask for directions in a new city.",
            keyPhrases = listOf("excuse me", "could you tell me", "how do I get to"),
            vocabulary = listOf("directions", "nearby", "straight ahead"),
            sample = "Excuse me, could you tell me how do I get to the nearest train station?"
        ),
        scenario(
            category = ScenarioCategory.DAILY_LIFE,
            difficulty = Difficulty.BEGINNER,
            context = "You're on a long voyage and it's time for your evening video call home.",
            prompt = "Talk to your child on a video call.",
            keyPhrases = listOf("how was your day", "I miss you", "take care"),
            vocabulary = listOf("video call", "miss", "proud"),
            sample = "How was your day at school? I miss you so much, take care and I'll call again tomorrow."
        )
    )

    private fun scenario(
        category: String,
        difficulty: String,
        context: String,
        prompt: String,
        keyPhrases: List<String>,
        vocabulary: List<String>,
        sample: String
    ) = ScenarioEntity(
        category = category,
        difficulty = difficulty,
        context = context,
        promptText = prompt,
        keyPhrases = keyPhrases.joinToString("|"),
        vocabulary = vocabulary.joinToString("|"),
        sampleAnswer = sample
    )
}
