package de.till_s.spotifyshuffleradio.helper.spotify.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;

import de.till_s.spotifyshuffleradio.helper.tasks.BackgroundTask;
import de.till_s.spotifyshuffleradio.service.OverlayService;

/**
 * Created by Till on 27.04.2017.
 */

public class SpotifyUtils {

    private static final String TAG = SpotifyUtils.class.getSimpleName();

    /**
     * Open spotify and start music
     *
     * @param context    Context     Android context
     * @param userUri    String      URI of Spotify user (see Spotify)
     * @param playlistID String      ID of Spotify playlist
     * @param play       boolean     Autoplay
     */
    public static void openSpotify(final Context context, String userUri, String playlistID, boolean play) {
        final String uri = userUri + ":playlist:" + playlistID + (play ? ":play" : "");

        Log.i("SpotifyUtils", uri);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent launcher = new Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
                launcher.setClassName("com.spotify.music", "com.spotify.music.MainActivity");
                launcher.setData(Uri.parse(uri));
                launcher.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(launcher);
            }
        };

        BackgroundTask backgroundTask = new BackgroundTask(runnable, null);
        backgroundTask.execute();
    }

    /**
     * Toggle an {@see Intent#ACTION_MEDIA_BUTTON} from KeyEvent
     *
     * @param context   Context     Android context
     * @param id        int         KeyEvent media key
     */
    private static void toggleKey(Context context, int id) {
        String componentPackage = "com.spotify.music";
        String componentReceiver = "com.spotify.music.internal.receiver.MediaButtonReceiver";
        ComponentName componentName = new ComponentName(componentPackage, componentReceiver);
        Intent i = null;

        i = new Intent(Intent.ACTION_MEDIA_BUTTON);
        i.setComponent(componentName);
        i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, id));
        context.sendOrderedBroadcast(i, null);

        i = new Intent(Intent.ACTION_MEDIA_BUTTON);
        i.setComponent(componentName);
        i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, id));
        context.sendOrderedBroadcast(i, null);
    }

    /**
     * Showing Yes-No Popup
     *
     * @param context Context     Android context
     */
    public static void showPopup(Context context) {
        Intent intent = new Intent(context, OverlayService.class);
        context.startService(intent);
    }

}
