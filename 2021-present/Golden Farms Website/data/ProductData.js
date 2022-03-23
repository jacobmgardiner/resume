class ProductData {
    id;
    title;
    description;
    price;
    categoryNum;
    image;

    constructor(id, title, description, price, categoryNum, image) {
        this.id = id
        this.title = title
        this.description = description
        this.price = price
        this.categoryNum = categoryNum
        this.image = image
    }
}

export default ProductData;