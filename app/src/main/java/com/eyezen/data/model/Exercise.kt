package com.eyezen.data.model

/**
 * Exercise data model.
 */
data class Exercise(
    val id: String,
    val name: String,
    val description: String,
    val category: String, // "eye", "neck", "shoulder", "wrist", "back", "breathing"
    val duration: Int, // in seconds
    val instructions: List<String>,
    val imageUrl: String? = null,
    val videoUrl: String? = null,
    val difficulty: String = "easy" // easy, medium, hard
)

/**
 * Predefined exercises database
 */
object ExerciseDatabase {
    val exercises = listOf(
        // Eye Exercises
        Exercise(
            id = "eye_1",
            name = "Eye Rolling",
            description = "Gentle eye movements to relax eye muscles",
            category = "eye",
            duration = 30,
            instructions = listOf(
                "Close your eyes gently",
                "Roll your eyes slowly in a circle clockwise 5 times",
                "Roll counter-clockwise 5 times",
                "Open your eyes and blink several times"
            ),
            difficulty = "easy"
        ),
        Exercise(
            id = "eye_2",
            name = "Blinking Exercise",
            description = "Rapid blinking to refresh eyes",
            category = "eye",
            duration = 20,
            instructions = listOf(
                "Blink rapidly 10-15 times",
                "Close your eyes and relax for 2 seconds",
                "Repeat 3 times"
            ),
            difficulty = "easy"
        ),
        Exercise(
            id = "eye_3",
            name = "Focus Shift",
            description = "Change focus from near to far objects",
            category = "eye",
            duration = 60,
            instructions = listOf(
                "Hold your finger 6 inches from your eyes",
                "Focus on it for 3-5 seconds",
                "Look at a distant object for 3-5 seconds",
                "Repeat 10 times"
            ),
            difficulty = "easy"
        ),

        // Neck Exercises
        Exercise(
            id = "neck_1",
            name = "Neck Rotations",
            description = "Gentle neck stretches",
            category = "neck",
            duration = 45,
            instructions = listOf(
                "Sit upright with shoulders relaxed",
                "Slowly turn head to the right, hold 15 seconds",
                "Return to center",
                "Turn to the left, hold 15 seconds",
                "Repeat 3 times each side"
            ),
            difficulty = "easy"
        ),
        Exercise(
            id = "neck_2",
            name = "Neck Tilt",
            description = "Side-to-side neck stretching",
            category = "neck",
            duration = 40,
            instructions = listOf(
                "Sit upright",
                "Tilt your head towards right shoulder, hold 15 seconds",
                "Return to center",
                "Tilt to left shoulder, hold 15 seconds",
                "Repeat 3 times each side"
            ),
            difficulty = "easy"
        ),

        // Shoulder Exercises
        Exercise(
            id = "shoulder_1",
            name = "Shoulder Rolls",
            description = "Rolling shoulders to release tension",
            category = "shoulder",
            duration = 50,
            instructions = listOf(
                "Sit or stand with good posture",
                "Roll shoulders backward 10 times",
                "Roll shoulders forward 10 times",
                "Repeat 2 times"
            ),
            difficulty = "easy"
        ),
        Exercise(
            id = "shoulder_2",
            name = "Shoulder Shrug",
            description = "Raise and lower shoulders",
            category = "shoulder",
            duration = 30,
            instructions = listOf(
                "Sit upright",
                "Raise shoulders to ears, hold 2 seconds",
                "Lower shoulders, relax",
                "Repeat 15 times"
            ),
            difficulty = "easy"
        ),

        // Wrist Exercises
        Exercise(
            id = "wrist_1",
            name = "Wrist Rotation",
            description = "Rotate wrists to improve flexibility",
            category = "wrist",
            duration = 30,
            instructions = listOf(
                "Extend arms in front of you",
                "Rotate wrists clockwise 10 times",
                "Rotate counter-clockwise 10 times",
                "Shake out hands"
            ),
            difficulty = "easy"
        ),
        Exercise(
            id = "wrist_2",
            name = "Wrist Flexion",
            description = "Flex and extend wrists",
            category = "wrist",
            duration = 40,
            instructions = listOf(
                "Hold hands in front of you, palms down",
                "Flex wrists up, hold 2 seconds",
                "Extend wrists down, hold 2 seconds",
                "Repeat 15 times"
            ),
            difficulty = "easy"
        ),

        // Back Exercises
        Exercise(
            id = "back_1",
            name = "Back Stretch",
            description = "Stretch back muscles",
            category = "back",
            duration = 45,
            instructions = listOf(
                "Sit upright in your chair",
                "Clasp hands behind your head",
                "Gently arch backward, hold 15-20 seconds",
                "Return to center",
                "Repeat 3 times"
            ),
            difficulty = "easy"
        ),

        // Breathing Exercises
        Exercise(
            id = "breathing_1",
            name = "Deep Breathing",
            description = "Calming deep breathing",
            category = "breathing",
            duration = 60,
            instructions = listOf(
                "Sit comfortably",
                "Inhale deeply for 4 counts",
                "Hold for 4 counts",
                "Exhale for 4 counts",
                "Repeat 5 times"
            ),
            difficulty = "easy"
        )
    )

    fun getExercisesByCategory(category: String): List<Exercise> {
        return exercises.filter { it.category == category }
    }

    fun getExerciseById(id: String): Exercise? {
        return exercises.find { it.id == id }
    }
}
