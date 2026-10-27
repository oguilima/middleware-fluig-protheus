package com.fluig;

import java.nio.charset.StandardCharsets;

public class RestConstant {
    // Troque esta chave, atenção ao formato aaaa-bbbb-cccc-dddd.
    public static final String APP_KEY = "abcd-aaaa-bbcc-baaa";
    public static final String UTF_8_ENCODE = StandardCharsets.UTF_8.name();

    public static final String SERVICE_NAME = "restProtheus";

    public static final String SERVICE_CODE_PROTHEUS = System.getProperty("fluig.authorize.serviceCode", "restProtheus");
    public static final String COMPANY_ID = System.getProperty("fluig.authorize.companyId", "1");
    public static final String TIMEOUT_SECONDS = System.getProperty("fluig.authorize.timeout", "100");

}