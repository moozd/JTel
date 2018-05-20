package com.jtel.common.db;

/**
 * This file is part of jTelClient
 * IntelliJ idea.
 * Date     : 7/23/2016
 * Package : com.jtel.common.db
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class ConfApi {
    int apiId;
    String apiHash;
    int apiSchemaLayer;
    String apiTlSchemaProvider;

    public ConfApi(int apiId, String apiHash, int apiSchemaLayer, String apiTlSchemaProvider) {
        this.apiId = apiId;
        this.apiHash = apiHash;
        this.apiSchemaLayer = apiSchemaLayer;
        this.apiTlSchemaProvider = apiTlSchemaProvider;
    }

    public int getApiId() {
        return apiId;
    }

    public void setApiId(int apiId) {
        this.apiId = apiId;
    }

    public String getApiHash() {
        return apiHash;
    }

    public void setApiHash(String apiHash) {
        this.apiHash = apiHash;
    }

    public int getApiSchemaLayer() {
        return apiSchemaLayer;
    }

    public void setApiSchemaLayer(int apiSchemaLayer) {
        this.apiSchemaLayer = apiSchemaLayer;
    }

    public String getApiTlSchemaProvider() {
        return apiTlSchemaProvider;
    }

    public void setApiTlSchemaProvider(String apiTlSchemaProvider) {
        this.apiTlSchemaProvider = apiTlSchemaProvider;
    }
}
