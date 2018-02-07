package com.fyp3.quiz

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_answer_sheet_objective.*
import kotlinx.android.synthetic.main.fragment_answer_sheet_subjective.*
import java.io.Serializable

class AnswerSheetFragment : Fragment() {

    companion object {
        private val TAG = AnswerSheetFragment::class.java.simpleName

        private const val QUES_OBJ = "QUES_OBJ"
        private const val POSITION = "POSITION"

        fun newInstance(position : Int, quesObject: QuesObject) : AnswerSheetFragment {

            val args = Bundle()
            args.putSerializable(QUES_OBJ, quesObject as Serializable)
            args.putInt(POSITION, position)

            val fragment = AnswerSheetFragment()
            fragment.arguments = args

            return fragment
        }
    }

    lateinit var question : QuesObject
    var position : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        question = arguments!!.getSerializable(QUES_OBJ) as QuesObject
        position = arguments!!.getInt(POSITION)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if( question.isObjective )
        {
            return inflater.inflate(R.layout.fragment_answer_sheet_objective, container, false)
        }
        else
        {
            return inflater.inflate(R.layout.fragment_answer_sheet_subjective, container, false)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pos = this.position.inc()

        val txt_ques_title = view.findViewById<TextView>(R.id.txt_ques_title) as TextView
        txt_ques_title.text = pos.toString() + ". " + question.ques_title

        val txt_ques_content = view.findViewById<TextView>(R.id.txt_ques_content) as TextView

        if(question.ques_content.contains("[BLANK]", true))
        {
            val replace_string = question.ques_content.replace("[BLANK]",
                    "<u>_______</u>", true)

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                txt_ques_content.text = Html.fromHtml(replace_string, Html.FROM_HTML_MODE_LEGACY);
            } else {
                txt_ques_content.text = Html.fromHtml(replace_string);
            }

        }
        else
        {
            txt_ques_content.text = question.ques_content
        }

        // Objective UI Layout
        if(question.isObjective) {

            val radiobutton_option_a = view.findViewById<RadioButton>(R.id.radiobutton_option_a) as RadioButton
            radiobutton_option_a.text = question.option_a

            val radiobutton_option_b = view.findViewById<RadioButton>(R.id.radiobutton_option_b) as RadioButton
            radiobutton_option_b.text = question.option_b

            val radiobutton_option_c = view.findViewById<RadioButton>(R.id.radiobutton_option_c) as RadioButton
            radiobutton_option_c.text = question.option_c

            val radiobutton_option_d = view.findViewById<RadioButton>(R.id.radiobutton_option_d) as RadioButton
            radiobutton_option_d.text = question.option_d

            radiobutton_option_a.isEnabled = false
            radiobutton_option_b.isEnabled = false
            radiobutton_option_c.isEnabled = false
            radiobutton_option_d.isEnabled = false

            if( question.objective_answer.equals("A", true))
            {
                radiobutton_option_a.isChecked = true
                radiobutton_option_a.setTextColor(ContextCompat.getColor(view.context, android.R.color.holo_green_dark))

            }
            else if( question.objective_answer.equals("B", true))
            {
                radiobutton_option_b.isChecked = true
                radiobutton_option_b.setTextColor(ContextCompat.getColor(view.context, android.R.color.holo_green_dark))

            }
            else if(question.objective_answer.equals("C", true))
            {
                radiobutton_option_c.isChecked = true
                radiobutton_option_c.setTextColor(ContextCompat.getColor(view.context, android.R.color.holo_green_dark))

            }
            else if(question.objective_answer.equals("D", true))
            {
                radiobutton_option_d.isChecked = true
                radiobutton_option_d.setTextColor(ContextCompat.getColor(view.context, android.R.color.holo_green_dark))

            }

            if( question.isCorrectAnswer == 1 )
            {
                img_icon_objective.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_tick))

            }
            else
            {
                img_icon_objective.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_cross))

                if( question.your_answer.equals("A", true))
                {
                    radiobutton_option_a.isChecked = true
                    radiobutton_option_a.setTextColor(ContextCompat.getColor(view.context, android.R.color.holo_red_light))

                }
                else if( question.your_answer.equals("B", true))
                {
                    radiobutton_option_b.isChecked = true
                    radiobutton_option_b.setTextColor(ContextCompat.getColor(view.context, android.R.color.holo_red_light))

                }
                else if(question.your_answer.equals("C", true))
                {
                    radiobutton_option_c.isChecked = true
                    radiobutton_option_c.setTextColor(ContextCompat.getColor(view.context, android.R.color.holo_red_light))

                }
                else if(question.your_answer.equals("D", true))
                {
                    radiobutton_option_d.isChecked = true
                    radiobutton_option_d.setTextColor(ContextCompat.getColor(view.context, android.R.color.holo_red_light))

                }

            }

        } else {

            // is Subjective

            val txt_your_answer = view.findViewById<TextView>(R.id.txt_your_answer)
            val txt_correct_answer = view.findViewById<TextView>(R.id.txt_correct_answer)
            val txt_title_correct_answer = view.findViewById<TextView>(R.id.txt_title_correct_answer)

            if( question.isCorrectAnswer == 1 )
            {
                img_icon_subjective.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_tick))
                txt_correct_answer.visibility = View.GONE
                txt_title_correct_answer.visibility = View.GONE

                txt_your_answer.text = question.your_answer
                txt_your_answer.setTextColor(ContextCompat.getColor(view.context, android.R.color.holo_green_dark))

            }
            else
            {
                img_icon_subjective.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_cross))

                if( question.your_answer.equals("null", true))
                {
                    txt_your_answer.text = "-"
                }
                else
                {
                    txt_your_answer.text = question.your_answer
                }
                txt_your_answer.setTextColor(ContextCompat.getColor(view.context, android.R.color.holo_red_light))

                txt_correct_answer.text = question.subjective_answer
                txt_correct_answer.setTextColor(ContextCompat.getColor(view.context, android.R.color.holo_green_dark))

            }

        }

    }
}