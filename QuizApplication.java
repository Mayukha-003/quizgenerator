import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class QuizApplication {
    private static Map<String, Quiz> quizzes = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter command: ");
            String command = scanner.nextLine().trim();
            String[] parts = command.split(" ", 2);
            String cmd = parts[0];
            String argsStr = parts.length > 1 ? parts[1] : "";

            switch (cmd) {
                case "create_quiz":
                    createQuiz(argsStr);
                    break;
                case "add_question":
                    addQuestion(argsStr);
                    break;
                case "take_quiz":
                    takeQuiz(argsStr);
                    break;
                case "exit":
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Unknown command.");
            }
        }
    }

    private static void createQuiz(String title) {
        if (title.isEmpty()) {
            System.out.println("Usage: create_quiz [quiz_title]");
            return;
        }
        quizzes.put(title, new Quiz(title));
        System.out.println("Quiz created: " + title);
    }

    private static void addQuestion(String argsStr) {
        Pattern pattern = Pattern.compile("\"([^\"]*)\"|(\\S+)");
        Matcher matcher = pattern.matcher(argsStr);
        List<String> args = new ArrayList<>();
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                args.add(matcher.group(1));
            } else {
                args.add(matcher.group(2));
            }
        }

        if (args.size() < 7) {
            System.out.println("Usage: add_question [quiz_title] [question_text] [option1] [option2] [option3] [option4] [correct_option_index]");
            return;
        }

        String quizTitle = args.get(0);
        String questionText = args.get(1);
        String option1 = args.get(2);
        String option2 = args.get(3);
        String option3 = args.get(4);
        String option4 = args.get(5);
        int correctOptionIndex;

        try {
            correctOptionIndex = Integer.parseInt(args.get(6));
        } catch (NumberFormatException e) {
            System.out.println("Correct option index must be an integer.");
            return;
        }

        Quiz quiz = quizzes.get(quizTitle);
        if (quiz == null) {
            System.out.println("Quiz not found: " + quizTitle);
            return;
        }

        quiz.addQuestion(new Question(questionText, Arrays.asList(option1, option2, option3, option4), correctOptionIndex));
        System.out.println("Question added to quiz: " + quizTitle);
    }

    private static void takeQuiz(String title) {
        Quiz quiz = quizzes.get(title);
        if (quiz == null) {
            System.out.println("Quiz not found: " + title);
            return;
        }

        Scanner scanner = new Scanner(System.in);
        int score = 0;
        for (Question question : quiz.getQuestions()) {
            System.out.println(question.getText());
            List<String> options = question.getOptions();
            for (int i = 0; i < options.size(); i++) {
                System.out.println((i + 1) + ": " + options.get(i));
            }
            System.out.print("Your answer: ");
            int answer = scanner.nextInt();
            if (answer == question.getCorrectOptionIndex()) {
                score++;
            }
        }

        System.out.println("Quiz completed. Your score: " + score + "/" + quiz.getQuestions().size());
    }
}

class Quiz {
    private String title;
    private List<Question> questions;

    public Quiz(String title) {
        this.title = title;
        this.questions = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }
}

class Question {
    private String text;
    private List<String> options;
    private int correctOptionIndex;

    public Question(String text, List<String> options, int correctOptionIndex) {
        this.text = text;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    public String getText() {
        return text;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }
}
