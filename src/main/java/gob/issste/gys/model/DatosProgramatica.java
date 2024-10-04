package gob.issste.gys.model;

public class DatosProgramatica {
    private int id;
    private int idpresupuesto;
    private String gf;
    private String fn;
    private String sf;
    private String pg;
    private String ff;
    private String ai;
    private String ap;
    private String sp;
    private String r;
    private String mun;
    private String fd;
    private String ptda;
    private String sbptd;
    private String tp;
    private String tpp;
    private String fdo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdpresupuesto() {
        return idpresupuesto;
    }

    public void setIdpresupuesto(int idpresupuesto) {
        this.idpresupuesto = idpresupuesto;
    }

    public String getGf() {
        return gf;
    }

    public void setGf(String gf) {
        this.gf = gf;
    }

    public String getFn() {
        return fn;
    }

    public void setFn(String fn) {
        this.fn = fn;
    }

    public String getSf() {
        return sf;
    }

    public void setSf(String sf) {
        this.sf = sf;
    }

    public String getPg() {
        return pg;
    }

    public void setPg(String pg) {
        this.pg = pg;
    }

    public String getFf() {
        return ff;
    }

    public void setFf(String ff) {
        this.ff = ff;
    }

    public String getAi() {
        return ai;
    }

    public void setAi(String ai) {
        this.ai = ai;
    }

    public String getAp() {
        return ap;
    }

    public void setAp(String ap) {
        this.ap = ap;
    }

    public String getSp() {
        return sp;
    }

    public void setSp(String sp) {
        this.sp = sp;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getMun() {
        return mun;
    }

    public void setMun(String mun) {
        this.mun = mun;
    }

    public String getFd() {
        return fd;
    }

    public void setFd(String fd) {
        this.fd = fd;
    }

    public String getPtda() {
        return ptda;
    }

    public void setPtda(String ptda) {
        this.ptda = ptda;
    }

    public String getSbptd() {
        return sbptd;
    }

    public void setSbptd(String sbptd) {
        this.sbptd = sbptd;
    }

    public String getTp() {
        return tp;
    }

    public void setTp(String tp) {
        this.tp = tp;
    }

    public String getTpp() {
        return tpp;
    }

    public void setTpp(String tpp) {
        this.tpp = tpp;
    }

    public String getFdo() {
        return fdo;
    }

    public void setFdo(String fdo) {
        this.fdo = fdo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    private String area;
    private String tipo;

    public DatosProgramatica(){}

    public DatosProgramatica(int idpresupuesto, String gf, String fn, String sf, String pg, String ff,
                             String ai, String ap, String sp, String r, String mun, String fd, String ptda,
                             String sbptd, String tp, String tpp, String fdo, String area, String tipo) {
        super();
        this.idpresupuesto = idpresupuesto;
        this.gf = gf;
        this.fn = fn;
        this.sf = sf;
        this.pg = pg;
        this.ff = ff;
        this.ai = ai;
        this.ap = ap;
        this.sp = sp;
        this.r = r;
        this.mun = mun;
        this.fd = fd;
        this.ptda = ptda;
        this.sbptd = sbptd;
        this.tp = tp;
        this.tpp = tpp;
        this.fdo = fdo;
        this.area = area;
        this.tipo = tipo;
    }
    public DatosProgramatica(int id, int idpresupuesto, String gf, String fn, String sf, String pg, String ff,
                             String ai, String ap, String sp, String r, String mun, String fd, String ptda,
                             String sbptd, String tp, String tpp, String fdo, String area, String tipo) {
        super();
        this.id = id;
        this.idpresupuesto = idpresupuesto;
        this.gf = gf;
        this.fn = fn;
        this.sf = sf;
        this.pg = pg;
        this.ff = ff;
        this.ai = ai;
        this.ap = ap;
        this.sp = sp;
        this.r = r;
        this.mun = mun;
        this.fd = fd;
        this.ptda = ptda;
        this.sbptd = sbptd;
        this.tp = tp;
        this.tpp = tpp;
        this.fdo = fdo;
        this.area = area;
        this.tipo = tipo;
    }


}
