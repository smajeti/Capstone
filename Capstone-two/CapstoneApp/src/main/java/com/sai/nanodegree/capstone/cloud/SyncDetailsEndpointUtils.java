package com.sai.nanodegree.capstone.cloud;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.sai.nanodegree.capstone.Constants;
import com.sai.nanodegree.capstone.backend.syncDetailsEndPointApi.SyncDetailsEndPointApi;
import com.sai.nanodegree.capstone.backend.syncDetailsEndPointApi.model.SyncDetailsBean;

import java.io.IOException;
import java.util.Date;

/**
 * Created by smajeti on 5/15/16.
 */
public class SyncDetailsEndpointUtils {
    public enum CommandType {
        NONE,
        GET_SYNC_DETAILS,
        UPDATE_SYNC_TO_SERVER_TIME,
        UPDATE_SYNC_FROM_SERVER_TIME
    }

    public interface CallbackIface {
        void onCommandExecutionDone(SyncDetailsBean syncDetailsBean, CommandType cmdType);
    }


    public static class EndpointsAsyncTask extends AsyncTask<Object, Void, SyncDetailsBean> {
        private static final String TAG = EndpointsAsyncTask.class.getSimpleName();

        private static SyncDetailsEndPointApi syncDetailsEndPointApi = null;
        private Context context;
        private CommandType cmdType = CommandType.NONE;
        private CallbackIface callbackIface = null;


        public void setCommandType(CommandType cmdType) {
            this.cmdType = cmdType;
        }

        public void setCallback(CallbackIface callback) {
            this.callbackIface = callback;
        }

        public SyncDetailsBean doInForeground(Object... params) {
            return run(params);
        }

        @Override
        protected SyncDetailsBean doInBackground(Object... params) {
            return run(params);
        }

        @Override
        protected void onPostExecute(SyncDetailsBean result) {
            if (callbackIface != null) {
                callbackIface.onCommandExecutionDone(result, cmdType);
            }
        }

        private SyncDetailsBean run(Object... params) {
            if(syncDetailsEndPointApi == null) {  // Only do this once
                SyncDetailsEndPointApi.Builder builder = new SyncDetailsEndPointApi.Builder(AndroidHttp.newCompatibleTransport(),
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

                syncDetailsEndPointApi = builder.build();
            }

            switch (cmdType) {
                case NONE:
                    break;
                case GET_SYNC_DETAILS:
                    return runGetSyncDetails(params);
                case UPDATE_SYNC_TO_SERVER_TIME:
                    return runUpdateSyncTime(true, params);
                case UPDATE_SYNC_FROM_SERVER_TIME:
                    return runUpdateSyncTime(false, params);
                default:
                    break;
            }

            return null;
        }

        private SyncDetailsBean runGetSyncDetails(Object... params) {
            context = (Context) params[0];
            String email = (String) params[1];

            try {
                return syncDetailsEndPointApi.getSyncDetails(email).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        private SyncDetailsBean runUpdateSyncTime(boolean syncToFlag, Object... params) {
            context = (Context) params[0];
            String email = (String) params[1];
            Long syncTime = (Long) params[2];

            try {
                return syncDetailsEndPointApi.updateSyncTime(email, (syncToFlag ? -1L : syncTime), (syncToFlag ? syncTime : -1L)).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
