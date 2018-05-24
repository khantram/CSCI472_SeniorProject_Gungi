# Gungi: Bringing a Fictional Board Game to Life

Gungi is a fictional, two-player strategy board game that is played by the citizens of East Gorteau and other major characters in [Hunter x Hunter](https://en.wikipedia.org/wiki/Hunter_%C3%97_Hunter)—a Japanese comic and animated series written by Yoshihiro Togashi. This project is a Windows application of the fictional board game written in Java.

- [INSTALLATION](#installation)
- [DESCRIPTION](#description)
- [RULESET](#ruleset)
- [SCREENSHOTS](#screenshots)

## INSTALLATION

Using a command-line navigate to the project directory and type the following lines to compile and run the project.

    javac -d bin src/tram/model/*.java src/tram/controller/*.java src/tram/view/*.java src/tram/*.java
    java -cp bin tram.Main

## DESCRIPTION

**gungi** is Windows application for a fictional, two-player strategy board game—in the same family as chess and shogi—with a unique use of the third-dimension. This mechanic allows players to stack their pieces on any other pieces, including the opposing player’s, and form towers. The added dimensionality along with other uncommon features, such as player-decided starting placements and placing pieces into play mid-match, provides players with a multitude of strategies to checkmate the opposing Marshal (King) or defend their own. To appeal to a broad audience—especially those outside of the fictional series’ fanbase—a large focus of the application is in good GUI and UX design. With continued user testing and feedback, the application aims to become the standard platform for playing Gungi.

## RULESET

See [RULESET.md](RULESET.md)
