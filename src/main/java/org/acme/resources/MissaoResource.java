package org.acme.resources;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dtos.MissaoCriadaDto;
import org.acme.dtos.MissaoRespostaDto;
import org.acme.entities.Missao;
import org.acme.repositories.MissaoRepository;
import org.acme.services.MissaoService;
import org.jboss.resteasy.reactive.RestResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/missao")
public class MissaoResource {
    private MissaoRepository missaoRepository = new MissaoRepository();
    private final MissaoService missaoService = new MissaoService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Missao> getMissoes() {return missaoRepository.listarTodos();}

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMissao(@PathParam("id") int id){
        var missao = missaoRepository.buscarPorId(id);

        return missao.map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @GET
    @Path("/listar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarMissoes() {
        List<Missao> missoes = missaoRepository.listar();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        List<MissaoRespostaDto> respostaDtos = missoes.stream()
                .map(m -> new MissaoRespostaDto(
                        m.getId(),
                        m.getTitulo(),
                        m.getLocalizacao(),
                        m.getOrganizacao(),
                        m.getDescricao(),
                        m.getDataCriacao().format(formatter) // aqui pegamos da ENTIDADE Missao e formatamos
                ))
                .collect(Collectors.toList());

        return Response.ok(respostaDtos).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMissao(Missao missao){
        if(!missaoService.validateMissao(missao))
            return Response.status(Response.Status.BAD_REQUEST).build();

        missaoRepository.adicionar(missao);
        return Response.status(Response.Status.CREATED)
                .entity(missao)
                .build();
    }

    @POST
    @Path("/criar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response criarMissao(MissaoCriadaDto dto) {
        if (dto.getTitulo() == null || dto.getTitulo().trim().isEmpty() ||
                dto.getDescricao() == null || dto.getDescricao().trim().isEmpty() ||
                dto.getOrganizacao() == null || dto.getOrganizacao().trim().isEmpty() ||
                dto.getLocalizacao() == null || dto.getLocalizacao().trim().isEmpty()) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Os campos não podem estar vazios ou conter apenas espaços.")
                    .build();
        }

        Missao missao = new Missao();
        missao.setTitulo(dto.getTitulo());
        missao.setDescricao(dto.getDescricao());
        missao.setOrganizacao(dto.getOrganizacao());
        missao.setLocalizacao(dto.getLocalizacao());
        missao.setDataCriacao(LocalDateTime.now());

        missaoRepository.adicionar(missao);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String dataFormatada = missao.getDataCriacao().format(formatter);

        MissaoRespostaDto respostaDto = new MissaoRespostaDto(
                missao.getId(),
                missao.getTitulo(),
                missao.getDescricao(),
                missao.getOrganizacao(),
                missao.getLocalizacao(),
                dataFormatada
        );

        return Response.status(Response.Status.CREATED)
                .entity(respostaDto)
                .build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response atualizarMissao(@PathParam("id") int id, MissaoCriadaDto dto){
        Optional<Missao> missaoOptional = missaoRepository.buscarPorId(id);

        if(missaoOptional.isEmpty()) {
            return Response.status(RestResponse.Status.NOT_FOUND).entity("Missão não encontrada").build();
        }

        Missao notificacao = missaoOptional.get();

        if (dto.getTitulo() != null && !dto.getTitulo().isEmpty()) {
            notificacao.setTitulo(dto.getTitulo());
        }

        if (dto.getDescricao() != null && !dto.getDescricao().isEmpty()) {
            notificacao.setDescricao(dto.getDescricao());
        }

        if (dto.getLocalizacao() != null && !dto.getLocalizacao().isEmpty()) {
            notificacao.setLocalizacao(dto.getLocalizacao());
        }

        if (dto.getOrganizacao() != null && !dto.getOrganizacao().isEmpty()) {
            notificacao.setOrganizacao(dto.getOrganizacao());
        }

        missaoRepository.atualizar(id, notificacao);

        return Response.ok(notificacao).build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteMissao(@PathParam("id") int id){
        var missao = missaoRepository.buscarPorId(id);

        if (missao == null)
            return Response.status(RestResponse.Status.NOT_FOUND).build();

        missaoRepository.deleteById(id);
        return Response.noContent().build();
    }
}
