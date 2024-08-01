package com.banorte.adapter.input;

import java.util.List;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;

import com.banorte.application.port.input.UserUseCase;
import com.banorte.domain.User;

import io.quarkus.cache.CacheResult;
import io.quarkus.security.Authenticated;
import io.smallrye.common.constraint.NotNull;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private static final Logger LOGGER = Logger.getLogger(UserResource.class);

    @Inject
    UserUseCase userUseCase;

    @POST
    public User createUser(@Valid CreateUserRequest request) {
        return userUseCase.createUser(request.getName());
    }

    @GET
    @Path("/secure/{id}")
    @Authenticated
    @RolesAllowed({"ROLE_NAME"})
    public Uni<User> getUserSecure(@PathParam("id") String id) {
        return userUseCase.getUser(id);
    }

    @GET
    @Path("/{id}")
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 1000)
    @Fallback(fallbackMethod = "fallbackGetUserById")
    public Uni<User> getUser(@PathParam("id") String id) {
        return userUseCase.getUser(id);
    }

    @GET
    @Path("/")
    @CacheResult(cacheName = "user-cache")
    public Uni<List<User>> getAll() {
        return userUseCase.getAll();
    }

    @GET
    @Path("/cb/{id}")
    @Retry(maxRetries = 3, delay = 1000)
    @Fallback(fallbackMethod = "defaultUser")
    public Uni<User> getUserByIdCB(@PathParam("id") String userId) {
        if (Math.random() > 0.5) {
            throw new RuntimeException("Simulated error");
        }
        return userUseCase.getUser(userId);
    }

    public Uni<User> defaultUser(String userId, RuntimeException e) {        
        LOGGER.error("defaultUser " + userId + " e->" + e.getMessage(), e);
        return Uni.createFrom().item(User::new);
    }

    public Uni<User> fallbackGetUserById(String id, Exception e) {
        // Lógica de fallback en caso de error o circuito abierto
        LOGGER.error("fallbackGetUserById "+ id+" e -> " + e.getMessage(), e);
        return Uni.createFrom().item(User::new);
    }

    public static class CreateUserRequest {        
        @NotNull
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
        private String name;

        // Constructor vacío necesario para la deserialización
        public CreateUserRequest() {
        }

        // Constructor con parámetros para facilitar la creación de instancias
        public CreateUserRequest(String name) {
            this.name = name;
        }

        // Getter
        public String getName() {
            return name;
        }

        // Setter
        public void setName(String name) {
            this.name = name;
        }
    }
}
