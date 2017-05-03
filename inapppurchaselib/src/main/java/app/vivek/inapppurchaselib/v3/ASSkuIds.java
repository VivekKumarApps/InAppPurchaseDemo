package app.vivek.inapppurchaselib.v3;

/**
 * This is an object representation of the Passport object in Google Play Here
 * you can store the items ID (its SKU) it's price and any other fields
 * <br/>
 * <b>Id's</b> for Sku should be start with <b>SKU_INAPP</b> for InApp product and <b>SKU_SUBS</b> for InAPP Subscription.
 * 
 * <br/<br/>For Testing purpose you can change the SKU_ID with <b>android.test.purchased</b> but not for subscription 
 * @author Vivek Kumar(Appstudioz)
 
 */
public class ASSkuIds {

	public static final String SKU_INAPP_TEST = "android.test.purchased";
	public static final String SKU_INAPP_5 = "app_inapp_5";//android.test.purchased
	public static final String SKU_INAPP_10 = "android.test.purchased";
	public static final String SKU_SUBS_MONTHLY = "appsinvo_monthly_sub_test";
	public static final String SKU_SUBS_ONE_DAY = "appsinvo_day_sub_test";
	public static final String SKU_INAPP_UPGRADE = "app_inapp_upgrade";
}
