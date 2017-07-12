package s.mahmud.com.mapdraw;

import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    FrameLayout fram_map;
    Boolean Is_MAP_Moveable = false; // to detect map is movable
    private boolean screenLeave = false;
    int source = 0;
    int destination = 1;
    private ArrayList<LatLng> val = new ArrayList<LatLng>();
    private ArrayList<HomeBean> mLatLongList = new ArrayList<HomeBean>();
    private double latitude;
    private double longitude;

    Button btn_free_hand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fram_map = (FrameLayout) findViewById(R.id.fram_map);
        btn_free_hand = (Button) findViewById(R.id.btn_free_hand);
        btn_free_hand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Is_MAP_Moveable = true;
                screenLeave = false;
                source = 0;
                destination = 1;
                mLatLongList.clear();
                val.clear();
                mMap.clear();
                btn_free_hand.setEnabled(false);
            }
        });


        fram_map.setOnTouchListener(new View.OnTouchListener() {
            @Override

            public boolean onTouch(View v, MotionEvent event) {

                if (Is_MAP_Moveable) {
                    float x = event.getX();
                    float y = event.getY();

                    int x_co = Math.round(x);
                    int y_co = Math.round(y);

//                Projection projection = mMap.getProjection();
                    Point x_y_points = new Point(x_co, y_co);

                    LatLng latLng = mMap.getProjection().fromScreenLocation(x_y_points);
                    latitude = latLng.latitude;

                    longitude = latLng.longitude;
                    HomeBean bean = new HomeBean();
                    bean.setPost_lat(String.valueOf(latitude));
                    bean.setPost_long(String.valueOf(longitude));
                    bean.setSource_id("3");

                    mLatLongList.add(bean);


                    System.out.println("LatLng : " + latitude + " : " + longitude);

                    LatLng point = new LatLng(latitude, longitude);

                    int eventaction = event.getAction();
                    switch (eventaction) {
                        case MotionEvent.ACTION_DOWN:
                            // finger touches the screen
                            screenLeave = false;
//                            System.out.println("ACTION_DOWN");

//                            val.add(new LatLng(latitude, longitude));

                        case MotionEvent.ACTION_MOVE:
                            // finger moves on the screen
//                            System.out.println("ACTION_MOVE");
                          /*  if (val.size()==3){
                                val.remove(1);
                            }*/

                            val.add(new LatLng(latitude, longitude));
                            screenLeave = false;
                            Draw_Map();

                        case MotionEvent.ACTION_UP:

//                            System.out.println("ACTION_UP");
                            if (!screenLeave) {
                                screenLeave = true;
                            } else {
                                System.out.println("ACTION_UP ELSE CAse");
                                Is_MAP_Moveable = false; // to detect map is movable

                                source = 0;
                                destination = 1;
                                draw_final_polygon();

                            }

                            // finger leaves the screen
//                            Is_MAP_Moveable = false; // to detect map is movable
//                            Draw_Map();

                            break;
                        default:
                            break;
                    }

                    if (Is_MAP_Moveable) {
                        Log.e("DRAW on MAP : ", "LatLng ArrayList Size : " + mLatLongList.size());
                        return true;

                    } else {
                        return false;
                    }


                } else {
                    return false;
                }


            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap mMap) {
        this.mMap = mMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        this.mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        this.mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        this.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(sydney.latitude, sydney.longitude), 16.0f));
    }

    private void draw_final_polygon() {

        val.add(val.get(0));

        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.addAll(val);
        polygonOptions.strokeColor(ContextCompat.getColor(MapsActivity.this, R.color.colorAccent));
        polygonOptions.strokeWidth(8);
        polygonOptions.fillColor(ContextCompat.getColor(MapsActivity.this, R.color.colormapfill));
        Polygon polygon = mMap.addPolygon(polygonOptions);

        btn_free_hand.setEnabled(true);

    }

    public void Draw_Map() {


//        specify latitude and longitude of both source and destination Polyline

        if (val.size() > 1) {
            mMap.addPolyline(new PolylineOptions().add(val.get(source), val.get(destination)).width(8).color(ContextCompat.getColor(MapsActivity.this, R.color.colorAccent)));
            source++;
            destination++;
        }


    }

}
