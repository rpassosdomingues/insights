import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class StartupEvaluationApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Startup Evaluation");

        TabPane tabPane = new TabPane();

        tabPane.getTabs().add(createTab("Development Stage", "/development-stage"));
        tabPane.getTabs().add(createTab("Competitive Analysis", "/competitive-analysis"));
        tabPane.getTabs().add(createTab("Portfolio Development", "/portfolio-development"));
        tabPane.getTabs().add(createTab("Intellectual Property", "/intellectual-property"));
        tabPane.getTabs().add(createTab("Process Adaptation", "/process-adaptation"));
        tabPane.getTabs().add(createTab("Visual Identity", "/visual-identity"));
        tabPane.getTabs().add(createTab("Digital Presence", "/digital-presence"));
        tabPane.getTabs().add(createTab("Customer Portfolio", "/customer-portfolio"));
        tabPane.getTabs().add(createTab("Sales Force", "/sales-force"));
        tabPane.getTabs().add(createTab("Pricing", "/pricing"));
        tabPane.getTabs().add(createTab("Communication Plan", "/communication-plan"));
        tabPane.getTabs().add(createTab("Investment Plan", "/investment-plan"));
        tabPane.getTabs().add(createTab("Fundraising Knowledge", "/fundraising-knowledge"));
        tabPane.getTabs().add(createTab("Resource Independence", "/resource-independence"));
        tabPane.getTabs().add(createTab("Financial Management", "/financial-management"));
        tabPane.getTabs().add(createTab("Financial Sustainability", "/financial-sustainability"));
        tabPane.getTabs().add(createTab("Corporate Aspects", "/corporate-aspects"));
        tabPane.getTabs().add(createTab("Legal and Tax Aspects", "/legal-and-tax-aspects"));
        tabPane.getTabs().add(createTab("Business Model", "/business-model"));
        tabPane.getTabs().add(createTab("Legal Security", "/legal-security"));
        tabPane.getTabs().add(createTab("Team", "/team"));
        tabPane.getTabs().add(createTab("Goals and Objectives", "/goals-and-objectives"));
        tabPane.getTabs().add(createTab("Business Commitment", "/business-commitment"));
        tabPane.getTabs().add(createTab("Continuous Learning", "/continuous-learning"));
        tabPane.getTabs().add(createTab("Networking", "/networking"));
        tabPane.getTabs().add(createTab("Oral and Written Communication", "/oral-written-communication"));

        VBox vbox = new VBox(tabPane);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Tab createTab(String title, String endpoint) {
        Tab tab = new Tab(title);
        tab.setClosable(false);

        TextField param1Field = new TextField();
        param1Field.setPromptText("Parameter 1");

        TextField param2Field = new TextField();
        param2Field.setPromptText("Parameter 2");

        Button evaluateButton = new Button("Evaluate");
        Label resultLabel = new Label();

        evaluateButton.setOnAction(event -> {
            Map<String, Object> data = new HashMap<>();
            data.put("param1", param1Field.getText());
            data.put("param2", param2Field.getText());

            try {
                double score = sendEvaluationRequest(endpoint, data);
                resultLabel.setText("Score: " + score);
            } catch (Exception e) {
                resultLabel.setText("Error: " + e.getMessage());
            }
        });

        VBox vbox = new VBox(10, param1Field, param2Field, evaluateButton, resultLabel);
        vbox.setPadding(new Insets(10));
        tab.setContent(vbox);

        return tab;
    }

    private double sendEvaluationRequest(String endpoint, Map<String, Object> data) throws Exception {
        URL url = new URL("http://localhost:8080" + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (Writer writer = new OutputStreamWriter(conn.getOutputStream())) {
            writer.write(new ObjectMapper().writeValueAsString(data));
        }

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                 BufferedReader bufferedReader = new BufferedReader(reader)) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = bufferedReader.readLine()) != null) {
                    response.append(responseLine);
                }
                Map<String, Object> responseData = new ObjectMapper().readValue(response.toString(), Map.class);
                return (double) responseData.get("score");
            }
        } else {
            throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
