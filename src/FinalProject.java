import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Graphics;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

// This project is about implementing a typing game, author Xinru Li
// To run this program, please run the file "FinalProject.java"
// In This project:
// Use Thread concurrency to let GUI and words will run on separate threads
// Use File IO for getting word list and updating/storing/displaying the players' records
// Use Graphics to draw all the windows

public class FinalProject {
    static MyFrame MyFrame; // Main page for entry
    static playGame playgame; // Starting Playing Game Page
    static ArrayList<String> words; // word list for typing game
    static String username;
    static long starttimestamp;
    static long endtimestamp;
    static long startpausetime;
    static long endpausetime;
    static long totalpausetime;
    static String durationtime;
    static int highestscore;

    public static void main(String[] args) {
        // Start game
        words = getWords("src/words.txt");
        MyFrame = new MyFrame("Typing Game");
        playgame = new playGame();
    }

    // get words from txt file (file io)
    public static ArrayList<String> getWords(String filePath){
        words = new ArrayList<String>();
        BufferedReader bufferedReader = null;
        InputStreamReader read = null ;
        try {
            File f = new File(filePath);
            read = new InputStreamReader(new FileInputStream(f));
            bufferedReader = new BufferedReader(read);
            String oneline = null;
            // Get each line in txt file to get words
            while((oneline = bufferedReader.readLine()) != null){
                words.add(oneline);
            }
            read.close();
        }
        catch (Exception e) {
            System.out.println("Error when reading" + filePath);
        }
        return words;
    }

    // Main page for playing game
    static class MyFrame extends JFrame {
        static int width = 500;
        static int height = 700;
        public MyFrame(String text) {
            super(text);
            this.setSize(width, height);
            this.getLayeredPane().setLayout(null);
            this.setLayout(null);
            this.setLocationRelativeTo(null);
            this.setResizable(false);
            // Set background image
            JPanel imageJP = (JPanel) this.getContentPane();
            imageJP.setOpaque(false);
            imageJP.setBounds(0, 0, width, height);
            imageJP.setLayout(null);
            ImageIcon image = new ImageIcon("src/bg.jpg");
            JLabel label = new JLabel(image);
            label.setBounds(0, 0, this.getWidth(), this.getHeight());
            image.setImage(image.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_DEFAULT));
            this.getLayeredPane().add(label, Integer.valueOf(Integer.MIN_VALUE));

            // Create another thread for animation
            Title animationTitle = new Title(); // make a dropping animationTitle
            this.add(animationTitle);
            Thread t = new Thread(animationTitle); // Add the title panel to a thread
            t.start();

