package com.rsschool.quiz

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.rsschool.quiz.databinding.FragmentQuizBinding


class FragmentQuiz : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private var intfListener: FragmentsInterface? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setTheme((arguments?.get("Quiz") as QuizQuestions).currentQuestion)
        _binding= FragmentQuizBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        intfListener = context as FragmentsInterface
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val quiz = (arguments?.get("Quiz") as QuizQuestions)
        loadQuestion(quiz)


        //Checks
        if (quiz.currentQuestion == 0){
            binding.previousButton.isEnabled = false
            binding.toolbar.navigationIcon = null
        }
        if (quiz.questions[quiz.currentQuestion].selectedAnswer == -1){
            binding.nextButton.isEnabled = false
        }else{
            binding.radioGroup.check(
                 (binding.radioGroup.getChildAt(quiz.questions[quiz.currentQuestion].selectedAnswer) as RadioButton).id
            )
        }
        if (quiz.currentQuestion+1 == quiz.questionCount){
            binding.nextButton.text = getString(R.string.next_btn_submit)
        }


        //Listeners
        binding.nextButton.setOnClickListener{
            openPage(quiz)
        }
        binding.radioGroup.setOnCheckedChangeListener { _, _ -> binding.nextButton.isEnabled = true }

        binding.previousButton.setOnClickListener{
            openPage(quiz, false)
        }
        binding.toolbar.setNavigationOnClickListener{
            openPage(quiz, false)
        }
    }

    private fun loadQuestion(quiz: QuizQuestions){
        binding.question.text =
            quiz.questions[quiz.currentQuestion].questionName
        binding.optionOne.text =
            quiz.questions[quiz.currentQuestion].answers[0]
        binding.optionTwo.text =
            quiz.questions[quiz.currentQuestion].answers[1]
        binding.optionThree.text =
            quiz.questions[quiz.currentQuestion].answers[2]
        binding.optionFour.text =
            quiz.questions[quiz.currentQuestion].answers[3]
        binding.optionFive.text =
            quiz.questions[quiz.currentQuestion].answers[4]

        binding.toolbar.title =
            "Question "+(quiz.currentQuestion+1).toString()
    }

    fun openPage(quiz: QuizQuestions, next:Boolean = true){
        quiz.questions[quiz.currentQuestion].selectedAnswer =
            when (binding.radioGroup.checkedRadioButtonId){
                binding.optionOne.id -> 0
                binding.optionTwo.id -> 1
                binding.optionThree.id -> 2
                binding.optionFour.id -> 3
                binding.optionFive.id -> 4
                else -> -1
            }

        if (next)  quiz.currentQuestion++
        else quiz.currentQuestion--

        intfListener?.onNextPage(quiz)
    }

    private fun setTheme(themeNo: Int){
        val thNumber = when (themeNo){
            0 -> "First"
            1 -> "Second"
            2 -> "Third"
            3 -> "Fourth"
            4 -> "Fifth"
            else -> "First"
        }
        context?.setTheme(resources.getIdentifier("Theme.Quiz.$thNumber","style", context?.packageName))
        val typedValue = TypedValue()
        context?.theme?.resolveAttribute(android.R.attr.statusBarColor, typedValue, true)
        activity?.window?.statusBarColor = typedValue.data
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance(quiz:QuizQuestions): FragmentQuiz {
            val fragment = FragmentQuiz()
            val args = bundleOf(
                "Quiz" to quiz
            )
            fragment.arguments = args
            return fragment
        }
    }
}