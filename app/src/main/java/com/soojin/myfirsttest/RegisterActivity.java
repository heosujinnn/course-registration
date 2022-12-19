package com.soojin.myfirsttest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private ArrayAdapter adapter;
    private Spinner spinner;

    private String userID;
    private String userPassword;
    private String userGender;
    private String userMajor;
    private String userEmail;
    private AlertDialog dialog;
    private boolean validate=false; //사용할 수 있는 아이디 체크




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        spinner=(Spinner)findViewById(R.id.majorSpinner);
        //학과들 리소스에서 가져와서 넣을 수 있게한다.
        adapter=ArrayAdapter.createFromResource(this,R.array.major, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        final EditText idText=(EditText) findViewById(R.id.idText);
        final EditText passwordText=(EditText) findViewById(R.id.passwordText);
        final EditText emailText=(EditText) findViewById(R.id.emailText);

        RadioGroup genderGroup=(RadioGroup) findViewById(R.id.genderGroup);
        int genderGroupID=genderGroup.getCheckedRadioButtonId();
        userGender=((RadioButton)findViewById(genderGroupID)).getText().toString();

        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton genderButton=(RadioButton) findViewById(i);
                userGender=genderButton.getText().toString();
            }
        });

        //회원가입시 아이디 사용가능한지
        final Button validateButton =(Button)findViewById(R.id.validateButton);
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(RegisterActivity.this,"ok",Toast.LENGTH_SHORT).show();
                String userID=idText.getText().toString();
                if(validate){
                    return; //ok
                }
                if(userID.equals("")){
                    AlertDialog.Builder builder=new AlertDialog.Builder(RegisterActivity.this);
                    dialog=builder.setMessage("아이디는 빈 칸일 수 없습니다.")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }
                //중복체크
                Response.Listener<String> responseListener = new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        try{
                            Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();

                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){//사용할 수 있는 아이디라면
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용가능한 아이디입니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                idText.setEnabled(false);//아이디값을 바꿀 수 없도록 함
                                validate = true;//검증완료
                                idText.setBackgroundColor(getResources().getColor(R.color.colorGray));
                                validateButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
                            }else{//사용할 수 없는 아이디라면
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("이미 존재하는 아이디 입니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }

                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };//Response.Listener 완료

                //Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분
                ValidateRequest validateRequest = new ValidateRequest(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(validateRequest);
            }
        });

        Button registerButton=(Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID=idText.getText().toString();
                String userPassword=passwordText.getText().toString();

                String userMajor=spinner.getSelectedItem().toString();
                String userEmail=emailText.getText().toString();

                if(!validate){
                    AlertDialog.Builder builder=new AlertDialog.Builder(RegisterActivity.this);
                    dialog=builder.setMessage("먼저 중복 체크를 해 주세요")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }
                if(userID.equals("")||userPassword.equals("")||userMajor.equals("")||userEmail.equals("")){
                    AlertDialog.Builder builder=new AlertDialog.Builder(RegisterActivity.this);
                    dialog=builder.setMessage("빈칸없이 입력해주세요")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;

                }


                Response.Listener<String>responseListener=new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse=new JSONObject(response);
                            boolean success=jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder=new AlertDialog.Builder(RegisterActivity.this);
                                dialog=builder.setMessage("회원등록에 성공하였습니다")
                                        .setPositiveButton("확인",null)
                                        .create();
                                dialog.show();
                                finish();
                            }
                            else{
                                AlertDialog.Builder builder=new AlertDialog.Builder(RegisterActivity.this);
                                dialog=builder.setMessage("회원등록에 실패했습니다.")
                                        .setNegativeButton("확인",null)
                                        .create();
                                dialog.show();

                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                RegisterRequest registerRequest=new RegisterRequest(userID,userPassword,userGender,userMajor,userEmail,responseListener);
                RequestQueue queue= Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);

            }
        });
    }
    //회원 등록이 이루어진 이후에 회원 등록창이 꺼지게 되는
    @Override
    protected void onStop (){
        super .onStop();
        if(dialog!=null){
            dialog.dismiss();
            dialog=null;
        }
    }
}