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

    it("should have a Map of letters corresponding to first letters of search words") {
      search.letterMap.contains('H') should be(true)
      search.letterMap('H').head should be(Letter('H', 5, 0))

      search.letterMap('P').size should be(4)

      search.letterMap.contains('W') should be(false)
    }

    describe("searching") {
      it("should find a valid word starting at a given letter") {
        val result = search.findWordAtLetter("HOG", search.board.get(5, 0))
        result should be(Some(List(Letter('H', 5, 0), Letter('O', 4, 0), Letter('G', 3, 0))))
        lettersToString(result.get) should be("HOG")
      }

      it("should return None if search word is not found starting at a given letter") {
        val result = search.findWordAtLetter("TOFU", search.board.get(0, 0))
        result should be(None)
      }

      it("should find a valid word anywhere in the puzzle") {
        search.findWord("HOG").head should be(List(Letter('H', 5, 0), Letter('O', 4, 0), Letter('G', 3, 0)))
        search.findWord("BBQ").head should be(List(Letter('B', 6, 1), Letter('B', 6, 2), Letter('Q', 6, 3)))
      }

      it("should return empty if word is not found on board") {
        search.findWord("BAT") should be(List())
      }
    }

    describe("solution") {
      val boardString = """U E W R T R B H C D
                           |C X G Z U W R Y E R
                           |R O C K S B A U C U
                           |S F K F M T Y S G E
                           |Y S O O U N M Z I M
                           |T C G P R T I D A N
                           |H Z G H Q G W T U V
                           |H Q M N D X Z B S T
                           |N T C L A T N B C E
                           |Y B U R P Z U X M S
                           |"""
      val words = "ruby dan rocks matz"
      val search = new WordSearch(boardString, words)
      val soln = search.findAll

      it("should find all search words") {
        soln.size should be(5) // ruby found twice in puzzle
      }

      it("should format correctly") {
        val expected = """+ + + R + + + + + +
                          |+ + + + U + + + + +
                          |R O C K S B + + + +
                          |+ + + + + + Y + + +
                          |+ + + + + + + + + M
                          |+ + + + + + + D A N
                          |+ + + + + + + T + +
                          |+ + + + + + Z + + +
                          |+ + + + + + + + + +
                          |Y B U R + + + + + +
                          |""".stripMargin

        val formatted = search.format(soln)
        formatted should be(expected)
      }
    }
  }

  describe("WordSearchInput") {
    it("should solve a puzzle entered line-by-line") {
      WordSearchInput("ABCGOHG") should be(None)
      WordSearchInput("TPJKLMB") should be(None)
      WordSearchInput("IPIRSPB") should be(None)
      WordSearchInput("PIXGZAQ") should be(None)
      WordSearchInput("") should be(None)
      val result = WordSearchInput("hog Pig BBQ pit")
      result.get.split("\n").head should be("+ + + G O H +")
    }
  }
}

