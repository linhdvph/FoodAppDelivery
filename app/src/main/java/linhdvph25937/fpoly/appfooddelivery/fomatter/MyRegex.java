package linhdvph25937.fpoly.appfooddelivery.fomatter;

public class MyRegex {
    public static final String PHONE_NUMBER_VN_REGEX = "(84|0[1|3|5|7|8|9])+([0-9]{8})\\b";
    public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,20}$";
    public static final String NAME_REGEX = "^[a-zA-Z0-9_.-]{6,20}$";

}
