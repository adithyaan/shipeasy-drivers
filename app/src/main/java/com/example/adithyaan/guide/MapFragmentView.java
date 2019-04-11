

package com.example.adithyaan.guide;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.here.android.mpa.common.GeoBoundingBox;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.ViewObject;
import com.here.android.mpa.guidance.NavigationManager;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapGesture;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapProxyObject;
import com.here.android.mpa.mapping.MapRoute;
import com.here.android.mpa.mapping.MapTrafficLayer;
import com.here.android.mpa.mapping.TrafficEvent;
import com.here.android.mpa.mapping.TrafficEventObject;
import com.here.android.mpa.routing.CoreRouter;
import com.here.android.mpa.routing.Maneuver;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteResult;
import com.here.android.mpa.routing.RouteTta;
import com.here.android.mpa.routing.RouteWaypoint;
import com.here.android.mpa.routing.Router;
import com.here.android.mpa.routing.RoutingError;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MapFragmentView implements MapGesture.OnGestureListener{
    private MapFragment m_mapFragment;
    private Activity m_activity;
    private Button m_naviControlButton;
    private Map m_map;
    private NavigationManager m_navigationManager;
    private GeoBoundingBox m_geoBoundingBox;
    private Route m_route;
    private boolean m_foregroundServiceStarted;
    MapRoute mapRoute;
    EditText f,t;
    Map map;
    TextView instructionText;
    TextView estimatedTime;
    String final_name = "Source";

    Date currentTime = Calendar.getInstance().getTime();
    public MapFragmentView(Activity activity) {
        m_activity = activity;
        initMapFragment();
        initNaviControlButton();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(m_activity);
        alertDialogBuilder.setTitle("Navigation Suggestions");
        alertDialogBuilder.setMessage("Follow the places one by one to navigate quickly... ");
//        alertDialogBuilder.setMessage("1223456");
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog.Builder alertDialogBuild = new AlertDialog.Builder(m_activity);
                alertDialogBuild.setTitle("Navigation Suggestions");
                for (i = 0;i < Constants.Name.size();i++) {
                    final_name = final_name + " -> " + Constants.Name.get(i);
                }
                alertDialogBuild.setMessage(final_name);
                alertDialogBuild.setPositiveButton("Simulate", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        m_navigationManager = NavigationManager.getInstance();
//                        m_navigationManager.simulate(m_route,60);//Simualtion speed is set to 60 m/s
//                        m_map.setTilt(60);
//                        startForegroundService();
                    }});
                alertDialogBuild.show();
            }});
        alertDialogBuilder.show();
    }



    private void initMapFragment() {
        /* Locate the mapFragment UI element */
        Log.e("time",currentTime.toString());
        t=m_activity.findViewById(R.id.t);
        f=m_activity.findViewById(R.id.f);
        t.setText("Ghansoli");
        f.setText("Thane");
        m_mapFragment = (MapFragment) m_activity.getFragmentManager()
                .findFragmentById(R.id.mapfragment);
            instructionText=m_activity.findViewById(R.id.instructiontext);
            estimatedTime=m_activity.findViewById(R.id.estimatedTime);
        // Set path of isolated disk cache
        String diskCacheRoot = Environment.getExternalStorageDirectory().getPath()
                + File.separator + ".isolated-here-maps";
        // Retrieve intent name from manifest
        String intentName = "";
        try {
            ApplicationInfo ai = m_activity.getPackageManager().getApplicationInfo(m_activity.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            intentName = bundle.getString("INTENT_NAME");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(this.getClass().toString(), "Failed to find intent name, NameNotFound: " + e.getMessage());
        }

        boolean success = com.here.android.mpa.common.MapSettings.setIsolatedDiskCacheRootPath(diskCacheRoot, intentName);
        if (!success) {

        }
        else
            {
            if (m_mapFragment != null) {
                m_mapFragment.init(new OnEngineInitListener() {
                    @Override
                    public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {

                        if (error == Error.NONE) {
                            m_map = m_mapFragment.getMap();
                            m_map.setCenter(new GeoCoordinate(13.0827, 80.2707),
                                    Map.Animation.NONE);
                            //Put this call in Map.onTransformListener if the animation(Linear/Bow)
                            //is used in setCenter()
                            m_map.setZoomLevel(13.2);
                            m_map.setTrafficInfoVisible(true);
                        /*
                         * Get the NavigationManager instance.It is responsible for providing voice
                         * and visual instructions while driving and walking
                         */
                            m_navigationManager = NavigationManager.getInstance();
                            MapTrafficLayer traffic = m_map.getMapTrafficLayer();
                            traffic.setDisplayFilter(TrafficEvent.Severity.VERY_HIGH);
                             NavigationManager.NewInstructionEventListener instructListener
                                    = new NavigationManager.NewInstructionEventListener() {

                                @Override
                                public void onNewInstructionEvent() {
                                    // Interpret and present the Maneuver object as it contains
                                    // turn by turn navigation instructions for the user.
                                    m_navigationManager.getNextManeuver();
                                }
                            };

                             NavigationManager.PositionListener positionListener
                                    = new NavigationManager.PositionListener() {

                                @Override
                                public void onPositionUpdated(GeoPosition loc) {

                                    loc.getCoordinate();
                                    loc.getHeading();
                                    loc.getSpeed();

                                    // also remaining time and distance can be
                                    // fetched from navigation manager
                                    m_navigationManager.getTta(Route.TrafficPenaltyMode.DISABLED, true);
                                    m_navigationManager.getDestinationDistance();
                                }
                            };
                            m_navigationManager.addNewInstructionEventListener(
                                    new WeakReference<NavigationManager.NewInstructionEventListener>(instructListener));
                            m_navigationManager.addPositionListener(
                                    new WeakReference<NavigationManager.PositionListener>(positionListener));

                        } else {
                            Toast.makeText(m_activity,
                                    "ERROR: Cannot initialize Map with error " + error,
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        }
    }

    private void createRoute() {
        /* Initialize a CoreRouter */
        CoreRouter coreRouter = new CoreRouter();

        /* Initialize a RoutePlan */
        RoutePlan routePlan = new RoutePlan();


        RouteOptions routeOptions = new RouteOptions();
        routeOptions.setTransportMode(RouteOptions.TransportMode.CAR);
        /* Disable highway in this route. */
        routeOptions.setHighwaysAllowed(false);
        /* Calculate the shortest route available. */
        routeOptions.setRouteType(RouteOptions.Type.SHORTEST);
        /* Calculate 1 route. */
        routeOptions.setRouteCount(1);
        /* Finally set the route option */
        routePlan.setRouteOptions(routeOptions);

                /* Define waypoints for the route */
        /* START: 4350 Still Creek Dr */
        RouteWaypoint startPoint = new RouteWaypoint(new GeoCoordinate(Constants.Latitude.get(Constants.Latitude.size() - 1),Constants.Longitude.get(Constants.Longitude.size() - 1)));
        /* END: Langley BC */
        RouteWaypoint destination = new RouteWaypoint(new GeoCoordinate(Constants.Latitude.get(0), Constants.Longitude.get(0)));

        /* Add both waypoints to the route plan */
        routePlan.addWaypoint(startPoint);
        routePlan.addWaypoint(destination);
//        routePlan.addWaypoint(new RouteWaypoint(new GeoCoordinate(19.158854, 72.999298)));
//        routePlan.addWaypoint(new RouteWaypoint(new GeoCoordinate(19.186854, 72.975447)));

        com.here.android.mpa.common.Image image = new com.here.android.mpa.common.Image();
        try {
            image.setImageResource(R.drawable.markera);

        } catch (IOException e) {
            e.printStackTrace();
        }
//        MapMarker mapMarker = new MapMarker(new GeoCoordinate(19.138403, 73.001384), image1);
//        mapMarker.setDescription("1");
//        mapMarker.setTitle("1");
//        MapMarker mapMarker1 = new MapMarker(new GeoCoordinate(19.158854, 72.999298), image1);
//        MapMarker mapMarker2 = new MapMarker(new GeoCoordinate(19.186854, 72.975447), image1);
//        routePlan.addWaypoint(new RouteWaypoint(new GeoCoordinate(19.119572, 73.011394)));
//        mapMarker.setCoordinate(new GeoCoordinate(19.119572, 73.011394));
//        mapMarker.setTitle("Hello");
//        m_map.addMapObject(mapMarker);
//        m_map.addMapObject(mapMarker1);
//        m_map.addMapObject(mapMarker2);

        for (int i = 0;i < Constants.Name.size();i++) {
            m_map.addMapObject(new MapMarker(new GeoCoordinate(Constants.Latitude.get(i), Constants.Longitude.get(i)), image));
        }




        /* Trigger the route calculation,results will be called back via the listener */
        coreRouter.calculateRoute(routePlan,
                new Router.Listener<List<RouteResult>, RoutingError>() {

                    @Override
                    public void onProgress(int i) {
                        /* The calculation progress can be retrieved in this callback. */
                    }

                    @Override
                    public void onCalculateRouteFinished(List<RouteResult> routeResults,
                                                         RoutingError routingError) {
                        /* Calculation is done.Let's handle the result */
                        if (routingError == RoutingError.NONE) {
                            if (routeResults.get(0).getRoute() != null) {

                                m_route = routeResults.get(0).getRoute();
                                /* Create a MapRoute so that it can be placed on the map */
                                MapRoute mapRoute = new MapRoute(routeResults.get(0).getRoute());

                                /* Show the maneuver number on top of the route */
                                mapRoute.setManeuverNumberVisible(true);

                                /* Add the MapRoute to the map */
                                m_map.addMapObject(mapRoute);

                                /*
                                 * We may also want to make sure the map view is orientated properly
                                 * so the entire route can be easily seen.
                                 */
                                m_geoBoundingBox = routeResults.get(0).getRoute().getBoundingBox();
                                m_map.zoomTo(m_geoBoundingBox, Map.Animation.NONE,
                                        Map.MOVE_PRESERVE_ORIENTATION);


                                Log.e("SublegCount",m_route.getSublegCount()+"");

                                mapRoute.setManeuverNumberVisible(true);
                                mapRoute.setTrafficEnabled(true);

                                RouteTta tt = m_route.getTta(Route.TrafficPenaltyMode.OPTIMAL,m_route.getSublegCount()>=0?m_route.getSublegCount()-1:0);
                                long timeInSeconds = tt.getDuration();
                                long timeInMinutes = timeInSeconds/60;
                                Toast.makeText(m_activity, "Time"+timeInMinutes, Toast.LENGTH_SHORT).show();
                                estimatedTime.setText("Estimated Time: "+timeInMinutes+"mins");
                                startNavigation();

                            } else {
                                Toast.makeText(m_activity,
                                        "Error:route results returned is not valid",
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(m_activity,
                                    "Error:route calculation returned error code: " + routingError,
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private void initNaviControlButton() {
        m_naviControlButton = (Button) m_activity.findViewById(R.id.naviCtrlButton);
        m_naviControlButton.setText("start");
        m_naviControlButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                if (m_route == null) {
                    createRoute();
                } else {
                    m_navigationManager.stop();
                    /*
                     * Restore the map orientation to show entire route on screen
                     */
                    m_map.zoomTo(m_geoBoundingBox, Map.Animation.NONE, 0f);
                    m_naviControlButton.setText("test");
                    m_route = null;
                }
            }
        });
    }


    private void startForegroundService() {
        if (!m_foregroundServiceStarted) {
            m_foregroundServiceStarted = true;
            Intent startIntent = new Intent(m_activity, ForegroundService.class);
            startIntent.setAction(ForegroundService.START_ACTION);
            m_activity.getApplicationContext().startService(startIntent);
        }
    }

    private void stopForegroundService() {
        if (m_foregroundServiceStarted) {
            m_foregroundServiceStarted = false;
            Intent stopIntent = new Intent(m_activity, ForegroundService.class);
            stopIntent.setAction(ForegroundService.STOP_ACTION);
            m_activity.getApplicationContext().startService(stopIntent);
        }
    }

    private void startNavigation() {
        m_naviControlButton.setText("start" +
                "");
        /* Display the position indicator on map */
        m_map.getPositionIndicator().setVisible(true);
        /* Configure Navigation manager to launch navigation on current map */
        m_navigationManager.setMap(m_map);

        /*
         * Start the turn-by-turn navigation.Please note if the transport mode of the passed-in
         * route is pedestrian, the NavigationManager automatically triggers the guidance which is
         * suitable for walking. Simulation and tracking modes can also be launched at this moment
         * by calling either simulate() or startTracking()
         */

        /* Choose navigation modes between real time navigation and simulation */
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(m_activity);
        alertDialogBuilder.setTitle("Navigation");
        alertDialogBuilder.setMessage("Choose Mode");
        alertDialogBuilder.setNegativeButton("Navigation",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i) {
                m_navigationManager.startNavigation(m_route);
                m_map.setTilt(60);
                startForegroundService();
            };
        });
        alertDialogBuilder.setPositiveButton("Simulation",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i) {
                m_navigationManager.simulate(m_route,60);//Simualtion speed is set to 60 m/s
                m_map.setTilt(60);
                startForegroundService();
            };
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        /*
         * Set the map update mode to ROADVIEW.This will enable the automatic map movement based on
         * the current location.If user gestures are expected during the navigation, it's
         * recommended to set the map update mode to NONE first. Other supported update mode can be
         * found in HERE Android SDK API doc
         */
        m_navigationManager.setMapUpdateMode(NavigationManager.MapUpdateMode.ROADVIEW);


        addNavigationListeners();
    }

    NavigationManager.NewInstructionEventListener instlistener = new NavigationManager.NewInstructionEventListener() {
        @Override
        public void onNewInstructionEvent() {
            super.onNewInstructionEvent();
            Maneuver maneuver= m_navigationManager.getNextManeuver();
            Maneuver.Turn turn = maneuver.getTurn();
            String turnName=turn.name();
            int distance = maneuver.getDistanceFromPreviousManeuver();
            String nextRoadName = maneuver.getNextRoadName();
            instructionText.setText("Take a "+turnName+"to the "+nextRoadName+" in "+distance+"mts");
            Log.e("ins",nextRoadName);
            instructionText.setText("Will take a "+turnName+"to the "+nextRoadName+" in "+distance+"mts");
            ImageView img=m_activity.findViewById(R.id.imgview);
            if (turnName.toLowerCase().contains("right"))
            img.setImageResource(R.drawable.rightturn);
            else
                img.setImageResource(R.drawable.leftturn);


        }
    };

    private void addNavigationListeners() {

        /*
         * Register a NavigationManagerEventListener to monitor the status change on
         * NavigationManager
         */
        m_navigationManager.addNewInstructionEventListener(new WeakReference<NavigationManager.NewInstructionEventListener>(instlistener));

        m_navigationManager.addNavigationManagerEventListener(
                new WeakReference<NavigationManager.NavigationManagerEventListener>(
                        m_navigationManagerEventListener));

        /* Register a PositionListener to monitor the position updates */
        m_navigationManager.addPositionListener(
                new WeakReference<NavigationManager.PositionListener>(m_positionListener));
    }

    private NavigationManager.PositionListener m_positionListener = new NavigationManager.PositionListener() {
        @Override
        public void onPositionUpdated(GeoPosition geoPosition) {
            /* Current position information can be retrieved in this callback */
        }
    };

    private NavigationManager.NavigationManagerEventListener m_navigationManagerEventListener = new NavigationManager.NavigationManagerEventListener() {
        @Override
        public void onRunningStateChanged() {
            Toast.makeText(m_activity, "Running state changed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNavigationModeChanged() {
            Toast.makeText(m_activity, "Navigation mode changed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEnded(NavigationManager.NavigationMode navigationMode) {
            Toast.makeText(m_activity, navigationMode + " was ended", Toast.LENGTH_SHORT).show();
            stopForegroundService();
        }

        @Override
        public void onMapUpdateModeChanged(NavigationManager.MapUpdateMode mapUpdateMode) {
            Toast.makeText(m_activity, "Map update mode is changed to " + mapUpdateMode,
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRouteUpdated(Route route) {
            Toast.makeText(m_activity, "Route updated", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCountryInfo(String s, String s1) {
            Toast.makeText(m_activity, "Country info updated from " + s + " to " + s1,
                    Toast.LENGTH_SHORT).show();
        }
    };

    public void onDestroy() {
        /* Stop the navigation when app is destroyed */
        if (m_navigationManager != null) {
            stopForegroundService();
            m_navigationManager.stop();
        }
    }

    @Override
    public void onPanStart() {

    }

    @Override
    public void onPanEnd() {

    }

    @Override
    public void onMultiFingerManipulationStart() {

    }

    @Override
    public void onMultiFingerManipulationEnd() {

    }

    @Override
    public boolean onMapObjectsSelected(List<ViewObject> list) {
//        return false;
        for (ViewObject obj : list) {
            if (obj.getBaseType() == ViewObject.Type.PROXY_OBJECT) {
                MapProxyObject proxyObj = (MapProxyObject) obj;
                if (proxyObj.getType() == MapProxyObject.Type.TRAFFIC_EVENT) {
                    TrafficEventObject trafficEventObj =
                            (TrafficEventObject) proxyObj;
                    TrafficEvent trafficEvent =
                            trafficEventObj.getTrafficEvent();
//                    Toast.makeText((), trafficEvent.getEventText(),
//                            Toast.LENGTH_LONG).show();
                }
            }
        }

        return true;
    }

    @Override
    public boolean onTapEvent(PointF pointF) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(PointF pointF) {
        return false;
    }

    @Override
    public void onPinchLocked() {

    }

    @Override
    public boolean onPinchZoomEvent(float v, PointF pointF) {
        return false;
    }

    @Override
    public void onRotateLocked() {

    }

    @Override
    public boolean onRotateEvent(float v) {
        return false;
    }

    @Override
    public boolean onTiltEvent(float v) {
        return false;
    }

    @Override
    public boolean onLongPressEvent(PointF pointF) {
        return false;
    }

    @Override
    public void onLongPressRelease() {

    }

    @Override
    public boolean onTwoFingerTapEvent(PointF pointF) {
        return false;
    }
}

//
//    private class RouteInterface implements CoreRouter.Listener {
//        MapRoute mapRoute;
//        Map map;
//        @Override
//        public void onProgress(int i) {
//
//        }
//
//        @Override
//        public void onCalculateRouteFinished(List<RouteResult> list, RoutingError routingError) {
//
//            if (routingError == RoutingError.NONE) {
//                Log.e("Routes",list.size()+"");
//                for (RouteResult rr:list)
//                    Log.e("RouteResult",rr.getRoute().getTta(Route.TrafficPenaltyMode.OPTIMAL,rr.getRoute().getSublegCount()>=0?rr.getRoute().getSublegCount()-1:0).getDuration()+"");
//                Route r = list.get(0).getRoute();
//                Log.e("SublegCount",r.getSublegCount()+"");
//                mapRoute = new MapRoute(r);
//                mapRoute.setManeuverNumberVisible(true);
//                mapRoute.setTrafficEnabled(true);
//                map.addMapObject(mapRoute);
//                RouteTta tt = r.getTta(Route.TrafficPenaltyMode.OPTIMAL,r.getSublegCount()>=0?r.getSublegCount()-1:0);
//                long timeInSeconds = tt.getDuration();
//                long timeInMinutes = timeInSeconds/60;
////                Toast.makeText(m_activity, "Time"+timeInMinutes, Toast.LENGTH_SHORT).show();
////                estimatedTime.setText("Estimated Time: "+timeInMinutes+"mins");
//                final NavigationManager manager = NavigationManager.getInstance();
//                map.getPositionIndicator().setVisible(true);
//                manager.setMap(map);
//                GeoBoundingBox gbb =r.getBoundingBox();
//                map.zoomTo(gbb, Map.Animation.NONE, Map.MOVE_PRESERVE_ORIENTATION);
//                manager.startNavigation(r);
//                map.setTilt(90);
////                startForegroundService();
////                instructionText.setVisibility(View.VISIBLE);
////                instructionText.bringToFront();
////                instructionText.invalidate();
//                manager.setMapUpdateMode(NavigationManager.MapUpdateMode.ROADVIEW);
//                manager.addNewInstructionEventListener(new WeakReference<NavigationManager.NewInstructionEventListener>(new NavigationManager.NewInstructionEventListener() {
//                    @Override
//                    public void onNewInstructionEvent() {
//                        Maneuver maneuver= manager.getNextManeuver();
//                        Maneuver.Turn turn = maneuver.getTurn();
//                        String turnName=turn.name();
//                        int distance = maneuver.getDistanceFromPreviousManeuver();
//                        String nextRoadName = maneuver.getNextRoadName();
//                            instructionText.setText("Take a "+turnName+"to the "+nextRoadName+" in "+distance+"mts");
//                        Log.e("ins",nextRoadName);
//                        instructionText.setText("Will take a "+turnName+"to the "+nextRoadName+" in "+distance+"mts");
//                    }
//                }));
//                manager.addNavigationManagerEventListener(new WeakReference<NavigationManager.NavigationManagerEventListener>(new NavigationManager.NavigationManagerEventListener() {
//                    @Override
//                    public void onEnded(NavigationManager.NavigationMode navigationMode) {
//                        stopForegroundService();
//                        Toast.makeText(m_activity, "Navigation Ended!", Toast.LENGTH_SHORT).show();
//                        instructionText.setText("Destination Reached!");
//
//                    }
//                }));
//            }
//            else {
//                Toast.makeText(m_activity, "Retry Once"+routingError, Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//    public void checkLocationServices(){
//        LocationManager lm = (LocationManager)m_activity.getSystemService(Context.LOCATION_SERVICE);
//        boolean gps_enabled = false;
//        boolean network_enabled = false;
//
//        try {
//            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        } catch(Exception ex) {}
//
//        try {
//            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        } catch(Exception ex) {}
//
//        if(!gps_enabled && !network_enabled) {
//            // notify user
//            AlertDialog.Builder dialog = new AlertDialog.Builder(m_activity);
//            dialog.setMessage("internet not enabled");
//            dialog.setPositiveButton("settings", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                    // TODO Auto-generated method stub
//                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    //startActivity(myIntent);
//                    //get gps
//                }
//            });
//            dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                    // TODO Auto-generated method stub
//                    Toast.makeText(m_activity, "Enable Location Services", Toast.LENGTH_SHORT).show();
//                }
//            });
//            dialog.show();
//
//        }
//
//    }
//}
//
//
