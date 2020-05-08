package com.demomodel.bean.result;
/**
 * result = Result.success(dataMap);// 成功，并返回数据和retCode和message  
  
// result = Result.success();// 成功，不返回数据，只返回retCode和message  
  
// result = Result.error(CodeMsg.SERVER_EXCEPTION);// 失败返回错误信息  
  
// result = Result.error(CodeMsg.SERVER_EXCEPTION,e.toString());// 失败返回错误+扩展错误信息  
 * @author wgz
 * @date 创建时间：2020年4月21日 下午8:21:08
 */
public class Result<T> {  
    private String message;  
    private int code;  
    private T data;  
    
    private Result(T data) {  
        this.code = 20;  
        this.message = "成功";  
        this.data = data;  
    }  
    private Result(CodeMsg cm) {  
        if(cm == null){  
            return;  
        }  
        this.code = cm.getCode();  
        this.message = cm.getMessage();  
    }  
    /**  
     * 成功时候的调用  
     * @return  
     */  
    public static <T> Result<T> success(T data){  
        return new Result<T>(data);  
    }  
    /**  
     * 成功，不需要传入参数  
     * @return  
     */  
    @SuppressWarnings("unchecked")  
    public static <T> Result<T> success(){  
        return (Result<T>) success("");  
    }  
    /**  
     * 失败时候的调用  
     * @return  
     */  
    public static <T> Result<T> error(CodeMsg cm){  
        return new Result<T>(cm);  
    }  
    /**  
     * 失败时候的调用,扩展消息参数  
     * @param cm  
     * @param msg  
     * @return  
     */  
    public static <T> Result<T> error(CodeMsg cm,String msg){  
        cm.setMessage(cm.getMessage()+"--"+msg);  
        return new Result<T>(cm);  
    }  
    public T getData() {  
        return data;  
    }  
    public String getMessage() {  
        return message;  
    }
	public int getCode() {
		return code;
	}
	
    
}  