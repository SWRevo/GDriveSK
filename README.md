# GDriveSK
[![](https://jitpack.io/v/SWRevo/GDriveSK.svg)](https://jitpack.io/#SWRevo/GDriveSK)

Implementation Googe Drive Api in Android.

## Requirements

- Java 1.8
- This library made with kotlin language

## Gradle

Add the following to your `build.gradle` to use:
```
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
  implementation 'com.github.SWRevo:GDriveSK:1.1.2'
}
```

## Usage
## Sample in Kotlin :

```java
class MainActivityKT : AppCompatActivity() {
    private var mDriveServiceHelper: DriveServiceHelper? = null
    private val login: Button? = null
    private var searchFile: Button? = null
    private var searchFolder: Button? = null
    private var createTextFile: Button? = null
    private var createFolder: Button? = null
    private var uploadFile: Button? = null
    private var downloadFile: Button? = null
    private var email: TextView? = null
    private var viewFileFolder: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        searchFile!!.setOnClickListener {
            if (mDriveServiceHelper == null) {
                return@setOnClickListener
            }
            mDriveServiceHelper!!.searchFile("textfilename.txt", "text/plain")
                    .addOnSuccessListener { googleDriveFileHolders: List<GoogleDriveFileHolder?>? ->
                        val gson = Gson()
                        Log.d(TAG, "onSuccess: " + gson.toJson(googleDriveFileHolders))
                    }
                    .addOnFailureListener { e: Exception -> Log.d(TAG, "onFailure: " + e.message) }
        }
        searchFolder!!.setOnClickListener {
            if (mDriveServiceHelper == null) {
                return@setOnClickListener
            }
            mDriveServiceHelper!!.searchFolder("testDummy")
                    .addOnSuccessListener { googleDriveFileHolders: List<GoogleDriveFileHolder?>? ->
                        val gson = Gson()
                        Log.d(TAG, "onSuccess: " + gson.toJson(googleDriveFileHolders))
                    }
                    .addOnFailureListener { e: Exception -> Log.d(TAG, "onFailure: " + e.message) }
        }
        createTextFile!!.setOnClickListener {
            if (mDriveServiceHelper == null) {
                return@setOnClickListener
            }
            mDriveServiceHelper!!.createTextFile("textfilename.txt", "some text", null)
                    .addOnSuccessListener { googleDriveFileHolder: GoogleDriveFileHolder? ->
                        val gson = Gson()
                        Log.d(TAG, "onSuccess: " + gson.toJson(googleDriveFileHolder))
                    }
                    .addOnFailureListener { e: Exception -> Log.d(TAG, "onFailure: " + e.message) }
        }
        createFolder!!.setOnClickListener {
            if (mDriveServiceHelper == null) {
                return@setOnClickListener
            }
            mDriveServiceHelper!!.createFolder("testDummyss", null)
                    .addOnSuccessListener { googleDriveFileHolder: GoogleDriveFileHolder? ->
                        val gson = Gson()
                        Log.d(TAG, "onSuccess: " + gson.toJson(googleDriveFileHolder))
                    }
                    .addOnFailureListener { e: Exception -> Log.d(TAG, "onFailure: " + e.message) }
        }
        uploadFile!!.setOnClickListener {
            if (mDriveServiceHelper == null) {
                return@setOnClickListener
            }
            mDriveServiceHelper!!.uploadFile(File(applicationContext.filesDir, "dummy.txt"), "text/plain", null)
                    .addOnSuccessListener { googleDriveFileHolder: GoogleDriveFileHolder? ->
                        val gson = Gson()
                        Log.d(TAG, "onSuccess: " + gson.toJson(googleDriveFileHolder))
                    }
                    .addOnFailureListener { e: Exception -> Log.d(TAG, "onFailure: " + e.message) }
        }
        downloadFile!!.setOnClickListener {
            if (mDriveServiceHelper == null) {
                return@setOnClickListener
            }
            mDriveServiceHelper!!.downloadFile(File(applicationContext.filesDir, "filename.txt"), "google_drive_file_id_here")
                    .addOnSuccessListener {

                    }
                    .addOnFailureListener { e: Exception? ->
                        @Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
                        var errorString: String? = null
                        @Suppress("UNUSED_VALUE")
                        errorString = e!!.message
                    }
        }
        viewFileFolder!!.setOnClickListener {

        }
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(applicationContext)
        if (account == null) {
            signIn()
        } else {
            email!!.text = account.email
            mDriveServiceHelper = DriveServiceHelper(DriveServiceHelper.getGoogleDriveService(applicationContext, account, "appName"))
        }
    }

    private fun signIn() {
        val mGoogleSignInClient = buildGoogleSignInClient()
        startActivityForResult(mGoogleSignInClient.signInIntent, REQUEST_CODE_SIGN_IN)
    }

    private fun buildGoogleSignInClient(): GoogleSignInClient {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Drive.SCOPE_FILE)
                .requestEmail()
                .build()
        return GoogleSignIn.getClient(applicationContext, signInOptions)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            if (resultCode == RESULT_OK && resultData != null) {
                handleSignInResult(resultData)
            }
        }
        super.onActivityResult(requestCode, resultCode, resultData)
    }

    fun test() {
        println("test")
    }

    private fun handleSignInResult(result: Intent) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
                .addOnSuccessListener { googleSignInAccount: GoogleSignInAccount ->
                    Log.d(TAG, "Signed in as " + googleSignInAccount.email)
                    email!!.text = googleSignInAccount.email
                    mDriveServiceHelper = DriveServiceHelper(DriveServiceHelper.getGoogleDriveService(applicationContext, googleSignInAccount, "appName"))
                    Log.d(TAG, "handleSignInResult: $mDriveServiceHelper")
                }
                .addOnFailureListener { e: Exception? -> Log.e(TAG, "Unable to sign in.", e) }
    }

    @Suppress("UNUSED_VARIABLE")
    private fun initView() {
        email = findViewById(R.id.email)
        val gDriveAction = findViewById<LinearLayout>(R.id.g_drive_action)
        searchFile = findViewById(R.id.search_file)
        searchFolder = findViewById(R.id.search_folder)
        createTextFile = findViewById(R.id.create_text_file)
        createFolder = findViewById(R.id.create_folder)
        uploadFile = findViewById(R.id.upload_file)
        downloadFile = findViewById(R.id.download_file)
        val deleteFileFolder = findViewById<Button>(R.id.delete_file_folder)
        viewFileFolder = findViewById(R.id.view_file_folder)
    }

    companion object {
        private const val REQUEST_CODE_SIGN_IN = 100
        private const val TAG = "MainActivityKT"
    }
}

```

## Usage
## Sample in Java :

```java
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


```

## License

    Copyright 2021 indosw

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
