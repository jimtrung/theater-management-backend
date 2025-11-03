package com.github.jimtrung.theater.service;

import com.github.jimtrung.theater.dao.DirectorDAO;
import com.github.jimtrung.theater.model.Director;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectorService {
    private final DirectorDAO directorDAO;

    public DirectorService(DirectorDAO directorDAO) {
        this.directorDAO = directorDAO;
    }

    public List<Director> getAllDirector() {
        return directorDAO.getAll();
    }
}
