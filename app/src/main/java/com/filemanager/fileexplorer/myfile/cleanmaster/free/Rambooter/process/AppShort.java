package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.process;

import java.util.Comparator;


public class AppShort implements Comparator {
    public int compare(TaskInfo taskInfo, TaskInfo taskInfo2) {
        return (taskInfo == null || (taskInfo2 != null && taskInfo.mem > taskInfo2.mem)) ? -1 : 1;
    }

    @Override
    public int compare(Object obj, Object obj2) {
        return compare((TaskInfo) obj, (TaskInfo) obj2);
    }
}
