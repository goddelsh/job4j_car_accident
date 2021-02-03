package ru.job4j.accident.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class AccidentMem {
    private AtomicInteger integer = new AtomicInteger();
    final private Map<Integer, Accident> accidents = new ConcurrentHashMap<>();

    public AccidentMem() {
        accidents.put(integer.incrementAndGet(), new Accident(integer.get(),"First accident"));
        accidents.put(integer.incrementAndGet(), new Accident(integer.get(),"Second accident"));
        accidents.put(integer.incrementAndGet(), new Accident(integer.get(),"Third accident"));
    }

    public List<AccidentType> getAccidentTypes() {
        return List.of(new AccidentType(1, "Две машины"),
                new AccidentType(2, "Машина и человек"),
                new AccidentType(3, "Машина и велосиед"));
    }


    public List<Rule> getRules() {
        return List.of(Rule.of(1, "Статья. 1"),
                Rule.of(1, "Статья. 2"),
                Rule.of(1, "Статья. 3"));
    }

    public Map<Integer, Accident> getAccidents() {
        return accidents;
    }

    public List<Accident> getAccidentsList() {
        return accidents.values().stream().collect(Collectors.toList());
    }

    public void create(Accident accident) {
        accidents.putIfAbsent(integer.incrementAndGet(), accident);
    }

    public Accident findById(int id) {
        return accidents.get(id);
    }

    public void edit(Accident accident) {
        this.accidents.computeIfPresent(accident.getId(), (key, value) -> accident);
    }
}
