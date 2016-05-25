/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.sai.nanodegree.capstone.backend;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultIterable;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.oauth.OAuthRequestException;

import java.io.IOException;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "myApi",
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
public class MyEndpoint {

    /**
     * A simple endpoint method that takes a name and says Hi back
     */
    @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String name, User user) throws OAuthRequestException, IOException {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        NamespaceManager.set("capstone");
        Query q = new Query("UserData");
        Query.FilterPredicate emailIdPredicate = new Query.FilterPredicate("emailId", Query.FilterOperator.EQUAL, "john.doe@doe.com");
        PreparedQuery pq = datastore.prepare(q);
        QueryResultIterable<Entity> ri = pq.asQueryResultIterable();
        QueryResultIterator<Entity> iterator = ri.iterator();

        Entity result = iterator.next();

        String userName = iterator.getIndexList().size() + " KK";

        //BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        //GcsService gcsService = GcsServiceFactory.createGcsService();

        //Entity newe = new Entity("UserData", "capstone");
        //newe.setProperty("emailId", "mike.mike@mike.com");
        //newe.setProperty("userName", "Mike Mike");
        //datastore.put(newe);


        MyBean response = new MyBean();
        if (result != null) {
            response.setData((String) result.getProperty("userName"));
            response.setVideoRelPath((String) result.getProperty("videoUri"));
        }

        if ((user != null)) {
            response.setData("Welcome1, " + user.getEmail());
        }

        return response;
    }

}
