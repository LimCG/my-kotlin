package com.fyp3.quiz

/**
 * Created by limcg on 28/12/2017.
 */
class Constants {

    companion object {

//        private val BASE_URL = "https://fypfypmmu.000webhostapp.com/"

        val BASE_URL = "http://188.166.186.198/quiz3/"
        val UPLOAD_PDF_URL = BASE_URL + "upload.php"
        val GET_PDF_LIST = BASE_URL + "list_pdf.php"
        val REMOVE_PDF = BASE_URL + "remove_pdf.php"
        val LOGIN_URL = BASE_URL + "user_login.php"
        val GET_EXERCISE_QUESTION_URL = BASE_URL + "list_exercise_ques.php"
        val GET_TRIAL_QUESTION_URL = BASE_URL + "list_trial_ques.php"
        val GET_FINAL_QUESTION_URL = BASE_URL + "list_final_ques.php"
        val GET_TOPIC_LIST = BASE_URL + "list_topic.php"
        val GET_SUBTOPIC_LIST = BASE_URL + "list_subtopic.php"
        val CHECK_ANSWER_URL = BASE_URL + "check_answer.php"
        val LIST_TRIAL_SCORE = BASE_URL + "list_trial_score.php"
        val LIST_FINAL_SCORE = BASE_URL + "list_final_score.php"
        val LIST_EXERCISE_SCORE = BASE_URL + "list_exercise_score.php"
        val REGISTER_URL = BASE_URL + "register.php"
        val LIST_STUDENT_URL = BASE_URL + "list_student.php"

        val PARENT_LIST_TRIAL_SCORE = BASE_URL + "parent_list_trial_score.php"
        val PARENT_LIST_FINAL_SCORE = BASE_URL + "parent_list_final_score.php"
        val PARENT_LIST_EXERCISE_SCORE = BASE_URL + "parent_list_exercise_score.php"

    }

}