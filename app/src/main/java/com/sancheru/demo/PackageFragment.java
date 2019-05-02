package com.sancheru.demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class PackageFragment extends Fragment implements View.OnClickListener {

    private TextView mPackageName;
    private Button mButton;
    private PackagePathPresenter mPackagePresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_package, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPackagePresenter.fetchData();
    }

    private void initViews(View view) {
        mPackageName = view.findViewById(R.id.tv_package);
        mButton = view.findViewById(R.id.bt_reveal_path);

        mPackageName.setText("/data/user/0/com.trekbikes.codetest/files/MyFile.zip");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.bt_reveal_path) {
            processButtonClick();
        }
    }

    private void processButtonClick() {

    }

    public static void downloadFile(String url, File outputFile) {
        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();

            DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
            fos.write(buffer);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            return; // swallow a 404
        } catch (IOException e) {
            return; // swallow a 404
        }
    }
}
