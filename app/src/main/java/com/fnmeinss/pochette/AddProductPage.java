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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class AddProductPage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    public static DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("product");
    private Button upLoadButton;
    private CheckBox checkClothes, checkToys;
    public String type = "";
    private EditText Price, Description;
    public String imageName, seller, gelenid = "";
    private LinearLayout biglinear,producttypelinear;
    private ProgressBar progressBar;
    private Boolean edittengeldi;
    Uri imageData;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLuncher;
    private ImageView uploadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_page);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        upLoadButton = findViewById(R.id.upLoadButton);
        Description = findViewById(R.id.editTextproductdes);
        Price = findViewById(R.id.editTextproductPrice);
        checkToys = findViewById(R.id.checkboxToys);
        checkClothes = findViewById(R.id.checkboxClothes);
        biglinear = findViewById(R.id.big_linear);
        progressBar = findViewById(R.id.progressBar);
        producttypelinear = findViewById(R.id.producttypelinear);

        mAuth = FirebaseAuth.getInstance();
        uploadImage = findViewById(R.id.uploadImage);

        edittengeldi = getIntent().getBooleanExtra("edittengeldi", false);
        if (edittengeldi) {
            gelenid = getIntent().getStringExtra("gelenid");
            producttypelinear.setVisibility(View.INVISIBLE);
            upLoadButton.setText("Update product");
        }
        SharedPreferences sp = getSharedPreferences("aktifkullanici", Context.MODE_PRIVATE);//local saves 01.03
        seller = sp.getString("nameandsurname", "");

        registerLauncher();

        checkToys.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (checkClothes.isChecked()) {
                        checkClothes.setChecked(false);

                    }
                    type = "toys";
                }

            }
        });
        checkClothes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (checkToys.isChecked() == true) {
                        checkToys.setChecked(false);
                    }
                    type = "clothes";
                }
            }
        });
        upLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gelenid.equals("")) {
                    String description = Description.getText().toString();
                    if (imageData == null) {
                        Toast.makeText(getApplicationContext(), " 'Image' cannot be left blank", Toast.LENGTH_SHORT).show();
                    } else if (Price.getText().toString().equals(""))
                        Price.setError(" 'Price' cannot be left blank");
                    else if (description.equals(""))
                        Description.setError(" 'Description' cannot be left blank");
                    else if (type.equals(""))
                        Toast.makeText(getApplicationContext(), " 'Product Type' cannot be left blank", Toast.LENGTH_SHORT).show();
                    else {
                        biglinear.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                        UUID uuid = UUID.randomUUID();
                        imageName = "images/" + uuid + ".jpg";
                        final StorageReference newReference = reference.child(imageName);

                        reference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        addProduct(imageUrl);
                                        Toast.makeText(AddProductPage.this, "uploaded successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AddProductPage.this, SellerMainPage.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddProductPage.this, "sdcsdcsd", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } else {

                    Map<String, Object> güncelleme = new HashMap<>();
                    Boolean istoys = getIntent().getBooleanExtra("istoys", false);

                    DatabaseReference myRef;
                    if (istoys) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        myRef = database.getReference("product").child("toys");
                    } else {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        myRef = database.getReference("product").child("clothes");
                    }


                    String description = Description.getText().toString();
                    if (!Price.getText().toString().equals(""))
                        güncelleme.put("product_price", Integer.valueOf(Price.getText().toString()));
                    if (!description.equals(""))
                        güncelleme.put("product_description2",Description.getText().toString());

                    if (imageData != null) {

                        biglinear.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                        UUID uuid = UUID.randomUUID();
                        imageName = "images/" + uuid + ".jpg";
                        final StorageReference newReference = reference.child(imageName);

                        reference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {//.child dersek products altına bir dosya daha açar
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        String imageUrl = uri.toString();
                                        güncelleme.put("imageUrl",imageUrl);
                                        myRef.child(gelenid).updateChildren(güncelleme);
                                        Toast.makeText(AddProductPage.this, "updated successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AddProductPage.this, SellerMainPage.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("ss","545645"+newReference);
                                Toast.makeText(AddProductPage.this, "sdcsdcsd", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    else {


                        myRef.child(gelenid).updateChildren(güncelleme);
                        Toast.makeText(AddProductPage.this, "updated successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddProductPage.this, SellerMainPage.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }
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


    public void addProduct(String url) {

        float price = Float.parseFloat(Price.getText().toString());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("product");
        String description = Description.getText().toString();
        Product newProduct = new Product(seller, type, url, description, price);
        myRef.child(type).push().setValue(newProduct);

    }

    public void addProduct(String url,String seller, String type,String description,String price,String mail) {

        float price2 = Float.valueOf(price);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Cart");
        CartModel cartModel = new CartModel(url,mail,description,price2,seller,1);
        myRef.child(type).push().setValue(cartModel);


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
                permissionLuncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

            }


        } else {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intentToGallery);
        }

    }

    private void registerLauncher() {

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intentFromResult = result.getData();
                    if (intentFromResult != null) {
                        imageData = intentFromResult.getData();
                        uploadImage.setImageURI(imageData);
                    }
                }
            }
        });
        permissionLuncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result) {
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGallery);
                } else {
                    Toast.makeText(AddProductPage.this, "Permission needed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionsmenu_product, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.seller_page) {
            Intent customerpageIntent = new Intent(AddProductPage.this, SellerMainPage.class);
            startActivity(customerpageIntent);
            finish();
        } else if (item.getItemId() == R.id.customer_page) {
            Intent customerpageIntent = new Intent(AddProductPage.this, MainPage.class);
            startActivity(customerpageIntent);
            finish();
        } else if (item.getItemId() == R.id.logout) {
            mAuth.signOut();
            SharedPreferences sp = getSharedPreferences("aktifkullanici", Context.MODE_PRIVATE);//local saves 01.03
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("nameandsurname", "");
            editor.commit();
            Intent intentToMain = new Intent(AddProductPage.this, MainActivity.class);
            startActivity(intentToMain);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        Intent List_Ekran_gecis = new Intent(AddProductPage.this, SellerMainPage.class);
        startActivity(List_Ekran_gecis);
        finish();
    }

}