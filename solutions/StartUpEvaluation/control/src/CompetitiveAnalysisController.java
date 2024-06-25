import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/competitive-analysis")
public class CompetitiveAnalysisController {

    private final Map<String, Object> dataStore = new HashMap<>();

    @PostMapping
    public Map<String, Object> evaluate(@RequestBody Map<String, Object> data) {
        double score = calculateScore(data);
        dataStore.putAll(data);  // Armazena os dados na memória para demonstração
        Map<String, Object> response = new HashMap<>();
        response.put("score", score);
        return response;
    }

    private double calculateScore(Map<String, Object> data) {
        return data.values().stream()
                .mapToDouble(value -> Double.parseDouble(value.toString()))
                .average()
                .orElse(0);
    }
}
