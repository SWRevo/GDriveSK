package id.indosw.gdrivesk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.drive.Drive;
import com.google.gson.Gson;

import id.indosw.gdriverest.DriveServiceHelper;

import static id.indosw.gdriverest.DriveServiceHelper.getGoogleDriveService;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SIGN_IN = 100;
    private DriveServiceHelper mDriveServiceHelper;
    private static final String TAG = "MainActivity";
    private Button login;
    private Button searchFile;
    private Button searchFolder;
    private Button createTextFile;
    private Button createFolder;
    private Button uploadFile;
    private Button downloadFile;
    private TextView email;
    private Button viewFileFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        searchFile.setOnClickListener(view -> {
            if (mDriveServiceHelper == null) {
                return;
            }
            mDriveServiceHelper.searchFile("textfilename.txt", "text/plain")
                    .addOnSuccessListener(googleDriveFileHolders -> {
                        Gson gson = new Gson();
                        Log.d(TAG, "onSuccess: " + gson.toJson(googleDriveFileHolders));
                    })
                    .addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.getMessage()));
        });

        searchFolder.setOnClickListener(view -> {
            if (mDriveServiceHelper == null) {
                return;
            }
            mDriveServiceHelper.searchFolder("testDummy")
                    .addOnSuccessListener(googleDriveFileHolders -> {
                        Gson gson = new Gson();
                        Log.d(TAG, "onSuccess: " + gson.toJson(googleDriveFileHolders));
                    })
                    .addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.getMessage()));
        });

        createTextFile.setOnClickListener(view -> {
            if (mDriveServiceHelper == null) {
                return;
            }
            mDriveServiceHelper.createTextFile("textfilename.txt", "some text", null)
                    .addOnSuccessListener(googleDriveFileHolder -> {
                        Gson gson = new Gson();
                        Log.d(TAG, "onSuccess: " + gson.toJson(googleDriveFileHolder));
                    })
                    .addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.getMessage()));
        });

        createFolder.setOnClickListener(view -> {
            if (mDriveServiceHelper == null) {
                return;
            }
            mDriveServiceHelper.createFolder("testDummyss", null)
                    .addOnSuccessListener(googleDriveFileHolder -> {
                        Gson gson = new Gson();
                        Log.d(TAG, "onSuccess: " + gson.toJson(googleDriveFileHolder));
                    })
                    .addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.getMessage()));
        });

        uploadFile.setOnClickListener(view -> {
            if (mDriveServiceHelper == null) {
                return;
            }
            mDriveServiceHelper.uploadFile(new java.io.File(getApplicationContext().getFilesDir(), "dummy.txt"), "text/plain", null)
                    .addOnSuccessListener(googleDriveFileHolder -> {
                        Gson gson = new Gson();
                        Log.d(TAG, "onSuccess: " + gson.toJson(googleDriveFileHolder));
                    })
                    .addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.getMessage()));
        });

        downloadFile.setOnClickListener(view -> {
            if (mDriveServiceHelper == null) {
                return;
            }
            mDriveServiceHelper.downloadFile(new java.io.File(getApplicationContext().getFilesDir(), "filename.txt"), "google_drive_file_id_here")
                    .addOnSuccessListener(aVoid -> {
                        //Do something
                    })
                    .addOnFailureListener(e -> {
                        //Do something
                    });
        });

        viewFileFolder.setOnClickListener(view -> {
            //Intent openActivity = new Intent(getApplicationContext(), GDriveDebugViewActivity.class);
            //startActivity(openActivity);
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account == null) {
            signIn();
        } else {
            email.setText(account.getEmail());
            mDriveServiceHelper = new DriveServiceHelper(getGoogleDriveService(getApplicationContext(), account, "appName"));
        }
    }
    private void signIn() {
        GoogleSignInClient mGoogleSignInClient = buildGoogleSignInClient();
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }
    @SuppressWarnings("deprecation")
    private GoogleSignInClient buildGoogleSignInClient() {
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(Drive.SCOPE_FILE)
                        .requestEmail()
                        .build();
        return GoogleSignIn.getClient(getApplicationContext(), signInOptions);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK && resultData != null) {
                handleSignInResult(resultData);
            }
        }
        super.onActivityResult(requestCode, resultCode, resultData);
    }

    public void test() {
        System.out.println("test");
    }

    private void handleSignInResult(Intent result) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
                .addOnSuccessListener(googleSignInAccount -> {
                    Log.d(TAG, "Signed in as " + googleSignInAccount.getEmail());
                    email.setText(googleSignInAccount.getEmail());
                    mDriveServiceHelper = new DriveServiceHelper(getGoogleDriveService(getApplicationContext(), googleSignInAccount, "appName"));
                    Log.d(TAG, "handleSignInResult: " + mDriveServiceHelper);
                })
                .addOnFailureListener(e -> Log.e(TAG, "Unable to sign in.", e));
    }

    private void initView() {
        email = findViewById(R.id.email);
        LinearLayout gDriveAction = findViewById(R.id.g_drive_action);
        searchFile = findViewById(R.id.search_file);
        searchFolder = findViewById(R.id.search_folder);
        createTextFile = findViewById(R.id.create_text_file);
        createFolder = findViewById(R.id.create_folder);
        uploadFile = findViewById(R.id.upload_file);
        downloadFile = findViewById(R.id.download_file);
        Button deleteFileFolder = findViewById(R.id.delete_file_folder);
        viewFileFolder = findViewById(R.id.view_file_folder);
    }
}
