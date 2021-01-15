package chatty.util;

import chatty.Chatty;

import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class SpellChecker {

    private static Map<String, String> spellingMap;

    public SpellChecker() {

    }

    public void init() {
        spellingMap = new HashMap<>();
        getGlobalSpellingFile();
        readSpellingFile();
    }
    public void readSpellingFile() {
        try {
            String filepath = Chatty.getUserDataDirectory() + "spelling";
            File spelling = new File(filepath);
            if (!spelling.exists()) {
                filepath += "txt";
                spelling = new File(filepath);
                if (!spelling.exists()) return;
            }
            Files.lines(new File(filepath).toPath()).forEach(s -> addToMap(s));
        }
        catch (IOException IO_exception) {
            JOptionPane.showMessageDialog(null, "Es ist ein Fehler beim Spell Check einlesen passiert", "Spell Check Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    private void addToMap(String string) {
        if (string.trim().contains("#") || string.trim().isEmpty() || !string.trim().contains(";")) return;
        System.err.println(string);
        //JOptionPane.showMessageDialog(null, "Es ist ein Fehler beim Spell Check einlesen passiert\r\n" + string, "Spell Check Fehler", JOptionPane.ERROR_MESSAGE);
        String[] split = string.split(";");
        spellingMap.put(split[0], split[1]);
    }

    public String rewrite(String message, boolean allowCustomVariables) {
        String returner = "";

        String[] split = message.split(" ");

        for (int i = 0; i < split.length; i++){
            String s = split[i];
            if (allowCustomVariables && s.startsWith("{")){
                s = Chatty.getCustomVariablePlacer().place(s);
            }

            for (String pattern : spellingMap.keySet()) {
                if (s.matches(pattern)) {
                    s = spellingMap.get(pattern);
                    break;
                }
            }

            split[i] = s;

        }

        for(String s : split) returner += s + " ";

        return returner;
    }

    public String rewrite(String message) {
        return rewrite(message, false);
    }

    public String replaceHTMLChar(String string) {
        string = string.replace("%20", " ");
        return string;
    }

    public boolean getGlobalSpellingFile()
    {
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;

        try {
            url = new URL("https://recklessGreed.de/twitch/spelling");
            is = url.openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                addToMap(line);
            }
        } catch (IOException mue) {
            mue.printStackTrace();
            return false;
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                // nothing to see here
            }
        }
        return true;
    }

}
