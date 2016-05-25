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
 * on 2016-05-25 at 07:00:45 UTC 
 * Modify at your own risk.
 */

package com.sai.nanodegree.capstone.backend.userEndPointApi.model;

/**
 * Model definition for UserBean.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the userEndPointApi. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class UserBean extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private com.google.api.client.util.DateTime creationDate;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String displayName;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String email;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long id;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Boolean subscriptionPaid;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private com.google.api.client.util.DateTime subscriptionPaidDate;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private com.google.api.client.util.DateTime updatedDate;

  /**
   * @return value or {@code null} for none
   */
  public com.google.api.client.util.DateTime getCreationDate() {
    return creationDate;
  }

  /**
   * @param creationDate creationDate or {@code null} for none
   */
  public UserBean setCreationDate(com.google.api.client.util.DateTime creationDate) {
    this.creationDate = creationDate;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getDisplayName() {
    return displayName;
  }

  /**
   * @param displayName displayName or {@code null} for none
   */
  public UserBean setDisplayName(java.lang.String displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getEmail() {
    return email;
  }

  /**
   * @param email email or {@code null} for none
   */
  public UserBean setEmail(java.lang.String email) {
    this.email = email;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getId() {
    return id;
  }

  /**
   * @param id id or {@code null} for none
   */
  public UserBean setId(java.lang.Long id) {
    this.id = id;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Boolean getSubscriptionPaid() {
    return subscriptionPaid;
  }

  /**
   * @param subscriptionPaid subscriptionPaid or {@code null} for none
   */
  public UserBean setSubscriptionPaid(java.lang.Boolean subscriptionPaid) {
    this.subscriptionPaid = subscriptionPaid;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public com.google.api.client.util.DateTime getSubscriptionPaidDate() {
    return subscriptionPaidDate;
  }

  /**
   * @param subscriptionPaidDate subscriptionPaidDate or {@code null} for none
   */
  public UserBean setSubscriptionPaidDate(com.google.api.client.util.DateTime subscriptionPaidDate) {
    this.subscriptionPaidDate = subscriptionPaidDate;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public com.google.api.client.util.DateTime getUpdatedDate() {
    return updatedDate;
  }

  /**
   * @param updatedDate updatedDate or {@code null} for none
   */
  public UserBean setUpdatedDate(com.google.api.client.util.DateTime updatedDate) {
    this.updatedDate = updatedDate;
    return this;
  }

  @Override
  public UserBean set(String fieldName, Object value) {
    return (UserBean) super.set(fieldName, value);
  }

  @Override
  public UserBean clone() {
    return (UserBean) super.clone();
  }

}