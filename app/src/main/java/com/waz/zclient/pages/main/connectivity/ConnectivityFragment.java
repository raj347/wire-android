/**
 * Wire
 * Copyright (C) 2016 Wire Swiss GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.waz.zclient.pages.main.connectivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.waz.zclient.R;
import com.waz.zclient.core.stores.network.NetworkStoreObserver;
import com.waz.zclient.pages.BaseFragment;
import com.waz.zclient.ui.animation.interpolators.penner.Quart;
import com.waz.zclient.utils.ViewUtils;

public class ConnectivityFragment extends BaseFragment<ConnectivityFragment.Container> implements NetworkStoreObserver,
                                                                                                  ConnectivityIndicatorView.OnExpandListener {

    public static final String TAG = ConnectivityFragment.class.getName();


    private ConnectivityIndicatorView connectivityIndicatorView;
    private ImageView connectivityIndicatorViewForeground;

    public static ConnectivityFragment newInstance() {
        return new ConnectivityFragment();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //  LifeCycle
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainLayout = inflater.inflate(R.layout.fragment_connectivity, container, false);
        connectivityIndicatorView = ViewUtils.getView(mainLayout, R.id.civ__connectivity_indicator);
        connectivityIndicatorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIndicator();
            }
        });
        connectivityIndicatorViewForeground = ViewUtils.getView(mainLayout, R.id.iv__connectivity_foreground);
        connectivityIndicatorView.setOnExpandListener(this);
        return mainLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
        getStoreFactory().getNetworkStore().addNetworkControllerObserver(this);
    }

    @Override
    public void onStop() {
        getStoreFactory().getNetworkStore().removeNetworkControllerObserver(this);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        connectivityIndicatorView = null;
        super.onDestroyView();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //  Notifications
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onConnectivityChange(boolean hasInternet) {
        if (hasInternet) {
            hideIndicator();
        } else {
            showIndicator();
        }
    }

    private void showIndicator() {
        connectivityIndicatorView.show();
    }

    private void hideIndicator() {
        connectivityIndicatorView.hide();
    }

    @Override
    public void onNetworkAccessFailed() {
        if (!getStoreFactory().getNetworkStore().hasInternetConnection()) {
            showIndicator();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //  OnExpandListener
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onExpandBegin(long animationDuration) {
        connectivityIndicatorViewForeground.animate()
                                          .alpha(0)
                                          .setDuration(animationDuration)
                                          .setInterpolator(new Quart.EaseIn())
                                          .start();
    }

    @Override
    public void onCollapseBegin(long animationDuration) {
        connectivityIndicatorViewForeground.animate()
                                          .alpha(1)
                                          .setDuration(animationDuration)
                                          .setInterpolator(new Quart.EaseOut())
                                          .start();
    }

    @Override
    public void onHideBegin(long animationDuration) {
        connectivityIndicatorViewForeground.animate()
                                          .alpha(0)
                                          .setDuration(animationDuration)
                                          .setInterpolator(new Quart.EaseIn())
                                          .start();
    }

    public interface Container {
    }
}
