package br.com.budgeting.application.input;

import org.springframework.ai.tool.annotation.ToolParam;
import br.com.budgeting.application.domain.Category;

public record PersistTransactionInput(@ToolParam(description = "Descrição do gasto") String description,
		@ToolParam(description = "Valor do gasto (em centavos)") long amount,
		@ToolParam(description = "Categoria de uma transação") Category category) {
}
