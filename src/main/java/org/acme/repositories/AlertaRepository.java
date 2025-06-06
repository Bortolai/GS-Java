package org.acme.repositories;

import org.acme.entities.Alerta;
import org.acme.infrastructure.DatabaseConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlertaRepository implements CrudRepository<Alerta>{
    public static Logger logger = LogManager.getLogger(AlertaRepository.class);
    private List<Alerta> alertas = new ArrayList<>(List.of());

    @Override
    public void adicionar(Alerta object) {
        var query = "INSERT INTO T_ELO_ALERTA (DELETED, TT_ALERTA, LC_ALERTA, DC_ALERTA, RC_ALERTA) VALUES (?,?,?,?,?)";
        try(var conn = DatabaseConfig.getConnection())
        {
            var stmt = conn.prepareStatement(query);
            stmt.setBoolean(1, false);
            stmt.setString(2, object.getTitulo());
            stmt.setString(3, object.getLocalizacao());
            stmt.setString(4, object.getDescricao());
            stmt.setString(5, object.getRecomendacao());
            var result = stmt.executeUpdate();
            if(result > 0)
                logger.info("Alerta adicionado com sucesso!");
        }
        catch(SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
    }

    @Override
    public void atualizar(int id, Alerta alerta) {
        var query = "UPDATE T_ELO_ALERTA SET tt_alerta = ?, lc_alerta = ?, dc_alerta = ?, rc_alerta = ? WHERE id_alerta = ?";

        try (var conn = DatabaseConfig.getConnection()) {
            var stmt = conn.prepareStatement(query);
            // Definindo os parâmetros para o PreparedStatement
            stmt.setString(1, alerta.getTitulo());
            stmt.setString(2, alerta.getLocalizacao());
            stmt.setString(3, alerta.getDescricao());
            stmt.setString(4, alerta.getRecomendacao());
            stmt.setInt(5, id);

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
    public void remover(Alerta object) {
        alertas.remove(object);
    }

    @Override
    public void remover(int id) {
        alertas.removeIf(a -> a.getId() == id);
    }

    @Override
    public void delete(Alerta object) {
        object.setDeleted(true);
    }

    @Override
    public void deleteById(int id) {
        var query = "UPDATE T_ELO_ALERTA SET DELETED = 1 WHERE ID_ALERTA = ?";
        try(var conn =  DatabaseConfig.getConnection())
        {
            var stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            var result = stmt.executeUpdate();
            if(result > 0)
                logger.info("Alerta removido com sucesso!");
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
    }

    @Override
    public List<Alerta> listarTodos() {
        var alertasDb = new ArrayList<Alerta>();
        var query = "SELECT * FROM T_ELO_ALERTA";
        try (var connection = DatabaseConfig.getConnection())
        {
            var statement = connection.prepareStatement(query);
            var result = statement.executeQuery();
            while (result.next())
            {
                var alerta = new Alerta();
                alerta.setId(result.getInt("id_alerta"));
                alerta.setDeleted(result.getBoolean("deleted"));
                alerta.setTitulo(result.getString("tt_alerta"));
                alerta.setLocalizacao(result.getString("lc_alerta"));
                alerta.setDescricao(result.getString("dc_alerta"));
                alerta.setRecomendacao(result.getString("rc_alerta"));
                alertasDb.add(alerta);
            }
            connection.close();
            return alertasDb;
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return null;
    }

    @Override
    public List<Alerta> listar() {
        var alertasDb = new ArrayList<Alerta>();
        var query = "SELECT * FROM T_ELO_ALERTA WHERE DELETED = 0";
        try (var connection = DatabaseConfig.getConnection())
        {
            var statement = connection.prepareStatement(query);
            var result = statement.executeQuery();
            while (result.next())
            {
                var alerta = new Alerta();
                alerta.setId(result.getInt("id_alerta"));
                alerta.setDeleted(result.getBoolean("deleted"));
                alerta.setTitulo(result.getString("tt_alerta"));
                alerta.setLocalizacao(result.getString("lc_alerta"));
                alerta.setDescricao(result.getString("dc_alerta"));
                alerta.setRecomendacao(result.getString("rc_alerta"));
                alertasDb.add(alerta);
            }
            return alertasDb;
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return null;
    }

    @Override
    public Optional<Alerta> buscarPorId(int id) {
        var query = "SELECT * from T_ELO_ALERTA WHERE ID_ALERTA = ?";
        try(var connection = DatabaseConfig.getConnection()){
            var stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            var result = stmt.executeQuery();
            if(result.next()){
                var alerta = new Alerta();
                alerta.setId(result.getInt("id_alerta"));
                alerta.setDeleted(result.getBoolean("deleted"));
                alerta.setTitulo(result.getString("tt_alerta"));
                alerta.setLocalizacao(result.getString("lc_alerta"));
                alerta.setDescricao(result.getString("dc_alerta"));
                alerta.setRecomendacao(result.getString("rc_alerta"));
                return Optional.of(alerta);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return Optional.empty();
    }
}
