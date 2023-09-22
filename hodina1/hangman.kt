import java.io.File //naimportovanie kniznice na spracovanie suborov

val hangman = listOf("""
    ----
    |   |
    |   °
    |  /|\
    |  / \
    |
    ------
""".trimIndent(),"""
    ----
    |   |
    |   °
    |  /|\
    |  / 
    |
    ------
""".trimIndent(),"""
    ----
    |   |
    |   °
    |  /|\
    |   
    |
    ------
""".trimIndent(),"""
    ----
    |   |
    |   °
    |  /|
    |  
    |
    ------
""".trimIndent(),"""
    ----
    |   |
    |   °
    |  /
    |  
    |
    ------
""".trimIndent(),"""
    ----
    |   |
    |   °
    |  
    |  
    |
    ------
""".trimIndent(),"""
    ----
    |   |
    |   
    | 
    |  
    |
    ------
""".trimIndent(),"""
    ----
    |   
    |
    |
    |
    |
    ------
""".trimIndent())


fun main() { // fun is function
    val word = File("words.txt").readLines().random() //val word je premennna s nazvom word
    var status = ".".repeat(word.length).toCharArray() //toCharArray do pola znakov
    var life = hangman.lastIndex

    //println(word)
    //println(status)
    //println(hangman[hangman.lastIndex])
    //println(life)

    while(life > 0 && String(status) != word){
        println(hangman[life])
        println(status)

        var input = readLine()!!
        //println(input)
        //life--

        if(input.length > 1){
            if(input==word) {
                status = input.toCharArray()
            } else life--
        } else if( input in word ) {//v kotline je contains vyraz TAKTO: INPUT IN WORD
            word.forEachIndexed { index, c ->
                if (c in input ){
                    status[index] = c
                }
            }
        } else life--
    }
    
    if (life >0) println("Hra ukoncena, VYHRAL SI KAMO")
    else println("Hra ukoncena prehral si. Slovo bolo: $word")
}