package org.acme.repositories;

import org.acme.entities.Alerta;
import org.acme.entities.Missao;
import org.acme.infrastructure.DatabaseConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MissaoRepository implements CrudRepository<Missao>{
    public static Logger logger = LogManager.getLogger(MissaoRepository.class);
    private List<Missao> missoes = new ArrayList<>(List.of());

    @Override
    public void adicionar(Missao object) {
        var query = "INSERT INTO T_ELO_MISSAO (DELETED, TT_MISSAO, DT_MISSAO, ORG_MISSAO, LC_MISSAO, DC_MISSAO) VALUES (?,?,?,?,?,?)";
        try(var conn = DatabaseConfig.getConnection())
        {
            var stmt = conn.prepareStatement(query);
            stmt.setBoolean(1, false);
            stmt.setString(2, object.getTitulo());
            stmt.setTimestamp(3, Timestamp.valueOf(object.getDataCriacao()));
            stmt.setString(4, object.getOrganizacao());
            stmt.setString(5, object.getLocalizacao());
            stmt.setString(6, object.getDescricao());
            var result = stmt.executeUpdate();
            if(result > 0)
                logger.info("Missão adicionada com sucesso!");
        }
        catch(SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
    }

    @Override
    public void atualizar(int id, Missao missao) {
        var query = "UPDATE T_ELO_MISSAO SET tt_missao = ?, dt_missao = ?, org_missao = ?, lc_missao = ?, dc_missao = ? WHERE id_missao = ?";

        try (var conn = DatabaseConfig.getConnection()) {
            var stmt = conn.prepareStatement(query);
            // Definindo os parâmetros para o PreparedStatement
            stmt.setString(1, missao.getTitulo());
            stmt.setTimestamp(2, Timestamp.valueOf(missao.getDataCriacao()));
            stmt.setString(3, missao.getOrganizacao());
            stmt.setString(4, missao.getLocalizacao());
            stmt.setString(5, missao.getDescricao());
            stmt.setInt(6, id);

            // Executando o update e verificando quantas linhas foram afetadas
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Alerta atualizado com sucesso!");
            } else {
                System.out.println("Nenhum alerta com o ID fornecido.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remover(Missao object) {
        missoes.remove(object);
    }

    @Override
    public void remover(int id) {
        missoes.removeIf(m -> m.getId() == id);
    }

    @Override
    public void delete(Missao object) {
        object.setDeleted(true);
    }

    @Override
    public void deleteById(int id) {
        var query = "UPDATE T_ELO_MISSAO SET DELETED = 1 WHERE ID_MISSAO = ?";
        try(var conn =  DatabaseConfig.getConnection())
        {
            var stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            var result = stmt.executeUpdate();
            if(result > 0)
                logger.info("Missão removida com sucesso!");
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
    }

    @Override
    public List<Missao> listarTodos() {
        var missoesDb = new ArrayList<Missao>();
        var query = "SELECT * FROM T_ELO_MISSAO";
        try (var connection = DatabaseConfig.getConnection())
        {
            var statement = connection.prepareStatement(query);
            var result = statement.executeQuery();
            while (result.next())
            {
                var missao = new Missao();
                missao.setId(result.getInt("id_missao"));
                missao.setDeleted(result.getBoolean("deleted"));
                missao.setTitulo(result.getString("tt_missao"));
                missao.setDataCriacao(result.getTimestamp("dt_missao").toLocalDateTime());
                missao.setOrganizacao(result.getString("org_missao"));
                missao.setLocalizacao(result.getString("lc_missao"));
                missao.setDescricao(result.getString("dc_missao"));
                missoesDb.add(missao);
            }
            return missoesDb;
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return null;
    }

    @Override
    public List<Missao> listar() {
        var missoesDb = new ArrayList<Missao>();
        var query = "SELECT * FROM T_ELO_MISSAO WHERE DELETED = 0";
        try (var connection = DatabaseConfig.getConnection())
        {
            var statement = connection.prepareStatement(query);
            var result = statement.executeQuery();
            while (result.next())
            {
                var missao = new Missao();
                missao.setId(result.getInt("id_missao"));
                missao.setDeleted(result.getBoolean("deleted"));
                missao.setTitulo(result.getString("tt_missao"));
                missao.setDataCriacao(result.getTimestamp("dt_missao").toLocalDateTime());
                missao.setOrganizacao(result.getString("org_missao"));
                missao.setLocalizacao(result.getString("lc_missao"));
                missao.setDescricao(result.getString("dc_missao"));
                missoesDb.add(missao);
            }
            return missoesDb;
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return null;
    }

    @Override
    public Optional<Missao> buscarPorId(int id) {
        var query = "SELECT * from T_ELO_MISSAO WHERE ID_MISSAO = ?";
        try(var connection = DatabaseConfig.getConnection()){
            var stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            var result = stmt.executeQuery();
            if(result.next()){
                var missao = new Missao();
                missao.setId(result.getInt("id_missao"));
                missao.setDeleted(result.getBoolean("deleted"));
                missao.setTitulo(result.getString("tt_missao"));
                missao.setDataCriacao(result.getTimestamp("dt_missao").toLocalDateTime());
                missao.setOrganizacao(result.getString("org_missao"));
                missao.setLocalizacao(result.getString("lc_missao"));
                missao.setDescricao(result.getString("dc_missao"));
                return Optional.of(missao);
            }
        }
        catch(SQLException e){
            logger.error(e);
        }
        return Optional.empty();
    }
}
