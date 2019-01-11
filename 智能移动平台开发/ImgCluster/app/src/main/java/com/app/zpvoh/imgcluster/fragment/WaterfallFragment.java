package com.app.zpvoh.imgcluster.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.zpvoh.imgcluster.Constant;
import com.app.zpvoh.imgcluster.activity.Main2Activity;
import com.app.zpvoh.imgcluster.R;
import com.app.zpvoh.imgcluster.entities.Comment;
import com.app.zpvoh.imgcluster.entities.Image;
import com.app.zpvoh.imgcluster.sqlUtils.SqlDao;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WaterfallFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WaterfallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WaterfallFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String LIST = "imgList";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    GridView mGridView;
    ArrayList<Image> mItems;
    ImageLoader imageLoader;

    private OnFragmentInteractionListener mListener;

    public WaterfallFragment() {

    }


    /*public static WaterfallFragment newInstance(String param1, String param2) {
        WaterfallFragment fragment = new WaterfallFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    public static WaterfallFragment newInstance(ArrayList<Image> mItems) {
        WaterfallFragment waterfallFragment = new WaterfallFragment();
        Bundle args = new Bundle();
        args.putSerializable(LIST, mItems);
        waterfallFragment.setArguments(args);
        return waterfallFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItems = (ArrayList<Image>) getArguments().getSerializable(LIST);
        }

        //        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(getContext());
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(20))
                .showImageOnFail(R.mipmap.ic_launcher)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getContext())
                .memoryCacheSizePercentage(20)
                .diskCacheFileCount(100)
                .diskCacheSize(5 * 1024 * 1024)
                .defaultDisplayImageOptions(options).build();

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(configuration);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_waterfall, container, false);
        mGridView = v.findViewById(R.id.grid);
        setupAdapter();

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void setupAdapter() {
        if (getActivity() == null || mGridView == null)
            return;

        if (mItems != null) {
            //mItems.add(new Image(-1,-1,"0000-0-0", "", "drawable://vadim.jpg"));
            mGridView.setAdapter(new GalleryItemAdapter(mItems));
        } else {
            mGridView.setAdapter(null);
        }
    }


    private class GalleryItemAdapter extends ArrayAdapter<Image> {
        public GalleryItemAdapter(ArrayList<Image> items) {
            super(getActivity(), 0, items);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.gallery_item, parent, false);
            }
            if (mItems != null && mItems.size() != 0) {
                ImageView imageView = convertView.findViewById(R.id.photoItem);
                Image image = mItems.get(position);
                //imageView.setImageResource(R.drawable.anastasia);
                imageLoader.displayImage(image.path, imageView);
                switch (image.getImg_id()) {
                    case -1:
                        imageView.setOnClickListener(view -> {
                            Main2Activity.groupId=image.getGroup_id();
                            ((Main2Activity)getActivity()).openAlbum(image.getGroup_id());
                        });
                        break;
                    case -2:
                        imageView.setOnClickListener(view -> {
                            String validNumber = SqlDao.getValidNumber(Main2Activity.connection, image.getGroup_id());
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                                builder.setIcon(R.drawable.picture);
                            builder.setTitle("验证码");
                            builder.setMessage(validNumber);
                            builder.setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        });
                        break;
                    default:
                        imageView.setOnClickListener(view -> {
                            ImageDisplayFragment displayFragment = ImageDisplayFragment.newInstance(image);
                            getFragmentManager().beginTransaction().replace(R.id.contentFragment, displayFragment).commit();
                            Main2Activity activity = (Main2Activity) getActivity();
                            activity.fragmentStack.push(displayFragment);
                        });
                }
            }
            return convertView;
        }


    }
}
