package com.neonai.axocomplaints;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CreateTickets extends AppCompatActivity {

    Spinner compsubject;
    EditText compdescription;
    TextView usersid,submit,uploadImage,uploadVideo,selectPath,audioPath,uploadAudio;
    public static final String MY_PREFS_NAME = "Login_data";
    private static final String ROOT_URL = "https://tsfc.axolotlsapps.in/public/api/raiseANewTicket";
    private static final int REQUEST_PERMISSIONS = 100;
    private static final int PICK_IMAGE_REQUEST =1 ;
    private static final int REQUEST_IMAGE_CAPTURE = 4;
    private static final int RECORD_AUDIO_REQUEST = 8;
    private static final int SELECT_VIDEO =3 ;
    private static final int REQUEST_VIDEO_CAPTURE =6 ;
    private static final int SELECT_AUDIO =2 ;
    private Bitmap bitmap;
    private String filePath;
    ImageView imageView;
    String user_id;
    Activity context = CreateTickets.this;
    double latitude,longitude;
    String State,addressLone,compSub;
    Uri mVideoUri,mAudioUri;

    private static final int PERMISSION_CAMERA = 5;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 7;

    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    private String selectedPath="0",selectctedPaths;

    String[] subject = {"तक्रारीचा विषय निवडा","वैद्यकीय मदत","शेतकरी मदत","महावितरण","ग्रामपंचायत","पंचायत समिती","जिल्हा परिषद","तहसीलदार कार्यालय","जिल्हाधिकारी कार्यालय","पोलिस स्टेशन","शिक्षण मदत","हस्तांतरण","पीडब्लूडी","कॅबिनेट मंत्री मदत"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tickets);

        ImageView lefticon = findViewById(R.id.left_arrow_icon);
        TextView title1 = findViewById(R.id.toolbarTitle);

        compsubject = findViewById(R.id.edtSubject);
        compdescription = findViewById(R.id.edtDescription);
        submit = findViewById(R.id.btnTicketSubmit);
        uploadImage = findViewById(R.id.upload_attachment);
        imageView = findViewById(R.id.img_attachment);
        uploadVideo = findViewById(R.id.upload_Video);
        selectPath = findViewById(R.id.video_Path);
        uploadAudio = findViewById(R.id.upload_Audio);
        audioPath = findViewById(R.id.audio_Path);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        user_id = prefs.getString("id",null);

        fn_locationRequestforGPSCoordinates();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CreateTickets.this, R.layout.custom_spinner_item,subject);
        arrayAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
        compsubject.setAdapter(arrayAdapter);

        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

//        getActionBar().hide();


        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        compsubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                compSub = parent.getItemAtPosition(position).toString();
                Toast.makeText(CreateTickets.this,compSub,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lefticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(CreateTickets.this, "मागे", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(CreateTickets.this, DashBoardMain.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

            }
        });
        title1.setText("तक्रार नोंदवा");


        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
                    if ((ActivityCompat.shouldShowRequestPermissionRationale(CreateTickets.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(CreateTickets.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE))) {


                    } else {
                        ActivityCompat.requestPermissions(CreateTickets.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);
                    }
                } else {
                    Log.e("Else", "Else");
                    Log.d("123", "456");
                    ActivityCompat.requestPermissions(CreateTickets.this, new String[] {Manifest.permission.CAMERA}, PERMISSION_CAMERA);
                    showFileChooser();
                }
            }
        });

        uploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
                    if ((ActivityCompat.shouldShowRequestPermissionRationale(CreateTickets.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(CreateTickets.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE))) {

                    } else {
                        ActivityCompat.requestPermissions(CreateTickets.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);
                    }
                } else {
                    Log.e("Else", "Else");
                    ActivityCompat.requestPermissions(CreateTickets.this, new String[] {Manifest.permission.CAMERA}, PERMISSION_CAMERA);
                    showFileChoosers();
                }


            }
        });

        uploadAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    if ((ActivityCompat.shouldShowRequestPermissionRationale(CreateTickets.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(CreateTickets.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE))) {


                    } else {
                        ActivityCompat.requestPermissions(CreateTickets.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);
                    }
                } else {
                    Log.e("Else", "Else");
                    ActivityCompat.requestPermissions(CreateTickets.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
                    showFileChoose();
                }


            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String compdesc = compdescription.getText().toString();

                if (!compSub.isEmpty()) {
                    if (!compdesc.isEmpty()) {
                        compdescription.setError(null);

                            callPUTDataMethod(compSub,compdescription.getText().toString());

                    } else {
                            compdescription.setError("कृपया तक्रार संक्षिप्त प्रविष्ट करा");
                    }
                }else{
                    Toast.makeText(CreateTickets.this, "कृपया तक्रारीचा विषय निवडा", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "mr-IN");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//            case REQ_CODE_SPEECH_INPUT: {
//                if (resultCode == RESULT_OK && null != data) {
//
//                    ArrayList<String> result = data
//                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    txtSpeechInput.setText(result.get(0));
//                }
//                break;
//            }
//
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    public void showFileChoosers() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("व्हिडिओ अपलोड");
        builder.setMessage("तुम्हाला नवीन व्हिडिओ रेकॉर्ड करायचा आहे की विद्यमान व्हिडिओ निवडायचा आहे?");
        builder.setPositiveButton("रेकॉर्ड करा", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Open the camera app to record a new video
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
            }
        });
        builder.setNegativeButton("निवडा", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Open the file manager to select an existing video
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                startActivityForResult(intent, SELECT_VIDEO);
            }
        });
        builder.show();
    }

