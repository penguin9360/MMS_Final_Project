package com.project.plantappui.menu.camera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.plantappui.R;
// @TODO - fix the camera fragment - clicking button makes app crash
public class CameraFragment extends Fragment {
//    public static final String EXTRA_INFO = "default";
//    private Button btnCapture;
//    private ImageView imgCapture;
//    private static final int Image_Capture_Code = 1;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_camera, container, false);
//        btnCapture =(Button) view.findViewById(R.id.btnTakePicture);
//        imgCapture = (ImageView) view.findViewById(R.id.capturedImage);
//        btnCapture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cInt,Image_Capture_Code);
//            }
//        });
//
//        return view;
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == Image_Capture_Code) {
//            if (resultCode == RESULT_OK) {
//                Bitmap bp = (Bitmap) data.getExtras().get("data");
//                imgCapture.setImageBitmap(bp);
//            } else if (resultCode == RESULT_CANCELED) {
//                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
//            }
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("Now in camera fragment");
        return inflater.inflate(R.layout.fragment_camera, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}