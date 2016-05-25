package com.sai.nanodegree.capstone.cloud;

import android.content.Context;
import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.sai.nanodegree.capstone.Constants;
import com.sai.nanodegree.capstone.backend.songDetailsEndPointApi.SongDetailsEndPointApi;
import com.sai.nanodegree.capstone.backend.songDetailsEndPointApi.model.SongDetailsBean;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by smajeti on 5/15/16.
 */
public class SongDetailsEndpointUtils {
    public enum CommandType {
        NONE,
        GET_SONG_DETAILS,
        CREATE_SONG_DETAILS
    }

    public interface CallbackIface {
        void onCommandExecutionDone(SongDetailsBean sdBean, CommandType cmdType);
        void onCommandExecutionDone(List<SongDetailsBean> sdBeanList, CommandType cmdType);
    }


    public static class EndpointsAsyncTask extends AsyncTask<Object, Void, Object> {
        private static final String TAG = EndpointsAsyncTask.class.getSimpleName();

        private static SongDetailsEndPointApi sdEndPointApi = null;
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
                    case GET_SONG_DETAILS:
                        callbackIface.onCommandExecutionDone((List<SongDetailsBean>)result, cmdType);
                        break;
                    case CREATE_SONG_DETAILS:
                        callbackIface.onCommandExecutionDone((SongDetailsBean)result, cmdType);
                        break;
                }
            }
        }

        private Object run(Object... params) {
            if(sdEndPointApi == null) {  // Only do this once
                SongDetailsEndPointApi.Builder builder = new SongDetailsEndPointApi.Builder(AndroidHttp.newCompatibleTransport(),
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

                sdEndPointApi = builder.build();
            }

            switch (cmdType) {
                case NONE:
                    break;
                case GET_SONG_DETAILS:
                    return runGetSongDetails(params);
                case CREATE_SONG_DETAILS:
                    return runCreateSongDetails(params);
                default:
                    break;
            }

            return null;
        }

        private List<SongDetailsBean> runGetSongDetails(Object... params) {
            context = (Context) params[0];
            Long after = (Long) params[1];

            try {
                return sdEndPointApi.getAllSongsDetails(after).execute().getItems();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        private SongDetailsBean runCreateSongDetails(Object... params) {
            context = (Context) params[0];
            String aarohana = (String) params[1];
            String avarohana = (String) params[2];
            String categoryName = (String) params[3];
            String details = (String) params[4];
            Double duration = (Double) params[5];
            String pictureUrl = (String) params[6];
            String videoUrl = (String) params[7];
            String ragam = (String) params[8];
            String title = (String) params[9];
            Date updatedDate = (Date) params[10];
            Date creationDate = (Date) params[11];

            try {
                return sdEndPointApi.createSongDetails(aarohana, avarohana, categoryName, details, duration, pictureUrl, videoUrl, ragam, title).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
