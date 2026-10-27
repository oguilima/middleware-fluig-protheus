package com.fluig.controller;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.fluig.service.ProtheusService;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProtheusController {

    private final ProtheusService protheusService = new com.fluig.service.ProtheusServiceImpl();

    @GET
    @Path("/api/framework/v1/genericQuery")
    public Response genericQuery(
            @QueryParam("pageSize") String pageSize,
            @QueryParam("page") String page,
            @QueryParam("FromQry") String fromQry,
            @QueryParam("tables") String tables,
            @QueryParam("fields") String fields,
            @QueryParam("where") String where,
            @QueryParam("FilialFilter") @DefaultValue("true") boolean filialFilter,
            @HeaderParam("tenantid") String tenantId) {

        return protheusService.genericQuery(pageSize, page, fromQry, tables, fields, where, tenantId, filialFilter);
    }

    @GET
    @Path("/api/framework/v1/users")
    public Response users(
            @QueryParam("externalId") String externalId,
            @HeaderParam("tenantid") String tenantId) {

        return protheusService.users(externalId, tenantId);
    }

    @GET
    @Path("/api/framework/v1/basicProtheusServices/fwformstructview")
    public Response fwformstructview(
            @QueryParam("alias") String alias,
            @HeaderParam("tenantid") String tenantId) {

        return protheusService.fwformstructview(alias, tenantId);
    }

}