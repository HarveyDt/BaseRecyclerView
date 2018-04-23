/*
******************************* Copyright (c)*********************************\
**
**                 (c) Copyright 2015, Allen, china, shanghai
**                          All Rights Reserved
**
**                          
**                         
**-----------------------------------版本信息------------------------------------
** 版    本: V0.1
**
**------------------------------------------------------------------------------
********************************End of Head************************************\
*/
package com.harvey.view.baserecyclerviewdemo;

import android.app.Application;
import com.harvey.view.baserecyclerviewdemo.util.Utils;

/**
 * MyApplication
 *
 * @author yuyang
 *         2018/4/18
 *         下午5:30
 */

public class MyApplication extends Application {
  private static MyApplication appContext;

  public static MyApplication getInstance() {
    return appContext;
  }

  @Override public void onCreate() {
    super.onCreate();
    appContext = this;
    Utils.init(this);
  }
}
