package br.com.fatecrestapi.FoodFatec.controller;

import br.com.fatecrestapi.FoodFatec.dto.ProductDTO;
import br.com.fatecrestapi.FoodFatec.entity.Product;
import br.com.fatecrestapi.FoodFatec.exception.ResponseGenericException;
import br.com.fatecrestapi.FoodFatec.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/product")
@CrossOrigin(value = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(value = "/list")
    @Operation(summary = "Method for list all products")
    public ResponseEntity<Object> getInfoCategories() {
        List<Product> result = productService.getInfoProducts();
        return ResponseEntity.status(HttpStatus.OK).body(ResponseGenericException.response(result));
    }

    @PostMapping(value = "/create")
    @Operation(summary = "Method for registering product")
    public ResponseEntity<Object> saveProduct(@RequestBody ProductDTO productDTO) {
        Product result = productService.saveProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseGenericException.response(result));
    }

    @DeleteMapping(value = "/delete/{idProduct}")
    @Operation(summary = "Method for delete product by ID")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long idProduct) {
        HashMap<String, Object> result = productService.deleteProduct(idProduct);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseGenericException.response(result));
    }

    @GetMapping(value = "/findProduct/{idProduct}")
    @Operation(summary = "Method for find product by ID")
    public ResponseEntity<Object> getProductById(@PathVariable Long idProduct){
        Product result = productService.findProductById(idProduct);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseGenericException.response(result));
    }

    @PutMapping(value = "/update")
    @Operation(summary = "Method for update product")
    public ResponseEntity<Object> updateProduct(@RequestBody ProductDTO productDTO) {
        Product result = productService.updateProduct(productDTO);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseGenericException.response(result));
    }

}