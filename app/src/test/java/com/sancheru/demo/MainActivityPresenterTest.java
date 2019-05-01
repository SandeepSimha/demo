package com.sancheru.demo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MainActivityPresenterTest {
    private MainActivityPresenter mainActivityPresenter;

    @Mock
    private MainActivityViewInterface mainActivityViewInterface;

    @Before
    public void setUp() throws Exception {
        mainActivityPresenter = new MainActivityPresenter(mainActivityViewInterface);
    }

    @Test
    public void testIfPresenterUpdateCharacterLeftText() {
        //Arrange
        int noteLength = 150;
        int maxLength = 200;

        String count = String.valueOf(maxLength - noteLength);

        mainActivityPresenter.setCharacterCount(noteLength, maxLength);
        Mockito.verify(mainActivityViewInterface).updateCharacterCount(count);

    }
}