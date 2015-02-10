package com.pixelimpressions.www.jsonparsingexample;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            new GetContacts().execute();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            return rootView;
        }

        private class GetContacts extends AsyncTask<Void, Void, Void> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //show progress dialog
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Please wait momentarily...");
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                ServiceHandler serviceHandler = new ServiceHandler();

                //make the request to url and get the response
                String jsonStr = serviceHandler.makeServiceCall(url, ServiceHandler.GET);
                Log.d("Response: ", "> " + jsonStr);
                if (jsonStr != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);

                        //get the array node
                        contacts = jsonObject.getJSONArray(TAG_CONTACTS);
                        //loop through the contacts
                        for (int i = 0; i < contacts.length(); i++) {
                            JSONObject contact = contacts.getJSONObject(i);

                            String id = contact.getString(TAG_ID);
                            String name = contact.getString(TAG_NAME);
                            String email = contact.getString(TAG_EMAIL);
                            String address = contact.getString(TAG_ADDRESS);
                            String gender = contact.getString(TAG_GENDER);

                            //Phone node is a JSON Object
                            JSONObject phoneObject = contact.getJSONObject(TAG_PHONE);
                            String mobile = phoneObject.getString(TAG_PHONE_MOBILE);
                            String home = phoneObject.getString(TAG_PHONE_HOME);
                            String office = phoneObject.getString(TAG_PHONE_OFFICE);

                            //tmp Hashmap for single contact
                            HashMap<String, String> contactMap = new HashMap<String, String>();
                            //add each child node to hash map key => value
                            contactMap.put(TAG_ID, id);
                            contactMap.put(TAG_NAME, name);
                            contactMap.put(TAG_EMAIL, email);
                            contactMap.put(TAG_PHONE_MOBILE, mobile);

                            //adding contact to contact list
                            contactList.add(contactMap);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //Dismiss the dialog
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }

                // update parsed data into the listview
                ListAdapter adapter = new SimpleAdapter(getActivity(),
                        contactList,
                        R.layout.list_item,
                        new String[]{TAG_NAME, TAG_EMAIL, TAG_PHONE_MOBILE},
                        new int[]{R.id.name_textview, R.id.email_textview, R.id.mobile_textview});
                setListAdapter(adapter);
            }
        }
    }
}
