package com.github.jimtrung.theater.service;

import com.github.jimtrung.theater.dao.ActorDAO;
import com.github.jimtrung.theater.model.Actor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActorService {
    private final ActorDAO actorDAO;

    public ActorService(ActorDAO actorDAO) {
        this.actorDAO = actorDAO;
    }

    public List<Actor> getAllActor() {
        return actorDAO.getAll();
    }
}
