package potato.dao;
import potato.models.Tea;

import java.io.IOException;
import java.util.List;

public interface TeaDAO {
    List<Tea> getAllTeas() throws Exception;
    Tea getTeaById(int id) throws Exception;
    void addTea(Tea tea) throws Exception;
    void updateTea(Tea tea) throws Exception;
    void deleteTea(int id) throws Exception;
}