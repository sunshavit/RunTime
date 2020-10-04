package com.example.runtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import static java.security.AccessController.getContext;


public class MainActivity extends AppCompatActivity implements MessagesFragment.OnClickOnMessages, DataBaseClass.OnChangeUserListener, HomeFragment.CreateNewEventListener, BottomNavBarFragment.OnNavigationListener, WelcomeFragment.OnRegisterClick, DataBaseClass.OnUserCreateListener
        ,SignUp3Fragment.OnSignUpLastListener, RegisterClass.SignUpStatusListener, DataBaseClass.OnUserPreferenceCreateListener,
        RegisterClass.SignInStatusListener,DataBaseClass.OnUserListsListener, HomeFragment.findPeopleListener, CreateEventFragment.OnMapListener,
        MapFragment.OnCreateEventListener, /*FindPeopleFragment.OnStrangerCellClickListener,*/ HomeFragment.findEventsListener, CreateEventFragment.OnBackFromCreateEventListener{

    // where to do the user authentication
    // local time and local date require sdk 26
//    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
//    FirebaseAuth.AuthStateListener authStateListener;
//    String userName;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    final String WELCOMEFRAGMENTTAG="welcomefregmenttag";
    final String SIGNUP1TAG="signup1tag";
    final String SIGNUP2TAG="signup2tag";
    final String SIGNUP3TAG="signup3tag";
    private static final String MESSAGES_TAG = "messagestag";
    final String HOME_TAG="homeTag";
    final String CREATEEVENT_TAG="eventtag";

    final String MAP_TAG="map_tag";

    final String TOOLBAR_TAG="toolbartag";

    final String FIND_PEOPLE = "findPeople";
    final String FIND_EVENTS = "findEvents";
    final String STRANGER_FRAGMENT = "strangerFragment";

    final String NAV_TAG = "nav";
    final String PROFiLE_TAG = "profiletag";


    private boolean isFirstFragment=true;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase firebaseDatabase ;
    private boolean isPreferencesCreated;
    private boolean isUserListsCreated;
    private HomeFragment homeFragment;
    private UserInstance userInstance;
    private DrawerLayout drawerLayout;

    CreateEventFragment createEventFragment;
    RelativeLayout toolbarLayout;
    String isNull;




    // private FirebaseStorage firebaseStorage;
    RegisterClass registerClass;
    DataBaseClass dataBaseClass;
    Fragment toolBarFragment;
    Fragment navigationFragment;

    SharedPreferences sp;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment();
        fragmentManager = getSupportFragmentManager();

        sp = getSharedPreferences("details", MODE_PRIVATE);

        userInstance = UserInstance.getInstance();
        registerClass = RegisterClass.getInstance();
        dataBaseClass = DataBaseClass.getInstance();
        registerClass.setSignUpListener(this);
        dataBaseClass.setCallBackCreate(this);
        dataBaseClass.setCallBackPreferenceCreate(this);
        registerClass.setSignInListener(this);
        dataBaseClass.setCallBackUserLists(this);
        homeFragment.setFindPeopleCallback(this);
        homeFragment.setFindEventsCallback(this);
        dataBaseClass.setOnChangeUserListener(this);


        Log.d("bug", "onCreate");
        Log.d("bug", String.valueOf(sp.getBoolean("is_new_launch",true)));



        //registerClass.stateListener();
       createEventFragment = CreateEventFragment.getInstance(false);


        toolbarLayout = findViewById(R.id.toolbarLayout);



        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


                final FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null) { //sign up or sign in
                    Log.d("bug", "first condition");



                    ValueEventListener listener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild("gender")){


                                if (!sp.getBoolean("isChangingConfigurations", false)){
                                    Log.d("home", "snapshot");
                                    fragmentManager.beginTransaction().replace(R.id.rootLayout,homeFragment,HOME_TAG).commit();
                                    //fragmentTransaction.commit();
                                    Log.d("bug", "inside value event listener");

                                    fragmentManager.beginTransaction().replace(R.id.toolbarLayout,new ToolBarFragment(),TOOLBAR_TAG).commit();

                                    fragmentManager.beginTransaction().replace(R.id.layoutBottomNavgtionBar,new BottomNavBarFragment(),NAV_TAG).commit();

                                }



                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    };



                    dataBaseClass.isUserExists(registerClass.getUserId(), listener);
                    //UserInstance userInstance = UserInstance.getInstance();
                    /*if (userInstance.getUser().getGender() != null){


                    }*/
                    getLocationUpdates();

                    FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (task.isSuccessful()){
                                String token = task.getResult().getToken();
                                dataBaseClass.saveUserToken(token);
                            }
                        }
                    });



                    //dataBaseClass.updateActive(true);
                    //Toast.makeText(MainActivity.this,user.getUid(),Toast.LENGTH_LONG).show();
                }
                else {
                    fragmentManager.beginTransaction().replace(R.id.rootLayout,new WelcomeFragment(),WELCOMEFRAGMENTTAG).commit();
                    toolBarFragment =getSupportFragmentManager().findFragmentByTag(TOOLBAR_TAG);
                    if(toolBarFragment!=null) {
                        fragmentManager.beginTransaction().remove(toolBarFragment).commit();
                        fragmentManager.beginTransaction().remove(getSupportFragmentManager().findFragmentByTag(NAV_TAG)).commit();
                    }
                }

            }
        };

        registerClass.addStateListener(authStateListener);

        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.NavigationSide);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.signOutSidebar:
                        registerClass.signOut();
                        break;
                    case R.id.editProfileSidebar:
                        fragmentManager.beginTransaction().replace(R.id.rootLayout, new EditProfileFragment(), "editProfile").commit();
                        break;
                }
                return false;
            }
        });


    }

    @Override
    public void onCreateNewEvent(boolean isNew) {
        createEventFragment = null;
        //createEventFragment = CreateEventFragment.getInstance(true);


        //createEventFragment = CreateEventFragment.getInstance();

        createEventFragment = CreateEventFragment.getInstance(true);
        fragmentManager.beginTransaction().replace(R.id.rootLayout,createEventFragment ,CREATEEVENT_TAG).addToBackStack(null).commit();
        //to remove toolbar and navigation bar.
        /*navigationFragment =getSupportFragmentManager().findFragmentByTag(NAV_TAG);
        if(navigationFragment!=null) {
            toolbarLayout.setVisibility(View.GONE);
            fragmentManager.beginTransaction().remove(getSupportFragmentManager().findFragmentByTag(NAV_TAG)).commit();
        }*/
    }

    @Override
    public void onSignInClick() {
        fragmentManager.beginTransaction().replace(R.id.rootLayout,new SignInFragment(),SIGNUP1TAG).commit();
    }

    @Override
    public void onSignUpClick() {
        fragmentManager.beginTransaction().replace(R.id.rootLayout,new SignUp1Fragment(),SIGNUP1TAG).commit();
    }



    @Override
    public void onSignUpLast() {
        Toast.makeText(MainActivity.this,"sign up successful",Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        //registerClass.addStateListener(authStateListener);

    }

    @Override
    public void onClickMessages(User user) {
        MessagesFragment2 messagesFragment2 = MessagesFragment2.newInstance(user);
        fragmentManager.beginTransaction().replace(R.id.rootLayout,messagesFragment2,SIGNUP1TAG).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean("isChangingConfigurations",false);
        editor.commit();
       // Toast.makeText(this, "welcome" , Toast.LENGTH_SHORT).show();
        Log.d("bug", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isChangingConfigurations()){
            SharedPreferences.Editor editor=sp.edit();
            editor.putBoolean("isChangingConfigurations",true);
            editor.commit();
            //registerClass.removeStateListener(authStateListener);
        }
        Log.d("bug", "onDestroy");

       registerClass.removeStateListener(authStateListener);
    }

    @Override
    public void onSuccessSignUp(String userId) {
        fragmentManager.beginTransaction().replace(R.id.rootLayout, new SignUp2Fragment(), SIGNUP2TAG).commit();
        Toast.makeText(this, "welcome" + userId, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSuccessUserLists() {
        isUserListsCreated=true;
        moveToHomeFragment();
    }

    @Override
    public void onFailedUserLists() {
        isUserListsCreated=false;
        moveToHomeFragment();

    }

    @Override
    public void onNavChange(String page) {
        switch (page){
            case "home":
                Toast.makeText(this,"home", Toast.LENGTH_LONG).show();
                fragmentManager.beginTransaction().replace(R.id.rootLayout,homeFragment,HOME_TAG).commit();
                break;
            case "group":
                Toast.makeText(this,"group", Toast.LENGTH_LONG).show();
                fragmentManager.beginTransaction().replace(R.id.rootLayout,new FriendsFragment(),"friends").commit();
                break;
            case "location":
                Toast.makeText(this,"loction", Toast.LENGTH_LONG).show();
                fragmentManager.beginTransaction().replace(R.id.rootLayout,new EventsFragment(),"events").commit();
                break;
            case "profile":
                Toast.makeText(this,"profile", Toast.LENGTH_LONG).show();
                fragmentManager.beginTransaction().replace(R.id.rootLayout,new ProfileFragment(),PROFiLE_TAG).commit();
                break;
            case "message":
                Toast.makeText(this,"message", Toast.LENGTH_LONG).show();
                fragmentManager.beginTransaction().replace(R.id.rootLayout,new MessagesFragment(),MESSAGES_TAG).commit();
                break;
        }
    }
    //    private void getLocationUpdates() {
//        CurrentLocationListener.getInstance(getApplicationContext()).observe(this, new Observer<Location>() {
//            @Override
//            public void onChanged(@Nullable Location location) {
//                if (location != null) {
//                    Log.d(MainActivity.class.getSimpleName(),
//                            "Location Changed " + location.getLatitude() + " : " + location.getLongitude());
//                    Toast.makeText(MainActivity.this, "Location Changed", Toast.LENGTH_SHORT).show();
//                    builder.append(location.getLatitude()).append(" : ").append(location.getLongitude()).append("\n");
//                    textView.setText(builder.toString());
//                }
//            }
//        });
//    }

    @Override
    public void onFailedSignUp(String problem) {
        Toast.makeText(this, problem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessCreate() {

        fragmentManager.beginTransaction().replace(R.id.rootLayout,new SignUp3Fragment(),SIGNUP3TAG).commit();
    }

    @Override
    public void onFailedCreate(String problem) {
        Toast.makeText(this, problem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessPreferenceCreate() {
        Toast.makeText(MainActivity.this,"hi",Toast.LENGTH_LONG).show();
        isPreferencesCreated=true;
        //getLocationUpdates();
        //fragmentManager.beginTransaction().replace(R.id.rootLayout,new HomeFragment(),HOME_TAG).commit();
    }

    @Override
    public void onFailedPreferenceCreate() {
        isPreferencesCreated=false;

    }

    @Override
    public void onSuccessSignIn(String userId) {

        fragmentManager.beginTransaction().replace(R.id.toolbarLayout,new ToolBarFragment(),TOOLBAR_TAG).commit();

        fragmentManager.beginTransaction().replace(R.id.rootLayout,homeFragment,HOME_TAG).commit();

        fragmentManager.beginTransaction().replace(R.id.layoutBottomNavgtionBar,new BottomNavBarFragment(),NAV_TAG).commit();

        Toast.makeText(MainActivity.this,"sign up successful",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onFailedSignIn(String problem) {
        Toast.makeText(MainActivity.this,problem,Toast.LENGTH_LONG).show();
    }

    public void getLocationUpdates(){
        CurrentLocationListener.getInstance(getApplicationContext()).observe(this, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                if(location!=null){
                    //Toast.makeText(MainActivity.this,location.toString(),Toast.LENGTH_LONG).show();
                    double longitude=location.getLongitude();
                    double latitude=location.getLatitude();
                    userInstance.getUser().setLongitude(longitude);
                    userInstance.getUser().setLatitude(latitude);
                    dataBaseClass.updateLocation(longitude,latitude);

                }
                else
                    Toast.makeText(MainActivity.this,"no location",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void moveToHomeFragment(){
        if (isPreferencesCreated && isUserListsCreated){

            fragmentManager.beginTransaction().replace(R.id.rootLayout,homeFragment,HOME_TAG).commit();

            fragmentManager.beginTransaction().replace(R.id.toolbarLayout,new ToolBarFragment(),TOOLBAR_TAG).commit();
            fragmentManager.beginTransaction().replace(R.id.layoutBottomNavgtionBar,new BottomNavBarFragment(),"nav").commit();

        }
        else
            Toast.makeText(MainActivity.this,"failed",Toast.LENGTH_LONG).show();
    }


    @Override
    public void onFindPeopleClicked() {
        fragmentManager.beginTransaction().replace(R.id.rootLayout, new FindPeopleFragment(), FIND_PEOPLE).addToBackStack(null).commit();
    }

    @Override
    public void onFindEventsClicked() {
        fragmentManager.beginTransaction().replace(R.id.rootLayout, new /**/FindEventsFragment(), FIND_EVENTS).addToBackStack(null).commit();
    }

    //when a stranger at findPeopleFragment recycler is clicked

   /* @Override
    public void onStrangerCellClicked(String strangerId, boolean isRequested) {
        StrangerFragment strangerFragment = StrangerFragment.newInstance(strangerId, isRequested);
        fragmentManager.beginTransaction().replace(R.id.rootLayout, strangerFragment, STRANGER_FRAGMENT).addToBackStack(null).commit();
    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            drawerLayout.openDrawer(GravityCompat.START);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapOkClick() {
        fragmentManager.beginTransaction().replace(R.id.rootLayout, new MapFragment(), MAP_TAG).addToBackStack(null).commit();
    }

    @Override
    public void onCreateEventFromMap(boolean isNew) {

        for(int i=1; i<getSupportFragmentManager().getBackStackEntryCount() ; i++){
            getSupportFragmentManager().popBackStack();
        }
        fragmentManager.beginTransaction().replace(R.id.rootLayout, CreateEventFragment.getInstance(false), CREATEEVENT_TAG).addToBackStack(null).commit();
            /*for(int i=1; i<getSupportFragmentManager().getBackStackEntryCount() ; i++){
                getSupportFragmentManager().popBackStack();
            }*/
    }

    @Override

    public void toHomeFromCreateEvent() {
        //fragmentManager.beginTransaction().replace(R.id.rootLayout, new HomeFragment(), HOME_TAG).commit();}
        fragmentManager.beginTransaction().replace(R.id.rootLayout, homeFragment, HOME_TAG).commit();}

    public void onChangeUserSuccess() {
        fragmentManager.beginTransaction().replace(R.id.rootLayout,homeFragment,HOME_TAG).commit();
    }

    @Override
    public void onChangeUserFailed() {
        Toast.makeText(MainActivity.this,"failed",Toast.LENGTH_LONG).show();

    }
}






