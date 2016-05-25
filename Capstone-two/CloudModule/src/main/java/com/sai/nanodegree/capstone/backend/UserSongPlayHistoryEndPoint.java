package com.sai.nanodegree.capstone.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by smajeti on 5/13/16.
 */
@Api(
        name = "userSongPlayHistoryEndPointApi",
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
public class UserSongPlayHistoryEndPoint {
    private static final Logger log = Logger.getLogger(SongDetailsEndpoint.class.getName());
    private static final String QT_GET = "get";
    private static final String QT_UPDATE = "update";

    @ApiMethod(name = "getUpdatedHistory")
    public CollectionResponse<UserSongPlayHistoryBean> getUpdatedHistory(@Named("after") Long afterThisDate,
                                                                     @Named(Constants.DB_UD_EMAIL_PROP) String email) {

        NamespaceManager.set(Constants.DB_NAME_SPACE);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        UserEndpoint userEndPoint = new UserEndpoint();
        UserBean userBean = userEndPoint.getUser(email);

        Query.FilterPredicate datePredicate = new Query.FilterPredicate(Constants.DB_LAST_UPDATED_DATE_PROP,
                                                    Query.FilterOperator.GREATER_THAN, new Date(afterThisDate));
        Query.FilterPredicate userPredicate = new Query.FilterPredicate(Constants.DB_USPH_USER_ID_PROP,
                                                    Query.FilterOperator.EQUAL, userBean.getId());
        Query.CompositeFilter compositeFilter = Query.CompositeFilterOperator.and(userPredicate, datePredicate);

        Query q = new Query(Constants.DB_KIND_USER_SONG_PLAY_HISTORY);
        q.setFilter(compositeFilter);
        //q.setFilter(userPredicate);
        PreparedQuery preparedQuery = datastore.prepare(q);

        ArrayList<UserSongPlayHistoryBean> arrayList = new ArrayList<UserSongPlayHistoryBean>(20);
        for (Entity entity : preparedQuery.asIterable()) {
            arrayList.add(getUserSongPlayHistoryBean(entity, datastore));
        }

        return CollectionResponse.<UserSongPlayHistoryBean>builder().setItems(arrayList).build();
    }

    @ApiMethod(name = "songPlayHistory")
    public UserSongPlayHistoryBean songPlayHistory(@Named("queryType") String queryType,
                                                   @Named(Constants.DB_UD_EMAIL_PROP) String email,
                                                   @Named(Constants.DB_SD_TITLE_PROP) String title,
                                                   @Named(Constants.DB_SD_CATEGORY_PROP) String category,
                                                   @Named(Constants.DB_SD_RAGAM_PROP) String ragam,
                                                   @Named(Constants.DB_USPH_PLAYED_TIME_PROP) Double playedTime) {
        NamespaceManager.set(Constants.DB_NAME_SPACE);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        UserEndpoint userEndpoint = new UserEndpoint();
        UserBean userBean = userEndpoint.getUser(email);
        SongDetailsEndpoint songDetailsEndpoint = new SongDetailsEndpoint();
        SongDetailsBean songDetailsBean = songDetailsEndpoint.getSongDetails(title, category, ragam);
        if ((userBean == null) || (songDetailsBean == null)) {
            return new UserSongPlayHistoryBean();
        }

        Query q = new Query(Constants.DB_KIND_USER_SONG_PLAY_HISTORY);
        Query.FilterPredicate userPredicate = new Query.FilterPredicate(Constants.DB_USPH_USER_ID_PROP, Query.FilterOperator.EQUAL, userBean.getId());
        Query.FilterPredicate songPredicate = new Query.FilterPredicate(Constants.DB_USPH_SONG_ID_PROP, Query.FilterOperator.EQUAL, songDetailsBean.getId());
        Query.CompositeFilter compositeFilter = Query.CompositeFilterOperator.and(userPredicate, songPredicate);
        q.setFilter(compositeFilter);
        PreparedQuery preparedQuery = datastore.prepare(q);

        Entity entity = preparedQuery.asSingleEntity();
        if (queryType.equals(QT_GET)) {
            return getUserSongPlayHistoryBean(entity, songDetailsBean, userBean);
        } else if (entity == null) {
            entity = new Entity(Constants.DB_KIND_USER_SONG_PLAY_HISTORY);
        }

        entity.setIndexedProperty(Constants.DB_USPH_SONG_ID_PROP, songDetailsBean.getId());
        entity.setIndexedProperty(Constants.DB_USPH_USER_ID_PROP, userBean.getId());
        entity.setUnindexedProperty(Constants.DB_USPH_PLAYED_TIME_PROP, playedTime);
        entity.setIndexedProperty(Constants.DB_LAST_UPDATED_DATE_PROP, new Date());
        datastore.put(entity);

        return getUserSongPlayHistoryBean(entity, songDetailsBean, userBean);
    }

    private UserSongPlayHistoryBean getUserSongPlayHistoryBean(Entity usphEntity, SongDetailsBean songDetailsBean, UserBean userBean) {
        UserSongPlayHistoryBean userSongPlayHistoryBean = new UserSongPlayHistoryBean();
        if (usphEntity != null) {
            userSongPlayHistoryBean.setId(usphEntity.getKey().getId());
            userSongPlayHistoryBean.setEmail(userBean.getEmail());
            userSongPlayHistoryBean.setTitle(songDetailsBean.getTitle());
            userSongPlayHistoryBean.setRagam(songDetailsBean.getRagam());
            userSongPlayHistoryBean.setCategoryName(songDetailsBean.getCategoryName());
            userSongPlayHistoryBean.setPalyedTime((Double) usphEntity.getProperty(Constants.DB_USPH_PLAYED_TIME_PROP));
            userSongPlayHistoryBean.setCreationDate((Date) usphEntity.getProperty(Constants.DB_CREATION_DATE_PROP));
            userSongPlayHistoryBean.setUpdatedDate((Date) usphEntity.getProperty(Constants.DB_LAST_UPDATED_DATE_PROP));
        }

        return userSongPlayHistoryBean;
    }

    private UserSongPlayHistoryBean getUserSongPlayHistoryBean(Entity usphEntity, DatastoreService dataStore) {
        UserSongPlayHistoryBean userSongPlayHistoryBean = new UserSongPlayHistoryBean();

        long userId = (long) usphEntity.getProperty(Constants.DB_USPH_USER_ID_PROP);
        long songId = (long) usphEntity.getProperty(Constants.DB_USPH_SONG_ID_PROP);
        Key userKey = KeyFactory.createKey(Constants.DB_KIND_USER_DETAILS, userId);
        Key songKey = KeyFactory.createKey(Constants.DB_KIND_SONG_DETAILS, songId);
        try {
            Entity userEntity = dataStore.get(userKey);
            Entity songEntity = dataStore.get(songKey);
            long categoryId = (long) songEntity.getProperty(Constants.DB_SD_CATEGORY_PROP);
            Key categoryKey = KeyFactory.createKey(Constants.DB_KIND_SONG_CATEGORIES, categoryId);
            Entity categoryEntity = dataStore.get(categoryKey);

            userSongPlayHistoryBean.setId(usphEntity.getKey().getId());
            userSongPlayHistoryBean.setEmail((String) userEntity.getProperty(Constants.DB_UD_DISPLAY_NAME_PROP));
            userSongPlayHistoryBean.setRagam((String) songEntity.getProperty(Constants.DB_SD_RAGAM_PROP));
            userSongPlayHistoryBean.setTitle((String) songEntity.getProperty(Constants.DB_SD_TITLE_PROP));
            userSongPlayHistoryBean.setCategoryName((String) categoryEntity.getProperty(Constants.DB_SC_NAME_PROP));
            userSongPlayHistoryBean.setPalyedTime((Double) usphEntity.getProperty(Constants.DB_USPH_PLAYED_TIME_PROP));
            userSongPlayHistoryBean.setCreationDate((Date) usphEntity.getProperty(Constants.DB_CREATION_DATE_PROP));
            userSongPlayHistoryBean.setUpdatedDate((Date) usphEntity.getProperty(Constants.DB_LAST_UPDATED_DATE_PROP));

        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }

        return userSongPlayHistoryBean;
    }
}
