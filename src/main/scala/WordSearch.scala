package org.be.wordsearch

case class Letter(char: Char, x: Int = 0, y: Int = 0)

trait LetterSequenceFormatter {
  def lettersToString(letters: List[Letter]) = {
    letters.map(_.char).mkString
  }
}

class Board(val boardString: String) {
  val boardLines: List[String] = boardString.stripMargin.filterNot(_ == ' ').split('\n').toList
  val charMatrix: List[List[Char]] = boardLines.map(_.toList)

  val height = charMatrix.length;
  val width = charMatrix.head.length;

  def get(x: Int, y: Int) = Letter(charMatrix(y)(x), x, y)

  // Return all instances of a given character on the board as Letter objects
  def boardLettersForChar(char: Char): List[Letter] = {
    val charRegex = char.toString.r

    boardLines.zipWithIndex.map {
      case(line, yIndex) => charRegex.findAllMatchIn(line).map {
        charMatch => get(charMatch.start, yIndex)
      }
    }.filterNot(_.isEmpty).flatten
  }

  // Letter sequence between two points on the board
  def sequenceBetween(letterA: Letter, letterB: Letter, length: Int) = {
    def nextMove(a: Int, b: Int) = a + (b compare a)

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

  // Returns letter sequences (potential words) of a given length
  // in a radial pattern arround a given letter.
  def surroundingSequences(letter: Letter, length: Int) = {
    def validCoordinates(expr: (Int, Int)) = expr match {
      case (x, y) if x < 0 || y < 0 => false
      case (x, _) if x >= width     => false
      case (_, y) if y >= height    => false
      case (letter.x, letter.y)     => false
      case _                        => true
    }

    // Determine the valid end letters
    // (i.e. the "to" letters for our "from" letter argument)
    val farPoints = Range(-1, 1).inclusive.map {
      xDir => Range(-1, 1).inclusive.map {
        yDir => (letter.x + (xDir * (length - 1)), letter.y + (yDir * (length - 1)))
      }
    }.flatten.filter(validCoordinates _) // filter out invalid coordinates

    // Get the corresponding letter sequences between from and to
    farPoints.map(t => get(t._1, t._2)).map(sequenceBetween(letter, _, length))
  }
}

class WordSearch(val boardString: String, val wordList: String) extends LetterSequenceFormatter {
  val board = new Board(boardString)

  val words = wordList.split(" ").map(_.toUpperCase)

  // Map search word first letters to Letter instances on board
  val letterMap = {
    val firstLetters = words.map(_.head).toSet.toList
    val letterInstances = firstLetters.map(board.boardLettersForChar(_))
    (firstLetters zip letterInstances).toMap
  }

  // Search for a word starting at a given letter.
  // Note: Option return types wrap results in Some() or return None for empty.
  def findWordAtLetter(word: String, letter: Letter): Option[List[Letter]] = {
    val seqs = board.surroundingSequences(letter, word.size)
    seqs.find(lettersToString(_) == word)
  }

  // Search the entire board for a word match. May find multiple matches.
  def findWord(word: String): List[List[Letter]] = {
    letterMap(word.head).map(findWordAtLetter(word, _)).filter(_ != None).map(_.get)
  }

  def findAll: List[List[Letter]] = {
    words.map(findWord _).flatten.toList
  }

  def format(words: List[List[Letter]]): String = {
    // All-plus board
    val builder = new StringBuilder("+" * board.height * board.width)

    // Substitute letters from found words
    words.map {
      word => word.map {
        letter =>  {
          val offset = (letter.y * board.width) + letter.x
          builder.replace(offset, offset + 1, letter.char.toString)
        }
      }
    }

    // Add spaces and newlines
    builder.grouped(board.width).map(_.mkString("", " ", "\n")).mkString
  }
}

class WordSearchInput

object WordSearchInput {
  val matrix = new StringBuilder
  var blankLine = false

  def apply(line: String): Option[String] = {
    line match {
      case(line) if line.size > 0 && !blankLine => {
        matrix.append(line + "\n")
        None
      }
      case(line) if line.size == 0 => {
        blankLine = true
        None
      }
      case _ => {
        val search = new WordSearch(matrix.toString, line)
        val soln = search.findAll
        Some(search.format(soln))
      }
    }
  }

  def main(args: Array[String]) {
    var last: Option[String] = None

    do {
      last = apply(readLine())
    } while(last == None)

    println(last.get)
  }
}

