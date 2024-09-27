package gob.issste.gys.repository.jdbc;

import gob.issste.gys.JdbcTemplateDemo01Application;
import gob.issste.gys.model.Programatica;
import gob.issste.gys.repository.IProgramaticaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class JdbcProgramaticaRepository implements IProgramaticaRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;
    Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

    public List<Programatica> findAllProgramatica(int page, int size){
        logger.info(GET_PROGRAMATICA_DATA);
        return jdbcTemplate.query(GET_PROGRAMATICA_DATA, ps -> {
            ps.setInt(1, page);
            ps.setInt(2, size);
        }, BeanPropertyRowMapper.newInstance(Programatica.class));
    }

    @Override
    public List<Programatica> findAllProgramaticaByType(String tipo, int page, int size) {
        return jdbcTemplate.query(GET_PROGRAMATICA_DATA_BY_TYPE, ps -> {
            ps.setString(1, tipo);
            ps.setInt(2, page);
            ps.setInt(3, size);
        }, BeanPropertyRowMapper.newInstance(Programatica.class));
    }

    public Long getProgramSize(){
        return jdbcTemplate.queryForObject(GET_PROGRAMATICA_DATA_SIZE, Long.class);
    }

    public int upadteProgramatica(Programatica programatica) {
        logger.info(UPDATE_PROGRAMATICA_DATA);
        return jdbcTemplate.update(UPDATE_PROGRAMATICA_DATA,
                new Object[] {
                        programatica.getAnio(),
                        programatica.getGf(),
                        programatica.getFn(),
                        programatica.getSf(),
                        programatica.getPg(),
                        programatica.getFf(),
                        programatica.getAi(),
                        programatica.getAp(),
                        programatica.getSp(),
                        programatica.getR(),
                        programatica.getUr(),
                        programatica.getCt(),
                        programatica.getAux(),
                        programatica.getMun(),
                        programatica.getFd(),
                        programatica.getPtda(),
                        programatica.getSbptd(),
                        programatica.getTp(),
                        programatica.getTpp(),
                        programatica.getFdo(),
                        programatica.getArea(),
                        programatica.getTipo(),
                        programatica.getRowid()
                });
    }
}
