package com.yoho.myapplication;

import java.util.Random;

public class Config {

  //  public  static  String BASEURL="http://yohela.com/mobileApi/";
    public  static  String BASEURL="http://192.168.1.7/yoshna/";

    public static String INSERT_CART_URL=BASEURL+"cart_insert.php";
  public static String NOTIFY=BASEURL +"insert.php";
    public static String DELETE_CART_ITEM=BASEURL+"delete_cart_item.php";
    //public static String GET_CART_ITEM=BASEURL+"get_cart_item.php";
    public static String GET_CART_ITEM=BASEURL+"cartView_sqlite.php";
    public static String CATEGORY_ITEM_PARTICULAR=BASEURL+"category_item_particular.php";

    public static String ADD_ADDRESS=BASEURL+"add_address.php";//address page
    public static String GET_YOSHNA_ITEM=BASEURL+"get_yoshna_item.php";//home activity
    public static String GET_OHPAIR_ITEM=BASEURL+"get_ohpair_item.php";//home activity
    public static String GET_YOSHNA_CATEGORY=BASEURL+"get_yoshna_category.php";
    public static String GET_OHPAIR_CATEGORY=BASEURL+"get_ohpair_category.php";//ohpair activity

    public static String DETAILED_CATEGORY_ITEM=BASEURL+"detailed_category_item.php";


    public static String GET_SEARCH_ITEM=BASEURL+"search_item.php";

    public static String FETCH_IMAGE_YOSHNA=BASEURL+"flipper_images.php";//home activity
    public static String STORE_EXIST_CART=BASEURL+"store_exist_cart.php";//Login activity

    public static String DETAILED_IMAGES=BASEURL+"detailed_images.php";//Detailed activity
    public static String FORGETPIN=BASEURL+"flipper_images.php";//forgetpin  activity
    public static final String FORGET_PHONE_GET_URL = BASEURL+"getphonenumber.php";//forget passwor activity
    public static String REGISTER_URL=BASEURL+"register.php";//register activity

    public static String LOGIN_URL=BASEURL+"login_new.php";//Signin_login activity activity
    public static String GET_CART_DATA=BASEURL+"get_cart_data.php";//Signin_login activity activity

    public static String GET_ADDRESS=BASEURL+"get_address.php";//Payment activity
    public static String STORE_MY_ORDERS=BASEURL+"store_myorder.php";//Payment activity

    public static String STORE_MYORDER_ADDRESS=BASEURL+"store_myorder_address.php";//Payment activity
    public static String MYORDER_OVERVIEW=BASEURL+"myorder_overview.php";//myorder overview activity

    public static String GET_MYORDER=BASEURL+"get_myorder.php";//myorder activity
    public static String GET_MYORDER_IMAGE=BASEURL+"get_myorder_image.php";//myorder activity

    public static String SORTING=BASEURL+"sorting.php";//sorting dialoge activity

    public static final String KEY_PHONE = "phone";

    //for changing theme
    public static String THEME_NAME = "ohpair";
    public static String CATEGORY_NAME = "ohpair";

    //JSON Tag from response from server
    public static final String TAG_RESPONSE= "ErrorMessage";


    public static String rand_number()
    {
        int rand_num=0;
        Random random=new Random();
        for(int count=0;count<10;count++)
        {
            rand_num=random.nextInt(1000);
            System.out.println("Random number : "+rand_num);

        }
        return String.valueOf(rand_num);
    }
}
