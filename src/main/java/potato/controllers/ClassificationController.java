package potato.controllers;

public class ClassificationController {

    public static String classifyTeaType(String description) {
        description = description.toLowerCase();
        if (description.contains("green")) return "Зелёный";
        else if (description.contains("black")) return "Чёрный";
        else if (description.contains("oolong") || description.contains("ulun")) return "Улун";
        else return "Неизвестный тип";
    }

    public static String calculateStrength(int brewTimeSeconds) {
        if (brewTimeSeconds < 120) return "Слабый";
        else if (brewTimeSeconds < 300) return "Средний";
        else return "Крепкий";
    }
}