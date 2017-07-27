package com.upadhyay.nilay.driver.UserInterface;

import android.view.View;

/**
 * Created by nilay on 4/7/2017.
 */

public interface Communicator {
    public void auctionClicked(View view, int position, String[] auctionDetail);
    public void loginResponse(String username, String type);
}
