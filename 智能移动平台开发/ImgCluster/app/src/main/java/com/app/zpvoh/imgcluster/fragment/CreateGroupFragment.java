package com.app.zpvoh.imgcluster.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.zpvoh.imgcluster.sqlUtils.SqlDao;
import com.app.zpvoh.imgcluster.activity.Main2Activity;
import com.app.zpvoh.imgcluster.R;
import com.app.zpvoh.imgcluster.entities.Group;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateGroupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateGroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateGroupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private EditText createText;
    private EditText joinText;
    private Button createButton;
    private Button joinButton;

    public CreateGroupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateGroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateGroupFragment newInstance(String param1, String param2) {
        CreateGroupFragment fragment = new CreateGroupFragment();
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
        } else {
            Toast.makeText(getContext(), R.string.login_hint, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_create_group, container, false);
        createText=view.findViewById(R.id.create_group_name_text);
        joinText=view.findViewById(R.id.join_group_number_text);
        createButton = view.findViewById(R.id.create_group_button);
        joinButton = view.findViewById(R.id.join_group_button);
        setAction();
        return view;
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
    private void setAction(){
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createText.setError(null);
                String groupName=createText.getText().toString();
                if(!TextUtils.isEmpty(groupName)){
                    Group group=SqlDao.createGroup(Main2Activity.connection,Main2Activity.user.getUid(),groupName);
                    if(group!=null){
                        Toast.makeText(getActivity(),"成功创建分组",Toast.LENGTH_SHORT).show();
                    }else {
                        createText.setError(getString(R.string.error_group_name_exist));
                        createText.requestFocus();
                    }
                }else {
                    createText.setError(getString(R.string.error_group_name_empty));
                    createText.requestFocus();
                }
            }
        });
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinText.setError(null);
                String validNumber=joinText.getText().toString();
                if(!TextUtils.isEmpty(validNumber)){
                    Group group=SqlDao.joinGroup(Main2Activity.connection,Main2Activity.user.getUid(),validNumber);
                    if(group!=null){
                        Toast.makeText(getActivity(),"成功加入分组",Toast.LENGTH_SHORT).show();
                    }else {
                        joinText.setError(getString(R.string.error_valid_number_invalid));
                        joinText.requestFocus();
                    }
                }else {
                    joinText.setError(getString(R.string.error_valid_number_empty));
                    joinText.requestFocus();
                }
            }
        });
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
}
