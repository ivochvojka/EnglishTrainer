package cz.ich.englishtrainer.model

import com.google.firebase.firestore.Exclude
import java.util.*

data class Word(var lan1: String?,
                var lan2: String?,
                var desc: String? = null,
                var kn: Int = Knowledge.DONT_KNOW.ordinal,
                var updated: Date? = null,
                @get:Exclude var isRevealed: Boolean = false,
                @get:Exclude var path: String? = null
                ) {
    constructor() : this(null, null)
}