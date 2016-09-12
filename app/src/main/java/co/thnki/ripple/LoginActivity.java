package co.thnki.ripple;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.thnki.ripple.models.Users;

import static co.thnki.ripple.Ripples.toast;

public class LoginActivity extends AppCompatActivity
{

    private static final String TAG = "saleem";
    private static final String LOGIN_STATUS = "loginStatus";
    private static final String USERS = "users";
    private static final String PROFILE_PHOTOS = "profilePhotos";
    public static final String LOGOUT = "logout";

    @Bind(R.id.login_button)
    LoginButton mFacebookLoginButton;

    private CallbackManager mFacebookCallbackManager;
    public FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog mProgress;
    private SharedPreferences mPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mPreference = Ripples.getPreferences();
        Intent intent = getIntent();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                }
                else
                {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        FacebookSdk.sdkInitialize(getApplicationContext());
        Log.d(TAG, "facebook:started");
        mFacebookCallbackManager = CallbackManager.Factory.create();
        mFacebookLoginButton.setReadPermissions("email", "public_profile");
        mFacebookLoginButton.registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel()
            {
                Log.d(TAG, "facebook:onCancel");

            }

            @Override
            public void onError(FacebookException error)
            {
                Log.d(TAG, "facebook:onError", error);
                error.printStackTrace();
            }
        });

        if(intent != null && intent.hasExtra(LOGOUT))
        {
            logout();
        }

        if (mPreference.getBoolean(LOGIN_STATUS, false))
        {
            handleLogin();
        }
    }

    private void handleLogin()
    {
        startActivity(new Intent(LoginActivity.this, PlayerActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token)
    {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        mProgress.dismiss();
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful())
                        {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            toast("Authentication failed.");
                        }
                        else
                        {
                            mPreference.edit().putBoolean(LOGIN_STATUS, true).apply();
                            DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference().child(USERS);
                            Users user = new Users(Profile.getCurrentProfile());
                            mDatabaseRef.child(user.userId).setValue(user);
                            handleLogin();
                        }
                    }
                });
    }

    public void logout()
    {
        mPreference.edit().putBoolean(LOGIN_STATUS, false).apply();
        LoginManager.getInstance().logOut();
        mAuth.signOut();
    }

    @OnClick(R.id.login_button)
    public void login()
    {
        mProgress = new ProgressDialog(this);
        mProgress.setMessage(getString(R.string.signing_in));
        mProgress.show();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (mAuthListener != null)
        {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
