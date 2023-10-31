package com.promoweb.mercadona.controller;


import com.promoweb.mercadona.exception.NoProductsFoundException;
import com.promoweb.mercadona.model.Category;
import com.promoweb.mercadona.model.Product;
import com.promoweb.mercadona.model.Promotion;
import com.promoweb.mercadona.model.User;
import com.promoweb.mercadona.service.CategoryService;
import com.promoweb.mercadona.service.ProductService;
import com.promoweb.mercadona.service.PromotionService;
import com.promoweb.mercadona.service.UserService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;


@Controller
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;
    private final CategoryService categoryService;

    private final PromotionService promotionService;

    private final UserService userService;


    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService, PromotionService promotionService, UserService userService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.promotionService = promotionService;
        this.userService = userService;
    }

    @GetMapping("/products")
    public String products() {
        return "products/listProducts";
    }

    @GetMapping("/listProducts")
    public String listAllProducts(Model model,
                                  @RequestParam(name = "page", defaultValue = "0") int page,
                                  @RequestParam(name = "size", defaultValue = "5") int size,
                                  @RequestParam(name = "category", defaultValue ="0") Long category) {
        try {
            Page<Product> pageProducts = productService.findProduct(category, PageRequest.of(page, size));
            model.addAttribute("products", pageProducts.getContent());
            model.addAttribute("pages", new int[pageProducts.getTotalPages()]);
            model.addAttribute("currentPage", page);
            model.addAttribute("category", category);

        } catch (NoProductsFoundException e) {
            String errorMessage = e.getMessage();
            model.addAttribute("errorMessage", errorMessage);
        } catch (Exception e) {

            logger.error("Une erreur s'est produite lors de la récupération des produits.", e);
            String errorMessage = "Une erreur s'est produite lors de la récupération des produits.";
            model.addAttribute("errorMessage", errorMessage);

        }
        return "products/produtcsFragment";
    }

    @GetMapping("/")
    public String catalogue() {
        return "redirect:/listProducts";
    }

    @GetMapping("/allProducts")
    public String getAllProducts(Model model) {
        try {
            List<Product> products = productService.getAllProducts();
            model.addAttribute("products", products);
            return "/products/catalogue";
        } catch (Exception e) {
            String errorMessage = "Erreur lors de la récupération des produits: " + e.getMessage();
            throw new RuntimeException(errorMessage, e);
        }
    }

    //Read
    @GetMapping("/{id}")
    public String getProductById(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product != null) {
            model.addAttribute("product", product);
            return "/products/catalogue"; // Thymeleaf template name
        } else {
            throw new EntityNotFoundException("Le produit avec l'id : " + id + " n'existe pas");
        }
    }

    //Create the new product
    @GetMapping("/formProduct")
    public String showCreateForm(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username =  authentication.getName();
        Long userId = userService.getIdUserByUsername(username);

        model.addAttribute("userId", userId);

        // Récupérer toutes les catégories de la base de données
        List<Category> categories = categoryService.getAllCategories();

        // Ajouter les catégories au modèle pour les rendre disponibles dans la vue Thymeleaf
        model.addAttribute("categories", categories);

        // Ajouter un nouvel objet Product au modèle pour le formulaire
        model.addAttribute("product", new Product());

        // Retourner le nom de la vue Thymeleaf
        return "products/formProduct";

    }

    @PostMapping("/saveProduct")
    public String createProduct(@ModelAttribute @Valid Product product,
                                BindingResult bindingResult,
                                @RequestParam(name = "userId", required = true) Long user_id,
                                @RequestParam(name = "existingCategoryId", required = false) Long category_id,
                                @RequestParam(name = "newCategoryLabel", required = false) String newCategoryLabel,
                                @RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
                                Model model) {
        model.addAttribute("product", new Product());

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "redirect:../saveProduct";
        }

        User user = userService.getUserById(user_id);
        product.setUser(user);
        try {
            // Vérifiez si une catégorie existante est sélectionnée
            createCategoryByNewProduct(product, category_id, newCategoryLabel);
            // Gestion du fichier image
            String imagePath = productService.uploadImage(imageFile);
            if (!imagePath.isEmpty()) {
                product.setImagePath(imagePath);
            }

            productService.createProduct(product);

            // Utilisez la redirection pour éviter les problèmes de re-soumission du formulaire
            return "redirect:listProducts";
        } catch (Exception e) {
            // Gestion des erreurs spécifiques à la création du produit
            logger.error("Une erreur s'est produite lors de la création du produit." + e.getMessage());
            System.out.println(e.getMessage());
            model.addAttribute("error", "Une erreur s'est produite lors de la création du produit.");
            return "errors/error";
        }
    }

    //Update
    @GetMapping("/editProduct/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {

        try {
           Product product = productService.getProductById(id);
            if (product != null) {
                model.addAttribute("product", product);

                // Récupérer toutes les catégories de la base de données
                List<Category> categories = categoryService.getAllCategories();

                // Ajouter les catégories au modèle pour les rendre disponibles dans la vue Thymeleaf
                model.addAttribute("categories", categories);

                return "products/editProduct"; // Thymeleaf template name pour afficher le formulaire de mise à jour
            } else {
                throw new EntityNotFoundException("Le produit avec l'id : " + id + " n'existe pas");
            }
        } catch (EntityNotFoundException e) {
            // Gestion spécifique de l'exception EntityNotFoundException
            logger.warn("Produit non trouvé: {}", e.getMessage());
            model.addAttribute("error", "Produit non trouvé.");
            return "/errors/error";
        } catch (Exception e) {
            // Gestion générale des erreurs
            logger.error("Une erreur s'est produite lors de l'affichage du formulaire de mise à jour.", e);
            model.addAttribute("error", "Une erreur s'est produite lors de l'affichage du formulaire de mise à jour.");
            return "/errors/error";
        }
    }

    @PostMapping("/updateProduct/{id}")
    public String updateProduct(@PathVariable Long id,
                                @ModelAttribute @Valid Product product,
                                BindingResult bindingResult,
                                @RequestParam(name = "existingCategoryId", required = false) Long category_id,
                                @RequestParam(name = "newCategoryLabel", required = false) String newCategoryLabel,
                                @RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
                                Model model) throws EntityNotFoundException {
        System.out.println("--------------------------------------------\n");
        System.out.println(bindingResult.hasErrors());
        System.out.println("--------------------------------------------\n");
        if (bindingResult.hasErrors()) {
            System.out.println("-----------------toto---------------------------\n");
            //model.addAttribute("errors", bindingResult.getAllErrors());
            //model.addAttribute("errors", bindingResult.getAllErrors());
            return "products/editProduct";
        }

        try {
            createCategoryByNewProduct(product, category_id, newCategoryLabel);
            product.setImagePath(productService.uploadImage(imageFile));
            productService.updateProduct(id, product);

            return "redirect:../products";

        } catch (EntityNotFoundException e) {
            logger.warn("Produit non trouvé lors de la mise à jour: {}", e.getMessage());
            model.addAttribute("error", "Produit non trouvé lors de la mise à jour.");
            return "errors/error";
        } catch (Exception e) {
            // Gestion générale des erreurs
            logger.error("Une erreur s'est produite lors de la mise à jour du produit.", e);
            model.addAttribute("error", "Une erreur s'est produite lors de la mise à jour du produit.");
            return "errors/error";
        }
    }

    public void createCategoryByNewProduct(@ModelAttribute Product product, @RequestParam(name = "existingCategoryId", required = false) Long category_id, @RequestParam(name = "newCategoryLabel", required = false) String newCategoryLabel) {
        if (category_id != null) {
            Category existingCategory = categoryService.getCategoryById(category_id);
             product.setCategory(existingCategory);
        } else {
            // Si une nouvelle catégorie est saisie, créez-la et utilisez-la pour le produit
            if (!StringUtils.isEmpty(newCategoryLabel)) {
                Category newCategory = new Category();
                newCategory.setLabel(newCategoryLabel);
                categoryService.saveCategory(newCategory);
                product.setCategory(newCategory);
            }
        }

    }


    //Delete
    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable Long id, Product product,  Model model) {

        try {
            System.out.println(product);
            productService.deleteProduct(id);

            return "redirect:../products";

        } catch (EntityNotFoundException e) {

            // Gestion spécifique de l'exception EntityNotFoundException
            logger.warn("Produit non trouvé lors de la suppression: {}", e.getMessage());
            model.addAttribute("error", "Produit non trouvé lors de la suppression.");
            return "errors/error";

        } catch (Exception e) {

            // Gestion générale des erreurs
            logger.error("Une erreur s'est produite lors de la suppression du produit.", e);
            model.addAttribute("error", "Une erreur s'est produite lors de la suppression du produit.");
            return "errors/error";
        }
    }

    @GetMapping("/promotions")
    public ResponseEntity<List<Product>> getPromotionalProducts() {
        try {
            List<Product> promotionalProducts = productService.getPromotionalProducts();
            return ResponseEntity.ok(promotionalProducts);
        } catch (NoProductsFoundException e) {

            logger.warn("Exception lors de la récupération des produits en promotion: {}", e.getMessage());
            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            logger.error("Une erreur s'est produite lors de la récupération des produits en promotion.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/savePromotion")
    @ResponseBody
    public Product savePromotion(@RequestParam Long productId,
                                 @RequestParam String startDate,
                                 @RequestParam String endDate,
                                 @RequestParam double discountPercentage) {
        Promotion promotion = new Promotion(LocalDate.parse(startDate), LocalDate.parse(endDate), discountPercentage);
        promotionService.addPromotion(promotion);
        Product product = productService.getProductById(productId);
        product.setPromotion(promotion);
        product.setPrix(product.getPrix(), discountPercentage);

        productService.updateProduct(productId, product);

        return product;
    }

    @GetMapping("/byUser/{user_id}")
    public ResponseEntity<List<Product>> getProductsByAdmin(@PathVariable Long user_id) {
        try {
            List<Product> productsByUser = productService.getProductsByUser(user_id);
            return ResponseEntity.ok(productsByUser);
        } catch (NoProductsFoundException e) {

            logger.warn("Exception lors de la récupération des produits par admin: {}", e.getMessage());
            throw new EntityNotFoundException("L'administrateur avec l'id : " + user_id + " n'existe pas");
            //return ResponseEntity.notFound().build();

        } catch (Exception e) {
            logger.error("Une erreur s'est produite lors de la récupération des produits par l'administrateur.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}