package ru.job4j.accident.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import ru.job4j.accident.model.Accident;
import ru.job4j.accident.repository.AccidentMem;

@Controller
public class IndexControl {

    final private AccidentMem accidentMem;

    public IndexControl(AccidentMem accidentMem) {
        this.accidentMem = accidentMem;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Accident> list = accidentMem.getAccidents()
                .entrySet() // получаю сет
                .stream() //стрим
                .map(f -> f.getValue()) //мэплю значение
                .collect(Collectors.toList()); //собираю в лист
        model.addAttribute("list", list);
        return "index";
    }
}