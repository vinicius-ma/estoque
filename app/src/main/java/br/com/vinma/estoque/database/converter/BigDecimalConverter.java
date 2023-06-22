package br.com.vinma.estoque.database.converter;

import java.math.BigDecimal;

import androidx.room.TypeConverter;

public class BigDecimalConverter {

    @TypeConverter
    public Double toDouble(BigDecimal value) {
        return value.doubleValue();
    }

    @TypeConverter
    public BigDecimal toBigDecimal(Double value) {
        if (value != null) {
            return new BigDecimal(value);
        }
        return BigDecimal.ZERO;
    }

}
