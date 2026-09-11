package com.vesseltutor.app.data.seed

import com.vesseltutor.app.data.local.entity.ScenarioEntity

/**
 * Starter content for a Vessel Manager audience: status updates, calls, meetings, problem
 * explanations, requests and confirmations, all drawn from everyday shipping operations.
 */
object ScenarioSeeder {

    fun seedScenarios(): List<ScenarioEntity> = listOf(

        // --- Status Updates ---------------------------------------------------------------
        scenario(
            category = ScenarioCategory.STATUS_UPDATES,
            difficulty = Difficulty.BEGINNER,
            prompt = "Give me an update on the vessel's arrival status.",
            keyPhrases = listOf("expected to arrive", "on schedule", "delayed due to"),
            vocabulary = listOf("ETA", "berth", "anchorage"),
            sample = "The vessel is on schedule and expected to arrive at the berth by 0600 tomorrow."
        ),
        scenario(
            category = ScenarioCategory.STATUS_UPDATES,
            difficulty = Difficulty.BEGINNER,
            prompt = "What is the current status of cargo loading?",
            keyPhrases = listOf("loading is in progress", "we have loaded", "completed by"),
            vocabulary = listOf("metric tons", "hold", "stowage"),
            sample = "Loading is in progress, we have loaded eight thousand metric tons so far and expect to be completed by midnight."
        ),
        scenario(
            category = ScenarioCategory.STATUS_UPDATES,
            difficulty = Difficulty.INTERMEDIATE,
            prompt = "How much fuel do we have remaining on board?",
            keyPhrases = listOf("remaining on board", "consumption per day", "sufficient for"),
            vocabulary = listOf("metric tons of fuel", "bunkers", "reserve"),
            sample = "We have four hundred metric tons of fuel remaining on board, with a consumption of twenty tons per day, which is sufficient for the next voyage."
        ),
        scenario(
            category = ScenarioCategory.STATUS_UPDATES,
            difficulty = Difficulty.INTERMEDIATE,
            prompt = "Report the vessel's current position and speed.",
            keyPhrases = listOf("current position", "we are making", "on course for"),
            vocabulary = listOf("knots", "nautical miles", "course"),
            sample = "Our current position is fifty miles south of the strait, we are making twelve knots and on course for the next waypoint."
        ),
        scenario(
            category = ScenarioCategory.STATUS_UPDATES,
            difficulty = Difficulty.ADVANCED,
            prompt = "Update the office on the crew change status.",
            keyPhrases = listOf("crew change", "has been completed", "signed on", "signed off"),
            vocabulary = listOf("relief crew", "sign-off", "immigration"),
            sample = "The crew change has been completed, three officers signed off and the relief crew signed on without any issues."
        ),

        // --- Phone Calls --------------------------------------------------------------------
        scenario(
            category = ScenarioCategory.PHONE_CALLS,
            difficulty = Difficulty.BEGINNER,
            prompt = "Call the agent and ask about berth availability.",
            keyPhrases = listOf("this is", "calling regarding", "could you confirm"),
            vocabulary = listOf("berth availability", "agent", "schedule"),
            sample = "Good morning, this is the Vessel Manager calling regarding berth availability, could you confirm the expected berthing time?"
        ),
        scenario(
            category = ScenarioCategory.PHONE_CALLS,
            difficulty = Difficulty.INTERMEDIATE,
            prompt = "Call the charterer to explain a delay.",
            keyPhrases = listOf("calling to inform you", "due to", "apologize for the delay"),
            vocabulary = listOf("revised ETA", "charterer", "weather delay"),
            sample = "Hello, I am calling to inform you that, due to heavy weather, the vessel is delayed. I apologize for the delay and the revised ETA is Friday morning."
        ),
        scenario(
            category = ScenarioCategory.PHONE_CALLS,
            difficulty = Difficulty.INTERMEDIATE,
            prompt = "Call port control to request the pilot boarding time.",
            keyPhrases = listOf("requesting the pilot boarding time", "this is vessel", "over"),
            vocabulary = listOf("pilot station", "boarding time", "over"),
            sample = "Port Control, this is vessel Ocean Star, requesting the pilot boarding time, over."
        ),
        scenario(
            category = ScenarioCategory.PHONE_CALLS,
            difficulty = Difficulty.BEGINNER,
            prompt = "Return a missed call from the superintendent.",
            keyPhrases = listOf("returning your call", "sorry I missed your call", "how can I help"),
            vocabulary = listOf("superintendent", "missed call", "available"),
            sample = "Hello, I am returning your call, sorry I missed your call earlier, how can I help you?"
        ),
        scenario(
            category = ScenarioCategory.PHONE_CALLS,
            difficulty = Difficulty.ADVANCED,
            prompt = "Call the technical department to request spare parts urgently.",
            keyPhrases = listOf("we urgently require", "spare part", "as soon as possible"),
            vocabulary = listOf("spare part", "urgent", "advise"),
            sample = "Hello, we urgently require a replacement spare part for the main engine, please arrange delivery as soon as possible and advise the tracking details."
        ),

        // --- Meetings ------------------------------------------------------------------------
        scenario(
            category = ScenarioCategory.MEETINGS,
            difficulty = Difficulty.BEGINNER,
            prompt = "Open the daily operations meeting.",
            keyPhrases = listOf("good morning everyone", "let's begin", "first item"),
            vocabulary = listOf("agenda", "operations meeting", "attendance"),
            sample = "Good morning everyone, let's begin the meeting, the first item on the agenda is today's cargo plan."
        ),
        scenario(
            category = ScenarioCategory.MEETINGS,
            difficulty = Difficulty.INTERMEDIATE,
            prompt = "Present the safety briefing to the crew.",
            keyPhrases = listOf("safety briefing", "please be aware", "in case of emergency"),
            vocabulary = listOf("emergency muster", "hazard", "precautions"),
            sample = "This is the safety briefing, please be aware of the hazards on deck today, and in case of emergency proceed to the muster station."
        ),
        scenario(
            category = ScenarioCategory.MEETINGS,
            difficulty = Difficulty.INTERMEDIATE,
            prompt = "Discuss the maintenance schedule at the meeting.",
            keyPhrases = listOf("maintenance schedule", "scheduled for", "top priority"),
            vocabulary = listOf("inspection", "overdue", "priority"),
            sample = "Regarding the maintenance schedule, the main engine overhaul is scheduled for next week, and it is our top priority."
        ),
        scenario(
            category = ScenarioCategory.MEETINGS,
            difficulty = Difficulty.ADVANCED,
            prompt = "Summarize the action items at the end of the meeting.",
            keyPhrases = listOf("to summarize", "action items", "responsible for"),
            vocabulary = listOf("deadline", "follow up", "assigned"),
            sample = "To summarize, we have three action items, the chief officer is responsible for the cargo plan and the deadline is tomorrow morning."
        ),
        scenario(
            category = ScenarioCategory.MEETINGS,
            difficulty = Difficulty.BEGINNER,
            prompt = "Give a toolbox talk before cargo operations begin.",
            keyPhrases = listOf("before we start", "safety precautions", "report any hazards"),
            vocabulary = listOf("toolbox talk", "protective equipment", "hazards"),
            sample = "Before we start cargo operations, please review the safety precautions, wear your protective equipment, and report any hazards immediately."
        ),

        // --- Explaining Problems --------------------------------------------------------------
        scenario(
            category = ScenarioCategory.EXPLAINING_PROBLEMS,
            difficulty = Difficulty.INTERMEDIATE,
            prompt = "Explain an engine problem to the chief engineer.",
            keyPhrases = listOf("we have a problem with", "is not functioning properly", "needs repair"),
            vocabulary = listOf("main engine", "malfunction", "repair"),
            sample = "We have a problem with the main engine, the cooling system is not functioning properly and it needs repair before departure."
        ),
        scenario(
            category = ScenarioCategory.EXPLAINING_PROBLEMS,
            difficulty = Difficulty.BEGINNER,
            prompt = "Explain why the vessel is delayed.",
            keyPhrases = listOf("the vessel is delayed because", "due to bad weather", "we expect to"),
            vocabulary = listOf("delay", "bad weather", "revised schedule"),
            sample = "The vessel is delayed because of bad weather, due to a storm in the area, and we expect to arrive one day later than planned."
        ),
        scenario(
            category = ScenarioCategory.EXPLAINING_PROBLEMS,
            difficulty = Difficulty.ADVANCED,
            prompt = "Explain a cargo damage issue to the surveyor.",
            keyPhrases = listOf("there is damage to", "caused by", "we have reported"),
            vocabulary = listOf("cargo damage", "surveyor", "claim"),
            sample = "There is damage to a section of the cargo, caused by rough weather during the voyage, and we have reported it to the office."
        ),
        scenario(
            category = ScenarioCategory.EXPLAINING_PROBLEMS,
            difficulty = Difficulty.ADVANCED,
            prompt = "Explain a documentation discrepancy to port authorities.",
            keyPhrases = listOf("there is a discrepancy", "in the documents", "we will correct"),
            vocabulary = listOf("discrepancy", "documentation", "port authority"),
            sample = "There is a discrepancy in the documents, the quantity does not match the manifest, and we will correct it as soon as possible."
        ),
        scenario(
            category = ScenarioCategory.EXPLAINING_PROBLEMS,
            difficulty = Difficulty.INTERMEDIATE,
            prompt = "Explain a crew medical issue to the company.",
            keyPhrases = listOf("one of the crew members", "is experiencing", "medical assistance"),
            vocabulary = listOf("medical assistance", "symptoms", "next port"),
            sample = "One of the crew members is experiencing severe stomach pain, and we require medical assistance at the next port."
        ),

        // --- Requests --------------------------------------------------------------------------
        scenario(
            category = ScenarioCategory.REQUESTS,
            difficulty = Difficulty.BEGINNER,
            prompt = "Request additional bunkers for the next voyage.",
            keyPhrases = listOf("I would like to request", "for the next voyage", "please arrange"),
            vocabulary = listOf("bunkers", "next voyage", "arrange"),
            sample = "I would like to request additional bunkers for the next voyage, please arrange delivery before departure."
        ),
        scenario(
            category = ScenarioCategory.REQUESTS,
            difficulty = Difficulty.ADVANCED,
            prompt = "Request permission to deviate from the planned route.",
            keyPhrases = listOf("requesting permission", "to deviate from", "for safety reasons"),
            vocabulary = listOf("deviation", "route", "safety reasons"),
            sample = "We are requesting permission to deviate from the planned route, due to severe weather ahead, for safety reasons."
        ),
        scenario(
            category = ScenarioCategory.REQUESTS,
            difficulty = Difficulty.INTERMEDIATE,
            prompt = "Request spare parts from the technical department.",
            keyPhrases = listOf("we require", "spare parts", "at the earliest"),
            vocabulary = listOf("spare parts", "technical department", "earliest"),
            sample = "We require spare parts for the generator, could you please send them at the earliest opportunity."
        ),
        scenario(
            category = ScenarioCategory.REQUESTS,
            difficulty = Difficulty.INTERMEDIATE,
            prompt = "Request an extension of the port stay.",
            keyPhrases = listOf("requesting an extension", "of our port stay", "because"),
            vocabulary = listOf("extension", "port stay", "additional time"),
            sample = "We are requesting an extension of our port stay, because cargo operations are taking longer than planned."
        ),
        scenario(
            category = ScenarioCategory.REQUESTS,
            difficulty = Difficulty.BEGINNER,
            prompt = "Request the agent's assistance with customs clearance.",
            keyPhrases = listOf("we need assistance with", "customs clearance", "could you help"),
            vocabulary = listOf("customs clearance", "documents", "assistance"),
            sample = "We need assistance with customs clearance, could you help us prepare the required documents."
        ),

        // --- Confirmations -----------------------------------------------------------------------
        scenario(
            category = ScenarioCategory.CONFIRMATIONS,
            difficulty = Difficulty.BEGINNER,
            prompt = "Confirm the vessel's ETA to the agent.",
            keyPhrases = listOf("I confirm", "our ETA is", "please prepare"),
            vocabulary = listOf("ETA", "berth number", "confirm"),
            sample = "I confirm our ETA is 0800 tomorrow at berth number four, please prepare for our arrival."
        ),
        scenario(
            category = ScenarioCategory.CONFIRMATIONS,
            difficulty = Difficulty.INTERMEDIATE,
            prompt = "Confirm receipt of instructions from the office.",
            keyPhrases = listOf("I confirm receipt of", "your instructions", "we will proceed"),
            vocabulary = listOf("receipt", "instructions", "proceed"),
            sample = "I confirm receipt of your instructions, and we will proceed as advised."
        ),
        scenario(
            category = ScenarioCategory.CONFIRMATIONS,
            difficulty = Difficulty.INTERMEDIATE,
            prompt = "Confirm completion of cargo operations.",
            keyPhrases = listOf("I confirm that", "cargo operations", "have been completed"),
            vocabulary = listOf("cargo operations", "completed", "documents ready"),
            sample = "I confirm that cargo operations have been completed, and all documents are ready for signature."
        ),
        scenario(
            category = ScenarioCategory.CONFIRMATIONS,
            difficulty = Difficulty.ADVANCED,
            prompt = "Confirm the pilot boarding arrangements.",
            keyPhrases = listOf("I confirm", "pilot boarding", "the vessel is ready"),
            vocabulary = listOf("pilot boarding", "arrangements", "ready"),
            sample = "I confirm the pilot boarding arrangements, the pilot ladder is rigged and the vessel is ready."
        ),
        scenario(
            category = ScenarioCategory.CONFIRMATIONS,
            difficulty = Difficulty.BEGINNER,
            prompt = "Confirm safety checks before departure.",
            keyPhrases = listOf("I confirm", "all safety checks", "ready for departure"),
            vocabulary = listOf("safety checks", "departure", "ready"),
            sample = "I confirm all safety checks have been completed, and the vessel is ready for departure."
        )
    )

    private fun scenario(
        category: String,
        difficulty: String,
        prompt: String,
        keyPhrases: List<String>,
        vocabulary: List<String>,
        sample: String
    ) = ScenarioEntity(
        category = category,
        difficulty = difficulty,
        promptText = prompt,
        keyPhrases = keyPhrases.joinToString("|"),
        vocabulary = vocabulary.joinToString("|"),
        sampleAnswer = sample
    )
}
