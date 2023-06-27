package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import javax.sql.DataSource;
import java.util.List;

// add the annotations to make this a REST controller
// add the annotation to make this controller the endpoint for the following url
    // http://localhost:8080/categories
// add annotation to allow cross site origin requests
@CrossOrigin
@RestController
public class CategoriesController
{
    private CategoryDao categoryDao;
    private ProductDao productDao;

    private DataSource dataSource;


    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao, DataSource dataSource) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
        this.dataSource = dataSource;
    }

    // create an Autowired controller to inject the categoryDao and ProductDao

    // add the appropriate annotation for a get action

@RequestMapping(path = "/categories" , method = RequestMethod.GET)
    public List<Category> getAll()
    {
        return categoryDao.getAllCategories();
        // find and return all categories
    }

    // add the appropriate annota@tion for a get action
    @RequestMapping(path = "/categories/{categoryID}" , method = RequestMethod.GET)
    public Category getById(@PathVariable int id)
    {
        // get the category by id
        return categoryDao.getById(id);
    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId)
    {
        // get a list of product by categoryId
        return productDao.listByCategoryId(categoryId);
    }

    // add annotation to call this method for a POST action
    // add annotation to ensure that only an ADMIN can call this function
    @PreAuthorize("hasRole('ADMIN')")
@RequestMapping(path = "/categories", method = RequestMethod.POST)
@ResponseStatus(value = HttpStatus.CREATED)
    public Category addCategory(@RequestBody Category category)
    {
        // insert the category
        return categoryDao.create(category);
    }

    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
@PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(path = "categories/{id}", method = RequestMethod.PUT)
    public void updateCategory(@PathVariable int id, @RequestBody Category category)
    {
         categoryDao.update(id , category);
        // update the category by id
    }


    // add annotation to call this method for a DELETE action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(path = "/categories{id}" , method = RequestMethod.DELETE)
    public void deleteCategory(@PathVariable int id)
    {
        categoryDao.delete(id);
        // delete the category by id
    }
}
