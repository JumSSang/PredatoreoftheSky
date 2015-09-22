package com.jjumsang.predatorsofthesky;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.jjumsang.predatorsofthesky.Game_NetWork.NetState;
import com.jjumsang.predatorsofthesky.GoogleAPI.BaseGameUtils;
import com.jjumsang.predatorsofthesky.immortal.AppManager;
import com.jjumsang.predatorsofthesky.immortal.DBManager;


public class MainActivity extends Activity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    EditText edit_password;
    EditText edit_id;
    Button button_default;
    boolean connect = true;




    private static final String TAG = "TrivialQuest";

    // Request code used to invoke sign in user interactions.
    private static final int RC_SIGN_IN = 9001;

    // Client used to interact with Google APIs.


    // Are we currently resolving a connection failure?
    private boolean mResolvingConnectionFailure = false;

    // Has the user clicked the sign-in button?
    private boolean mSignInClicked = false;

    // Set to true to automatically start the sign in flow when the Activity starts.
    // Set to false to require the user to click the button in order to sign in.
    private boolean mAutoStartSignInFlow = true;

    private static final int A_ACTIVITY = 0;
    SharedPreferences pref;

    private CheckBox m_GoogleCheckbox;
    private CheckBox m_myDB_CheckBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");

        super.onCreate(savedInstanceState);

        // Create the Google Api Client with access to Plus and Games


        DBManager.getInstance().connection = new NetConnect();
        DBManager.getInstance().connection.execute(null, null, null);



        setContentView(R.layout.activity_main);
        m_GoogleCheckbox=(CheckBox)findViewById(R.id.google_check);
     //   m_GoogleCheckbox
        // set this class to listen for the button clicks

        findViewById(R.id.button_sign_in).setOnClickListener(this);
        findViewById(R.id.button_sign_out).setOnClickListener(this);
        findViewById(R.id.default_login).setOnClickListener(this);
        edit_password=(EditText)findViewById(R.id.edit_password);
        edit_id=(EditText)findViewById(R.id.edit_id);
        button_default=(Button)findViewById(R.id.default_login);



       pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        //저장된 값을 불러온다.
        Boolean chk1=pref.getBoolean("check1",false); //구글 자동로그인 체크여부부
        m_GoogleCheckbox.setChecked(chk1);



        //findViewById(R.id.google_check).setVisibility(View.GONE);


    }

    protected void onStart() {
        Log.d(TAG, "onStart()");
        super.onStart();
        if(m_GoogleCheckbox.isChecked()) {
             ConnectDB();

        }
    }



    protected void onStop() {
        Log.d(TAG, "onStop()");
        super.onStop();
//        if (AppManager.getInstance().mGoogleApiClient.isConnected()) {
  //          AppManager.getInstance().mGoogleApiClient.disconnect();
    //    }
    }

    // Shows the "sign in" bar (explanation and button).
    private void showSignInBar() {
        Log.d(TAG, "Showing sign in bar");
        findViewById(R.id.sign_in_bar).setVisibility(View.VISIBLE);
        findViewById(R.id.sign_out_bar).setVisibility(View.GONE);
    }

    // Shows the "sign out" bar (explanation and button).
    private void showSignOutBar() {
        Log.d(TAG, "Showing sign out bar");
        findViewById(R.id.sign_in_bar).setVisibility(View.GONE);
        findViewById(R.id.sign_out_bar).setVisibility(View.VISIBLE);
    }
    private void ConnectDB()
    {
        AppManager.getInstance().mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
        mSignInClicked = true;
        AppManager.getInstance().mGoogleApiClient.connect();





    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_sign_in:
                // Check to see the developer who's running this sample code read the instructions :-)
                // NOTE: this check is here only because this is a sample! Don't include this
                // check in your actual production app.


                ConnectDB();
             /*   if (!BaseGameUtils.verifySampleSetup(this, R.string.app_id,
                        R.string.achievement_trivial_victory)) {
                    Log.w(TAG, "*** Warning: setup problems detected. Sign in may not work!");
                }
*/
                // start the sign-in flow
               // Log.d(TAG, "Sign-in button clicked");

                m_GoogleCheckbox.setChecked(true);
                //UI저장
                SharedPreferences.Editor editor=pref.edit();
                editor.putBoolean("check1",m_GoogleCheckbox.isChecked());
                editor.commit();

                break;
            case R.id.button_sign_out:
                // sign out.
                Log.d(TAG, "Sign-out button clicked");
                mSignInClicked = false;
                Games.signOut(AppManager.getInstance().mGoogleApiClient);
                AppManager.getInstance().mGoogleApiClient.disconnect();
                showSignInBar();
                m_GoogleCheckbox.setChecked(false);

                SharedPreferences.Editor editor1=pref.edit();
                editor1.putBoolean("check1",m_GoogleCheckbox.isChecked());
                editor1.commit();
                break;
            case R.id.default_login:
                try {
                    connect = true;
                    DBManager.getInstance().connection.oos.writeObject("기존정보"+":"+edit_id.getText() + ":" + edit_password.getText());
                    DBManager.getInstance().connection.oos.flush();
                    while (connect) {
                        if (DBManager.getInstance().getResponse().equals("접속성공")) {
                            DBManager.getInstance().setNetState(NetState.USERLOAD);
                            connect = false;
                            final Intent i = new Intent(this, GameActiviry.class);
                            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MainActivity.this);
                            startActivity(i);


                            AlertDialog alert = alert_confirm.create();
                            alert.show();
                            //  DBManager.getInstance().SetResponse("");
                        }else if (DBManager.getInstance().getResponse().equals("중복로그인")) {
                            connect = false;
                            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MainActivity.this);
                            alert_confirm.setMessage("접속 중복로그인").setCancelable(false).setPositiveButton("확인",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // 'YES'
                                        }
                                    });
                            AlertDialog alert = alert_confirm.create();
                            alert.show();
                            // DBManager.getInstance().SetResponse("");
                        }

                        else if (DBManager.getInstance().getResponse().equals("접속실패")) {
                            connect = false;
                            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MainActivity.this);
                            alert_confirm.setMessage("접속 실패").setCancelable(false).setPositiveButton("확인",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // 'YES'
                                        }
                                    });
                            AlertDialog alert = alert_confirm.create();
                            alert.show();
                            //   DBManager.getInstance().SetResponse("");
                        }
                        else
                        {
                            connect=false;
                            //  DBManager.getInstance().SetResponse("");
                        }
                    }
                    //한번에 4kb쓰는게 빠르다. 한번에 패킷을 크게 보내는게 오버헤드가 적다
                    //그것을 만든게 버퍼드 스트림 이다. flush를 할때까지 저장
                    //flush 는 버버드 아웃풋 시스템

                    // connection.ois.readUTF();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                break;


           /* case R.id.button_win:
                // win!

           *//*     Log.d(TAG, "Win button clicked");
                BaseGameUtils.showAlert(this, getString(R.string.you_won));
                if (mGoogleApiClient.isConnected()) {
                    // unlock the "Trivial Victory" achievement.
                    Games.Achievements.unlock(mGoogleApiClient,
                            getString(R.string.achievement__1));
                }*//*
                break;
            case R.id.button_confirm:
                Log.d(TAG, "확인해봐");


                startActivityForResult(Games.Achievements.getAchievementsIntent(AppManager.getInstance().mGoogleApiClient),0);
                //  Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                break;

            case R.id.getId:
                Plus.PeopleApi.getCurrentPerson(AppManager.getInstance().mGoogleApiClient).getId();
                Toast toast = Toast.makeText(MainActivity.this, ""+  Plus.PeopleApi.getCurrentPerson(AppManager.getInstance().mGoogleApiClient).getId(), Toast.LENGTH_SHORT );
                toast.show();
                // startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient),0);
                break;*/
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected() called. Sign in successful!");

        showSignOutBar();
        try {
            connect = true;
            DBManager.getInstance().connection.oos.writeObject("구글"+":"+Plus.PeopleApi.getCurrentPerson(AppManager.getInstance().mGoogleApiClient).getId());
            DBManager.getInstance().connection.oos.flush();
            while (connect) {
                if (DBManager.getInstance().getResponse().equals("접속성공")) {
                    DBManager.getInstance().setNetState(NetState.USERLOAD);
                    connect = false;
                    final Intent i = new Intent(this, GameActiviry.class);
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MainActivity.this);
                    startActivity(i);


                    AlertDialog alert = alert_confirm.create();
                    alert.show();
                    //  DBManager.getInstance().SetResponse("");
                }else if (DBManager.getInstance().getResponse().equals("중복로그인")) {
                    connect = false;
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MainActivity.this);
                    alert_confirm.setMessage("접속 중복로그인").setCancelable(false).setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 'YES'
                                }
                            });
                    AlertDialog alert = alert_confirm.create();
                    alert.show();
                    // DBManager.getInstance().SetResponse("");
                }

                else if (DBManager.getInstance().getResponse().equals("접속실패")) {
                    connect = false;
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MainActivity.this);
                    alert_confirm.setMessage("접속 실패").setCancelable(false).setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 'YES'
                                }
                            });
                    AlertDialog alert = alert_confirm.create();
                    alert.show();
                    //   DBManager.getInstance().SetResponse("");
                }
                else
                {
                    connect=false;
                    //  DBManager.getInstance().SetResponse("");
                }
            }
            //한번에 4kb쓰는게 빠르다. 한번에 패킷을 크게 보내는게 오버헤드가 적다
            //그것을 만든게 버퍼드 스트림 이다. flush를 할때까지 저장
            //flush 는 버버드 아웃풋 시스템

            // connection.ois.readUTF();

        } catch (Exception ex) {
            ex.printStackTrace();
        }




    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended() called. Trying to reconnect.");
        AppManager.getInstance().mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed() called, result: " + connectionResult);

        if (mResolvingConnectionFailure) {
            Log.d(TAG, "onConnectionFailed() ignoring connection failure; already resolving.");
            return;
        }

        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = BaseGameUtils.resolveConnectionFailure(this, AppManager.getInstance().mGoogleApiClient,
                    connectionResult, RC_SIGN_IN, getString(R.string.signin_other_error));
        }
        showSignInBar();
    }

    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult with requestCode == RC_SIGN_IN, responseCode="
                    + responseCode + ", intent=" + intent);
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (responseCode == RESULT_OK) {
                AppManager.getInstance().mGoogleApiClient.connect();
            } else {
                BaseGameUtils.showActivityResultError(this,requestCode,responseCode, R.string.signin_other_error);
            }
        }
    }



}
