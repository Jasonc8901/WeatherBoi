package com.example.jasonc.weatherboi;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Button tempChanger;
    private TextView currentTemp;
    Button locationChanger;
    Button mapView;
    TextView tempTypeView;
    TextView locationView;
    TextView alertView;
    TextView statusView;
    TextView upDate;
    TextView pressureView;
    TextView humidView;
    ImageView statusImg;
    ImageView boii;
    RelativeLayout bg;
    int tempValF;
    int tempValC;
    int tempHighF;
    int tempHighC;
    int tempLowF;
    int tempLowC;
    double longitude;
    double latitude;

    //sendMessage defines a function to execute the map activity and give it appropriate location details
    public void sendMessage(View view){
        Intent startMap = new Intent(this, mapActivity.class);  //defines an intent to the map activity
        Bundle bundle = new Bundle();      //defines a bundle to hold longitude and latitude information for the map activity
        bundle.putDouble("lat",latitude);
        bundle.putDouble("longi",longitude);
        startMap.putExtras(bundle);     //stores bundle data into intent
        startActivity(startMap);        //starts the map activity using the given intent
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeLocation("Corpus Christi");       //sets default location
        bg = findViewById(R.id.bg);

        //defines views to member variables
        locationChanger = findViewById(R.id.locationBtn);
        tempChanger = findViewById(R.id.temptype);
        mapView = findViewById(R.id.parkFinder);
        pressureView = findViewById(R.id.pressureView);
        boii = findViewById(R.id.dogImg);
        humidView = findViewById(R.id.humidView);
        statusImg = findViewById(R.id.statusImg);
        statusView = findViewById(R.id.weatherstatus);
        locationView = findViewById(R.id.locationView);
        currentTemp = findViewById(R.id.currentT);
        tempTypeView = findViewById(R.id.tempFormatView);
        upDate = findViewById(R.id.upDate);

        //The tempchanger button reverts the displayed temperature from fahrenheit to celcius or vice versa
        tempChanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tempChanger.getText()=="C")
                {
                    tempChanger.setText("F");
                    currentTemp.setText((int)tempValC + "");
                    tempTypeView.setText("C");
                }
                else{
                    tempChanger.setText("C");
                    currentTemp.setText((int)tempValF + "");
                    tempTypeView.setText("F");
                }
            }
        });

        //creates a dialog box containing an edittext where the user can change their location
        locationChanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder settingsAlert = new AlertDialog.Builder(
                        MainActivity.this);
                settingsAlert.setTitle("Enter your city name and state: ");

                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                settingsAlert.setView(input);   //adds the edittext to the dialog layout
                settingsAlert.setPositiveButton("Go", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!input.getText().toString().equals(""))      //ensures that an empty string is not entered
                        {
                            changeLocation(input.getText().toString());     //updates the location
                        }

                    }
                });
                settingsAlert.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                                //does nothing for cancel but remove dialog box
                    }
                });
                settingsAlert.show();
            }
        });



    }

    //responds to a change in city by altering values in the page
    public void changeLocation(String city)
    {
        //attempts to find latitude and longitude using a provided string and a Geocoder object
        Geocoder geocoder = new Geocoder(this, Locale.US);
        List<Address> addresses = null;
        Address address;
        try {
            addresses = geocoder.getFromLocationName(city, 1);
            address = addresses.get(0);
        } catch (IOException e) {
            Toast.makeText(MainActivity.this,latitude+"",Toast.LENGTH_LONG).show();
            return;
        }

        longitude = address.getLongitude();
        latitude = address.getLatitude();

        //Asynchronously calls to get information from the openWeather API
        Function.placeIdTask asyncTask =new Function.placeIdTask(new Function.AsyncResponse() {
            public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure, String weather_updatedOn, String weather_iconText, String sun_rise) {

                locationView.setText(weather_city);
                upDate.setText(weather_updatedOn + " GMT");
                humidView.setText(weather_humidity);
                pressureView.setText(weather_pressure);

                currentTemp.setText(weather_temperature);
                tempValF = 9*(Integer.parseInt(currentTemp.getText().toString()))/5 +32;//finds default farenheit temp

                //updates weather icons, background, and weather status text based on conditions
                switch(weather_description) {
                    case "LIGHT RAIN":
                        statusImg.setImageResource(R.drawable.sprinkle);
                        statusView.setText(weather_description);
                        boii.setImageResource(R.drawable.raindog);
                        bg.setBackgroundResource(R.drawable.partlycloudgradient);
                        break;
                    case "MODERATE RAIN":
                        statusImg.setImageResource(R.drawable.rain);
                        statusView.setText(weather_description);
                        boii.setImageResource(R.drawable.raindog);
                        bg.setBackgroundResource(R.drawable.cloudygradient);
                        break;
                    case "SKY IS CLEAR":
                        statusImg.setImageResource(R.drawable.sunny);
                        statusView.setText("CLEAR");
                        if(tempValF < 60)
                            boii.setImageResource(R.drawable.colddog);
                        else
                            boii.setImageResource(R.drawable.sundog);

                        bg.setBackgroundResource(R.drawable.sunnygradient);
                        break;
                    case "HEAVY INTENSITY RAIN":
                        statusImg.setImageResource(R.drawable.thunderstorm);
                        statusView.setText("THUNDERSTORM");
                        boii.setImageResource(R.drawable.raindog);
                        bg.setBackgroundResource(R.drawable.cloudygradient);
                        break;
                    case "FEW CLOUDS":
                        bg.setBackgroundResource(R.drawable.gradient);
                        statusImg.setImageResource(R.drawable.partlycloud);
                        statusView.setText("PARTLY CLOUDY");
                        //checks temperature for how boii wants to go out
                        if(tempValF < 60)
                            boii.setImageResource(R.drawable.colddog);
                        else
                            boii.setImageResource(R.drawable.defaultdog);
                        break;
                    case "SCATTERED CLOUDS":
                        bg.setBackgroundResource(R.drawable.gradient);
                        statusImg.setImageResource(R.drawable.partlycloud);
                        statusView.setText("PARTLY CLOUDY");
                        //checks temperature for how boii wants to go out
                        if(tempValF < 60)
                            boii.setImageResource(R.drawable.colddog);
                        else
                            boii.setImageResource(R.drawable.defaultdog);
                        break;
                    case "BROKEN CLOUDS":
                        bg.setBackgroundResource(R.drawable.partlycloudgradient);
                        statusImg.setImageResource(R.drawable.partlycloud);
                        statusView.setText("MOSTLY CLOUDY");
                        //checks temperature for how boii wants to go out
                        if(tempValF < 60)
                            boii.setImageResource(R.drawable.colddog);
                        else
                            boii.setImageResource(R.drawable.defaultdog);
                        break;
                    case "OVERCAST CLOUDS":
                        bg.setBackgroundResource(R.drawable.cloudygradient);
                        statusImg.setImageResource(R.drawable.cloudy);
                        boii.setImageResource(R.drawable.cloudydog);
                        statusView.setText("CLOUDY");
                        break;
                    case "SNOW":
                        bg.setBackgroundResource(R.drawable.cloudygradient);
                        statusImg.setImageResource(R.drawable.snow);
                        boii.setImageResource(R.drawable.colddog);
                        statusView.setText("SNOW");
                        break;
                    case "LIGHT SNOW":
                        bg.setBackgroundResource(R.drawable.cloudygradient);
                        statusImg.setImageResource(R.drawable.snow);
                        boii.setImageResource(R.drawable.colddog);
                        statusView.setText("LIGHT SNOW");
                        break;
                    case "MIST":
                        bg.setBackgroundResource(R.drawable.cloudygradient);
                        statusImg.setImageResource(R.drawable.wind);
                        boii.setImageResource(R.drawable.cloudydog);
                        statusView.setText("MIST");
                        break;
                    case "HAZE":
                        bg.setBackgroundResource(R.drawable.cloudygradient);
                        statusImg.setImageResource(R.drawable.wind);
                        boii.setImageResource(R.drawable.cloudydog);
                        statusView.setText("HAZE");
                        break;
                }
                //updates temperature to current location


                currentTemp.setText(tempValF + "");
                tempValC = (int) ((tempValF-32)/1.8);                              //stores default celcius temp
            }
        });
        asyncTask.execute(""+ latitude, ""+ longitude);

    }
}

