package br.com.budgeting.infrastructure.http.request;

import br.com.budgeting.application.domain.Category;
import br.com.budgeting.application.input.PersistTransactionInput;

public record TransactionRequest(String description, Category category, long amount) {
    public PersistTransactionInput toInput() {
        return new PersistTransactionInput(description, amount, category);
    }
}
