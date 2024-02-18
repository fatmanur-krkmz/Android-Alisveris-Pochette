package com.fnmeinss.pochette;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AddDonation extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private StorageReference reference= FirebaseStorage.getInstance().getReference();
    public static DatabaseReference db= FirebaseDatabase.getInstance().getReference().child("Donations");
    private Button upLoadButton;
    private CheckBox checkClothes,checkToys;
    private EditText Description,Price;
    public  String imageName;
    private TextView textViewType,textViewPrice;
    private LinearLayout biglinear;
    private ProgressBar progressBar;
    Uri imageData;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLuncher;
    private ImageView uploadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_page);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        upLoadButton=findViewById(R.id.upLoadButton);
        Description=findViewById(R.id.editTextproductdes);
        Price=findViewById(R.id.editTextproductPrice);
        checkToys = findViewById(R.id.checkboxToys);
        checkClothes = findViewById(R.id.checkboxClothes);
        textViewType = findViewById(R.id.textViewType);
        textViewPrice = findViewById(R.id.priceTextView);
        progressBar = findViewById(R.id.progressBar);
        biglinear = findViewById(R.id.big_linear);

        upLoadButton.setText("Add Donation");
        Price.setVisibility(View.INVISIBLE);
        checkClothes.setVisibility(View.INVISIBLE);
        checkToys.setVisibility(View.INVISIBLE);
        textViewType.setVisibility(View.INVISIBLE);
        textViewPrice.setVisibility(View.INVISIBLE);

        mAuth=FirebaseAuth.getInstance();
        uploadImage = findViewById(R.id.uploadImage);
        SharedPreferences sp = getSharedPreferences("aktifkullanici", Context.MODE_PRIVATE);//local saves 01.03
        registerLauncher();


        upLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = Description.getText().toString();
                if(imageData==null){
                    Toast.makeText(getApplicationContext(), " 'Image' cannot be left blank", Toast.LENGTH_SHORT).show();
                }
                else if (description.equals(""))
                    Description.setError(" 'Description' cannot be left blank");
                else {
                        biglinear.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                        UUID uuid=UUID.randomUUID(); //random isim veriyor
                        imageName="images/"+uuid+".jpg";
                        final StorageReference newReference=reference.child(imageName);

                        reference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {//.child dersek products altına bir dosya daha açar
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        addProduct(imageUrl);
                                        Toast.makeText(AddDonation.this, "uploaded successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AddDonation.this, Donation.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        }) .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddDonation.this,"sdcsdcsd",Toast.LENGTH_LONG).show();
                            }
                        });

                }}
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            imageData = data.getData();
            uploadImage.setImageURI(imageData);


        }
    }


    public void addProduct(String url){
        String description = Description.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Donations");

        Product newProduct1 = new Product(url,description);
        myRef.push().setValue(newProduct1);

    }
    public void selectImage(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        permissionLuncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            } else {
                permissionLuncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE); //izin isteniyor

            }


        }else

        {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intentToGallery);
        }

    }
    private void registerLauncher(){
        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode()==RESULT_OK){
                    Intent intentFromResult=result.getData();
                    if (intentFromResult!=null){
                        imageData=intentFromResult.getData();
                        uploadImage.setImageURI(imageData);
                    }
                }
            }
        });
        //izin isteme kısmı aşağısı
        permissionLuncher=registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result) {
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGallery);
                } else {
                    Toast.makeText(AddDonation.this, "Permission needed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent List_Ekran_gecis = new Intent(AddDonation.this, Donation.class);
        startActivity(List_Ekran_gecis);
        finish();
    }
}