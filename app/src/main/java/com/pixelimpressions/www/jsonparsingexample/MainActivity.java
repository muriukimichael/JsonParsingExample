package com.pixelimpressions.www.jsonparsingexample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ContactListFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ContactListFragment extends ListFragment {

        private ProgressDialog pDialog;

        //Url to get the JSON
        private static String url = "http://api.androidhive.info/contacts/";

        //JSON node names
        private static final String TAG_CONTACTS = "contacts";
        private static final String TAG_ID = "id";
        private static final String TAG_NAME = "name";
        private static final String TAG_EMAIL = "email";
        private static final String TAG_ADDRESS = "address";
        private static final String TAG_GENDER = "gender";
        private static final String TAG_PHONE = "phone";
        private static final String TAG_PHONE_MOBILE = "mobile";
        private static final String TAG_PHONE_HOME = "home";
        private static final String TAG_PHONE_OFFICE = "office";

        //contacts JSONArray
        JSONArray contacts = null;

        //Hashmap for Listview
        ArrayList<HashMap<String, String>> contactList;

        public ContactListFragment() {

        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            contactList = new ArrayList<HashMap<String, String>>();
            ListView lv = getListView();

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            return rootView;
        }
    }
}
