package controllers;

<<<<<<< HEAD
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


public class QuizController {

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


    private List<Question> questions = new ArrayList<>();

    private static class Question {
        String text;
        String[] options;
        String correct;
        Question(String t, String[] o, String c) { text = t; options = o; correct = c; }
    }

    @FXML
    public void initialize() {
        // Les questions seront chargées via setGameConfig
    }

    public void setGameConfig(Games config) {
        this.gameConfig = config;
        if (config != null) {
            if (config.getTimeLimit() > 0) {
                this.timeLeft = config.getTimeLimit();
            }
            lblScore.setText("Score: 0 / " + config.getScoreMax());
            
            // CHARGEMENT DYNAMIQUE DES QUESTIONS
            services.ServiceQuestions sq = new services.ServiceQuestions();
            List<entities.Question> dbQuestions = sq.getByGameId(config.getId());
            
            questions.clear();
            for (entities.Question dq : dbQuestions) {
                questions.add(new Question(dq.getQuestionText(), 
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
            Question q = questions.get(currentQuestionIndex);
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
            stopQuiz("Quiz Termine !");
        }
    }

    private void stopQuiz(String title) {
        if (timeline != null) timeline.stop();
        
        StringBuilder result = new StringBuilder();
        result.append(title).append("\n\n");
        result.append("Score Final: ").append(score).append(" / ").append(gameConfig != null ? gameConfig.getScoreMax() : "???").append("\n");
        result.append("Temps utilise: ").append(timeSpent).append("s\n");
        
        if (!errors.isEmpty()) {
            result.append("\nErreurs commises:\n");
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


    private void startTimer() {
        if (timeline != null) timeline.stop();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), new javafx.event.EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                timeLeft--;
                timeSpent++;
                updateTimerUI();
                if (timeLeft <= 0) {
                    timeline.stop();
                    errors.add("Temps ecoule sur: " + questions.get(currentQuestionIndex).text);
                    currentQuestionIndex++;
                    loadQuestion();
                }
            }
        }));

=======
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
        
        // On charge les questions depuis la DB
        ServiceQuestion sq = new ServiceQuestion();
        questions = sq.getByExam(currentExam.getId());
        
        // Si aucune question n'est trouvée, on garde le fallback par défaut selon le niveau
        if (questions.isEmpty()) {
            int level = currentExam.getLevel();
            if (level <= 2) {
                // NIVEAU DÉBUTANT (0-2)
                questions.add(new Question("Quel est le type de données pour un nombre entier ?", Arrays.asList("float", "int", "String", "boolean"), 1, 25));
                questions.add(new Question("Comment écrit-on un commentaire sur une ligne en Java ?", Arrays.asList("# comment", "// comment", "/* comment", "-- comment"), 1, 25));
                questions.add(new Question("Quelle boucle est utilisée pour répéter un bloc un nombre connu de fois ?", Arrays.asList("while", "if", "for", "switch"), 2, 25));
                questions.add(new Question("Quel est le point d'entrée d'un programme Java ?", Arrays.asList("start()", "main()", "run()", "init()"), 1, 25));
            } else if (level <= 5) {
                // NIVEAU INTERMÉDIAIRE (3-5)
                questions.add(new Question("Quel mot-clé est utilisé pour hériter d'une classe ?", Arrays.asList("implements", "extends", "inherits", "super"), 1, 25));
                questions.add(new Question("Qu'est-ce qu'un constructeur ?", Arrays.asList("Une méthode pour détruire un objet", "Une méthode pour initialiser un objet", "Une variable statique", "Une interface"), 1, 25));
                questions.add(new Question("Quel mot-clé empêche une classe d'être héritée ?", Arrays.asList("static", "final", "abstract", "private"), 1, 25));
                questions.add(new Question("Quelle interface est utilisée pour définir une collection de type clé-valeur ?", Arrays.asList("List", "Set", "Map", "Queue"), 2, 25));
            } else if (level <= 8) {
                // NIVEAU AVANCÉ (6-8)
                questions.add(new Question("Quelle classe est la classe parente de toutes les classes en Java ?", Arrays.asList("System", "Main", "Object", "Root"), 2, 25));
                questions.add(new Question("Comment gère-t-on les exceptions en Java ?", Arrays.asList("try-catch", "if-else", "throw-throws", "A et C sont corrects"), 3, 25));
                questions.add(new Question("Qu'est-ce qu'une interface fonctionnelle ?", Arrays.asList("Une interface avec plusieurs méthodes", "Une interface avec une seule méthode abstraite", "Une classe abstraite", "Une méthode statique"), 1, 25));
                questions.add(new Question("Quel mot-clé est utilisé pour synchroniser l'accès à un bloc de code par plusieurs threads ?", Arrays.asList("volatile", "transient", "synchronized", "atomic"), 2, 25));
            } else {
                // NIVEAU EXPERT (9-11)
                questions.add(new Question("Lequel n'est pas un Garbage Collector en Java ?", Arrays.asList("G1", "ZGC", "V8", "Parallel"), 2, 25));
                questions.add(new Question("Que signifie l'acronyme SOLID en programmation orientée objet ?", Arrays.asList("Un type de base de données", "5 principes de conception logicielle", "Une bibliothèque Java", "Un format de fichier"), 1, 25));
                questions.add(new Question("Quelle est la différence entre JVM et JRE ?", Arrays.asList("Aucune", "JRE inclut la JVM et les bibliothèques", "JVM inclut le JRE", "JVM est pour Windows uniquement"), 1, 25));
                questions.add(new Question("Quel design pattern est utilisé pour créer une seule instance d'une classe ?", Arrays.asList("Factory", "Observer", "Singleton", "Strategy"), 2, 25));
            }
        }

        totalPoints = 0; // Reset totalPoints
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
>>>>>>> b4c9e27997fb144b40ee765e4c8448734eccd960
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

<<<<<<< HEAD

    private void updateTimerUI() {
        lblTimer.setText("Temps: " + timeLeft + "s");
        double maxTime = (gameConfig != null && gameConfig.getTimeLimit() > 0) ? gameConfig.getTimeLimit() : 10.0;
        progressTimer.setProgress((double)timeLeft / maxTime);
    }

    @FXML
    private void handleAnswer(ActionEvent event) {
        Button clicked = (Button) event.getSource();
        Question current = questions.get(currentQuestionIndex);
        
        if (clicked.getText().equals(current.correct)) {
            int pointsPerQuest = gameConfig.getScoreMax() / questions.size();
            score += pointsPerQuest;
        } else {
            errors.add(current.text + " (Attendu: " + current.correct + ")");
        }
        
        String maxScore = (gameConfig != null) ? String.valueOf(gameConfig.getScoreMax()) : "???";
        lblScore.setText("Score: " + score + " / " + maxScore);
        currentQuestionIndex++;
        loadQuestion();
    }


    @FXML
    private void handleAIHelp() {
        if (currentQuestionIndex < questions.size()) {
            Question q = questions.get(currentQuestionIndex);
            Alert aiAlert = new Alert(Alert.AlertType.INFORMATION);
            aiAlert.setTitle("Assistant IA SkillQuest");
            aiAlert.setHeaderText("Indice de l'IA");
            String hint = "La reponse commence par '" + q.correct.charAt(0) + 
                          "' et contient " + q.correct.length() + " caracteres.";
            aiAlert.setContentText("Question : \"" + q.text + "\"\n\n" + hint);
            aiAlert.showAndWait();
=======
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
        RadioButton selected = (RadioButton) toggleGroup.getSelectedToggle();
        if (selected != null) {
            int selectedIndex = (int) selected.getUserData();
            Question q = questions.get(currentQuestionIndex);
            if (selectedIndex == q.getCorrectOptionIndex()) {
                userScore += q.getPoints();
            }
>>>>>>> b4c9e27997fb144b40ee765e4c8448734eccd960
        }
    }

    @FXML
<<<<<<< HEAD
    private void handleBack() {
        if (timeline != null) timeline.stop();
        ((javafx.stage.Stage) lblQuestion.getScene().getWindow()).close();
    }
}


=======
    private void handleFinish(ActionEvent event) {
        if (timeline != null) timeline.stop();
        checkAnswer();

        double percentage = ((double) userScore / totalPoints) * 100;

        if (percentage == 100) {
            // Enregistrer la réussite de l'examen et vérifier le déblocage du niveau suivant
            boolean levelUnlocked = SessionManager.getInstance().completeExam(currentExam);
            
            if (levelUnlocked) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/LevelUnlocked.fxml"));
                    Parent root = loader.load();
                    
                    LevelUnlockedController controller = loader.getController();
                    controller.setMessage("QUÊTE ACCOMPLIE ! Tu as maîtrisé le niveau " + currentExam.getLevel() + ".\nLe niveau " + (currentExam.getLevel() + 1) + " t'attend désormais !");
                    
                    Stage stage = new Stage();
                    stage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
                    Scene scene = new Scene(root);
                    scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
                    stage.setScene(scene);
                    stage.showAndWait();
                    
                    // C'est seulement ici qu'on affiche le certificat
                    afficherCertificat(event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Succès intermédiaire : l'examen est réussi mais le niveau n'est pas encore fini
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ExamSuccess.fxml"));
                    Parent root = loader.load();
                    
                    Stage stage = new Stage();
                    stage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
                    Scene scene = new Scene(root);
                    scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
                    stage.setScene(scene);
                    stage.showAndWait();
                    
                    retourMenu(event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            showResultAlert(percentage);
        }
    }

    private void showResultAlert(double percentage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Résultat de l'examen");
        alert.setHeaderText("Score : " + (int)percentage + "%");
        alert.setContentText("Vous devez obtenir 100% pour recevoir le certificat. Réessayez !");
        alert.showAndWait();
        retourMenu(null);
    }

    private void afficherCertificat(ActionEvent event) {
        try {
            ServiceCertification sc = new ServiceCertification();
            List<Certification> certs = sc.getAll();
            Certification matchingCert = certs.stream()
                    .filter(c -> c.getLevel() == currentExam.getLevel())
                    .findFirst()
                    .orElse(null);

            if (matchingCert == null) {
                matchingCert = new Certification();
                matchingCert.setTitle("Certification " + currentExam.getNom());
                matchingCert.setDescription("Délivrée pour avoir réussi l'examen avec un score de 100%.");
                matchingCert.setLevel(currentExam.getLevel());
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CertificatVue.fxml"));
            Parent root = loader.load();
            
            CertificatVueController controller = loader.getController();
            controller.setData("Etudiant", matchingCert);
            
            Stage stage;
            if (event != null) {
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            } else {
                stage = (Stage) lbQuestion.getScene().getWindow();
            }
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void retourMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeExamensNiveau.fxml"));
            Parent root = loader.load();
            
            ListeExamensNiveauController controller = loader.getController();
            controller.setNiveau(currentExam.getLevel());
            
            Stage stage = (Stage) lbQuestion.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
>>>>>>> b4c9e27997fb144b40ee765e4c8448734eccd960
