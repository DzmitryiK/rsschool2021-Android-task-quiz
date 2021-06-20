package com.rsschool.quiz

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import java.lang.StringBuilder

//Class fo storing Quiz data
@Parcelize
data class QuizQuestions(val questionCount:Int,
                         val questionsArray:Array<String>,
                         val answersArray:Array<Array<String>>): Parcelable{
    var currentQuestion = 0
    var questions : List<Question> = initialize()

    private fun initialize(): List<Question>{
        val resQ = mutableListOf<Question>()
        for(i in 0 until questionCount){
            val ansArr = answersArray[i].copyOfRange(0,5)
            resQ.add(Question(questionsArray[i],ansArr,answersArray[i].last().toInt()))
        }
        return resQ.toList()
    }

    data class Question(val questionName:String,
                        val answers: Array<String>,
                        val rightAnswer:Int){
        var selectedAnswer = -1

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Question

            if (questionName != other.questionName) return false
            if (!answers.contentEquals(other.answers)) return false
            if (rightAnswer != other.rightAnswer) return false
            if (selectedAnswer != other.selectedAnswer) return false

            return true
        }

        override fun hashCode(): Int {
            var result = questionName.hashCode()
            result = 31 * result + answers.contentHashCode()
            result = 31 * result + rightAnswer
            result = 31 * result + selectedAnswer
            return result
        }
    }


    fun dropQuiz(){
        for (e in questions){
            e.selectedAnswer = -1
        }
        currentQuestion = 0
    }

    fun generateResult():String{
        var res = 0
        for (e in questions){
            if (e.selectedAnswer == e.rightAnswer){
                res++
            }
        }
        return "$res out of $questionCount"
    }

    fun generateShare():String{
        val res = StringBuilder()
        res.append("Your result: "+generateResult())
        for (i in questions.indices){
            res.append(System.getProperty("line.separator"))
            res.append(System.getProperty("line.separator"))
            res.append((i+1).toString()+") "+questions[i].questionName)
            res.append(System.getProperty("line.separator"))
            res.append("Your answer: "+ (questions[i].answers[questions[i].selectedAnswer]))
        }

        return res.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuizQuestions

        if (questionCount != other.questionCount) return false
        if (!questionsArray.contentEquals(other.questionsArray)) return false
        if (!answersArray.contentDeepEquals(other.answersArray)) return false
        if (currentQuestion != other.currentQuestion) return false
        if (questions != other.questions) return false

        return true
    }

    override fun hashCode(): Int {
        var result = questionCount
        result = 31 * result + questionsArray.contentHashCode()
        result = 31 * result + answersArray.contentDeepHashCode()
        result = 31 * result + currentQuestion
        result = 31 * result + questions.hashCode()
        return result
    }
}


