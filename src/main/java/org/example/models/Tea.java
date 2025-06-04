package org.example.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tea {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private int id;

    private String name;

    @JsonProperty("type")
    private String type;

    private String description;

//    private String flavorProfileAsString;

//    @JsonProperty("flavor_profile")
    private String flavor_profile;

    private int quantity;

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getFlavor_profile() {
        return flavor_profile;
    }
    public void setFlavor_profile(String flavor_profile) {
        this.flavor_profile = flavor_profile;
    }



    // Парсер для JsonNode
//    public String getFlavorProfileAsString() {
//        try {
//            return flavorProfile != null ? objectMapper.writeValueAsString(flavorProfile) : "{}";
//        } catch (JsonProcessingException e) {
//            return "{}";
//        }
//    }
//    public void setFlavorProfileFromString(String json) throws JsonProcessingException {
//        this.flavorProfile = objectMapper.readTree(json);
//    }
    @Override
    public String toString(){
        return name;
    }
}