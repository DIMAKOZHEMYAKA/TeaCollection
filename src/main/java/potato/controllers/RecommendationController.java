package potato.controllers;

import potato.models.Tea;

import java.util.Map;

public class RecommendationController {
    public static Map<String, Object> recommendBrewing(Tea tea, Map<String, Object> preferences) {
        int temperature = 85; // default
        int time = 180; // seconds

        switch (tea.getType()) {
            case "зеленый":
                temperature = 75;
                time = 120;
                break;
            case "черный":
                temperature = 95;
                time = 240;
                break;
            case "улун":
                temperature = 90;
                time = 180;
                break;
        }

        if ((Boolean) preferences.getOrDefault("stronger", false)) {
            time += 60;
        }

        return Map.of("temperature", temperature, "time_seconds", time);
    }
}