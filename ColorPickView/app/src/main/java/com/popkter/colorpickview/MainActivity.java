package com.popkter.colorpickview;

import static com.popkter.colorpickview.ColorPickerCommonUtil.hsbToRgb;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();

        ColorPickerView mColorPickerView = findViewById(R.id.color_picker_view);
        TextView mTopText = findViewById(R.id.background);
        TextView mBottomText = findViewById(R.id.rgbvalue);

        mColorPickerView.setOnColorChangedListener(hsb -> {
            mTopText.setBackgroundColor(Color.HSVToColor(hsb));
            mBottomText.setText(hsbToRgb(hsb));
        });
    }


}