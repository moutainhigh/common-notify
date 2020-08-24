package com.developcollect.commonnotify.notify;

import com.developcollect.dcinfra.utils.CollectionUtil;
import lombok.Data;

import java.util.Collection;

/**
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2020/8/24 16:34
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Data
public abstract class BaseNotifyParameter implements NotifyParameter {

    private String templateSymbol;

    private Collection<String> tos;


    public void setTos(Collection<String> tos) {
        this.tos = CollectionUtil.distinct(tos);
    }

}
