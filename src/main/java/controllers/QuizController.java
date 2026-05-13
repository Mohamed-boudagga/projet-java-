package controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Certification;
import models.Exam;
import models.Question;
import services.ServiceCertification;
import services.ServiceQuestion;
import utils.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizController {

    @FXML private Label lbTimer;
    @FXML private Label lbProgress;
    @FXML private Label lbQuestion;
    @FXML private ProgressBar pbProgress;
    @FXML private VBox vbOptions;
    @FXML private Button btnNext;
    @FXML private Button btnFinish;

    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int totalPoints = 0;
    private int userScore = 0;
    private Exam currentExam;
    
    private int timeLeft; // seconds
    private Timeline timeline;
    private ToggleGroup toggleGroup;

    public void setExam(Exam exam) {
        this.currentExam = exam;
        this.timeLeft = exam.getDureeMinutes() * 60;
        loadQuestions();
        startTimer();
        showQuestion();
    }

    private void loadQuestions() {
        questions = new ArrayList<>();
        ServiceQuestion sq = new ServiceQuestion();
        questions = sq.getByExam(currentExam.getId());
        
        if (questions.isEmpty()) {
            int level = currentExam.getLevel();
            if (level <= 2) {
                questions.add(new Question("Quel est le type de données pour un nombre entier ?", Arrays.asList("float", "int", "String", "boolean"), 1, 25));
                questions.add(new Question("Comment écrit-on un commentaire sur une ligne en Java ?", Arrays.asList("# comment", "// comment", "/* comment", "-- comment"), 1, 25));
                questions.add(new Question("Quelle boucle est utilisée pour répéter un bloc un nombre connu de fois ?", Arrays.asList("while", "if", "for", "switch"), 2, 25));
                questions.add(new Question("Quel est le point d'entrée d'un programme Java ?", Arrays.asList("start()", "main()", "run()", "init()"), 1, 25));
            }
        }

        totalPoints = 0;
        for (Question q : questions) {
            totalPoints += q.getPoints();
        }
    }

    private void startTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeLeft--;
            int mins = timeLeft / 60;
            int secs = timeLeft % 60;
            lbTimer.setText(String.format("%02d:%02d", mins, secs));
            if (timeLeft <= 0) {
                timeline.stop();
                handleFinish(null);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void showQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            btnNext.setVisible(false);
            btnFinish.setVisible(true);
            return;
        }

        Question q = questions.get(currentQuestionIndex);
        lbQuestion.setText(q.getText());
        lbProgress.setText("Question " + (currentQuestionIndex + 1) + "/" + questions.size());
        pbProgress.setProgress((double) (currentQuestionIndex + 1) / questions.size());

        vbOptions.getChildren().clear();
        toggleGroup = new ToggleGroup();

        for (int i = 0; i < q.getOptions().size(); i++) {
            RadioButton rb = new RadioButton(q.getOptions().get(i));
            rb.setToggleGroup(toggleGroup);
            rb.setStyle("-fx-text-fill: white; -fx-font-size: 16;");
            rb.setUserData(i);
            vbOptions.getChildren().add(rb);
        }

        if (currentQuestionIndex == questions.size() - 1) {
            btnNext.setVisible(false);
            btnFinish.setVisible(true);
        }
    }

    @FXML
    private void handleNext(ActionEvent event) {
        checkAnswer();
        currentQuestionIndex++;
        showQuestion();
    }

    private void checkAnswer() {
        if (toggleGroup == null) return;
        RadioButton selected = (RadioButton) toggleGroup.getSelectedToggle();
        if (selected != null) {
            int selectedIndex = (int) selected.getUserData();
            Question q = questions.get(currentQuestionIndex);
            if (selectedIndex == q.getCorrectOptionIndex()) {
                userScore += q.getPoints();
            }
        }
    }

    @FXML
    private void handleFinish(ActionEvent event) {
        if (timeline != null) timeline.stop();
        checkAnswer();
        retourMenu(event);
    }

    private void retourMenu(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/ListeExamensNiveau.fxml"));
            Stage stage = (Stage) lbQuestion.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
