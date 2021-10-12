package com.calintat.explorer.Activity;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ammarptn.gdriverest.DriveServiceHelper;
import com.ammarptn.gdriverest.GoogleDriveFileHolder;
import com.calintat.explorer.Model.GDrive;
import com.calintat.explorer.R;
import com.calintat.explorer.fragment.GDrive.GDriveFragment;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;
import static com.ammarptn.gdriverest.DriveServiceHelper.getGoogleDriveService;

public class DriveActivity extends AppCompatActivity {

    private static final String TAG = DriveActivity.class.getSimpleName();
    private static final int RC_ACCOUNT_PICKER = 1;
    private static final int RC_AUTHORIZATION = 2;
    private static final int RC_EDIT_DATA = 3;

    public static ArrayList<GDrive> gDriveArrayList = new ArrayList<>();

    @BindView(R.id.rl_fram)
    RelativeLayout rl_fram;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.rl_progress)
    RelativeLayout rl_progress;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.img_filter)
    ImageView img_filter;

    Bundle savedInstanceState1;
    private GoogleAccountCredential mCredential = null;
    @Nonnull
    private Drive mService = getDriveService(null);
    private Handler mHandler;
    private DriveServiceHelper mDriveServiceHelper;

    void startAnim() {
        rl_progress.setVisibility(View.VISIBLE);
        avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim() {
        rl_progress.setVisibility(View.GONE);
        avi.hide();
        // or avi.smoothToHide();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);

        savedInstanceState1 = savedInstanceState;

        ButterKnife.bind(DriveActivity.this);
        tv_title.setText("Drive");

        img_filter.setVisibility(View.GONE);

        HandlerThread handlerThread = new HandlerThread(getPackageName());
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());

    }

    @Override
    protected void onStart() {
        super.onStart();
        signIn();
    }

    private void signIn() {
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(DriveActivity.this);
        if (signInAccount != null) {
            initializeDriveClient(signInAccount);
        } else {
            chooseNewAccount();
        }
    }

    private void chooseNewAccount() {
        GoogleSignInClient googleSignInClient = getGoogleSignInClient();
        googleSignInClient.signOut();
        startActivityForResult(googleSignInClient.getSignInIntent(), RC_ACCOUNT_PICKER);
    }

    private GoogleSignInClient getGoogleSignInClient() {
//        Scope driveScope = new Scope("https://www.googleapis.com/auth/drive");
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(DriveScopes.DRIVE))
                .requestEmail()
                .build();
