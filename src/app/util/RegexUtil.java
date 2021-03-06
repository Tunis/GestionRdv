package app.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//This class perform to validate a String via regex : mail, tel, etc ...
public class RegexUtil {
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
		    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	public static final Pattern VALID_TEL_REGEX = 
		    Pattern.compile("^[0-9.]+$", Pattern.CASE_INSENSITIVE);
	public static final Pattern VALID_INT_FIELD_REGEX = 
		    Pattern.compile("^[0-9]+$", Pattern.CASE_INSENSITIVE);
	public static final Pattern VALID_FLOAT_FIELD_REGEX = 
		    Pattern.compile("^[0-9.]+$", Pattern.CASE_INSENSITIVE);
	
	public static boolean validateMail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
	}
	
	public static boolean validateTel(String tel) {
        Matcher matcher = VALID_TEL_REGEX .matcher(tel);
        return matcher.find();
	}
	
	public static boolean validateIntField(String field) {
        Matcher matcher = VALID_INT_FIELD_REGEX .matcher(field);
        return matcher.find();
	}
	
	public static boolean validateFloatField(String field) {
        Matcher matcher = VALID_FLOAT_FIELD_REGEX .matcher(field);
        return matcher.find();
	}
}
