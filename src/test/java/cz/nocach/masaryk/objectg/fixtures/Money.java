package cz.nocach.masaryk.objectg.fixtures;

import java.math.BigDecimal;

/**
 * User: __nocach
 * Date: 29.9.12
 */
public class Money {
    private BigDecimal amount;
    private String currency;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
