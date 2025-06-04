package org.example.dao;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.models.Tea;


import java.io.File;
import java.io.IOException;
import java.util.List;


public class TeaJSON implements TeaDAO {
    private static final String JSON_FILE = "teas.json";
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<Tea> getAllTeas() throws IOException {
        return mapper.readValue(new File(JSON_FILE), new TypeReference<List<Tea>>() {});
    }

    @Override
    public Tea getTeaById(int id) throws IOException {
        return getAllTeas().stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void addTea(Tea tea) throws IOException {
        List<Tea> teas = getAllTeas();
        tea.setId(teas.stream().mapToInt(Tea::getId).max().orElse(0) + 1);
        teas.add(tea);
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(JSON_FILE), teas);
    }

    @Override
    public void updateTea(Tea tea) throws IOException {
        List<Tea> teas = getAllTeas();
        teas.replaceAll(t -> t.getId() == tea.getId() ? tea : t);
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(JSON_FILE), teas);
    }

    @Override
    public void deleteTea(int id) throws IOException {
        List<Tea> teas = getAllTeas();
        teas.removeIf(t -> t.getId() == id);
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(JSON_FILE), teas);
    }
}