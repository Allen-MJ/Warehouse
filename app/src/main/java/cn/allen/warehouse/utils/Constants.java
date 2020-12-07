package cn.allen.warehouse.utils;

import cn.allen.warehouse.R;

public class Constants {
    public static final String Url = "http://122.112.179.223:8033/api/FlowerApi/";
//    public static final String Url = "http://39.108.216.58:8033/api/FlowerApi/";

    public static final String UserId = "_User_Id";
    public static final String UserName = "_User_Name";
    public static final String UserAccount = "_User_Account";
    public static final String UserPsw = "_User_Password";
    public static final String UserCreateTime = "_User_Create_Time";
    public static final String UserPhone = "_User_Phone";
    public static final String UserWareHouse = "_User_Ware_House";
    public static final String UserType = "_User_Type";

    public static int getStatusResId(int status){
        int resId = 0;
        switch (status){
            case 1:
                resId = R.mipmap.ic_logo_06;
                break;
            case 2:
                resId = R.mipmap.ic_logo_02;
                break;
            case 3:
                resId = R.mipmap.ic_logo_04;
                break;
            case 4:
                resId = R.mipmap.ic_logo_28;
                break;
            case 5:
                resId = R.mipmap.ic_logo_21;
                break;
        }
        return resId;
    }

    public static String getStatusName(int status){
        String name = "";
        switch (status){
            case 1:
                name = "待配货";
                break;
            case 2:
                name = "待出库";
                break;
            case 3:
                name = "待回库";
                break;
            case 4:
                name = "已回库";
                break;
            case 5:
                name = "完成清点";
                break;
        }
        return name;
    }
}
