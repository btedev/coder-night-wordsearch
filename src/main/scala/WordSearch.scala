package org.be.wordsearch

case class Letter(char: Char, x: Int = 0, y: Int = 0)

trait LetterSequenceFormatter {
  def lettersToString(letters: List[Letter]) = {
    letters.map(_.char).mkString
  }
}

class Board(val boardString: String) {
  val boardLines: List[String] = boardString.stripMargin.split('\n').toList
  val charMatrix: List[List[Char]] = boardLines.map(_.toList)

  val height = charMatrix.length;
  val width = charMatrix.head.length;

  def get(x: Int, y: Int) = Letter(charMatrix(y)(x), x, y)

  // Return all instances of a given character on the board as Letter objects
  def boardLettersForChar(char: Char): List[Letter] = {
    val charRegex = char.toString.r

    boardLines.zipWithIndex.map {
      case(line, yIndex) => charRegex.findAllMatchIn(line).map(_.start).toList.map {
        get(_, yIndex)
      }
    }.filterNot(_.isEmpty).flatten
  }

  // Get letter sequence between two points on the board
  def sequenceBetween(letterA: Letter, letterB: Letter, length: Int) = {
    def nextMove(a: Int, b: Int) = {
      val delta = b - a
      delta match {
        case 0 => a
        case delta if delta > 0 => a + 1
        case _ => a - 1
      }
    }

    def step(letters: List[Letter], lastLetter: Letter): List[Letter] = {
      letters.size match {
        case `length` => letters
        case _ => {
          val l = get(nextMove(lastLetter.x, letterB.x), nextMove(lastLetter.y, letterB.y))
          step(letters ::: List(l), l)
        }
      }
    }

    step(List(letterA), letterA)
  }

  // Get letter sequences (potential words) of a given length
  // in a radial pattern arround a given letter.
  def surroundingSequences(letter: Letter, length: Int) = {
    def validCoordinates(expr: (Int, Int)) = expr match {
      case (x, y) if x < 0 || y < 0 => false
      case (x, _) if x >= width => false
      case (_, y) if y >= height => false
      case (letter.x, letter.y) => false
      case _ => true
    }

    // Determine the valid end letters
    // (i.e. the "to" letters for our "from" letter argument)
    val farPoints = Range(-1, 1).inclusive.map {
      xDir => Range(-1, 1).inclusive.map {
        yDir => (letter.x + (xDir * (length - 1)), letter.y + (yDir * (length - 1)))
      }
    }.flatten.filter(validCoordinates _) // filter out invalid coordinates

    // Get the corresponding letter sequences between from and to
    farPoints.map(t => get(t._1, t._2)).map(u => sequenceBetween(letter, u, length))
  }
}

class WordSearch(val boardString: String, val wordList: String) extends LetterSequenceFormatter {
  val board = new Board(boardString)

  val words = wordList.split(" ").map(_.toUpperCase)

  // Search for a word starting at a given letter.
  // Note: Option return types wrap results in Some() or return None for empty
  def findWord(word: String, start: Letter): Option[List[Letter]] = {
    val seqs = board.surroundingSequences(start, word.size)
    seqs.find(lettersToString(_) == word)
  }

  /*
  def indexesForLetterInLine(char: Char, line: String): Option[List[Int]] = {
    val indexes = char.toString.r.findAllMatchIn(line).map(_.start).toList
    indexes.isEmpty match {
      case true => None
      case false => Some(indexes)
    }
  }

  def sequencesForWordInLine(word: String, lineIndex: Int) = {
    val firstChar = word.head
    val wordLen = word.size

    val indexes = indexesForLetterInLine(word.head, board.boardLines(lineIndex))
    indexes match {
      case None => None
      case Some(_) => {
        indexes.get.map {
          xIndex =>
            val letter = board.get(xIndex, lineIndex)
            board.surroundingSequences(letter, wordLen)
        }
      }
    }
  }

  // Search the entire board for a word match
  def findWord(word: String): Option[List[Letter]] = {
    board.boardLines.find {
    }
    //"B".r findAllMatchIn "ABCBD" map(_.start).toList
  }
  */
}

/*
class WordSearchLoader {

}
*/
