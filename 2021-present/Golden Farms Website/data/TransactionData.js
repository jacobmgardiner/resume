class TransactionData {
    constructor(id, time, products, quantities, first_name, last_name, email, address1, address2, city, state, postal, country) {
        this.id = id
        this.time = time
        this.productIds = products
        this.quantities = quantities
        this.first_name = first_name
        this.last_name = last_name
        this.email = email
        this.address1 = address1
        this.address2 = address2
        this.city = city
        this.state = state
        this.postal = postal
        this.country = country

        this.toMap = this.toMap.bind(this)
    }

    toMap() {
        return {
            id: this.id,
            time: this.time,
            products: this.productIds,
            quantities: this.quantities,
            firstName: this.first_name,
            lastName: this.last_name,
            email: this.email,
            address1: this.address1,
            address2: (!this.address2) ? "" : this.address2,
            city: this.city,
            state: this.state,
            postal: this.postal,
            country: this.country,
            fulfilled: false,
        }
    }
}

export default TransactionData