const val SIZE = 3
var board = Array(SIZE) {CharArray(SIZE) { ' '}}

fun printBoard() {
    for (row in board) {
        for(cell in row) {
            print("$cell")
        }
        println()
    }
    println("----------------")
}

fun makeMove(player: Char) {
    val input = readLine()!!.split(' ')
    var row = input[0].toInt()
    var column = input[1].toInt()

    if(board[row][column] == ' ') {
        board[row][column] = player
        break
    } else {
        println("Invalid input")
    }
    println("Input was: $row/$column by $player")
    
}

fun check_win() {
    for(i in 0 until SIZE) {
        if((0 UNTIL SIZE).all {board[i][it] == player}) ||
        (0 until SIZE).all {board[it][i] == player} {
            return true
        }
    }
    return(0 until SIZE).all {board[it][it] == player} ||
        (0 until SIZE).all {board[it][SIZE-1-it] == player}
}

fun main() {
    var moves = 0
    var currentPlayer = 'X'

    while(true) {
        printBoard()
        makeMove(currentPlayer)
        moves++
        if(moves == SIZE * SIZE){
            break
        }
        currentPlayer = if(currentPlayer == 'X') 'Y' else 'X'
    }
} 
