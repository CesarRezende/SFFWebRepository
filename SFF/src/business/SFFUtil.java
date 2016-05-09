package business;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.Entity;

import org.json.JSONArray;

import data.GenericEntity;

public class SFFUtil {

	public enum DateFormat {
		SHORT("dd/MM"), MEDIUM("dd/MM/yyyy"), LONG("dd/MM/yyyy HH:mm:ss");
		private String format;

		private DateFormat(String format) {
			this.setFormat(format);
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}
	}
	
	
	public static String getFormattedData(Date date, DateFormat dateFormat) {

		String returnValue = "Sem Data";
		SimpleDateFormat dataFormatada = new SimpleDateFormat(
				dateFormat.getFormat(), new Locale("pt", "BR"));

		if (date != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);

			returnValue = dataFormatada.format(c.getTime());
		}
		return returnValue;
	}
	public static String encodeSenha(String senha) {
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		if (m != null) {
			m.update(senha.getBytes(), 0, senha.length());
			senha = String.format("%032d", new BigInteger(1, m.digest()));
		}

		return senha;
	}

	public static String getMonthDescr(int month, boolean shortForm) {

		String monthDescr = "";

		switch (month) {
		case Calendar.JANUARY:
			if (shortForm)
				monthDescr = "Jan";
			else
				monthDescr = "Janeiro";
			break;
		case Calendar.FEBRUARY:
			if (shortForm)
				monthDescr = "Fev";
			else
				monthDescr = "Fevereiro";
			break;
		case Calendar.MARCH:
			if (shortForm)
				monthDescr = "Mar";
			else
				monthDescr = "Março";
			break;
		case Calendar.APRIL:
			if (shortForm)
				monthDescr = "Abr";
			else
				monthDescr = "Abril";
			break;
		case Calendar.MAY:
			if (shortForm)
				monthDescr = "Mai";
			else
				monthDescr = "Maio";
			break;
		case Calendar.JUNE:
			if (shortForm)
				monthDescr = "Jun";
			else
				monthDescr = "Junho";
			break;
		case Calendar.JULY:
			if (shortForm)
				monthDescr = "Jul";
			else
				monthDescr = "Julho";
			break;
		case Calendar.AUGUST:
			if (shortForm)
				monthDescr = "Ago";
			else
				monthDescr = "Agosto";
			break;
		case Calendar.SEPTEMBER:
			if (shortForm)
				monthDescr = "Set";
			else
				monthDescr = "Setembro";
			break;
		case Calendar.OCTOBER:
			if (shortForm)
				monthDescr = "Out";
			else
				monthDescr = "Outubro";
			break;
		case Calendar.NOVEMBER:
			if (shortForm)
				monthDescr = "Nov";
			else
				monthDescr = "Novembro";
			break;
		case Calendar.DECEMBER:
			if (shortForm)
				monthDescr = "Dez";
			else
				monthDescr = "Dezembro";
			break;
		default:
			break;
		}

		return monthDescr;

	}

	public static Calendar createReferenceDate(int year, int month) {
		Calendar referenceDate = Calendar.getInstance();

		referenceDate.set(year, (month - 1), 1, 0, 0, 0);

		return referenceDate;
	}

	public static Calendar createInitialDate(int year, int month) {
		Calendar initialDate = Calendar.getInstance();

		initialDate.set(year, (month - 1), 1, 0, 0, 0);
		if (initialDate.get(Calendar.DST_OFFSET) > 0)
			initialDate.add(Calendar.HOUR_OF_DAY, -2);
		else
			initialDate.add(Calendar.HOUR_OF_DAY, -3);
		initialDate.add(Calendar.SECOND, -1);

		return initialDate;
	}

	public static Calendar createFinalDate(int year, int month) {
		Calendar referenceDate = createReferenceDate(year, month);
		Calendar finalDate = Calendar.getInstance();

		finalDate.set(year, (month - 1),
				referenceDate.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59,
				59);
		if (finalDate.get(Calendar.DST_OFFSET) > 0)
			finalDate.add(Calendar.HOUR_OF_DAY, -2);
		else
			finalDate.add(Calendar.HOUR_OF_DAY, -3);

		return finalDate;
	}

	public static Calendar createPreviewMonthLastDay(int year, int month) {
		Calendar previewMonthLastDay = createReferenceDate(year, month);

		previewMonthLastDay.set(year, (month - 1), 1, 23, 59, 59);
		previewMonthLastDay.add(Calendar.DAY_OF_MONTH, -1);
		if (previewMonthLastDay.get(Calendar.DST_OFFSET) > 0)
			previewMonthLastDay.add(Calendar.HOUR_OF_DAY, -2);
		else
			previewMonthLastDay.add(Calendar.HOUR_OF_DAY, -3);

		return previewMonthLastDay;
	}
	public static Date parseDate(String text, DateFormat dateFormat) {
		Date date = null;
		SimpleDateFormat format = new SimpleDateFormat(
				DateFormat.MEDIUM.getFormat(), new Locale("pt", "BR"));

		try {
			date = format.parse(text);
		} catch (Exception e) {

		}

		return date;
	}
	
	public static double parseDouble(String text) {
		double returnValue = 0;
		Locale locale = new Locale("pt", "BR");
		NumberFormat formatter = NumberFormat.getInstance(locale);
		formatter.setMinimumFractionDigits(2);

		try {
			returnValue = formatter.parse(text).doubleValue();
		} catch (Exception e) {

		}

		return returnValue;
	}
	
	public static <T> String getJsonObjectList(List<T> list, Class<T> c, boolean dateWithTimeZone) {
		String returnValue = "";

		int loopCount = 0;
		for (T t : list) {
			if (loopCount == 0)
				returnValue += "{";
			else
				returnValue += ";{";

			int count = 0;
			for (Field field : c.getDeclaredFields()) {
				String fieldStrValue = "";
				String methodName = "";

				if (field.getType().equals(List.class)|| Comparator.class.isAssignableFrom(field.getType())) {
					continue;
				}

				try {

					if (boolean.class.isAssignableFrom(field.getType())
							|| Boolean.class.isAssignableFrom(field.getType()))
						methodName = "is"
								+ field.getName().substring(0, 1).toUpperCase()
								+ field.getName().substring(1);
					else
						methodName = "get"
								+ field.getName().substring(0, 1).toUpperCase()
								+ field.getName().substring(1);

					Method m = c.getDeclaredMethod(methodName, new Class[] {});
					Object fieldValue = m.invoke(t, null);

					if (fieldValue != null) {
						if (fieldValue instanceof String
								|| Character.class.isAssignableFrom(field
										.getType())
								|| char.class.isAssignableFrom(field.getType())) {
							fieldStrValue = "\'" + fieldValue.toString() + "\'";
						} else if (fieldValue instanceof Date) {
							Date dateValue = (Date) fieldValue;
							if(dateWithTimeZone){
								
							Calendar date = Calendar.getInstance();
							date.setTime((Date) fieldValue);
							if(date.get(Calendar.DST_OFFSET) > 0)
								date.add(Calendar.HOUR_OF_DAY, 2);
							else
								date.add(Calendar.HOUR_OF_DAY, 3);
							
							dateValue = date.getTime();
							}
							fieldStrValue = "\'" +SFFUtil.getFormattedData(dateValue, DateFormat.MEDIUM) + "\'";

						} else if (GenericEntity.class.isAssignableFrom(field
								.getType())) {

							Method m2 = fieldValue.getClass()
									.getDeclaredMethod("getId", new Class[] {});
							Object idField = m2.invoke(fieldValue, null);
							fieldStrValue = idField.toString();

						} else {
							fieldStrValue = fieldValue.toString();
						}

					} else
						fieldStrValue = "null";

					String fieldName = "";
					if (GenericEntity.class.isAssignableFrom(field.getType()))
						fieldName = field.getName() + "Id";
					else
						fieldName = field.getName();

					if (count == 0)
						returnValue += fieldName + " : " + fieldStrValue;
					else
						returnValue += ", " + fieldName + " : " + fieldStrValue;

					count++;

				} catch (IllegalAccessException e) {

					e.printStackTrace();
				} catch (IllegalArgumentException e) {

					e.printStackTrace();
				} catch (InvocationTargetException e) {

					e.printStackTrace();
				} catch (NoSuchMethodException e) {

					e.printStackTrace();
				} catch (SecurityException e) {

					e.printStackTrace();
				}

			}

			returnValue += "}";
			loopCount++;
		}

		return returnValue;
	}

}