//    private void showFileChoosers() {
//        Intent intent = new Intent();
//        intent.setType("video/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
//    }

    public void showFileChoose() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ऑडिओ अपलोड");
        builder.setMessage("तुम्हाला नवीन ऑडिओ रेकॉर्ड करायचा आहे की विद्यमान ऑडिओ निवडायचा आहे?");
        builder.setPositiveButton("रेकॉर्ड करा", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Open the recorder app to record a new audio

                try {
                    // Launch the default audio recorder app
                    Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                    startActivityForResult(intent, RECORD_AUDIO_REQUEST);
                } catch (Exception e) {
                    // Show a message to the user that an audio recording app is not available
                    Toast.makeText(CreateTickets.this, "कोणतेही ऑडिओ रेकॉर्डिंग ॲप उपलब्ध नाही", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("निवडा", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Open the file manager to select an existing audio
                Intent intent = new Intent();
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, SELECT_AUDIO);
            }
        });
        builder.show();
    }

//    private void showFileChoose() {
//        Intent intent = new Intent();
//        intent.setType("audio/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select a Audio "), SELECT_AUDIO);
//    }

    private void showFileChooser() {
        // Create an array of options for the user to choose from
        String[] options = {"फोटो काढा", "मोबाइल मधून निवडा"};

        // Create a dialog to display the options
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("एक पर्याय निवडा");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // Option to take a new photo with the camera
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                } else if (which == 1) {
                    // Option to select a photo from the file manager
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "फोटो निवडा"), PICK_IMAGE_REQUEST);
                }
            }
        });
        builder.show();
    }

