@file:Suppress("DEPRECATION", "unused")

package id.indosw.gdrivesk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.drive.Drive
import com.google.gson.Gson
import id.indosw.gdriverest.DriveServiceHelper
import id.indosw.gdriverest.GoogleDriveFileHolder
import java.io.File

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