package com.sjmtechs.park.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.sjmtechs.park.R;
import com.sjmtechs.park.utils.InternetConnection;

public class ParkLaterFragment extends Fragment implements LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "ParkLaterFragment";
    private GoogleMap mParkLaterGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
            Log.e("mGoogleApiClient", "onCreate mGoogleApiClient connect " + mGoogleApiClient.isConnecting());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_park_later, container, false);

        if (mGoogleApiClient == null) {
            try {
                mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                        .addApi(Places.GEO_DATA_API)
                        .addConnectionCallbacks(this)
                        .build();
                if (mGoogleApiClient == null) {
                    Log.e("mGoogleApiClient", "onCreate mGoogleApiClient null");
                } else {
                    Log.e("mGoogleApiClient", "onCreate mGoogleApiClient not null");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(InternetConnection.checkConnection(getActivity())){
            SupportMapFragment fm = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.park_later_map);


            fm.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mParkLaterGoogleMap = googleMap;
                    Log.e(TAG, "onMapReady: " + mParkLaterGoogleMap.isMyLocationEnabled());
                }
            });
        }

        return view;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
