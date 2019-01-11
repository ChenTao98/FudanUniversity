package com.app.zpvoh.imgcluster.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.zpvoh.imgcluster.sqlUtils.SqlDao;
import com.app.zpvoh.imgcluster.activity.Main2Activity;
import com.app.zpvoh.imgcluster.R;
import com.app.zpvoh.imgcluster.entities.Group;
import com.app.zpvoh.imgcluster.entities.Image;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyClusterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyClusterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyClusterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<Group> groups;
    private GridView myClusterGrid;
    ImageLoader imageLoader;

    private OnFragmentInteractionListener mListener;

    public MyClusterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyClusterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyClusterFragment newInstance(String param1, String param2) {
        MyClusterFragment fragment = new MyClusterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        if (Main2Activity.user != null) {
//            groups = SqlDao.getUserGroups(Main2Activity.user.uid);
            groups = SqlDao.getUserGroups(Main2Activity.connection, Main2Activity.user.uid);
        } else {
            Toast.makeText(getContext(), R.string.login_hint, Toast.LENGTH_SHORT).show();
        }
//        groups=new ArrayList<>();
//        groups.add(new Group(1,"Seven"));
//        groups.add(new Group(2, "Jack"));
//        groups.add(new Group(3,"Ace"));
//        groups.add(new Group(4,"Taro"));
//        groups.add(new Group(5,"Leo"));
//        groups.add(new Group(6,"80"));

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
        View v = inflater.inflate(R.layout.fragment_my_cluster, container, false);
        myClusterGrid = v.findViewById(R.id.myClusterViews);

        if (groups.size() != 0) {
            myClusterGrid.setAdapter(new GroupsAdapter(groups));
        } else {
            myClusterGrid.setAdapter(new GroupsAdapter(new ArrayList<Group>()));
        }
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

    private class GroupsAdapter extends ArrayAdapter<Group> {
        public GroupsAdapter(ArrayList<Group> items) {
            super(getActivity(), 0, items);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.group_layout, parent, false);
            }

            if (groups.size() != 0) {
                Group group = groups.get(position);
                ImageView imageView = convertView.findViewById(R.id.group_view);
                TextView nameView = convertView.findViewById(R.id.group_name_text);
                nameView.setText(group.group_name);
                //imageView.setImageResource(R.drawable.anastasia);
                ArrayList<Image> imgList = SqlDao.getImageByGroup(Main2Activity.connection, group.group_id);
                Image loadImage = new Image(-1, group.group_id, "", "load_img", "drawable://" + R.drawable.load_img);
                imgList.add(loadImage);
                Image shareImage = new Image(-2, group.group_id, "", "share_img", "drawable://" + R.drawable.share_img);
                imgList.add(shareImage);
                Log.i("iamge", imgList.size() + " " + imgList.get(imgList.size() - 1).getPath());
                if (imgList.size() != 0) {
                    imageLoader.displayImage(imgList.get(0).path, imageView);
                }

                convertView.setOnClickListener(view -> {
                    WaterfallFragment groupImgFragment = WaterfallFragment.newInstance(imgList);
                    getFragmentManager().beginTransaction().replace(R.id.contentFragment, groupImgFragment).commit();
                    Main2Activity activity = (Main2Activity) getActivity();
                    activity.fragmentStack.push(groupImgFragment);
                });
            }

            return convertView;
        }
    }
}
