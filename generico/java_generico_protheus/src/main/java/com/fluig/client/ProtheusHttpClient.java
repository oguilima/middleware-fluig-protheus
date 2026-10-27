package com.fluig.client;

import javax.ws.rs.core.*;

import com.fluig.RestConstant;
import com.fluig.sdk.api.FluigAPI;
import com.fluig.sdk.service.AuthorizeClientSdkService;
import com.fluig.sdk.api.authorizeclient.AuthorizeClientSdkServiceVO;

import javax.json.*;
import java.io.*;
import java.net.URLEncoder;
import java.util.Map;

public class ProtheusHttpClient {



    private String enc(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8")
                    .replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 não suportado", e);
        }
    }

    public Response callEndpoint(
            String path,
            String httpMethod,
            Map<String, String> queryParams,
            String bodyJson,
            String tenantId) {
        try {
            FluigAPI fluigAPI = new FluigAPI();
            AuthorizeClientSdkService clientService = fluigAPI.getAuthorizeClientService();

            JsonObjectBuilder dataBuilder = Json.createObjectBuilder();
            dataBuilder.add("companyId", tenantId != null && !tenantId.isEmpty() ? tenantId : RestConstant.COMPANY_ID);
            dataBuilder.add("serviceCode", RestConstant.SERVICE_CODE_PROTHEUS);

            StringBuilder endpoint = new StringBuilder(path);
            if (queryParams != null && !queryParams.isEmpty()) {
                String qs = queryParams.entrySet().stream()
                        .map(e -> e.getKey() + "=" + enc(e.getValue()))
                        .collect(java.util.stream.Collectors.joining("&"));
                endpoint.append(endpoint.indexOf("?") >= 0 ? "&" : "?").append(qs);
            }
            dataBuilder.add("endpoint", endpoint.toString());
            dataBuilder.add("method", httpMethod.toLowerCase());
            dataBuilder.add("timeoutService", RestConstant.TIMEOUT_SECONDS);

            JsonObjectBuilder headersBuilder = Json.createObjectBuilder();
            headersBuilder.add("Content-Type", "application/json; charset=UTF-8");
            headersBuilder.add("tenantid", tenantId != null && !tenantId.isEmpty() ? tenantId : "01,5303");
            dataBuilder.add("headers", headersBuilder);

            if (bodyJson != null && !bodyJson.isEmpty()
                    && !httpMethod.equalsIgnoreCase("GET")
                    && !httpMethod.equalsIgnoreCase("DELETE")) {
                try (JsonReader reader = Json.createReader(new StringReader(bodyJson))) {
                    JsonStructure js = reader.read();
                    dataBuilder.add("params", js);
                } catch (Exception ex) {
                    // ignorar
                }
            } else {
                dataBuilder.add("params", Json.createObjectBuilder().build());
            }

            String jsonPayload = dataBuilder.build().toString();

            AuthorizeClientSdkServiceVO vo = clientService.invoke(jsonPayload);

            if (vo == null || vo.getResult() == null || vo.getResult().isEmpty()) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"error\":\"Retorno vazio do serviço " + RestConstant.SERVICE_CODE_PROTHEUS + "\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            String responseBody = vo.getResult();

            boolean looksJson = responseBody.trim().startsWith("{") || responseBody.trim().startsWith("[");
            if (looksJson) {
                try (JsonReader reader = Json.createReader(new StringReader(responseBody))) {
                    JsonStructure js = reader.read();
                    return Response.status(200).type(MediaType.APPLICATION_JSON).entity(js).build();
                } catch (Exception ex) {
                    return Response.status(200).type(MediaType.TEXT_PLAIN)
                            .entity("Falha ao parsear JSON: " + ex.getMessage() + "\n\n" + responseBody).build();
                }
            } else {
                return Response.status(200)
                        .type(MediaType.TEXT_HTML)
                        .entity(responseBody)
                        .build();
            }

        } catch (Exception e) {
            JsonObject err = Json.createObjectBuilder()
                    .add("error", e.getClass().getSimpleName())
                    .add("message", e.getMessage() == null ? "" : e.getMessage())
                    .build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_JSON).entity(err).build();
        }
    }
}
