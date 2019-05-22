package com.jack90john.common.utils.http.client;

import lombok.Data;
import org.apache.http.Header;

/**
 * {@link HttpClientUtils}返回结果
 *
 * @author jack
 * @version 1.0.0
 * @since 2.0.0.RELEASE
 */

@Data
public class HttpClientResult {

    Integer status;     //http状态码
    String result;      //返回结果
    Header[] headers;   //头信息

}
