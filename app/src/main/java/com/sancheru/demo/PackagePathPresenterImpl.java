package com.sancheru.demo;

public class PackagePathPresenterImpl implements PackagePathPresenter {
    @Override
    public void fetchData() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                processRequest(new DataResponseListener());
            }
        };
        new Thread(runnable).start();
    }

    private void processRequest(final AsyncListener asyncListener) {


    }
}
