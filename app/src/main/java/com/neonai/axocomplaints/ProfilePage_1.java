package com.neonai.axocomplaints;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.neonai.axocomplaints.databinding.FragmentLogOutBinding;
import com.neonai.axocomplaints.smpleclasses.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilePage_1 extends AppCompatActivity {


    private static final String ROOT_URL = "https://tanajisawant.org/public/api/raiseANewTicket";
    private static final int REQUEST_PERMISSIONS = 100;
    private static final int PICK_IMAGE_REQUEST =1 ;
    private Bitmap bitmap;
    private String filePath;
    public static final String MY_PREFS_NAME = "Login_data";
    String user_id,Taluka_,Gaon_;
    int year;
    int month;
    int day;
    EditText userfirstname, usermiddlename, userlastname, useraddress, userpanno, useradhharno, useremail, userbirthdate, usertaluka, uservoterno, uservoterlistno, userwardno, usergaon, useraltphoneno;
    TextView userdistrict, userstate, userphoneno, userid;
    Button registerbutton;
    RadioGroup radioGroup;
    RadioButton rb;
    boolean isSelected = false;
    CircleImageView profileImage;
    Spinner spinnerTaluka,spinnerGaon;

    String[] taluka = {"भूम","परांडा","वाशी"};

    String[] gaon = {"अंबी","आनंदवाडी","आंद्रुड","अंजन सोडा","अंतरगाव","अंतरवली","अरसोली","आष्टा","अष्टेवाडी","बागलवाडी","बर्‍हाणपूर","बावी","बेदरवाडी","बेळगाव","भवानवाडी","भवानवाडी","भोगलगाव","भोनगिरी","भूम","बिरोबाचीवाडी",
            "बुरुडवाडी","चांदवड","चिंचोली","चिंचपूर","चुंबळी","दांडेगाव","देवळाली","देवांग्रा","दिंडोरी","डोकेवाडी","दुधोडी","डुक्करवाडी","गणेगाव","घाटनांदूर","गिखली","गिरळगाव","गोळेगाव","गोरमाला","हाडोंगी","हांगेवाडी","हिवरा","हिवरडा","इडा",
            "लीट","इराचीवाडी","जांब","जयवंतनगर","जेजला","जोतिबाचीवाडी","कानडी","कासारी","कृष्णापूर","लांजेश्वर","मालेवाडी","माणकेश्वर","मात्रेवाडी","नागेवाडी","नली","नळीवडगाव","नान्नजवाडी","नवलगाव","निपाणी","पडोळी","पाखरुड",
            "पांढरेवाडी","पन्हाळवाडी","पाथरुड","पतसांगवी","पिडा","पिंपळगाव","राळेसांगवी","रामेश्वर","रामकुंड","रोसांबा","साडेसांगवी","सामनगाव","सानेवाडी","सावरगाव","सावरगाव","शेखापूर","सोनगिरी","सुक्त","तांबेवाडी","तींतराज","उलुप",
            "उमाचीवाडी","वडाचीवाडी","वाकवड","वाल्हा","वालवड","वांगी बुद्रुक.","वांगी ख.","वंजारवाडी","वारेवडगाव","वरुड","ऐनापूरवाडी","अलेश्वर","अनाला","अंधोरा","अंधोरी","अरणगाव","आसू","आवार पिंपरी","बंगलवाडी","बावची","भांडगाव"
            ,"भोईंजा","भोत्रा","बोडाखा","ब्रह्मगाव","चिंचपूर बुद्रुक.","चिंचपूर ख.","दहिताना","देवगाव बुद्रुक.","देवगाव ख.","देऊळगाव","धगपिंप्री","धोत्री","डोमगाव","डोंजा","दुधी","घारगाव","गोसावीवाडी","गोसावीवाडी","हिंगणगाव बुद्रुक."
            ,"हिंगणगाव ख.","इंगोडा","जगदलवाडी","जकाते वाडी","जळके पिंपरी","जामगाव","जावळा","काळेवाडी","कंडारी","कंगलगाव","कपिलापुरी","कारंजा","कारला","काटेवाडी","कात्राबाद","कौंडगाव","खानापूर","खांदेश्वर वाडी","खासापुरी","खासगाव"
            ,"कोकरवाडी","कुक्कडगाव","कुंभेजा","कुंभेफळ","लाखी","लोहारा","लोणारवाडी","लोणी","मलकापूर","माणिक नगर","मुगाव","नळगाव","पाचपिंपळा","पांढरेवाडी","परंडा","पारेवाडी","पिंपळवाडी","पिंपरखेड","पिस्तमवाडी","पिठापुरी","राजुरी",
            "रत्नापूर","रोहकल","रोजा","रुई","सकट बुद्रुक.","सकट ख.","सक्करवाडी","सारणवाडी","सावदरवाडी","शेलगाव","शिराळा","सिरसाव","सोनारी","सोनगिरी","टाकळी","तकमोदवाडी","तांदूळ वाडी","उंडेगाव","वडनेर","वाकडी","वाणेवाडी",
            "वांगेगव्हाण","वाटेफळ","येणेगाव","बावी","बोरी","ब्रह्मगाव","दहीफळ","दसमेगाव","डोंगरेवाडी","फकराबाद","घाटपिंपरी","घोडकी","गोजवाडा","गोळेगाव","हातोला","इंदापूर","इस्रुप","इझोरा","जनकपूर","जावळका","जेबा","कडकनाथवाडी"
            ,"कान्हेरी","कवडेवाडी","केळेवाडी","खामकरवाडी","खानापूर","लाखणगाव","लोणखास","महालदारपुरी","मांडवा","मसोबाचीवाडी","नांदगाव","पांगरी","पॅरा","पारडी","पारगाव","पिंपळगाव","पिंपळवाडी","रुई","सरमकोंडी","सरोल","सारोळा"
            ,"सातवईवाडी","सेलू","शेलगाव","शेंडी","सोनारवाडी","सोनेगाव","तांदुळवाडी","तेरखेडा","वडजी","वाशी","येसवंडी","जिनर"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page1);


        BottomNavigationView bottom_navigation = findViewById(R.id.bottom_navigation);

        spinnerGaon = findViewById(R.id.spinner_Gaon);
        spinnerTaluka = findViewById(R.id.spinner_taluka);
        profileImage = findViewById(R.id.profile_image);
        radioGroup = findViewById(R.id.radioGroup);
        userfirstname = findViewById(R.id.name1);
        usermiddlename = findViewById(R.id.name2);
        userlastname = findViewById(R.id.name3);
//        usertaluka = findViewById(R.id.txttaluka);
        uservoterno = findViewById(R.id.txtVoterIdNo);
        uservoterlistno = findViewById(R.id.txtVotingListNo);
        userwardno = findViewById(R.id.txtVotingWardNo);
//        usergaon = findViewById(R.id.txtGaon);
        useraltphoneno = findViewById(R.id.txtAltPhonNo);
        userdistrict = findViewById(R.id.txtdistrict);
        userstate = findViewById(R.id.txtState);
        useraddress = findViewById(R.id.txtaddress);
        userpanno = findViewById(R.id.txtPanNo);
        useradhharno = findViewById(R.id.txtAddharNo);
        useremail = findViewById(R.id.txtemailid);
        userbirthdate = findViewById(R.id.txtbdate);
        userphoneno = findViewById(R.id.txtphoneno);
        registerbutton = findViewById(R.id.register1);


        callPUTDataMethods();
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);

        String phone_no = prefs.getString("phone_no", null);
        user_id = prefs.getString("id", null);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ProfilePage_1.this, R.layout.custom_spinner_item,taluka);
        arrayAdapter.setDropDownViewResource(R.layout.custom_spinner_item);

        spinnerTaluka.setAdapter(arrayAdapter);

        ArrayAdapter<String> arrayAdapters = new ArrayAdapter<String>(ProfilePage_1.this, R.layout.custom_spinner_item,gaon);
        arrayAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
        spinnerGaon.setAdapter(arrayAdapters);


        userphoneno.setText(phone_no);


        spinnerGaon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Gaon_ = parent.getItemAtPosition(position).toString();
                Toast.makeText(ProfilePage_1.this,Gaon_,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerTaluka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Taluka_ = parent.getItemAtPosition(position).toString();
                Toast.makeText(ProfilePage_1.this,Taluka_,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.home_1:

                        Intent i = new Intent(ProfilePage_1.this, DashBoardMain.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        break;

                    case R.id.profile_1:

                        Intent ii = new Intent(ProfilePage_1.this, ProfilePage_1.class);
                        ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(ii);
                        break;

                    case R.id.ticket_1:

                        Intent iii = new Intent(ProfilePage_1.this, Total_Tickets.class);
                        iii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(iii);
                        break;
                }

                return true;
            }
        });

        final Calendar calender = Calendar.getInstance();
        userbirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the DatePicker widget from the layout
