package com.sai.nanodegree.capstone.cloud;

import android.content.Context;
import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.sai.nanodegree.capstone.Constants;
import com.sai.nanodegree.capstone.backend.songCategoryEndPointApi.SongCategoryEndPointApi;
import com.sai.nanodegree.capstone.backend.songCategoryEndPointApi.model.SongCategoryBean;

import java.io.IOException;
import java.util.List;

/**
 * Created by smajeti on 5/12/16.
 */
public class SongCategoryEndpointUtils {
    public enum CommandType {
        NONE,
        GET_CATEGORIES,
        CREATE_CATEGORY
    }

    public interface CallbackIface {
        void onCommandExecutionDone(SongCategoryBean scBean, CommandType cmdType);
        void onCommandExecutionDone(List<SongCategoryBean> scBeanList, CommandType cmdType);
    }


    public static class EndpointsAsyncTask extends AsyncTask<Object, Void, Object> {
        private static final String TAG = EndpointsAsyncTask.class.getSimpleName();

        private static SongCategoryEndPointApi scEndPointApi = null;
        private Context context;
        private CommandType cmdType = CommandType.NONE;
        private CallbackIface callbackIface = null;


        public void setCommandType(CommandType cmdType) {
            this.cmdType = cmdType;
        }

        public void setCallback(CallbackIface callback) {
            this.callbackIface = callback;
        }

        public Object doInForeground(Object... params) {
            return run(params);
        }

        @Override
        protected Object doInBackground(Object... params) {
            return run(params);
        }

        @Override
        protected void onPostExecute(Object result) {
            if (callbackIface != null) {
                switch (cmdType) {
                    case GET_CATEGORIES:
                        callbackIface.onCommandExecutionDone((List<SongCategoryBean>)result, cmdType);
                        break;
                    case CREATE_CATEGORY:
                        callbackIface.onCommandExecutionDone((SongCategoryBean)result, cmdType);
                        break;
                }
            }
        }

        private Object run(Object... params) {
            if(scEndPointApi == null) {  // Only do this once
                SongCategoryEndPointApi.Builder builder = new SongCategoryEndPointApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // options for running against local devappserver
                        // - 10.0.2.2 is localhost's IP address in Android emulator
                        // - turn off compression when running against local devappserver
                        .setRootUrl(Constants.CLOUD_END_POINTS_URL)
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                // end options for devappserver

                scEndPointApi = builder.build();
            }

            switch (cmdType) {
                case NONE:
                    break;
                case GET_CATEGORIES:
                    return runGetCategories(params);
                case CREATE_CATEGORY:
                    return runCreateCategory(params);
                default:
                    break;
            }

            return null;
        }

        private List<SongCategoryBean> runGetCategories(Object... params) {
            context = (Context) params[0];
            Long after = (Long) params[1];

            try {
                return scEndPointApi.getCategories(after).execute().getItems();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        private SongCategoryBean runCreateCategory(Object... params) {
            context = (Context) params[0];
            String name = (String) params[1];
            Long level = (Long) params[2];

            try {
                return scEndPointApi.createCategory(name, level).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
