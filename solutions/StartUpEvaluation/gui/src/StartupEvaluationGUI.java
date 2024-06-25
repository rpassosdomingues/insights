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
                double score = sendEvaluationRequest(data);
                resultLabel.setText("Score: " + score);
            } catch (Exception e) {
                resultLabel.setText("Error: " + e.getMessage());
            }
        });

        VBox vbox = new VBox(10, param1Field, param2Field, evaluateButton, resultLabel);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private double sendEvaluationRequest(Map<String, Object> data) throws Exception {
        URL url = new URL("http://localhost:8080/evaluate");
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
                String responseLine;
                StringBuilder response = new StringBuilder();
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
