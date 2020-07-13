package different.db;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser; // 현재 로그인된 유저 정보
    private static final String TAG="EmailPWD";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World!"); // key, value 형태로 저장

        mAuth = FirebaseAuth.getInstance();
        final EditText emailTxt = (EditText)findViewById(R.id.email);
        final EditText pwdTxt = (EditText)findViewById(R.id.password);
        final Button joinBtn = (Button)findViewById(R.id.button);
        final Button loginBtn = (Button)findViewById(R.id.button2);

        joinBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email = emailTxt.getText().toString();
                String pwd = pwdTxt.getText().toString();
                createAccount(email,pwd);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email = emailTxt.getText().toString();
                String pwd = pwdTxt.getText().toString();
                login(email,pwd);
            }
        });
    }

    @Override
    public void onStart(){ // 활동 초기화 시 사용자가 현재 로그인되어 있는지 확인
        super.onStart();

        currentUser = mAuth.getCurrentUser();
    }

    public void createAccount(String email, String password){
        Log.d(TAG,"계정 생성: "+email);

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){ // 계정 생성 성공시 UI
                            currentUser = mAuth.getCurrentUser();
                            Log.d(TAG,"계정 생성: 성공");
                            Toast.makeText(MainActivity.this,"성공",Toast.LENGTH_SHORT).show();

                        }else{
                            try{
                                throw task.getException();
                            }catch(FirebaseAuthWeakPasswordException e){
                                Toast.makeText(MainActivity.this,"비밀번호 간단함",Toast.LENGTH_SHORT).show();
                            }catch(FirebaseAuthInvalidCredentialsException e){
                                Toast.makeText(MainActivity.this,"이메일형식 아님",Toast.LENGTH_SHORT).show();
                            }catch(FirebaseAuthUserCollisionException e){
                                Toast.makeText(MainActivity.this,"존재하는 이메일",Toast.LENGTH_SHORT).show();
                            }catch(Exception e){
                                Toast.makeText(MainActivity.this,"Retry...",Toast.LENGTH_SHORT).show();
                            }
                            Log.w(TAG,"계정 생성: 실패",task.getException());
                            Toast.makeText(MainActivity.this,"실패",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void login(String email, String pwd){
        mAuth.signInWithEmailAndPassword(email,pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            currentUser = mAuth.getCurrentUser();
                            Log.d(TAG,"로그인: 성공");
                            Toast.makeText(MainActivity.this,"로그인 성공",Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this,currentUser.getEmail(),Toast.LENGTH_SHORT).show();

                        }else{
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                Toast.makeText(MainActivity.this,"존재하지 않는 id 입니다." ,Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(MainActivity.this,"이메일 형식이 맞지 않습니다." ,Toast.LENGTH_SHORT).show();
                            } catch (FirebaseNetworkException e) {
                                Toast.makeText(MainActivity.this,"Firebase NetworkException" ,Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this,"Exception" ,Toast.LENGTH_SHORT).show();
                            }
                            Log.w(TAG,"로그인: 실패",task.getException());
                            Toast.makeText(MainActivity.this,"로그인 실패",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}