package org.sgorski.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ini4j.Ini;
import org.ini4j.IniPreferences;
import org.sgorski.Compare;

import java.io.File;
import java.io.IOException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class ConfigFileService {
    private static final String FILENAME = System.getProperty("user.dir") + "\\config.ini";
    private static final Logger log = LogManager.getLogger(ConfigFileService.class);

    public Preferences getIniPreferences(String filename) {
        Preferences iniPrefs = null;
        try {
            Ini ini =  new Ini(new File(filename));
            iniPrefs = new IniPreferences(ini);
        }catch (IOException e) {
            log.error("File: {} not found.", filename);
            System.exit(1);
        }
        return iniPrefs;
    }

    public String getSourceDir(int taskNumber) {
        String sourceDir = getField(taskNumber, "SourceDirectory");
        log.info("Source directory: {}", sourceDir);
        return sourceDir;
    }

    public String getDestinationDir(int taskNumber) {
        String destinationDir = getField(taskNumber, "DestinationDirectory");
        log.info("Destination directory: {}", destinationDir);
        return destinationDir;
    }

    public String[] getExtensions(int taskNumber) {
        String extensionsString = getField(taskNumber, "Extensions");
        String[] extensions = extensionsString.split(",");
        for(String ex : extensions){
            if(extensions.length == 1 && ex.isEmpty()){
                ex = ".*";
            }else{
                ex = '.' + ex.trim();
            }
            log.info("Extension from list: {}", ex);
        }
        return extensions;
    }

    public Compare getLengthComparator(int taskNumber) {
        String lengthComparator = getField(taskNumber, "LengthComparator")
                .toUpperCase();
        Compare compare = null;
        try {
            compare = Compare.valueOf(lengthComparator);
            log.info("Lenght comparator: {}", compare);

        }catch (IllegalArgumentException e){
            if(!lengthComparator.isEmpty()) {
                log.info("Comparator for string: {} does not exists.", lengthComparator);
            }
            System.exit(1);
        }
        return compare;
    }

    public int getFileLength(int taskNumber){
        String lengthString = getField(taskNumber, "FileLength");
        int length = 0;
        try {
            length = Integer.parseInt(lengthString);
            if(length<0){
                throw new IllegalArgumentException("Length can not be less than 0.");
            }
            log.info("File name length: {}", length);
        }catch (NumberFormatException e){
            log.error("Wrong format in the field FileLength: {}", lengthString);
            System.exit(1);
        }catch (IllegalArgumentException e){
            log.error(e.getMessage());
            System.exit(1);
        }
        return length;
    }

    private String getField(int taskNumber, String variable) {
        String[] tasks = getConfigurationChildren();
        return getIniPreferences(FILENAME)
                .node(tasks[taskNumber])
                .get(variable, null);
    }

    public String[] getConfigurationChildren(){
        try {
            return getIniPreferences(FILENAME).childrenNames();
        } catch (BackingStoreException e) {
            log.error("Something went wrong with configuration file!");
            System.exit(1);
            return new String[]{"FirstTask"};
        }
    }
}
