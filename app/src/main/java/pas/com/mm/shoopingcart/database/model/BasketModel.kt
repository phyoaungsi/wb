package pas.com.mm.shoopingcart.database.model

class BasketModel(productId: String, amount: Double, quantity: Int, memberId: String) {

    data class Customer(val name: String, val email: String)

    private var productId: String? = null
    private var amount: Double = 0.toDouble()
    private var quantity: Int = 0
    private var memberId: String? = null



    fun getProductId(): String? {
        return productId
    }

    fun setProductId(productId: String) {
        this.productId = productId
    }

    fun getAmount(): Double {
        return amount
    }

    fun setAmount(amount: Double) {
        this.amount = amount
    }

    fun getQuantity(): Int {
        return quantity
    }

    fun setQuantity(quantity: Int) {
        this.quantity = quantity
    }

    fun getMemberId(): String? {
        return memberId
    }

    fun setMemberId(memberId: String) {
        this.memberId = memberId
    }
}