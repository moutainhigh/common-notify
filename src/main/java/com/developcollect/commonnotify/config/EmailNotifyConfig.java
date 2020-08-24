package com.developcollect.commonnotify.config;

import cn.hutool.extra.mail.MailAccount;
import lombok.Data;

import java.util.function.Supplier;

/**
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2020/8/24 14:58
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Data
public class EmailNotifyConfig extends AbstractNotifyConfig {

    private Supplier<MailAccount> mailAccountSupplier;

    // 收件人
    // 抄送
    // 密送
    // 标题
    // 正文
    // 附件

}
