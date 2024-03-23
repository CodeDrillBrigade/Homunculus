package org.cdb.labwitch.models.types

@JvmInline
value class ShortText(val text: String) {
    init {
        require(text.length <= 300) {
            "A ShortText cannot be greater than 300 chracters"
        }
    }
}
