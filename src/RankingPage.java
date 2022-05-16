import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import java.io.*;

public class RankingPage extends JFrame {
    static JTextArea jta;
    static ArrayList curr_ranklist;
    public static final String NEW_LIEN= System.getProperty("line.separator");

    RankingPage() {
        super("Ranking");
        curr_ranklist = new ArrayList();
        jta = new JTextArea(5,5);
        jta.setEditable(false);

        try {
            File file = new File("src/record.txt");
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line;
            while ((line = in.readLine()) != null) {
                curr_ranklist.add(line);
            }
            in.close();
        }
        catch (IOException e) {
            e.getStackTrace();
        }

        for (int i = 0; i < curr_ranklist.size(); i++) {
            jta.append( "Rank" + (i+1) + ": " + curr_ranklist.get(i) + NEW_LIEN);
        }

        JPanel jpc = new JPanel(new GridLayout(1, 2));
        // Automatically switch to the next line
        jta.setLineWrap(true);
        JScrollPane jsp2 = new JScrollPane(jta,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jpc.add(jsp2);
        add(jpc);

        setTitle("Ranking");
        setSize(250,170);
        setLocationRelativeTo(null);
    }
}
