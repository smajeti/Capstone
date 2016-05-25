package com.sai.nanodegree.capstone.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by smajeti on 5/9/16.
 */

@Api(
        name = "userEndPointApi",
        version = "v1",
        scopes = {Constants.EMAIL_SCOPE},
        clientIds = {Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID, Constants.IOS_CLIENT_ID},
        audiences = {Constants.ANDROID_AUDIENCE},
        namespace = @ApiNamespace(
                ownerDomain = "backend.capstone.nanodegree.sai.com",
                ownerName = "backend.capstone.nanodegree.sai.com",
                packagePath = ""
        )
)
public class UserEndpoint {

    private static final Logger log = Logger.getLogger(UserEndpoint.class.getName());

    @ApiMethod(name = "getUser")
    public UserBean getUser(@Named("email") String emailId) {

        NamespaceManager.set(Constants.DB_NAME_SPACE);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query q = new Query(Constants.DB_KIND_USER_DETAILS);
        Query.FilterPredicate emailIdPredicate = new Query.FilterPredicate(Constants.DB_UD_EMAIL_PROP, Query.FilterOperator.EQUAL, emailId);
        q.setFilter(emailIdPredicate);
        PreparedQuery preparedQuery = datastore.prepare(q);
        Entity userEntity = preparedQuery.asSingleEntity();
        return getUserBean(userEntity);
    }

    @ApiMethod(name = "createUser")
    public UserBean createUser(@Named("displayName") String displayName,
                                @Named("email") String emailId) {
        NamespaceManager.set(Constants.DB_NAME_SPACE);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Entity userEntity = new Entity(Constants.DB_KIND_USER_DETAILS);
        userEntity.setIndexedProperty(Constants.DB_UD_EMAIL_PROP, emailId);
        userEntity.setUnindexedProperty(Constants.DB_UD_DISPLAY_NAME_PROP, displayName);
        userEntity.setUnindexedProperty(Constants.DB_UD_SUBSCRIPTION_PAID_PROP, false);
        userEntity.setUnindexedProperty(Constants.DB_UD_SUBSCRIPTION_PAID_DATE_PROP, null);
        Date now = new Date();
        userEntity.setUnindexedProperty(Constants.DB_CREATION_DATE_PROP, now);
        userEntity.setIndexedProperty(Constants.DB_LAST_UPDATED_DATE_PROP, now);

        Key userKey = datastore.put(userEntity);
        log.info("Created user " + emailId + " with ID " + userKey.getId());
        return getUserBean(userEntity);
    }

    private UserBean getUserBean(Entity userEntity) {
        UserBean userBean = new UserBean();
        if (userEntity != null) {
            userBean.setId(userEntity.getKey().getId());
            userBean.setEmail((String) userEntity.getProperty(Constants.DB_UD_EMAIL_PROP));
            userBean.setDisplayName((String) userEntity.getProperty(Constants.DB_UD_DISPLAY_NAME_PROP));
            userBean.setSubscriptionPaid((Boolean) userEntity.getProperty(Constants.DB_UD_SUBSCRIPTION_PAID_PROP));
            userBean.setSubscriptionPaidDate((Date) userEntity.getProperty(Constants.DB_UD_SUBSCRIPTION_PAID_DATE_PROP));
            userBean.setCreationDate((Date) userEntity.getProperty(Constants.DB_CREATION_DATE_PROP));
            userBean.setUpdatedDate((Date) userEntity.getProperty(Constants.DB_LAST_UPDATED_DATE_PROP));
        }

        return userBean;
    }

}
