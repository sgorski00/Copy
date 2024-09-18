package org.sgorski;

import org.sgorski.service.FileService;

public class Main {

    private static final FileService fileService = new FileService();

    public static void main(String[] args) {
        fileService.copyFiles();
    }
}