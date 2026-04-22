package edziekanat.isi.repositories;

import edziekanat.isi.models.Form;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FormRepository {
    private final JdbcTemplate jdbcTemplate; //move this to a signle file

    public FormRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean insertFormTemplate(Form form) {
        try {
            String sql = "INSERT INTO form_templates VALUES(? ?)";
            jdbcTemplate.update(sql, form.getFormName(), form.getJson());
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public boolean deleteFormTemplate(Form form) {
        try {
            String sql = "DELETE FROM form_templates WHERE id = ?";
            jdbcTemplate.update(sql, form.getId());
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }
}
