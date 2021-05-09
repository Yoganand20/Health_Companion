package com.project.healthcompanion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginNSignUpActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private FragmentStateAdapter fragmentStateAdapter;
    private String[] Titles=new String[]{
            "Login","Sign Up"
    };





    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_n_sign_up);

        /*Setting Tabs and fragments*/
        //get viewPager view
        viewPager=findViewById(R.id.viewPager_ls);
        //initialise and set adapter object to viewPager view
        fragmentStateAdapter=new LS_ViewPagerAdapter(this);
        viewPager.setAdapter(fragmentStateAdapter);

        //set tabLayoutMediator to viewPager and tabLayout view
        TabLayout tabLayout=findViewById(R.id.tabLayout_ls);
        new TabLayoutMediator(tabLayout,viewPager,((tab, position) -> tab.setText(Titles[position]))).attach();



        /*Hide and show google sign in option when keyboard is opened or closed*/
        //get outer constraint layout
        constraintLayout=findViewById(R.id.constraintlayout_ls);
        //set global layout listener on constraint layout
        constraintLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            @Override
            public void onGlobalLayout(){
                Rect r=new Rect();
                constraintLayout.getWindowVisibleDisplayFrame(r);
                int screenHeight=constraintLayout.getRootView().getHeight();
                int keypadHeight=screenHeight-r.bottom;
                ConstraintLayout loginOptions=findViewById(R.id.constraintLayout_loginOptions);
                if(keypadHeight>screenHeight*0.15){
                    loginOptions.setVisibility(View.GONE);
                }else{
                    loginOptions.setVisibility(View.VISIBLE);
                }
        }});



    }

    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem()==0){
            super.onBackPressed();
        } else{
            viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
        }
    }

}