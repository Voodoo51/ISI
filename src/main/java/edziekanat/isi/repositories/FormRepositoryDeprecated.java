package edziekanat.isi.repositories;

import edziekanat.isi.models.FormTemplate;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FormRepositoryDeprecated {
    private final JdbcTemplate jdbcTemplate; //move this to a signle file

    public FormRepositoryDeprecated(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean insertFormTemplate(FormTemplate formTemplate) {
        try {
            String sql = "INSERT INTO form_templates VALUES(? ?)";
            jdbcTemplate.update(sql, formTemplate.getTitle(), formTemplate.getJson());
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public boolean deleteFormTemplate(FormTemplate formTemplate) {
        try {
            String sql = "DELETE FROM form_templates WHERE id = ?";
            jdbcTemplate.update(sql, formTemplate.getId());
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }
}
