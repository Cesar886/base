package com.exa.base.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.exa.base.model.Usuario;

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
                    String role = postgresTemplate.queryForObject(query, String.class, roleId);
                    grabaRoles += String.valueOf(role) + "-";
                }
            }

            Object[] parametros = new Object[] {
                    objeto.getNombre(), objeto.getUserName(), objeto.getEmail(), objeto.getPassword(), grabaRoles
            };

            query = "INSERT INTO USUARIO(NOMBRE,USER_NAME,EMAIL,PASSWORD,ROLES,ACTIVO) VALUES(?, ?, ?, ?, ?, TRUE)";

            if (postgresTemplate.update(query, parametros) >= 1) {
                query = "SELECT ID FROM USUARIO WHERE NOMBRE = ? AND EMAIL = ?";

                parametros = new Object[] {
                        objeto.getNombre(), objeto.getEmail()
                };

                // int usuarioIdDos = postgresTemplate.queryForObject(query, Integer.class, parametros);
                Integer tmpId = postgresTemplate.queryForObject(query, Integer.class, parametros);
                int usuarioId = (tmpId != null) ? tmpId : 0;

                if (roles != null) {
                    for (int roleId : roles) {
                        query = "INSERT INTO USUARIO_ROLE(USUARIO_ID, ROLE_ID) VALUES( ?, ?)";
                        postgresTemplate.update(query, new Object[] { usuarioId, roleId });
                    }
                }
                ok = true;
            }
        }
        return ok;
    }

    public boolean buscaEmail(String email) {
        boolean ok = false;
        String query = "SELECT COUNT(*) FROM USUARIO WHERE EMAIL = ?";

        try {
            Integer tmpId = postgresTemplate.queryForObject(query, Integer.class, email);
            int existe = (tmpId != null) ? tmpId : 0;
            if (existe > 0) {
                ok = true;
            }
        } catch (Exception e) {
            System.out.println("ERROR : UsuarioDao | buscaEmail | " + e);
        }

        return ok;
    }
}
