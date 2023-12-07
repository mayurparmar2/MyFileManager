package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public abstract class JSONDataSet<T extends IJSONSerializer> implements IDataSet<T> {
    Context _context;
    IDataSetChangesListener _dataSetChangesListener;
    String _filePath;
    IFactory<T> _nodeFactory;
    private Set<T> _set;

//
//    @Override
//    public boolean addItem(Object obj) {
//        return addItem((JSONDataSet<T>) ((IJSONSerializer) obj));
//    }
//
//
//    @Override
//    public boolean removeItem(Object obj) {
//        return removeItem( ((IJSONSerializer) obj));
//    }

    @Override
    public int getItemCount() {
        return this._set.size();
    }

    @Override
    public Set<T> getSet() {
        return this._set;
    }

    void setSet(Set<T> set) {
        this._set = set;
    }

    public void setDataSetChangesListener(IDataSetChangesListener iDataSetChangesListener) {
        this._dataSetChangesListener = iDataSetChangesListener;
    }

    public void unregisterDataSetChangesListener() {
        this._dataSetChangesListener = null;
    }

    private JSONDataSet() {
        this._filePath = null;
        this._context = null;
        this._nodeFactory = null;
        this._dataSetChangesListener = null;
    }

    public JSONDataSet(Context context, String str, IFactory<T> iFactory) {
        this._filePath = null;
        this._context = null;
        this._nodeFactory = null;
        this._dataSetChangesListener = null;
        this._context = context;
        this._nodeFactory = iFactory;
        this._set = new HashSet();
        String str2 = StaticTools.getInternalDataPath(this._context) + File.separatorChar + str;
        this._filePath = str2;
        if (!StaticTools.existsFile(str2)) {
            try {
                StaticTools.writeTextFile(this._filePath, "{\n  \"data\": [  ]\n}\n");
                return;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        loadFromJSON();
    }

    public void loadFromJSON() {
        try {
            JSONArray jSONArray = new JSONObject(StaticTools.loadJSONFromFile(this._context, this._filePath)).getJSONArray("data");
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                T createInstance = this._nodeFactory.createInstance(jSONObject.getString("type"));
                createInstance.loadFromJSON(jSONObject);
                this._set.add(createInstance);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public synchronized void writeToJSON() {
        try {
            try {
                JSONArray jSONArray = new JSONArray();
                for (T t : this._set) {
                    jSONArray.put(t.buildJSONObject());
                }
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("data", jSONArray);
                StaticTools.writeTextFile(this._filePath, jSONObject.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
    }

    public boolean addItem(Object t) {
        IDataSetChangesListener iDataSetChangesListener;
        boolean add = this._set.add((T) t);
        if (add && (iDataSetChangesListener = this._dataSetChangesListener) != null) {
            iDataSetChangesListener.onSetChanged();
        }
        return add;
    }

    public boolean removeItem(T t) {
        IDataSetChangesListener iDataSetChangesListener;
        boolean remove = this._set.remove(t);
        if (remove && (iDataSetChangesListener = this._dataSetChangesListener) != null) {
            iDataSetChangesListener.onSetChanged();
        }
        return remove;
    }

    @Override
    public boolean addItems(Collection<? extends T> collection) {
        IDataSetChangesListener iDataSetChangesListener;
        boolean addAll = this._set.addAll(collection);
        if (addAll && (iDataSetChangesListener = this._dataSetChangesListener) != null) {
            iDataSetChangesListener.onSetChanged();
        }
        return addAll;
    }
}
