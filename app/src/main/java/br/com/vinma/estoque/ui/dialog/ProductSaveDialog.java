package br.com.vinma.estoque.ui.dialog;

import android.content.Context;

import br.com.vinma.estoque.R;

public class ProductSaveDialog extends ProductFormDialog {
    public ProductSaveDialog(Context context,
                             ConfirmationListener listener) {
        super(context, context.getString(R.string.product_save_dialog_title),
                context.getString(R.string.product_save_dialog_positive_button_title),
                listener);
    }

}
