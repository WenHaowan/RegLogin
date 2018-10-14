package as.bwei.com.reglogin.presenter;

import as.bwei.com.reglogin.model.AccountModel;
import as.bwei.com.reglogin.view.AccountView;

/**
 * Created by HP on 2018/10/13.
 */

public class AccountPresenter {
    //v层引用
    private AccountView view;
    //model引用
    private AccountModel model;

    public AccountPresenter(AccountView view) {
        this.view = view;
        model = new AccountModel();
    }

    public void reg(String name,String password){
        model.reg(name, password, new AccountModel.AccountCallback() {
            @Override
            public void onSuccess(String msg) {
                view.showSuccess(msg);
            }

            @Override
            public void onError(String errorMsg) {
                view.showError("注册失败");
            }
        });
    }
    public void login(String name,String password){
        model.login(name, password, new AccountModel.AccountCallback() {
            @Override
            public void onSuccess(String msg) {
                view.login_success(msg);
            }

            @Override
            public void onError(String errorMsg) {
                view.showError("登录失败");
            }
        });
    }
}
