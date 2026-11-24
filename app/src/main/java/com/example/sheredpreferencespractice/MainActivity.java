package com.example.sheredpreferencespractice;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "profile_prefs";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_TEXT_SIZE = "text_size";

    private static final String LANG_AR = "ar";
    private static final String LANG_EN = "en";
    private static final String SIZE_SMALL = "small";
    private static final String SIZE_MEDIUM = "medium";
    private static final String SIZE_LARGE = "large";

    private View rootLayout;
    private TextView tvProfile;
    private RadioGroup rgTextSize;
    private RadioGroup rgLanguage;
    private RadioButton rbSmall, rbMedium, rbLarge;
    private RadioButton rbArabic, rbEnglish;
    private Button btnApply;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        keepContentAwayFromSystemBars();

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        String language = prefs.getString(KEY_LANGUAGE, LANG_AR);
        String textSize = prefs.getString(KEY_TEXT_SIZE, SIZE_MEDIUM);

        rgLanguage.check(language.equals(LANG_EN) ? R.id.rbEnglish : R.id.rbArabic);

        if (textSize.equals(SIZE_SMALL)) {
            rgTextSize.check(R.id.rbSmall);
        } else if (textSize.equals(SIZE_LARGE)) {
            rgTextSize.check(R.id.rbLarge);
        } else {
            rgTextSize.check(R.id.rbMedium);
        }

        applySettings(language, textSize);

        btnApply.setOnClickListener(v -> {
            String selectedLanguage = getSelectedLanguage();
            String selectedSize = getSelectedTextSize();

            prefs.edit()
                    .putString(KEY_LANGUAGE, selectedLanguage)
                    .putString(KEY_TEXT_SIZE, selectedSize)
                    .apply();

            applySettings(selectedLanguage, selectedSize);
        });
    }

    private void findViews() {
        rootLayout = findViewById(R.id.rootLayout);
        tvProfile = findViewById(R.id.tvProfile);
        rgTextSize = findViewById(R.id.rgTextSize);
        rgLanguage = findViewById(R.id.rgLanguage);
        rbSmall = findViewById(R.id.rbSmall);
        rbMedium = findViewById(R.id.rbMedium);
        rbLarge = findViewById(R.id.rbLarge);
        rbArabic = findViewById(R.id.rbArabic);
        rbEnglish = findViewById(R.id.rbEnglish);
        btnApply = findViewById(R.id.btnApply);
    }

    private void keepContentAwayFromSystemBars() {
        final int base = rootLayout.getPaddingLeft(); // the 15dp from the XML, in pixels
        ViewCompat.setOnApplyWindowInsetsListener(rootLayout, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(base + bars.left, base + bars.top, base + bars.right, base + bars.bottom);
            return insets;
        });
    }
    private String getSelectedLanguage() {
        if (rgLanguage.getCheckedRadioButtonId() == R.id.rbEnglish) {
            return LANG_EN;
        }
        return LANG_AR;
    }

    private String getSelectedTextSize() {
        int checkedId = rgTextSize.getCheckedRadioButtonId();
        if (checkedId == R.id.rbSmall) {
            return SIZE_SMALL;
        } else if (checkedId == R.id.rbLarge) {
            return SIZE_LARGE;
        }
        return SIZE_MEDIUM;
    }

    private void applySettings(String language, String textSize) {
        boolean arabic = language.equals(LANG_AR);

        tvProfile.setText(arabic ? R.string.profile_ar : R.string.profile_en);

        float sizeSp;
        if (textSize.equals(SIZE_SMALL)) {
            sizeSp = 14;
        } else if (textSize.equals(SIZE_LARGE)) {
            sizeSp = 22;
        } else {
            sizeSp = 18;
        }
        tvProfile.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeSp);

        rootLayout.setLayoutDirection(arabic ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        tvProfile.setTextDirection(arabic ? View.TEXT_DIRECTION_RTL : View.TEXT_DIRECTION_LTR);

        rbSmall.setText(arabic ? R.string.small_ar : R.string.small_en);
        rbMedium.setText(arabic ? R.string.medium_ar : R.string.medium_en);
        rbLarge.setText(arabic ? R.string.large_ar : R.string.large_en);
        rbArabic.setText(arabic ? R.string.arabic_ar : R.string.arabic_en);
        rbEnglish.setText(arabic ? R.string.english_ar : R.string.english_en);
        btnApply.setText(arabic ? R.string.apply_ar : R.string.apply_en);
    }
}