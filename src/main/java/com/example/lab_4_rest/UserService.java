package com.example.lab_4_rest;

import com.example.lab_4_rest.models.Token;
import com.example.lab_4_rest.models.User;
import com.example.lab_4_rest.models.UserBean;
import com.example.lab_4_rest.utils.ExceptionCodes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
public class UserService {

    private static final ObjectMapper mapper = new ObjectMapper();
    @EJB
    UserBean userBean;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String checkUsers_get(){
        return "userService online";
    }

    @Path("/check")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkCoords(String incoming_token) throws JsonProcessingException {
        Token json = mapper.readValue(incoming_token, Token.class);
        ObjectNode node = mapper.createObjectNode();
        Integer token = json.token;
        boolean result = userBean.checkToken(token);
        node.put("result", result);
        return Response
                .status(200)
                .entity(node)
                .build();
    }


    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(String incoming_user) throws JsonProcessingException {
        User user = mapper.readValue(incoming_user, User.class);
        Integer logStatus = userBean.login(user.user, user.password);
        ObjectNode node = mapper.createObjectNode();
        int responseStatus;
        if(logStatus==null){
            responseStatus = 401;
        }
        else if(logStatus==0){
            responseStatus = 403;
        } else {
            responseStatus = 200;
            node.put("token", logStatus);
        }
        return Response
                .status(responseStatus)
                .entity(node)
                .build();
    }

    @Path("/register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(String incoming_user) throws JsonProcessingException {
        User user = mapper.readValue(incoming_user, User.class);
        ExceptionCodes code = userBean.addUser(user.user, user.password);
        ObjectNode node = mapper.createObjectNode();
        int responseStatus;
        switch (code){
            case NULL_USERNAME:
            case NULL_PASSWORD:
                responseStatus = 406;
                break;
            case TAKEN_USERNAME:
                responseStatus = 409;
                break;
            default:
                responseStatus = 200;
                break;
        }
        return Response.status(responseStatus).entity(' ').build();
    }

    @Path("/exit")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response exit(String incoming_token) throws JsonProcessingException {
        Token elem = mapper.readValue(incoming_token, Token.class);
        userBean.exit(elem.token);
        return Response.status(200).entity("").build();
    }

}