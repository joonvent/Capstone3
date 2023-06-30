package org.yearup.data.mysql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import org.yearup.models.Category;



import static org.junit.jupiter.api.Assertions.*;

class MySqlCategoryDaoTest extends BaseDaoTestClass{

    private MySqlCategoryDao dao;
    @BeforeEach
    public void setup()
    {
        dao = new MySqlCategoryDao(dataSource, dataSource);
    }
    @Test
    void getById_shouldReturn_theCorrectCategory() {
        //arrange
        int categoryId = 1;
        Category expected = new Category();
        expected.setCategoryId(categoryId);

          //act
            Category actual = dao.getById(categoryId);


                //assert
        assertNotNull(actual, "should not return null");
        assertEquals(expected.getCategoryId(), actual.getCategoryId());
            }




    @Test
    void delete() {

        //arrange

        int categoryID = 1;


        //act
        dao.delete(categoryID);



        ///assert
        Category deletedCategory = dao.getById(categoryID);
        assertNull(deletedCategory, "Category is gone");
    }
}