package com.example.comp7855;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int IMAGE_REQUEST = 1;
    public static final int SEARCH_ACTIVITY_REQUEST_CODE = 2;
    private ImageView imageViewer;
    private TextView count;
    private TextView capShow;
    private TextView dateShow;
    private TextView latShow;
    private TextView longShow;
    private EditText cap;
    private ArrayList<String> photoGallery;
    private String currentPhotoPath = null;
    private int currentPhotoIndex = 0;
    RunSearch runSearch;

    Animation animFadeIn, animFadeOut, animBounce1, animBounce2, animBounce3, animBounce4, animBounce5, animBounce6, animBounce7, animCrossFadeIn, animCrossFadeOut;
    Button leftButton, rightButton, clearButton, searchButton, snapButton, captionButton, uploadButtonn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runSearch = new RunSearch();
        this.cap = this.findViewById(R.id.editCaption);
        this.imageViewer = this.findViewById(R.id.imageView);
        photoGallery = populateGallery();
        this.count = this.findViewById(R.id.photoCount);
        this.capShow = this.findViewById(R.id.textCaption);
        this.dateShow = this.findViewById(R.id.textDate);
        this.latShow = this.findViewById(R.id.textLat);
        this.longShow = this.findViewById(R.id.textLong);
        count.setText(String.valueOf(photoGallery.size()));
        capShow.setText("N/A");
        dateShow.setText("N/A");
        latShow.setText("N/A");
        longShow.setText("N/A");


        imageViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewer.setVisibility(View.VISIBLE);
                imageViewer.startAnimation(animCrossFadeIn);
                if (photoGallery.size() > 0) {
                    currentPhotoPath = photoGallery.get(currentPhotoIndex);
                    try {

                        displayPhoto(currentPhotoPath);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else
                    imageViewer.setImageResource(R.drawable.ic_launcher_background);
                imageViewer.startAnimation(animCrossFadeOut);

            }
        });

        // set bounce animation for left
        animBounce1 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce);
        leftButton = (Button) findViewById(R.id.leftButton);


        // set bounce animation for right
        animBounce2 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce);
        rightButton = (Button) findViewById(R.id.rightButton);

        // set bounce animation for clear
        animBounce3 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce);
        clearButton = (Button) findViewById(R.id.clearButton);

        // set bounce animation for search
        animBounce4 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce);
        searchButton = (Button) findViewById(R.id.searchButton);

        // set bounce animation for snap
        animBounce5 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce);
        snapButton = (Button) findViewById(R.id.snapButton);

        // set bounce animation for caption
        animBounce6 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce);
        captionButton = (Button) findViewById(R.id.captionButton);
        captionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captionButton.startAnimation(animBounce6);
            }
        });

        // set bounce animation for upload
        animBounce7 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce);
        uploadButtonn = (Button) findViewById(R.id.uploadButton);
        uploadButtonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadButtonn.startAnimation(animBounce7);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK) {
            photoGallery = populateGallery();
            currentPhotoIndex = photoGallery.size() - 1;
            currentPhotoPath = photoGallery.get(currentPhotoIndex);
            galleryAddPic();


            imageViewer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageViewer.setVisibility(View.VISIBLE);
                    imageViewer.startAnimation(animCrossFadeIn);
                    imageViewer.startAnimation(animCrossFadeOut);

                    if (photoGallery.size() > 0) {
                        currentPhotoPath = photoGallery.get(currentPhotoIndex);
                        try {

                            displayPhoto(currentPhotoPath);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            count.setText(String.valueOf(photoGallery.size()));
        }

        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                photoGallery = runSearch.run(photoGallery, data.getStringExtra("STARTDATE"),
                        data.getStringExtra("ENDDATE"), data.getStringExtra("NAME"),
                        data.getStringExtra("LAT"), data.getStringExtra("LONG"),
                        data.getStringExtra("DIST"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoGallery.size() > 0) {
                currentPhotoIndex = 0;
                currentPhotoPath = photoGallery.get(currentPhotoIndex);
                try {
                    displayPhoto(currentPhotoPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                count.setText(String.valueOf(photoGallery.size()));
            }
            else
                displayDefault();
        }

        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            currentPhotoIndex = data.getIntExtra("OLDPICTURE", photoGallery.size() - 1);
            currentPhotoPath = photoGallery.get(currentPhotoIndex);
            try {
                displayPhoto(currentPhotoPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            count.setText(String.valueOf(photoGallery.size()));
        }
    }

    public void leftClicked() {
        leftButton.startAnimation(animBounce1);
    }

    public void leftClick(View view) throws IOException {
        leftClicked();
        if (photoGallery.size() > 0) {
            currentPhotoIndex--;
            if (currentPhotoIndex < 0)
                currentPhotoIndex = 0;
            if (currentPhotoIndex >= photoGallery.size())
                currentPhotoIndex = photoGallery.size() - 1;
            currentPhotoPath = photoGallery.get(currentPhotoIndex);
            displayPhoto(currentPhotoPath);
        } else {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, "No Photos In Gallery", duration);
            toast.show();
        }
    }

    public void rightClicked() {
        rightButton.startAnimation(animBounce2);
    }

    public void rightClick(View view) throws IOException {
        rightClicked();
        if (photoGallery.size() > 0) {
            currentPhotoIndex++;
            if (currentPhotoIndex < 0)
                currentPhotoIndex = 0;
            if (currentPhotoIndex >= photoGallery.size())
                currentPhotoIndex = photoGallery.size() - 1;
            currentPhotoPath = photoGallery.get(currentPhotoIndex);
            displayPhoto(currentPhotoPath);
        } else {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, "No Photos In Gallery", duration);
            toast.show();
        }

    }

    public void snapClicked() {
        snapButton.startAnimation(animBounce5);
    }

    public void snapClick(View view) {
        snapClicked();
        // Create an Intent to take a picture
        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Check if camera hardware is available
        if (takePic.resolveActivity(getPackageManager()) != null) {

            // Create a file to capture the image
            File imageFile = null;
            try {
                imageFile = createImageFile();
            } catch (IOException e) {
                Log.d("FileCreation", "Failed");
            }
            // If image file was created properly, start image activity
            if (imageFile != null) {
                Uri imageUri = FileProvider.getUriForFile(this,
                        "com.example.comp7855.fileprovider", imageFile);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePic, IMAGE_REQUEST);
            }
        }
    }

    public void searchClicked() {
        searchButton.startAnimation(animBounce4);
    }

    public void searchClick(View view) {
        searchClicked();
        if (photoGallery.size() > 0) {
            Intent search = new Intent(MainActivity.this, SearchActivity.class);
            search.putExtra("photoIndex", currentPhotoIndex);
            startActivityForResult(search, SEARCH_ACTIVITY_REQUEST_CODE);
        } else {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, "No Photos In Gallery", duration);
            toast.show();
        }
    }

    public void clearClicked() {
        clearButton.startAnimation(animBounce3);
    }

    public void clearClick(View view) throws IOException {
        clearClicked();
        photoGallery = populateGallery();
        if (photoGallery.size() > 0) {
            currentPhotoIndex = 0;
            currentPhotoPath = photoGallery.get(currentPhotoIndex);
            cap.setText("");
            displayPhoto(currentPhotoPath);
        }
        else {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, "No Photos In Gallery", duration);
            toast.show();
        }
        count.setText(String.valueOf(photoGallery.size()));
    }

    public void captionClick(View view) throws IOException {
        String caption = cap.getText().toString();
        ExifInterface exif;
        if (photoGallery.size() > 0){
            exif = new ExifInterface(photoGallery.get(currentPhotoIndex));
            exif.setAttribute(ExifInterface.TAG_USER_COMMENT,caption);
            exif.saveAttributes();
            currentPhotoPath = photoGallery.get(currentPhotoIndex);
            displayPhoto(currentPhotoPath);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageName, ".jpg", storageDir);
        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    private ArrayList<String> populateGallery() {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.comp7855/files/Pictures");
        ArrayList<String> imggallery = new ArrayList<>();
        File[] fList = file.listFiles();
        if (fList != null) {
            for (File f : file.listFiles()) {
                imggallery.add(f.getPath());
            }
        }
        return imggallery;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void displayPhoto(String path) throws IOException {
        this.imageViewer = this.findViewById(R.id.imageView);
        imageViewer.setImageBitmap(BitmapFactory.decodeFile(path));
        displayPhotoInfo(path);
    }

    private void displayPhotoInfo (String path) throws IOException {
        String caption, date, lat, lon;
        float[] latLong = {0,0};
        ExifInterface exif;
        date = path;
        exif = new ExifInterface(path);
        caption = exif.getAttribute(ExifInterface.TAG_USER_COMMENT);

        if (date.contains("JPEG_")){
            String[] date_parsed = date.split("_", 4);
            if (date_parsed.length > 3){
                date = date_parsed[1] + " " + date_parsed[2];
                dateShow.setText(date);
            }
            else
                dateShow.setText("N/A");
        }
        else
            dateShow.setText("N/A");

        if (TextUtils.isEmpty(caption))
            capShow.setText("N/A");
        else
            capShow.setText(caption);

        if (exif.getLatLong(latLong)){
            if (latLong[0] >= -90 && latLong[0] <= 90)
                latShow.setText(String.valueOf(latLong[0]));
            else
                latShow.setText("N/A");

            if (latLong[1] >= -180 && latLong[1] <= 180)
                longShow.setText(String.valueOf(latLong[1]));
            else
                longShow.setText("N/A");
        }
        else {
            latShow.setText("N/A");
            longShow.setText("N/A");
        }
    }

    private void displayDefault() {
        this.imageViewer = this.findViewById(R.id.imageView);
        imageViewer.setImageResource(R.drawable.ic_launcher_background);
        count.setText(String.valueOf(photoGallery.size()));
        capShow.setText("N/A");
        dateShow.setText("N/A");
        latShow.setText("N/A");
        longShow.setText("N/A");
    }
}