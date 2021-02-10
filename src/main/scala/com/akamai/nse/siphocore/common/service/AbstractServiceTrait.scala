package com.akamai.nse.siphocore.common.service

import com.akamai.nse.siphocore.common.AppContext

trait AbstractServiceTrait {
  lazy val appContext: AppContext = AppContext()
}
