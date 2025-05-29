package potato.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import potato.models.Tea;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TeaImporter {

    private final TeaDAO teaDao;

    public TeaImporter(TeaDAO teaDao) {
        this.teaDao = teaDao;
    }

    public void importFromJson(String filePath) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            List<Tea> teas = mapper.readValue(new File(filePath), new TypeReference<List<Tea>>() {});
            for (Tea tea : teas) {
                teaDao.addTea(tea);
            }
            System.out.println("Импорт завершен: " + teas.size() + " чаёв добавлено.");
        } catch (IOException e) {
            System.err.println("Ошибка чтения JSON: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Ошибка при сохранении в БД: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}