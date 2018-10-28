package controller;

import com.darkprograms.speech.synthesiser.SynthesiserV2;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.IOException;

class TextToSpeech {
    private SynthesiserV2 synthesiser = new SynthesiserV2("AIzaSyC8ypj4Tk5HlrTZK9-PumktkcDTzTwd2gs");
    void speak(String text) {
        Thread thread = new Thread (() -> {
            try {
                AdvancedPlayer player = new AdvancedPlayer(synthesiser.getMP3Data(text));
                player.play();
            }
            catch (IOException | JavaLayerException e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(false); // App doesn't shutdown before thread terminates
        thread.start();
    }
}
