import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

class QuizManager {

    private List<Question> questionBank = new ArrayList<>();
    private int score = 0;
    private User user;
    private static final int TOTAL_QUESTIONS = 5;

    public QuizManager(User user) {
        this.user = user;
        loadQuestions();
        Collections.shuffle(questionBank);
    }

    private void loadQuestions() {
        questionBank.add(new Question(
                "What is JVM?",
                new String[]{"Java Virtual Machine", "Java Variable Method", "Just VM", "None"},
                1));

        questionBank.add(new Question(
                "Which keyword is used to inherit a class?",
                new String[]{"this", "super", "extends", "implements"},
                3));

        questionBank.add(new Question(
                "Which collection does not allow duplicates?",
                new String[]{"List", "Set", "Map", "Array"},
                2));

        questionBank.add(new Question(
                "Which method is entry point of Java program?",
                new String[]{"start()", "run()", "main()", "init()"},
                3));

        questionBank.add(new Question(
                "Which concept supports method overriding?",
                new String[]{"Encapsulation", "Inheritance", "Abstraction", "Polymorphism"},
                4));
    }

    public Question getQuestion(int index) {
        return questionBank.get(index);
    }

    public void checkAnswer(int index, int answer) {
        if (questionBank.get(index).isCorrect(answer)) {
            score++;
        }
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return TOTAL_QUESTIONS;
    }

    public String getUsername() {
        return user.getUsername();
    }
}
 class QuizFrame extends JFrame {

    private QuizManager quizManager;
    private JLabel questionLabel;
    private JRadioButton[] options;
    private ButtonGroup group;
    private JButton nextButton;
    private int currentIndex = 0;

    public QuizFrame(User user) {
        quizManager = new QuizManager(user);
        initUI();
        loadQuestion();
    }

    private void initUI() {
        setTitle("Online Quiz Application");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        questionLabel = new JLabel("Question");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(questionLabel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel(new GridLayout(4, 1));
        options = new JRadioButton[4];
        group = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            group.add(options[i]);
            optionsPanel.add(options[i]);
        }

        add(optionsPanel, BorderLayout.CENTER);

        nextButton = new JButton("Next");
        nextButton.addActionListener(this::nextClicked);
        add(nextButton, BorderLayout.SOUTH);
    }

    private void loadQuestion() {
        if (currentIndex >= quizManager.getTotalQuestions()) {
            showResult();
            return;
        }

        Question q = quizManager.getQuestion(currentIndex);
        questionLabel.setText(q.getQuestion());

        String[] opts = q.getOptions();
        for (int i = 0; i < 4; i++) {
            options[i].setText(opts[i]);
        }

        group.clearSelection();
    }

    private void nextClicked(ActionEvent e) {
        for (int i = 0; i < 4; i++) {
            if (options[i].isSelected()) {
                quizManager.checkAnswer(currentIndex, i + 1);
                break;
            }
        }
        currentIndex++;
        loadQuestion();
    }

    private void showResult() {
        JOptionPane.showMessageDialog(this,
                "User: " + quizManager.getUsername() +
                        "\nScore: " + quizManager.getScore(),
                "Quiz Result",
                JOptionPane.INFORMATION_MESSAGE);

        FileUtil.saveResult(
                quizManager.getUsername(),
                quizManager.getScore(),
                quizManager.getTotalQuestions()
        );
        dispose();
    }
}

    class User {
    private String username;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}


    class FileUtil {

    public static void saveResult(String username, int score, int total) {
        try {
            FileWriter writer = new FileWriter("scores.txt", true);
            writer.write("User: " + username + " | Score: " + score + "/" + total + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving result.");
        }
    }
}

 class Question {

    private String question;
    private String[] options;
    private int correctAnswer;

    public Question(String question, String[] options, int correctAnswer) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    // ✅ ADD THIS METHOD
    public String getQuestion() {
        return question;
    }

    // ✅ ADD THIS METHOD (THIS FIXES YOUR ERROR)
    public String[] getOptions() {
        return options;
    }

    public boolean isCorrect(int userAnswer) {
        return userAnswer == correctAnswer;
    }
}






public class Main {
    public static void main(String[] args) {
        String name = JOptionPane.showInputDialog("Enter your name");

        if (name != null && !name.trim().isEmpty()) {
            User user = new User(name);
            SwingUtilities.invokeLater(() -> new QuizFrame(user).setVisible(true));
        }
    }
}

