package com.rongyant.commonlib.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by XRY on 2016/9/1.
 */
public class ActivityUtils {
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int fgmId, @Nullable String tag) {
        if (fragmentManager == null || fragment == null) {
            return;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
////遍历隐藏所有添加的fragment
//        for (Fragment fragment1 : fragmentManager.getFragments()) {
//            transaction.hide(fragment1);
//        }
        if (tag == null) {
            transaction.add(fgmId, fragment);
        } else {
            transaction.add(fgmId, fragment, tag);
        }



        transaction.commit();
    }



    public static void showFragment(@NonNull FragmentManager fragmentManager,
                                    @NonNull Fragment fragment){
        if (fragmentManager == null || fragment == null) {
            return;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.show(fragment);

        transaction.commit();
    }

    public static void replaceFragment(FragmentManager manager, Fragment to, int fgmId, @Nullable String tag) {
        FragmentTransaction transaction = manager.beginTransaction();
        //遍历隐藏所有添加的fragment
        for (Fragment fragment : manager.getFragments()) {
            transaction.hide(fragment);
        }
        if (!to.isAdded()) { //若没有添加过
            if (tag == null) {
                transaction.add(fgmId, to).commit();
            } else {
                transaction.add(fgmId, to, tag).commit();
            }
        } else { //若已经添加
            transaction.show(to).commit();
        }

    }
}
