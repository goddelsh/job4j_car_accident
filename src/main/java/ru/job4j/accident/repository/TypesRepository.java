package ru.job4j.accident.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.accident.model.AccidentType;

public interface TypesRepository  extends CrudRepository<AccidentType, Integer> {
}
