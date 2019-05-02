package com.sancheru.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class PackageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);

        openPackageFragment();
    }

    private void openPackageFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new PackageFragment())
                .commitAllowingStateLoss();
    }

}
