package com.sancheru.demo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sancheru.demo.network.GetDataService;
import com.sancheru.demo.network.RetrofitClientInstance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PackageFragment extends Fragment implements View.OnClickListener {

    private TextView mPackageName;
    private Button mButton;
    private PackagePathPresenter mPackagePresenter;
    private ProgressDialog progressDoalog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_package, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDoalog = new ProgressDialog(getContext());
        progressDoalog.setMessage("Loading....");

        initViews(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        //mPackagePresenter.fetchData();
    }

    private void initViews(View view) {
        mPackageName = view.findViewById(R.id.tv_package);
        mButton = view.findViewById(R.id.bt_reveal_path);
        mButton.setOnClickListener(this);

        mPackageName.setText("/data/user/0/com.trekbikes.codetest/files/MyFile.zip");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.bt_reveal_path) {
            downloadFile("https://www.dropbox.com/s/s1dqshs8s4mkxq4/AZipFile.zip?dl=0", null);
            //byRetrofit();
        }
    }

    private void byRetrofit() {
        progressDoalog.show();

        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<File> call = service.getZipFile();
        call.enqueue(new Callback<File>() {
            @Override
            public void onResponse(Call<File> call, Response<File> response) {
                progressDoalog.dismiss();
                Log.e("Pak", "onSucess");
            }

            @Override
            public void onFailure(Call<File> call, Throwable t) {
                progressDoalog.dismiss();
                Log.e("Pak", "failure " + t.getMessage());
            }
        });
    }

    public static void downloadFile(final String url, final File outputFile) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    /*URL u = new URL(url);
                    URLConnection conn = u.openConnection();
                    conn.connect();

                    int contentLength = conn.getContentLength();

                    DataInputStream stream = new DataInputStream(u.openStream());

                    byte[] buffer = new byte[contentLength];
                    stream.readFully(buffer);
                    stream.close();

                    DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
                    fos.write(buffer);
                    fos.flush();
                    fos.close();*/

                    URL sourceUrl = new URL(url);
                    URLConnection conn = sourceUrl.openConnection();
                    conn.connect();
                    InputStream inputStream = conn.getInputStream();

                    int fileSize = conn.getContentLength();

                    File savefilepath = new File("/data/user/0/com.trekbikes.codetest/files/");
                    if (!savefilepath.exists()) {
                        savefilepath.mkdirs();
                    }
                    File savefile = new File("/data/user/0/com.trekbikes.codetest/files/");
                    if (savefile.exists()) {
                        savefile.delete();
                    }
                    savefile.createNewFile();

                    FileOutputStream outputStream = new FileOutputStream("/data/user/0/com.trekbikes.codetest/files/", true);
                    byte[] buffer = new byte[1024];
                    int readCount = 0;
                    int readNum = 0;
                    int prevPercent = 0;
                    while (readCount < fileSize && readNum != -1) {
                        readNum = inputStream.read(buffer);
                        if (readNum > -1) {
                            outputStream.write(buffer);

                            readCount = readCount + readNum;

                            int percent = (int) (readCount * 100 / fileSize);
                            if (percent > prevPercent) {
                                prevPercent = percent;
                            }
                        }
                    }
                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();

                } catch (FileNotFoundException e) {
                    return; // swallow a 404
                } catch (IOException e) {
                    return; // swallow a 404
                }
            }
        });
    }
}
