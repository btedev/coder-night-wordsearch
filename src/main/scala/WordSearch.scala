package org.be.wordsearch

import scala.collection.mutable

case class Coordinate(x: Int = 0, y: Int = 0)
case class Letter(char: Char, coordinate: Coordinate)

class Board(val boardString: String) {
  val charMatrix: List[List[Char]] = boardString.stripMargin.split('\n').toList.map(_.toList)

  val height = charMatrix.length;
  val width = charMatrix.head.length;

  def get(x: Int, y: Int) = Letter(charMatrix(y)(x), Coordinate(x, y))

  def rangeForX(x: Int) = Range(if (x > 0) x-1 else 0, if (x < width-1) x+1 else x).inclusive
  def rangeForY(y: Int) = Range(if (y > 0) y-1 else 0, if (y < height-1) y+1 else y).inclusive

  def surroundingLetters(letter: Letter) = {
    rangeForX(letter.coordinate.x).map {
      x => rangeForY(letter.coordinate.y).map(y => get(x, y))
    }.flatten.filterNot(_ == letter)
  }

  val singleLineBoard = boardString.stripMargin.filterNot(_ == '\n')

  def coordinateForSingleLineIndex(idx: Int): Coordinate = {
    val y = idx / width
    val x = idx - (y * width)
    Coordinate(x, y)
  }
}

class WordSearch(val boardString: String, val wordList: String) {
  val board = new Board(boardString)

  val words = wordList.split(" ").map(_.toUpperCase)

  val uniqHeads = words.map(_.head).toSet

  /*
  val letterIndex: Map[Char, List[Coordinate]] = {

  }
  */

  /*
  val letterIndex = {
    var letterMap = mutable.Map[Char, List[Letter]]()
    //letterMap += 'C' -> List(Letter('C'))
    charMatrix.foreach {
      line => line.foreach {
        char => if uniqHeads.contains(char) 
        }
      }
    }
  }
*/

  /*
  def findWord(word: String) = {
  }
  */
}

/*
class WordSearchLoader {

}
*/
