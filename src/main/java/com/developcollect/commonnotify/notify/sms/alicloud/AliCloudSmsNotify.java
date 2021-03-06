package com.developcollect.commonnotify.notify.sms.alicloud;

import cn.hutool.core.lang.Assert;
import com.aliyuncs.CommonResponse;
import com.developcollect.commonnotify.NotifyContext;
import com.developcollect.commonnotify.SendResult;
import com.developcollect.commonnotify.notify.AbstractNotify;
import com.developcollect.commonnotify.notify.sms.SmsNotifyConfig;
import com.developcollect.commonnotify.notify.sms.SmsNotifyParameter;
import com.developcollect.commonnotify.notify.sms.SmsNotifyResult;
import com.developcollect.commonnotify.utils.sms.AliCloudSmsUtil;
import com.developcollect.dcinfra.utils.SerializeUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 阿里云短信通知
 */
@Slf4j
public class AliCloudSmsNotify extends AbstractNotify<SmsNotifyParameter, SmsNotifyResult> {


    /**
     * 阿里云短信发送
     * https://help.aliyun.com/document_detail/101414.html?spm=a2c4g.11186623.2.13.27af3e2cVWQqXz
     *
     * @param context
     */
    @Override
    protected SmsNotifyResult send(NotifyContext context) {
        SmsNotifyConfig notifyConfig = context.getNotifyConfig();
        SmsNotifyParameter notifyParameter = context.getNotifyParameter();

        String regionId = notifyConfig.getPlatformConfig(AliCloudSmsConstants.REGION_ID);
        String accessKeyId = notifyConfig.getPlatformConfig(AliCloudSmsConstants.ACCESS_KEY_ID);
        String accessSecret = notifyConfig.getPlatformConfig(AliCloudSmsConstants.ACCESS_SECRET);

        String signName = notifyParameter.getExt(AliCloudSmsConstants.SIGN_NAME);
        String smsUpExtendCode = notifyParameter.getExt(AliCloudSmsConstants.SMS_UP_EXTEND_CODE);


        CommonResponse response = AliCloudSmsUtil.send(
                regionId, accessKeyId, accessSecret,
                notifyParameter.getTos(), signName,
                notifyParameter.getTemplateSymbol(), notifyParameter.getMessageTemplateValueMap(),
                smsUpExtendCode
        );
        return buildNotifyResult(context, response);
    }

    @Override
    protected void validParameter(SmsNotifyParameter notifyParameter) {
        super.validParameter(notifyParameter);
        Assert.notBlank(notifyParameter.getExt(AliCloudSmsConstants.REGION_ID), "阿里云短信RegionId不能为空");
        Assert.notBlank(notifyParameter.getExt(AliCloudSmsConstants.ACCESS_KEY_ID), "阿里云短信accessKeyId不能为空");
        Assert.notBlank(notifyParameter.getExt(AliCloudSmsConstants.ACCESS_SECRET), "阿里云短信accessSecret不能为空");

        Assert.notBlank(notifyParameter.getExt(AliCloudSmsConstants.SIGN_NAME), "阿里云短信签名不能为空");
    }

    private SmsNotifyResult buildNotifyResult(NotifyContext context, CommonResponse response) {
        SmsNotifyResult smsNotifyResult = new SmsNotifyResult();

        String data = response.getData();
        HashMap dataMap = SerializeUtil.jsonToBean(data, HashMap.class);

        List<SendResult> sendResults = context.getNotifyParameter().getTos().stream().map(to -> {
            SendResult sendResult = new SendResult();
            sendResult.setMessageId(dataMap.get("BizId").toString());
            sendResult.setSuccess("OK".equals(dataMap.get("Code")));
            sendResult.setCode(dataMap.get("Code").toString());
            sendResult.setCodeDesc(dataMap.get("Message").toString());
            sendResult.setRecipient(to);
            return sendResult;
        }).collect(Collectors.toList());

        smsNotifyResult.setRawResult(response);
        smsNotifyResult.setSendResults(sendResults);

        return smsNotifyResult;
    }
}
