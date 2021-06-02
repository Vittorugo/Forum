package br.com.alura.forum.controller;

import br.com.alura.forum.dto.AtualizarTopicoForm;
import br.com.alura.forum.dto.DetalhesTopicoDto;
import br.com.alura.forum.dto.TopicoDto;
import br.com.alura.forum.dto.TopicoForm;
import br.com.alura.forum.entities.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicosRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicosRespository repository;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    public List<TopicoDto> listar(String nomeCurso) {
        if(nomeCurso == null){
            List<Topico> topicos = repository.findAll();
            return TopicoDto.converter(topicos);
        } else {
            List<Topico> topicos = repository.findByCursoNome(nomeCurso);
            return TopicoDto.converter(topicos);
        }
    }

    @PostMapping()
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm topicoForm, UriComponentsBuilder uriBuilder) {
        Topico novoTopico = topicoForm.converter(cursoRepository);
        repository.save(novoTopico);

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(novoTopico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(novoTopico));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalhesTopicoDto> detalhar(@PathVariable("id") Long codigo) {

        Optional<Topico> topico = repository.findById(codigo);
        if(topico.isPresent()){
            return ResponseEntity.ok(new DetalhesTopicoDto(topico.get()));
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizarTopicoForm form) {
        Optional<Topico> optional = repository.findById(id);
        if(optional.isPresent()){
            Topico topico = form.atualizar(id, repository);
            return ResponseEntity.ok(new TopicoDto(topico));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> remover(@PathVariable("id") Long codigo) {
        Optional<Topico> optional = repository.findById(codigo);
        if(optional.isPresent()){
            repository.deleteById(codigo);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
