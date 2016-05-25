package com.sai.nanodegree.capstone;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by smajeti on 4/23/16.
 */
public class VideoLibraryItemData {
    public String songName;
    public int itemImgId;
    public int watchedPercent;

    public static List<VideoLibraryItemData> getSampleData() {
        Random random = new Random();
        ArrayList<VideoLibraryItemData> libItemsList = new ArrayList<VideoLibraryItemData>(6);
        VideoLibraryItemData data = new VideoLibraryItemData();
        data.songName = "Rara Venu Gopabala";
        data.itemImgId = R.drawable.rara_venu;
        data.watchedPercent = random.nextInt(100);
        libItemsList.add(data);

        data = new VideoLibraryItemData();
        data.songName = "Aanalekara Unni";
        data.itemImgId = R.drawable.instruments_img;
        data.watchedPercent = random.nextInt(100);
        libItemsList.add(data);

        data = new VideoLibraryItemData();
        data.songName = "Sree Gananaatha";
        data.itemImgId = R.drawable.peacock_img;
        data.watchedPercent = random.nextInt(100);
        libItemsList.add(data);

        data = new VideoLibraryItemData();
        data.songName = "Vara Veena Mridu";
        data.itemImgId = R.drawable.veena_img;
        data.watchedPercent = random.nextInt(100);
        libItemsList.add(data);

        data = new VideoLibraryItemData();
        data.songName = "Mandara Dharare";
        data.itemImgId = R.drawable.rainbow_img;
        data.watchedPercent = random.nextInt(100);
        libItemsList.add(data);

        data = new VideoLibraryItemData();
        data.songName = "Sambha Siva Yanave";
        data.itemImgId = R.drawable.peacock_img;
        data.watchedPercent = random.nextInt(100);
        libItemsList.add(data);

        data = new VideoLibraryItemData();
        data.songName = "Rara Venu Gopabala";
        data.itemImgId = R.drawable.rara_venu;
        data.watchedPercent = random.nextInt(100);
        libItemsList.add(data);

        data = new VideoLibraryItemData();
        data.songName = "Aanalekara Unni";
        data.itemImgId = R.drawable.instruments_img;
        data.watchedPercent = random.nextInt(100);
        libItemsList.add(data);

        data = new VideoLibraryItemData();
        data.songName = "Sree Gananaatha";
        data.itemImgId = R.drawable.peacock_img;
        data.watchedPercent = random.nextInt(100);
        libItemsList.add(data);

        data = new VideoLibraryItemData();
        data.songName = "Vara Veena Mridu";
        data.itemImgId = R.drawable.veena_img;
        data.watchedPercent = random.nextInt(100);
        libItemsList.add(data);

        data = new VideoLibraryItemData();
        data.songName = "Mandara Dharare";
        data.itemImgId = R.drawable.rainbow_img;
        data.watchedPercent = random.nextInt(100);
        libItemsList.add(data);

        data = new VideoLibraryItemData();
        data.songName = "Sambha Siva Yanave";
        data.itemImgId = R.drawable.peacock_img;
        data.watchedPercent = random.nextInt(100);
        libItemsList.add(data);

        return libItemsList;
    }
}
