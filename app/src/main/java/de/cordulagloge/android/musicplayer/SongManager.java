package de.cordulagloge.android.musicplayer;

import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.File;
import java.util.ArrayList;

/**
 * Find all MP3-Files on sdCard
 *
 * Created by Cordula Gloge on 26/03/2018.
 * Based on http://programondaspot.blogspot.de/2015/07/listing-all-audio-in-sdcard-using.html
 */

public class SongManager {
    public static ArrayList<String> getAudioFiles(){
        //String mediaPath  = Environment.getExternalStorageDirectory().toString();
        String mediaPath = "/storage/extSdCard/Music/";
        File homeFolder = new File(mediaPath);
        ArrayList<String> audioFiles = new ArrayList<>();
        File[] allFiles = homeFolder.listFiles();

        audioFiles = loopThroughFiles(allFiles, audioFiles);
        return audioFiles;
    }

    /**
     * Loop through directory to find all audio files
     * @param rootDirectoryFiles directory with files
     * @param audioFileList list where
     * @return paths of audio files
     */
    private static ArrayList<String> loopThroughFiles(File[] rootDirectoryFiles, ArrayList<String> audioFileList){
        // check if directory is empty
        if (rootDirectoryFiles != null) {
            for (File currentFile : rootDirectoryFiles) {
                if (currentFile.isDirectory()) {
                    File[] subFolder = currentFile.listFiles();
                    loopThroughFiles(subFolder, audioFileList);
                } else if (isAudioFile(currentFile)) {
                    audioFileList.add(currentFile.getAbsolutePath());
                }
            }
        }
        return audioFileList;
    }

    /**
     * Return if file is MP3 oder WAV file which should be listed
     * @param currentFile file which shal be tested
     * @return true when file is a audio file
     */
    private static boolean isAudioFile(File currentFile){
        return currentFile.getName().endsWith(".mp3") || currentFile.getName().endsWith(".MP3") ||
                currentFile.getName().endsWith(".wav");
    }
}
