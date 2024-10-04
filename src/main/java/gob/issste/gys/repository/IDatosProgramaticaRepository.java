package gob.issste.gys.repository;

import gob.issste.gys.model.DatosProgramatica;
import gob.issste.gys.model.Presupuesto;

import java.sql.SQLException;
import java.util.List;

public interface IDatosProgramaticaRepository {

    public final String QUERY_GET_ALL_PROG_DATA = "SELECT * FROM gys_presupuesto_prog";

    public final String QUERY_GET_PROG_DATA_BY_ID_PRESUP = "SELECT * FROM gys_presupuesto_prog WHERE idpresupuesto=?";
    public List<DatosProgramatica> getProgDataByIdPresupuesto(int idpresupuesto);

    public final String QUERY_ADD_DATOS_PRESUP = "INSERT INTO gys_presupuesto_prog\n" +
            "(\n" +
            "  idpresupuesto,\n" +
            "  gf,\n" +
            "  fn,\n" +
            "  sf,\n" +
            "  pg,\n" +
            "  ff,\n" +
            "  ai,\n" +
            "  ap,\n" +
            "  sp,\n" +
            "  r,\n" +
            "  mun,\n" +
            "  fd,\n" +
            "  ptda,\n" +
            "  sbptd,\n" +
            "  tp,\n" +
            "  tpp,\n" +
            "  fdo,\n" +
            "  area,\n" +
            "  tipo\n" +
            ")\n" +
            "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    int save(DatosProgramatica datosProgramatica) throws SQLException;
}

