package com.nevs.car.tools.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import com.nevs.car.R;
import com.nevs.car.tools.interfaces.OnResponseListener;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;

import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession;

/**
 * Created by mac on 2018/7/3.
 */

public class WXShare {
    public static final String APP_ID = "wx6eebbcf8e0971eb0";
//    public static final String ACTION_SHARE_RESPONSE = "action_wx_share_response";
//    public static final String EXTRA_RESULT = "result";
//
//    private final Context context;
//    private final IWXAPI api;
//    private OnResponseListener listener;
//    private ResponseReceiver receiver;
//
//    public WXShare(Context context) {
//        api = WXAPIFactory.createWXAPI(context, APP_ID);
//        this.context = context;
//    }
//
//    public WXShare register() {
//        // 微信分享
//        api.registerApp(APP_ID);
//        receiver = new ResponseReceiver();
//        IntentFilter filter = new IntentFilter(ACTION_SHARE_RESPONSE);
//        context.registerReceiver(receiver, filter);
//        return this;
//    }
//
//    public void unregister() {
//        try {
//            api.unregisterApp();
//            context.unregisterReceiver(receiver);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public WXShare shareu(int type) {
//        WXTextObject textObject = new WXTextObject();
//        textObject.text = "https://www.nevs.com/";
//
//        WXMediaMessage msg = new WXMediaMessage();
//        msg.mediaObject = textObject;
//        msg.description = "国能官网";
//
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("text");
//        req.message = msg;
//        req.scene = type;
//
//        api.sendReq(req);
//        return this;
//    }
//
//
//    public WXShare share(String text) {
//        WXTextObject textObj = new WXTextObject();
//        textObj.text = text;
//
//        WXMediaMessage msg = new WXMediaMessage();
//        msg.mediaObject = textObj;
//        //        msg.title = "Will be ignored";
//        msg.description = text;
//
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("text");
//        req.message = msg;
//        req.scene = SendMessageToWX.Req.WXSceneSession;
//
//        boolean result = api.sendReq(req);
//        MLog.e("text shared: " + result);
//        return this;
//    }
//
    //flag用来判断是分享到微信好友还是分享到微信朋友圈，
    //0代表分享到微信好友，1代表分享到朋友圈
    public WXShare shareUrl(int flag, Context context, String url, String title, String descroption){
        //初始化一个WXWebpageObject填写url
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = url;
        //用WXWebpageObject对象初始化一个WXMediaMessage，天下标题，描述
        WXMediaMessage msg = new WXMediaMessage(webpageObject);
        msg.title = title;
        msg.description = descroption;
        //这块需要注意，图片的像素千万不要太大，不然的话会调不起来微信分享，
        //我在做的时候和我们这的UIMM说随便给我一张图，她给了我一张1024*1024的图片
        //当时也不知道什么原因，后来在我的机智之下换了一张像素小一点的图片好了！
        Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.zxing_nevs);
        msg.setThumbImage(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
        return this;
    }
    /**
     * Bitmap转换成byte[]并且进行压缩,压缩到不大于maxkb
     * @param bitmap
     ***@param *MAGE_SIZE
     * @return
     */
    public  byte[] bitmap2Bytes(Bitmap bitmap, int maxkb) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        int options = 100;
        while (output.toByteArray().length > maxkb&& options != 10) {
            output.reset(); //清空output
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, output);//这里压缩options%，把压缩后的数据存放到output中
            options -= 10;
        }
        return output.toByteArray();
    }

    public void sharePicture(Context mContext, int shareType) {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.zxing_nevs);
        WXImageObject imgObj = new WXImageObject(bitmap);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

//        Bitmap thumbBitmap =  Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
//        bitmap.recycle();
        msg.thumbData = bitmap2Bytes(bitmap,31);  //设置缩略图

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("imgshareappdata");
        req.message = msg;
        req.scene = shareType;
        api.sendReq(req);
    }

