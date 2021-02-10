package io.riskscanner.base.rs;

import com.huaweicloud.sdk.iam.v3.IamClient;
import com.huaweicloud.sdk.iam.v3.model.ShowCredential;
import com.huaweicloud.sdk.iam.v3.model.ShowPermanentAccessKeyRequest;
import io.riskscanner.commons.exception.RSException;

/**
 * @author maguohao
 */
public class AuthUtil {

    public static ShowCredential validate(IamClient client ,String ak) throws RSException {
        ShowPermanentAccessKeyRequest request = new ShowPermanentAccessKeyRequest();
        request.withAccessKey(ak);
        ShowCredential result = client.showPermanentAccessKey(request).getCredential();
        return result;
    }


}
