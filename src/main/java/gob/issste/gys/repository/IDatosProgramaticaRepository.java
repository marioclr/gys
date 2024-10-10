package gob.issste.gys.repository;

import gob.issste.gys.model.DatosProgramatica;
import gob.issste.gys.model.Presupuesto;

import java.sql.SQLException;
import java.util.List;

public interface IDatosProgramaticaRepository {

    public final String QUERY_GET_PROG_DATA_BY_ID_PRESUP = "SELECT * FROM gys_presupuesto_prog WHERE idpresupuesto=?";
    public DatosProgramatica getProgDataByIdPresupuesto(int idpresupuesto);
    public String QUERY_GET_PRESUPUESTO_BY_ID_W_PROG_DATA     = "Select GPP.id, P.id AS idpresupuesto, P.anio, P.mes, P.saldo, P.idDelegacion, D.n_div_geografica,\n" +
            "NVL(P.id_centro_trabajo, '00000') id_centro_trabajo, P.idTipoPresup, T.clave clave_tipo_presup, T.descripcion descripcion_tipo_presup,\n" +
            "NVL(C.id_centro_trabajo, '') Clave, NVL(n_centro_trabajo, '') Descripcion, NVL(id_tipo_ct, '') Tipo, NVL(id_zona, '') Zona,\n" +
            "GPP.gf, GPP.fn,GPP.sf,GPP.pg,GPP.ff,GPP.ai,GPP.ap,GPP.sp,GPP.r,GPP.mun,GPP.fd,GPP.ptda,GPP.sbptd,GPP.tp,GPP.tpp,GPP.fdo,GPP.area,GPP.tipo\n" +
            "From gys_presupuesto P Left Join m4t_centros_trab C ON P.id_centro_trabajo = C.id_centro_trabajo, gys_tip_presupuesto T, m4t_delegaciones D, gys_presupuesto_prog GPP\n" +
            "Where P.idTipoPresup=T.id\n" +
            "And P.idDelegacion=D.id_div_geografica\n" +
            "And GPP.idpresupuesto = P.id\n" +
            "And P.id= ?";
    Presupuesto getElementByIdWProgData(int idDeleg);

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

    public final String QUERY_UPDATE_DATOS_PROG = "UPDATE gys_presupuesto_prog\n" +
            "SET \n" +
            "gf = ?,\n" +
            "fn = ?,\n" +
            "sf = ?,\n" +
            "pg = ?,\n" +
            "ff = ?,\n" +
            "ai = ?,\n" +
            "ap = ?,\n" +
            "sp = ?,\n" +
            "r = ?,\n" +
            "mun = ?,\n" +
            "fd = ?,\n" +
            "ptda = ?,\n" +
            "sbptd = ?,\n" +
            "tp = ?,\n" +
            "tpp = ?,\n" +
            "fdo = ?,\n" +
            "area = ?,\n" +
            "tipo = ?\n" +
            "WHERE id = ?";

    int update(DatosProgramatica datosProgramatica) throws SQLException;
}

