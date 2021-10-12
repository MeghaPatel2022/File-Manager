package com.calintat.explorer.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.calintat.explorer.Adapter.DrawerAdapter;
import com.calintat.explorer.Adapter.RecentAdapter;
import com.calintat.explorer.Adapter.RecyclerAdapter;
import com.calintat.explorer.Model.DataModel;
import com.calintat.explorer.R;
import com.calintat.explorer.RecyclerOnItemClickListener;
import com.calintat.explorer.RecyclerOnSelectionListener;
import com.calintat.explorer.Utils.DownloadData;
import com.calintat.explorer.Utils.FileType;
import com.calintat.explorer.Utils.FileUtils;
import com.calintat.explorer.Utils.FullDrawerLayout;
import com.calintat.explorer.Utils.InputDialog;
import com.calintat.explorer.Utils.ListViewUtil;
import com.calintat.explorer.Utils.PreferenceUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.calintat.explorer.Utils.FileUtils.getInternalStorage;
import static com.calintat.explorer.Utils.FileUtils.getMimeType;
import static com.calintat.explorer.Utils.FileUtils.getName;
import static com.calintat.explorer.Utils.FileUtils.getPath;
import static com.calintat.explorer.Utils.FileUtils.removeExtension;
import static com.calintat.explorer.Utils.FileUtils.unzip;

public class MainActivity extends AppCompatActivity {

    private static final String SAVED_DIRECTORY = "com.calintat.explorer.SAVED_DIRECTORY";
    private static final String SAVED_SELECTION = "com.calintat.explorer.SAVED_SELECTION";
    private static final String EXTRA_NAME = "com.calintat.explorer.EXTRA_NAME";
    private static final String EXTRA_TYPE = "com.calintat.explorer.EXTRA_TYPE";
    //----------------------------------------------------------------------------------------------
    private final ArrayList<String> menuItems = new ArrayList<>();
    private final ArrayList<Integer> menuimages = new ArrayList<>();
    ArrayList<DataModel> imgDownloadList = new ArrayList<>();
    ArrayList<DataModel> imgDownloadList1 = new ArrayList<>();
    ArrayList<DataModel> allDataList = new ArrayList<>();
    ArrayList<DataModel> allMainDataList = new ArrayList<>();
    //    private CollapsingToolbarLayout toolbarLayout;
    private CoordinatorLayout coordinatorLayout;
    private FullDrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView img_menu, img_close;
    private LinearLayout ll_image, ll_vidoes, ll_audios, ll_docs, ll_download, ll_zip, ll_cloud, ll_app;
    private Toolbar toolbar;
    private ListViewUtil nav_list;
    private File currentDirectory;
    private RecyclerAdapter recyclerAdapter;
    private ImageView img_external, img_internal;
    private RecyclerView rv_recent;
    private TextView tv_more;
    private RecentAdapter recentAdapter;

