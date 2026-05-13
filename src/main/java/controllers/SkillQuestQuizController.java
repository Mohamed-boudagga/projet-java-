package controllers;

import entities.Games;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;
import javafx.scene.control.Alert;
import java.util.ArrayList;
import java.util.List;

/**
 * Contrôleur spécifique pour les Quiz de SkillQuest (Jeux éducatifs)
 */
public class SkillQuestQuizController {

    @FXML private Label lblQuestion;
    @FXML private Label lblScore;
    @FXML private Label lblTimer;
    @FXML private ProgressBar progressTimer;
    @FXML private Button btnOpt1, btnOpt2, btnOpt3;

    private int score = 0;
    private int timeLeft = 10;
    private int timeSpent = 0;
    private Timeline timeline;
    private int currentQuestionIndex = 0;
    private Games gameConfig;
    private List<String> errors = new ArrayList<>();

    private List<QuestionInner> questions = new ArrayList<>();

    private static class QuestionInner {
        String text;
        String[] options;
        String correct;
        QuestionInner(String t, String[] o, String c) { text = t; options = o; correct = c; }
    }

    @FXML
    public void initialize() {
        // Initialisation par défaut
    }

    public void setGameConfig(Games config) {
        this.gameConfig = config;
        if (config != null) {
            if (config.getTimeLimit() > 0) {
                this.timeLeft = config.getTimeLimit();
            }
            lblScore.setText("Score: 0 / " + config.getScoreMax());
            
            services.ServiceQuestions sq = new services.ServiceQuestions();
            List<entities.Question> dbQuestions = sq.getByGameId(config.getId());
            
            questions.clear();
            for (entities.Question dq : dbQuestions) {
                questions.add(new QuestionInner(dq.getQuestionText(), 
                    new String[]{dq.getOpt1(), dq.getOpt2(), dq.getOpt3()}, 
                    dq.getCorrectAnswer()));
            }
            
            if (questions.isEmpty()) {
                lblQuestion.setText("Ce quiz n'a pas encore de questions.");
                btnOpt1.setDisable(true);
                btnOpt2.setDisable(true);
                btnOpt3.setDisable(true);
            } else {
                currentQuestionIndex = 0;
                loadQuestion();
            }
        }
    }

    private void loadQuestion() {
        if (currentQuestionIndex < questions.size()) {
            QuestionInner q = questions.get(currentQuestionIndex);
            lblQuestion.setText(q.text);
            btnOpt1.setText(q.options[0]);
            btnOpt2.setText(q.options[1]);
            btnOpt3.setText(q.options[2]);
            
            if (gameConfig != null && gameConfig.getTimeLimit() > 0) {
                timeLeft = gameConfig.getTimeLimit();
            } else {
                timeLeft = 10;
            }
            startTimer();
        } else {
            stopQuiz("Quiz Terminé !");
        }
    }

    private void startTimer() {
        if (timeline != null) timeline.stop();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeLeft--;
            timeSpent++;
            updateTimerUI();
            if (timeLeft <= 0) {
                timeline.stop();
                errors.add("Temps écoulé sur : " + questions.get(currentQuestionIndex).text);
                currentQuestionIndex++;
                loadQuestion();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateTimerUI() {
        lblTimer.setText("Temps: " + timeLeft + "s");
        double maxTime = (gameConfig != null && gameConfig.getTimeLimit() > 0) ? (double)gameConfig.getTimeLimit() : 10.0;
        progressTimer.setProgress((double)timeLeft / maxTime);
    }

    @FXML
    private void handleAnswer(ActionEvent event) {
        Button clicked = (Button) event.getSource();
        QuestionInner current = questions.get(currentQuestionIndex);
        
        if (clicked.getText().equals(current.correct)) {
            int pointsPerQuest = gameConfig.getScoreMax() / Math.max(1, questions.size());
            score += pointsPerQuest;
        } else {
            errors.add(current.text + " (Attendu : " + current.correct + ")");
        }
        
        String maxScore = (gameConfig != null) ? String.valueOf(gameConfig.getScoreMax()) : "???";
        lblScore.setText("Score : " + score + " / " + maxScore);
        currentQuestionIndex++;
        loadQuestion();
    }

    @FXML
    private void handleAIHelp() {
        if (currentQuestionIndex < questions.size()) {
            QuestionInner q = questions.get(currentQuestionIndex);
            Alert aiAlert = new Alert(Alert.AlertType.INFORMATION);
            aiAlert.setTitle("Assistant IA SkillQuest");
            aiAlert.setHeaderText("Indice de l'IA");
            String hint = "La réponse commence par '" + q.correct.charAt(0) + 
                          "' et contient " + q.correct.length() + " caractères.";
            aiAlert.setContentText("Question : \"" + q.text + "\"\n\n" + hint);
            aiAlert.showAndWait();
        }
    }

    private void stopQuiz(String title) {
        if (timeline != null) timeline.stop();
        
        StringBuilder result = new StringBuilder();
        result.append(title).append("\n\n");
        result.append("Score Final : ").append(score).append(" / ").append(gameConfig != null ? gameConfig.getScoreMax() : "???").append("\n");
        result.append("Temps utilisé : ").append(timeSpent).append("s\n");
        
        if (!errors.isEmpty()) {
            result.append("\nErreurs commises :\n");
            for (String err : errors) {
                result.append("- ").append(err).append("\n");
            }
        } else if (score > 0) {
            result.append("\nParfait ! Aucune erreur.");
        }

        lblQuestion.setText(result.toString());
        lblQuestion.setStyle("-fx-font-size: 16; -fx-text-alignment: center; -fx-text-fill: white;");
        
        btnOpt1.setVisible(false);
        btnOpt2.setVisible(false);
        btnOpt3.setVisible(false);
        progressTimer.setVisible(false);
        lblTimer.setVisible(false);
    }

    @FXML
    private void handleBack() {
        if (timeline != null) timeline.stop();
        ((javafx.stage.Stage) lblQuestion.getScene().getWindow()).close();
    }
}
