package gob.issste.gys.repository.jdbc;

import gob.issste.gys.JdbcTemplateDemo01Application;
import gob.issste.gys.model.DatosProgramatica;
import gob.issste.gys.repository.IDatosProgramaticaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class JdbcDatosProgramatica implements IDatosProgramaticaRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

    @Override
    public List<DatosProgramatica> getProgDataByIdPresupuesto(int idpresupuesto) {
        List<DatosProgramatica> datosProgramatica =
                jdbcTemplate.query(
                QUERY_GET_PROG_DATA_BY_ID_PRESUP, new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setInt(1, idpresupuesto);
                    }
                },
                 BeanPropertyRowMapper.newInstance(DatosProgramatica.class));
        return datosProgramatica;
    }

    @Override
    public int save(DatosProgramatica datosProgramatica) throws SQLException {
        logger.info(QUERY_ADD_DATOS_PRESUP);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator statementCreator = (Connection connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_ADD_DATOS_PRESUP, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, datosProgramatica.getIdpresupuesto());
            preparedStatement.setString(2, datosProgramatica.getGf());
            preparedStatement.setString(3, datosProgramatica.getFn());
            preparedStatement.setString(4, datosProgramatica.getSf());
            preparedStatement.setString(5, datosProgramatica.getPg());
            preparedStatement.setString(6, datosProgramatica.getFf());
            preparedStatement.setString(7, datosProgramatica.getAi());
            preparedStatement.setString(8, datosProgramatica.getAp());
            preparedStatement.setString(9, datosProgramatica.getSp());
            preparedStatement.setString(10, datosProgramatica.getR());
            preparedStatement.setString(11, datosProgramatica.getMun());
            preparedStatement.setString(12, datosProgramatica.getFd());
            preparedStatement.setString(13, datosProgramatica.getPtda());
            preparedStatement.setString(14, datosProgramatica.getSbptd());
            preparedStatement.setString(15, datosProgramatica.getTp());
            preparedStatement.setString(16, datosProgramatica.getTpp());
            preparedStatement.setString(17, datosProgramatica.getFdo());
            preparedStatement.setString(18, datosProgramatica.getArea());
            preparedStatement.setString(19, datosProgramatica.getTipo());
            return preparedStatement;
        };
        int updatesCount = jdbcTemplate.update(statementCreator, keyHolder);
        if (updatesCount == 1) {
            Number generatedKey = keyHolder.getKey();
            if (generatedKey == null) {
                throw new SQLException("Getting user id error.");
            }
            return generatedKey.intValue();
        }
        throw new SQLException("Expected one row insert.");
    }
}
