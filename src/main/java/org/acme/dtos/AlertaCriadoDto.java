package org.acme.dtos;

public class AlertaCriadoDto {
    private String titulo;
    private String localizacao;
    private String descricao;
    private String recomendacao;

    public AlertaCriadoDto() {
    }

    public AlertaCriadoDto(String titulo, String localizacao, String descricao, String recomendacao) {
        this.titulo = titulo;
        this.localizacao = localizacao;
        this.descricao = descricao;
        this.recomendacao = recomendacao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getRecomendacao() {
        return recomendacao;
    }

    public void setRecomendacao(String recomendacao) {
        this.recomendacao = recomendacao;
    }
}
