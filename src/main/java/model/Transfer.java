package model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class Transfer {
    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    private Long transferId;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private Currency currency;

    public Long getTransferId() {
        return transferId;
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Transfer() {

    }

    public Transfer(long fromAccountId, long toAccountId, BigDecimal amount, Currency currency) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transfer that = (Transfer) o;
        return Objects.equals(getTransferId(), that.getTransferId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTransferId());
    }
}
