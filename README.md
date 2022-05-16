# Type Game

## Description

This project is about implementing a typing game, author Xinru Li
In This project:
- Use Thread concurrency to let GUI and words will run on separate threads
- Use File IO for getting the word list and updating/storing/displaying the players' records
- Use Graphics to draw all the windows


## Game Rule

After starting the project, press “Start game” to select health points(1~3) and enter the username. 
During the game, words will drop from the screen. Correctly typing the showing word in the input box will eliminate the dropping words and earn scores. (Under faster dropping speed will earn more scores).
- 10 points for level 1 difficulty
- 20 points for level 2 difficulty
- 30 points for level 3 difficulty
- 40 points for level 4 difficulty
- 50 points for level 5 difficulty

Health Point will be reduced by 1 when a word drops to the bottom of the screen.
When the health point becomes 0, the game ends. 

Difficulty level: The difficulty of the game will be the word's dropping speed. The system will automatically increase the difficulty level when the score increases or the player can use the slider to adjust the difficulty level of the number. 
But the player can only select the higher difficulty level than what the system set.


## Run
To run this program, please run the file "src/FinalProject.java".