    private String name;
    private String type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        initActivityFromIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i <= 90000; i++) {
            Log.d("LLLL i :", String.valueOf(i));
        }

        initAppBarLayout();
        initCoordinatorLayout();
        initDrawerLayout();
        initNavigationView();
        initRecyclerView();
        loadIntoRecyclerView();
    }


    private void onScrolledToBottom() {
        if (allMainDataList.size() < allDataList.size()) {
            int x, y;
            if ((allDataList.size() - allMainDataList.size()) >= 9) {
                x = allMainDataList.size();
                y = x + 9;
            } else {
                x = allMainDataList.size();
                y = x + allDataList.size() - allMainDataList.size();
            }
            for (int i = x; i < y; i++) {
                allMainDataList.add(allDataList.get(i));
            }
            runOnUiThread(() -> {
                recentAdapter.notifyDataSetChanged();
            });

        }

    }

    private void initMainData() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!getPackageManager().canRequestPackageInstalls()) {
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).setData(Uri.parse(String.format("package:%s", getPackageName()))), 1234);
            }
        }

        ll_image = findViewById(R.id.ll_image);
        ll_vidoes = findViewById(R.id.ll_vidoes);
        ll_audios = findViewById(R.id.ll_audios);
        ll_docs = findViewById(R.id.ll_docs);
        ll_download = findViewById(R.id.ll_download);
        ll_zip = findViewById(R.id.ll_zip);
        ll_cloud = findViewById(R.id.ll_cloud);
        ll_app = findViewById(R.id.ll_app);
        img_internal = findViewById(R.id.img_internal);
        img_external = findViewById(R.id.img_external);


        rv_recent = findViewById(R.id.rv_recent);
        tv_more = findViewById(R.id.tv_more);
        rv_recent.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
        rv_recent.setNestedScrollingEnabled(false);

        allDataList.clear();
        allDataList = DownloadData.getAllRecentData(allDataList);

        recentAdapter = new RecentAdapter(allMainDataList, MainActivity.this, (view1, position) -> {
        });
        rv_recent.setAdapter(recentAdapter);

        onScrolledToBottom();

        tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecentActivity.class);
                startActivity(intent);
                finish();
            }
        });

        if (DownloadData.SELECT_STORAGE_TYPE.equals("internal")) {
            img_internal.setImageDrawable(getResources().getDrawable(R.drawable.select_internal));
        } else {
            img_external.setImageDrawable(getResources().getDrawable(R.drawable.select_external));
        }

        img_internal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DownloadData.SELECT_STORAGE_TYPE = "internal";
                        if (DownloadData.SELECT_STORAGE_TYPE.equals("internal")) {
                            img_internal.setImageDrawable(getResources().getDrawable(R.drawable.select_internal));
                            img_external.setImageDrawable(getResources().getDrawable(R.drawable.external_storage));
                        } else {
                            img_external.setImageDrawable(getResources().getDrawable(R.drawable.select_external));
                            img_internal.setImageDrawable(getResources().getDrawable(R.drawable.internal_storage));
                        }
                    }
                });

            }
        });

        img_external.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DownloadData.SELECT_STORAGE_TYPE = "external";
                        if (DownloadData.SELECT_STORAGE_TYPE.equals("internal")) {
                            img_internal.setImageDrawable(getResources().getDrawable(R.drawable.select_internal));
                            img_external.setImageDrawable(getResources().getDrawable(R.drawable.external_storage));
                        } else {
                            img_external.setImageDrawable(getResources().getDrawable(R.drawable.select_external));
                            img_internal.setImageDrawable(getResources().getDrawable(R.drawable.internal_storage));
                        }
                    }
                });

            }
        });


        ll_audios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DownloadData.SELECT_STORAGE_TYPE.equals("internal")) {
                    setType("audio");
                    Intent audioIntent = new Intent(MainActivity.this, AudioActivity.class);
                    startActivity(audioIntent);
                } else {
                    Intent intent = new Intent(MainActivity.this, InternMusicActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        ll_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DownloadData.SELECT_STORAGE_TYPE.equals("internal")) {
                    Intent intent = new Intent(MainActivity.this, ImgeViewActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, InternImgActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        ll_vidoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DownloadData.SELECT_STORAGE_TYPE.equals("internal")) {
//                    UwMediaPicker.Companion
//                            .with(MainActivity.this)
//                            .setGalleryMode(UwMediaPicker.GalleryMode.VideoGallery)
//                            .setGridColumnCount(3)
//                            .setMaxSelectableMediaCount(1)
//                            .setLightStatusBar(false)
//                            .enableImageCompression(false)
//                            .launch(new kotlin.jvm.functions.Function1<List<UwMediaPickerMediaModel>, kotlin.Unit>() {
//                                @Override
//                                public Unit invoke(List<UwMediaPickerMediaModel> uwMediaPickerMediaModels) {
//                                    Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
//                                    startActivity(intent1);
//                                    finish();
//                                    return null;
//                                }
//                            });// Is image compression enable, default is false
                    Intent videoIntent = new Intent(MainActivity.this, VideoActivity.class);
                    startActivity(videoIntent);
                    finish();


                } else {
                    Intent intent = new Intent(MainActivity.this, InternVidActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        ll_docs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DownloadData.SELECT_STORAGE_TYPE.equals("internal")) {
                    Intent documentIntent = new Intent(MainActivity.this, DocumentActivity.class);
                    startActivity(documentIntent);
                    finish();
                } else {
                    Intent intent = new Intent(MainActivity.this, InternDocActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        ll_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DownloadData.SELECT_STORAGE_TYPE.equals("internal")) {
                    Intent downloadIntent = new Intent(MainActivity.this, DownloadActivity.class);
                    startActivity(downloadIntent);
                    finish();
                } else {
                    Intent intent = new Intent(MainActivity.this, InternDownloadActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        ll_zip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DownloadData.SELECT_STORAGE_TYPE.equals("internal")) {
                    Intent zipIntent = new Intent(MainActivity.this, ZipActivity.class);
                    startActivity(zipIntent);
                    finish();
                } else {
                    Intent intent = new Intent(MainActivity.this, InternZipActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        ll_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DownloadData.SELECT_STORAGE_TYPE.equals("internal")) {
                    Intent appIntent = new Intent(MainActivity.this, AppActivity.class);
                    startActivity(appIntent);
                    finish();
                } else {
                    Intent intent = new Intent(MainActivity.this, InternAppActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        ll_cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DriveActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawers();

            return;
        }

        if (recyclerAdapter.anySelected()) {
            recyclerAdapter.clearSelection();

            return;
        }

        if (!FileUtils.isStorage(currentDirectory)) {
            setPath(currentDirectory.getParentFile());

            return;
        }

        finishAffinity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length != 0) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    String message = "App doesn't work without permission";

                    Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE)
                            .setAction("Settings", v -> {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.fromParts("package", "com.calintat.explorer", null));
                                startActivity(intent);
                            })
                            .show();
                } else {
                    allDataList.clear();
                    allDataList = DownloadData.getAllRecentData(allDataList);

                    recentAdapter = new RecentAdapter(allMainDataList, MainActivity.this, (view1, position) -> {
                    });

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rv_recent.setAdapter(recentAdapter);
                        }
                    });

                    loadIntoRecyclerView();
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        if (recyclerAdapter != null) {
            for (int i = 0; i <= recyclerAdapter.getItemCount() - 1; i++) {
                recyclerAdapter.notifyItemChanged(i);
            }
        }
        initMainData();

        super.onResume();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        String path = savedInstanceState.getString(SAVED_DIRECTORY, getInternalStorage().getPath());

        if (currentDirectory != null) setPath(new File(path));
        recyclerAdapter.select(savedInstanceState.getIntegerArrayList(SAVED_SELECTION));
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putIntegerArrayList(SAVED_SELECTION, recyclerAdapter.getSelectedPositions());
        outState.putString(SAVED_DIRECTORY, getPath(currentDirectory));
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                actionDelete();
                return true;

            case R.id.action_rename:
                actionRename();
                return true;

            case R.id.action_search:
                actionSearch();
                return true;

            case R.id.action_copy:
                actionCopy();
                return true;

            case R.id.action_move:
                actionMove();
                return true;

            case R.id.action_send:
                actionSend();
                return true;

            case R.id.action_sort:
                actionSort();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (recyclerAdapter != null) {
            int count = recyclerAdapter.getSelectedItemCount();
            menu.findItem(R.id.action_delete).setVisible(count >= 1);
            menu.findItem(R.id.action_rename).setVisible(count >= 1);
            menu.findItem(R.id.action_search).setVisible(count == 0);
            menu.findItem(R.id.action_copy).setVisible(count >= 1 && name == null && type == null);
            menu.findItem(R.id.action_move).setVisible(count >= 1 && name == null && type == null);
            menu.findItem(R.id.action_send).setVisible(count >= 1);
            menu.findItem(R.id.action_sort).setVisible(count == 0);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    //----------------------------------------------------------------------------------------------

    private void initActivityFromIntent() {
        name = getIntent().getStringExtra(EXTRA_NAME);

        type = getIntent().getStringExtra(EXTRA_TYPE);

        if (type != null) {
            switch (type) {
                case "audio":
                    setTheme(R.style.AppTheme_Audio);
                    break;

                case "image":
                    setTheme(R.style.AppTheme_Image);
                    break;

                case "video":
                    setTheme(R.style.AppTheme_Video);
                    break;
            }
        }
    }

    private void loadIntoRecyclerView() {
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;

        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, permission)) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 0);

            return;
        }

        final Context context = this;

        if (name != null) {
            recyclerAdapter.addAll(FileUtils.searchFilesName(context, name));

            return;
        }

        if (type != null) {
            switch (type) {
                case "audio":
                    recyclerAdapter.addAll(FileUtils.getAudioLibrary(context));
                    break;

                case "image":
                    recyclerAdapter.addAll(FileUtils.getImageLibrary(context));
                    Intent intent = new Intent(MainActivity.this, ImgeViewActivity.class);
                    startActivity(intent);
                    finish();
                    break;

                case "video":
//                    recyclerAdapter.addAll(FileUtils.getVideoLibrary(context));
//                    UwMediaPicker.Companion
//                            .with(MainActivity.this)                        // Activity or Fragment
//                            .setGalleryMode(UwMediaPicker.GalleryMode.VideoGallery) // GalleryMode: ImageGallery/VideoGallery/ImageAndVideoGallery, default is ImageGallery
//                            .setGridColumnCount(3)                                  // Grid column count, default is 3
//                            .setMaxSelectableMediaCount(1)                         // Maximum selectable media count, default is null which means infinite
//                            .setLightStatusBar(false)                                // Is llight status bar enable, default is true
//                            .enableImageCompression(false);
                    Intent videoIntent = new Intent(MainActivity.this, VideoActivity.class);
                    startActivity(videoIntent);
                    finish();

                    break;
            }

            return;
        }

        setPath(getInternalStorage());
    }

    //----------------------------------------------------------------------------------------------

    private void initAppBarLayout() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.ic_more));
        setSupportActionBar(toolbar);
    }

    private void initCoordinatorLayout() {
        coordinatorLayout = findViewById(R.id.coordinator_layout);
    }

    private void initDrawerLayout() {
        drawerLayout = findViewById(R.id.drawer_layout);

        if (name != null || type != null) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    private void initNavigationView() {
        navigationView = findViewById(R.id.navigation_view);
        nav_list = findViewById(R.id.nav_list);

        img_menu = findViewById(R.id.img_menu);
        img_close = findViewById(R.id.img_close);
        img_menu = findViewById(R.id.img_menu);
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawers();

                    return;
                }
            }
        });

        menuItems.add("Images");
        menuItems.add("Videos");
        menuItems.add("Audios");
        menuItems.add("Documents");
        menuItems.add("Downloads");
        menuItems.add("Zip Files");
        menuItems.add("Cloud");
        menuItems.add("App");

        menuimages.add(R.drawable.images);
        menuimages.add(R.drawable.videos);
        menuimages.add(R.drawable.audios);
        menuimages.add(R.drawable.documents);
        menuimages.add(R.drawable.downloads);
        menuimages.add(R.drawable.zip_files);
        menuimages.add(R.drawable.cloud);
        menuimages.add(R.drawable.app);

        nav_list.setAdapter(new DrawerAdapter(this, menuItems, menuimages));
        nav_list.setOnItemClickListener((parent, view, position, id) -> {
            drawerLayout.closeDrawers();
            switch (position) {
                case 2:
                    Intent audioIntent = new Intent(MainActivity.this, AudioActivity.class);
                    startActivity(audioIntent);
                    break;
                case 0:
                    Intent intent = new Intent(MainActivity.this, ImgeViewActivity.class);
                    startActivity(intent);
                    break;

                case 1:
//                    UwMediaPicker.Companion
//                            .with(this)                        // Activity or Fragment
//                            .setGalleryMode(UwMediaPicker.GalleryMode.VideoGallery) // GalleryMode: ImageGallery/VideoGallery/ImageAndVideoGallery, default is ImageGallery
//                            .setGridColumnCount(3)                                  // Grid column count, default is 3
//                            .setMaxSelectableMediaCount(1)                         // Maximum selectable media count, default is null which means infinite
//                            .setLightStatusBar(false)                                // Is llight status bar enable, default is true
//                            .enableImageCompression(false)
//                            .launch(new kotlin.jvm.functions.Function1<List<UwMediaPickerMediaModel>, kotlin.Unit>() {
//                                @Override
//                                public Unit invoke(List<UwMediaPickerMediaModel> uwMediaPickerMediaModels) {
//                                    Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
//                                    startActivity(intent1);
//                                    finish();
//                                    return null;
//                                }
//                            });// Is image compression enable, default is false

                    Intent videoIntent = new Intent(MainActivity.this, VideoActivity.class);
                    startActivity(videoIntent);
                    finish();

                    break;
                case 3:
                    Intent documentIntent = new Intent(MainActivity.this, DocumentActivity.class);
                    startActivity(documentIntent);
                    finish();
                    break;
                case 4:
                    Intent downloadIntent = new Intent(MainActivity.this, DownloadActivity.class);
                    startActivity(downloadIntent);
                    finish();
                    break;
                case 5:
                    Intent zipIntent = new Intent(MainActivity.this, ZipActivity.class);
                    startActivity(zipIntent);
                    finish();
                    break;
                case 7:
                    Intent appIntent = new Intent(MainActivity.this, AppActivity.class);
                    startActivity(appIntent);
                    finish();
                    break;
//                case R.id.navigation_feedback:
//                    gotoFeedback();
//                    break;
//
//                case R.id.navigation_settings:
//                    gotoSettings();
//                    break;
            }

            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }

    private void initRecyclerView() {
        recyclerAdapter = new RecyclerAdapter(this);
        recyclerAdapter.setOnItemClickListener(new OnItemClickListener(this));
        recyclerAdapter.setOnSelectionListener(new OnSelectionListener());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperSimpleCallback());
        recyclerAdapter.setItemTouchHelper(itemTouchHelper);

        if (type != null) {
            switch (type) {
                case "audio":
                    recyclerAdapter.setItemLayout(R.layout.list_item_1);
                    recyclerAdapter.setSpanCount(getResources().getInteger(R.integer.span_count1));
                    break;

                case "image":
                    recyclerAdapter.setItemLayout(R.layout.list_item_2);
                    recyclerAdapter.setSpanCount(getResources().getInteger(R.integer.span_count_3));

                    break;

                case "video":
                    recyclerAdapter.setItemLayout(R.layout.list_item_3);
                    recyclerAdapter.setSpanCount(getResources().getInteger(R.integer.span_count3));
                    break;
            }
        } else {
            recyclerAdapter.setItemLayout(R.layout.list_item_0);
            recyclerAdapter.setSpanCount(getResources().getInteger(R.integer.span_count0));
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(recyclerAdapter);
    }


    private void actionDelete() {
        actionDelete(recyclerAdapter.getSelectedItems());

        recyclerAdapter.clearSelection();
    }

    private void actionDelete(final List<File> files) {
        final File sourceDirectory = currentDirectory;

        recyclerAdapter.removeAll(files);

        String message = String.format("%s files deleted", files.size());

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentDirectory == null || currentDirectory.equals(sourceDirectory)) {
                    recyclerAdapter.addAll(files);
                }
            }
        };

        Snackbar.Callback callback = new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (event != DISMISS_EVENT_ACTION) {
                    try {
                        for (File file : files) FileUtils.deleteFile(file);
                    } catch (Exception e) {
                        showMessage(e);
                    }
                }

                super.onDismissed(snackbar, event);
            }
        };

        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG)
                .setAction("Undo", onClickListener)
                .setCallback(callback)
                .show();
    }

    private void actionRename() {
        final List<File> selectedItems = recyclerAdapter.getSelectedItems();

        InputDialog inputDialog = new InputDialog(this, "Rename", "Rename") {
            @Override
            public void onActionClick(String text) {
                recyclerAdapter.clearSelection();

                try {
                    if (selectedItems.size() == 1) {
                        File file = selectedItems.get(0);
                        int index = recyclerAdapter.indexOf(file);
                        recyclerAdapter.updateItemAt(index, FileUtils.renameFile(file, text));
                    } else {
                        int size = String.valueOf(selectedItems.size()).length();
                        String format = " (%0" + size + "d)";

                        for (int i = 0; i < selectedItems.size(); i++) {
                            File file = selectedItems.get(i);
                            int index = recyclerAdapter.indexOf(file);
                            File newFile = FileUtils.renameFile(file, text + String.format(format, i + 1));
                            recyclerAdapter.updateItemAt(index, newFile);
                        }
                    }
                } catch (Exception e) {
                    showMessage(e);
                }
            }
        };

        if (selectedItems.size() == 1) {
            inputDialog.setDefault(removeExtension(selectedItems.get(0).getName()));
        }

        inputDialog.show();
    }

    private void actionSearch() {
        InputDialog inputDialog = new InputDialog(this, "Search", "Search") {
            @Override
            public void onActionClick(String text) {
                setName(text);
            }
        };

        inputDialog.show();
    }

    private void actionCopy() {
        List<File> selectedItems = recyclerAdapter.getSelectedItems();
        recyclerAdapter.clearSelection();
        transferFiles(selectedItems, false);
    }

    private void actionMove() {
        List<File> selectedItems = recyclerAdapter.getSelectedItems();
        recyclerAdapter.clearSelection();
        transferFiles(selectedItems, true);
    }

    private void actionSend() {
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("*/*");
        ArrayList<Uri> uris = new ArrayList<>();
        for (File file : recyclerAdapter.getSelectedItems()) {
            if (file.isFile()) uris.add(Uri.fromFile(file));
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        startActivity(intent);
    }

    private void actionSort() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int checkedItem = PreferenceUtils.getInteger(this, "pref_sort", 0);
        String[] sorting = {"Name", "Last modified", "Size (high to low)"};
        final Context context = this;

        builder.setSingleChoiceItems(sorting, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recyclerAdapter.update(which);
                PreferenceUtils.putInt(context, "pref_sort", which);
                dialog.dismiss();

            }
        });

        builder.setTitle("Sort by");
        builder.show();
    }

    //----------------------------------------------------------------------------------------------

    private void transferFiles(final List<File> files, final Boolean delete) {
        String paste = delete ? "moved" : "copied";
        String message = String.format("%d items waiting to be %s", files.size(), paste);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    for (File file : files) {
                        recyclerAdapter.addAll(FileUtils.copyFile(file, currentDirectory));
                        if (delete) FileUtils.deleteFile(file);
                    }
                } catch (Exception e) {
                    showMessage(e);
                }
            }
        };

        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("Paste", onClickListener)
                .show();
    }

    private void showMessage(Exception e) {
        showMessage(e.getMessage());
    }

    private void showMessage(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    //----------------------------------------------------------------------------------------------

    private void gotoFeedback() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=com.calintat.explorer"));
        startActivity(intent);
    }

    private void gotoSettings() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    private void setPath(File directory) {
        if (!directory.exists()) {
            Toast.makeText(this, "Directory doesn't exist", Toast.LENGTH_SHORT).show();

            return;
        }

        currentDirectory = directory;
        recyclerAdapter.clear();
        recyclerAdapter.clearSelection();
        File[] children = FileUtils.getChildren(currentDirectory);
        if (children == null) return;
        if (children.length == 0) showMessage("Nothing here");
        recyclerAdapter.addAll(children);

//        invalidateTitle();
    }

    private void setName(String name) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_NAME, name);
        startActivity(intent);
    }

    private void setType(String type) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        if (Build.VERSION.SDK_INT >= 21) {
            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        }

        startActivity(intent);
    }

    //----------------------------------------------------------------------------------------------

    private final class ItemTouchHelperSimpleCallback extends ItemTouchHelper.SimpleCallback {
        public ItemTouchHelperSimpleCallback() {
            super(0, ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return !recyclerAdapter.anySelected() && recyclerAdapter.getSpanCount() == 1;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            File file = recyclerAdapter.get(viewHolder.getAdapterPosition());
            actionDelete(Collections.singletonList(file));
        }
    }

    private final class OnItemClickListener implements RecyclerOnItemClickListener {
        private final Context context;

        private OnItemClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onItemClick(int position) {
            final File file = recyclerAdapter.get(position);

            if (recyclerAdapter.anySelected()) {
                recyclerAdapter.toggle(position);
                return;
            }

            if (file.isDirectory()) {
                if (file.canRead()) {
                    setPath(file);
                } else {
                    showMessage("Cannot open directory");
                }
            } else {
                if (Intent.ACTION_GET_CONTENT.equals(getIntent().getAction())) {
                    Intent intent = new Intent();
                    intent.setDataAndType(Uri.fromFile(file), getMimeType(file));
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else if (FileType.getFileType(file) == FileType.ZIP) {
                    final ProgressDialog dialog = ProgressDialog.show(context, "", "Unzipping", true);

                    Thread thread = new Thread(() -> {
                        try {
                            setPath(unzip(file));
                            runOnUiThread(() -> dialog.dismiss());
                        } catch (Exception e) {
                            showMessage(e);
                        }
                    });

                    thread.run();
                } else {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(file), getMimeType(file));
                        startActivity(intent);
                    } catch (Exception e) {
                        showMessage(String.format("Cannot open %s", getName(file)));
                    }
                }
            }
        }

        @Override
        public boolean onItemLongClick(int position) {
            recyclerAdapter.toggle(position);

            return true;
        }
    }

    private final class OnSelectionListener implements RecyclerOnSelectionListener {
        @Override
        public void onSelectionChanged() {
            invalidateOptionsMenu();
        }
    }
}