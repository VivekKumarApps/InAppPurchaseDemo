# InAPP Purchase Lib #

InAppPurchase Library is an android in-app purchase library though with, we can purchase the Product on the Google Play. We can also implement the subscription for the recurring purchase. 

### How to Use ###

Steps:-

* Put your google play key (Base 64 InApp key) in the VKInAppProperties.java (app.vivek.inapppurchaselib.v3).

* Open the google play developer console and move to in-app product options and create the inapp product id(for inapp product or subscription).

* Put these ids in the SkuIds.java(appstudioz.android.inappbiling3) or you can test the inapp product by placing the id like "android.test.purchased".

* For Purchasing any product on any view click put the following code in onClick() method (like MainActivity.java) :-
    
	
```
#!java

Intent mIntent=new Intent(MainActivity.this, InAppPurchaseActivity.class);  //InAppPurchaseActivity.class handle all the callback releated to the in-app purchase.
mIntent.putExtra(InAppConstants.INAPP_SKU_ID, SkuIds.SKU_INAPP_5);          //Pass your SkuId(Product Id)
mIntent.putExtra(InAppConstants.INAPP_SKU_TYPE,IabHelper.ITEM_TYPE_INAPP);  //Pass the SkuType(Product type :- inapp or subs)
mIntent.putExtra(InAppConstants.INAPP_PRODUCT_TYPE,VKInAppConstants.INAPP_CONSUMABLE);  // Constant for inapp product type like(Consumable or not) if value = INAPP_CONSUMABLE than Consumable else Non Consumable product
startActivityForResult(mIntent, 101); 	// Open the activity for result
```
Implement the onActivityResult() method, where you will be get all callback related to status of InApp purchase.
	
* After getting the result in onActivityResult() method update your server as per your requirement.