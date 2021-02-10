package io.riskscanner.commons.constants;

import java.util.HashMap;
import java.util.Map;

public class RegionsConstants {

    public static Map<String, String> AwsMap;

    public static Map<String, String> AzureMap;

    public static Map<String, String> AliyunMap;

    public static Map<String, String> HuaweiMap;

    public static Map<String, String> TencentMap;

    static
    {
        AwsMap = new HashMap<>();
        // AWS 中国 start
        AwsMap.put("cn-north-1", "中国（北京）");
        AwsMap.put("cn-northwest-1", "中国（宁夏）");
        // AWS 中国 end

        // AWS 国际 start
        AwsMap.put("us-east-2", "美国东部（俄亥俄州）");
        AwsMap.put("us-east-1", "美国东部（弗吉尼亚北部）");
        AwsMap.put("us-west-1", "美国西部（加利福尼亚北部）");
        AwsMap.put("us-west-2", "美国西部（俄勒冈）");
        AwsMap.put("af-south-1", "非洲（开普敦）");
        AwsMap.put("ap-east-1", "亚太地区（香港）");
        AwsMap.put("ap-south-1", "亚太地区（孟买）");
        AwsMap.put("ap-northeast-3", "亚太区域（大阪当地）");
        AwsMap.put("ap-northeast-2", "亚太区域（首尔）");
        AwsMap.put("ap-southeast-1", "亚太区域（新加坡）");
        AwsMap.put("ap-southeast-2", "亚太区域（悉尼）");
        AwsMap.put("ap-northeast-1", "亚太区域（东京）");
        AwsMap.put("ca-central-1", "加拿大（中部）");
        AwsMap.put("eu-central-1", "欧洲（法兰克福）");
        AwsMap.put("eu-west-1", "欧洲（爱尔兰）");
        AwsMap.put("eu-west-2", "欧洲（伦敦）");
        AwsMap.put("eu-south-1", "欧洲（米兰）");
        AwsMap.put("eu-west-3", "欧洲（巴黎）");
        AwsMap.put("eu-north-1", "欧洲（斯德哥尔摩）");
        AwsMap.put("me-south-1", "中东（巴林）");
        AwsMap.put("sa-east-1", "南美洲（圣保罗）");
        // AWS 国际 end

        AzureMap = new HashMap<>();
        // Azure start
        AzureMap.put("AzureChinaCloud", "中国区");
        AzureMap.put("AzureCloud", "国际区");
        // Azure end

        AliyunMap = new HashMap<>();
        // Aliyun start
        AliyunMap.put("cn-qingdao", "华北 1（青岛）");
        AliyunMap.put("cn-beijing", "华北 2（北京）");
        AliyunMap.put("cn-zhangjiakou", "华北 3（张家口）");
        AliyunMap.put("cn-huhehaote", "华北 5（呼和浩特）");
        AliyunMap.put("cn-wulanchabu", "华北6（乌兰察布）");
        AliyunMap.put("cn-hangzhou", "华东 1（杭州）");
        AliyunMap.put("cn-shanghai", "华东 2（上海）");
        AliyunMap.put("cn-shenzhen", "华南 1（深圳）");
        AliyunMap.put("cn-heyuan", "华南2（河源）");
        AliyunMap.put("cn-guangzhou", "华南3（广州）");
        AliyunMap.put("cn-chengdu", "西南1（成都）");
        AliyunMap.put("cn-hongkong", "中国香港（香港）");
        AliyunMap.put("ap-northeast-1", "亚太东北 1 (东京)");
        AliyunMap.put("ap-southeast-1", "亚太东南 1 (新加坡)");
        AliyunMap.put("ap-southeast-2", "亚太东南 2 (悉尼)");
        AliyunMap.put("ap-southeast-3", "亚太东南 3 (吉隆坡)");
        AliyunMap.put("ap-southeast-5", "亚太东南 5 (雅加达)");
        AliyunMap.put("ap-south-1", "亚太南部 1 (孟买)");
        AliyunMap.put("us-east-1", "美国东部 1 (弗吉尼亚)");
        AliyunMap.put("us-west-1", "美国西部 1 (硅谷)");
        AliyunMap.put("eu-west-1", "英国 (伦敦)");
        AliyunMap.put("me-east-1", "中东东部 1 (迪拜)");
        AliyunMap.put("eu-central-1", "欧洲中部 1 (法兰克福)");
        // Aliyun end

        HuaweiMap = new HashMap<>();
        // Huawei start
        HuaweiMap.put("af-south-1", "非洲-约翰内斯堡");
        HuaweiMap.put("ap-southeast-1", "亚太-香港");
        HuaweiMap.put("ap-southeast-2", "亚太-曼谷");
        HuaweiMap.put("ap-southeast-3", "亚太-新加坡");
        HuaweiMap.put("cn-east-2", "华东-上海二");
        HuaweiMap.put("cn-east-3", "华东-上海一");
        HuaweiMap.put("cn-north-1", "华北-北京一");
        HuaweiMap.put("cn-north-4", "华北-北京四");
        HuaweiMap.put("cn-south-1", "华南-广州");
        HuaweiMap.put("cn-southwest-2", "西南-贵阳一");
        HuaweiMap.put("la-south-2", "拉美-圣地亚哥");
        HuaweiMap.put("sa-brazil-1", "拉美-圣保罗一");
        // Huawei end

        TencentMap = new HashMap<>();
        // Tencent start
        TencentMap.put("ap-bangkok", "亚太地区(曼谷)");
        TencentMap.put("ap-beijing", "华北地区(北京)");
        TencentMap.put("ap-chengdu", "西南地区(成都)");
        TencentMap.put("ap-chongqing", "西南地区(重庆)");
        TencentMap.put("ap-guangzhou", "华南地区(广州)");
        TencentMap.put("ap-guangzhou-open", "华南地区(广州Open)");
        TencentMap.put("ap-hongkong", "港澳台地区(中国香港)");
        TencentMap.put("ap-mumbai", "亚太地区(孟买)");
        TencentMap.put("ap-nanjing", "华东地区(南京)");
        TencentMap.put("ap-seoul", "亚太地区(首尔)");
        TencentMap.put("ap-shanghai", "华东地区(上海)");
        TencentMap.put("ap-shanghai-fsi", "华东地区(上海金融)");
        TencentMap.put("ap-shenzhen-fsi", "华南地区(深圳金融)");
        TencentMap.put("ap-singapore", "东南亚地区(新加坡)");
        TencentMap.put("ap-tokyo", "亚太地区(东京)");
        TencentMap.put("eu-frankfurt", "欧洲地区(法兰克福)");
        TencentMap.put("eu-moscow", "欧洲地区(莫斯科)");
        TencentMap.put("na-ashburn", "美国东部(弗吉尼亚)");
        TencentMap.put("na-siliconvalley", "美国西部(硅谷)");
        TencentMap.put("na-toronto", "北美地区(多伦多)");
        // Tencent end
    }

}
