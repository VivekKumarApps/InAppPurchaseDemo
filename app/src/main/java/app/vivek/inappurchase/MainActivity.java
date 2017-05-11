package app.vivek.inappurchase;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import app.vivek.inapppurchaselib.utils.IabHelper;
import app.vivek.inapppurchaselib.utils.Purchase;
import app.vivek.inapppurchaselib.v3.VKInAppConstants;
import app.vivek.inapppurchaselib.v3.VKInAppProperties;
import app.vivek.inapppurchaselib.v3.VKInAppPurchaseActivity;
import app.vivek.inapppurchaselib.v3.VKLogger;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_consumable_real).setOnClickListener(onConsumableRealProduct);
        findViewById(R.id.btn_consumable_test).setOnClickListener(onConsumableTestProduct);
        findViewById(R.id.btn_subscription_real).setOnClickListener(onSubscriptionRealProduct);
        findViewById(R.id.btn_non_consumable_real).setOnClickListener(onNonConsumeableRealProduct);

        VKInAppProperties.BASE_64_KEY="";

    }

    private View.OnClickListener onConsumableRealProduct=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent mIntent=new Intent(MainActivity.this, VKInAppPurchaseActivity.class);
            mIntent.putExtra(VKInAppConstants.INAPP_SKU_ID, SkuIds.SKU_INAPP_5);
            mIntent.putExtra(VKInAppConstants.INAPP_SKU_TYPE, IabHelper.ITEM_TYPE_INAPP);
            mIntent.putExtra(VKInAppConstants.INAPP_PRODUCT_TYPE,VKInAppConstants.INAPP_CONSUMABLE);
            startActivityForResult(mIntent, 101);
        }
    };


    private View.OnClickListener onConsumableTestProduct=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO: 2/13/2016   For check perpose
            Intent mIntent=new Intent(MainActivity.this, VKInAppPurchaseActivity.class);
            mIntent.putExtra(VKInAppConstants.INAPP_SKU_ID, SkuIds.SKU_INAPP_TEST);
            mIntent.putExtra(VKInAppConstants.INAPP_SKU_TYPE,IabHelper.ITEM_TYPE_INAPP);
            mIntent.putExtra(VKInAppConstants.INAPP_PRODUCT_TYPE,VKInAppConstants.INAPP_CONSUMABLE);
            startActivityForResult(mIntent, 101);
        }
    };


    private View.OnClickListener onSubscriptionRealProduct=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent mIntent=new Intent(MainActivity.this, VKInAppPurchaseActivity.class);
            mIntent.putExtra(VKInAppConstants.INAPP_SKU_ID, SkuIds.SKU_SUBS_ONE_DAY);
            mIntent.putExtra(VKInAppConstants.INAPP_SKU_TYPE,IabHelper.ITEM_TYPE_SUBS);
            mIntent.putExtra(VKInAppConstants.INAPP_PRODUCT_TYPE,0);
            startActivityForResult(mIntent, 101);
        }
    };


    private View.OnClickListener onNonConsumeableRealProduct=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent mIntent=new Intent(MainActivity.this, VKInAppPurchaseActivity.class);
            mIntent.putExtra(VKInAppConstants.INAPP_SKU_ID, SkuIds.SKU_INAPP_UPGRADE);
            mIntent.putExtra(VKInAppConstants.INAPP_SKU_TYPE,IabHelper.ITEM_TYPE_SUBS);
            mIntent.putExtra(VKInAppConstants.INAPP_PRODUCT_TYPE,VKInAppConstants.INAPP_NON_CONSUMABLE);
            startActivityForResult(mIntent, 101);
        }
    };





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==101){
                String mInapSkuId=data.getExtras().getString(VKInAppConstants.INAPP_SKU_ID);
                if(data.getExtras().getString(VKInAppConstants.INAPP_PURCHASE_TOKEN)!=null)
                    VKLogger.e("Purchase Token",data.getExtras().getString(VKInAppConstants.INAPP_PURCHASE_TOKEN));
                Purchase purchaseReceipt;
                if(data.getExtras().containsKey(VKInAppConstants.INAPP_PURCHASE_INFO)){
                    purchaseReceipt= (Purchase) data.getExtras().getSerializable(VKInAppConstants.INAPP_PURCHASE_INFO);
                    VKLogger.e("OrderId:- "+purchaseReceipt.getOrderId()+"\n Token:-"+purchaseReceipt.getToken());
                }

                // {"productId":"appsinvo_day_sub_test","type":"subs","price":"₹ 10.00","price_amount_micros":10000000,"price_currency_code":"INR","title":"OneDaySubscription (InApp Test)","description":"Testing Purpose"}
                int value=data.getExtras().getInt("response_code");
                switch (value) {
                    case VKInAppConstants.RESULT_PRODUCT_CONSUME_SUCCESSFULLY:
                        responseAlertDialog("You have successfully consume "+mInapSkuId+" product.");
                        break;
                    case VKInAppConstants.RESULT_PRODUCT_PURCHASE_CONSUME_SUCCESSFULLY:
                        responseAlertDialog("You have successfully purchase "+mInapSkuId+" product.");
                        break;
                    case VKInAppConstants.RESULT_PROPUR_SUCC_CONSUME_FAIL:
                        responseAlertDialog("You have failed to consume "+mInapSkuId+" product.");
                        break;
                    case VKInAppConstants.RESULT_SUBS_CONTINUE:
                        responseAlertDialog("Your subsription is continue for id "+mInapSkuId+" product.");
                        break;
                    case VKInAppConstants.ERROR_DEVICE_NOT_SUPPORT_SUBS:
                        responseAlertDialog(getString(R.string.error_msg_not_support_subs));
                        break;
                    case VKInAppConstants.ERROR_BASE_64_KEY_NOT_SETUP:
                        responseAlertDialog(getString(R.string.error_msg_base64key));
                        break;
                    case VKInAppConstants.ERROR_PACKAGE_NAME:
                        responseAlertDialog(getString(R.string.error_msg_package_name));
                        break;
                    case VKInAppConstants.ERROR_DEVICE_NOT_SUPPORT_INAPP:
                        responseAlertDialog(getString(R.string.error_msg_not_support_inapp));
                        break;
                    case VKInAppConstants.ERROR_PRODUCT_PURCHASE:
                        responseAlertDialog(getString(R.string.error_msg_in_purchase));
                        break;

                    default:
                        responseAlertDialog("Error is occured "+value);
                        break;
                }


            }
        }
    }


    /**
     * Show the InApp purchase status dialog
     * @param message
     */
    private void responseAlertDialog(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setCancelable(false);
        bld.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        bld.create().show();
    }
}
