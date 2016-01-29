package com.joshbgold.simplemusicplayer;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class SongsManager {

    public HashMap<String, String> song;
    public String songTitle;
    private String uniqueSongIDString = "0";
    public Context mContext;

    // SDCard Path
    private String MEDIA_PATH = "/storage/extSdCard/music";
    private String tempMediaPath = "";
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> filteredSongsList = new ArrayList<>();


    public SongsManager(Context context) {
        mContext = context;
    }

    /**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     */
    public ArrayList<HashMap<String, String>> getPlayList() {

        //If user has selected a media folder, retrieve that now
        MainActivity mainActivity = new MainActivity();
        tempMediaPath = mainActivity.getFolderPath();
        if(tempMediaPath != ""){
            MEDIA_PATH = tempMediaPath;
            Toast.makeText(mContext, "tempMediaPath is set to " + tempMediaPath + "." + "MEDIA_PATH is set to " + MEDIA_PATH + ".", Toast
                    .LENGTH_LONG).show();
        }

        File home = new File(MEDIA_PATH);

        uniqueSongIDString = "0";
        int uniqueSongIDInt = 0;

        if (songsList != null){
            songsList.clear();
        }

        if (home.listFiles(new FileExtensionFilter()).length > 0) {
            for (File file : home.listFiles(new FileExtensionFilter())) {
                song = new HashMap<>();

                //songTitle = file.getName().substring(0, (file.getName().length() - 4));  //not necessary to omit the file type
                songTitle = file.getName();

                //remove track numbers from song titles
                songTitle = songTitle.replaceFirst("^\\d*\\s", "");  //replaces leading digits & following space
                songTitle = songTitle.replaceFirst("^\\d*\\-\\d*", "");  //replaces leading digits, following hyphen, and following digits

                song.put("songTitle", songTitle);
                song.put("songPath", file.getPath());
                song.put("songUniqueID", uniqueSongIDString);
                uniqueSongIDInt++;
                uniqueSongIDString = String.valueOf(uniqueSongIDInt);

                // Adding each song to SongList
                songsList.add(song);
            }
        }
        // return songs playlist_item array
        return songsList;
    }

    /**
     * Class to filter files which are having .mp3 extension
     */
    class FileExtensionFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3") || name.endsWith(".MP3") || name.endsWith(".wma"));
        }
    }

    // Filter Class
    public ArrayList<HashMap<String, String>> filter(String searchString) {
        searchString = searchString.toLowerCase(Locale.getDefault());

        songsList.clear();
        songsList = getPlayList();

        //searchString is empty, so show all songs in results
        if (searchString.length() == 0) {

            if (filteredSongsList != null){
                filteredSongsList.clear();
            }
            filteredSongsList = songsList;
        }

        //only return songs that match the search string
        else {

            if (filteredSongsList != null){
                filteredSongsList.clear();
            }

            for (HashMap<String, String> song : songsList) {
                if (song != null) {
                    String songTitle = song.get("songTitle");
                    if (songTitle.toLowerCase().contains(searchString)) {
                        filteredSongsList.add(song);
                    }
                }
            }
        }

        return filteredSongsList;
    }
}
