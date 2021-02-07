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
import ru.job4j.accident.repository.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AccidentControl {
    private final AccidentRepository accidents;
    private final RuleRepository rules;
    private final TypesRepository accidentTypes;

    public AccidentControl(AccidentRepository accidents, RuleRepository rules, TypesRepository accidentTypes) {
        this.accidents = accidents;
        this.rules = rules;
        this.accidentTypes = accidentTypes;
    }

    @GetMapping("/create")
    public String create(Model model) {
        List<AccidentType> types = new ArrayList<>();
        accidentTypes.findAll().forEach(types::add);
        List<Rule> ruleList = new ArrayList<>();
        rules.findAll().forEach(ruleList::add);
        model.addAttribute("types", types);
        model.addAttribute("rules", ruleList);
        return "accident/create";
    }

    @GetMapping("/update")
    public String update(@RequestParam("id") int id, Model model) {
        model.addAttribute("accident", accidents.findById(id).orElse(null));
        return "accident/edit";
    }

    @PostMapping ("/edit")
    public String edit(@ModelAttribute Accident accident) {
        accidents.save(accident);
        return "redirect:/";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Accident accident, HttpServletRequest req) {
        if (req.getParameterValues("rIds") != null) {
            Set<Rule> r = new HashSet<>();
            rules
                    .findAllById(Arrays.asList(req.getParameterValues("rIds"))
                            .stream()
                            .map(el -> Integer.parseInt(el)).
                                    collect(Collectors.toSet())).forEach(r::add);
            accident.setRules(r);
        }

        accidents.save(accident);
        return "redirect:/";
    }
}