package com.promoweb.mercadona.service;

import com.promoweb.mercadona.exception.NoProductsFoundException;
import com.promoweb.mercadona.model.Category;
import com.promoweb.mercadona.model.Product;
import com.promoweb.mercadona.model.Promotion;
import com.promoweb.mercadona.repository.ProductRepository;
import com.promoweb.mercadona.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product createProduct(Product product) {
            //isValidProduct(product);
            return productRepository.save(product);
    }

    public void updateProduct(Long id, Product product) {
        Product existingProduct = getProductById(id);
        if (existingProduct != null && isValidProduct(product)) {
            // Eenregistrer dans la base de données
            existingProduct.setLabel(product.getLabel());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setPrix(product.getPrix());
            existingProduct.setImagePath(product.getImagePath());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setPromotion(product.getPromotion());
             productRepository.save(existingProduct);
        }
    }

    public void deleteProduct(Long id) {

        //Vérifier l'existence du produit
        Product product = productRepository
                .findById(id).orElseThrow(() ->
                        new EntityNotFoundException("Produit non trouvé avec l'ID : " + id));
        //Détacher le produit de l'utilisateur si on veut supprimer que le produit.
        product.setUser(null);

        //Spprimer le produit sans affecter l'utilisater associé.

        productRepository.delete(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    private boolean isValidProduct(Product product) {
        return product != null && product.getPrix() > 0 && categoryExists(product.getCategory());
    }

    private boolean categoryExists(Category category) {
        return category != null && categoryRepository.existsById(category.getId());
    }


    public List<Product> getPromotionalProducts() {
        List<Product> promotionalProducts = productRepository.findByPromotionIsNotNull();
        if (promotionalProducts.isEmpty()) {
            logger.info("Aucun produit trouvé en promotion.");
            throw new NoProductsFoundException("Il n'y a aucun produit en promotion pour le moment.");
        }
        return promotionalProducts;
    }

    public List<Product> getProductsByUser(Long user_id) {
        List<Product> productsByUser = productRepository.findByUserId(user_id);
        if (productsByUser.isEmpty()) {
            throw new NoProductsFoundException("Aucun produit trouvé pour l'administrateur avec l'ID : " + user_id);
        }
        return productsByUser;
    }

    public List<Product> getProductsByCategory(Long category_id) {
        List<Product> productsByCategory = productRepository.findByCategoryId(category_id);
        if (productsByCategory.isEmpty()) {
            throw new NoProductsFoundException("Aucun produit trouvé dans cette catégory: " + category_id);
        }
        return productsByCategory;
    }

    public Page<Product> findProductsWithPagination(String kw, Pageable pageable) {
        if (kw == null || kw.trim().isEmpty()) {
            // Si le mot-clé est vide, récupérez tous les utilisateurs avec pagination
            return productRepository.findAll(pageable);
        } else {
            // Si un mot-clé est fourni, recherchez les produits par mot-clé avec pagination
            return productRepository.findByCategoryContains(kw, pageable);
        }
    }

    public Page<Product> findProduct(Long category, Pageable pageable) throws NoProductsFoundException {
        Page<Product> products;
        if (category == 0) {
            // Si la catégorie est vide, récupérez tous les produits avec pagination
            return productRepository.findAll(pageable);
        } else {
            // Si une catégorie est fourni, recherchez les produits par catégorie avec pagination
            products =  productRepository.findAllByCategoryId(category, pageable);
            System.out.println(products);
        }

        if (products.isEmpty()) {

            throw new NoProductsFoundException("Aucun produit trouvé dans cette catégorie.");
        }
        return products;
    }

    public String uploadImage(MultipartFile imageFile) throws IOException {
        // Vérifiez si un fichier a été fourni
        if (imageFile != null && !imageFile.isEmpty()) {
            // Obtenez le chemin du répertoire où vous souhaitez stocker les images
            String uploadDir ="src/main/resources/static/assets/";

            // Assurez-vous que le répertoire existe, sinon créez-le
            File dir = new File(uploadDir);
            if (!dir.exists()) {

                throw new IOException("Le répértoire assets n'existe pas");
            }

            // Générez un nom de fichier unique pour éviter les conflits
            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();

            // Construisez le chemin complet du fichier
            String filePath = uploadDir + File.separator + fileName;

            // Copiez le contenu du fichier vers le chemin spécifié
            FileCopyUtils.copy(imageFile.getBytes(), new File(filePath));

            // Retournez le chemin du fichier enregistré
            return fileName;
        }

        // Si aucun fichier n'est fourni, retournez une chaîne vide ou null
        return "";
    }

}
