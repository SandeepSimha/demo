package com.sancheru.demo;

public class MainActivityPresenter implements MainActivityPresenterInterface {


    private MainActivityViewInterface viewInterface;


    public MainActivityPresenter(MainActivityViewInterface viewInterface) {
        this.viewInterface = viewInterface;
    }

    @Override
    public void setCharacterCount(int noteLength, int maxLength) {
        String count = String.valueOf(maxLength - noteLength);
        viewInterface.updateCharacterCount(count);
    }

    @Override
    public void setLabelColor(int noteLength, int maxLength) {

    }
}
