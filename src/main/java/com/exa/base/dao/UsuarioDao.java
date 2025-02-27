package com.exa.base.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.exa.base.model.Usuario;
import org.springframework.dao.EmptyResultDataAccessException;

@Component
public class UsuarioDao {
    @Autowired
    private BCryptPasswordEncoder bCrypt;

    @Autowired
    private JdbcTemplate postgresTemplate;

    public boolean grabaUsuario(Usuario objeto, Integer[] roles) {
        boolean ok = false;

        if (!buscaEmail(objeto.getEmail())) {
            objeto.setPassword(bCrypt.encode(objeto.getPassword()));

            String query = "SELECT NOMBRE FROM ROLE WHERE ID = ?";
            String grabaRoles = "-";

            if (roles != null) {
                for (int roleId : roles) {
                    try {
                        String role = postgresTemplate.queryForObject(query, String.class, roleId);
                        grabaRoles += role + "-";
                    } catch (EmptyResultDataAccessException e) {
                        System.out.println("ERROR: No se encontró el rol con ID " + roleId);
                    }
                }
            }

            Object[] parametros = new Object[] {
                    objeto.getNombre(), objeto.getUserName(), objeto.getEmail(), objeto.getPassword(), grabaRoles
            };

            query = "INSERT INTO USUARIO(NOMBRE,USER_NAME,EMAIL,PASSWORD,ROLES,ACTIVO) VALUES(?, ?, ?, ?, ?, TRUE)";

            if (postgresTemplate.update(query, parametros) >= 1) {
                query = "SELECT ID FROM USUARIO WHERE NOMBRE = ? AND EMAIL = ?";
                parametros = new Object[] { objeto.getNombre(), objeto.getEmail() };
                System.out.println("Intentando registrar usuario: " + objeto.getEmail());

                List<Integer> ids = postgresTemplate.queryForList(query, Integer.class, parametros);
                int usuarioId = (!ids.isEmpty()) ? ids.get(0) : 0;

                if (usuarioId > 0 && roles != null) {
                    for (int roleId : roles) {
                        query = "INSERT INTO USUARIO_ROLE(USUARIO_ID, ROLE_ID) VALUES(?, ?)";
                        postgresTemplate.update(query, usuarioId, roleId);
                    }
                }
                ok = true;
            }
            System.out.println("Intentando registrar usuario: " + objeto.getEmail());
        }
        return ok;
    }

    public boolean buscaEmail(String email) {
        String query = "SELECT COUNT(*) FROM USUARIO WHERE EMAIL = ?";
        try {
            Integer count = postgresTemplate.queryForObject(query, Integer.class, email);
            return (count != null && count > 0);
        } catch (EmptyResultDataAccessException e) {
            return false; // Si no encuentra nada, simplemente retorna false
        } catch (Exception e) {
            System.out.println("ERROR : UsuarioDao | buscaEmail | " + e);
            return false;
        }
    }
}
