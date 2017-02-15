/**
 * Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 * <p>
 * package com.example.android.justjava;
 */
package com.example.kiko.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

import static android.R.attr.name;
import static com.example.kiko.justjava.R.string.name_quantity;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view) {
        if (quantity == 100) {
            // Show an error message as toast
            Toast.makeText(this, "You cannot have more than 100 coffees", Toast.LENGTH_SHORT).show();
            // Exit this method early because there's nothing left to do
            return;
        }
        quantity = quantity + 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {
        if (quantity == 1) {
            return;
        }
        quantity = quantity - 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {


        //figure out if the user wants whipped cream
        CheckBox WhippedCreamCheckBox = (CheckBox) findViewById(R.id.Whipped_cream_box);
        boolean hasWhippedCream = WhippedCreamCheckBox.isChecked();
        //figure out if the user wants chocolate
        CheckBox ChocolateCheckBox = (CheckBox) findViewById(R.id.chocolate);
        boolean hasChocolate = ChocolateCheckBox.isChecked();
        //set the order's name
        EditText editTextName = (EditText) findViewById(R.id.name_field);
        String nameField = editTextName.getText().toString();
        // set a string priceMessage
        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String priceMessage = createOrderSummary(price, hasWhippedCream, hasChocolate, nameField);
        //set an intent to send a mail with the order summary
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, "fragargon@gmail.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Just java order for " + nameField);
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Calculates the price of the order.
     *
     * @param addWhippedCream is whether or not the user wants whipped cream topping
     * @param addChocolate    is whether or not hte user wants chocolat topping
     * @return total price
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        // Price of 1 cup coffee
        int basePrice = 5;

        // Price 1€ if the user wants whipped cream
        if (addWhippedCream) {
            basePrice += +1;
        }

        // Price 2€ if the user wants chocolate
        if (addChocolate) {
            basePrice += +2;
        }

        // Calculate the total order price by multiplying by quantity
        return quantity * basePrice;
    }

    /**
     * @param Name            of the order
     * @param price           of the order
     * @param addWhippedCream is whether or not the user wants cream
     * @param addChocolate    is whether or not the user wants chocolate
     * @return priceMessage
     */

    private String createOrderSummary(int price, boolean addWhippedCream, boolean addChocolate, String Name) {
        String priceMessage = getString(R.string.name) + Name;
        priceMessage += "\n" + getString(R.string.cream) + addWhippedCream;
        priceMessage += "\n" + getString(R.string.choco) + addChocolate;
        priceMessage += "\n" + getString(R.string.quantity) + quantity;
        priceMessage += "\n" + getString(R.string.total) + calculatePrice(addWhippedCream, addChocolate);
        priceMessage += "\n" + getString(R.string.thanks);
        return priceMessage;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }
}
