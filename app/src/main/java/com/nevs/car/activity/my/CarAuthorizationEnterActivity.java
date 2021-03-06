package com.nevs.car.activity.my;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ispr.uilibrary.DialogUIUtils;
import com.ispr.uilibrary.listener.DialogUIDateTimeSaveListener;
import com.ispr.uilibrary.widget.DateSelectorWheelView;
import com.nevs.car.R;
import com.nevs.car.constant.Constant;
import com.nevs.car.tools.Base.BaseActivity;
import com.nevs.car.tools.CustomDatePicker;
import com.nevs.car.tools.SharedPreferencesUtil.SharedPHelper;
import com.nevs.car.tools.interfaces.DialogHintListener;
import com.nevs.car.tools.rx.HttpRxListener;
import com.nevs.car.tools.rx.HttpRxUtils;
import com.nevs.car.tools.rx.TspRxListener;
import com.nevs.car.tools.rx.TspRxUtils;
import com.nevs.car.tools.util.ActivityUtil;
import com.nevs.car.tools.util.ClickUtil;
import com.nevs.car.tools.util.DialogUtils;
import com.nevs.car.tools.util.HashmapTojson;
import com.nevs.car.tools.util.MLog;
import com.nevs.car.tools.util.MyUtils;
import com.nevs.car.tools.view.SwitchButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CarAuthorizationEnterActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.text_time)
    TextView textTime;
    @BindView(R.id.text_times)
    TextView textTimes;
    @BindView(R.id.text_alias)
    EditText textAlias;

    @BindView(R.id.id_switch)
    SwitchButton idSwitch;
    @BindView(R.id.switch_two)
    SwitchButton switchTwo;
    @BindView(R.id.switch_three)
    SwitchButton switchThree;
    @BindView(R.id.switch_four)
    SwitchButton switchFour;
    @BindView(R.id.switch_five)
    SwitchButton switchFive;
    @BindView(R.id.switch_six)
    SwitchButton switchSix;
    @BindView(R.id.text_number)
    TextView textNumber;
    @BindView(R.id.n_view)
    RelativeLayout nView;
    private String auvin = "";
    private int a = 0;
    private int b = 0;
    private int c = 0;
    private int d = 0;
    private int e = 0;
    private int f = 0;
    private List<Integer> aa = new ArrayList();
    private List<Integer> bb = new ArrayList();
    private List<Integer> li = new ArrayList();

    private CustomDatePicker datePicker, timePicker;
    private String time;
    private String date;
    private int poss = 0;
    private String targetUserAccount = "";

    @Override
    public int getContentViewResId() {
        return R.layout.activity_car_authorization_enter;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        MyUtils.setPadding(nView,mContext);
        initSwichClick();
        initIntent();

        initPicker();
    }

    private void initPicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        time = sdf.format(new Date());
        date = time.split(" ")[0];
//        //设置当前显示的日期
//        currentDate.setText(date);
//        //设置当前显示的时间
//        currentTime.setText(time);

        /**
         * 设置年月日
         */
        datePicker = new CustomDatePicker(this, "请选择日期", new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                //  currentDate.setText(time.split(" ")[0]);

            }
        }, "2007-01-01 00:00", time);
        datePicker.showSpecificTime(false); //显示时和分
        datePicker.setIsLoop(false);
        datePicker.setDayIsLoop(true);
        datePicker.setMonIsLoop(true);

        timePicker = new CustomDatePicker(this, getResources().getString(R.string.title), new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                // currentTime.setText(time);
                showTime(time);
            }
        }, "2007-01-01 00:00", "2027-12-31 23:59");//"2027-12-31 23:59"
        timePicker.showSpecificTime(true);
        timePicker.setIsLoop(true);

