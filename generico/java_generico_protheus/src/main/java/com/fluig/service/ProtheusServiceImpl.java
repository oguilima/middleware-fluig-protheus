package com.fluig.service;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fluig.client.ProtheusHttpClient;

public class ProtheusServiceImpl implements ProtheusService {

    private final ProtheusHttpClient protheusHttpClient = new ProtheusHttpClient();

    @Override
    public Response genericQuery(
            String pageSize, String page, String fromQry, String tables, String fields, String where,
            String tenantId, boolean filialFilter) {

        if (tables == null || tables.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"'tables' é obrigatório\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        if (fields == null || fields.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"'fields' é obrigatório\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        final String path = "/api/framework/v1/genericQuery";

        Map<String, String> qs = new LinkedHashMap<>();
        qs.put("tables", tables);
        qs.put("fields", fields);
        if (fromQry != null && !fromQry.isEmpty())
            qs.put("FromQry", fromQry);
        if (pageSize != null && !pageSize.isEmpty())
            qs.put("pageSize", pageSize);
        if (page != null && !page.isEmpty())
            qs.put("page", page);
        if (where != null && !where.isEmpty())
            qs.put("where", where);
        if (filialFilter == false)
            qs.put("FilialFilter", filialFilter + "");

        return protheusHttpClient.callEndpoint(
                path,
                "GET",
                qs,
                null,
                tenantId);
    }

    @Override
    public Response users(
            String externalId, String tenantId) {

        final String path = "/api/framework/v1/users";

        Map<String, String> qs = new LinkedHashMap<>();

        if (externalId != null && !externalId.isEmpty())
            qs.put("externalId", externalId);

        return protheusHttpClient.callEndpoint(
                path,
                "GET",
                qs,
                null,
                tenantId);
    }

    @Override
    public Response fwformstructview(
            String alias, String tenantId) {

        final String path = "/api/framework/v1/basicProtheusServices/fwformstructview";

        Map<String, String> qs = new LinkedHashMap<>();

        if (alias != null && !alias.isEmpty())
            qs.put("alias", alias);

        return protheusHttpClient.callEndpoint(
                path,
                "GET",
                qs,
                null,
                tenantId);
    }

}
