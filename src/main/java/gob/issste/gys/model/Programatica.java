package gob.issste.gys.model;

import java.io.Serial;

public class Programatica {

    private int rowid;
    private String anio;

    public int getRowid() {
        return rowid;
    }

    public void setRowid(int rowid) {
        this.rowid = rowid;
    }

    private String gf;
    private String fn;
    private String sf;
    private String pg;
    private String ff;
    private String ai;
    private String ap;
    private String sp;
    private String r;
    private String ur;
    private String ct;
    private String aux;
    private String mun;
    private String fd;
    private String ptda;
    private String sbptd;
    private String tp;
    private String tpp;
    private String fdo;
    private String area;
    private String tipo;

    public Programatica(int rowid, String anio, String gf, String fn, String sf, String pg, String ff, String ai, String ap,
                        String sp, String r, String ur, String ct, String aux, String mun, String fd, String ptda,
                        String sbptd, String tp, String tpp, String fdo, String area, String tipo) {
        this.rowid =rowid;
        this.anio = anio;
        this.gf = gf;
        this.fn = fn;
        this.sf = sf;
        this.pg = pg;
        this.ff = ff;
        this.ai = ai;
        this.ap = ap;
        this.sp = sp;
        this.r = r;
        this.ur = ur;
        this.ct = ct;
        this.aux = aux;
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

    public Programatica(){}


    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
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

    public String getUr() {
        return ur;
    }

    public void setUr(String ur) {
        this.ur = ur;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public String getAux() {
        return aux;
    }

    public void setAux(String aux) {
        this.aux = aux;
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

    @Override
    public String toString() {
        return "Programatica{" +
                "rowid='" +rowid + '\'' +
                "anio='" + anio + '\'' +
                ", gf='" + gf + '\'' +
                ", fn='" + fn + '\'' +
                ", sf='" + sf + '\'' +
                ", pg='" + pg + '\'' +
                ", ff='" + ff + '\'' +
                ", ai='" + ai + '\'' +
                ", ap='" + ap + '\'' +
                ", sp='" + sp + '\'' +
                ", r='" + r + '\'' +
                ", ur='" + ur + '\'' +
                ", ct='" + ct + '\'' +
                ", aux='" + aux + '\'' +
                ", mun='" + mun + '\'' +
                ", fd='" + fd + '\'' +
                ", ptda='" + ptda + '\'' +
                ", sbptd='" + sbptd + '\'' +
                ", tp='" + tp + '\'' +
                ", tpp='" + tpp + '\'' +
                ", fdo='" + fdo + '\'' +
                ", area='" + area + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
