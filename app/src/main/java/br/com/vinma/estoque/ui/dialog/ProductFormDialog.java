package br.com.vinma.estoque.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;

import androidx.appcompat.app.AlertDialog;
import br.com.vinma.estoque.R;
import br.com.vinma.estoque.model.Produto;

abstract public class ProductFormDialog {

    private final String title;
    private final String title_button_positive;
    private final ConfirmationListener listener;
    private final Context context;
    private Produto product;

    ProductFormDialog(Context context,
                      String title,
                      String title_button_positive,
                      ConfirmationListener listener) {
        this.title = title;
        this.title_button_positive = title_button_positive;
        this.listener = listener;
        this.context = context;
    }

    ProductFormDialog(Context context,
                      String title,
                      String title_button_positive,
                      ConfirmationListener listener,
                      Produto product) {
        this(context, title, title_button_positive, listener);
        this.product = product;
    }

    public void show() {
        @SuppressLint("InflateParams") View inflatedView = LayoutInflater.from(context)
                .inflate(R.layout.dialog_product_form, null);
        tryFulfillProductForm(inflatedView);
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(inflatedView)
                .setPositiveButton(title_button_positive, (dialog, which) -> {
                    EditText nameEt = getEditText(inflatedView, R.id.dialog_product_form_name);
                    EditText priceEt = getEditText(inflatedView, R.id.dialog_product_form_price);
                    EditText stockEt = getEditText(inflatedView, R.id.dialog_product_form_stock);
                    createProduct(nameEt, priceEt, stockEt);
                })
                .setNegativeButton(context.getString(R.string.cancel), null)
                .show();
    }

    @SuppressLint("SetTextI18n")
    private void tryFulfillProductForm(View inflatedView) {
        if (product != null) {
            TextView idTv = inflatedView.findViewById(R.id.dialog_product_form_id);
            idTv.setText(String.valueOf(product.getId()));
            idTv.setVisibility(View.VISIBLE);
            EditText nameEt = getEditText(inflatedView, R.id.dialog_product_form_name);
            nameEt.setText(product.getNome());
            EditText PriceEt = getEditText(inflatedView, R.id.dialog_product_form_price);
            PriceEt.setText(product.getPreco().toString());
            EditText StockEt = getEditText(inflatedView, R.id.dialog_product_form_stock);
            StockEt.setText(String.valueOf(product.getQuantidade()));
        }
    }

    private void createProduct(EditText nameEt, EditText priceEt, EditText stockEt) {
        String name = nameEt.getText().toString();
        BigDecimal price = tryConvertPrice(priceEt);
        int stock = tryConvertStock(stockEt);
        long id = fulfillId();
        Produto product = new Produto(id, name, price, stock);
        listener.onConfirmed(product);
    }

    private long fulfillId() {
        if (product != null) {
            return product.getId();
        }
        return 0;
    }

    private BigDecimal tryConvertPrice(EditText priceEt) {
        try {
            return new BigDecimal(priceEt.getText().toString());
        } catch (NumberFormatException ignored) {
            return BigDecimal.ZERO;
        }
    }

    private int tryConvertStock(EditText stockEt) {
        try {
            return Integer.parseInt(
                    stockEt.getText().toString());
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    private EditText getEditText(View inflatedView, int idTextInputLayout) {
        TextInputLayout textInputLayout = inflatedView.findViewById(idTextInputLayout);
        return textInputLayout.getEditText();
    }

    public interface ConfirmationListener {
        void onConfirmed(Produto product);
    }


}
