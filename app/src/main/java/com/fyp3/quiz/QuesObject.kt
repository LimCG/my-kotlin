package com.fyp3.quiz

import java.io.Serializable

/**
 * Created by limcg on 02/01/2018.
 */
class QuesObject : Serializable {

    var ques_id : Int = 0
    var isObjective : Boolean = false

    lateinit var ques_topic : String
    lateinit var ques_subTopic : String

    lateinit var ques_title : String
    lateinit var ques_content : String

    lateinit var option_a : String
    lateinit var option_b : String
    lateinit var option_c : String
    lateinit var option_d : String

    lateinit var objective_answer : String
    lateinit var subjective_answer : String

    var isCorrectAnswer : Int = 0
    lateinit var your_answer : String


}