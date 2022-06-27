package com.bestinyu.wowo.admin.controller;

import com.bestinyu.wowo.common.ResponseTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseController {

    protected static Logger logger = LoggerFactory.getLogger(BaseController.class);

    protected ResponseTO success(Object data) {
        return new ResponseTO(1, data);
    }

}
