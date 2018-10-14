package as.bwei.com.reglogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import as.bwei.com.reglogin.bean.User;
import as.bwei.com.reglogin.presenter.AccountPresenter;
import as.bwei.com.reglogin.view.AccountView;

public class MainActivity extends AppCompatActivity implements AccountView,View.OnClickListener{

    private AccountPresenter presenter;
    private EditText edit_name;
    private EditText edit_password;
    private Button but_reg;
    private Button but_login;
    private String edit_name1;
    private String edit_password1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        onClick();
    }

    private void onClick() {
        but_login.setOnClickListener(this);
        but_reg.setOnClickListener(this);
    }

    private void initView() {
        but_reg = (Button) findViewById(R.id.but_reg);
        but_login = (Button) findViewById(R.id.but_login);
        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_password = (EditText) findViewById(R.id.edit_password);
        presenter = new AccountPresenter(MainActivity.this);
    }

    @Override
    public void onClick(View v) {
        edit_name1 = edit_name.getText().toString();
        edit_password1 = edit_password.getText().toString();
        switch (v.getId()){
            case R.id.but_login:
                presenter.login(edit_name1,edit_password1);
                break;
            case R.id.but_reg:
                presenter.reg(edit_name1,edit_password1);
                break;
        }
    }

    /*成功以后获取返回值吐司*/
    @Override
    public void showSuccess(final String msg) {
        //需要在线程中执行
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showError(final String msg) {
        //需要在线程中执行
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void login_success(String msg) {
        if (msg.equals("1")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this,"手机号或者密码错误",Toast.LENGTH_SHORT).show();
                }
            });
        }else {

            Intent intent = new Intent(MainActivity.this, ShowActivity.class);
            startActivity(intent);
        }
    }

    private void saveData(User user){
        SharedPreferences sp=getSharedPreferences("user",MODE_PRIVATE);
        sp.edit().putString("name",user.getData().getMobile())
                .putString("password",user.getData().getMobile()).commit();
        if (user.getData().getNickname()!=null){
            sp.edit().putString("usernickname",user.getData().getNickname().toString());
        }
    }

}