//    private void showFileChooser() {
////        Intent intent = new Intent();
////        intent.setType("image/*");
////        intent.setAction(Intent.ACTION_GET_CONTENT);
//        Log.e("321", "654");
//        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/*");
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    compdescription.setText(result.get(0));
                }
                break;
            }

        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                // Get the image captured by the camera
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            // Display the image in an ImageView or do something else with it
            imageView.setImageBitmap(bitmap);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri picUri = data.getData();
            filePath = getPath(picUri);
            if (filePath != null) {
                try {

                    Log.d("filePath", String.valueOf(filePath));
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
//                    uploadBitmap(bitmap);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(CreateTickets.this,"no image selected", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK)
        {
            Log.d("123", "axix");
            Uri aa = data.getData();
            selectedPath = getPat(aa);
            Log.d("123", "cv"+selectedPath);

//            if (selectedPath != null) {
            try {
                Log.d("filePath", "321");
                mVideoUri = Uri.parse(String.valueOf(aa));
                Log.d("filePath", "123");
//                    selectedPath = getPaths(aa);
                selectPath.setText(selectedPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //  }
        }

        if (requestCode == SELECT_VIDEO && resultCode == RESULT_OK)
        {
            Uri aa = data.getData();
            selectedPath = getPat(aa);
                try {
                    mVideoUri = Uri.parse(String.valueOf(aa));
                    selectPath.setText(selectedPath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

        if (requestCode == RECORD_AUDIO_REQUEST && resultCode == RESULT_OK)
        {
            try {
                Uri bb = data.getData();
                mAudioUri = Uri.parse(String.valueOf(bb));
                selectctedPaths = getPathhs(bb);
                audioPath.setText(selectctedPaths);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        if (requestCode == SELECT_AUDIO && resultCode == RESULT_OK)
        {
            try {
                Uri bb = data.getData();
                mAudioUri = Uri.parse(String.valueOf(bb));
                selectctedPaths = getPathh(bb);
                audioPath.setText(selectctedPaths);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        } return;

    }

    public String getPathhs(Uri uri) {
        String[] projection = {MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Video.Media._ID};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        String pat = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                String name = cursor.getString(nameIndex);
                if (name != null) {
                    pat = Environment.getExternalStorageDirectory().toString() + "/" + name;
                }
            }
            cursor.close();
        }
        return pat;
    }


    public String getPathh(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Audio.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String pathh = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
        cursor.close();

        return pathh;
    }

    public  String getPaths(Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }

    public String getPat(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media._ID};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        String pat = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
                String name = cursor.getString(nameIndex);
                if (name != null) {
                    pat = Environment.getExternalStorageDirectory().toString() + "/" + name;
                }
            }
            cursor.close();
        }
        return pat;
    }

//    public String getPaths(Uri uri) {
//        String[] proj = { MediaStore.Video.Media.DATA };
//        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
//        cursor.moveToFirst();
//        int column_index = cursor.getColumnIndexOrThrow( proj[0] );
//        String document_id = cursor.getString(column_index);
//        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
//        cursor.close();
//
//        cursor = getContentResolver().query(
//                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
//                null, MediaStore.Video.Media._ID + " = ? ", new String[]{document_id}, null);
//        cursor.moveToFirst();
//        String paths = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
//        cursor.close();
//
//        return paths;
//    }


    public  String getPath(Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void fn_locationRequestforGPSCoordinates() {
        com.google.android.gms.location.LocationRequest locationRequest = new com.google.android.gms.location.LocationRequest()
                .setInterval(500)
                .setFastestInterval(500)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationServices.getFusedLocationProviderClient(context)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        if (locationResult != null && locationResult.getLastLocation() != null) {
                            latitude = locationResult.getLastLocation().getLatitude();
                            longitude = locationResult.getLastLocation().getLongitude();
                          //  Toast.makeText(context, "location = "+latitude + ":" + longitude, Toast.LENGTH_SHORT).show();
                            Geocoder geocoder = new Geocoder(context, Locale.getDefault());

                            try {
                                List<Address> address = geocoder.getFromLocation(latitude, longitude, 1);
                                 addressLone = address.get(0).getAddressLine(0);
                                 State = address.get(0).getAdminArea();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, Looper.myLooper());
    }

    private void callPUTDataMethod(String compSub, String compldescription){

//        final ProgressDialog progressDialog = new ProgressDialog(CreateTickets.this);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Uploading...");
//        progressDialog.show();

        String compSubj=StringFormatter.convertStringToUTF8(compSub);
        String compDescription=StringFormatter.convertStringToUTF8(compldescription);
        int maxVideoSize = 50000000;

        File videoFile = new File(selectedPath);
        if (videoFile.length() <= maxVideoSize) {
            final ProgressDialog progressDialog = new ProgressDialog(CreateTickets.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("तक्रार दाखल होत आहे...");
            progressDialog.show();

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ROOT_URL,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {

                            compdescription.setText("");

                            progressDialog.dismiss();
                            Intent i = new Intent(CreateTickets.this, ThanksPage.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);

                            Toast.makeText(CreateTickets.this, "तक्रार दाखल झाली..", Toast.LENGTH_SHORT).show();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(CreateTickets.this, "तक्रार दाखल झाली नाही..", Toast.LENGTH_SHORT).show();
                        }
                    }) {

                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    Log.d("comSub", " " + compSub);

                    params.put("subject", compSubj);
                    params.put("ticket_description", compDescription);
                    params.put("user_id", user_id);
                    params.put("location", addressLone);

                    return params;
                }


                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    long imagename = System.currentTimeMillis();
                    if (mVideoUri != null) {
                        params.put("vedio", new DataPart("file_avatar.mp4", UploadHelper.getFileDataFromDrawable(getApplicationContext(), mVideoUri)));
                    }
                    if (mAudioUri != null) {
                        params.put("audio", new DataPart("file_avatars.mp3", UploadHelper.getFileDataFromDrawable(getApplicationContext(), mAudioUri)));
                    }
                    if (bitmap != null) {
                        params.put("file", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                    }
                    return params;
                }
            };
            Volley.newRequestQueue(this).add(volleyMultipartRequest);
        } else {
            Toast.makeText(CreateTickets.this, "50MB च्या आत व्हिडिओ निवडा..", Toast.LENGTH_SHORT).show();
        }
    }
}

