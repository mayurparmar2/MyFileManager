package com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO;

import java.io.Serializable;
import java.util.ArrayList;


public class File_DTO implements Serializable {
    private String abumid;
    private String abumname;
    private String artistid;
    private String artistname;
    private String date;
    private String duration;
    private ArrayList<File_DTO> file_dtos;
    private String id;
    private String lastmodified;
    private String minetype;
    private String name;
    private String packagename;
    private String path;
    private String realpath;
    private String size;
    private String title;
    private int totalitem;
    private String volumname;

    public ArrayList<File_DTO> getFile_dtos() {
        return this.file_dtos;
    }

    public void setFile_dtos(ArrayList<File_DTO> arrayList) {
        this.file_dtos = arrayList;
    }

    public String getLastmodified() {
        return this.lastmodified;
    }

    public void setLastmodified(String str) {
        this.lastmodified = str;
    }

    public String getVolumname() {
        return this.volumname;
    }

    public void setVolumname(String str) {
        this.volumname = str;
    }

    public String getRealpath() {
        return this.realpath;
    }

    public void setRealpath(String str) {
        this.realpath = str;
    }

    public String getMinetype() {
        return this.minetype;
    }

    public void setMinetype(String str) {
        this.minetype = str;
    }

    public String getPackagename() {
        return this.packagename;
    }

    public void setPackagename(String str) {
        this.packagename = str;
    }

    public int getTotalitem() {
        return this.totalitem;
    }

    public void setTotalitem(int i) {
        this.totalitem = i;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String str) {
        this.duration = str;
    }

    public String getAbumid() {
        return this.abumid;
    }

    public void setAbumid(String str) {
        this.abumid = str;
    }

    public String getArtistid() {
        return this.artistid;
    }

    public void setArtistid(String str) {
        this.artistid = str;
    }

    public String getArtistname() {
        return this.artistname;
    }

    public void setArtistname(String str) {
        this.artistname = str;
    }

    public String getAbumname() {
        return this.abumname;
    }

    public void setAbumname(String str) {
        this.abumname = str;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String str) {
        this.date = str;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String str) {
        this.size = str;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }
}
