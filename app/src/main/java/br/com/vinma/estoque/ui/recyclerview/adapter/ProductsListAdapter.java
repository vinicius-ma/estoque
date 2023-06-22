package br.com.vinma.estoque.ui.recyclerview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.vinma.estoque.R;
import br.com.vinma.estoque.model.Produto;

public class ProductsListAdapter extends
        RecyclerView.Adapter<ProductsListAdapter.ViewHolder> {

    private final OnItemClickListener onItemClickListener;
    private OnItemClickRemoveContextMenuListener
            onItemClickRemoveContextMenuListener = (position, productRemoved) -> {
    };
    private final Context context;
    private final List<Produto> products = new ArrayList<>();

    public ProductsListAdapter(Context context,
                               OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        this.context = context;
    }

    public void setOnItemClickRemoveContextMenuListener(OnItemClickRemoveContextMenuListener onItemClickRemoveContextMenuListener) {
        this.onItemClickRemoveContextMenuListener = onItemClickRemoveContextMenuListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Produto product = products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void update(List<Produto> products) {
        this.products.clear();
        this.products.addAll(products);
        this.notifyItemRangeInserted(0, this.products.size());
    }

    public void add(Produto... products) {
        int sizeCurrent = this.products.size();
        Collections.addAll(this.products, products);
        int sizeNew = this.products.size();
        notifyItemRangeInserted(sizeCurrent, sizeNew);
    }

    public void edit(int position, Produto product) {
        products.set(position, product);
        notifyItemChanged(position);
    }

    public void remove(int position) {
        products.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView idTv;
        private final TextView NameTv;
        private final TextView priceTv;
        private final TextView stockTv;
        private Produto product;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            idTv = itemView.findViewById(R.id.produto_item_id);
            NameTv = itemView.findViewById(R.id.product_item_name);
            priceTv = itemView.findViewById(R.id.product_item_price);
            stockTv = itemView.findViewById(R.id.product_item_stock);
            configureItemClick(itemView);
            configureContextMenu(itemView);
        }

        private void configureContextMenu(@NonNull View itemView) {
            itemView.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
                new MenuInflater(context).inflate(R.menu.lista_produtos_menu, menu);
                menu.findItem(R.id.menu_list_product_remove)
                        .setOnMenuItemClickListener(
                                item -> {
                                    int productPosition = getAdapterPosition();
                                    onItemClickRemoveContextMenuListener
                                            .onItemClick(productPosition, product);
                                    return true;
                                });
            });
        }

        private void configureItemClick(@NonNull View itemView) {
            itemView.setOnClickListener(v -> onItemClickListener
                    .onItemClick(getAdapterPosition(), product));
        }

        void bind(Produto product) {
            this.product = product;
            idTv.setText(String.valueOf(product.getId()));
            NameTv.setText(product.getNome());
            priceTv.setText(formatToCurrency(product.getPreco()));
            stockTv.setText(String.valueOf(product.getQuantidade()));
        }

        private String formatToCurrency(BigDecimal value) {
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
            return numberFormat.format(value);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position, Produto product);
    }

    public interface OnItemClickRemoveContextMenuListener {
        void onItemClick(int position, Produto productRemoved);
    }

}
