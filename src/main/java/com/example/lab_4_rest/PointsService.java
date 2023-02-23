package com.example.lab_4_rest;

import com.example.lab_4_rest.models.Point;
import com.example.lab_4_rest.models.Token;
import com.example.lab_4_rest.entities.TableElement;
import com.example.lab_4_rest.models.PointBean;
import com.example.lab_4_rest.models.UserBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Collections;
import java.util.List;

@Path("/points")
public class PointsService {

    private static final ObjectMapper mapper = new ObjectMapper();
    @EJB
    PointBean pointBean;
    @EJB
    UserBean userBean;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String checkPoints_get(){
        return "pointService online";
    }

    @Path("/load")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTable(String incoming_token) throws JsonProcessingException {
        Token token = mapper.readValue(incoming_token, Token.class);
        ObjectNode node = mapper.createObjectNode();
        ArrayNode arrayNode = mapper.createArrayNode();
        int responseStatus;
        if(!userBean.checkToken(token.token)){
            responseStatus = 401;
        } else {
            responseStatus = 200;
            List<TableElement> list = pointBean.getElements();
            for(TableElement elem : list){
                ObjectNode arrNode = mapper.createObjectNode();
                arrNode.put("x", elem.getX());
                arrNode.put("y", elem.getY());
                arrNode.put("r", elem.getR());
                arrNode.put("result", elem.isResult());
                arrNode.put("username", elem.getUsername());
                arrNode.put("id", elem.getId());
                arrayNode.addAll(Collections.singletonList(arrNode));
            }
            node.put("table", arrayNode);
        }
        return Response
                .status(responseStatus)
                .entity(node)
                .build();
    }

    @Path("/add")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(String incoming_point) throws JsonProcessingException {
        Point json = mapper.readValue(incoming_point, Point.class);
        ObjectNode node = mapper.createObjectNode();
        int responseStatus = 200;
        if(!userBean.checkToken(json.token)){
            responseStatus = 401;
            return Response
                    .status(responseStatus)
                    .entity(' ')
                    .build();
        }
        int x = json.x;
        int y = json.y;
        int r = json.r;
        boolean result = pointBean.check(x, y, r);
        pointBean.addPoint(x, y, r, result, json.username);
        return Response
                .status(responseStatus)
                .entity(node)
                .build();
    }

    @Path("/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(String incoming_point) throws JsonProcessingException {
        Point json = mapper.readValue(incoming_point, Point.class);
        ObjectNode node = mapper.createObjectNode();
        int responseStatus = 200;
        if(!userBean.checkToken(json.token)){
            responseStatus = 401;
            return Response.status(responseStatus).entity(' ').build();
        }
        boolean result = false;
        pointBean.updatePoint(json.x, json.y, json.r, json.id, result, json.username);
        return Response
                .status(responseStatus)
                .entity(node)
                .build();
    }
}