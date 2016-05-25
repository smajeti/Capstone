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
 * Created by smajeti on 5/13/16.
 */
@Api(
        name = "syncDetailsEndPointApi",
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
public class SyncDetailsEndpoint {
    private static final Logger log = Logger.getLogger(UserEndpoint.class.getName());

    @ApiMethod(name = "getSyncDetails")
    public SyncDetailsBean getSyncDetails(@Named(Constants.DB_UD_EMAIL_PROP) String emailId) {

        NamespaceManager.set(Constants.DB_NAME_SPACE);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        UserEndpoint userEndpoint = new UserEndpoint();
        UserBean userBean = userEndpoint.getUser(emailId);
        if (userBean == null) {
            return new SyncDetailsBean();
        }
        
        Entity syncDetailsEntity = queryForSyncDetails(datastore, userBean);
        return getSyncDetailsBean(syncDetailsEntity, userBean);
    }

    @ApiMethod(name = "updateSyncTime")
    public SyncDetailsBean updateSyncTime(@Named(Constants.DB_UD_EMAIL_PROP) String email,
                                                  @Named(Constants.DB_SYD_SYNC_FROM_SERVER_TIME_PROP) Long fromTime,
                                                  @Named(Constants.DB_SYD_SYNC_TO_SERVER_TIME_PROP) Long toTime) {
        NamespaceManager.set(Constants.DB_NAME_SPACE);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        UserEndpoint userEndpoint = new UserEndpoint();
        UserBean userBean = userEndpoint.getUser(email);
        if (userBean == null) {
            return new SyncDetailsBean();
        }

        Entity syncDetailsEntity = queryForSyncDetails(datastore, userBean);
        Date zeroDate = new Date(0);
        Date syncFromServerTime = (fromTime == -1) ? zeroDate : new Date(fromTime);
        Date syncToServerTime = (toTime == -1) ? zeroDate : new Date(toTime);

        if (syncDetailsEntity == null) {
            syncDetailsEntity = new Entity(Constants.DB_KIND_SYNC_DETAILS);
            syncDetailsEntity.setIndexedProperty(Constants.DB_SYD_USER_ID_PROP, userBean.getId());
            syncDetailsEntity.setIndexedProperty(Constants.DB_SYD_SYNC_FROM_SERVER_TIME_PROP, (fromTime == -1) ? zeroDate : syncFromServerTime);
            syncDetailsEntity.setIndexedProperty(Constants.DB_SYD_SYNC_TO_SERVER_TIME_PROP, (toTime == -1) ? zeroDate : syncToServerTime);
        } else {
            if (fromTime != -1L) {
                syncDetailsEntity.setIndexedProperty(Constants.DB_SYD_SYNC_FROM_SERVER_TIME_PROP, syncFromServerTime);
            }
            if (toTime != -1L) {
                syncDetailsEntity.setIndexedProperty(Constants.DB_SYD_SYNC_TO_SERVER_TIME_PROP, syncToServerTime);
            }
        }

        Key userKey = datastore.put(syncDetailsEntity);

        return getSyncDetailsBean(syncDetailsEntity, userBean);
    }

    private Entity queryForSyncDetails(DatastoreService datastore, UserBean userBean) {
        Query q = new Query(Constants.DB_KIND_SYNC_DETAILS);
        Query.FilterPredicate userIdPredicate = new Query.FilterPredicate(Constants.DB_SYD_USER_ID_PROP, Query.FilterOperator.EQUAL, userBean.getId());
        q.setFilter(userIdPredicate);
        PreparedQuery preparedQuery = datastore.prepare(q);
        return preparedQuery.asSingleEntity();
    }

    private SyncDetailsBean getSyncDetailsBean(Entity syncDetailsEntity, final UserBean userBean) {
        SyncDetailsBean syncDetailsBean = new SyncDetailsBean();
        if (syncDetailsEntity != null) {
            syncDetailsBean.setId(syncDetailsEntity.getKey().getId());
            syncDetailsBean.setUserEmail(userBean.getEmail());
            syncDetailsBean.setSyncFromServerTime((Date) syncDetailsEntity.getProperty(Constants.DB_SYD_SYNC_FROM_SERVER_TIME_PROP));
            syncDetailsBean.setSyncToServerTime((Date) syncDetailsEntity.getProperty(Constants.DB_SYD_SYNC_TO_SERVER_TIME_PROP));
        }
        
        return syncDetailsBean;
    }
}
