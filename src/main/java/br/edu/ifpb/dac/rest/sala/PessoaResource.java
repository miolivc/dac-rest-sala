
package br.edu.ifpb.dac.rest.sala;

import java.util.List;
import javax.ejb.Stateless;
import javax.ws.rs.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author miolivc
 * @mail miolivc@outlook.com
 * @since 05/10/2017
 */

@Path("pessoa")
@Stateless
public class PessoaResource {
    
    @PersistenceContext
    private EntityManager manager;
    
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response recuperar() {
        GenericEntity<List<Pessoa>> pessoas = new GenericEntity<List<Pessoa>>(
                manager.createQuery("SELECT p FROM Pessoa p", Pessoa.class)
                        .getResultList()){};
        return Response.ok().entity(pessoas).build();
    }
    
    @GET
    @Path("{nome}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response recuperar(@PathParam("nome") String nome) {
        Pessoa pessoa = manager
                .createQuery("SELECT p FROM Pessoa p WHERE p.nome=:nome", Pessoa.class)
                .setParameter("nome", nome)
                .getSingleResult();
        return Response.ok().entity(pessoa).build();
    }
    
    @POST
    @Path("form")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response adicionarPeloForm(@FormParam("cpf") String cpf, @FormParam("nome") String nome) {
        Pessoa pessoa = new Pessoa(cpf, nome);
        manager.persist(pessoa);
        return Response.ok().entity(pessoa).build();
    }
    
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response adicionar(Pessoa pessoa) {
        manager.persist(pessoa);
        return Response.ok().entity(pessoa).build();
    }
    
    
    @PUT
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response editar(Pessoa pessoa) {
        Pessoa pessoaAtual = manager.merge(pessoa);
        return Response.ok().entity(pessoaAtual).build();
    }
    
    @DELETE
    @Path("{cpf}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response remover(@PathParam("cpf") String cpf) {
        Pessoa pessoa = manager.find(Pessoa.class, cpf);
        manager.remove(pessoa);
        return Response.ok().entity(pessoa).build();
    }
    
}
