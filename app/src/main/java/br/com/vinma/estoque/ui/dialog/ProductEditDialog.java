package br.com.vinma.estoque.ui.dialog;

import android.content.Context;

import br.com.vinma.estoque.R;
import br.com.vinma.estoque.model.Product;

public class ProductEditDialog extends ProductFormDialog {
    public ProductEditDialog(Context context, Product product, ConfirmationListener listener) {
        super(context, context.getString(R.string.product_edit_dialog_title),
                context.getString(R.string.product_edit_dialog_positive_button_title),
                listener, product);
    }
}