package com.example.ch_b;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

public class Photo extends AppCompatActivity {
    SubsamplingScaleImageView imageView;
    View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo);

        imageView = findViewById(R.id.image);
        if (Profile.maskProfileImage.getImageProfile().exists()) {

            Bitmap myBitmap = BitmapFactory.decodeFile(Profile.maskProfileImage.getImageProfile().getAbsolutePath());
            imageView.setImage(ImageSource.bitmap(myBitmap));
        }

    }
    public void Close(View view)
    {
        Profile.maskProfileImage = null;
        startActivity(new Intent(this, Profile.class));
    }

    public void Delete(View view)
    {
        try {
            Profile.maskProfileImage.imageProfile.delete();
            Profile.maskProfileImage = null;
            Toast.makeText(Photo.this, "Фотография успешно удалена", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Photo.this, Profile.class));
        }
        catch (Exception e)
        {
            Toast.makeText(Photo.this, "При удаление фотографии возникла ошибка!", Toast.LENGTH_SHORT).show();
        }
    }
}
