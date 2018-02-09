package com.carto.advancedmap.baseclasses.activities;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.MenuItem;
import android.widget.Toast;

import com.carto.advancedmap.main.MainActivity;
import com.carto.advancedmap.utils.Colors;

/**
 * Created by aareundo on 20/09/16.
 */
public class BaseActivity extends Activity {

    protected static final int MARSHMALLOW = 23;

    protected  boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= MARSHMALLOW;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ColorDrawable background = new ColorDrawable(Colors.ACTION_BAR);
        getActionBar().setBackgroundDrawable(background);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        String title = getIntent().getStringExtra(MainActivity.TITLE);
        String description = getIntent().getStringExtra(MainActivity.DESCRIPTION);

        setTitle(title);
        getActionBar().setSubtitle(description);
    }

    protected boolean isPaused = true;

    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void requestPermission(String permission) {
        ActivityCompat.requestPermissions(this, new String[]{ permission }, 1);
    }

    protected void requestPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(this, permissions, 1);
    }

    public void alert(final String message)
    {
        if (isPaused) {
            System.out.println("Prevented alert (" + message + ") in paused activity");
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
