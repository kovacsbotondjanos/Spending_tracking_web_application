package com.example.monthlySpendingsBackend.models.expenseTables.moneyflow;

import com.example.monthlySpendingsBackend.models.expenseTables.BaseExpenseTableFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "INCOME")
public class Income extends BaseExpenseTableFormat {
}
