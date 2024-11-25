package org.sgorski.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sgorski.Compare;
import org.sgorski.Operation;

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

    public void executeTask(int taskNumber) {
        log.info("========================================");
        log.info("         * Task information *");
        log.info("========================================");
        log.info("Task name: {}", configFileService.getConfigurationChildren()[taskNumber]);
        Operation operation = configFileService.getOperation(taskNumber);
        String sourceDir = configFileService.getSourceDir(taskNumber);
        String destDir = configFileService.getDestinationDir(taskNumber);
        String[] extensions = configFileService.getExtensions(taskNumber);
        Compare comparator = configFileService.getLengthComparator(taskNumber);
        int nameLength = configFileService.getFileLength(taskNumber);
        String filenamePattern = configFileService.getFilenamePattern(taskNumber);
        log.info("========================================");
        log.info("          * Task execution *");
        log.info("========================================");

        File sourceFolder = getFile(sourceDir);
        File destinationFolder = getFile(destDir);

        for (String ext : extensions) {
            Optional<File[]> oFiles = Optional.ofNullable(sourceFolder.listFiles(
                    (dir, name) -> name.matches(filenamePattern + "\\." + ext)
            ));

            if(oFiles.isPresent()) {
                File[] files = oFiles.get();
                Stream.of(files).parallel().forEach(file -> {
                    String fileName = file.getName().replaceFirst("[.][^.]+$", "");
                    if (CompareService.compare(comparator, nameLength, fileName)) {
                        try {
                            handleOperation(file, destinationFolder, operation);
                        } catch (IOException e) {
                            log.error("Error while executing the task for the file: {}", file.getName());
                            e.getStackTrace();
                        }
                    }
                });
            }else {
                log.info("No files were found with extension: {}, in the {} path", ext, sourceDir);
            }
        }
        log.info("The end of the task");
        log.info("");
    }

    private static void handleOperation(File file, File destinationFolder, Operation operation) throws IOException {
        Path sourcePath = file.toPath();
        Path destinationPath = destinationFolder.toPath().resolve(file.getName());

        if(Files.exists(destinationPath)) {
            return;
        }

        if(operation.equals(Operation.MOVE) && !destinationFolder.toString().isEmpty()) {
            Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("The file: {}, has been moved to: {}", file.getName(), destinationPath);
        }else if(operation.equals(Operation.COPY) && !destinationFolder.toString().isEmpty()) {
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("The file: {}, has been copied to: {}", file.getName(), destinationPath);
        }else if(operation.equals(Operation.DELETE)) {
            Files.delete(sourcePath);
            log.info("The file: {}, has been deleted", file.getName());
        }else{
            log.error("Destination path can not be empty for this operation!");
        }
    }

    private File getFile(String path) {
        return new File(path);
    }

    public int getNumberOfTasks(){
        return configFileService.getConfigurationChildren().length;
    }
}
