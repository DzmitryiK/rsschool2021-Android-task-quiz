package com.rsschool.quiz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), FragmentsInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val quiz = loadQuestions(5)
        openPage(quiz)
    }

    //Loading questions and answers from resources (string_questions)
    private fun loadQuestions(questionsCount: Int): QuizQuestions{
        val questArr = resources.getStringArray(R.array.questions_names)
        val ansList = mutableListOf<Array<String>>()
        for (i in 0 until questionsCount) {
            ansList.add(resources.getStringArray(resources.getIdentifier("question_"+(i+1).toString()+"_answers", "array", packageName)))
        }

        return QuizQuestions(questionsCount, questArr, ansList.toTypedArray())
    }

    private fun openPage(quizQuestions: QuizQuestions){
        val fragment = if (quizQuestions.currentQuestion == quizQuestions.questionCount){
            FragmentResult.newInstance(quizQuestions)
        }else FragmentQuiz.newInstance(quizQuestions)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment, "tag_"+quizQuestions.currentQuestion.toString())
            .commit()
    }

    override fun onNextPage(quizQuestions: QuizQuestions) {
        openPage(quizQuestions)
    }

    override fun onBackPressed() {
      val foundFragment: FragmentResult? = supportFragmentManager.findFragmentByTag("tag_5") as FragmentResult?
        if (foundFragment != null && foundFragment.isVisible) {
            val quiz = foundFragment.arguments?.get("Quiz") as QuizQuestions
            quiz.dropQuiz()
            openPage(quiz)
        } else {
            var foundFragmentQ: FragmentQuiz? = supportFragmentManager.findFragmentByTag("tag_0") as FragmentQuiz?
            if (foundFragmentQ != null && foundFragmentQ.isVisible) {
                super.onBackPressed()
            }else {
                for (i in 1 until 5){
                    foundFragmentQ = supportFragmentManager.findFragmentByTag("tag_$i") as FragmentQuiz?
                    if (foundFragmentQ != null && foundFragmentQ.isVisible){
                        break
                    }
                }
                foundFragmentQ?.openPage(foundFragmentQ.arguments?.get("Quiz") as QuizQuestions, false)
            }
        }
    }
}