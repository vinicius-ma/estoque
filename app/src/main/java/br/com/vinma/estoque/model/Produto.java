package br.com.vinma.estoque.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Produto {

    @PrimaryKey(autoGenerate = true)
    private final long id;
    private final String nome;
    private final BigDecimal preco;
    private final int quantidade;

    public Produto(long id, String nome, BigDecimal preco, int quantidade) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getPreco() {
        if(preco != null) {
            return preco.setScale(2, RoundingMode.HALF_EVEN);
        }
        return BigDecimal.ZERO;
    }

    public int getQuantidade() {
        return quantidade;
    }

}
