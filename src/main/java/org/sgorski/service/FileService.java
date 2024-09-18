package org.sgorski.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sgorski.Compare;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.stream.Stream;

public class FileService {

    private static final Logger log = LogManager.getLogger(FileService.class);
    private final ConfigFileService configFileService = new ConfigFileService();

    public void copyFiles(int taskNumber) {
        log.info("========================================");
        log.info("Task number: {}", taskNumber + 1);
        String sourceDir = configFileService.getSourceDir(taskNumber);
        String destDir = configFileService.getDestinationDir(taskNumber);
        String[] extensions = configFileService.getExtensions(taskNumber);
        Compare comparator = configFileService.getLengthComparator(taskNumber);
        int nameLength = configFileService.getFileLength(taskNumber);
        log.info("========================================");
        log.info("Moved files:");

        File sourceFolder = getFile(sourceDir);
        File destinationFolder = getFile(destDir);

        for (String ext : extensions) {
            Optional<File[]> oFiles = Optional.ofNullable(sourceFolder.listFiles(
                    (dir, names) -> names.endsWith(ext)
            ));

            if(oFiles.isPresent()) {
                File[] files = oFiles.get();
                Stream.of(files).parallel().forEach(file -> {
                    String fileName = file.getName().replaceFirst("[.][^.]+$", "");
                    if (CompareService.compare(comparator, nameLength, fileName)) {
                        Path sourcePath = file.toPath();
                        Path destinationPath = destinationFolder.toPath().resolve(file.getName());

                        try {
                            Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                            log.info("Przeniesiono plik: {}, do folderu: {}", file.getName(), destinationPath);
                        } catch (IOException e) {
                            log.error("Błąd podczas przenoszenia pliku: {}, do folderu: {}", file.getName(), destinationPath);
                            log.error(e.getStackTrace());
                        }
                    }
                });
            }else {
                log.info("No files were found with extension: {}, in the {} path", ext, sourceDir);
            }
        }
    }

    private File getFile(String path) {
        return new File(path);
    }

    public int getNumberOfTasks(){
        return configFileService.getConfigurationChildren().length;
    }
}
