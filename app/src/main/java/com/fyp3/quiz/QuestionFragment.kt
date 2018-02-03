package com.fyp3.quiz

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.json.JSONArray
import java.io.Serializable

/**
 * Created by limcg on 03/01/2018.
 */
class QuestionFragment : Fragment() {

    interface OnResultOptionListener {

        fun onResultOption(ques_id : Int, ques_answer : String)
    }

    companion object {

        private val TAG = QuestionFragment::class.java.simpleName

        private const val QUES_OBJ = "QUES_OBJ"
        private const val POSITION = "POSITION"
        private const val ANSWER = "ANSWER"

        fun newInstance(position : Int, answer : String, question: QuesObject) : QuestionFragment
        {
            val args = Bundle()
            args.putSerializable(QUES_OBJ, question as Serializable)
            args.putInt(POSITION, position)
            args.putString(ANSWER, answer)
            val fragment = QuestionFragment()
            fragment.arguments = args

            return fragment
        }

    }

    lateinit var questionBank : QuesObject
    lateinit var onResultOptionListener : OnResultOptionListener
    var answerJsonArray : JSONArray? = null
    lateinit var answer : String
    var position : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        position = arguments!!.getInt(POSITION)
        questionBank = arguments!!.getSerializable(QUES_OBJ) as QuesObject
        answer = arguments!!.getString(ANSWER) as String

        answerJsonArray = JSONArray(answer)

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if(context is OnResultOptionListener)
        {
            onResultOptionListener = context
        }
        else
        {
            throw RuntimeException("OnResultOptionListener not implemented in context");
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if( questionBank.isObjective )
        {
            val view = inflater.inflate(R.layout.fragment_question_objective, container, false)

            return view
        }
        else
        {
            val view = inflater.inflate(R.layout.fragment_question_subjective, container, false)

            return view
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onResultOptionListener.onResultOption(questionBank.ques_id, "null")

        var pos = this.position
        pos = pos.inc()

        val txt_ques_title = view.findViewById<TextView>(R.id.txt_ques_title) as TextView
        txt_ques_title.text = pos.toString() + ". " + questionBank.ques_title

        val txt_ques_content = view.findViewById<TextView>(R.id.txt_ques_content) as TextView

        if(questionBank.ques_content.contains("[BLANK]", true))
        {
            val replace_string = questionBank.ques_content.replace("[BLANK]",
                    "<u>_______</u>", true)

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                txt_ques_content.text = Html.fromHtml(replace_string, Html.FROM_HTML_MODE_LEGACY);
            } else {
                txt_ques_content.text = Html.fromHtml(replace_string);
            }

        }
        else
        {
            txt_ques_content.text = questionBank.ques_content
        }

        // Objective UI Layout
        if(questionBank.isObjective)
        {
            val radiobutton_option_a = view.findViewById<RadioButton>(R.id.radiobutton_option_a) as RadioButton
            radiobutton_option_a.text = questionBank.option_a

            val radiobutton_option_b = view.findViewById<RadioButton>(R.id.radiobutton_option_b) as RadioButton
            radiobutton_option_b.text = questionBank.option_b

            val radiobutton_option_c = view.findViewById<RadioButton>(R.id.radiobutton_option_c) as RadioButton
            radiobutton_option_c.text = questionBank.option_c

            val radiobutton_option_d = view.findViewById<RadioButton>(R.id.radiobutton_option_d) as RadioButton
            radiobutton_option_d.text = questionBank.option_d

            try {

                if( answerJsonArray != null )
                {
                    Log.i(TAG, answerJsonArray.toString())

                    if( answerJsonArray!!.getJSONObject(this.position)
                            .getString("ans_ques_answer").equals("A", true)) {

                        radiobutton_option_a.isChecked = true
                        onResultOptionListener.onResultOption(questionBank.ques_id, "A")

                    } else if( answerJsonArray!!.getJSONObject(this.position)
                            .getString("ans_ques_answer").equals("B", true) ) {

                        radiobutton_option_b.isChecked = true
                        onResultOptionListener.onResultOption(questionBank.ques_id, "B")

                    } else if( answerJsonArray!!.getJSONObject(this.position)
                            .getString("ans_ques_answer").equals("C", true) ) {

                        radiobutton_option_c.isChecked = true
                        onResultOptionListener.onResultOption(questionBank.ques_id, "C")

                    } else if( answerJsonArray!!.getJSONObject(this.position)
                            .getString("ans_ques_answer").equals("D", true) ) {

                        radiobutton_option_d.isChecked = true
                        onResultOptionListener.onResultOption(questionBank.ques_id, "D")
                    }

                }

            } catch (e : Exception) {

                Log.i(TAG, e.toString())
            }

            val radiogroup_option = view.findViewById<RadioGroup>(R.id.radiogroup_option) as RadioGroup
            radiogroup_option.setOnCheckedChangeListener { radioGroup, i ->

                when(i) {

                    R.id.radiobutton_option_a -> {

                        onResultOptionListener.onResultOption(questionBank.ques_id, "A")
                    }

                    R.id.radiobutton_option_b -> {

                        onResultOptionListener.onResultOption(questionBank.ques_id, "B")
                    }

                    R.id.radiobutton_option_c -> {

                        onResultOptionListener.onResultOption(questionBank.ques_id, "C")

                    }

                    R.id.radiobutton_option_d -> {

                        onResultOptionListener.onResultOption(questionBank.ques_id, "D")
                    }

                    else -> {

                        Toast.makeText(context, "Wrong option", Toast.LENGTH_LONG).show()
                    }
                }
            }

        } // END OF Objective UI Layout
        else
        {
            val edt_subjective_answer = view.findViewById<EditText>(R.id.edt_subjective_answer) as EditText

            try {

                if( answerJsonArray != null )
                {
                    Log.i(TAG, "Answer: " + answerJsonArray.toString())

                    if( answerJsonArray!!.getJSONObject(this.position)
                            .getString("ans_ques_answer").equals("null", true)) {

                        edt_subjective_answer.setText("")

                        onResultOptionListener.onResultOption(questionBank.ques_id, "null")
                    }
                    else
                    {
                        edt_subjective_answer.setText(answerJsonArray!!.getJSONObject(this.position)
                                .getString("ans_ques_answer"))

                        onResultOptionListener.onResultOption(questionBank.ques_id, edt_subjective_answer.text.toString())

                    }

                }

            } catch (e : Exception) {

                Log.i(TAG, e.toString())
            }

            edt_subjective_answer.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {

                    if( p0!!.isEmpty())
                    {
                        onResultOptionListener.onResultOption(questionBank.ques_id, "null")
                    }
                    else
                    {
                        onResultOptionListener.onResultOption(questionBank.ques_id, p0.toString())
                    }

                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


                }

            })

        }


    }
}