package org.be.wordsearch
import org.scalatest._

class WordSearchSpec extends FunSpec with Matchers with LetterSequenceFormatter {
  import org.be.wordsearch._

  describe("Board") {
    val boardString = """ABCDEFG
                        |HIJKLMN
                        |OPQRSCU"""
    val board = new Board(boardString)

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

    it("should get all instances of a letter in the board") {
      board.boardLettersForChar('C') should be(List(Letter('C', 2, 0), Letter('C', 5, 2)))
      board.boardLettersForChar('Z') should be(List())
    }

    it("should get the letter sequence between two letters") {
      val start = board.get(0, 0)
      val end = board.get(2, 2)
      val seq = List(board.get(0, 0), board.get(1, 1), board.get(2, 2))
      board.sequenceBetween(start, end, 3) should be(seq)

      val start1 = board.get(1, 1)
      val end1 = board.get(6, 1)
      val seq1 = List(board.get(1, 1), board.get(2, 1), board.get(3, 1), board.get(4, 1), board.get(5, 1), board.get(6, 1))
      board.sequenceBetween(start1, end1, 6) should be(seq1)
    }

    it("should get valid surrounding letter sequences") {
      val k = board.get(3, 1)
      val seq = board.surroundingSequences(k, 2)
      val strings = seq.map(lettersToString _).toSet
      val expectedStrings = Set("KC", "KD", "KE", "KL", "KS", "KR", "KQ", "KJ")
      strings should be(expectedStrings)

      val seq1 = board.surroundingSequences(k, 3)
      val strings1 = seq1.map(lettersToString _).toSet
      val expectedStrings1 = Set("KJI", "KLM")
      strings1 should be(expectedStrings1)

      val seq2 = board.surroundingSequences(k, 5)
      seq2.isEmpty should be(true)
    }

    it("should get all instance of a letter in the board") {

    }
  }

  describe("WordSearch") {
    val boardString = """ABCGOHG
                        |TPJKLMB
                        |IPIRSPB
                        |PIXGZAQ"""
    val wordList = "hog Pig BBQ pit"
    val search = new WordSearch(boardString, wordList)

    it("should have a board") {
      search.board.isInstanceOf[Board] should be(true)
    }

    it("should have a list of search words") {
      search.words should be(List("HOG", "PIG", "BBQ", "PIT"))
    }

    /*
    it("should get all indexes for a given char in a line") {
      val seq = search.indexesForLetterInLine('P', "IPIRSPB")
      seq should be(Some(List(1, 5)))

      search.indexesForLetterInLine('X', "IPIRSPB") should be(None)
    }

    it("should get all sequences for letters in a line that match the first letter of a word") {

      val result = search.sequencesForWordInLine("PIG", 2)
      result.toSet should be(Set())
    }
    */

    describe("searching") {
      it("should find a valid word starting at a given letter") {
        val word = "HOG"
        val result = search.findWord(word, search.board.get(5, 0))
        result should be(Some(List(Letter('H', 5, 0), Letter('O', 4, 0), Letter('G', 3, 0))))
        lettersToString(result.get) should be("HOG")
      }

      it("should return None if search word is not in puzzle") {
        val result = search.findWord("TOFU", search.board.get(0, 0))
        result should be(None)
      }
    }
  }
}
