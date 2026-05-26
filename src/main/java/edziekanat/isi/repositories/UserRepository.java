package edziekanat.isi.repositories;

import edziekanat.isi.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    /*
    private final JdbcTemplate jdbcTemplate; //move this to a signle file

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Integer> userRoleMapper = (rs, rowNum) ->
            rs.getInt("role_id");

    private final RowMapper<String> passwordMapper = (rs, rowNum) -> rs.getString("password");

    public User findById(int id) {
        String sql = "SELECT * FROM app_user WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, userRowMapper, id);
    }

    public User credentialsCorrect(LoginData loginData) {
        try {
            String sql = "SELECT password FROM app_user WHERE email = ?";
            String hashedPassword = jdbcTemplate.queryForObject(sql, passwordMapper, loginData.getEmail());

            if(!BCrypt.checkpw(loginData.getPassword(), hashedPassword)) return null;
            else {
                sql = "SELECT * FROM app_user WHERE email = ?";
                return jdbcTemplate.queryForObject(sql, userRowMapper, loginData.getEmail());
            }
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public User getFromSession(HttpSession session) {
        User user;
        try {
            user = jdbcTemplate.queryForObject("SELECT * FROM app_user WHERE id = ?",
                    userRowMapper, (int) session.getAttribute("id"));
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return null;
        }

        return user;
    }


    public User.UserPermissions getUserPermisions(int id) {
        try {
            User.UserPermissions userPermissions;

            String sql = "SELECT role_id FROM app_user WHERE id = ?";
            int roleId = jdbcTemplate.queryForObject(sql, userRoleMapper, id);

            switch (roleId) {
                case 0 -> userPermissions = User.UserPermissions.Admin;
                case 1 -> userPermissions = User.UserPermissions.Supervisor;
                case 2 -> userPermissions = User.UserPermissions.Worker;
                default -> userPermissions = User.UserPermissions.Student; //3
            }

            return userPermissions;
        } catch (DataAccessException e) {
            return null;
        }
    }
    */
}
