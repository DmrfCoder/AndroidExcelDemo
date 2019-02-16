package cn.nuaa.dmrfcoder.androidexceldemo.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;

/**
 * @author dmrfcoder
 * @date 2019/2/14
 */

public class PermissionUtil {
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void RequestPermission(final Context context) {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (PermissionsUtil.hasPermission(context, permissions)) {
            //已经获取相关权限
        } else {
            PermissionsUtil.requestPermission((Activity) context, new PermissionListener() {
                @Override
                public void permissionGranted(@NonNull String[] permission) {
                }

                @Override
                public void permissionDenied(@NonNull String[] permission) {
                    Toast.makeText(context, "相关权限被拒绝，本应用将无法正常运行", Toast.LENGTH_SHORT).show();
                }
            }, permissions);
        }
    }
}
