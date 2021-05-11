package io.riskscanner.base.rs;

import com.google.gson.Gson;
import io.riskscanner.proxy.Request;
import io.riskscanner.proxy.huawei.HuaweiCloudCredential;

/**
 * @Author maguohao
 **/
public class IamRequest extends Request {

    private HuaweiCloudCredential huaweiCloudCredential;


    public HuaweiCloudCredential getHuaweiCloudCredential() {
        if (null == huaweiCloudCredential){
            huaweiCloudCredential = new Gson().fromJson(getCredential(), HuaweiCloudCredential.class);
        }
        return huaweiCloudCredential;
    }

    public void setHuaweiCloudCredential(HuaweiCloudCredential huaweiCloudCredential) {
        this.huaweiCloudCredential = huaweiCloudCredential;
    }
}
