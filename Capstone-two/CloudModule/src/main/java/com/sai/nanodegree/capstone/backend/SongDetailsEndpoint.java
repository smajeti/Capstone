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
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by smajeti on 5/13/16.
 */
@Api(
        name = "songDetailsEndPointApi",
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
public class SongDetailsEndpoint {
    private static final Logger log = Logger.getLogger(SongDetailsEndpoint.class.getName());

    @ApiMethod(name = "getAllSongsDetails")
    public CollectionResponse<SongDetailsBean> getAllSongsDetails(@Named("after") Long afterThisDate) {
        NamespaceManager.set(Constants.DB_NAME_SPACE);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        //createTestData();

        Query q = new Query(Constants.DB_KIND_SONG_DETAILS);
        Query.FilterPredicate datePredicate = new Query.FilterPredicate(Constants.DB_LAST_UPDATED_DATE_PROP,
                Query.FilterOperator.GREATER_THAN, new Date(afterThisDate));
        q.setFilter(datePredicate);
        PreparedQuery preparedQuery = datastore.prepare(q);

        HashMap<Long, SongCategoryBean> hashMap = getCategories();
        ArrayList<SongDetailsBean> arrayList = new ArrayList<SongDetailsBean>(20);
        for (Entity entity : preparedQuery.asIterable()) {
            arrayList.add(getSongDetailsBean(entity, hashMap));
        }

        return CollectionResponse.<SongDetailsBean>builder().setItems(arrayList).build();
    }

    @ApiMethod(name = "getSongDetails")
    public SongDetailsBean getSongDetails(@Named(Constants.DB_SD_TITLE_PROP) String title,
                          @Named(Constants.DB_SD_CATEGORY_PROP) String category,
                          @Named(Constants.DB_SD_RAGAM_PROP) String ragam) {
        NamespaceManager.set(Constants.DB_NAME_SPACE);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query q = new Query(Constants.DB_KIND_SONG_DETAILS);
        Query.FilterPredicate titlePredicate = new Query.FilterPredicate(Constants.DB_SD_TITLE_PROP, Query.FilterOperator.EQUAL, title);
        HashMap<Long, SongCategoryBean> hashMap = getCategories();
        Long categoryId = getCategoryId(category, hashMap);
        if (categoryId == null) {
            return new SongDetailsBean();
        }
        Query.FilterPredicate categoryPredicate = new Query.FilterPredicate(Constants.DB_SD_CATEGORY_PROP, Query.FilterOperator.EQUAL, categoryId);
        Query.FilterPredicate ragamPredicate = new Query.FilterPredicate(Constants.DB_SD_RAGAM_PROP, Query.FilterOperator.EQUAL, ragam);
        Query.CompositeFilter compositeFilter = Query.CompositeFilterOperator.and(titlePredicate,categoryPredicate,ragamPredicate);
        q.setFilter(compositeFilter);
        PreparedQuery preparedQuery = datastore.prepare(q);
        Entity entity = preparedQuery.asSingleEntity();

        return getSongDetailsBean(entity, hashMap);
    }

    @ApiMethod(name = "createSongDetails")
    public SongDetailsBean createSongDetails(@Named(Constants.DB_SD_AAROHANA_PROP) String aarohana,
                                             @Named(Constants.DB_SD_AVAROHANA_PROP) String avarohana,
                                             @Named(Constants.DB_SD_CATEGORY_PROP) String categoryName,
                                             @Named(Constants.DB_SD_DETAILS_PROP) String details,
                                             @Named(Constants.DB_SD_DURATION_PROP) Double duration,
                                             @Named(Constants.DB_SD_PICTURE_URL_PROP) String pictureUrl,
                                             @Named(Constants.DB_SD_VIDEO_URL_PROP) String videoUrl,
                                             @Named(Constants.DB_SD_RAGAM_PROP) String ragam,
                                             @Named(Constants.DB_SD_TITLE_PROP) String title) {
        NamespaceManager.set(Constants.DB_NAME_SPACE);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        HashMap<Long, SongCategoryBean> hashMap = getCategories();

        Entity sdEntity = new Entity(Constants.DB_KIND_SONG_DETAILS);

        sdEntity.setUnindexedProperty(Constants.DB_SD_AAROHANA_PROP, aarohana);
        sdEntity.setUnindexedProperty(Constants.DB_SD_AVAROHANA_PROP, avarohana);

        Long categoryId = getCategoryId(categoryName, hashMap);

        sdEntity.setIndexedProperty(Constants.DB_SD_CATEGORY_PROP, categoryId);
        sdEntity.setIndexedProperty(Constants.DB_SD_DETAILS_PROP, details);
        sdEntity.setUnindexedProperty(Constants.DB_SD_DURATION_PROP, duration);
        sdEntity.setUnindexedProperty(Constants.DB_SD_PICTURE_URL_PROP, pictureUrl);
        sdEntity.setUnindexedProperty(Constants.DB_SD_VIDEO_URL_PROP, videoUrl);
        sdEntity.setIndexedProperty(Constants.DB_SD_RAGAM_PROP, ragam);
        sdEntity.setIndexedProperty(Constants.DB_SD_TITLE_PROP, title);

        Date now = new Date();
        sdEntity.setUnindexedProperty(Constants.DB_CREATION_DATE_PROP, now);
        sdEntity.setIndexedProperty(Constants.DB_LAST_UPDATED_DATE_PROP, now);

        Key scKey = datastore.put(sdEntity);
        log.info("Created Song Details " + title + " with ID " + scKey.getId());
        return getSongDetailsBean(sdEntity, hashMap);
    }


    private SongDetailsBean getSongDetailsBean(Entity entity, HashMap<Long, SongCategoryBean> hashMap) {
        SongDetailsBean songDetailsBean = new SongDetailsBean();
        if (entity != null) {
            songDetailsBean.setId(entity.getKey().getId());

            songDetailsBean.setAarohana((String) entity.getProperty(Constants.DB_SD_AAROHANA_PROP));
            songDetailsBean.setAvarohana((String) entity.getProperty(Constants.DB_SD_AVAROHANA_PROP));
            Long categoryId = (Long) entity.getProperty(Constants.DB_SD_CATEGORY_PROP);
            songDetailsBean.setCategoryName(hashMap.get(categoryId).getName());
            songDetailsBean.setDetails((String) entity.getProperty(Constants.DB_SD_DETAILS_PROP));
            songDetailsBean.setDuration((Double) entity.getProperty(Constants.DB_SD_DURATION_PROP));
            songDetailsBean.setPictureUrl((String) entity.getProperty(Constants.DB_SD_PICTURE_URL_PROP));
            songDetailsBean.setVideoUrl((String) entity.getProperty(Constants.DB_SD_VIDEO_URL_PROP));
            songDetailsBean.setRagam((String) entity.getProperty(Constants.DB_SD_RAGAM_PROP));
            songDetailsBean.setTitle((String) entity.getProperty(Constants.DB_SD_TITLE_PROP));
            songDetailsBean.setCreationDate((Date) entity.getProperty(Constants.DB_CREATION_DATE_PROP));
            songDetailsBean.setUpdatedDate((Date) entity.getProperty(Constants.DB_LAST_UPDATED_DATE_PROP));
        }

        return songDetailsBean;
    }

    private HashMap<Long, SongCategoryBean> getCategories() {
        SongCategoryEndpoint songCategoryEndpoint = new SongCategoryEndpoint();
        CollectionResponse<SongCategoryBean> categoriesList = songCategoryEndpoint.getCategories(0L);
        HashMap<Long, SongCategoryBean> hashMap = new HashMap<Long, SongCategoryBean>(categoriesList.getItems().size());
        for (SongCategoryBean scBean : categoriesList.getItems()) {
            hashMap.put(scBean.getId(), scBean);
        }

        return hashMap;
    }

    private void createTestData() {
        String aarohana = "S R2 G3 P D2 S";
        String avarohana = "S N3 D2 P M1 G3 R2 S";
        String categoryName;
        String details = "Pallavi: Raaraa Venugopa Baala Raajita Sadguna Jaya Sheela\\nAnupallavi: Saarasaaksha Nera Memi Maarubaari Korvaleraa \\nCharanam 1: Nandagopaalaa Ne Nendu Pojaalaa Nee Vindu Raaraa Sadamalamadito Mudamala Raganaa Keduruga Gadiyara (Raaravenu) \\nCharanam 2: Palumaa Runugaa Ravamuna Nin Pilachina Palukavu Nalugakuraa Karivarada Marimarina Adharamugro Larakani Karamuga (Raaravenu) \\nCharanam 3: Raa Nagadara Raa Murahara Raa Bhavahara Raaveraa Ee Maguvanu Ee Lalalanu Ee Sogasini Chekoraa Korikalim Pondaa Dendamu Neeyanda Cherenu Neechenta Maruvakuraa Karamulache Marimari Ninu Sharanane dara (Raaravenu)";
        Double[] duration = {9.37, 10.37, 10.3, 7.83, 13.03, 9.37, 8.23, 12.8, 14.65, 11.58}; // mins
        String pictureUrl = "http://capstone-nanodegree-20160409.appspot.com.storage.googleapis.com/pictures/image_"; // 5 images
        String videoUrl = "http://capstone-nanodegree-20160409.appspot.com.storage.googleapis.com/videos/video_"; // 10 videos
        String ragam = " Ragam_";
        String title = " Lesson ";

        HashMap<Long, SongCategoryBean> categoriesMap = getCategories();
        for (SongCategoryBean categoryBean : categoriesMap.values()) {
            categoryName = categoryBean.getName();
            for (int i = 1; i < 11; ++i) {
                SongDetailsBean bean = createSongDetails(aarohana, avarohana, categoryName, details,
                                                        duration[i-1], pictureUrl + (((i - 1) % 5) + 1) + ".png",
                                                        videoUrl + (((i - 1) % 10) + 1) + ".mp4", categoryName + ragam + i, categoryName + title + i);
            }
        }

        //SongDetailsBean bean = createSongDetails("S R2 G3 P D2 S", "S N3 D2 P M1 G3 R2 S", "Geethams", "", 10.5, "http://capstone-nanodegree-20160409.appspot.com.storage.googleapis.com/test.jpg", "http://capstone-nanodegree-20160409.appspot.com.storage.googleapis.com/movie.mp4", "Bilahari", "Rara Venu Gopa Bala");
    }

    private Long getCategoryId(String categoryName, HashMap<Long, SongCategoryBean> hashMap) {
        Long categoryId = -1L;
        for (Long id : hashMap.keySet()) {
            SongCategoryBean songCategoryBean = hashMap.get(id);
            if (songCategoryBean.getName().equals(categoryName)) {
                categoryId = id;
                break;
            }
        }

        return categoryId;
    }
}