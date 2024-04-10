package br.com.fatecrestapi.FoodFatec.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long idProduct;
    private String nameProduct;
    private String descriptionProduct;
    private String skuProduct;
    private String eanProduct;
    private String costPriceProduct;
    private String publishedProduct;
    private String dateCreatedProduct;
    private String amountProduct;
    private Long idCategory;

}