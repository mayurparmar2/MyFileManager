package com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO;


public class ItemTest {
    private File_DTO file_dto;
    private String time;

    public ItemTest(String str, File_DTO file_DTO) {
        this.time = str;
        this.file_dto = file_DTO;
    }

    public File_DTO getFile_dto() {
        return this.file_dto;
    }

    public String getTime() {
        return this.time;
    }
}
