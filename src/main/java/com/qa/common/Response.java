package com.qa.common;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author Tesla.liu
 * @Date 2023/12/19
 * @Description 全局响应类
 */

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
	public static final String SUCCESS_CODE = "0";
	public static final String SUCCESS_MSG = "success";
	public static final String ERR_CODE = "10000";
	public static final String ERR_MSG = "fail";
	public static final String FAIL_CODE = "1";

	protected String resultCode;
	protected String resultMsg;
	protected T data;

	public Response(T data) {
		this.resultCode = SUCCESS_CODE;
		this.resultMsg = SUCCESS_MSG;
		this.data = data;
	}

	public Response(String resultCode, String resultMsg) {
		this.resultMsg = resultMsg;
		this.resultCode = resultCode;
	}

	public static Response<Void> success() {
		Response<Void> response = new Response<>();
		response.setResultCode(SUCCESS_CODE);
		response.setResultMsg(SUCCESS_MSG);
		return response;
	}

	public static final Response<Void> VOID = new Response((Object) null);
}
