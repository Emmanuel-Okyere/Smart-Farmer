package com.example.smartpro;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class OverviewFragment extends Fragment {
    final String APP_ID = "c3e9d4b681e802f49281d9e0bc456c08";
    final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";

    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;
    ProgressBar progressBar;


    String Location_Provider = LocationManager.GPS_PROVIDER;

    TextView NameofCity, weatherState, Temperature,IrrigatiobCondition;
    ImageView mweatherIcon;

    RelativeLayout mCityFinder;

    LocationManager mLocationManager;
    LocationListener mLocationListner;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View myView= inflater.inflate(R.layout.fragment_overview, container,false);
        final EditText editText=myView.findViewById(R.id.searchCity);

        weatherState = myView.findViewById(R.id.weatherCondition);
        Temperature = myView.findViewById(R.id.temperature);
        mweatherIcon = myView.findViewById(R.id.weatherIcon);
        mCityFinder = myView.findViewById(R.id.cityFinder);
        NameofCity = myView.findViewById(R.id.cityName);
        IrrigatiobCondition = myView.findViewById(R.id.irrigationCondition);
        progressBar = myView.findViewById(R.id.progressBar3);

        progressBar.setVisibility(View.VISIBLE);
        getWeatherForCurrentLocation();

        editText.setOnEditorActionListener((v, actionId, event) -> {
            String newCity= editText.getText().toString();

            fetchCity(newCity);
            return false;
        });

        return myView;
    }


    public void fetchCity(String cite) {
        if(cite !=null)
        {
            System.out.println("I am getting location now here");
            getWeatherForNewCity(cite);
        }
        else
        {
            getWeatherForCurrentLocation();
        }


    }


    private void getWeatherForNewCity(String city)
    {
        progressBar.setVisibility(View.VISIBLE);
        RequestParams params=new RequestParams();
        params.put("q",city);
        params.put("appid",APP_ID);
        letsdoSomeNetworking(params);

    }




    private void getWeatherForCurrentLocation() {
        System.out.println("I am working in here");
        Toast.makeText(getContext(),"Getting current location's weather, please wait",Toast.LENGTH_LONG).show();

        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mLocationListner = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {


                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());
                System.out.println("I am latitudes and longitudes in here");

                RequestParams params =new RequestParams();
                params.put("lat" ,Latitude);
                params.put("lon",Longitude);
                params.put("appid",APP_ID);
                letsdoSomeNetworking(params);


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(requireContext(),"Unable to get Location",Toast.LENGTH_SHORT).show();
                //not able to get location
            }
        };


        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }

        mLocationManager.requestLocationUpdates(Location_Provider, MIN_TIME, MIN_DISTANCE, mLocationListner);

    }


    @SuppressWarnings("deprecation")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.println("I am getting location in here");


        if(requestCode==REQUEST_CODE)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                System.out.println("I am trying to...");
                Toast.makeText(requireContext(),"Location get Successful",Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            }
            else
            {
                System.out.println("User Denied the location get");
            }
        }


    }



    public void letsdoSomeNetworking(RequestParams params)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL,params,new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                weatherData weatherD=weatherData.fromJson(response);
                Toast.makeText(getContext(),"Data Get Success",Toast.LENGTH_SHORT).show();
                assert weatherD != null;
                sendToFirebase(weatherD);
                updateUI(weatherD);
                progressBar.setVisibility(View.GONE);


                // super.onSuccess(statusCode, headers, response);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(requireContext(),"Unable to fetch data, check connectivity or city entered",Toast.LENGTH_SHORT).show();

            }
        });



    }

    private void sendToFirebase(weatherData weather) {
        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference myRef1 = database1.getReference("weather");
        System.out.println("This is the weather for firebase: "+myRef1.get());
        myRef1.setValue(weather.getNewCondition());
    }

    private  void updateUI(weatherData weather){
        IrrigatiobCondition.setText(weather.getNewCondition());
        Temperature.setText(weather.getmTemperature());
        NameofCity.setText(weather.getMcity());
        weatherState.setText(weather.getmWeatherType());
        int resourceID=getResources().getIdentifier(weather.getMicon(),"drawable", requireActivity().getPackageName());
        mweatherIcon.setImageResource(resourceID);


    }

    @Override
    public void onPause() {
        super.onPause();
        if(mLocationManager!=null)
        {
            mLocationManager.removeUpdates(mLocationListner);
        }
    }


}
