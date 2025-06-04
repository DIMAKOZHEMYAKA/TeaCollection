package org.example.service;


import org.example.dao.TeaDAO;
import org.example.models.Tea;

import java.util.List;

public class TeaService implements TeaDAO {
    private final TeaDAO teaDao;

    public TeaService(TeaDAO teaDao) {
        this.teaDao = teaDao;
    }

    public List<Tea> getAllTeas() throws Exception {
        return teaDao.getAllTeas();
    }

    public Tea getTeaById(int id) throws Exception {
        return teaDao.getTeaById(id);
    }

    public void addTea(Tea tea) throws Exception {
        teaDao.addTea(tea);
    }

    public void updateTea(Tea tea) throws Exception {
        teaDao.updateTea(tea);
    }

    public void deleteTea(int id) throws Exception {
        teaDao.deleteTea(id);
    }
}