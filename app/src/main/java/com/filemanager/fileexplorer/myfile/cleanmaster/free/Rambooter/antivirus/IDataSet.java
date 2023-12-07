package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import java.util.Collection;
import java.util.Set;


public interface IDataSet<T> {
//    boolean addItem(Object t);

    boolean addItems(Collection<? extends T> collection);

    boolean addItem(Object obj);

    int getItemCount();

    Set<T> getSet();

    boolean removeItem(T t);
}
