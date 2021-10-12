package com.calintat.explorer.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.calintat.explorer.Model.DataModel;
import com.calintat.explorer.Model.DocumentModel;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DownloadData {

    public static String SELECT_STORAGE_TYPE = "internal";
    public static String DRIVE_LOAD_LINK = "https://docs.google.com/uc?id=";
    public static String dirNamedir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
    public static String docDirNamedir = Environment.getExternalStorageDirectory().toString();
    public static String InternDirNamedir = Environment.getStorageDirectory().toString();
    public static String InternDownDirNamedir = Environment.getStorageDirectory().getAbsolutePath();

    public static ArrayList<DataModel> getAllData(ArrayList<DataModel> allDataList) {

        File dir = new File(dirNamedir);
        boolean success = true;

        if (success && dir.isDirectory()) {
            File[] listFile = dir.listFiles();
            for (int i = 0; i < listFile.length; i++) {
                DataModel dataModel = new DataModel();
                dataModel.setFilename(listFile[i].getName());
                dataModel.setSize(listFile[i].length());
                dataModel.setFilePath(listFile[i].getAbsolutePath());
                dataModel.setDirectory(listFile[i].isDirectory());
                allDataList.add(dataModel);
            }

        }

        Collections.reverse(allDataList);
        return allDataList;
    }

    public static ArrayList<DataModel> getAllRecentData(ArrayList<DataModel> allDataList) {

        File dir = new File(dirNamedir);
        boolean success = true;

        if (success && dir.isDirectory()) {
            File[] listFile = dir.listFiles();
            if (listFile != null) {
                for (int i = 0; i < listFile.length; i++) {

                    SimpleDateFormat df = new SimpleDateFormat("MMM-yyyy", Locale.getDefault());

                    Date c = Calendar.getInstance().getTime();
                    String formattedDate = df.format(c);

                    Date date1 = new Date(listFile[i].lastModified());
                    String formattedDate1 = df.format(date1);

                    Log.e("LLLLL_Date_Log: ", formattedDate + "        " + formattedDate1);

                    if (formattedDate.equals(formattedDate1)) {
                        DataModel dataModel = new DataModel();
                        dataModel.setFilename(listFile[i].getName());
                        dataModel.setSize(listFile[i].length());
                        dataModel.setFilePath(listFile[i].getAbsolutePath());
                        dataModel.setDirectory(listFile[i].isDirectory());
                        allDataList.add(dataModel);
                    }
                }
            }

        }

        Collections.reverse(allDataList);
        return allDataList;
    }

    public static ArrayList<DataModel> getAllImageData(String dirName, ArrayList<DataModel> imagesDataModel) {

        File dir = new File(dirName);
        boolean success = true;
        if (success && dir.isDirectory()) {
            File[] listFile = dir.listFiles();
            if (listFile != null) {
                for (int i = 0; i < listFile.length; i++) {
                    if (listFile[i].isFile()) {
                        if (listFile[i].getAbsolutePath().contains(".jpg") || listFile[i].getAbsolutePath().contains(".JPG")
                                || listFile[i].getAbsolutePath().contains(".jpeg") || listFile[i].getAbsolutePath().contains(".JPEG")
                                || listFile[i].getAbsolutePath().contains(".png") || listFile[i].getAbsolutePath().contains(".PNG")
                                || listFile[i].getAbsolutePath().contains(".gif") || listFile[i].getAbsolutePath().contains(".GIF")
                                || listFile[i].getAbsolutePath().contains(".bmp") || listFile[i].getAbsolutePath().contains(".BMP")
                        ) {
                            DataModel dataModel = new DataModel();
                            dataModel.setFilename(listFile[i].getName());
                            dataModel.setSize(listFile[i].length());
                            dataModel.setFilePath(listFile[i].getAbsolutePath());
                            imagesDataModel.add(dataModel);
                        }
                    } else if (listFile[i].isDirectory()) {
                        getAllImageData(listFile[i].getAbsolutePath(), imagesDataModel);
                    }
                }
            }
        }


        Collections.reverse(imagesDataModel);
        return imagesDataModel;
    }

    public static ArrayList<DocumentModel> getInternData(File filePath) {

        ArrayList<DocumentModel> documentModels = new ArrayList<>();
        File[] listFile = filePath.listFiles();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                File imagePath = listFile[i];
                String path = imagePath.getAbsolutePath();

                if (path.endsWith(".jpg") || path.endsWith(".JPG")
                        || path.endsWith(".jpeg") || path.endsWith(".JPEG")
                        || path.endsWith(".png") || path.endsWith(".PNG")
                        || path.endsWith(".gif") || path.endsWith(".GIF")
                        || path.endsWith(".bmp") || path.endsWith(".BMP")
                ) {

                    DocumentModel documentModel = new DocumentModel();
                    documentModel.setFileName(imagePath.getName());
                    documentModel.setFolderName(imagePath.getName());
                    documentModel.setFilePath(imagePath.getAbsolutePath());
                    documentModel.setLength(imagePath.length());

                    documentModels.add(documentModel);


                }
            }
        }

        Collections.reverse(documentModels);
        return documentModels;
    }


    public static ArrayList<DocumentModel> getInternVidData(File filePath) {

        ArrayList<DocumentModel> documentModels = new ArrayList<>();
        File[] listFile = filePath.listFiles();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                File imagePath = listFile[i];
                String path = imagePath.getAbsolutePath();

                if (listFile[i].getAbsolutePath().contains(".mp4") ||
                        listFile[i].getAbsolutePath().contains(".mkv")) {

                    DocumentModel documentModel = new DocumentModel();
                    documentModel.setFileName(imagePath.getName());
                    documentModel.setFolderName(imagePath.getName());
                    documentModel.setFilePath(imagePath.getAbsolutePath());
                    documentModel.setLength(imagePath.length());

                    documentModels.add(documentModel);


                }
            }
        }

        Collections.reverse(documentModels);
        return documentModels;
    }

    public static ArrayList<DocumentModel> getInternMusicData(File filePath) {

        ArrayList<DocumentModel> documentModels = new ArrayList<>();
        File[] listFile = filePath.listFiles();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                File imagePath = listFile[i];
                String path = imagePath.getAbsolutePath();

                if (listFile[i].getAbsolutePath().contains(".mp3") ||
                        listFile[i].getAbsolutePath().contains(".wav") ||
                        listFile[i].getAbsolutePath().contains(".ogg")) {

                    DocumentModel documentModel = new DocumentModel();
                    documentModel.setFileName(imagePath.getName());
                    documentModel.setFolderName(imagePath.getName());
                    documentModel.setFilePath(imagePath.getAbsolutePath());
                    documentModel.setLength(imagePath.length());

                    documentModels.add(documentModel);

                }
            }
        }

        Collections.reverse(documentModels);
        return documentModels;
    }

    public static ArrayList<DocumentModel> getInternDocData(File filePath) {

        ArrayList<DocumentModel> documentModels = new ArrayList<>();
        File[] listFile = filePath.listFiles();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                File imagePath = listFile[i];
                String path = imagePath.getAbsolutePath();

                if (listFile[i].getAbsolutePath().contains(".docx") ||
                        listFile[i].getAbsolutePath().contains(".pdf") ||
                        listFile[i].getAbsolutePath().contains(".txt") ||
                        listFile[i].getAbsolutePath().contains(".xml") ||
                        listFile[i].getAbsolutePath().contains(".ppt") ||
                        listFile[i].getAbsolutePath().contains(".pptx") ||
                        listFile[i].getAbsolutePath().contains(".html")) {

                    DocumentModel documentModel = new DocumentModel();
                    documentModel.setFileName(imagePath.getName());
                    documentModel.setFolderName(imagePath.getName());
                    documentModel.setFilePath(imagePath.getAbsolutePath());
                    documentModel.setLength(imagePath.length());

                    documentModels.add(documentModel);

                }
            }
        }

        Collections.reverse(documentModels);
        return documentModels;
    }

    public static ArrayList<DocumentModel> getInternAppData(File filePath) {

        ArrayList<DocumentModel> documentModels = new ArrayList<>();
        File[] listFile = filePath.listFiles();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                File imagePath = listFile[i];
                String path = imagePath.getAbsolutePath();

                if (listFile[i].getAbsolutePath().contains(".apk") ||
                        listFile[i].getAbsolutePath().contains(".aab")) {

                    DocumentModel documentModel = new DocumentModel();
                    documentModel.setFileName(imagePath.getName());
                    documentModel.setFolderName(imagePath.getName());
                    documentModel.setFilePath(imagePath.getAbsolutePath());
                    documentModel.setLength(imagePath.length());

                    documentModels.add(documentModel);

                }
            }
        }

        Collections.reverse(documentModels);
        return documentModels;
    }

    public static ArrayList<DocumentModel> getInternZipData(File filePath) {

        ArrayList<DocumentModel> documentModels = new ArrayList<>();
        File[] listFile = filePath.listFiles();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                File imagePath = listFile[i];
                String path = imagePath.getAbsolutePath();

                if (listFile[i].getAbsolutePath().contains(".zip")) {

                    DocumentModel documentModel = new DocumentModel();
                    documentModel.setFileName(imagePath.getName());
                    documentModel.setFolderName(imagePath.getName());
                    documentModel.setFilePath(imagePath.getAbsolutePath());
                    documentModel.setLength(imagePath.length());

                    documentModels.add(documentModel);

                }
            }
        }

        Collections.reverse(documentModels);
        return documentModels;
    }

    public static ArrayList<DataModel> getAllVideoData(String dirName, ArrayList<DataModel> videoDataList) {

        File dir = new File(dirName);
        boolean success = true;
        if (success && dir.isDirectory()) {
            File[] listFile = dir.listFiles();
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isFile()) {
                    if (listFile[i].getAbsolutePath().contains(".mp4") ||
                            listFile[i].getAbsolutePath().contains(".mkv")) {
                        DataModel dataModel = new DataModel();
                        dataModel.setFilename(listFile[i].getName());
                        dataModel.setSize(listFile[i].length());
                        dataModel.setFilePath(listFile[i].getAbsolutePath());
                        videoDataList.add(dataModel);
                    }
                } else if (listFile[i].isDirectory()) {
                    getAllVideoData(listFile[i].getAbsolutePath(), videoDataList);
                }
            }
        }
        Collections.reverse(videoDataList);
        return videoDataList;
    }

    public static ArrayList<DataModel> getAllDocData(String filePath, ArrayList<DataModel> docDataList) {

        File dir = new File(filePath);
        boolean success = true;
        if (success && dir.isDirectory()) {
            File[] listFile = dir.listFiles();
            if (listFile != null) {
                for (int i = 0; i < listFile.length; i++) {
                    if (listFile[i].isFile()) {
                        if (listFile[i].getAbsolutePath().contains(".docx") ||
                                listFile[i].getAbsolutePath().contains(".pdf") ||
                                listFile[i].getAbsolutePath().contains(".txt") ||
                                listFile[i].getAbsolutePath().contains(".xml") ||
                                listFile[i].getAbsolutePath().contains(".ppt") ||
                                listFile[i].getAbsolutePath().contains(".pptx") ||
                                listFile[i].getAbsolutePath().contains(".html")) {
                            DataModel dataModel = new DataModel();
                            dataModel.setFilename(listFile[i].getName());
                            dataModel.setSize(listFile[i].length());
                            dataModel.setFilePath(listFile[i].getAbsolutePath());
                            docDataList.add(dataModel);
                        }
                    } else if (listFile[i].isDirectory()) {
                        getAllDocData(listFile[i].getAbsolutePath(), docDataList);
                    }
                }
            }
        }

        Collections.reverse(docDataList);
        return docDataList;
    }

    public static ArrayList<DataModel> getAllAudData(String filePath, ArrayList<DataModel> docDataList) {

        File dir = new File(filePath);
        boolean success = true;
        if (success && dir.isDirectory()) {
            File[] listFile = dir.listFiles();
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isFile()) {
                    if (listFile[i].getAbsolutePath().contains(".mp3") ||
                            listFile[i].getAbsolutePath().contains(".wav") ||
                            listFile[i].getAbsolutePath().contains(".ogg")) {
                        DataModel dataModel = new DataModel();
                        dataModel.setFilename(listFile[i].getName());
                        dataModel.setSize(listFile[i].length());
                        dataModel.setFilePath(listFile[i].getAbsolutePath());
                        docDataList.add(dataModel);
                    }
                } else if (listFile[i].isDirectory()) {
                    getAllDocData(listFile[i].getAbsolutePath(), docDataList);
                }
            }
        }

        Collections.reverse(docDataList);
        return docDataList;
    }

    public static ArrayList<DataModel> getOtherDownloadData(String filePath, ArrayList<DataModel> docDataList) {

        File dir = new File(filePath);
        boolean success = true;
        if (success && dir.isDirectory()) {
            File[] listFile = dir.listFiles();
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isFile()) {
                    if (listFile[i].getAbsolutePath().contains(".")) {
                        String extension = listFile[i].getAbsolutePath().substring(listFile[i].getAbsolutePath().lastIndexOf("."));

                        if (!(extension.equals(".mp3") ||
                                extension.equals(".wav") ||
                                extension.equals(".ogg") ||
                                extension.equals(".docx") ||
                                extension.equals(".pdf") ||
                                extension.equals(".html") ||
                                extension.equals(".txt") ||
                                extension.equals(".xml") ||
                                extension.equals(".ppt") ||
                                extension.equals(".pptx") ||
                                extension.equals(".mp4") ||
                                extension.equals(".mkv") ||
                                extension.equals(".png") ||
                                extension.equals(".jpeg") ||
                                extension.equals(".JPG") ||
                                extension.equals(".CR2") ||
                                extension.equals(".webp") ||
                                extension.equals(".jpg"))) {
                            Log.e("LLLLL_Data: ", listFile[i].getAbsolutePath().substring(listFile[i].getAbsolutePath().lastIndexOf(".")));
                            DataModel dataModel = new DataModel();
                            dataModel.setFilename(listFile[i].getName());
                            dataModel.setSize(listFile[i].length());
                            dataModel.setFilePath(listFile[i].getAbsolutePath());
                            dataModel.setDirectory(listFile[i].isDirectory());
                            docDataList.add(dataModel);
                        }
                    }
                } else if (listFile[i].isDirectory()) {
                    getOtherDownloadData(listFile[i].getAbsolutePath(), docDataList);
                }
            }
        }

        Collections.reverse(docDataList);
        return docDataList;
    }

    public static ArrayList<DocumentModel> getDocumentsData(String filePath) {

        ArrayList<DocumentModel> docDataList = new ArrayList<>();
        File dir = new File(filePath);
        boolean success = true;
        if (success && dir.isDirectory()) {
            File[] listFile = dir.listFiles();
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isFile()) {
                    if (listFile[i].getAbsolutePath().contains(".docx") ||
                            listFile[i].getAbsolutePath().contains(".pdf") ||
                            listFile[i].getAbsolutePath().contains(".txt") ||
                            listFile[i].getAbsolutePath().contains(".xml") ||
                            listFile[i].getAbsolutePath().contains(".ppt") ||
                            listFile[i].getAbsolutePath().contains(".pptx") ||
                            listFile[i].getAbsolutePath().contains(".html")) {

                        DocumentModel documentModel = new DocumentModel();
                        documentModel.setFileName(listFile[i].getName());
                        documentModel.setFolderName(listFile[i].getParentFile().getName());
                        documentModel.setFilePath(listFile[i].getAbsolutePath());
                        documentModel.setLength(listFile[i].length());

                        docDataList.add(documentModel);
                    }
                } else if (listFile[i].isDirectory()) {
                    getDocumentsData(listFile[i].getAbsolutePath());
                }
            }
        }

        Collections.reverse(docDataList);
        return docDataList;
    }

    public static ArrayList<DocumentModel> getDocumentsFolder(String filePath, ArrayList<DocumentModel> docDataList) {

        File dir = new File(filePath);
        boolean success = true;
        if (success && dir.isDirectory()) {
            File[] listFile = dir.listFiles();
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isFile()) {
                    if (listFile[i].getAbsolutePath().contains(".docx") ||
                            listFile[i].getAbsolutePath().contains(".pdf") ||
                            listFile[i].getAbsolutePath().contains(".txt") ||
                            listFile[i].getAbsolutePath().contains(".xml") ||
                            listFile[i].getAbsolutePath().contains(".ppt") ||
                            listFile[i].getAbsolutePath().contains(".pptx") ||
                            listFile[i].getAbsolutePath().contains(".html")) {

                        DocumentModel documentModel = new DocumentModel();
                        documentModel.setFileName(listFile[i].getName());
                        documentModel.setFolderName(listFile[i].getParentFile().getName());
                        documentModel.setFilePath(listFile[i].getParentFile().getAbsolutePath());
                        documentModel.setLength(listFile[i].length());

                        docDataList.add(documentModel);

                    }
                } else if (listFile[i].isDirectory()) {
                    getDocumentsFolder(listFile[i].getAbsolutePath(), docDataList);
                }
            }
        }

        Collections.reverse(docDataList);
        return docDataList;
    }

    public static ArrayList<DocumentModel> getZipFolder(String filePath, ArrayList<DocumentModel> docDataList) {

        File dir = new File(filePath);
        boolean success = true;
        if (success && dir.isDirectory()) {
            File[] listFile = dir.listFiles();
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isFile()) {
                    if (listFile[i].getAbsolutePath().contains(".zip")) {

                        DocumentModel documentModel = new DocumentModel();
                        documentModel.setFileName(listFile[i].getName());
                        documentModel.setFolderName(listFile[i].getParentFile().getName());
                        documentModel.setFilePath(listFile[i].getParentFile().getAbsolutePath());
                        documentModel.setLength(listFile[i].length());
//                        if (i > 0) {
//                            if (!listFile[i - 1].getParentFile().getName().equals(listFile[i].getParentFile().getName())) {
//                                docDataList.add(documentModel);
//                            }
//                        } else {
                        docDataList.add(documentModel);
//                        }

                    }
                } else if (listFile[i].isDirectory()) {
                    getZipFolder(listFile[i].getAbsolutePath(), docDataList);
                }
            }
        }

        Collections.reverse(docDataList);
        return docDataList;
    }

    public static ArrayList<DocumentModel> getZipData(String filePath) {

        ArrayList<DocumentModel> docDataList = new ArrayList<>();
        File dir = new File(filePath);
        boolean success = true;
        if (success && dir.isDirectory()) {
            File[] listFile = dir.listFiles();
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isFile()) {
                    if (listFile[i].getAbsolutePath().contains(".zip")) {

                        DocumentModel documentModel = new DocumentModel();
                        documentModel.setFileName(listFile[i].getName());
                        documentModel.setFolderName(listFile[i].getParentFile().getName());
                        documentModel.setFilePath(listFile[i].getAbsolutePath());
                        documentModel.setLength(listFile[i].length());

                        docDataList.add(documentModel);
                    }
                } else if (listFile[i].isDirectory()) {
                    getZipData(listFile[i].getAbsolutePath());
                }
            }
        }

        Collections.reverse(docDataList);
        return docDataList;
    }

    public static ArrayList<DocumentModel> getAppFolder(String filePath, ArrayList<DocumentModel> docDataList) {

        File dir = new File(filePath);
        boolean success = true;
        if (success && dir.isDirectory()) {
            File[] listFile = dir.listFiles();
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isFile()) {
                    if (listFile[i].getAbsolutePath().contains(".apk") ||
                            listFile[i].getAbsolutePath().contains(".aab")) {

                        DocumentModel documentModel = new DocumentModel();
                        documentModel.setFileName(listFile[i].getName());
                        documentModel.setFolderName(listFile[i].getParentFile().getName());
                        documentModel.setFilePath(listFile[i].getParentFile().getAbsolutePath());
                        documentModel.setLength(listFile[i].length());

                        docDataList.add(documentModel);

                    }
                } else if (listFile[i].isDirectory()) {
                    getAppFolder(listFile[i].getAbsolutePath(), docDataList);
                }
            }
        }

        Collections.reverse(docDataList);
        return docDataList;
    }

    public static ArrayList<DocumentModel> getAppData(String filePath) {

        ArrayList<DocumentModel> docDataList = new ArrayList<>();
        File dir = new File(filePath);
        boolean success = true;
        if (success && dir.isDirectory()) {
            File[] listFile = dir.listFiles();
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isFile()) {
                    if (listFile[i].getAbsolutePath().contains(".apk") ||
                            listFile[i].getAbsolutePath().contains(".aab")) {

                        DocumentModel documentModel = new DocumentModel();
                        documentModel.setFileName(listFile[i].getName());
                        documentModel.setFolderName(listFile[i].getParentFile().getName());
                        documentModel.setFilePath(listFile[i].getAbsolutePath());
                        documentModel.setLength(listFile[i].length());

                        docDataList.add(documentModel);
                    }
                } else if (listFile[i].isDirectory()) {
                    getAppData(listFile[i].getAbsolutePath());
                }
            }
        }

        Collections.reverse(docDataList);
        return docDataList;
    }

    public static ArrayList<DocumentModel> getVideoFolder(String filePath, ArrayList<DocumentModel> docDataList) {

        File dir = new File(filePath);
        boolean success = true;
        if (success && dir.isDirectory()) {
            File[] listFile = dir.listFiles();
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isFile()) {
                    if (listFile[i].getAbsolutePath().contains(".mp4") ||
                            listFile[i].getAbsolutePath().contains(".mkv")) {

                        DocumentModel documentModel = new DocumentModel();
                        documentModel.setFileName(listFile[i].getName());
                        documentModel.setFolderName(listFile[i].getParentFile().getName());
                        documentModel.setFilePath(listFile[i].getParentFile().getAbsolutePath());
                        documentModel.setLength(listFile[i].length());

                        docDataList.add(documentModel);

                    }
                } else if (listFile[i].isDirectory()) {
                    getVideoFolder(listFile[i].getAbsolutePath(), docDataList);
                }
            }
        }

        Collections.reverse(docDataList);
        return docDataList;
    }

    public static ArrayList<DocumentModel> getVideoData(String filePath) {

        ArrayList<DocumentModel> docDataList = new ArrayList<>();
        File dir = new File(filePath);
        boolean success = true;
        if (success && dir.isDirectory()) {
            File[] listFile = dir.listFiles();
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isFile()) {
                    if (listFile[i].getAbsolutePath().contains(".mp4") ||
                            listFile[i].getAbsolutePath().contains(".mkv")) {

                        DocumentModel documentModel = new DocumentModel();
                        documentModel.setFileName(listFile[i].getName());
                        documentModel.setFolderName(listFile[i].getParentFile().getName());
                        documentModel.setFilePath(listFile[i].getAbsolutePath());
                        documentModel.setLength(listFile[i].length());

                        docDataList.add(documentModel);
                    }
                } else if (listFile[i].isDirectory()) {
                    getVideoData(listFile[i].getAbsolutePath());
                }
            }
        }

        Collections.reverse(docDataList);
        return docDataList;
    }

    @Nonnull
    public static String getCurrentTimestamp() {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    @Nonnull
    public static String readExternalFileContent(Context context, String fileName)
            throws IOException {
        java.io.File file = new java.io.File(context.getExternalFilesDir(null), fileName);
        StringBuilder result = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        while ((line = br.readLine()) != null) {
            result.append(line);
            result.append('\n');
        }
        br.close();

        return result.toString();
    }

    @Nullable
    public static java.io.File createExternalFileContent(Context context, String fileName, String content)
            throws IOException {
        if (!isExternalStorageWritable()) {
            return null;
        }

        java.io.File file = new java.io.File(context.getExternalFilesDir(null), fileName);
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(content.getBytes());
        outputStream.close();

        return file;
    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }


    public Bitmap loadBitmap(String url) {
        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 8192);
            bm = BitmapFactory.decodeStream(bis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bm;
    }

    /**
     * helper class for Drive API operations
     * all public methods are executed SYNCHRONOUSLY, and must be called in background thread
     */


//    public static class DriveApiUtils {
//
//        @Nullable
//        private static FileList getFileList(Drive service, String q)
//                throws UserRecoverableAuthIOException, GoogleJsonResponseException {
//            FileList fileList = null;
//            try {
//                Drive.Files.List list = service.files().list();
//                if (q != null && q.length() > 0) {
//                    list.setQ(q);
//                }
//                fileList = list.execute();
//            } catch (UserRecoverableAuthIOException | GoogleJsonResponseException e) {
//                throw e;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return fileList;
//        }
//
//        @Nonnull
//        public static String getSharedWithMeFolder(Drive service, String folderName)
//                throws UserRecoverableAuthIOException, GoogleJsonResponseException {
//            String folderId = "";
//            FileList fileList = getFileList(service, "sharedWithMe = true");
//            if (fileList != null) {
//                for (com.google.api.services.drive.model.File file : fileList.getFiles()) {
//                    if (file.getName().equals(folderName)) {
//                        folderId = file.getId();
//                        break;
//                    }
//                }
//            }
//            return folderId;
//        }
//
//        @Nonnull
//        public static ArrayList<String> getSharedWithMeFileList(Drive service, String folderId)
//                throws UserRecoverableAuthIOException, GoogleJsonResponseException {
//            String q = String.format("'%s' in parents", folderId);
//            FileList fileList = getFileList(service, q);
//
//            ArrayList<String> nameList = new ArrayList<>();
//            if (fileList != null) {
//                int totalFiles = fileList.getFiles().size();
//                nameList = new ArrayList<>(totalFiles);
//                for (int i = 0; i < totalFiles; i++) {
//                    String fileName = fileList.getFiles().get(i).getName();
//                    nameList.add(fileName);
//                }
//            }
//            return nameList;
//        }
//
//        @Nonnull
//        public static String getFileId(Drive service, String folderId, String fileName)
//                throws UserRecoverableAuthIOException, GoogleJsonResponseException {
//            String q = String.format("'%s' in parents and name='%s'", folderId, fileName);
//            FileList fileList = getFileList(service, q);
//            if (fileList != null && !fileList.getFiles().isEmpty()) {
//                return fileList.getFiles().get(0).getId();
//            }
//            return "";
//        }
//
//        @Nullable
//        public static String readFileContent(Drive service, @NonNull String fileId) {
//            try {
//                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//                service.files().get(fileId).executeMediaAndDownloadTo(outputStream);
//                InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
//                return convertInputStreamToString(is);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Nullable
//        private static String convertInputStreamToString(@Nonnull InputStream inputStream) {
//            try {
//                ByteArrayOutputStream result = new ByteArrayOutputStream();
//                byte[] buffer = new byte[1024];
//                int length;
//                while ((length = inputStream.read(buffer)) != -1) {
//                    result.write(buffer, 0, length);
//                }
//                return result.toString("UTF-8");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Nullable
//        public static com.google.api.services.drive.model.File updateFile(Drive service, @Nonnull String fileId, @Nonnull java.io.File newContent)
//                throws UserRecoverableAuthIOException, GoogleJsonResponseException {
//            com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
//            fileMetadata.setName(newContent.getName());
//            FileContent mediaContent = new FileContent("text/plain", newContent);
//            try {
//                return service.files().update(fileId, fileMetadata, mediaContent)
//                        .setFields("id")
//                        .execute();
//            } catch (UserRecoverableAuthIOException | GoogleJsonResponseException e) {
//                throw e;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Nullable
//        public static com.google.api.services.drive.model.File createNewFile(Drive service, @Nonnull String folderId, @Nonnull java.io.File file)
//                throws UserRecoverableAuthIOException, GoogleJsonResponseException {
//            com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
//            fileMetadata.setName(file.getName());
//            fileMetadata.setParents(Collections.singletonList(folderId));
//            FileContent mediaContent = new FileContent("text/plain", file);
//            try {
//                return service.files().create(fileMetadata, mediaContent)
//                        .setFields("id, parents")
//                        .execute();
//            } catch (UserRecoverableAuthIOException | GoogleJsonResponseException e) {
//                throw e;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        public static void deleteFile(Drive service, @Nonnull String fileId)
//                throws UserRecoverableAuthIOException, GoogleJsonResponseException {
//            try {
//                service.files().delete(fileId).execute();
//            } catch (UserRecoverableAuthIOException | GoogleJsonResponseException e) {
//                throw e;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        public static void trashFile(Drive service, @Nonnull String fileId)
//                throws UserRecoverableAuthIOException, GoogleJsonResponseException {
//            try {
//                com.google.api.services.drive.model.File newContent = new com.google.api.services.drive.model.File();
//                newContent.setTrashed(true);
//                service.files().update(fileId, newContent).execute();
//            } catch (UserRecoverableAuthIOException | GoogleJsonResponseException e) {
//                throw e;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

}
