package jsfbench.component;

import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.NamingContainer;
import jakarta.faces.component.UIInput;
import jakarta.faces.component.UINamingContainer;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.ConverterException;

import java.io.IOException;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;

@FacesComponent("inputYearMonth")
public class InputYearMonth extends UIInput implements NamingContainer {

    // Fields -------------------------------------------------------------------------------------
    private UIInput month;
    private UIInput year;

    // Actions ------------------------------------------------------------------------------------

    /**
     * Returns the component family of {@link UINamingContainer}.
     * (that's just required by composite component)
     */
    @Override
    public String getFamily() {
        return UINamingContainer.COMPONENT_FAMILY;
    }

    /**
     * Set the selected and available values of the day, month and year fields based on the model.
     */
    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        YearMonth yearMonth = (YearMonth) getValue();
        if (yearMonth == null) {
            yearMonth = YearMonth.now();
        }
        month.setValue(yearMonth.getMonthValue());
        year.setValue(yearMonth.getYear());
        super.encodeBegin(context);
    }

    /**
     * Returns the submitted value in uuuu-MM format.
     */
    @Override
    public Object getSubmittedValue() {
        String monthValue = String.valueOf(month.getSubmittedValue() == null && month.isLocalValueSet() ? month.getValue() : month.getSubmittedValue());
        if (monthValue.length() == 1) {
            monthValue = "0" + monthValue;
        }
        return (year.getSubmittedValue() == null && year.isLocalValueSet() ? year.getValue() : year.getSubmittedValue()) + "-" + monthValue;
    }

    /**
     * Converts the submitted value to concrete {@link YearMonth} instance.
     */
    @Override
    protected Object getConvertedValue(FacesContext context, Object submittedValue) {
        try {
            return YearMonth.parse((String) submittedValue);
        }
        catch (DateTimeParseException e) {
            throw new ConverterException(e); // This is not to be expected in normal circumstances.
        }
    }

    // Getters/setters ----------------------------------------------------------------------------
    public UIInput getMonth() {
        return month;
    }

    public void setMonth(UIInput month) {
        this.month = month;
    }

    public UIInput getYear() {
        return year;
    }

    public void setYear(UIInput year) {
        this.year = year;
    }

}
