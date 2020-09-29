package travelplanner.model;


/**
 * response object that'll be returned to front-end
 * it wraps a responseObj, which can be an AttractionResult
 * or an AttractionDetail. Together with a responseCode and 
 * a message(usually for error)
 */
@lombok.Data
public class BaseResponse<T> {
	protected String responseCode;
	protected T responseObj;
	protected String message;

	public BaseResponse(String responseCode, T responseObj, String message) {
		this.responseCode = responseCode;
		this.responseObj = responseObj;
		this.message = message;
	}
}
