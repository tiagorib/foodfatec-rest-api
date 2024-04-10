package br.com.fatecrestapi.FoodFatec.controller;

import br.com.fatecrestapi.FoodFatec.dto.ProductDTO;
import br.com.fatecrestapi.FoodFatec.entity.Category;
import br.com.fatecrestapi.FoodFatec.entity.Product;
import br.com.fatecrestapi.FoodFatec.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Object> getInfoCategories() {
        List<Product> result = productService.getInfoProducts();
        return ResponseEntity.ok().body(result);
    }
    @PostMapping(value = "/create")
    public ResponseEntity<Object> saveProduct(@RequestBody ProductDTO productDTO) {
        Product result = productService.saveProduct(productDTO);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping(value = "/delete/{idProduct}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long idProduct) {
        HashMap<String, Object> result = productService.deleteProduct(idProduct);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/findProduct/{idProduct}")
    public ResponseEntity<Object> getProductById(@PathVariable Long idProduct){
        Product result = productService.findProductById(idProduct);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<Object> updateProduct(@RequestBody ProductDTO productDTO) {
        Product result = productService.updateProduct(productDTO);
        return ResponseEntity.ok().body(result);
    }

}