//                DatePicker datePicker = findViewById(R.id.date_picker);

// Create a DatePickerDialog with the current date as default
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ProfilePage_1.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                                // Do something with the selected date
                                // For example, update a TextView with the date
                                userbirthdate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }
                        },
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                );

// Show the DatePickerDialog when the user clicks a button or performs any other action
                datePickerDialog.show();

//                year = calender.get(Calendar.YEAR);
//                month = calender.get(Calendar.MONTH);
//                day = calender.get(Calendar.DAY_OF_MONTH);
//                DatePickerDialog datePickerDialog = new DatePickerDialog(ProfilePage_1.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        userbirthdate.setText(SimpleDateFormat.getDateInstance().format(calender.getTime()));
//                    }
//                }, year, month, day);
//                datePickerDialog.show();
            }
        });


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    if ((ActivityCompat.shouldShowRequestPermissionRationale(ProfilePage_1.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(ProfilePage_1.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE))) {

                    } else {
                        ActivityCompat.requestPermissions(ProfilePage_1.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);
                    }
                } else {
                    Log.e("Else", "Else");
                    showFileChooser();
                }
            }
        });

        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Pattern prvoter = Pattern.compile("^([a-zA-Z]){3}([0-9]){7}?$");
                Pattern praltpone = Pattern.compile("[6-9]{1}[0-9]{9}");
                Pattern prpanno = Pattern.compile("[a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}");
                Pattern praddharno = Pattern.compile("^[2-9]{1}[0-9]{3}[0-9]{4}[0-9]{4}$");
                Pattern premail = Pattern.compile("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$");

                String usermiddlename_ = usermiddlename.getText().toString().trim();
                String userfirstname_ = userfirstname.getText().toString().trim();
                String userlastname_ = userlastname.getText().toString().trim();
