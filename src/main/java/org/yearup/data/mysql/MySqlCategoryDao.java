package org.yearup.data.mysql;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories()
    {
        List<Category> categories = new ArrayList<>();

        // get all categories
        String query = "SELECT * FROM Categories";

        try(Connection connection = super.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int category_id = resultSet.getInt("category_id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");

                Category category = new Category();
                category.setCategoryId(category_id);
                category.setName(name);
                category.setDescription(description);

                categories.add(category);

            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return categories;
    }

    @Override
    public Category getById(int categoryId)
    {
        Category category = null;
        // get category by id
        String query = "SELECT * FROM categories WHERE category_id = ?";

        try (Connection connection = super.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, categoryId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int categoryID = resultSet.getInt("category_id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");


                category = new Category();
                category.setCategoryId(categoryID);
                category.setName(name);
                category.setDescription(description);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return category;
    }
    @Override
    public Category create(Category category)
    {
        // create a new category
        String query = "INSERT INTO categories (category_id , name , description) VALUES (? , ? , ?)";

        try(Connection connection = super.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)){

            statement.setInt(1, category.getCategoryId());
            statement.setString(2, category.getName());
            statement.setString(3, category.getDescription());

            int rowsChanged = statement.executeUpdate();
            if (rowsChanged > 0) {
                return category;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(int categoryId, Category category)
    {
        // update category
        String query = "UPDATE categories SET category_id = ? , name = ? , description = ?";

        try(Connection connection = super.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, category.getCategoryId());
            preparedStatement.setString(2, category.getName());
            preparedStatement.setString(3, category.getDescription());

            preparedStatement.executeUpdate();

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int categoryId)
    {
        // delete category
        String query = "DELETE FROM categories WHERE category_id = ?";

        try(Connection connection = super.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){

            statement.setInt(1, categoryId);

            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
