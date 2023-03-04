package com.jigar.me.data.model

import com.google.gson.annotations.SerializedName

data class PojoAbacus(
    @SerializedName("id") var id: String = "",
    @SerializedName("q0") var q0: String = "",
    @SerializedName("q1") var q1: String = "",
    @SerializedName("q2") var q2: String? = null,
    @SerializedName("q3") var q3: String? = null,
    @SerializedName("q4") var q4: String? = null,
    @SerializedName("q5") var q5: String? = null,
    @SerializedName("h1") var h1: String? = null,
    @SerializedName("h2") var h2: String? = null,
    @SerializedName("h3") var h3: String? = null,
    @SerializedName("h4") var h4: String? = null,
    @SerializedName("h5") var h5: String? = null,
    @SerializedName("s1") var s1: String = "",
    @SerializedName("s2") var s2: String? = null,
    @SerializedName("s3") var s3: String? = null,
    @SerializedName("s4") var s4: String? = null,
    @SerializedName("s5") var s5: String? = null
//    @SerializedName("Ans") var ans: String = "",
//    @SerializedName("abacus_id") var abacusId: String = "",
    ){
    fun getAnswer() : Int{
        var answer = 0
        if (s1 == "+"){
            answer = q0.toInt() + q1.toInt()
        }else if (s1 == "-"){
            answer = q0.toInt() - q1.toInt()
        }
        if (!s2.isNullOrEmpty()){
            if (s2 == "+"){
                answer += (q2 ?: "0").toInt()
            }else if (s2 == "-"){
                answer -= (q2 ?: "0").toInt()
            }

            if (!s3.isNullOrEmpty()){
                if (s3 == "+"){
                    answer += (q3 ?: "0").toInt()
                }else if (s3 == "-"){
                    answer -= (q3 ?: "0").toInt()
                }

                if (!s4.isNullOrEmpty()){
                    if (s4 == "+"){
                        answer += (q4 ?: "0").toInt()
                    }else if (s4 == "-"){
                        answer -= (q4 ?: "0").toInt()
                    }

                    if (!s5.isNullOrEmpty()){
                        if (s2 == "+"){
                            answer += (q5 ?: "0").toInt()
                        }else if (s5 == "-"){
                            answer -= (q5 ?: "0").toInt()
                        }
                    }
                }

            }
        }
        return answer
    }
}