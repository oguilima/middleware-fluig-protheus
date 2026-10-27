package com.fluig.service;

import javax.ws.rs.core.Response;

public interface ProtheusService {
        Response genericQuery(
                        String pageSize, String page, String fromQry, String tables, String fields, String where,
                        String tenantId, boolean filialFilter);

       Response users(String externalId, String tenantId);

       Response fwformstructview(String alias, String tenantId);
}
