package com.sai.nanodegree.capstone.cloud;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.sai.nanodegree.capstone.Constants;
import com.sai.nanodegree.capstone.backend.userEndPointApi.UserEndPointApi;
import com.sai.nanodegree.capstone.backend.userEndPointApi.model.UserBean;

import java.io.IOException;

/**
 * Created by smajeti on 5/4/16.
 */
public class UserEndPointUtils {

    public enum CommandType {
        NONE,
        GET_USER,
        CREATE_USER
    }

    public interface CallbackIface {
        void onCommandExecutionDone(UserBean userBean, CommandType cmdType);
    }


    public static class EndpointsAsyncTask extends AsyncTask<Object, Void, UserBean> {
        private static final String TAG = EndpointsAsyncTask.class.getSimpleName();

        private static UserEndPointApi userEndPointApi = null;
        private Context context;
        private CommandType cmdType = CommandType.NONE;
        private CallbackIface callbackIface = null;


        public void setCommandType(CommandType cmdType) {
            this.cmdType = cmdType;
        }

        public void setCallback(CallbackIface callback) {
            this.callbackIface = callback;
        }

        @Override
        protected UserBean doInBackground(Object... params) {
            if(userEndPointApi == null) {  // Only do this once
                UserEndPointApi.Builder builder = new UserEndPointApi.Builder(AndroidHttp.newCompatibleTransport(),
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

                userEndPointApi = builder.build();
            }

            switch (cmdType) {
                case NONE:
                    break;
                case GET_USER:
                    return runGetUser(params);
                case CREATE_USER:
                    return runCreateUser(params);
                default:
                    break;
            }

            return null;
        }

        @Override
        protected void onPostExecute(UserBean result) {
            if (callbackIface != null) {
                callbackIface.onCommandExecutionDone(result, cmdType);
            }
            Log.d(TAG, "User Values ID: " + result.getId() + " email: " + result.getEmail() + " DisplayName: " + result.getDisplayName());
        }

        private UserBean runGetUser(Object... params) {
            context = (Context) params[0];
            String email = (String) params[1];

            try {
                return userEndPointApi.getUser(email).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        private UserBean runCreateUser(Object... params) {
            context = (Context) params[0];
            String displayName = (String) params[1];
            String email = (String) params[2];

            try {
                return userEndPointApi.createUser(displayName, email).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
