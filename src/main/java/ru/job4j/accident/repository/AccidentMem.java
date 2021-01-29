package ru.job4j.accident.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;

import java.util.HashMap;
import java.util.Map;

@Repository
public class AccidentMem {
    Map<Integer, Accident> accidents = new HashMap<>();

    public AccidentMem() {
        accidents.put(0, new Accident("First accident"));
        accidents.put(1, new Accident("Second accident"));
        accidents.put(3, new Accident("Third accident"));
    }

    public Map<Integer, Accident> getAccidents() {
        return accidents;
    }
}