//        // 日期格式为yyyy-MM-dd
//        datePicker.show(date);
//        // 日期格式为yyyy-MM-dd HH:mm
//        timePicker.show(time);
    }

    private void showTime(String time) {
        MLog.e("选择的时间：" + time);
        String sub = time.substring(0, time.length() - 3);
        String selectedDate = time.substring(0, time.length() - 6);
        switch (poss) {
            case 1:
                long cc = HashmapTojson.getStringToDates(sub+":59:59", "yyyy-MM-dd HH:mm:ss");
                MLog.e("选择时间戳：" + HashmapTojson.getStringToDates(selectedDate, "yyyy-MM-dd"));
               // long nowZero = MyUtils.getTimesnight() - 24 * 3600;
                if (cc >= MyUtils.timeStampNow()) {
                    textTimes.setText(sub + ":00");
                } else {
                    ActivityUtil.showToast(mContext, getResources().getString(R.string.pleasebigtiome));
                }

                MLog.e("当前的时间戳：" + MyUtils.timeStampNow());
                MLog.e("当天24点的时间戳：" + MyUtils.getTimesnight());
                break;
            case 2:
                String hours = time.substring(0, time.length() - 3) + ":00:00";
                if (textTimes.getText().length() > 0) {
                    long nows = HashmapTojson.getStringToDates(hours, "yyyy-MM-dd HH:mm:ss");
                    long editDatasText = HashmapTojson.getStringToDates(textTimes.getText().toString() + ":00:00", "yyyy-MM-dd HH:mm:ss");
                    if (nows > editDatasText) {
                        textTime.setText(sub + ":00");
                    } else {
                        ActivityUtil.showToast(mContext, getResources().getString(R.string.time_hint));
                    }

                } else {
                    textTime.setText(sub);
                }
                break;
        }


    }

    private void initSwichClick() {
        idSwitch.setOnCheckedChangeListener(this);
        switchTwo.setOnCheckedChangeListener(this);
        switchThree.setOnCheckedChangeListener(this);
        switchFour.setOnCheckedChangeListener(this);
        switchFive.setOnCheckedChangeListener(this);
        switchSix.setOnCheckedChangeListener(this);
    }

    private void initIntent() {
        auvin = getIntent().getStringExtra("AUVIN");
        List cc = new ArrayList();
        cc.addAll((Collection) getIntent().getSerializableExtra("permissions"));

        for (int i = 0; i < cc.size(); i++) {
            MLog.e("ssssssssss" + cc.get(i));
        }

        for (int i = 0; i < cc.size(); i++) {
            aa.add((int) Math.ceil((Double) cc.get(i)));
        }
        for (int j = 0; j < aa.size(); j++) {
            switch (aa.get(j)) {
                case 1:
                    a = 1;
                    break;
                case 2:
                    b = 2;
                    break;
                case 3:
                    c = 3;
                    break;
                case 4:
                    d = 4;
                    break;
                case 5:
                    e = 5;
                    break;
                case 6:
                    f = 6;
                    break;
            }
        }
        MLog.e("权限：" + aa.size());
        initWitch();  //初始化按钮
        //   textTime.setText(getIntent().getStringExtra("endTime"));
        textTime.setText(HashmapTojson.getTimez(getIntent().getStringExtra("endTime"), "yyyy-MM-dd HH") + ":00");
        textTimes.setText(HashmapTojson.getTimez(getIntent().getStringExtra("startTime"), "yyyy-MM-dd HH") + ":00");
        //textNumber.setText(getIntent().getStringExtra("targetUserAccount"));
        textAlias.setText(getIntent().getStringExtra("nickName"));
        if (getIntent().getStringExtra("targetUserAccount") == null) {
            return;
        }
        targetUserAccount = getIntent().getStringExtra("targetUserAccount");
        getIDPhone(getIntent().getStringExtra("targetUserAccount"));
    }

    private void getIDPhone(String targetUserAccount) {
        DialogUtils.loading(mContext, true);
        HttpRxUtils.getIdPhone(mContext,
                new String[]{"UserID", "accessToken"},
                new Object[]{
                        targetUserAccount,
                        new SharedPHelper(mContext).get(Constant.ACCESSTOKEN, "")
                },
                new HttpRxListener() {
                    @Override
                    public void onSucc(Object obj) {
                        DialogUtils.hidding((Activity) mContext);
                        String phone = String.valueOf(obj);
                        textNumber.setText(phone);
                    }

                    @Override
                    public void onFial(String str) {
                        DialogUtils.hidding((Activity) mContext);
                    }
                }

        );
    }

    private void initWitch() {
        for (int i = 0; i < aa.size(); i++) {
            switch (aa.get(i)) {
                case 1:
                    idSwitch.setChecked(true);
                    a = 1;
                    break;
                case 2:
                    switchTwo.setChecked(true);
                    b = 2;
                    break;
                case 3:
                    switchThree.setChecked(true);
                    c = 3;
                    break;
                case 4:
                    switchFour.setChecked(true);
                    d = 4;
                    break;
                case 5:
                    switchFive.setChecked(true);
                    e = 5;
                    break;
                case 6:
                    switchSix.setChecked(true);
                    f = 6;
                    break;
            }
        }
    }

    private void getInt() {
        if (a != 0) {
            li.add(a);
        }
        if (b != 0) {
            li.add(b);
        }
        if (c != 0) {
            li.add(c);
        }
        if (d != 0) {
            li.add(d);
        }
        if (e != 0) {
            li.add(e);
        }
        if (f != 0) {
            li.add(f);
        }
        if (li.size() != 0) {
            for (int i = 0; i < li.size(); i++) {
                bb.add(li.get(i));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.back, R.id.remove, R.id.next, R.id.rel_time, R.id.rel_times, R.id.text_number, R.id.text_alias})
    public void onViewClicked(View view) {
        if (!ClickUtil.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.remove:
                DialogUtils.hint1(mContext, false, getResources().getString(R.string.isunbind), new DialogHintListener() {
                    @Override
                    public void callBack() {
                        getTsp11();
                    }
                });

                break;
            case R.id.next:
                getInt();
                if (textNumber.getText().toString().trim().length() == 0) {
                    ActivityUtil.showToast(mContext, getResources().getString(R.string.toast_nevsnub));
                } else if (textAlias.getText().toString().trim().length() == 0) {
                    ActivityUtil.showToast(mContext, getResources().getString(R.string.toast_name));
                } else if (bb.size() == 0) {
                    ActivityUtil.showToast(mContext, getResources().getString(R.string.toast_settingaut));
                } else {
                    getTsp9();
                }
                break;
            case R.id.rel_times:
                //showDialogs(1);
                poss = 1;
                timePicker.show(time);
                break;
            case R.id.rel_time:
                //showDialogs(2);
                poss = 2;
                timePicker.show(time);
                break;
            case R.id.text_number:
                //  textNumber.setCursorVisible(true);
                break;
            case R.id.text_alias:
                textAlias.setCursorVisible(true);
                break;
        }
    }


    private void showDialogs(final int id) {
        DialogUIUtils.showDatePick(mContext, Gravity.CENTER, getResources().getString(R.string.toast_choosedate), System.currentTimeMillis() + 60000, DateSelectorWheelView.TYPE_YYYYMMDD, 0, new DialogUIDateTimeSaveListener() {
            @Override
            public void onSaveSelectedDate(int tag, String selectedDate) {
                switch (id) {
                    case 1:
                        long cc = HashmapTojson.getStringToDates(selectedDate, "yyyy-MM-dd");
                        MLog.e("选择时间戳：" + HashmapTojson.getStringToDates(selectedDate, "yyyy-MM-dd"));
                        long nowZero = MyUtils.getTimesnight() - 24 * 3600;
                        if (cc >= nowZero) {
                            textTimes.setText(selectedDate);
                        } else {
                            ActivityUtil.showToast(mContext, getResources().getString(R.string.pleasebigtiome));
                        }

                        MLog.e("当前的时间戳：" + MyUtils.timeStampNow());
                        MLog.e("当天24点的时间戳：" + MyUtils.getTimesnight());


                        break;
                    case 2:
                        textTime.setText(selectedDate);
                        break;
                }


            }
        }).show();
    }

    private void getTsp9() {
        /**
         * {"resultMessage":"Service Success","resultDescription":"AuthorizeUser success"}
         * */
        DialogUtils.loading(CarAuthorizationEnterActivity.this, true);
        TspRxUtils.getAuthorize(this,
                new String[]{Constant.TSP.CONTENTTYPE, Constant.TSP.ACCEPT, Constant.TSP.AUTHORIZATION},
                new Object[]{Constant.TSP.CONTENTTYPEVALUE, Constant.TSP.ACCEPTVALUE, "Bearer" + " " + new SharedPHelper(CarAuthorizationEnterActivity.this).get(Constant.ACCESSTOKENS, "")},
                new String[]{"vin", "nickName", "targetUserAccount", "startTime", "endTime", "permissions"},
                new Object[]{auvin,
                        textAlias.getText().toString().trim(),
                        targetUserAccount,
                        HashmapTojson.getTimeSecond(HashmapTojson.getStringToDate(textTimes.getText().toString() + ":00", "yyyy-MM-dd HH:mm")),
                        HashmapTojson.getTimeSecond(HashmapTojson.getStringToDate(textTime.getText().toString() + ":00", "yyyy-MM-dd HH:mm")),
                        bb},
                new TspRxListener() {
                    @Override
                    public void onSucc(Object obj) {
                        DialogUtils.hidding(CarAuthorizationEnterActivity.this);
                        MyUtils.upLogTSO(mContext, "授权车辆", String.valueOf(obj), MyUtils.getTimeNow(), MyUtils.getTimeNow(), "", MyUtils.timeStampNow() + "");

                        ActivityUtil.showToast(CarAuthorizationEnterActivity.this, getResources().getString(R.string.safesuc));
                        finish();
                    }

                    @Override
                    public void onFial(String str) {
                        DialogUtils.hidding(CarAuthorizationEnterActivity.this);
                        if (str.contains("400") || str.contains("无效的请求")) {
                            ActivityUtil.showToast(mContext, getResources().getString(R.string.arthorfail));
                        } else if (str.contains("500") || str.contains("无效的网址")) {
                            ActivityUtil.showToast(mContext, getResources().getString(R.string.neterror));
                        } else if (str.contains("未授权的请求")) {
                            MyUtils.exitToLongin(mContext);
                        } else if (str.contains("401")) {
                            MyUtils.exitToLongin401(mContext);
                        } else if (str.contains("连接超时")) {
                            ActivityUtil.showToast(mContext, getResources().getString(R.string.timeout));
                        } else {
                            //  ActivityUtil.showToast(mContext, str);
                        }
                        MyUtils.upLogTSO(mContext, "授权车辆", String.valueOf(str), MyUtils.getTimeNow(), MyUtils.getTimeNow(), "", MyUtils.timeStampNow() + "");

                    }
                }
        );

    }

    private void getTsp11() {
        /**
         *{"resultMessage":"Service success","resultDescription":"Revoke success"}
         * */
        DialogUtils.loading(CarAuthorizationEnterActivity.this, true);
        TspRxUtils.getRemoke(this,
                new String[]{Constant.TSP.CONTENTTYPE, Constant.TSP.ACCEPT, Constant.TSP.AUTHORIZATION},
                new Object[]{Constant.TSP.CONTENTTYPEVALUE, Constant.TSP.ACCEPTVALUE, "Bearer" + " " + new SharedPHelper(CarAuthorizationEnterActivity.this).get(Constant.ACCESSTOKENS, "")},
                new String[]{"items"},
                new Object[]{new String[]{getIntent().getStringExtra("bindingId")}},
                new TspRxListener() {
                    @Override
                    public void onSucc(Object obj) {
                        DialogUtils.hidding(CarAuthorizationEnterActivity.this);
                        MyUtils.upLogTSO(mContext, "解除授权车辆", String.valueOf(obj), MyUtils.getTimeNow(), MyUtils.getTimeNow(), "", MyUtils.timeStampNow() + "");

                        ActivityUtil.showToast(CarAuthorizationEnterActivity.this, getResources().getString(R.string.toast_revoke));
                        finish();
                    }

                    @Override
                    public void onFial(String str) {
                        DialogUtils.hidding(CarAuthorizationEnterActivity.this);
                        if (str.contains("400") || str.contains("无效的请求")) {
                            ActivityUtil.showToast(mContext, getResources().getString(R.string.ungfail));
                        } else if (str.contains("500") || str.contains("无效的网址")) {
                            ActivityUtil.showToast(mContext, getResources().getString(R.string.neterror));
                        } else if (str.contains("未授权的请求")) {
                            MyUtils.exitToLongin(mContext);
                        } else if (str.contains("401")) {
                            MyUtils.exitToLongin401(mContext);
                        } else if (str.contains("连接超时")) {
                            ActivityUtil.showToast(mContext, getResources().getString(R.string.timeout));
                        } else {
                            //  ActivityUtil.showToast(mContext, str);
                        }
                        MyUtils.upLogTSO(mContext, "解除授权车辆", String.valueOf(str), MyUtils.getTimeNow(), MyUtils.getTimeNow(), "", MyUtils.timeStampNow() + "");

                    }
                }
        );

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.id_switch:
                if (isChecked) {
                    a = 1;
                } else {
                    a = 0;
                }
                MLog.e("a=" + a);
                break;
            case R.id.switch_two:
                if (isChecked) {
                    b = 2;
                } else {
                    b = 0;
                }
                MLog.e("b=" + b);
                break;
            case R.id.switch_three:
                if (isChecked) {
                    c = 3;
                } else {
                    c = 0;
                }
                MLog.e("c=" + c);
                break;
            case R.id.switch_four:
                if (isChecked) {
                    d = 4;
                } else {
                    d = 0;
                }
                MLog.e("d=" + d);
                break;
            case R.id.switch_five:
                if (isChecked) {
                    e = 5;
                } else {
                    e = 0;
                }
                MLog.e("e=" + e);
                break;
            case R.id.switch_six:
                if (isChecked) {
                    f = 6;
                } else {
                    f = 0;
                }
                MLog.e("f=" + f);
                break;
        }
    }
}
