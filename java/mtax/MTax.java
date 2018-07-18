import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MTax implements Constant {
    
    public MTax(){
        
    }
    
    public static List<String> validate(List<X_Tax> xTaxList) {

        List<String> errorList = new ArrayList<>();

        if (xTaxList != null && xTaxList.size() > 0) {

            // boolean to know if taxes have at least one non-local rate
            boolean hasNonLocalRate = false;
            List<String> validIds = new ArrayList<>();

            // get the tax category only if there are taxes in xTaxList
            List<String> taxCategoryList = MInfoTaxCategory.getTaxCategoryStringList();

            for (X_Tax tax : xTaxList) {

                // the tax must exist and in the tax category in order to be valid
                if (isStringNullOrBlank(tax.getTax())) {
                    errorList.add("El impuesto es obligatorio");
                } else {
                    if (!taxCategoryList.contains(tax.getTax())) {
                        errorList.add("El impuesto no es un dato valido");
                        continue;
                    }
                }

                // the tax should has an Id to be valid
                if (tax.getId() != null) {

                    // the tax should has an amount
                    if (tax.getAmount() == null) {
                        errorList.add("El importe es obligatorio");
                        continue;
                    }

                    if (tax.isLocal()) {
                        if (tax.isTrasladado()) {
                            errorList.add("El impuesto local debe ser transferido");
                            continue;
                        }
                    } else {
                        // update non local rates
                        if (!hasNonLocalRate) {
                            hasNonLocalRate = true;
                        }
                    }

                    // add valid id's only if tax have all required fileds
                    validIds.add(tax.getId().toString());
                } else {
                    errorList.add("El impuesto id es obligatorio");
                }
            }

            if (validIds.size() > 0) {
                    List<X_Tax> xt = TaxsByListId(validIds, false);

                    if (xt.size() != validIds.size()) {
                        errorList.add("Existen datos no guardados previamente");
                    } else {
                        // map tax id to created date only
                        HashMap<String, Date> map_taxs = new HashMap<String, Date>();

                        for (X_Tax tax: xt) {
                            map_taxs.put(tax.getId().toString(), tax.getCreated());
                        }

                        for (int i = 0; i < xTaxList.size(); i++) {
                            if (xTaxList.get(i).getId() != null) {
                                xTaxList.get(i).setCreated(map_taxs.get(xTaxList.get(i).getId().toString()));
                            }
                        }
                    }
            }

            if (!hasNonLocalRate) {
                errorList.add("Debe de incluir al menos una tasa no local");
            }

        } else {
            errorList.add("El documento no tiene tasas");
        }
        
        return errorList;
    }

    // to check if string has data or not
    public static boolean isStringNullOrBlank(String param) {
        return param == null || param.trim().length() == 0;
    }
}
