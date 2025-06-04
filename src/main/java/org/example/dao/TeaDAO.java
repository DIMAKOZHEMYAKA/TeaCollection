package org.example.dao;
import org.example.models.Tea;

import java.util.List;

public interface TeaDAO {
    List<Tea> getAllTeas() throws Exception;
    Tea getTeaById(int id) throws Exception;
    void addTea(Tea tea) throws Exception;
    void updateTea(Tea tea) throws Exception;
    void deleteTea(int id) throws Exception;
}