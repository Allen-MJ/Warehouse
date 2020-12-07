package cn.allen.warehouse.data;

public class Api {
    public static final String Login = "Login";//登录 get
    public static final String GetAllOrder = "GetAllOrder";//二、	根据登录人查询所有订单（get）
    public static final String GetInformation = "GetInformation";//三、	消息通知   （get）
    public static final String GetSeachOrder = "GetSeachOrder";//四、	根据订单号和时间查询订单（get）
    public static final String GetOrderState = "GetOrderState";//五、	根据订单状态查询订单（get）
    public static final String GetGrade = "GetGrade";//六、	查询鲜花的大类小类（销售端 get）
    public static final String GetFlowers = "GetFlowers";//七、	根据小类别查询鲜花
    public static final String GetSeachFlower = "GetSeachFlower";//八、	根据时间和名称查询花材
    public static final String PlacingOrder = "Placing_order";//九、	下单（销售端：post）
    public static final String UpdateOrder = "UpdateOrder";//十、	修改订单（销售端：post）
    public static final String GetOrder = "GetOrder";//十一、	添加主订单号（销售端：get）
    public static final String AdditionalOrder = "Additional_order";//十二、	追加订单（销售端：post）
    public static final String SubmitReturn = "Submit_Return";//十三、	已回库（销售端：get）
    public static final String ReturnOrder = "Return_order";//十四、	根据订单号查询订单内容（销售端/仓库端：get）
    public static final String GetCustomerName = "GetCustomer_name";//十五、	根据客户名称查询订单
    public static final String IsFlowerTrue = "IsFlowerTrue";//十六、	鲜花确认配送（仓库端：get）
    public static final String WatingSubmit = "WatingSubmit";//十七、	待配货提交（仓库端：get）
    public static final String WarehouseOut = "WarehouseOut";//十八、	确认出库 （仓库端：get）
    public static final String ImgUpload = "ImgUpload";//十九、	上传图片（仓库端：get）
    public static final String Recover = "Recover";//二十、	确认回收（仓库端：post）
    public static final String Writeloss = "Writeloss";//二十一、	填写回收损耗数据（仓库端：post）
    public static final String GetState = "GetState";//二十二、	获取各订单状态数量之和（仓库端：get）
    public static final String GetStates = "GetStates";//二十二、	获取各订单状态数量之和（仓库端：get）
    public static final String GetAllType = "GetAllType";//二十二、	获取各订单状态数量之和（仓库端：get）
    public static final String GetRed = "GetRed";//二十二、	获取各订单状态数量之和（仓库端：get）
    public static final String GetDetail = "GetDetail";//	获取订单详情（销售端）
    public static final String Delete = "Delete";//	删除订单中的花（销售端）
    public static final String SetSign = "SetSign";//	标记（仓库端）
    public static final String jiezhang = "SetIs_ornot";//结账
}
