package com.connectedminds.sdks.utils

object AppUtils {
    fun formatString(text: String): String? {
        val json = StringBuilder()
        var indentString = ""
        for (element in text) {
            val letter = element
            when (letter) {
                '{', '[' -> {
                    json.append(
                        """
                        
                        $indentString$letter
                        
                        """.trimIndent()
                    )
                    indentString = indentString + "\t"
                    json.append(indentString)
                }
                '}', ']' -> {
                    indentString = indentString.replaceFirst("\t".toRegex(), "")
                    json.append(
                        """
                        
                        $indentString$letter
                        """.trimIndent()
                    )
                }
                ',' -> json.append(
                    """
                    $letter
                    $indentString
                    """.trimIndent()
                )
                else -> json.append(letter)
            }
        }
        return json.toString()
    }
}