package zxf.java.functional.function.check.product.model;

public class ProductUpdateInput {
    private final Product preProduct;
    private final Product postProduct;

    public ProductUpdateInput(Product preProduct, Product postProduct) {
        this.preProduct = preProduct;
        this.postProduct = postProduct;
    }

    public boolean preHasName() {
        return preProduct.getName() != null;
    }

    public boolean postHasName() {
        return postProduct.getName() != null;
    }

    public boolean preHasEmail() {
        return preProduct.getEmail() != null;
    }

    public boolean postHasEmail() {
        return postProduct.getEmail() != null;
    }
}