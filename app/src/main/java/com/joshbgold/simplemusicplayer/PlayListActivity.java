package com.joshbgold.simplemusicplayer;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class PlayListActivity extends ListActivity {

    private EditText editSearch;
    private ImageView searchIcon;
    private SimpleAdapter adapter;
    private ListView listView;
    private ArrayList<HashMap<String, String>> songsListData;
    private int songsAddedCounter = 0;  //counter for debugging -> are songs being added to list?

    // Songs playlist_item
    public ArrayList<HashMap<String, String>> songsList = new ArrayList<>();
    public ArrayList<HashMap<String, String>> filteredSongsList = new ArrayList<HashMap<String, String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist);

        songsListData = new ArrayList<>();

        final SongsManager songsManager = new SongsManager();
        // get all songs from SD card
        this.songsList = songsManager.getPlayList();

        createListViewUsingSongs();


        // Set up the layout elements for this activity
        editSearch = (EditText) findViewById(R.id.search);
        searchIcon = (ImageView) findViewById(R.id.search_icon);

        /**
         * When user clicks search icon, execute a search, then update the ListView adapter
         */
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String text = editSearch.getText().toString().toLowerCase(Locale.getDefault());
                filteredSongsList = songsManager.filter(text);
                updateListViewUsingSongs();
            }
        });

        // selecting single ListView item
        listView = getListView();
        // listening to single playlist_item item click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                // getting list item index
                                                int songIndex = position;

                                                // Starting new intent
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                // Sending songIndex to PlayerActivity
                                                intent.putExtra("songIndex", songIndex);
                                                setResult(100, intent);
                                                // Closing PlayListView
                                                finish();
                                            }
                                        }

        );
    }

    private void createListViewUsingSongs() {
            // looping through playlist
            for (int i = 0; i < songsList.size(); i++){
                // creating new HashMap
                HashMap<String, String> song = songsList.get(i);
                // adding HashList to ArrayList
                songsListData.add(song);

        }

        // Adding menuItems to ListView
        adapter = new SimpleAdapter(this, songsListData,
                R.layout.playlist_item, new String[]{"songTitle"}, new int[]{
                R.id.songTitle});

        //listView.setAdapter(adapter);
        setListAdapter(adapter);
    }

    private void updateListViewUsingSongs() {

        // looping through playlist
        for (int i = 0; i < filteredSongsList.size(); i++) {
            // creating new HashMap
            HashMap<String, String> song = songsList.get(i);
            // adding HashList to ArrayList
            songsListData.add(song);
            songsAddedCounter++;
        }
        Toast.makeText(getApplicationContext(), "Search results: " + songsAddedCounter + " songs", Toast.LENGTH_SHORT).show();

        adapter = new SimpleAdapter(this, songsListData,
                R.layout.playlist_item, new String[]{"songTitle"}, new int[]{
                R.id.songTitle});

        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}