package gob.issste.gys.repository;

import gob.issste.gys.model.Programatica;

import java.math.BigInteger;
import java.util.List;

public interface IProgramaticaRepository {
    String GET_PROGRAMATICA_DATA =
            "SELECT *,rowid FROM gys_programatica\r\n" +
            "ORDER BY rowid\r\n" +
            "SKIP ?\r\n" +
            "FIRST ?";
    List<Programatica> findAllProgramatica(int page, int size);

    String GET_PROGRAMATICA_DATA_BY_TYPE =
            "SELECT *,rowid FROM gys_programatica\r\n" +
                    "WHERE tipo = ?\r\n"+
                    "ORDER BY rowid\r\n" +
                    "SKIP ?\r\n" +
                    "FIRST ?";
    List<Programatica> findAllProgramaticaByType(String tipo, int page, int size);
    String GET_PROGRAMATICA_DATA_SIZE =
            "SELECT COUNT(*) FROM gys_programatica";
    Long getProgramSize();

    final String UPDATE_PROGRAMATICA_DATA =
            "UPDATE gys_programatica\r\n" +
                    "   SET anio= ?,\r\n" +
                    "       gf =  ?,\r\n" +
                    "       fn =  ?,\r\n" +
                    "       sf =  ?,\r\n" +
                    "       pg =  ?,\r\n" +
                    "       ff =  ?,\r\n" +
                    "       ai =  ?,\r\n" +
                    "       ap =  ?,\r\n" +
                    "       sp =  ?,\r\n" +
                    "       r =   ?,\r\n" +
                    "       ur =  ?,\r\n" +
                    "       ct =  ?,\r\n" +
                    "       aux = ?,\r\n" +
                    "       mun = ?,\r\n" +
                    "       fd =  ?,\r\n" +
                    "       ptda= ?,\r\n" +
                    "       sbptd=  ?,\r\n" +
                    "       tp =  ?,\r\n" +
                    "       tpp = ?,\r\n" +
                    "       fdo = ?,\r\n" +
                    "       area= ?,\r\n" +
                    "       tipo= ?\r\n" +
                    "WHERE rowid = ?";

    int upadteProgramatica(Programatica programatica);

}
