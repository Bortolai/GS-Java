package org.acme.repositories;

import org.acme.entities.Usuario;
import org.acme.infrastructure.DatabaseConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepository implements CrudRepository<Usuario>{
    public static Logger logger = LogManager.getLogger(UsuarioRepository.class);

    List<Usuario> usuarios = new ArrayList<>(List.of());

    @Override
    public void adicionar(Usuario usuario) {
        var query = "INSERT INTO T_ELO_USUARIO (DELETED, CTG_USUARIO, NM_USUARIO, EM_USUARIO, TL_USUARIO) VALUES (?, ?, ?, ?, ?)";

        try (var conn = DatabaseConfig.getConnection();
             var stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setBoolean(1, false);
            stmt.setString(2, usuario.getCategoria());
            stmt.setString(3, usuario.getNome());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getTelefone());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Falha ao inserir usuário, nenhuma linha afetada.");
            }

            try (var generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    usuario.setId(generatedKeys.getInt(1));
                } else {
                    throw new RuntimeException("Falha ao obter o ID gerado para o usuário.");
                }
            }

        } catch (SQLException e) {
            logger.error("Erro ao adicionar usuário", e);
            throw new RuntimeException("Erro ao adicionar usuário", e);
        }
    }

    public void cadastrarUsuario(Usuario usuario, String senha) {
        String insertUsuario = "INSERT INTO T_ELO_USUARIO (DELETED, CTG_USUARIO, NM_USUARIO, EM_USUARIO, TL_USUARIO) " +
                "VALUES (?, ?, ?, ?, ?)";
        String insertAutentica = "INSERT INTO T_ELO_AUTENTICA (DELETED, SENHA, ID_USUARIO) VALUES (?, ?, ?)";

        try (var conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);  // <- garante transação

            // Inserir usuário
            var stmtUsuario = conn.prepareStatement(insertUsuario, new String[] { "ID_USUARIO" });
            stmtUsuario.setBoolean(1, false);
            stmtUsuario.setString(2, usuario.getCategoria());
            stmtUsuario.setString(3, usuario.getNome());
            stmtUsuario.setString(4, usuario.getEmail());
            stmtUsuario.setString(5, usuario.getTelefone());
            stmtUsuario.executeUpdate();

            // Recuperar ID gerado
            var rs = stmtUsuario.getGeneratedKeys();
            int idUsuario = 0;
            if (rs.next()) {
                idUsuario = rs.getInt(1);
            } else {
                conn.rollback();
                throw new SQLException("Falha ao obter ID do usuário.");
            }

            // Inserir autenticação
            var stmtAutentica = conn.prepareStatement(insertAutentica);
            stmtAutentica.setBoolean(1, false);
            stmtAutentica.setString(2, senha);
            stmtAutentica.setInt(3, idUsuario);
            stmtAutentica.executeUpdate();

            conn.commit();
            logger.info("Usuário e autenticação cadastrados com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Erro ao cadastrar usuário", e);
            throw new RuntimeException("Erro ao cadastrar usuário", e);
        }
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        String query = "SELECT u.id_usuario, u.nm_usuario, u.em_usuario, u.ctg_usuario, u.tl_usuario " +
                "FROM T_ELO_USUARIO u WHERE u.em_usuario = ?";

        try (var conn = DatabaseConfig.getConnection();
             var stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id_usuario"));
                usuario.setNome(rs.getString("nm_usuario"));
                usuario.setEmail(rs.getString("em_usuario"));
                usuario.setCategoria(rs.getString("ctg_usuario"));
                usuario.setTelefone(rs.getString("tl_usuario"));

                return Optional.of(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Usuario> buscarPorTelefone(String telefone) {
        String query = "SELECT u.id_usuario, u.nm_usuario, u.em_usuario, u.ctg_usuario, u.tl_usuario " +
                "FROM T_ELO_USUARIO u WHERE u.tl_usuario = ?";

        try (var conn = DatabaseConfig.getConnection();
             var stmt = conn.prepareStatement(query)) {

            stmt.setString(1, telefone);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id_usuario"));
                usuario.setNome(rs.getString("nm_usuario"));
                usuario.setEmail(rs.getString("em_usuario"));
                usuario.setCategoria(rs.getString("ctg_usuario"));
                usuario.setTelefone(rs.getString("tl_usuario"));

                return Optional.of(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Usuario> fazerLogin(String email, String senha) {
        String query = """
        SELECT u.ID_USUARIO, u.NM_USUARIO, u.EM_USUARIO, u.CTG_USUARIO, u.TL_USUARIO
        FROM T_ELO_USUARIO u
        INNER JOIN T_ELO_AUTENTICA a ON u.ID_USUARIO = a.ID_USUARIO
        WHERE u.EM_USUARIO = ? AND a.SENHA = ? AND u.DELETED = 0 AND a.DELETED = 0
        """;

        try (var conn = DatabaseConfig.getConnection();
             var stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);

            var rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("ID_USUARIO"));
                usuario.setNome(rs.getString("NM_USUARIO"));
                usuario.setEmail(rs.getString("EM_USUARIO"));
                usuario.setCategoria(rs.getString("CTG_USUARIO"));
                usuario.setTelefone(rs.getString("TL_USUARIO"));

                return Optional.of(usuario);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Erro ao fazer login", e);
        }

        return Optional.empty();
    }

    @Override
    public void atualizar(int id, Usuario usuario) {
        String query = "UPDATE T_ELO_USUARIO SET nm_usuario = ?, em_usuario = ?, ctg_usuario = ?, tl_usuario = ? WHERE id_usuario = ?";

        try (var conn = DatabaseConfig.getConnection();
             var stmt = conn.prepareStatement(query)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getCategoria());
            stmt.setString(4, usuario.getTelefone());
            stmt.setInt(5, id);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Usuário atualizado com sucesso!");
            } else {
                System.out.println("Nenhum usuário encontrado com o ID fornecido.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizarSenhaUsuario(int id, String novaSenha) {
        String query = "UPDATE T_ELO_AUTENTICA SET senha = ? WHERE id_usuario = ?";

        try (var conn = DatabaseConfig.getConnection();
             var stmt = conn.prepareStatement(query)) {

            stmt.setString(1, novaSenha);
            stmt.setInt(2, id);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Senha atualizada com sucesso!");
            } else {
                System.out.println("Nenhum registro de senha encontrado para o usuário.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remover(Usuario object) {
        usuarios.remove(object);
    }

    @Override
    public void remover(int id) {
        usuarios.removeIf(u -> u.getId() == id);
    }

    @Override
    public void delete(Usuario object) {
        object.setDeleted(true);
    }

    @Override
    public void deleteById(int id) {
        String softDeleteUsuarioQuery = "UPDATE T_ELO_USUARIO SET DELETED = 1 WHERE ID_USUARIO = ?";
        String softDeleteAutenticaQuery = "UPDATE T_ELO_AUTENTICA SET DELETED = 1 WHERE ID_USUARIO = ?";

        try (var conn = DatabaseConfig.getConnection()) {

            // Marcar usuário como deletado
            try (var stmtUsuario = conn.prepareStatement(softDeleteUsuarioQuery)) {
                stmtUsuario.setInt(1, id);
                stmtUsuario.executeUpdate();
            }

            // Marcar autenticação como deletada (se sua tabela tiver esse campo)
            try (var stmtAutentica = conn.prepareStatement(softDeleteAutenticaQuery)) {
                stmtAutentica.setInt(1, id);
                stmtAutentica.executeUpdate();
            }

            logger.info("Usuário e autenticação marcados como deletados!");

        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Erro ao fazer soft delete", e);
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        var usuariosDb = new ArrayList<Usuario>();
        var query = "SELECT * FROM T_ELO_USUARIO";
        try (var connection = DatabaseConfig.getConnection())
        {
            var statement = connection.prepareStatement(query);
            var result = statement.executeQuery();
            while (result.next())
            {
                var usuario = new Usuario();
                usuario.setId(result.getInt("id_usuario"));
                usuario.setDeleted(result.getBoolean("deleted"));
                usuario.setNome(result.getString("nm_usuario"));
                usuario.setEmail(result.getString("em_usuario"));
                usuario.setCategoria(result.getString("ctg_usuario"));
                usuario.setTelefone(result.getString("tl_usuario"));
                usuariosDb.add(usuario);
            }
            return usuariosDb;
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return null;
    }

    @Override
    public List<Usuario> listar() {
        var usuariosDb = new ArrayList<Usuario>();
        var query = "SELECT * FROM T_ELO_USUARIO WHERE DELETED = 0";
        try (var connection = DatabaseConfig.getConnection())
        {
            var statement = connection.prepareStatement(query);
            var result = statement.executeQuery();
            while (result.next())
            {
                var usuario = new Usuario();
                usuario.setId(result.getInt("id_usuario"));
                usuario.setDeleted(result.getBoolean("deleted"));
                usuario.setNome(result.getString("nm_usuario"));
                usuario.setEmail(result.getString("em_usuario"));
                usuario.setCategoria(result.getString("ctg_usuario"));
                usuario.setTelefone(result.getString("tl_usuario"));
                usuariosDb.add(usuario);
            }
            return usuariosDb;
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return null;
    }


    @Override
    public Optional<Usuario> buscarPorId(int id) {
        var query = "SELECT * from T_ELO_USUARIO WHERE ID_USUARIO = ?";
        try(var connection = DatabaseConfig.getConnection()){
            var stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            var result = stmt.executeQuery();
            if(result.next()){
                var usuario = new Usuario();
                usuario.setId(result.getInt("id_usuario"));
                usuario.setDeleted(result.getBoolean("deleted"));
                usuario.setNome(result.getString("nm_usuario"));
                usuario.setEmail(result.getString("em_usuario"));
                usuario.setCategoria(result.getString("ctg_usuario"));
                usuario.setTelefone(result.getString("tl_usuario"));
                return Optional.of(usuario);
            }
        }
        catch(SQLException e){
            logger.error(e);
        }
        return Optional.empty();
    }

}
