package com.pestopasta.cluzcs160;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.cloud.backend.core.AsyncBlobUploader;
import com.google.cloud.backend.core.CloudBackend;
import com.google.cloud.backend.core.CloudBackendFragment;
import com.google.cloud.backend.core.CloudEntity;

import java.io.File;
import java.io.IOException;

public class DatabaseInterface extends Activity {

    private final static int AUDIO_TYPE = 1111;
    private final static int IMAGE_TYPE = 1112;
    private final static int VIDEO_TYPE = 1113;

    private static final String PROCESSING_FRAGMENT_TAG = "BACKEND_FRAGMENT";

    private FragmentManager mFragmentManager;
    private CloudBackendFragment mProcessingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_interface);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.database_interface, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public Bundle getTag(String id) {
        Bundle wrapper = new Bundle();
        try {
            CloudBackend cb = new CloudBackend();
            CloudEntity tag = cb.get("Tag", id);
            for (String key: tag.getProperties().keySet()) {
                if ((tag.get(key)) instanceof String) {
                    wrapper.putString(key, (String) tag.get(key));
                } else if ((tag.get(key)) instanceof Integer) {
                    wrapper.putInt(key, (Integer) tag.get(key));
                }
            }
        } catch (IOException e) {
            Log.e("Error", "Retrieving tag from database failed");
        }
        return wrapper;
    }

    public boolean putTag(double latitude, double longitude, Bundle wrapper) {
        if (wrapper.containsKey("Tags") && wrapper.containsKey("tagTitle") && wrapper.containsKey("tagContentType") && wrapper.containsKey("fileName")) {
            CloudEntity tag = new CloudEntity("Tag");
            String tagTitle = wrapper.getString("tagTitle");
            int tagContentType = wrapper.getInt("tagContentType");
            String fileName = wrapper.getString("fileName");
            tag.put("title", tagTitle);
            tag.put("contentType", tagContentType);
            tag.put("latitude", latitude);
            tag.put("longitude", longitude);

            try {
                CloudBackend cb = new CloudBackend();
                cb.insert(tag);
            } catch (IOException e) {
                Log.e("Error", "Updating database failed");
                return false;
            }

            mProcessingFragment = (CloudBackendFragment) mFragmentManager.
                    findFragmentByTag(PROCESSING_FRAGMENT_TAG);

            File fileUp = new File(Environment.getExternalStorageDirectory(), fileName);
            new AsyncBlobUploader(this, mProcessingFragment.getCloudBackend(), tagContentType).execute(fileUp);
            return true;
        } else {
            Log.d("Placing tag", "Missing necessary values");
            return false;
        }
    }

    public void getTagsWithinCooords(double minLatitude, double minLongitude, double maxLatitude, double maxLongitude) {

    }

}
