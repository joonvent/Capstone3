package org.yearup.data.mysql;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {

    private DataSource dataSource;

    @Autowired
    public MySqlCategoryDao(DataSource dataSource, DataSource dataSource1) {
        super(dataSource);
        this.dataSource = dataSource1;
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();

        // get all categories
        String query = "SELECT * FROM Categories";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Category category = mapRow(resultSet);
                categories.add(category);


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    @Override
    public  Category getById(int categoryId) {
        Category category = null;
        String sql = "SELECT * FROM categories WHERE category_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, categoryId);
            ResultSet row = statement.executeQuery();

            if (row.next()) {
                return mapRow(row);

            }else return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



        @Override
        public Category create (Category category)
        {
            // create a new category
            String query = "INSERT INTO categories ( name , description) VALUES (? , ?)";

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query , Statement.RETURN_GENERATED_KEYS)) {


                statement.setString(1, category.getName());
                statement.setString(2, category.getDescription());

                int rowsChanged = statement.executeUpdate();
                if (rowsChanged == 0) {
                     throw new SQLException("FAILED");
                }
            try(ResultSet Key = statement.getGeneratedKeys()) {
                if (Key.next()){
                    int keys = Key.getInt(1);
                    category.setCategoryId(keys);
                    return  category;
                }
            }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void update ( int categoryId, Category category)
        {
            // update category
            String query = "UPDATE categories SET category_id = ? , name = ? , description = ?";

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, category.getCategoryId());
                preparedStatement.setString(2, category.getName());
                preparedStatement.setString(3, category.getDescription());

                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void delete ( int categoryId)
        {
            // delete category
            String query = "DELETE FROM categories WHERE category_id = ?";

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, categoryId);

                statement.executeUpdate();

            } catch (SQLException e) {
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




