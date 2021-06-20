package com.rsschool.quiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.rsschool.quiz.databinding.FragmentResultBinding
import kotlin.system.exitProcess
class FragmentResult : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private var intfListener: FragmentsInterface? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setTheme()
        _binding= FragmentResultBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        intfListener = context as FragmentsInterface
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val quiz = (arguments?.get("Quiz") as QuizQuestions)

        binding.result.text =
            getString(R.string.result, quiz.generateResult())

        binding.share.setOnClickListener{
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.result_subject))
                putExtra(Intent.EXTRA_TEXT, quiz.generateShare())
                type = "text/plain"
            }
            startActivity(Intent.createChooser(sendIntent, null))
        }

        binding.back.setOnClickListener{
            quiz.dropQuiz()
            openPage(quiz)
        }
        binding.close.setOnClickListener{
            activity?.finish()
        }
    }

    private fun openPage(quiz: QuizQuestions){
        intfListener?.onNextPage(quiz)
    }

    private fun setTheme(){
        context?.setTheme(resources.getIdentifier("Theme.Quiz.Result","style", context?.packageName))
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
        fun newInstance(quiz:QuizQuestions): FragmentResult {
            val fragment = FragmentResult()
            val args = bundleOf(
                "Quiz" to quiz
            )
            fragment.arguments = args
            return fragment
        }
    }
}