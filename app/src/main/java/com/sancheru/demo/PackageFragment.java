package com.sancheru.demo;

import android.app.ProgressDialog;
import android.content.Context;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PackageFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "PackageFragment";
    private TextView mPackageName;
    private Button mButton;
    private ProgressDialog progressDoalog;
    public static final String EMPTY_STRING = "";

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

    private void initViews(View view) {
        mPackageName = view.findViewById(R.id.tv_package);
        mButton = view.findViewById(R.id.bt_reveal_path);
        mButton.setOnClickListener(this);

        mPackageName.setText(EMPTY_STRING);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.bt_reveal_path) {
            byRetrofit();
        }
    }

    private void byRetrofit() {
        progressDoalog.show();

        /*Create handle for the RetrofitInstance interface*/
        GetDataService downloadService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ResponseBody> call = downloadService.downloadFileWithDynamicUrlSync("https://www.dropbox.com/s/s1dqshs8s4mkxq4/AZipFile.zip");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "server contacted and has file");

                    File writtenToDiskFileLocation = writeResponseBodyToDisk(getActivity(), response.body());
                    if (writtenToDiskFileLocation != null)
                        mPackageName.setText(writtenToDiskFileLocation.toString());

                    Log.d(TAG, "file download was a success? " + writtenToDiskFileLocation);
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

    private File writeResponseBodyToDisk(Context context, ResponseBody body) {
        try {
            File futureStudioIconFile = new File(context.getExternalFilesDir(null) + File.separator + "MyFile.zip");
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
                    progressDoalog.cancel();
                }

                outputStream.flush();

                return futureStudioIconFile;
            } catch (IOException e) {
                return null;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return null;
        }
    }
}
