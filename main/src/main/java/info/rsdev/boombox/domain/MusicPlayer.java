package info.rsdev.boombox.domain;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class MusicPlayer implements Runnable {
    
    private static final Logger logger = Logger.getLogger(MusicPlayer.class.getName());
    
    private Song currentSong = null;
    
    private PlayerControls playerControls = null;
    
    public enum MusicPlayerReturnStates {
        SUCCESS,
        CANCELLED,
        ERROR
    }
    
    public MusicPlayer(Song currentSong, PlayerControls playerControls) {
        this.currentSong = currentSong;
        this.playerControls = playerControls;
    }
    
    @Override
    public void run() {
        try {
            AudioInputStream in = AudioSystem.getAudioInputStream(new File(currentSong.getUri()));
            AudioInputStream din = null;
            AudioFormat baseFormat = in.getFormat();
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16,
                    baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            din = AudioSystem.getAudioInputStream(decodedFormat, in);
            // Play now.
            MusicPlayerReturnStates state = MusicPlayerReturnStates.SUCCESS;
            boolean isCancelledByUser = rawplay(decodedFormat, din);
            if (isCancelledByUser) {
                state = MusicPlayerReturnStates.CANCELLED;
            }
            in.close();
            playerControls.onFinished(currentSong, state);
        } catch (Exception e) {
            logger.log(Level.SEVERE, String.format("Exception when playing file %s:", this.currentSong), e);
            playerControls.onFinished(currentSong, MusicPlayerReturnStates.ERROR);  //event that reports the error
        }
    }
    
    private boolean rawplay(AudioFormat targetFormat, AudioInputStream din) throws IOException, LineUnavailableException {
        byte[] data = new byte[16*1024];    //keep buffer large enough for seemless playback of lossless codecs
        SourceDataLine line = getLine(targetFormat);
        boolean userCancelled = false;
        if (line != null) {
            // Start
            line.start();
            int nBytesRead = 0;
            while ((nBytesRead != -1) && !userCancelled) {
                nBytesRead = din.read(data, 0, data.length);
                if (nBytesRead != -1) {
                    line.write(data, 0, nBytesRead);
                }
                userCancelled = Thread.currentThread().isInterrupted();
            }
            
            // Stop
            line.drain();
            line.stop();
            line.close();
            din.close();
        }
        return userCancelled;
    }
    
    private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException {
        SourceDataLine res = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        res = (SourceDataLine) AudioSystem.getLine(info);
        res.open(audioFormat);
        return res;
    }

}
