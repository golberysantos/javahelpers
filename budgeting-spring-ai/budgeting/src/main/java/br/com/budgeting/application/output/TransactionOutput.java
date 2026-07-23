package br.com.budgeting.application.output;

import java.math.BigDecimal;
import java.math.RoundingMode;
import br.com.budgeting.application.domain.Transaction;

public record TransactionOutput(String id, String description, String category, double value) {
	public static TransactionOutput from(Transaction transaction) {
		return new TransactionOutput(transaction.getId().uuid().toString(), transaction.getDescription(),
				transaction.getCategory().name(),
				BigDecimal.valueOf(transaction.getAmount()).setScale(2, RoundingMode.HALF_UP).doubleValue());
	}
}
