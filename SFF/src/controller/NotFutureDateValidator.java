package controller;

import java.util.Calendar;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("notFutureDateValidator")
public class NotFutureDateValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		
		if (value == null) {
            return;
        }
		
		UIInput startDateComponent = (UIInput) component;



        Date startDate = (Date) value;
        Calendar today =  Calendar.getInstance();
        today.add(Calendar.DAY_OF_MONTH, -1);
        if(today.get(Calendar.DST_OFFSET) > 0)
        	today.set(Calendar.HOUR_OF_DAY, 23);
        else
        	today.set(Calendar.HOUR_OF_DAY, 22);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        
        
        Date endDate = today.getTime();
        System.out.println("Data Limite - " + endDate);
        System.out.println("Data Lançamento - " + startDate);
        
        
        if (startDate.after(endDate)) {
        	startDateComponent.setValid(false);
            throw new ValidatorException(new FacesMessage(
                FacesMessage.SEVERITY_ERROR, startDateComponent.getValidatorMessage(), null));
        }

	}

}
