package com.cisco.cmad.blogs.rest;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.cisco.cmad.blogs.api.User;
import com.cisco.cmad.blogs.api.Users;
import com.cisco.cmad.blogs.service.UsersService;
import com.cisco.cmad.jwt.utils.KeyGenerator;
import com.cisco.cmad.jwt.utils.SecretKeyGenerator;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsersController {
    private static Users users = UsersService.getInstance();
    @Context
    private UriInfo uriInfo;

    private Logger logger = Logger.getLogger(getClass().getName());

    // @Inject
    private KeyGenerator keyGenerator = new SecretKeyGenerator();

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(User user) {
        users.create(user);
        return Response.ok().entity(user).build();
    }

    @GET
    @Path("/{userId}")
    public Response read(@PathParam("userId") String userId) {
        User user = users.read(userId);
        return Response.ok().entity(user).build();
    }

    @GET
    @Path("/")
    public Response readAllUsers() {
        List<User> matched;
        GenericEntity<List<User>> entities;
        matched = users.readAllUsers();
        entities = new GenericEntity<List<User>>(matched) {
        };
        return Response.ok().entity(entities).build();
    }

    @PUT
    @Path("/")
    public Response update(User user) {
        users.update(user);
        return Response.ok().entity(user).build();
    }

    @DELETE
    @Path("/{userId}")
    public Response delete(@PathParam("userId") String userId) {
        users.delete(userId);
        return Response.noContent().build();
    }

    @POST
    @Path("/{userId}") // used for authenticating user
    // @Path("/user")
    public Response authenticateUser(User user) {
        try {
            logger.info("#### login/password : " + user.getUserId() + "/" + user.getPassword());
            // Authenticate the user using the credentials provided
            users.authenticate(user.getUserId(), user.getPassword());

            // Issue a token for the user
            String token = issueToken(user.getUserId());

            // Return the token on the response
            return Response.ok().header(AUTHORIZATION, "Bearer " + token).build();

        } catch (Exception e) {
            return Response.status(UNAUTHORIZED).build();
        }
    }

    private String issueToken(String userId) {
        Key key = keyGenerator.generateKey();
        // todo: add timeout config
        String jwtToken = Jwts.builder().setSubject(userId).setIssuer(uriInfo.getAbsolutePath().toString())
                .setIssuedAt(new Date()).setExpiration(toDate(LocalDateTime.now().plusMinutes(15L)))
                .signWith(SignatureAlgorithm.HS512, key).compact();
        logger.info("#### generating token for a key : " + jwtToken + " - " + key);
        return jwtToken;
    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
