package com.app.zpvoh.imgcluster.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.zpvoh.imgcluster.Constant;
import com.app.zpvoh.imgcluster.R;
import com.app.zpvoh.imgcluster.entities.Group;
import com.app.zpvoh.imgcluster.entities.Image;
import com.app.zpvoh.imgcluster.entities.User;
import com.app.zpvoh.imgcluster.fragment.BlankFragment;
import com.app.zpvoh.imgcluster.fragment.CreateGroupFragment;
import com.app.zpvoh.imgcluster.fragment.ImageDisplayFragment;
import com.app.zpvoh.imgcluster.fragment.MyClusterFragment;
import com.app.zpvoh.imgcluster.fragment.WaterfallFragment;
import com.app.zpvoh.imgcluster.sqlUtils.SqlDao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Stack;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,
        WaterfallFragment.OnFragmentInteractionListener,
        MyClusterFragment.OnFragmentInteractionListener,CreateGroupFragment.OnFragmentInteractionListener,
        BlankFragment.OnFragmentInteractionListener ,
        ImageDisplayFragment.OnFragmentInteractionListener{

    private WaterfallFragment waterfallFragment=new WaterfallFragment();
    private MyClusterFragment myClusterFragment=new MyClusterFragment();
    private CreateGroupFragment createGroupFragment=new CreateGroupFragment();
    public Stack<android.app.Fragment> fragmentStack=new Stack<>();
    public static User user=null;
    public static Connection connection=null;
    public static int groupId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.inflateHeaderView(R.layout.nav_header_main2);
        navigationView.inflateMenu(R.menu.activity_main2_drawer);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        ImageView icon=(ImageView)header.findViewById(R.id.userView);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Main2Activity.this, LoginActivity.class);
                startActivityForResult(intent,1);
            }
        });


        if(user!=null){
            ArrayList<Group> groups=SqlDao.getUserGroups(connection,user.uid);
            ArrayList<Image> images=new ArrayList<>();
            groups.forEach(group->{
                ArrayList<Image> group_imgs=SqlDao.getImageByGroup(connection,group.group_id);
                images.addAll(group_imgs);
            });

            TextView username=header.findViewById(R.id.header_username);
            TextView email=header.findViewById(R.id.header_email);
            username.setText(user.username);
            email.setText(user.email);

            waterfallFragment=WaterfallFragment.newInstance(images);
        }else{
            Toast.makeText(this, R.string.login_hint, Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(Main2Activity.this, LoginActivity.class);
            startActivityForResult(intent,1);
        }
        getFragmentManager().beginTransaction().replace(R.id.contentFragment, waterfallFragment).commit();
        //Main2Activity activity=(Main2Activity)getActivity();
        fragmentStack.push(waterfallFragment);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(fragmentStack.size()>1){
            fragmentStack.pop();
            getFragmentManager().beginTransaction().replace(R.id.contentFragment, fragmentStack.peek()).commit();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {
            if(user!=null){
                ArrayList<Group> groups=SqlDao.getUserGroups(connection,user.uid);
                ArrayList<Image> images=new ArrayList<>();
                groups.forEach(group->{
                    ArrayList<Image> group_imgs=SqlDao.getImageByGroup(connection,group.group_id);
                    images.addAll(group_imgs);
                });

                waterfallFragment=WaterfallFragment.newInstance(images);
            }else{
                Toast.makeText(this, R.string.login_hint, Toast.LENGTH_SHORT).show();
            }
            getFragmentManager().beginTransaction().replace(R.id.contentFragment, waterfallFragment).commit();
            fragmentStack.push(waterfallFragment);

        } else if (id == R.id.nav_manage) {
            getFragmentManager().beginTransaction().replace(R.id.contentFragment, myClusterFragment).commit();
            fragmentStack.push(myClusterFragment);
        } else if(id==R.id.userView){
            Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
        } else if(id==R.id.nav_create){
            getFragmentManager().beginTransaction().replace(R.id.contentFragment,createGroupFragment).commit();
            fragmentStack.push(createGroupFragment);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && user!=null){
                NavigationView navigationView = findViewById(R.id.nav_view);
                View header=navigationView.getHeaderView(0);
                TextView username=header.findViewById(R.id.header_username);
                TextView email=header.findViewById(R.id.header_email);
                username.setText(user.username);
                email.setText(user.email);

                fragmentStack.removeAll(fragmentStack);

                if(user!=null){
                    ArrayList<Group> groups=SqlDao.getUserGroups(connection,user.uid);
                    ArrayList<Image> images=new ArrayList<>();
                    groups.forEach(group->{
                        ArrayList<Image> group_imgs=SqlDao.getImageByGroup(connection,group.group_id);
                        images.addAll(group_imgs);
                    });

                    TextView usernameV=header.findViewById(R.id.header_username);
                    TextView emailV=header.findViewById(R.id.header_email);
                    usernameV.setText(user.username);
                    emailV.setText(user.email);

                    waterfallFragment=WaterfallFragment.newInstance(images);
                }else{
                    Toast.makeText(this, R.string.login_hint, Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Main2Activity.this, LoginActivity.class);
                    startActivityForResult(intent,1);
                }
                getFragmentManager().beginTransaction().replace(R.id.contentFragment, waterfallFragment).commit();
                //Main2Activity activity=(Main2Activity)getActivity();
                fragmentStack.push(waterfallFragment);
            }else if(requestCode==Constant.upLoadRequestCode){
                handleImageOnKitKat(data);
            }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    public void openAlbum(int groupId) {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        this.groupId=groupId;
        startActivityForResult(intent,Constant.upLoadRequestCode);//打开相册
    }
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {

            Activity activty=this;

            ActivityCompat.requestPermissions(activty,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constant.upLoadRequestCode);
            return;
        }
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的Uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        upLoadImage(imagePath,groupId);
    }

    private void upLoadImage(String imagePath,int groupId) {
        final EditText et = new EditText(this);
        new AlertDialog.Builder(this).setTitle("添加图片描述")
                .setIcon(R.mipmap.ic_launcher)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //按下确定键后的事件
                        String content = et.getText().toString();
                        if (!TextUtils.isEmpty(content)) {
                            SqlDao.uploadImage(Main2Activity.connection,imagePath,content,groupId);
                        }
                        Toast.makeText(Main2Activity.this, "上传成功", Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("取消", null).show();
    }
//
//    private void handleImageBeforeKitKat(Intent data) {
//        Uri uri = data.getData();
//        String imagePath = getImagePath(uri, null);
//        diaplayImage(imagePath);
//    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (Constant.upLoadRequestCode) {
//            case 1:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    openAlbum(0);
//                } else {
//                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            default:
//                break;
//        }
        if (requestCode==Constant.upLoadRequestCode){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openAlbum(groupId);
            } else {
                Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case 1:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    openAlbum(0);
//                } else {
//                    Toast.makeText(getContext(), "You denied the permission", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            default:
//                break;
//        }
//    }
}
