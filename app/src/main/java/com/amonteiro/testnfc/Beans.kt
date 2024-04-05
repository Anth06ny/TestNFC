package com.amonteiro.testnfc

fun main() {
    val randomName = RandomName()
    randomName.add("bobby")
    repeat(10) {
        println(randomName.next() + " ")
    }
}

const val LONG_TEXT = """Le Lorem Ipsum est simplement du faux texte employé dans la composition
    et la mise en page avant impression. Le Lorem Ipsum est le faux texte standard
    de l'imprimerie depuis les années 1500"""

data class PictureBean(val id:Int, val url: String, val title: String, val longText: String, var favorite : Boolean = false)

//jeu de donnée
val pictureList = arrayListOf(PictureBean(1, "https://picsum.photos/200", "ABCD", LONG_TEXT),
    PictureBean(2, "https://picsum.photos/201", "BCDE", LONG_TEXT),
    PictureBean(3, "https://picsum.photos/202", "CDEF", LONG_TEXT),
    PictureBean(4, "https://picsum.photos/203", "EFGH", LONG_TEXT)
)

class RandomName {

    private val list = arrayListOf("Toto", "Tata", "titi")
    private var oldValue = ""

    fun add(name: String?) = if (!name.isNullOrBlank() && name !in list) list.add(name) else false

    fun next() = list.random()

    fun nextDiff() = list.filter { it != oldValue }
        .random()
        .also { oldValue = it }

    fun next2() = Pair(nextDiff(), nextDiff())

}


data class CarBean(var marque: String = "", var model: String? = null) {
    var color = ""

    fun print() = println("$marque $model $color")


}