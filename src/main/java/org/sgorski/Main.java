package org.sgorski;

import org.sgorski.service.FileService;

public class Main {

    private static final FileService fileService = new FileService();

    public static void main(String[] args) {
        int numberOfTasks = fileService.getNumberOfTasks();
        for(int taskNumber = 0; taskNumber<numberOfTasks; taskNumber++) {
            fileService.copyFiles(taskNumber);
        }
    }
}