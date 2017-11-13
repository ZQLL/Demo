/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package nc.bs.fba_scost.cost.interest.pub;

import java.util.ArrayList;
import java.util.List;
import nc.bs.uif2.validation.IValidationService;
import nc.bs.uif2.validation.ValidationException;

public class UtilsZqjdCompositeValidation implements IValidationService {
	private List<IValidationService> validators;

	public UtilsZqjdCompositeValidation() {
		this.validators = new ArrayList();
	}

	public void setValidators(List<IValidationService> newValidationService) {
		this.validators.clear();
		this.validators.addAll(newValidationService);
	}

	public List<IValidationService> getValidate() {
		return this.validators;
	}

	public void validate(Object obj) throws ValidationException {
		for (IValidationService service : this.validators)
			service.validate(obj);
	}
}