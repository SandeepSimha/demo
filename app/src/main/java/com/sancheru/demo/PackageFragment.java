package com.sancheru.demo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
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
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PackageFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "PackageFragment";
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

        mPackageName.setText("https://drive.google.com/open?id=1ehzo4dS1Fe6s5GfyCOdRfvbNK-eqCFrf");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.bt_reveal_path) {
            //downloadFile("https://www.dropbox.com/s/s1dqshs8s4mkxq4/AZipFile.zip?dl=0", null);
            byRetrofit();
        }
    }

    private void byRetrofit() {
        progressDoalog.show();

        /*Create handle for the RetrofitInstance interface*/
        GetDataService downloadService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ResponseBody> call = downloadService.downloadFileWithDynamicUrlSync("https://koenig-media.raywenderlich.com/uploads/2018/08/RW_Kotlin_Cheatsheet_1_0.pdf");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "server contacted and has file");

                    boolean writtenToDisk = writeResponseBodyToDisk(response.body());

                    Log.d(TAG, "file download was a success? " + writtenToDisk);
                } else {
                    Log.d(TAG, "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "error " + t.getLocalizedMessage());
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(Environment.getExternalStoragePublicDirectory(null) + File.separator + "MyFile.zip");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
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
