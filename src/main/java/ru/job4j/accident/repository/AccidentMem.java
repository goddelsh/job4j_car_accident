package ru.job4j.accident.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class AccidentMem {
    AtomicInteger integer = new AtomicInteger();
    final Map<Integer, Accident> accidents = new ConcurrentHashMap<>();

    public AccidentMem() {
        accidents.put(integer.incrementAndGet(), new Accident("First accident"));
        accidents.put(integer.incrementAndGet(), new Accident("Second accident"));
        accidents.put(integer.incrementAndGet(), new Accident("Third accident"));
    }

    public Map<Integer, Accident> getAccidents() {
        return accidents;
    }

    public void create(Accident accident) {
        accidents.putIfAbsent(integer.incrementAndGet(), accident);
    }
}
