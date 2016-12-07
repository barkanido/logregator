package com.tikal.logregator;


public class LogProcessor {
    public static void main(String[] args) {
        System.out.println(LogEntry.parse("2016-12-06T00:25:18.965682Z clicks 173.59.36.238:64740 10.10.22.225:6555 0.000021 0.004194 0.000021 302 302 0 0 \"GET https://app.appsflyer.com:443/id512393983?pid=taptica_int&af_click_lookback=7d&c=iOS_Taptica&tt_cid=79688f751fdb4408a1fa82c45c573613&tt_adv_id=2600&af_siteid=1720&idfa=82D9E6D9-0335-45D6-992E-52F8D6839CB3&sha1_idfa=&mac=&sha1_mac=&af_cpi=4.50000&af_sub1=3730_ HTTP/1.1\" \"Mozilla/5.0 (iPhone; CPU iPhone OS 10_1_1 like Mac OS X) AppleWebKit/602.2.14 (KHTML, like Gecko) Mobile/14B100\" ECDHE-RSA-AES128-GCM-SHA256 TLSv1.2"));
    }
}
