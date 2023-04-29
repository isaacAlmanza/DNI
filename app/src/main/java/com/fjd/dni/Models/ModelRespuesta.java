package com.fjd.dni.Models;

public class ModelRespuesta {

    private String codigo;
    private String nombre;
    private String tipoR;
    private String cabecera;
    private String foto_obligatorio;


    public ModelRespuesta(){

    }


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoR() {
        return tipoR;
    }

    public void setTipoR(String tipoR) {
        this.tipoR = tipoR;
    }

    public String getFoto_obligatorio() {
        return foto_obligatorio;
    }

    public void setFoto_obligatorio(String foto_obligatorio) {
        this.foto_obligatorio = foto_obligatorio;
    }

    public String getCabecera() {
        return cabecera;
    }

    public void setCabecera(String cabecera) {
        this.cabecera = cabecera;
    }



}