//        GoogleSignInOptions signInOptions =
//                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                        .requestIdToken(getString(R.string.api_key))
//                        .requestEmail()
//                        .build();
        return GoogleSignIn.getClient(this, signInOptions);
    }

    /**
     * Continues the sign-in process, initializing the Drive clients with the current
     * user's account.
     */
    private void initializeDriveClient(GoogleSignInAccount signInAccount) {
        mCredential = GoogleAccountCredential.usingOAuth2(this,
                Collections.singletonList(DriveScopes.DRIVE_FILE))
                .setBackOff(new ExponentialBackOff());
        if (signInAccount.getAccount() != null) {
            String name = signInAccount.getAccount().name;
            String type = signInAccount.getAccount().type;
            mCredential.setSelectedAccount(new Account(name, type));

            mDriveServiceHelper = new DriveServiceHelper(getGoogleDriveService(getApplicationContext(), signInAccount, "File Explorer"));
            if (mDriveServiceHelper != null) {

                mDriveServiceHelper.queryFiles(null)
                        .addOnSuccessListener(new OnSuccessListener<List<GoogleDriveFileHolder>>() {
                            @Override
                            public void onSuccess(List<GoogleDriveFileHolder> googleDriveFileHolders) {
                                Gson gson = new Gson();
//                                Log.e("LLLLLL_Data: ", "onSuccess: " + gson.toJson(googleDriveFileHolders));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
//                                Log.e("LLLLLL_Data: ", "onFailed: " + e.toString());
                            }
                        });

            }
        }
        mService = getDriveService(mCredential);
        new LongOperation().execute();

    }

    private List<File> retrieveAllFiles() throws IOException {

        gDriveArrayList.clear();
        // Print the names and IDs for up to 10 files.

        FileList result1 = mService.files().list()
                .setSpaces("drive")
                .setFields("nextPageToken,files(id, name, size)")
                .execute();

        List<File> files1 = result1.getFiles();
        if (files1 == null || files1.isEmpty()) {
            System.out.println("No files found.");
        } else {
            for (File file : files1) {
                String path = file.getName();
                GDrive gDrive = new GDrive();
                gDrive.setId(file.getId());
                gDrive.setName(file.getName());
                if (file.getSize() != null)
                    gDrive.setSize(file.getSize());

                gDriveArrayList.add(gDrive);
//                queryFiles(file.getId(), file.getName(), result1.getNextPageToken());
            }
            retrieveAllFilesToken(result1.getNextPageToken());
        }

        return files1;
    }

    private List<File> retrieveAllFilesToken(String token) throws IOException {

        // Print the names and IDs for up to 10 files.

        List<File> files = new ArrayList<>();
        if (token != null) {
            if (!token.isEmpty()) {
                FileList result = mService.files().list()
                        .setSpaces("drive")
                        .setPageToken(token)
                        .setFields("nextPageToken,files(id, name, size)")
                        .execute();

                files = result.getFiles();
                if (files == null || files.isEmpty()) {
                    System.out.println("No files found.");
                } else {
                    for (File file : files) {
                        String path = file.getName();
                        GDrive gDrive = new GDrive();
                        gDrive.setId(file.getId());
                        gDrive.setName(file.getName());
                        if (file.getSize() != null)
                            gDrive.setSize(file.getSize());

                        gDriveArrayList.add(gDrive);
//                        queryFiles(file.getId(), file.getName(), result.getNextPageToken());
                    }
                    retrieveAllFilesToken(result.getNextPageToken());
                }

                Log.e("LLLL_size1: ", String.valueOf(files.size()));

            }
        }

        return files;
    }

    public Task<List<GoogleDriveFileHolder>> queryFiles(@Nullable final String folderId, String foldername, String token) {
        return Tasks.call(THREAD_POOL_EXECUTOR, new Callable<List<GoogleDriveFileHolder>>() {
                    @Override
                    public List<GoogleDriveFileHolder> call() throws Exception {
                        List<GoogleDriveFileHolder> googleDriveFileHolderList = new ArrayList<>();
                        String parent = "root";
                        if (folderId != null) {
                            parent = folderId;
                        }

                        FileList result = mService.files().list()
                                .setQ("'" + parent + "' in parents")
                                .setFields("files(id, name,size,createdTime,modifiedTime,starred)")
                                .setSpaces("drive")
                                .execute();

                        for (int i = 0; i < result.getFiles().size(); i++) {

                            GoogleDriveFileHolder googleDriveFileHolder = new GoogleDriveFileHolder();
                            googleDriveFileHolder.setId(result.getFiles().get(i).getId());
                            googleDriveFileHolder.setName(result.getFiles().get(i).getName());
                            Log.e("LLLL_folder: ", foldername + "         filename: " + result.getFiles().get(i).getName());
                            if (result.getFiles().get(i).getSize() != null) {
                                googleDriveFileHolder.setSize(result.getFiles().get(i).getSize());
                            }

                            if (result.getFiles().get(i).getModifiedTime() != null) {
                                googleDriveFileHolder.setModifiedTime(result.getFiles().get(i).getModifiedTime());
                            }

                            if (result.getFiles().get(i).getCreatedTime() != null) {
                                googleDriveFileHolder.setCreatedTime(result.getFiles().get(i).getCreatedTime());
                            }

                            if (result.getFiles().get(i).getStarred() != null) {
                                googleDriveFileHolder.setStarred(result.getFiles().get(i).getStarred());
                            }

                            googleDriveFileHolderList.add(googleDriveFileHolder);

                        }


                        return googleDriveFileHolderList;


                    }
                }
        );
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_ACCOUNT_PICKER:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    // The signed in account is stored in the result.
                    GoogleSignInAccount signedInAccount = result.getSignInAccount();
                    Log.e("LLLL_Success: ", signedInAccount.getDisplayName());
                } else {
                    String message = result.getStatus().toString();
                    if (message == null || message.isEmpty()) {
                        Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show();
                    } else {
                        Log.e("LLLLLL_failed: ", message);
                    }
                    new AlertDialog.Builder(this).setMessage(message)
                            .setNeutralButton(android.R.string.ok, null).show();
                }
                if (resultCode != RESULT_OK) {
                    Log.e(TAG, "Sign-in failed.");
                    Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show();
                    return;
                }

                Task<GoogleSignInAccount> getAccountTask =
                        GoogleSignIn.getSignedInAccountFromIntent(data);
                if (getAccountTask.isSuccessful()) {
                    initializeDriveClient(getAccountTask.getResult());
                } else {
                    Log.e(TAG, "Sign-in failed.");
                    finish();
                }

                break;
            case RC_AUTHORIZATION:
                if (resultCode == Activity.RESULT_OK) {
                } else {
                    Toast.makeText(this, getString(R.string.require_permission), Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case RC_EDIT_DATA:

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Drive getDriveService(GoogleAccountCredential credential) {
        return new Drive
                .Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential)
                .build();
    }

    private final class LongOperation extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startAnim();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                retrieveAllFiles();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Execute";
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Execute")) {
                stopAnim();
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.rl_fram, new GDriveFragment()).commit();
            }
        }
    }
}