

package app.vivek.inapppurchaselib.v3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import app.vivek.inapppurchaselib.R;
import app.vivek.inapppurchaselib.utils.IabHelper;
import app.vivek.inapppurchaselib.utils.IabResult;
import app.vivek.inapppurchaselib.utils.Inventory;
import app.vivek.inapppurchaselib.utils.Purchase;


/**
 * Activity is used for show the UI for in-app purchase and manage all the callback. 
 * @author Vivek Kumar
 *
 */
public class VKInAppPurchaseActivity extends Activity implements IabHelper.OnIabSetupFinishedListener,IabHelper.QueryInventoryFinishedListener,IabHelper.OnConsumeFinishedListener,IabHelper.OnIabPurchaseFinishedListener {

	// (arbitrary) request code for the purchase flow
	static final int RC_REQUEST = 10001;
	// The helper object
	private IabHelper mHelper;

	/** * Define the inapp product type like InApp or Subs	 */
	private String mInAppSKUType;
	/**	 * Define the product id	 */
	private String mSkuId;

	/** Product type Consumable or not <br/> if 0 then Consumable <br/>else Non consumable **/
	private int mSkuType;

	/** This is used for sending unique data at time of purchase and verify when data is purchase for security reason **/
	private String payload = "";
	/** Used for check the consume iteration count if once it is failed than check again  **/
	private int consumeIteration=0;
	/** This is used for managing the status of product purchase, When we move to consume the product which we have already owned <br/> if mPurchaseStatus=0 :- Only consuming product(Which already owned not purchase new product) <br/> else mPurchaseStatus=1:- First purchased product and than consume **/
	private int mPurchaseStatus=0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inapppurchase);
		mSkuId=getIntent().getExtras().getString(VKInAppConstants.INAPP_SKU_ID);
		mInAppSKUType=getIntent().getExtras().getString(VKInAppConstants.INAPP_SKU_TYPE);
		mSkuType=getIntent().getExtras().getInt(VKInAppConstants.INAPP_PRODUCT_TYPE);
		if (TextUtils.isEmpty(VKInAppProperties.BASE_64_KEY)) {
			setupResult(VKInAppConstants.ERROR_BASE_64_KEY_NOT_SETUP);
		}
		if (getPackageName().startsWith("com.example")) {
			setupResult(VKInAppConstants.ERROR_PACKAGE_NAME);
		}
		setupInAppPurhcase();

	}

	/**
	 * This method is used for setup InApp Setup on Device and check device is support InApp or not. 
	 */
	private void setupInAppPurhcase(){
		mHelper = new IabHelper(this, VKInAppProperties.BASE_64_KEY);
		// enable debug logging (for a production application, you should set this to false).
		mHelper.enableDebugLogging(true);
		// Start setup. This is asynchronous and the specified listener
		// will be called once setup completes.
		mHelper.startSetup(this);
	}

	@Override
	public void onIabSetupFinished(IabResult result) {
		VKLogger.error("Setup finished.");
		if (!result.isSuccess()) {
			// Oh noes, there was a problem.
			setupResult(VKInAppConstants.ERROR_DEVICE_NOT_SUPPORT_INAPP);
		}else{
			// Have we been disposed of in the meantime? If so, quit.
			if (mHelper == null) return;
			// IAB is fully set up. Now, let's get an inventory of stuff we own.
			VKLogger.error("Setup successful. Querying inventory.");

			mHelper.queryInventoryAsync(VKInAppPurchaseActivity.this);
		}
	}

	@Override
	public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
		if (mHelper == null) return;
		// Is it a failure?
		if (result.isFailure()) {
			setupResult(VKInAppConstants.ERROR_FAILED_QUERY_INVENTORY);
			return;
		}else{
			// Checking the InApp product type for consume(Only for inapp product not for subscription product) or not
			if(mInAppSKUType.equalsIgnoreCase(IabHelper.ITEM_TYPE_INAPP)){
				Purchase skuPurchase = inventory.getPurchase(mSkuId);
				if (skuPurchase != null && verifyDeveloperPayload(skuPurchase)) {
					// You have purchase this mSkuId but you did not consume it, if you need to purchase this item(mSkuId) for this you need to consume first after that you will purchase again.
					mHelper.consumeAsync(inventory.getPurchase(mSkuId), VKInAppPurchaseActivity.this);
					return;
				}else{
					//Go For Purchase
					mHelper.launchPurchaseFlow(this, mSkuId, RC_REQUEST,
							VKInAppPurchaseActivity.this, payload);
				}
			}else{ // For SubScription:--
				if (!mHelper.subscriptionsSupported()) {
					setupResult(VKInAppConstants.ERROR_DEVICE_NOT_SUPPORT_SUBS);
					return;
				}

				/* for security, generate your payload here for verification. See the comments on
				 *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
				 *        an empty string, but on a production app you should carefully generate this. */
				Purchase skuSubsPurchase = inventory.getPurchase(mSkuId);
				if (skuSubsPurchase != null && verifyDeveloperPayload(skuSubsPurchase)) {
					//TODO Return back to activity for
					setupResult(VKInAppConstants.RESULT_SUBS_CONTINUE,skuSubsPurchase);
				}else{
					//Go For Purchase
					mHelper.launchPurchaseFlow(this,
							mSkuId, IabHelper.ITEM_TYPE_SUBS,
							RC_REQUEST, VKInAppPurchaseActivity.this, payload);
				}
			}
		}


	}

	/**
	 * Method is used for sending the status of the inapp purchase to caller activity
	 * @param type
	 */
	private void setupResult(final int type){
		Intent mIntent=new Intent();
		Bundle mBundle=new Bundle();
		mBundle.putInt("response_code", type);
		mBundle.putString(VKInAppConstants.INAPP_SKU_ID, mSkuId);
		mBundle.putString(VKInAppConstants.INAPP_SKU_TYPE,mInAppSKUType);
		mBundle.putInt(VKInAppConstants.INAPP_PRODUCT_TYPE,mSkuType);
		mIntent.putExtras(mBundle);
		setResult(RESULT_OK,mIntent);
		finish();
	}



	/**
	 /**
	 * Method is used for sending the status of the inapp purchase to caller activity
	 * @param type
	 */
	private void setupResult(final int type,Purchase token){
		Intent mIntent=new Intent();
		Bundle mBundle=new Bundle();
		mBundle.putInt("response_code", type);
		mBundle.putString(VKInAppConstants.INAPP_SKU_ID, mSkuId);
		mBundle.putString(VKInAppConstants.INAPP_SKU_TYPE,mInAppSKUType);
		mBundle.putInt(VKInAppConstants.INAPP_PRODUCT_TYPE, mSkuType);
		mBundle.putSerializable(VKInAppConstants.INAPP_PURCHASE_INFO, token);

		mIntent.putExtras(mBundle);
		setResult(RESULT_OK,mIntent);
		finish();
	}

	/**
	 /**
	 * Method is used for sending the status of the inapp purchase to caller activity
	 * @param type
	 */
	private void setupResult(final int type,String token){
		Intent mIntent=new Intent();
		Bundle mBundle=new Bundle();
		mBundle.putInt("response_code", type);
		mBundle.putString(VKInAppConstants.INAPP_SKU_ID, mSkuId);
		mBundle.putString(VKInAppConstants.INAPP_SKU_TYPE,mInAppSKUType);
		mBundle.putInt(VKInAppConstants.INAPP_PRODUCT_TYPE, mSkuType);
		mBundle.putInt(VKInAppConstants.INAPP_PRODUCT_TYPE,mSkuType);
		mBundle.putString(VKInAppConstants.INAPP_PURCHASE_TOKEN, token);

		mIntent.putExtras(mBundle);
		setResult(RESULT_OK,mIntent);
		finish();
	}
	/* *  Verifies the developer payload of a purchase.
	 * <br/><br/>
	 * verify that the developer payload of the purchase is correct. It will be
	 * the same one that you sent when initiating the purchase.
	 *<br/><br/>
	 * WARNING: Locally generating a random string when starting a purchase and
	 * verifying it here might seem like a good approach, but this will fail in the
	 * case where the user purchases an item on one device and then uses your app on
	 * a different device, because on the other device you will not have access to the
	 * random string you originally generated.
	 *<br/>
	 * So a good developer payload has these characteristics:
	 *<br/>
	 * 1. If two different users purchase an item, the payload is different between them,
	 *    so that one user's purchase can't be replayed to another user.
	 *<br/>
	 * 2. The payload must be such that you can verify it even when the app wasn't the
	 *    one who initiated the purchase flow (so that items purchased by the user on
	 *    one device work on other devices owned by the user).
	 *<br/><br/>
	 * Using your own server to store and verify developer payloads across app
	 * installations is recommended.
	 * @param p
	 * @return boolean
	 */
	private boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();
		if(payload.equals(this.payload)){
			return true;
		}
		return false;
	}

	@Override
	public void onConsumeFinished(Purchase purchase, IabResult result) {
		VKLogger.error("Consumption finished. Purchase: " + purchase + ", result: " + result);
		// if we were disposed of in the meantime, quit.
		if (mHelper == null) return;

		if (result.isSuccess()) {
			// successfully consumed, so we apply the effects of the item in our
			VKLogger.info("Consumption successful. Provisioning.");
			//TODO Send the callback for calling activity/fragment
			if(mPurchaseStatus==0){
				setupResult(VKInAppConstants.RESULT_PRODUCT_CONSUME_SUCCESSFULLY,purchase);
			}else{
				setupResult(VKInAppConstants.RESULT_PRODUCT_PURCHASE_CONSUME_SUCCESSFULLY,purchase);
			}


		}
		else {
			if(consumeIteration==0){
				consumeIteration++;
				mHelper.consumeAsync(purchase, VKInAppPurchaseActivity.this);
			}else{
				//Send the call back when Item is not consume
				setupResult(VKInAppConstants.RESULT_PROPUR_SUCC_CONSUME_FAIL);
			}
		}
	}

	@Override
	public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
		VKLogger.info("Purchase finished: " + result + ", purchase: " + purchase);
		// if we were disposed of in the meantime, quit.
		if (mHelper == null) return;

		if (result.isFailure()) {
			setupResult(VKInAppConstants.ERROR_PRODUCT_PURCHASE);
			return;
		}
		if (!verifyDeveloperPayload(purchase)) {
			setupResult(VKInAppConstants.ERROR_FAILED_AUTHENTICATION_PRODUCT_PURCHASE);
			return;
		}

		VKLogger.info("Purchase successful.");
		if(purchase.getItemType().equalsIgnoreCase(IabHelper.ITEM_TYPE_INAPP)){
			if(mSkuType==0){
				mPurchaseStatus++;
				mHelper.consumeAsync(purchase, VKInAppPurchaseActivity.this);
			}else{
				setupResult(VKInAppConstants.RESULT_PROPUR_SUCC_CONSUME_FAIL);
			}
			/*Intent mIntent=new Intent();
			Bundle mBundle=new Bundle();
			if(mPurchaseStatus==0){
				mBundle.putInt("response_code", InAppConstants.RESULT_PRODUCT_CONSUME_SUCCESSFULLY);
			}else{
				mBundle.putInt("response_code", InAppConstants.RESULT_PRODUCT_PURCHASE_SUCCESSFULLY);
			}
			mBundle.putString(InAppConstants.INAPP_SKU_ID, mSkuId);
			mBundle.putString(InAppConstants.INAPP_SKU_TYPE,mInAppSKUType);
			mIntent.putExtras(mBundle);
			setResult(RESULT_OK,mIntent);
			finish();*/
		}else{  //TODO Subs Item Purchased
			setupResult(VKInAppConstants.RESULT_SUBS_CONTINUE,purchase);
		}

	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		VKLogger.error("onActivityResult(" + requestCode + "," + resultCode + "," + data);

		if (mHelper == null) return;

		// Pass on the activity result to the helper for handling
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			// not handled, so handle it ourselves (here's where you'd
			// perform any handling of activity results not related to in-app
			// billing...
			super.onActivityResult(requestCode, resultCode, data);
		}
		else {
			VKLogger.error("onActivityResult handled by IABUtil.");
		}
	}




	@Override
	public void onBackPressed() {
	}
}
