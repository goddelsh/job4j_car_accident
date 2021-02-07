package ru.job4j.accident.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;
import ru.job4j.accident.repository.AccidentHibernate;
import ru.job4j.accident.repository.AccidentJdbcTemplate;
import ru.job4j.accident.repository.AccidentMem;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class AccidentControl {
    private final AccidentHibernate accidents;

    public AccidentControl(AccidentHibernate accidents) {
        this.accidents = accidents;
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("types", accidents.getAccidentTypes());
        model.addAttribute("rules", accidents.getRules());
        return "accident/create";
    }

    @GetMapping("/update")
    public String update(@RequestParam("id") int id, Model model) {
        model.addAttribute("accident", accidents.findById(id));
        return "accident/edit";
    }

    @PostMapping ("/edit")
    public String edit(@ModelAttribute Accident accident) {
        accidents.edit(accident);
        return "redirect:/";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Accident accident, HttpServletRequest req) {
        Set<Rule> ruleSet = accidents.getRulesByIds(req.getParameterValues("rIds"));
        accident.setRules(ruleSet);
        accidents.save(accident);
        return "redirect:/";
    }
}