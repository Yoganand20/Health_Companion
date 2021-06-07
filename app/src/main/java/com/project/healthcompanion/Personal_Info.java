
package com.project.healthcompanion;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.project.healthcompanion.databinding.FragmentPersonalInfoBinding;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Personal_Info extends Fragment {
    private FragmentPersonalInfoBinding binding;
    private UserInfoViewModel userInfoViewModel;
    private Date dateOfBirth;
    final Calendar DOB_Calendar = Calendar.getInstance();
    private String gender;

    private static final String TAG = MainActivity.class.getSimpleName();

    public Personal_Info() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPersonalInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        loadProfileDefault();

        binding.imageViewProfilePic.setOnClickListener(onClick_setProfilePic);
        binding.imgPlus.setOnClickListener(onClick_setProfilePic);

        ImgPickerActivity.clearCache(this);

        DatePickerDialog.OnDateSetListener DOB = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                DOB_Calendar.set(year, month, dayOfMonth);
                updateLabel();
            }
        };

        binding.editTextDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), DOB, DOB_Calendar.get(Calendar.YEAR), DOB_Calendar.get(Calendar.MONTH), DOB_Calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        binding.radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (binding.radioBtnMale.getId() == checkedId) {
                    gender = "Male";
                } else if (binding.radioBtnFemale.getId() == checkedId) {
                    gender = "Female";
                } else {
                    gender = "";
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userInfoViewModel = new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);
    }

    //updates editText view after selecting data (DOB)
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateOfBirth = DOB_Calendar.getTime();
        binding.editTextDOB.setText(sdf.format(dateOfBirth));
    }

    private void loadProfile(String url) {
        Log.d(TAG, "Image cache path: " + url);

        Glide.with(this).load(url).into(binding.imageViewProfilePic);
        binding.imageViewProfilePic
                .setColorFilter(ContextCompat.getColor(requireContext(), android.R.color.transparent));
    }

    private void loadProfileDefault() {
        Glide.with(this).load(R.drawable.baseline_account_circle_black_48)
                .into(binding.imageViewProfilePic);
        binding.imageViewProfilePic
                .setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey_300));
    }


    View.OnClickListener onClick_setProfilePic = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                Dexter.withContext(getContext())
                        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    showImagePickerOptions();
                                }

                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    showSettingsDialog();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            /*}
           else {
                Dexter.withContext(getContext())
                        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    showImagePickerOptions();
                                }

                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    showSettingsDialog();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }*/

        }
    };


    private void showImagePickerOptions() {
        ImgPickerActivity.showImagePickerOptions(this, new ImgPickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(this.getActivity(), ImgPickerActivity.class);
        intent.putExtra(ImgPickerActivity.INTENT_IMAGE_PICKER_OPTION, ImgPickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImgPickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImgPickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImgPickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImgPickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImgPickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImgPickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        someActivityResultLauncher.launch(intent);
        // startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(this.getActivity(), ImgPickerActivity.class);
        intent.putExtra(ImgPickerActivity.INTENT_IMAGE_PICKER_OPTION, ImgPickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImgPickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImgPickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImgPickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        someActivityResultLauncher.launch(intent);

//        startActivityForResult(intent, REQUEST_IMAGE);
    }

    //Showing Alert Dialog with Settings option
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Grant Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        //
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts("package", requireContext().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        Uri uri = data.getParcelableExtra("path");
                        Bitmap bitmap;

                        try {
                            //use ImageDecoder to get bitmap on android devices with api 29+
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                ImageDecoder.Source source = ImageDecoder.createSource(requireContext().getContentResolver(), uri);
                                bitmap = ImageDecoder.decodeBitmap(source);
                            } else {//on devices with api version 28 and lower use getBitmap (deprecated method)
                                bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), uri);
                            }
                            userInfoViewModel.setProfilePic(bitmap);
                            // loading profile image from local cache
                            loadProfile(uri.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


}