//                String usertaluka_ = usertaluka.getText().toString().trim();
                String uservoterno_ = uservoterno.getText().toString().trim();
                String uservoterlistno_ = uservoterlistno.getText().toString().trim();
                String userwardno_ = userwardno.getText().toString().trim();
//                String usergaon_ = usergaon.getText().toString().trim();
                String useraltphoneno_ = useraltphoneno.getText().toString().trim();
                String useraddress_ = useraddress.getText().toString().trim();
                String userpanno_ = userpanno.getText().toString().trim();
                String useradhharno_ = useradhharno.getText().toString().trim();
                String useremail_ = useremail.getText().toString().trim();
                String userbirthdate_ = userbirthdate.getText().toString().trim();
                String userdistrict_ = userdistrict.getText().toString().trim();
                String userstate_ = userstate.getText().toString().trim();
                String userphone_ = userphoneno.getText().toString().trim();

                if (!userfirstname_.isEmpty()) {
                    if (!usermiddlename_.isEmpty()) {
                        if (!userlastname_.isEmpty()) {
                            if (!useraddress_.isEmpty()) {
                                if (!Gaon_.isEmpty()) {
                                    if (!Taluka_.isEmpty()) {
                                        if (!userdistrict_.isEmpty()) {
                                            if (!userstate_.isEmpty()) {
                                                if (!userbirthdate_.isEmpty()) {
                                                    if (!useremail_.isEmpty()) {
                                                        if (premail.matcher(useremail_).matches()) {
                                                            if (!userpanno_.isEmpty()) {
                                                                if (prpanno.matcher(userpanno_).matches()) {
                                                                    if (!useradhharno_.isEmpty()) {
                                                                        if (praddharno.matcher(useradhharno_).matches()) {
                                                                            if (!uservoterno_.isEmpty()) {
                                                                                    if (!uservoterlistno_.isEmpty()) {
                                                                                        if (!userwardno_.isEmpty()) {
                                                                                            if (!useraltphoneno_.isEmpty()) {
                                                                                                if (praltpone.matcher(useraltphoneno_).matches()) {

                                                                                                    rb = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());

                                                                                                    callPUTDataMethod(rb.getText() + "", usermiddlename_, userfirstname_, userlastname_, Taluka_, uservoterno_, uservoterlistno_, userwardno_, Gaon_,
                                                                                                            useraltphoneno_, useraddress_, userdistrict_, userstate_, userphone_, userpanno_, useradhharno_, useremail_, userbirthdate_);

                                                                                                } else {
                                                                                                    Toast.makeText(ProfilePage_1.this, "वैध पर्यायी मोबाईल नंबर प्रविष्ट करा", Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            } else {
                                                                                                Toast.makeText(ProfilePage_1.this, "पर्यायी मोबाईल नंबर प्रविष्ट करा", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        } else {
                                                                                            Toast.makeText(ProfilePage_1.this, "प्रभाग नंबर प्रविष्ट करा", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    } else {
                                                                                        Toast.makeText(ProfilePage_1.this, "मतदान यादीचा नंबर प्रविष्ट करा", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                            } else {
                                                                                Toast.makeText(ProfilePage_1.this, "मतदान कार्ड नंबर प्रविष्ट करा", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        } else {
                                                                            Toast.makeText(ProfilePage_1.this, "वैध आधार नंबर प्रविष्ट करा", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    } else {
                                                                        Toast.makeText(ProfilePage_1.this, "आधार नंबर प्रविष्ट करा", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                } else {
                                                                    Toast.makeText(ProfilePage_1.this, "वैध पॅन नंबर प्रविष्ट करा", Toast.LENGTH_SHORT).show();
                                                                }
                                                            } else {
                                                                Toast.makeText(ProfilePage_1.this, "पॅन नंबर प्रविष्ट करा", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } else {
                                                            Toast.makeText(ProfilePage_1.this, "वैध ईमेल प्रविष्ट करा", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(ProfilePage_1.this, "ईमेल प्रविष्ट करा", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(ProfilePage_1.this, "जन्मदिनांक प्रविष्ट करा", Toast.LENGTH_SHORT).show();
                                                }

                                            } else {
                                                Toast.makeText(ProfilePage_1.this, "राज्य प्रविष्ट करा", Toast.LENGTH_SHORT).show();
                                            }

                                        } else {
                                            Toast.makeText(ProfilePage_1.this, "जिल्हा प्रविष्ट करा", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Toast.makeText(ProfilePage_1.this, "तालुका प्रविष्ट करा", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(ProfilePage_1.this, "गाव प्रविष्ट करा", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(ProfilePage_1.this, "पत्ता प्रविष्ट करा", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(ProfilePage_1.this, "शेवटचे नाव प्रविष्ट करा", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ProfilePage_1.this, "मधले नाव प्रविष्ट करा", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProfilePage_1.this, "प्रथम नाव प्रविष्ट करा", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showFileChooser() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri picUri = data.getData();
            filePath = getPath(picUri);
            if (filePath != null) {
                try {
                    Log.d("filePath", String.valueOf(filePath));
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                    profileImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(
                        ProfilePage_1.this,"no image selected",
                        Toast.LENGTH_LONG).show();
            }
        }

    }

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


//    public String getPath(Uri uri) {
//        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//        cursor.moveToFirst();
//        String document_id = cursor.getString(0);
//        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
//        cursor.close();
//
//        cursor = getContentResolver().query(
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
//        cursor.moveToFirst();
//        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//        cursor.close();
//
//        return path;
//    }


    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    private void callPUTDataMethod(final String mf,String usermiddlename_, String userfirstname_,String userlastname_,String Taluka_,String uservoterno_,String uservoterlistno_,String userwardno_,String Gaon_,
                                   String useraltphoneno_,String useraddress_,String userdistrict_,String userstate_,String userphone_,String userpanno_,String useradhharno_,String useremail_,String userbirthdate_) {

        String usermiddlenames=StringFormatter.convertStringToUTF8(usermiddlename_);
        String mfs=StringFormatter.convertStringToUTF8(mf);
        String userfirstnames=StringFormatter.convertStringToUTF8(userfirstname_);
        String userlastnames=StringFormatter.convertStringToUTF8(userlastname_);
        String usertalukas=StringFormatter.convertStringToUTF8(Taluka_);
        String uservoternos=StringFormatter.convertStringToUTF8(uservoterno_);
        String uservoterlistnos=StringFormatter.convertStringToUTF8(uservoterlistno_);
        String userwardnos=StringFormatter.convertStringToUTF8(userwardno_);
        String usergaons=StringFormatter.convertStringToUTF8(Gaon_);
        String useraddresss=StringFormatter.convertStringToUTF8(useraddress_);
        String userdistricts=StringFormatter.convertStringToUTF8(userdistrict_);
        String userstates=StringFormatter.convertStringToUTF8(userstate_);
        String userpannos=StringFormatter.convertStringToUTF8(userpanno_);
        String useraddharnos=StringFormatter.convertStringToUTF8(useradhharno_);

        Log.d("response12", "345" + mf);

        final ProgressDialog progressDialog = new ProgressDialog(ProfilePage_1.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("प्रोफाइल दाखल होत आहे...");
        progressDialog.show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Const.PROFILE_UPDATE,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        userfirstname.setText("");
                        usermiddlename.setText("");
                        userlastname.setText("");
//                        usertaluka.setText("");
                        uservoterno.setText("");
                        uservoterlistno.setText("");
                        userwardno.setText("");
//                        usergaon.setText("");
                        useraltphoneno.setText("");
                        userdistrict.setText("");
                        userstate.setText("");
                        useraddress.setText("");
                        userpanno.setText("");
                        useradhharno.setText("");
                        useremail.setText("");
                        userbirthdate.setText("");
                        userphoneno.setText("");

                        progressDialog.dismiss();

                        Intent i = new Intent(ProfilePage_1.this, DashBoardMain.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);

                        Toast.makeText(ProfilePage_1.this, "प्रोफाइल दाखल झाली..", Toast.LENGTH_SHORT).show();
                    }
                },           new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                Toast.makeText(ProfilePage_1.this, "प्रोफाइल दाखल झाली नाही..", Toast.LENGTH_SHORT).show();
            }
        }) {

            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("name", userfirstnames);
                params.put("middle_name", usermiddlenames);
                params.put("last_name", userlastnames);
                params.put("taluka", usertalukas);
                params.put("dist", userdistricts);
                params.put("voter_id", uservoternos);
                params.put("voter_list_no", uservoterlistnos);
                params.put("ward_no", userwardnos);
                params.put("alternate_mob_no", useraltphoneno_);
                params.put("city", usergaons);
                params.put("state", userstates);
                params.put("address", useraddresss);
                params.put("pan_no", userpannos);
                params.put("adhar_no", useraddharnos);
                params.put("gender", mfs);
                params.put("email", useremail_);
                params.put("dob", userbirthdate_);
                params.put("phone_no", userphone_);
                params.put("id", user_id);

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();

                if (bitmap != null) {
                    params.put("avatar", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                }
                return params;
            }
        };


        Volley.newRequestQueue(this).add(volleyMultipartRequest);

    }

    private void callPUTDataMethods() {

        RequestQueue queue = Volley.newRequestQueue(ProfilePage_1.this);

        StringRequest request = new StringRequest(Request.Method.POST, Const.PROFILE_GET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        userfirstname.setText("");
                        usermiddlename.setText("");
                        userlastname.setText("");
//                        usertaluka.setText("");
                        uservoterno.setText("");
                        uservoterlistno.setText("");
                        userwardno.setText("");
//                        usergaon.setText("");
                        useraltphoneno.setText("");
                        userdistrict.setText("");
                        userstate.setText("");
                        useraddress.setText("");
                        userpanno.setText("");
                        useradhharno.setText("");
                        useremail.setText("");
                        userbirthdate.setText("");
                        userphoneno.setText("");

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("response12", "34" + response);
                            if (jsonObject.has("data")) {

                                JSONObject jsonObject1 = jsonObject.getJSONArray("data").getJSONObject(0);
                                String firstname = jsonObject1.getString("name");
                                String middlename = jsonObject1.getString("middle_name");
                                String lastname = jsonObject1.getString("last_name");
                                String addresss = jsonObject1.getString("address");
                                String gaon = jsonObject1.getString("city");
                                String taluka = jsonObject1.getString("taluka");
                                String district = jsonObject1.getString("dist");
                                String state = jsonObject1.getString("state");
                                String birthdate = jsonObject1.getString("dob");
                                String email = jsonObject1.getString("email");
                                String phoneno = jsonObject1.getString("phone_no");
                                String genders = jsonObject1.getString("gender");
                                String panno = jsonObject1.getString("pan_no");
                                String adharno = jsonObject1.getString("adhar_no");
                                String votingno = jsonObject1.getString("voter_id");
                                String votinglistno = jsonObject1.getString("voter_list_no");
                                String wardno = jsonObject1.getString("ward_no");
                                String altphoneno = jsonObject1.getString("alternate_mob_no");

                                Log.d("response12", "342" + genders);

                                userfirstname.setText(firstname);
                                usermiddlename.setText(middlename);
                                userlastname.setText(lastname);
//                                usertaluka.setText(taluka);

                                uservoterno.setText(votingno);
                                uservoterlistno.setText(votinglistno);
                                userwardno.setText(wardno);
//                                usergaon.setText(gaon);
                                useraltphoneno.setText(altphoneno);
                                userdistrict.setText(district);
                                userstate.setText(state);
                                useraddress.setText(addresss);
                                userpanno.setText(panno);
                                useradhharno.setText(adharno);
                                useremail.setText(email);
                                userbirthdate.setText(birthdate);
                                userphoneno.setText(phoneno);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ProfilePage_1.this, "प्रोफाइल अपडेट झाली नाही...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("id", user_id);

                return params;
            }
        };

        queue.add(request);


    }
}