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
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by smajeti on 5/12/16.
 */

@Api(
        name = "songCategoryEndPointApi",
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
public class SongCategoryEndpoint {
    private static final Logger log = Logger.getLogger(UserEndpoint.class.getName());

    @ApiMethod(name = "getCategories")
    public CollectionResponse<SongCategoryBean> getCategories(@Named("after") Long afterThisDate) {

        NamespaceManager.set(Constants.DB_NAME_SPACE);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query q = new Query(Constants.DB_KIND_SONG_CATEGORIES);
        // We can't use sort on other parameter when we are trying to use selection condition
        //q.addSort(Constants.DB_SC_LEVEL_PROP, Query.SortDirection.ASCENDING);
        Query.FilterPredicate datePredicate = new Query.FilterPredicate(Constants.DB_LAST_UPDATED_DATE_PROP,
                                                            Query.FilterOperator.GREATER_THAN, new Date(afterThisDate));
        q.setFilter(datePredicate);
        PreparedQuery preparedQuery = datastore.prepare(q);

        ArrayList<SongCategoryBean> arrayList = new ArrayList<SongCategoryBean>(20);
        for (Entity entity : preparedQuery.asIterable()) {
            arrayList.add(getSongCategoryBean(entity));
        }

        // send them in 'level' sorted order
        Collections.sort(arrayList, new Comparator<SongCategoryBean>() {
            @Override
            public int compare(SongCategoryBean o1, SongCategoryBean o2) {
                return o1.getLevel().compareTo(o2.getLevel());
            }
        });

        return CollectionResponse.<SongCategoryBean>builder().setItems(arrayList).build();
    }

    @ApiMethod(name = "createCategory")
    public SongCategoryBean createCategory(@Named("name") String name, @Named("level") long level) {

        NamespaceManager.set(Constants.DB_NAME_SPACE);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Entity scEntity = new Entity(Constants.DB_KIND_SONG_CATEGORIES);
        scEntity.setIndexedProperty(Constants.DB_SC_NAME_PROP, name);
        scEntity.setIndexedProperty(Constants.DB_SC_LEVEL_PROP, level);
        Date now = new Date();
        scEntity.setUnindexedProperty(Constants.DB_CREATION_DATE_PROP, now);
        scEntity.setIndexedProperty(Constants.DB_LAST_UPDATED_DATE_PROP, now);

        Key scKey = datastore.put(scEntity);
        log.info("Created Song Category " + name + " with ID " + scKey.getId());
        return getSongCategoryBean(scEntity);
    }

    private SongCategoryBean getSongCategoryBean(Entity entity) {
        SongCategoryBean songCategoryBean = new SongCategoryBean();
        if (entity != null) {
            songCategoryBean.setId(entity.getKey().getId());
            songCategoryBean.setName((String) entity.getProperty(Constants.DB_SC_NAME_PROP));
            songCategoryBean.setLevel((Long) entity.getProperty(Constants.DB_SC_LEVEL_PROP));
            songCategoryBean.setCreationDate((Date) entity.getProperty(Constants.DB_CREATION_DATE_PROP));
            songCategoryBean.setUpdatedDate((Date) entity.getProperty(Constants.DB_LAST_UPDATED_DATE_PROP));
        }

        return songCategoryBean;
    }

    private void createTestCategories(DatastoreService datastore) {
        String categories[] = {"Sarali Varse", "Dhatu Varse", "Alankarams", "Geethams", "Swarajathis", "Varnams", "Krithis", "Manodharma", "Thillanas"};
        for (int i = 0; i < categories.length; ++i) {
            Entity scEntity = new Entity(Constants.DB_KIND_SONG_CATEGORIES);
            scEntity.setIndexedProperty(Constants.DB_SC_NAME_PROP, categories[i]);
            scEntity.setIndexedProperty(Constants.DB_SC_LEVEL_PROP, (i + 1) * 10);
            Date now = new Date();
            scEntity.setUnindexedProperty(Constants.DB_CREATION_DATE_PROP, now);
            scEntity.setIndexedProperty(Constants.DB_LAST_UPDATED_DATE_PROP, now);

            Key scKey = datastore.put(scEntity);
        }
    }

}
