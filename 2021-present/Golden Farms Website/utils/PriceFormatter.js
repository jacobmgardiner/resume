class PriceFormatter {
    static formatter = new Intl.NumberFormat('en-US',
        {
            style: 'currency',
            currency: 'USD',
        }
    );
    static asPrice(num) {
        return PriceFormatter.formatter.format(num)
    }
}

export default PriceFormatter;