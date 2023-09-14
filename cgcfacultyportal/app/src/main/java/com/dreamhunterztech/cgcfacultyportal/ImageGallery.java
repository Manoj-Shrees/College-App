package com.dreamhunterztech.cgcfacultyportal;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.veinhorn.scrollgalleryview.MediaInfo;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suwas on 10-12-2016.
 */

public class ImageGallery extends AppCompatActivity
{
    private static ArrayList<String> images = new ArrayList<>();

    private ScrollGalleryView scrollGalleryView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagegallerylayout);
        String value = getIntent().getExtras().get("img_url").toString();
        images.add(value);
        List<MediaInfo> infos = new ArrayList<>(images.size());
        for (String url : images) infos.add(MediaInfo.mediaLoader(new PicassoImageLoader(url)));
        scrollGalleryView = (ScrollGalleryView) findViewById(R.id.mediagallery);
        scrollGalleryView
                .setThumbnailSize(100)
                .setZoom(true)
                .setFragmentManager(getSupportFragmentManager())
                .addMedia(infos);
    }

    private Bitmap toBitmap(int image) {
        return ((BitmapDrawable) getResources().getDrawable(image)).getBitmap();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        images.clear();
    }

}
