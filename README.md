Coder Night Dec 2013 - Word Search
==================================

Puzzle: [http://rubyquiz.com/quiz107.html](http://rubyquiz.com/quiz107.html)

Goal
----

This is the first program I've written in Scala. My goal was to complete the
assignment following the 80/20 rule of writing as much as possible (~ 80%) of
the program in a functional style while using imperative programming
only when necessary (e.g. for input).

Since I'm new to Scala, this first attempt is very likely not close to
idiomatic Scala. As regards functional vs. imperative programming, I will note
the following:

* val (immutable) was used over var (mutable) in all instances except for the class devoted to
  parsing command-line input.

* No mutable collections were used except for StringBuilder which was used only
  for collecting command-line input and formatting the output.

* Functional methods like map and filter were used instead of for loops. The
  only imperative loop was used for command-line input in main().

Test and Run
------------

1. Install Scala: [http://www.scala-lang.org/download/](http://www.scala-lang.org/download/)

2. Install sbt: [http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html](http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html)

3. In the project root, run tests:

    sbt test

4. To manually type in puzzles or cat them to the program:

    cat puzzle_2.txt | sbt run

5. To play around in the REPL:

    sbt console

Learning Scala
--------------

I chose to first complete the [Koans](http://www.scalakoans.org)
before seriously studying any other reference. Other useful resources are:

* [Twitter's Scala school](http://twitter.github.io/scala_school/index.html)

* [The Neophyte's Guide to Scala](http://danielwestheide.com/scala/neophytes.html)

* [Scala for the Impatient](http://horstmann.com/scala/)

* [Scala Cookbook](http://www.amazon.com/Scala-Cookbook-Object-Oriented-Functional-Programming/dp/1449339611)