            // Set three buttons below
            // Set button of starting game
            JButton playjb = new JButton("Start Game");
            playjb.setFont(new Font("Arial", Font.PLAIN, 30));
            playjb.setBounds(width / 3, height * 2 / 6, width / 3, 50);
            playjb.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    SetGame setgame = new SetGame();
                }
            } );
            this.add(playjb);

            // Set button of rules
            JButton rulejb = new JButton("Rules");
            rulejb.setFont(new Font("Arial", Font.PLAIN, 30));
            rulejb.setBounds(width / 3, height * 3 / 6, width / 3, 50);
            // Set a new frame for introducing rules
            JFrame RuleFrame = new JFrame("Rules");
            JLabel ruletext = new JLabel("<html><body>"+"Rules：Press 'Start game' to select health points(1~3) and enter name. During game, words will drop from the screen. Correctly typing the showing word in the input box will eliminate the dropping words and earn scores. (Under faster dropping speed will earn more scores)." +
                    "<br>"+"<br>"+"Health Point will reduced by 1 when a word drops to the bottom of the screen."+"<br>"+"When health point becomes 0, game ends." +"<br>"+"<br>"+
                    "Difficulty：The difficulty of the game(word's dropping speed) will automatically increase as the score increases, or you can use the slider to adjust the difficulty level of the number."+"<br>"+"<br>"+
                    "Good Luck :)"+"</body></html>");
            ruletext.setFont(new Font("Arial", Font.PLAIN, 20));
            RuleFrame.add(ruletext);
            RuleFrame.setSize(2 * width - 100, height / 2);
            RuleFrame.setResizable(false);
            RuleFrame.setLocationRelativeTo(null);
            // Show rule's frame by clicking 'Rules' button
            rulejb.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Enter rules' page
                    RuleFrame.setVisible(true);
                }
            } );
            this.add(rulejb);

            // Set button of exit
            JButton exitjb = new JButton("Exit");
            exitjb.setFont(new Font("Arial", Font.PLAIN, 30));
            exitjb.setBounds(width / 3, height * 4 / 6, width / 3, 50);
            exitjb.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Exit
                    System.exit(0);
                }
            } );
            this.add(exitjb);

            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setVisible(true);
        }
    }

    // Setgame --> select HP and enter username
    static class SetGame extends JFrame {
        // if the first time start the game --> playbefore = false, otherwise true,
        static Boolean playbefore = false;
        SetGame() {
            this.setTitle("Choose HP + Enter Name");
            this.setSize(330, 180);
            this.setLocationRelativeTo(null);
            this.setAlwaysOnTop(true);
            this.setResizable(false);
            this.setLayout(null);
            this.makesetting();
            this.setVisible(true);
        }
        // set HP buttons and set username
        void makesetting() {
            JPanel setgameJP = new JPanel();
            // Get HP
            ButtonGroup healthpoints = new ButtonGroup();
            JRadioButton one = new JRadioButton("1", true);
            JRadioButton two = new JRadioButton("2", false);
            JRadioButton three = new JRadioButton("3", false);
            healthpoints.add(one);
            healthpoints.add(two);
            healthpoints.add(three);
            setgameJP.setBounds(0, 0, getWidth(), getHeight() - 60);
            setgameJP.add(one);
            setgameJP.add(two);
            setgameJP.add(three);
            this.add(setgameJP);

            // Get username
            JTextField getusername = new JTextField(20);
            setgameJP.add(getusername);
            JLabel textname = new JLabel("Enter username, or default username is User");
            setgameJP.add(textname);

            // start game button
            JButton startjb = new JButton("Start");
            startjb.setBounds(getWidth() / 3 + 20, setgameJP.getHeight(), 60, 25);
            this.add(startjb);

            startjb.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Set username, if username == "", username = "User"
                    username = getusername.getText();
                    if (username.equals("")){username = "User";}
                    System.out.println("Good Luck " + username);

                    // Set Health Point
                    if (one.isSelected()) { playgame.life = 1;}
                    else if (two.isSelected()) { playgame.life = 2;}
                    else { playgame.life = 3; }

                    // implement replay - to reset previous values
                    playgame.templife =playgame.life;
                    // if first time playing game
                    if (!playbefore) {
                        // Call the begin function to set some new variables and thread
                        playgame.begin();
                        playbefore = true;
                    } else {
                        // reset to the initial values
                        playgame.injp.reset_func();
                        playgame.GameJF.setVisible(true);
                    }
                    dispose();
                }
            });
        }
    }

    // playGame method
    public static class playGame {
        JFrame GameJF = new JFrame("Typing Game");
        static int width = 500;
        static int height = 700;
        // 4 number of words drop in one line
        static int N = 4;
        static int x[] = new int[N];
        static int y[] = new int[N];
        static Color wordcolor[] = new Color[N];
        static String[] num = new String[N];
        // default hp
        static int life = 1;
        static int templife = 1;
        Image hpicon = Toolkit.getDefaultToolkit().getImage("src/life.jpg");
        // did not finish one round, have not had one round time yet
        Boolean getstarttime = false;
        Boolean getendtime = false;
        String gametime = "";
        long totalpausetime = 0;
        // default difficulty
        static int difficult_level = 1;
        static int chosendifficult_level = 1;
        Boolean pauseflag = false;
        Boolean startflag = false;
        Boolean Gameoverflag = false;
        JTextField input = new JTextField(20);
        JLabel showtext = new JLabel();
        int score;
        int count = 3;
        int correctcount;
        WordsDropPanel mp;
        sidefunctionsJP injp;

        // first time playing game - later will have reply function
        public void begin() {
            // get four random words from the list
            this.get_random_word();
            GameJF.setSize(width, height);
            GameJF.setResizable(false);
            GameJF.setLocationRelativeTo(null);
            GameJF.getLayeredPane().setLayout(null);
            // dropping words panel
            mp = new WordsDropPanel();
            // side functions panel
            injp = new sidefunctionsJP();
            JPanel imgPanel = (JPanel) GameJF.getContentPane();
            imgPanel.setOpaque(false);
            imgPanel.setBounds(0, 0, width, height);
            imgPanel.setLayout(null);
            // set background
            ImageIcon icon = new ImageIcon("src/bg.jpg");
            JLabel label = new JLabel(icon);
            label.setBounds(0, 0, GameJF.getWidth(), GameJF.getHeight());
            icon.setImage(icon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_DEFAULT));
            GameJF.getLayeredPane().add(label, Integer.valueOf(Integer.MIN_VALUE));
            GameJF.getContentPane().add(mp);
            GameJF.getContentPane().add(injp, Integer.valueOf(Integer.MAX_VALUE));

            // add new thread for dropping words
            Thread t = new Thread(mp);
            t.start();

            GameJF.setVisible(true);
            input.requestFocus();
        }

        // randomly get four words and set x y coordinates
        public static void get_random_word() {
            Random r = new Random();
            int totalwordnum = words.size();
            int i, j;
            for (i = 0; i < N; i++) {
                num[i] = words.get(r.nextInt(totalwordnum)).toString();
                // set x y coordinates
                x[i] = (int) (0.1 * width + i * 0.20 * width);
                y[i] = 50;
                Color randcolor = new Color(r.nextInt(255),r.nextInt(255),r.nextInt(255));
                wordcolor[i] = randcolor;
            }
        }

        // words dropping panel
        class WordsDropPanel extends JPanel implements Runnable {
            long time;
            int height = 500;
            int width = GameJF.getWidth();

            public WordsDropPanel() {
                setOpaque(false);
                setBounds(0, 0, width, height);
                setBackground(Color.BLACK);
            }

            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                while (true) {
                    // count down - 3,2,1,Go!
                    if (!startflag) {
                        input.setEditable(false);
                        repaint();
                        try {
                            Thread.sleep(1000);
                            count --;
                            if (count < 0) {
                                startflag = true;
                                // record the playerstart time
                                getStartTime();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //start the game, record the playtimestamp time
                    else {
                        repaint();
                        try {
                            Thread.sleep(40 - difficult_level * 5);
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            public void getStartTime(){
                if (!getstarttime){
                    starttimestamp = new Date().getTime();
                    getstarttime = true;
                }
                // else{do nothing}
            }

            public String calculateGameTime(){
                // record the playerend time
                if (!getendtime){
                    endtimestamp = new Date().getTime();
                    String gametime = String.valueOf( (endtimestamp - starttimestamp - totalpausetime) / 1000);
                    System.out.println("This round time is: " + gametime + " seconds!");
                    getendtime = true;
                }
                return gametime;
            }

            // painting dropping words
            public void paint(Graphics g) {
                super.paint(g);
                // check whether game over
                if (Gameoverflag) {
                    calculateGameTime();
                    g.setColor(Color.RED);
                    g.setFont(new Font("Arial", Font.BOLD, 30));
                    g.drawString("Game Over", width / 3, height / 2);
                    g.drawString("Your Score:"+score,width / 3-15,height/2+35);
                    g.drawString("Highest Score:"+highestscore,width / 3-30,height/2+70);
                    // Empty input box and cannot enter anymore
                    input.setText("");
                    input.setEditable(false);
                }
                else {
                    // if in count down, not in the game yet
                    if (!startflag) {
                        g.setFont(new Font("Arial", Font.PLAIN, 50));
                        g.setColor(Color.RED);
                        if (count > 0) {
                            input.requestFocus();
                            g.drawString(String.valueOf(count), width / 2, height / 2);
                        }
                        else {
                            // count to 0, start the game
                            g.drawString("Go!", width / 2, height / 2);
                            input.setEditable(true);
                        }
                    }
                    // during game
                    else {
                        // set word's default style
                        g.setFont(new Font("Arial", Font.PLAIN, 20));
                        g.setColor(Color.white);
                        for (int i = 0; i < N; i++) {
                            g.setColor(wordcolor[i]);
                            g.drawString(num[i], x[i], y[i]);
                        }
                        // if in pause
                        if (pauseflag) {
                            g.setColor(Color.white);
                            g.setFont(new Font("Arial", Font.BOLD, 30));
                            g.drawString("Pause...", width / 4, height / 2);
                            g.setFont(new Font("Arial", Font.BOLD, 20));
                            g.drawString("Press space bar to continue...", width / 4, height / 2 + 30);
                        }
                        else {
                            // If not in pause,
                            // every time the ordinate increases by 1
                            // the bottom health drops by 1
                            // update all ordinates
                            for (int i = 0; i < N; i++) {
                                y[i] = y[i] + 1;
                                // if word drop till the bottom, health point -= 1
                                if (y[i] > getHeight() && templife>0) {
                                    templife--;
                                    for(int j = 0; j < N; j++){
                                        y[j]=50;
                                    }
                                }
                            }
                            // check whether game over and update record.txt
                            if(templife == 0){
                                Gameoverflag = true;
                                updaterecordfile();
                            }

                            // draw game time
                            g.setColor(Color.WHITE);
                            durationtime = String.valueOf( (new Date().getTime() - starttimestamp - totalpausetime) / 1000 );
                            g.drawString("Time:" + durationtime + "s", 150, 20 );
                        }
                        // draw updated score and HP
                        g.setColor(Color.WHITE);
                        g.setFont(new Font("Arial", Font.PLAIN, 20));
                        g.drawString("Score：" + score, 2, 20);
                        g.drawString("Health Point:", 310, 20);
                        // draw health points
                        for (int i = 0; i < templife; i++) {
                            g.drawImage(hpicon, 430 + i * 21, 1, 20, 20, this);
                        }
                    }
                }
            }
        }

        public void updaterecordfile(){
            ArrayList<RecordScore> history = new ArrayList<>();
            // get string from record.txt
            try {
                File file = new File("src/record.txt");
                BufferedReader in = new BufferedReader(new FileReader(file));
                String line;
                String[] temp = new String[3];
                while ((line = in.readLine()) != null) {
                    temp = line.split(",");
                    RecordScore temprs = new RecordScore();
                    temprs.setScore(Integer.parseInt(temp[0]));
                    temprs.setName(temp[1]);
                    temprs.setTime(temp[2]);
                    history.add(temprs);
                }
                in.close();

                //add current round result
                RecordScore currentrs = new RecordScore();
                currentrs.setScore(score);
                currentrs.setName(username);
                currentrs.setTime(("Time:" + durationtime + "s"));
                history.add(currentrs);

                // sort the result list by score
                Collections.sort(history, new Comparator<RecordScore>() {
                    @Override
                    public int compare(RecordScore o1, RecordScore o2) {
                        return -(o1.getScore() - o2.getScore());
                    }
                });
                // set highest score!
                highestscore = history.get(0).getScore();

                // rewrite the record.txt file with the latest result
                FileWriter out = new FileWriter(file);
                String NEW_LIEN= System.getProperty("line.separator");
                for (int i = 0; i < history.size(); i++) {
                    out.write(history.get(i).getScore() + "," + history.get(i).getName() + "," + history.get(i).getTime() + NEW_LIEN);
                }
                out.close();
            }
            catch (IOException e)
            {
                e.getStackTrace();
            }
        }

        // side functions panel
        class sidefunctionsJP extends JPanel {
            String typein;
            // pause
            JLabel pauseJL = new JLabel();
            JButton replay = new JButton("Replay");
            JButton backtomain = new JButton("Back to Main");
            JButton ranklist = new JButton("Ranking");
            // difficulty slider - 1~5
            JSlider difficultJS = new JSlider(1, 5, 1);
            JLabel difficultJL = new JLabel();

            sidefunctionsJP() {
                setLayout(null);
                setBounds(0, mp.getHeight(), GameJF.getWidth(), GameJF.getHeight() - mp.getHeight());
                set_replay();
                set_difficultJS();
                set_ranklist();
                set_typein();

                // set showing messages
                showtext.setFont(new Font("Arial", Font.PLAIN, 15));
                showtext.setBounds(width / 4, 10, getWidth() / 3, getHeight() / 2);
                showtext.setText("<html>Good Luck " + username + "<br/><br/>Correct Time：" + correctcount + "</html>");
                //showtext.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                add(showtext);

                // set press space bar to pause
                pauseJL.setText("Press space bar to pause");
                pauseJL.setFont(new Font("arial", Font.PLAIN, 15));
                pauseJL.setForeground(Color.RED);
                pauseJL.setBounds(width / 4, getHeight() - 95, width / 2, 30);
                add(pauseJL);

                // set go back to main page
                backtomain.setBounds(width * 3 / 4 + 10, 55, 100, 50);
                add(backtomain);
                backtomain.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        GameJF.setVisible(false);
                        MyFrame.setVisible(true);
                    }
                });
            }

            // set rank list page
            void set_ranklist(){
                ranklist.setBounds(width * 3 / 4 + 10, 110, 100, 50);
                add(ranklist);
                // Show rank's frame by clicking 'Ranking' button
                ranklist.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Enter rank' page
                        // RankFrame.setVisible(true);
                        new RankingPage().setVisible(true);
                    }
                } );
            }

            // set type in box function
            void set_typein() {
                input.setCaretPosition(input.getText().length());
                input.setBounds(width / 4, getHeight() - 70, width / 3, 30);
                input.setFont(new Font("Arial", Font.PLAIN, 15));
                input.addKeyListener(new KeyAdapter() {
                    public void keyPressed(KeyEvent e) {
                        super.keyPressed(e);
                        // Check whether the input is a space - to pause
                        if (e.getKeyChar() == KeyEvent.VK_SPACE) {
                            if (pauseflag) { // end pause
                                endpausetime = new Date().getTime();
                                totalpausetime += endpausetime - startpausetime;
                                pauseflag = false;
                            }
                            else { // start to pause
                                pauseflag = true;
                                startpausetime = new Date().getTime();
                            }
                        }
                    }
                    // check input
                    public void keyReleased(KeyEvent e) {
                        // System.out.println("KeyReleased："+input.getText());
                        String s = input.getText().replaceAll(" ", "");
                        input.setText(s);
                        if( pauseflag==true && e.getKeyChar() != KeyEvent.VK_SPACE){
                            pauseflag = false;
                        }
                        if ( input.getText().length() > 0) {
                            typein = input.getText();
                            for (int i = 0; i < N; i++) {
                                if (typein.equals(num[i])) {
                                    // clear input box
                                    input.setText("");
                                    y[i] = 50;
                                    // add score
                                    score += 10 * difficult_level;
                                    correctcount++;
                                    if (chosendifficult_level < 5 && score > 200) {
                                        chosendifficult_level = score / 100;
                                        difficultJS.setMinimum(chosendifficult_level);
                                        if (difficult_level < chosendifficult_level) {
                                            // If the current difficulty is lower than the minimum difficulty, adjust the minimum difficulty
                                            difficult_level = chosendifficult_level;
                                        }
                                        difficultJS.setValue(difficult_level);
                                    }
                                    difficultJL.setText("<html>Dropping" + "<br/>speed" + "<br/>level: "+ difficult_level + "<html>");

                                    Random r = new Random();
                                    num[i] = words.get(r.nextInt(words.size())).toString();
                                    wordcolor[i] = new Color(r.nextInt(255),r.nextInt(255),r.nextInt(255));

                                }
                            }
                        }
                        showtext.setText("<html>Good Luck " + username + "<br/><br/>Correct Time：" + correctcount + "</html>");
                    }
                });
                add(input);
            }

            // difficulty slider of dropping speed
            void set_difficultJS() {
                difficultJS.setBounds(10, getHeight() - 150, 80, 40);
                difficultJS.setMajorTickSpacing(1);
                difficultJS.setSnapToTicks(true);
                difficultJS.setPaintTicks(true);
                difficultJS.setPaintLabels(true);
                difficultJS.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        difficult_level = difficultJS.getValue();
                        difficultJL.setText("<html>Dropping" + "<br/>speed" + "<br/>level: "+ difficult_level + "<html>");
                    }
                });
                difficultJL.setBounds(20, getHeight() - 100, 100, 50);
                difficultJL.setFont(new Font("Arial", Font.PLAIN, 15));
                difficultJL.setText("<html>Dropping" + "<br/>speed" + "<br/>level: "+ difficult_level + "<html>");
                add(difficultJL);
                add(difficultJS, Integer.valueOf(Integer.MIN_VALUE));
            }

            // replay
            void set_replay() {
                replay.setBounds(width * 3 / 4 + 10, 0, 100, 50);
                add(replay);
                replay.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // reset the game marks values
                        reset_func();
                    }
                });
            }
            // function: reset the game marks values
            void reset_func() {
                input.setEditable(true);
                difficult_level = 1;
                difficultJS.setMinimum(1);
                difficultJS.setMaximum(5);
                difficultJS.setValue(1);
                templife=life;
                pauseflag=false;
                get_random_word();
                score = 0;
                count = 3;
                correctcount = 0;
                getstarttime = false;
                getendtime = false;
                totalpausetime = 0;
                gametime = "";
                input.setText("");
                showtext.setText("<html>Good Luck " + username + "<br/><br/>Correct Time：" + correctcount + "</html>");
                startflag = false;
                Gameoverflag = false;
                input.requestFocus();
            }
        }
    }
}



