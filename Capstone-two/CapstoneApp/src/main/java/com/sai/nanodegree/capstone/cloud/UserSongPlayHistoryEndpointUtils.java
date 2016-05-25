package com.sai.nanodegree.capstone.cloud;

import android.content.Context;
import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.sai.nanodegree.capstone.Constants;
import com.sai.nanodegree.capstone.backend.songCategoryEndPointApi.model.SongCategoryBean;
import com.sai.nanodegree.capstone.backend.userSongPlayHistoryEndPointApi.UserSongPlayHistoryEndPointApi;
import com.sai.nanodegree.capstone.backend.userSongPlayHistoryEndPointApi.model.UserSongPlayHistoryBean;

import java.io.IOException;
import java.util.List;

/**
 * Created by smajeti on 5/15/16.
 */
public class UserSongPlayHistoryEndpointUtils {
    public enum CommandType {
        NONE,
        GET_PLAY_HISTORY,
        GET_UPDATED_HISTORY,
        CREATE_PLAY_HISTORY
    }

    public interface CallbackIface {
        void onCommandExecutionDone(UserSongPlayHistoryBean userSongPlayHistoryBean, CommandType cmdType);
        void onCommandExecutionDone(List<UserSongPlayHistoryBean> usphBeanList, CommandType cmdType);
    }


    public static class EndpointsAsyncTask extends AsyncTask<Object, Void, Object> {
        private static final String TAG = EndpointsAsyncTask.class.getSimpleName();

        private static UserSongPlayHistoryEndPointApi playHistoryEndPointApi = null;
        private Context context;
        private CommandType cmdType = CommandType.NONE;
        private CallbackIface callbackIface = null;


        public void setCommandType(CommandType cmdType) {
            this.cmdType = cmdType;
        }

        public void setCallback(CallbackIface callback) {
            this.callbackIface = callback;
        }

        public Object doInForegournd(Object... params) {
            return run(params);
        }

        @Override
        protected Object doInBackground(Object... params) {
            return run(params);
        }

        private Object run(Object... params) {
            if(playHistoryEndPointApi == null) {  // Only do this once
                UserSongPlayHistoryEndPointApi.Builder builder = new UserSongPlayHistoryEndPointApi.Builder(AndroidHttp.newCompatibleTransport(),
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

                playHistoryEndPointApi = builder.build();
            }

            switch (cmdType) {
                case NONE:
                    break;
                case GET_PLAY_HISTORY:
                    return runGetHistory(params);
                case GET_UPDATED_HISTORY:
                    return runGetUpdatedHistory(params);
                case CREATE_PLAY_HISTORY:
                    return runCreateHistory(params);
                default:
                    break;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (callbackIface != null) {
                switch (cmdType) {
                    case GET_PLAY_HISTORY:
                    case CREATE_PLAY_HISTORY:
                        callbackIface.onCommandExecutionDone((UserSongPlayHistoryBean)result, cmdType);
                        break;
                    case GET_UPDATED_HISTORY:
                        callbackIface.onCommandExecutionDone((List<UserSongPlayHistoryBean>)result, cmdType);
                        break;
                }
            }
        }

        private UserSongPlayHistoryBean runGetHistory(Object... params) {
            context = (Context) params[0];
            String email = (String) params[1];
            String title = (String) params[2];
            String category = (String) params[3];
            String ragam = (String) params[4];
            Double playedTime = (Double) params[5];

            try {
                return playHistoryEndPointApi.songPlayHistory("get", email,title, category, ragam, playedTime).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        private List<UserSongPlayHistoryBean> runGetUpdatedHistory(Object... params) {
            context = (Context) params[0];
            Long after = (Long) params[1];
            String email = (String) params[2];

            try {
                return playHistoryEndPointApi.getUpdatedHistory(after, email).execute().getItems();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }


        private UserSongPlayHistoryBean runCreateHistory(Object... params) {
            context = (Context) params[0];
            String email = (String) params[1];
            String title = (String) params[2];
            String category = (String) params[3];
            String ragam = (String) params[4];
            Double playedTime = (Double) params[5];

            try {
                return playHistoryEndPointApi.songPlayHistory("update", email,title, category, ragam, playedTime).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
