package br.com.alura.forum.dto;

import br.com.alura.forum.entities.Topico;
import br.com.alura.forum.repository.TopicosRespository;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class AtualizarTopicoForm {

    @NotNull
    @NotEmpty
    private String titulo;

    @NotNull
    @NotEmpty
    private String mensagem;


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Topico atualizar(Long id, TopicosRespository repository) {
        Topico topico = repository.getById(id);

        topico.setTitulo(this.titulo);
        topico.setMensagem(this.mensagem);

        return topico;
    }
}
