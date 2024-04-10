package br.com.fatecrestapi.FoodFatec.service;

import br.com.fatecrestapi.FoodFatec.dto.ProductDTO;
import br.com.fatecrestapi.FoodFatec.entity.Product;
import br.com.fatecrestapi.FoodFatec.repository.ProductRepository;
import br.com.fatecrestapi.FoodFatec.util.ConverterData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ConverterData converterData;

    @Autowired
    private CategoryService categoryService;

    public List<Product> getInfoProducts() {
        return productRepository.findAll();
    }

    public Product saveProduct(ProductDTO productDTO) {

        Product product = new Product();
        product.setNameProduct(productDTO.getNameProduct());
        product.setDescriptionProduct(productDTO.getDescriptionProduct());
        product.setEanProduct(productDTO.getEanProduct());
        product.setSkuProduct(productDTO.getSkuProduct());

        product.setCostPriceProduct(converterData.convertingStringToBigDecimal(productDTO.getCostPriceProduct()));
        product.setAmountProduct(converterData.convertingStringToBigDecimal(productDTO.getAmountProduct()));

        //LocalDate dateCreatedProduct = LocalDate.parse(productDTO.getDateCreatedProduct(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        //product.setDateCreatedProduct(dateCreatedProduct);

        product.setCategory(categoryService.findCategoryById(productDTO.getIdCategory()));

        if (validateProduct(product)) {
            return productRepository.saveAndFlush(product);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Problemas ao cadastrar produto, os valores preço de venda e preço de custo " +
                            "devem ser maiores ou igual a 0 (zero)!");
        }
    }

    public HashMap<String, Object> deleteProduct(Long idProduct) {
        Optional<Product> product =
                Optional.ofNullable(productRepository.findById(idProduct).
                        orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Produto não encontrado!")));


        productRepository.delete(product.get());
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", "Produto: " + product.get().getNameProduct() +  " excluído com sucesso!");
        return result;
    }

    public Product findProductById(Long idProduct) {
        return productRepository.findById(idProduct)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Produto não encontrada!"));
    }

    public Product updateProduct(ProductDTO productDTO) {

        Product product = new Product();
        product.setIdProduct(productDTO.getIdProduct());
        product.setNameProduct(productDTO.getNameProduct());
        product.setDescriptionProduct(productDTO.getDescriptionProduct());
        product.setEanProduct(productDTO.getEanProduct());
        product.setSkuProduct(productDTO.getSkuProduct());

        product.setCostPriceProduct(converterData.convertingStringToBigDecimal(productDTO.getCostPriceProduct()));
        product.setAmountProduct(converterData.convertingStringToBigDecimal(productDTO.getAmountProduct()));

        product.setCategory(categoryService.findCategoryById(productDTO.getIdCategory()));

        product.setPublishedProduct(Boolean.parseBoolean(productDTO.getPublishedProduct()));
        LocalDate dateCreatedProduct = LocalDate.parse(productDTO.getDateCreatedProduct(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        product.setDateCreatedProduct(dateCreatedProduct);

        if (validateProduct(product)) {
            if (findProductById(product.getIdProduct()) != null) {
                return productRepository.saveAndFlush(product);
            } else {
                return null;
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Problemas ao cadastrar produto, os valores preço de venda e preço de custo " +
                            "devem ser maiores ou igual a 0 (zero)!");
        }
    }

    public Boolean validateProduct(Product product) {
        if (product.getCostPriceProduct().compareTo(BigDecimal.valueOf(0)) >= 0
        && product.getAmountProduct().compareTo(BigDecimal.valueOf(0)) >= 0) {
            return true;
        } else {
            return false;
        }
    }

}