//
//
//
//
//    public IWXAPI getApi() {
//        return api;
//    }
//
//    public void setListener(OnResponseListener listener) {
//        this.listener = listener;
//    }
//
//    private String buildTransaction(final String type) {
//        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
//    }
//
//    private class ResponseReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Response response = intent.getParcelableExtra(EXTRA_RESULT);
//            MLog.e("type: " + response.getType());
//            MLog.e("errCode: " + response.errCode);
//            String result;
//            if (listener != null) {
//                if (response.errCode == BaseResp.ErrCode.ERR_OK) {
//                    listener.onSuccess();
//                } else if (response.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
//                    listener.onCancel();
//                } else {
//                    switch (response.errCode) {
//                        case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                            result = "发送被拒绝";
//                            break;
//                        case BaseResp.ErrCode.ERR_UNSUPPORT:
//                            result = "不支持错误";
//                            break;
//                        default:
//                            result = "发送返回";
//                            break;
//                    }
//                    listener.onFail(result);
//                }
//            }
//        }
//    }
//
//    public static class Response extends BaseResp implements Parcelable {
//
//        public int errCode;
//        public String errStr;
//        public String transaction;
//        public String openId;
//
//        private int type;
//        private boolean checkResult;
//
//        public Response(BaseResp baseResp) {
//            errCode = baseResp.errCode;
//            errStr = baseResp.errStr;
//            transaction = baseResp.transaction;
//            openId = baseResp.openId;
//            type = baseResp.getType();
//            checkResult = baseResp.checkArgs();
//        }
//
//        @Override
//        public int getType() {
//            return type;
//        }
//
//        @Override
//        public boolean checkArgs() {
//            return checkResult;
//        }
//
//
//        @Override
//        public int describeContents() {
//            return 0;
//        }
//
//        @Override
//        public void writeToParcel(Parcel dest, int flags) {
//            dest.writeInt(this.errCode);
//            dest.writeString(this.errStr);
//            dest.writeString(this.transaction);
//            dest.writeString(this.openId);
//            dest.writeInt(this.type);
//            dest.writeByte(this.checkResult ? (byte) 1 : (byte) 0);
//        }
//
//        protected Response(Parcel in) {
//            this.errCode = in.readInt();
//            this.errStr = in.readString();
//            this.transaction = in.readString();
//            this.openId = in.readString();
//            this.type = in.readInt();
//            this.checkResult = in.readByte() != 0;
//        }
//
//        public static final Creator<Response> CREATOR = new Creator<Response>() {
//            @Override
//            public Response createFromParcel(Parcel source) {
//                return new Response(source);
//            }
//
//            @Override
//            public Response[] newArray(int size) {
//                return new Response[size];
//            }
//        };
//    }


    public static final String ACTION_SHARE_RESPONSE = "action_wx_share_response";
    public static final String EXTRA_RESULT = "result";

    private final Context context;
    private final IWXAPI api;
    private OnResponseListener listener;
    private ResponseReceiver receiver;

    public WXShare(Context context) {
        api = WXAPIFactory.createWXAPI(context, APP_ID);
        this.context = context;
    }

    public WXShare register() {
        // 微信分享
        api.registerApp(APP_ID);
        receiver = new ResponseReceiver();
        IntentFilter filter = new IntentFilter(ACTION_SHARE_RESPONSE);
        context.registerReceiver(receiver, filter);
        return this;
    }

    public void unregister() {
        try {
            api.unregisterApp();
            context.unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WXShare share(String text) {
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        //        msg.title = "Will be ignored";
        msg.description = text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = WXSceneSession;

        boolean result = api.sendReq(req);
        MLog.e("text shared: " + result);
        return this;
    }
    public WXShare shareu(int type,String url,String title) {
        WXTextObject textObject = new WXTextObject();
        textObject.text = url;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObject;
        msg.description = title;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = type;

        api.sendReq(req);
        return this;
    }

    public IWXAPI getApi() {
        return api;
    }

    public void setListener(OnResponseListener listener) {
        this.listener = listener;
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private class ResponseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Response response = intent.getParcelableExtra(EXTRA_RESULT);
            MLog.e("type: " + response.getType());
            MLog.e("errCode: " + response.errCode);
            String result;
            if (listener != null) {
                if (response.errCode == BaseResp.ErrCode.ERR_OK) {
                    listener.onSuccess();
                } else if (response.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
                    listener.onCancel();
                } else {
                    switch (response.errCode) {
                        case BaseResp.ErrCode.ERR_AUTH_DENIED:
                            result = "发送被拒绝";
                            break;
                        case BaseResp.ErrCode.ERR_UNSUPPORT:
                            result = "不支持错误";
                            break;
                        default:
                            result = "发送返回";
                            break;
                    }
                    listener.onFail(result);
                }
            }
        }
    }

    public static class Response extends BaseResp implements Parcelable {

        public int errCode;
        public String errStr;
        public String transaction;
        public String openId;

        private int type;
        private boolean checkResult;

        public Response(BaseResp baseResp) {
            errCode = baseResp.errCode;
            errStr = baseResp.errStr;
            transaction = baseResp.transaction;
            openId = baseResp.openId;
            type = baseResp.getType();
            checkResult = baseResp.checkArgs();
        }

        @Override
        public int getType() {
            return type;
        }

        @Override
        public boolean checkArgs() {
            return checkResult;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.errCode);
            dest.writeString(this.errStr);
            dest.writeString(this.transaction);
            dest.writeString(this.openId);
            dest.writeInt(this.type);
            dest.writeByte(this.checkResult ? (byte) 1 : (byte) 0);
        }

        protected Response(Parcel in) {
            this.errCode = in.readInt();
            this.errStr = in.readString();
            this.transaction = in.readString();
            this.openId = in.readString();
            this.type = in.readInt();
            this.checkResult = in.readByte() != 0;
        }

        public static final Creator<Response> CREATOR = new Creator<Response>() {
            @Override
            public Response createFromParcel(Parcel source) {
                return new Response(source);
            }

            @Override
            public Response[] newArray(int size) {
                return new Response[size];
            }
        };
    }



}


