package com.nitc.projectsgc.composable.util

import arrow.core.Either

object PathUtils {

    fun getMentorType(username: String): Either<Exception, String> {
        val underscoreIndex = username.indexOfFirst { it == '_' }
        if (underscoreIndex == -1) {
            return Either.Left(Exception("Username format invalid: Missing underscore"))
        }
        val lastUnderscoreIndex = username.indexOfLast { it == '_' }
        if (lastUnderscoreIndex == -1) {
            return Either.Left(Exception("Username format invalid: Missing ending underscore"))
        }
        return Either.Right(username.substring(underscoreIndex + 1, lastUnderscoreIndex))
    }

    fun isValidStudentEmail(email: String): Boolean {
        val regex = Regex("""^[a-zA-Z]+_[a-zA-Z][0-9]{6}[a-zA-Z]{2}@nitc\.ac\.in$""")
        return regex.matches(email)
    }
    fun isValidMentorUsername(username: String): Either<Exception,Boolean> {
        if (!username.contains("_") || !username.endsWith("@nitc.ac.in")) {
            Either.Left(Exception("Username must contain '_' and end with '@nitc.ac.in'"))
        }
        val parts = username.split("_")
        if (parts.size != 3) {
            Either.Left(Exception("Username must have two underscores separating three parts"))
        }

        val name1 = parts[0]
        val specialty = parts[1]
        val optionalNumber = parts[2]

        if (!name1.matches("[a-zA-Z]+".toRegex())) {
            Either.Left(Exception("First name part must contain only letters"))
        }
        if (!specialty.matches("[a-zA-Z]+".toRegex())) {
            Either.Left(Exception("Specialty part must contain only letters"))
        }

        val optionalNumberRegex = "[0-9]*".toRegex() // Matches zero or more digits

        if (!optionalNumber.matches(optionalNumberRegex)) {
            Either.Left(Exception("Optional number part must be digits or empty"))
        }

        return Either.Right(true)
    }


}