package ru.job4j.accident.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import ru.job4j.accident.model.Accident;
import ru.job4j.accident.repository.AccidentHibernate;
import ru.job4j.accident.repository.AccidentJdbcTemplate;
import ru.job4j.accident.repository.AccidentMem;
import ru.job4j.accident.repository.AccidentRepository;

@Controller
public class IndexControl {

    final private AccidentRepository accidentMem;

    public IndexControl(AccidentRepository  accidentMem) {
        this.accidentMem = accidentMem;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Accident> list = new ArrayList<>();
        accidentMem.findAll().forEach(list::add);
        model.addAttribute("list", list);
        return "index";
    }
}