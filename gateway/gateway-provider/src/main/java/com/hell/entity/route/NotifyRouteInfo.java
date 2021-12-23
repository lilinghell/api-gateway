package com.hell.entity.route;

import lombok.Data;

@Data
public class NotifyRouteInfo {
    //类型：delete、refresh、refreshAll
    private String type;
    //api
    private NotifyApiRoute apiRoute;
    // 处理后的回调url
    private String callBackUrl;
}
