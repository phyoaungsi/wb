package pas.com.mm.shoopingcart.database.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by phyo on 16/04/2018.
 */


@IgnoreExtraProperties
public class OrderForm extends Model {

    private String productId;
    private double amount;
    private int quantity;
    private String memberId;
    private Item item;


    private String key;
    public OrderForm(String productId, double amount, int quantity, String memberId) {
        this.productId = productId;
        this.amount = amount;
        this.quantity = quantity;
        this.memberId = memberId;
    }

    public OrderForm() {

    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }


    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
