package app.vivek.inapppurchaselib.v3;

/**
 * Constants file for managing the result status.
 * @author Vivek Kumar
 *
 */
public class VKInAppConstants {

	/**
	 * Error code if the Google Play Base64 Key is not setup in the AppProperties.java
	 */
	public static final int ERROR_BASE_64_KEY_NOT_SETUP=50001;
	/**
	 * Error code if Application main package name start with com.example
	 */
	public static final int ERROR_PACKAGE_NAME=50002;
	/**
	 * Error code if Device is not supporting the INAPP V3
	 */
	public static final int ERROR_DEVICE_NOT_SUPPORT_INAPP=50003;
	/**
	 * Error code if Product is failed.
	 */
	public static final int ERROR_PRODUCT_PURCHASE=50004;
	/**
	 * Error code if Payload is different from query payload while purchasing.
	 */
	public static final int ERROR_FAILED_AUTHENTICATION_PRODUCT_PURCHASE=50005;
	/**
	 * Result code if product is purchase and consume successfully.
	 */
	public static final int RESULT_PRODUCT_PURCHASE_CONSUME_SUCCESSFULLY=50006;
	/**
	 * Result code if Product is consume successfully.
	 */
	public static final int RESULT_PRODUCT_CONSUME_SUCCESSFULLY=50007;
	/**
	 * Result code if Product is purchase successfully but Consumption is failed.
	 */
	public static final int RESULT_PROPUR_SUCC_CONSUME_FAIL=50008;
	/**
	 * Error code if Device is not supported Subscription.
	 */
	public static final int ERROR_DEVICE_NOT_SUPPORT_SUBS=50009;
	/**
	 * Result Code if Subscription is continue.
	 */
	public static final int RESULT_SUBS_CONTINUE=50010;
	/**
	 * Error code if Inventory failed.
	 */
	public static final int ERROR_FAILED_QUERY_INVENTORY=50011;

	
	/**
	 * Constant for SKU(Product) Type i.e.
	 * <br/> inapp<br/> subs
	 */
	public static final  String INAPP_SKU_TYPE="SKU_TYPE";
	/**
	 * Constant for SKU(Product) ID.
	 */
	public static final String INAPP_SKU_ID="SKU_ID";
	/**
	 * Constant for inapp product type like(Consumable or not)
	 * if value = 0 than Consumable 
	 * else Non Consumable product
	 */
	public static final String INAPP_PRODUCT_TYPE="PRODUCT_TYPE";

	/**
	 * Constant for Purchase token after purchase the product.
	 */
	public static final String INAPP_PURCHASE_TOKEN="PRODUCT_TOKEN";


	/**
	 * Constant 0 for Consumable InApp Product Item.
	 */
	public static final int INAPP_CONSUMABLE=0;

	/**
	 * Constant 1 for Non Consumable InApp Product Item.
	 */
	public static final int INAPP_NON_CONSUMABLE=1;


}
