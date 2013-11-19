package org.be.wordsearch
import org.scalatest._

class WordSearchSpec extends FunSpec with Matchers {
  import org.be.wordsearch._

  describe("Board") {
    val boardString = """ABCDEFG
                        |HIJKLMN
                        |OPQRSTU"""
    val board = new Board(boardString)

    describe("as a matrix") {
      it("should define height") {
        board.height should be(3)
      }

      it("should define width") {
        board.width should be(7)
      }

      it("should get a letter by coordinates") {
        board.get(0, 0).char should be('A')
        board.get(4, 1).char should be('L')
        board.get(6, 2).char should be('U')
      }

      it("should get surrounding letters") {
        val upLeftLetters = board.surroundingLetters(board.get(0, 0)).map(_.char).toSet
        upLeftLetters should be(Set('B', 'I', 'H'))

        val midLetters = board.surroundingLetters(board.get(3, 1)).map(_.char).toSet
        midLetters should be(Set('J', 'C', 'D', 'E', 'L', 'S', 'R', 'Q'))

        val downRightLetters = board.surroundingLetters(board.get(6, 2)).map(_.char).toSet
        downRightLetters should be(Set('T', 'M', 'N'))
      }
    } // board as a matrix

    describe("as a single string") {
      it("should also have a single-line board") {
        board.singleLineBoard should be("ABCDEFGHIJKLMNOPQRSTU")
      }

      it("should translate single-line board indexes to matrix coordinates") {
        board.coordinateForSingleLineIndex(0) should be(Coordinate(0, 0)) // A
        board.coordinateForSingleLineIndex(6) should be(Coordinate(6, 0)) // G
        board.coordinateForSingleLineIndex(7) should be(Coordinate(0, 1)) // H
        board.coordinateForSingleLineIndex(11) should be(Coordinate(4, 1)) // L
        board.coordinateForSingleLineIndex(20) should be(Coordinate(6, 2)) // U
      }
    }
  }

  describe("WordSearch") {
    val boardString = """ABCGOHG
                        |TPJKLMB
                        |IPIRSTB
                        |PWXGZAQ"""
    val wordList = "hog Pig BBQ pit"
    val wordSearch = new WordSearch(boardString, wordList)

    it("should have a board") {
      wordSearch.board.isInstanceOf[Board] should be(true)
    }

    it("should have a list of search words") {
      wordSearch.words should be(List("HOG", "PIG", "BBQ", "PIT"))
    }

    it("should have a set of first letters of all search words") {
      wordSearch.uniqHeads should be(Set('H', 'P', 'B'))
    }

    ignore("should create an index of all instances of search word first letters") {

    }
  }
}
