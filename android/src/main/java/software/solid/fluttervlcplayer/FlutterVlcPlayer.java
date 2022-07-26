package software.solid.fluttervlcplayer;

//import org.videolan.libvlc.LibVLC;
//import org.videolan.libvlc.Media;
//import org.videolan.libvlc.MediaPlayer;
//import org.videolan.libvlc.RendererDiscoverer;
//import org.videolan.libvlc.RendererItem;
//import org.videolan.libvlc.interfaces.IMedia;
//import org.videolan.libvlc.interfaces.IVLCVout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.view.TextureRegistry;
import software.solid.fluttervlcplayer.Enums.HwAcc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

final class FlutterVlcPlayer implements PlatformView {

    private final String TAG = this.getClass().getSimpleName();
    private final boolean debug = false;
    //
    private final Context context;
    private final VLCTextureView textureView;
    private final TextureRegistry.SurfaceTextureEntry textureEntry;
    //
    private final QueuingEventSink mediaEventSink = new QueuingEventSink();
    private final EventChannel mediaEventChannel;
    //
    private final QueuingEventSink rendererEventSink = new QueuingEventSink();
    private final EventChannel rendererEventChannel;
    //
    private List<String> options;
      private boolean isDisposed = false;

    // Platform view
    @Override
    public View getView() {
        return textureView;
    }

    @Override
    public void dispose() {
        if (isDisposed)
            return;
        //
        textureView.dispose();
        textureEntry.release();
        mediaEventChannel.setStreamHandler(null);
        rendererEventChannel.setStreamHandler(null);


        isDisposed = true;
    }

    // VLC Player
    FlutterVlcPlayer(int viewId, Context context, BinaryMessenger binaryMessenger, TextureRegistry textureRegistry) {
        this.context = context;
        // event for media
        mediaEventChannel = new EventChannel(binaryMessenger, "flutter_video_plugin/getVideoEvents_" + viewId);
        mediaEventChannel.setStreamHandler(
                new EventChannel.StreamHandler() {
                    @Override
                    public void onListen(Object o, EventChannel.EventSink sink) {
                        mediaEventSink.setDelegate(sink);
                    }

                    @Override
                    public void onCancel(Object o) {
                        mediaEventSink.setDelegate(null);
                    }
                });
        // event for renderer
        rendererEventChannel = new EventChannel(binaryMessenger, "flutter_video_plugin/getRendererEvents_" + viewId);
        rendererEventChannel.setStreamHandler(
                new EventChannel.StreamHandler() {
                    @Override
                    public void onListen(Object o, EventChannel.EventSink sink) {
                        rendererEventSink.setDelegate(sink);
                    }

                    @Override
                    public void onCancel(Object o) {
                        rendererEventSink.setDelegate(null);
                    }
                });
        //
        textureEntry = textureRegistry.createSurfaceTexture();
        textureView = new VLCTextureView(context);
        textureView.setSurfaceTexture(textureEntry.surfaceTexture());
        textureView.forceLayout();
        textureView.setFitsSystemWindows(true);
    }

    // private Uri getStreamUri(String streamPath, boolean isLocal) {
    //     return isLocal ? Uri.fromFile(new File(streamPath)) : Uri.parse(streamPath);
    // }

    public void initialize(List<String> options) {
        this.options = options;

        setupVlcMediaPlayer();
    }

    private void setupVlcMediaPlayer() {
    }

    void play() {

    }

    void pause() {

    }

    void stop() {

    }

    boolean isPlaying() {
        return false;
    }

    boolean isSeekable() {
        return false;
    }

    void setStreamUrl(String url, boolean isAssetUrl, boolean autoPlay, long hwAcc) {
    }

    void setLooping(boolean value) {
    }

    void setVolume(long value) {
    }

    int getVolume() {
    }

    void setPlaybackSpeed(double value) {
    }

    float getPlaybackSpeed() {
    }

    void seekTo(int location) {
    }

    long getPosition() {
        return 0;
    }

    long getDuration() {
        return 0;
    }

    int getSpuTracksCount() {
        return 0;
    }

    HashMap<Integer, String> getSpuTracks() {

        HashMap<Integer, String> subtitles = new HashMap<>();

        return subtitles;
    }

    void setSpuTrack(int index) {

    }

    int getSpuTrack() {
        return 0;
    }

    void setSpuDelay(long delay) {

    }

    long getSpuDelay() {
        return 0;
    }

    void addSubtitleTrack(String url, boolean isSelected) {

    }

    int getAudioTracksCount() {
        return 0;
    }

    HashMap<Integer, String> getAudioTracks() {

        HashMap<Integer, String> audios = new HashMap<>();

        return audios;
    }

    void setAudioTrack(int index) {

    }

    int getAudioTrack() {
        return 0;
    }

    void setAudioDelay(long delay) {

    }

    long getAudioDelay() {
        return 0;

    }

    void addAudioTrack(String url, boolean isSelected) {

    }

    int getVideoTracksCount() {
        return 0;

    }

    HashMap<Integer, String> getVideoTracks() {

        HashMap<Integer, String> videos = new HashMap<>();

        return videos;
    }

    void setVideoTrack(int index) {

    }

    int getVideoTrack() {
        return 0;

    }

    void setVideoScale(float scale) {

    }

    float getVideoScale() {
        return 0;

    }

    void setVideoAspectRatio(String aspectRatio) {

    }

    String getVideoAspectRatio() {
        return "";

    }

    void startRendererScanning(String rendererService) {

    }

    void stopRendererScanning() {
    }

    ArrayList<String> getAvailableRendererServices() {

        ArrayList<String> availableRendererServices = new ArrayList<>();

        return availableRendererServices;
    }

    HashMap<String, String> getRendererDevices() {
        HashMap<String, String> renderers = new HashMap<>();

        return renderers;
    }

    void castToRenderer(String rendererDevice) {
    }

    String getSnapshot() {
        if (textureView == null) return "";

        Bitmap bitmap = textureView.getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP);
    }

    Boolean startRecording(String directory) {
        return false;
    }

    Boolean stopRecording() {
        return false;
    }

    private void log(String message) {
        if (debug) {
            Log.d(TAG, message);
        }
    }

}
