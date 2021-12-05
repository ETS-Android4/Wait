package com.example.daysmatter.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class MatterList extends LitePalSupport implements Serializable {

    private ArrayList<Matter> matterList;

    public MatterList(ArrayList<Matter> matterList){
        this.matterList = matterList;
    }

    public MatterList() {
        this.matterList = new ArrayList<>();
    }

    /**
     * this function will get the number of Matters in the habitList
     * @return integer indicating number of Matters in the list
     */
    public int getCount() {
        return matterList.size();
    }

    public void addMatter(Matter matter) {
        matterList.add(matter);
    }

    public void deleteMatter(Matter matter) {
        matterList.remove(matter);
    }

    /**
     * This function will return the specific Matter at the specified index
     * @param index the index of the Matter
     * @return the matter at the certain index
     */
    public Matter getMatter(int index) {
        return matterList.get(index);
    }

    public Matter getMatter(String title) throws Exception {
        for (Matter matter : matterList) {
            if (matter.getTitle().equals(title)) {
                return matter;
            }
        }
        throw new Exception("Habit does not exist");
    }

    /**
     * This function clears the matter list
     */
    public void clearMatterList() {
        this.matterList.clear();
    }

    /**
     * this function will return true if the matter is in the list and false otherwise
     * @param matter The habit to search for in the list
     */
    public Boolean hasMatter(Matter matter) {
        return matterList.contains(matter);
    }

    /**
     * Generate a unique number identifier
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(matterList);
    }

    /**
     * Getters and Setters
     */
    public ArrayList<Matter> getMatterList() {
        return matterList;
    }
}
