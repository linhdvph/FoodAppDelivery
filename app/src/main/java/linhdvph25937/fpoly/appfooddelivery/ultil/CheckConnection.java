package linhdvph25937.fpoly.appfooddelivery.ultil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

public class CheckConnection {
//    public static boolean haveNetworkConnection(Context context){
//        boolean haveConnectedWifi = false;
//        boolean haveConnectedMobile = false;
//
//        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
//        for (NetworkInfo ni : netInfo) {
//            if(ni.getTypeName().equalsIgnoreCase("WIFI"))
//                if (ni.isConnected())
//                    haveConnectedWifi = true;
//            if(ni.getTypeName().equalsIgnoreCase("MOBILE"))
//                if (ni.isConnected())
//                    haveConnectedMobile = true;
//        }
//
//        return haveConnectedWifi || haveConnectedMobile;
//    }

    public static boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false, haveConnectedMobile = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            // Kiểm tra kết nối Wi-Fi
            NetworkCapabilities wifiNetworkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            haveConnectedWifi = wifiNetworkCapabilities != null && wifiNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);

            // Kiểm tra kết nối dữ liệu di động
            NetworkCapabilities mobileNetworkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            haveConnectedMobile = mobileNetworkCapabilities != null && mobileNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
        }else{
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            return networkInfo != null && networkInfo.isConnectedOrConnecting();
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
