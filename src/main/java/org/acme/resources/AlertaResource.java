package org.acme.resources;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dtos.AlertaCriadoDto;
import org.acme.entities.Alerta;
import org.acme.repositories.AlertaRepository;
import org.acme.services.AlertaService;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;
import java.util.Optional;

@Path("/alerta")
public class AlertaResource {
    private AlertaRepository alertaRepository = new AlertaRepository();
    private final AlertaService alertaService = new AlertaService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Alerta> getAlertas() {return alertaRepository.listarTodos();}

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAlerta(@PathParam("id") int id){
        var alerta = alertaRepository.buscarPorId(id);

        return alerta.map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @GET
    @Path("/listar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarAlertas() {
        List<Alerta> alertas = alertaRepository.listar(); // implemente esse metodo
        return Response.ok(alertas).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAlerta(Alerta alerta){
        if(!alertaService.validateAlerta(alerta))
            return Response.status(Response.Status.BAD_REQUEST).build();

        alertaRepository.adicionar(alerta);
        return Response.status(Response.Status.CREATED)
                .entity(alerta)
                .build();
    }

    @POST
    @Path("/criar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response criarAlerta(AlertaCriadoDto dto) {
        if (dto.getTitulo() == null || dto.getTitulo().trim().isEmpty() ||
                dto.getDescricao() == null || dto.getDescricao().trim().isEmpty() ||
                dto.getLocalizacao() == null || dto.getLocalizacao().trim().isEmpty() ||
                dto.getRecomendacao() == null || dto.getRecomendacao().trim().isEmpty()) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Os campos não podem estar vazios ou conter apenas espaços.")
                    .build();
        }


        Alerta alerta = new Alerta();
        alerta.setTitulo(dto.getTitulo());
        alerta.setDescricao(dto.getDescricao());
        alerta.setLocalizacao(dto.getLocalizacao());
        alerta.setRecomendacao(dto.getRecomendacao());

        alertaRepository.adicionar(alerta);

        return Response.status(Response.Status.CREATED)
                .entity(alerta)
                .build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response atualizarAlerta(@PathParam("id") int id, AlertaCriadoDto dto){
        Optional<Alerta> alertaOptional = alertaRepository.buscarPorId(id);

        if(alertaOptional.isEmpty()) {
            return Response.status(RestResponse.Status.NOT_FOUND).entity("Alerta não encontrado").build();
        }

        Alerta alerta = alertaOptional.get();

        if (dto.getTitulo() != null && !dto.getTitulo().isEmpty()) {
            alerta.setTitulo(dto.getTitulo());
        }

        if (dto.getDescricao() != null && !dto.getDescricao().isEmpty()) {
            alerta.setDescricao(dto.getDescricao());
        }

        if (dto.getLocalizacao() != null && !dto.getLocalizacao().isEmpty()) {
            alerta.setLocalizacao(dto.getLocalizacao());
        }

        if (dto.getRecomendacao() != null && !dto.getRecomendacao().isEmpty()) {
            alerta.setRecomendacao(dto.getRecomendacao());
        }

        alertaRepository.atualizar(id, alerta);

        return Response.ok(alerta).build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAlerta(@PathParam("id") int id){
        var alerta = alertaRepository.buscarPorId(id);

        if (alerta == null)
            return Response.status(RestResponse.Status.NOT_FOUND).build();

        alertaRepository.deleteById(id);
        return Response.noContent().build();
    }
}
