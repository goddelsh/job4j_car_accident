package ru.job4j.accident.repository;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class AccidentJdbcTemplate {
    private final JdbcTemplate jdbc;

    public AccidentJdbcTemplate(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Accident save(Accident accident) {
        KeyHolder id = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement("insert into accident (name, text, address, type_id) values (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, accident.getName());
            ps.setString(2, accident.getText());
            ps.setString(3, accident.getAddress());
            ps.setInt(4, accident.getType().getId());
            return  ps;
        }, id);
        Integer key = (Integer) id.getKeys().get("id");
        accident.getRules().forEach(el -> jdbc.update("insert into accidents_rules (accident_id, rule_id) values (?, ?)",
                key, el.getId()));
        accident.setId(key);
        return accident;
    }

    public List<Accident> getAll() {
        return jdbc.query("select a.id, a.name, text, address, type_id, t.name as type from accident a left join accident_types t on a.type_id = t.id",
                (rs, row) -> {
                    Accident accident = new Accident();
                    accident.setId(rs.getInt("id"));
                    accident.setName(rs.getString("name"));
                    accident.setAddress(rs.getString("address"));
                    accident.setText(rs.getString("text"));
                    accident.setType(new AccidentType(rs.getInt("type_id"), rs.getString("type")));
                    List<Rule> rules = jdbc.query("select b.id as id, b.name as name from accidents_rules a right join accident_rules b on a.rule_id = b.id where a.accident_id = ?",
                            (rsr, rowr) -> {
                                Rule rule = new Rule();
                                rule.setId(rsr.getInt("id"));
                                rule.setName(rsr.getString("name"));
                                return rule;
                            },
                            accident.getId());
                    accident.setRules(Set.copyOf(rules));
                    return accident;
                });
    }

    public List<AccidentType> getAccidentTypes() {
        return jdbc.query("select id, name from accident_types",
                (rs, row) -> {
                    AccidentType accidentType = new AccidentType();
                    accidentType.setId(rs.getInt("id"));
                    accidentType.setName(rs.getString("name"));
                    return accidentType;
                });
    }


    public List<Rule> getRules() {
        return jdbc.query("select id, name from accident_rules",
                (rs, row) -> {
                    Rule rule = new Rule();
                    rule.setId(rs.getInt("id"));
                    rule.setName(rs.getString("name"));
                    return rule;
                });
    }

    public Set<Rule> getRulesByIds(String[] rIds) {
        return this.getRules().stream()
                .filter(elem -> Arrays.binarySearch(rIds, String.valueOf(elem.getId())) > 0)
                .collect(Collectors.toSet());
    }

    public Accident findById(int id) {
        return jdbc.query("select a.id, a.name, text, address, type_id, t.name as type from accident a left join accident_types t on a.type_id = t.id where a.id = ?",
                (rs, row) -> {
                    Accident accident = new Accident();
                    accident.setId(rs.getInt("id"));
                    accident.setName(rs.getString("name"));
                    accident.setAddress(rs.getString("address"));
                    accident.setText(rs.getString("text"));
                    accident.setType(new AccidentType(rs.getInt("type_id"), rs.getString("type")));
                    List<Rule> rules = jdbc.query("select b.id as id, b.name as name from accidents_rules a right join accident_rules b on a.rule_id = b.id where a.accident_id = ?",
                            (rsr, rowr) -> {
                                Rule rule = new Rule();
                                rule.setId(rsr.getInt("id"));
                                rule.setName(rsr.getString("name"));
                                return rule;
                            },
                            accident.getId());
                    accident.setRules(Set.copyOf(rules));
                    return accident;
                }, id).stream().findFirst().orElse(null);
    }

    public void edit(Accident accident) {
        this.jdbc.update("update accident set name = ? where id = ?",
                accident.getName(),
                accident.getId());
    }
}