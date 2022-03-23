class CartData {
    static products = new Map();
    static quantities = new Map();
    static addProduct(product, quantity = 1) {
        if (!CartData.products.has(product.id))
            CartData.products.set(product.id, product)
        CartData.quantities.set(product.id, this.getProductQuantity(product.id) + quantity)
    }
    static removeProduct(id) {
        CartData.products.delete(id)
        CartData.quantities.delete(id)
    }

    static clear() {
        CartData.products.clear()
        CartData.quantities.clear()
    }

    static getProductQuantity(id) {
        if (!CartData.quantities.get(id)) CartData.quantities.set(id, 0)
        return CartData.quantities.get(id)
    }

    static calculateTotalPrice() {
        let total = 0
        for (let product of CartData.products.values()) {
            total += CartData.products.get(product.id).price * CartData.quantities.get(product.id)
        }
        return total
    }

    static getNumberOfProducts() {
        return CartData.products.size
    }

    static isEmpty() {
        return CartData.getNumberOfProducts() === 0
    }

    static setQuantity(product, value) {
        CartData.quantities.set(product.id, value)
    }
    static getQuantity(id) {
        return CartData.quantities.get(id)
    }
}

export default CartData;