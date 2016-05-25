/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://github.com/google/apis-client-generator/
 * (build: 2016-05-19 20:48:09 UTC)
 * on 2016-05-25 at 07:00:47 UTC 
 * Modify at your own risk.
 */

package com.sai.nanodegree.capstone.backend.syncDetailsEndPointApi;

/**
 * Service definition for SyncDetailsEndPointApi (v1).
 *
 * <p>
 * This is an API
 * </p>
 *
 * <p>
 * For more information about this service, see the
 * <a href="" target="_blank">API Documentation</a>
 * </p>
 *
 * <p>
 * This service uses {@link SyncDetailsEndPointApiRequestInitializer} to initialize global parameters via its
 * {@link Builder}.
 * </p>
 *
 * @since 1.3
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public class SyncDetailsEndPointApi extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient {

  // Note: Leave this static initializer at the top of the file.
  static {
    com.google.api.client.util.Preconditions.checkState(
        com.google.api.client.googleapis.GoogleUtils.MAJOR_VERSION == 1 &&
        com.google.api.client.googleapis.GoogleUtils.MINOR_VERSION >= 15,
        "You are currently running with version %s of google-api-client. " +
        "You need at least version 1.15 of google-api-client to run version " +
        "1.22.0 of the syncDetailsEndPointApi library.", com.google.api.client.googleapis.GoogleUtils.VERSION);
  }

  /**
   * The default encoded root URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_ROOT_URL = "https://myApplicationId.appspot.com/_ah/api/";

  /**
   * The default encoded service path of the service. This is determined when the library is
   * generated and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_SERVICE_PATH = "syncDetailsEndPointApi/v1/syncdetailsbean/";

  /**
   * The default encoded base URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   */
  public static final String DEFAULT_BASE_URL = DEFAULT_ROOT_URL + DEFAULT_SERVICE_PATH;

  /**
   * Constructor.
   *
   * <p>
   * Use {@link Builder} if you need to specify any of the optional parameters.
   * </p>
   *
   * @param transport HTTP transport, which should normally be:
   *        <ul>
   *        <li>Google App Engine:
   *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
   *        <li>Android: {@code newCompatibleTransport} from
   *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
   *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
   *        </li>
   *        </ul>
   * @param jsonFactory JSON factory, which may be:
   *        <ul>
   *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
   *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
   *        <li>Android Honeycomb or higher:
   *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
   *        </ul>
   * @param httpRequestInitializer HTTP request initializer or {@code null} for none
   * @since 1.7
   */
  public SyncDetailsEndPointApi(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
      com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
    this(new Builder(transport, jsonFactory, httpRequestInitializer));
  }

  /**
   * @param builder builder
   */
  SyncDetailsEndPointApi(Builder builder) {
    super(builder);
  }

  @Override
  protected void initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest<?> httpClientRequest) throws java.io.IOException {
    super.initialize(httpClientRequest);
  }

  /**
   * Create a request for the method "getSyncDetails".
   *
   * This request holds the parameters needed by the syncDetailsEndPointApi server.  After setting any
   * optional parameters, call the {@link GetSyncDetails#execute()} method to invoke the remote
   * operation.
   *
   * @param email
   * @return the request
   */
  public GetSyncDetails getSyncDetails(java.lang.String email) throws java.io.IOException {
    GetSyncDetails result = new GetSyncDetails(email);
    initialize(result);
    return result;
  }

  public class GetSyncDetails extends SyncDetailsEndPointApiRequest<com.sai.nanodegree.capstone.backend.syncDetailsEndPointApi.model.SyncDetailsBean> {

    private static final String REST_PATH = "{email}";

    /**
     * Create a request for the method "getSyncDetails".
     *
     * This request holds the parameters needed by the the syncDetailsEndPointApi server.  After
     * setting any optional parameters, call the {@link GetSyncDetails#execute()} method to invoke the
     * remote operation. <p> {@link GetSyncDetails#initialize(com.google.api.client.googleapis.service
     * s.AbstractGoogleClientRequest)} must be called to initialize this instance immediately after
     * invoking the constructor. </p>
     *
     * @param email
     * @since 1.13
     */
    protected GetSyncDetails(java.lang.String email) {
      super(SyncDetailsEndPointApi.this, "GET", REST_PATH, null, com.sai.nanodegree.capstone.backend.syncDetailsEndPointApi.model.SyncDetailsBean.class);
      this.email = com.google.api.client.util.Preconditions.checkNotNull(email, "Required parameter email must be specified.");
    }

    @Override
    public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
      return super.executeUsingHead();
    }

    @Override
    public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
      return super.buildHttpRequestUsingHead();
    }

    @Override
    public GetSyncDetails setAlt(java.lang.String alt) {
      return (GetSyncDetails) super.setAlt(alt);
    }

    @Override
    public GetSyncDetails setFields(java.lang.String fields) {
      return (GetSyncDetails) super.setFields(fields);
    }

    @Override
    public GetSyncDetails setKey(java.lang.String key) {
      return (GetSyncDetails) super.setKey(key);
    }

    @Override
    public GetSyncDetails setOauthToken(java.lang.String oauthToken) {
      return (GetSyncDetails) super.setOauthToken(oauthToken);
    }

    @Override
    public GetSyncDetails setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetSyncDetails) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetSyncDetails setQuotaUser(java.lang.String quotaUser) {
      return (GetSyncDetails) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetSyncDetails setUserIp(java.lang.String userIp) {
      return (GetSyncDetails) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String email;

    /**

     */
    public java.lang.String getEmail() {
      return email;
    }

    public GetSyncDetails setEmail(java.lang.String email) {
      this.email = email;
      return this;
    }

    @Override
    public GetSyncDetails set(String parameterName, Object value) {
      return (GetSyncDetails) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "updateSyncTime".
   *
   * This request holds the parameters needed by the syncDetailsEndPointApi server.  After setting any
   * optional parameters, call the {@link UpdateSyncTime#execute()} method to invoke the remote
   * operation.
   *
   * @param email
   * @param syncFromServerTime
   * @param syncToServerTime
   * @return the request
   */
  public UpdateSyncTime updateSyncTime(java.lang.String email, java.lang.Long syncFromServerTime, java.lang.Long syncToServerTime) throws java.io.IOException {
    UpdateSyncTime result = new UpdateSyncTime(email, syncFromServerTime, syncToServerTime);
    initialize(result);
    return result;
  }

  public class UpdateSyncTime extends SyncDetailsEndPointApiRequest<com.sai.nanodegree.capstone.backend.syncDetailsEndPointApi.model.SyncDetailsBean> {

    private static final String REST_PATH = "{email}/{syncFromServerTime}/{syncToServerTime}";

    /**
     * Create a request for the method "updateSyncTime".
     *
     * This request holds the parameters needed by the the syncDetailsEndPointApi server.  After
     * setting any optional parameters, call the {@link UpdateSyncTime#execute()} method to invoke the
     * remote operation. <p> {@link UpdateSyncTime#initialize(com.google.api.client.googleapis.service
     * s.AbstractGoogleClientRequest)} must be called to initialize this instance immediately after
     * invoking the constructor. </p>
     *
     * @param email
     * @param syncFromServerTime
     * @param syncToServerTime
     * @since 1.13
     */
    protected UpdateSyncTime(java.lang.String email, java.lang.Long syncFromServerTime, java.lang.Long syncToServerTime) {
      super(SyncDetailsEndPointApi.this, "PUT", REST_PATH, null, com.sai.nanodegree.capstone.backend.syncDetailsEndPointApi.model.SyncDetailsBean.class);
      this.email = com.google.api.client.util.Preconditions.checkNotNull(email, "Required parameter email must be specified.");
      this.syncFromServerTime = com.google.api.client.util.Preconditions.checkNotNull(syncFromServerTime, "Required parameter syncFromServerTime must be specified.");
      this.syncToServerTime = com.google.api.client.util.Preconditions.checkNotNull(syncToServerTime, "Required parameter syncToServerTime must be specified.");
    }

    @Override
    public UpdateSyncTime setAlt(java.lang.String alt) {
      return (UpdateSyncTime) super.setAlt(alt);
    }

    @Override
    public UpdateSyncTime setFields(java.lang.String fields) {
      return (UpdateSyncTime) super.setFields(fields);
    }

    @Override
    public UpdateSyncTime setKey(java.lang.String key) {
      return (UpdateSyncTime) super.setKey(key);
    }

    @Override
    public UpdateSyncTime setOauthToken(java.lang.String oauthToken) {
      return (UpdateSyncTime) super.setOauthToken(oauthToken);
    }

    @Override
    public UpdateSyncTime setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (UpdateSyncTime) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public UpdateSyncTime setQuotaUser(java.lang.String quotaUser) {
      return (UpdateSyncTime) super.setQuotaUser(quotaUser);
    }

    @Override
    public UpdateSyncTime setUserIp(java.lang.String userIp) {
      return (UpdateSyncTime) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String email;

    /**

     */
    public java.lang.String getEmail() {
      return email;
    }

    public UpdateSyncTime setEmail(java.lang.String email) {
      this.email = email;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.Long syncFromServerTime;

    /**

     */
    public java.lang.Long getSyncFromServerTime() {
      return syncFromServerTime;
    }

    public UpdateSyncTime setSyncFromServerTime(java.lang.Long syncFromServerTime) {
      this.syncFromServerTime = syncFromServerTime;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.Long syncToServerTime;

    /**

     */
    public java.lang.Long getSyncToServerTime() {
      return syncToServerTime;
    }

    public UpdateSyncTime setSyncToServerTime(java.lang.Long syncToServerTime) {
      this.syncToServerTime = syncToServerTime;
      return this;
    }

    @Override
    public UpdateSyncTime set(String parameterName, Object value) {
      return (UpdateSyncTime) super.set(parameterName, value);
    }
  }

  /**
   * Builder for {@link SyncDetailsEndPointApi}.
   *
   * <p>
   * Implementation is not thread-safe.
   * </p>
   *
   * @since 1.3.0
   */
  public static final class Builder extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient.Builder {

    /**
     * Returns an instance of a new builder.
     *
     * @param transport HTTP transport, which should normally be:
     *        <ul>
     *        <li>Google App Engine:
     *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
     *        <li>Android: {@code newCompatibleTransport} from
     *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
     *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
     *        </li>
     *        </ul>
     * @param jsonFactory JSON factory, which may be:
     *        <ul>
     *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
     *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
     *        <li>Android Honeycomb or higher:
     *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
     *        </ul>
     * @param httpRequestInitializer HTTP request initializer or {@code null} for none
     * @since 1.7
     */
    public Builder(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
        com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      super(
          transport,
          jsonFactory,
          DEFAULT_ROOT_URL,
          DEFAULT_SERVICE_PATH,
          httpRequestInitializer,
          false);
    }

    /** Builds a new instance of {@link SyncDetailsEndPointApi}. */
    @Override
    public SyncDetailsEndPointApi build() {
      return new SyncDetailsEndPointApi(this);
    }

    @Override
    public Builder setRootUrl(String rootUrl) {
      return (Builder) super.setRootUrl(rootUrl);
    }

    @Override
    public Builder setServicePath(String servicePath) {
      return (Builder) super.setServicePath(servicePath);
    }

    @Override
    public Builder setHttpRequestInitializer(com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      return (Builder) super.setHttpRequestInitializer(httpRequestInitializer);
    }

    @Override
    public Builder setApplicationName(String applicationName) {
      return (Builder) super.setApplicationName(applicationName);
    }

    @Override
    public Builder setSuppressPatternChecks(boolean suppressPatternChecks) {
      return (Builder) super.setSuppressPatternChecks(suppressPatternChecks);
    }

    @Override
    public Builder setSuppressRequiredParameterChecks(boolean suppressRequiredParameterChecks) {
      return (Builder) super.setSuppressRequiredParameterChecks(suppressRequiredParameterChecks);
    }

    @Override
    public Builder setSuppressAllChecks(boolean suppressAllChecks) {
      return (Builder) super.setSuppressAllChecks(suppressAllChecks);
    }

    /**
     * Set the {@link SyncDetailsEndPointApiRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setSyncDetailsEndPointApiRequestInitializer(
        SyncDetailsEndPointApiRequestInitializer syncdetailsendpointapiRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(syncdetailsendpointapiRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        com.google.api.client.googleapis.services.GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}
