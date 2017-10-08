package com.example.yashw.uber;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DriverSettingsAcivity extends AppCompatActivity {

    private Button mConfirm,mBack;
    private EditText mName,mPhone;
    private ImageView mProfileImage;

    private Uri resultUri;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference mDriverDatabase;

    private String mUserId,mname,mphone,mprofileImageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_settings_acivity);

        mProfileImage=(ImageView) findViewById(R.id.profileImage);

        mConfirm=(Button)findViewById(R.id.confirm);
        mBack=(Button)findViewById(R.id.back);

        mName=(EditText)findViewById(R.id.name);
        mPhone=(EditText)findViewById(R.id.phone);

        firebaseAuth=FirebaseAuth.getInstance();
        mUserId=firebaseAuth.getCurrentUser().getUid();

        mDriverDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(mUserId);

        getUserInfo();

        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                return;
            }
        });
    }

    private void getUserInfo(){
        mDriverDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()&&dataSnapshot.getChildrenCount()>0){
                    Map<String,Object> map=(Map<String,Object>) dataSnapshot.getValue();
                    if (map.get("name")!=null){
                        mname=map.get("name").toString();
                        mName.setText(mname);
                    }
                    if (map.get("phone")!=null){
                        mphone=map.get("phone").toString();
                        mPhone.setText(mphone);
                    }
                    if (map.get("profileImageUrl")!=null){
                        mprofileImageUrl=map.get("profileImageUrl").toString();
                        Glide.with(getApplication()).load(mprofileImageUrl).into(mProfileImage);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void saveUserInformation(){

        mname=mName.getText().toString();
        mphone=mPhone.getText().toString();

        Map userInfo=new HashMap();
        userInfo.put("name",mname);
        userInfo.put("phone",mphone);

        mDriverDatabase.updateChildren(userInfo);

        if (resultUri!=null){
            StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile_images").child(mUserId);

            Bitmap bitmap=null;
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,20, baos);
            byte[] data=baos.toByteArray();
            UploadTask uploadTask=filePath.putBytes(data);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                    return;
                }
            });

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl=taskSnapshot.getDownloadUrl();

                    Map imageMap=new HashMap();
                    imageMap.put("profileImageUrl",downloadUrl.toString());
                    mDriverDatabase.updateChildren(imageMap);

                    finish();
                    return;
                }
            });

        }
        else{
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1&&resultCode== Activity.RESULT_OK){
            final Uri imageUri=data.getData();
            resultUri=imageUri;
            mProfileImage.setImageURI(resultUri);
        }
    }
}
