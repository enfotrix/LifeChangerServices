package com.enfotrix.life_changer_services.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.enfotrix.life_changer_services.R;
import com.enfotrix.life_changer_services.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class Activity_Profile extends AppCompatActivity implements View.OnClickListener {


    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    private String refUri;

    private AppCompatButton btn_buy;
    private FirebaseFirestore firestore;
    private Utils utils;
    private LinearLayout lay_refer, lay_team;
    private TextView txt_referral, txtname, txt_cnic, txt_number, txt_email, txt_membership;
    private ImageView img_logout, img_edit, imageprofile;


    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        iniviews();

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        utils = new Utils(this);

        getdata();
    }

    private void getdata() {

        utils.startLoading();
        firestore.collection("Customer").document(utils.getToken()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();

                        String name = document.getString("name");
                        String cnic = document.getString("ac_number");
                        String number = document.getString("number");
                        String email = document.getString("email");
                        //String address = document.getString("address");
                        String referral = document.getString("autoReferral");
                        String profilePic = document.getString("picture");

                        txtname.setText(name);
                        txt_cnic.setText(cnic);
                        txt_number.setText(number);
                        txt_email.setText(email);
                        txt_referral.setText(referral);

                        if (profilePic != null) {
                            Glide.with(getApplicationContext()).load(profilePic).into(imageprofile);
                        }


                        firestore.collection("Customer").document(utils.getToken())
                                .collection("Transactions").document(utils.getToken()).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot document = task.getResult();

                                        if (document.getString("status") != null) {

                                            if (document.getString("status").equals("approve")) {
                                                btn_buy.setEnabled(false);
                                                txt_membership.setText("Approved");
                                                utils.putIsBuy(true);

                                            }
                                            if (document.getString("status").equals("request")) {
                                                btn_buy.setEnabled(false);
                                                txt_membership.setText("Membership Pending");
                                                utils.putIsBuy(false);

                                            }

                                        }


                                        utils.endLoading();
                                    }

                                });

                    }

                });
    }

    private void iniviews() {


        btn_buy = findViewById(R.id.btn_buy);
        imageprofile = findViewById(R.id.imageprofile);
        lay_refer = findViewById(R.id.lay_refer);
        lay_team = findViewById(R.id.lay_team);
        txt_membership = findViewById(R.id.txt_membership);
        //lay_referral = findViewById(R.id.lay_referral);
        txt_referral = findViewById(R.id.txt_referral);
        txtname = findViewById(R.id.txtname);
        txt_cnic = findViewById(R.id.txt_cnic);
        txt_number = findViewById(R.id.txt_number);
        txt_email = findViewById(R.id.txt_email);
        img_logout = findViewById(R.id.img_logout);
        img_edit = findViewById(R.id.img_edit);

        Animation animationUtils = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        btn_buy.startAnimation(animationUtils);
        //img_logout.startAnimation(animationUtils);

        //lay_referral.setOnClickListener(this);
        btn_buy.setOnClickListener(this);
        img_logout.setOnClickListener(this);
        img_edit.setOnClickListener(this);
        lay_team.setOnClickListener(this);
        lay_refer.setOnClickListener(this);
        imageprofile.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            /*case R.id.lay_referral:
                setClipboard(Activity_Profile.this, txt_referral.getText().toString());//txt_refCode.getText().toString());

                break;*/
            case R.id.btn_buy:
                startActivity(new Intent(getApplicationContext(), Activity_Transaction.class));
                break;
            case R.id.imageprofile:
                imgProfile();
                break;
            case R.id.img_edit:
            case R.id.lay_team:
            case R.id.lay_refer:
                Toast.makeText(Activity_Profile.this, "Available soon...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_logout:
                logout();
                break;
        }
    }

    public void logout() {
        MaterialDialog mDialog = new MaterialDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure want to logout!")
                .setCancelable(false)
                .setPositiveButton("Logout", R.drawable.ic_baseline_logout_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        utils.logout();
                        startActivity(new Intent(getApplicationContext(), ActivityLogin.class));
                        finish();
                    }
                })
                .setNegativeButton("Cancel", R.drawable.ic_baseline_cancel_presentation_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .build();

        // Show Dialog
        mDialog.show();
    }


    private void setClipboard(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);

        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "" + clipboard.toString(), Toast.LENGTH_SHORT).show();

        }
        Toast.makeText(context, "Referral Code " + text + " Copied!", Toast.LENGTH_SHORT).show();
    }

    private void imgProfile() {
        MaterialDialog mDialog = new MaterialDialog.Builder(Activity_Profile.this)
                .setTitle("Upload")
                .setMessage("Are you sure want to Upload Image!")
                .setCancelable(false)
                .setPositiveButton("Upload", R.drawable.ic_baseline_file_upload_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {


                        chooseImage();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Cancel", R.drawable.ic_baseline_cancel_presentation_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .build();

        // Show Dialog
        mDialog.show();
    }


    private void updateUser(Uri filePath) {


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();


        StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
        ref.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                Map<String, Object> m = new HashMap<>();
                                m.put("picture", uri.toString());


                                firestore.collection("Customer").document(utils.getToken()).update(m)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                progressDialog.dismiss();
                                                Toast.makeText(Activity_Profile.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                                getdata();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Activity_Profile.this, "Connection Error", Toast.LENGTH_SHORT).show();
                                            }
                                        });


                            }
                        });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Activity_Profile.this, "Connection Error", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        //Toast.makeText(ActivityNominee.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploading Data " + (int) progress + "%");
                    }
                });


    }


    /////////////////////////  IMAGE  //////////////////
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                imageprofile.setImageBitmap(bitmap);

                updateUser(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    ///////////////////////////////////////////////////////////////
}
