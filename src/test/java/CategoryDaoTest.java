
import data.CategoryDao;
import data.DataAccessException;
import model.Category;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.awt.*;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


final class CategoryDaoTest {
    private static EmbeddedDataSource dataSource;
    private CategoryDao categoryDao;

    @BeforeAll
    static void initTestDataSource() {
        dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName("memory:piggy-bank-test");
        dataSource.setCreateDatabase("create");
    }

    @BeforeEach
    void createCategoryDao() throws SQLException {
        categoryDao = new CategoryDao(dataSource);
        try (var connection = dataSource.getConnection(); var st = connection.createStatement()) {
            st.executeUpdate("DELETE FROM APP.CATEGORIES");
        }
    }

    @AfterEach
    void cleanUp() {
        categoryDao.dropTable();
    }


    @Test
    void createCategory() {
        var food = new Category("Food", Color.red);
        categoryDao.create(food);

        assertThat(food.getId())
                .isNotNull();
        assertThat(categoryDao.findAll())
                .usingFieldByFieldElementComparator()
                .containsExactly(food);
    }

    @Test
    void createTransactionWithExistingId() {
        var food = new Category("Food", Color.red);
        food.setId(123L);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> categoryDao.create(food))
                .withMessage("Category already has ID: " + food);
    }

    @Test
    void createTransactionWithException() {
        var sqlException = new SQLException();
        CategoryDao failingDao = createFailingDao(sqlException);

        var food = new Category("Food", Color.red);
        assertThatExceptionOfType(DataAccessException.class)
                .isThrownBy(() -> failingDao.create(food))
                .withMessage("Failed to store category " + food)
                .withCause(sqlException);
    }

    @Test
    void findAllEmpty() {
        assertThat(categoryDao.findAll())
                .isEmpty();
    }

    @Test
    void findAll() {
        var food = new Category("Food", Color.red);
        var cars = new Category("Cars", Color.blue);
        var house = new Category("House", Color.green);

        categoryDao.create(food);
        categoryDao.create(cars);
        categoryDao.create(house);


        assertThat(categoryDao.findAll())
                .usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(food, cars, house);
    }

    @Test
    void findAllWithException() {
        var sqlException = new SQLException();
        CategoryDao failingDao = createFailingDao(sqlException);

        assertThatExceptionOfType(DataAccessException.class)
                .isThrownBy(failingDao::findAll)
                .withMessage("Failed to load all categories")
                .withCause(sqlException);
    }

    @Test
    void delete() {
        var food = new Category("Food", Color.red);
        var cars = new Category("Cars", Color.blue);

        categoryDao.create(food);
        categoryDao.create(cars);
        categoryDao.delete(food);

        assertThat(categoryDao.findAll())
                .usingFieldByFieldElementComparator()
                .containsExactly(cars);
    }

    @Test
    void deleteWithNullId() {
        var food = new Category("Food", Color.red);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> categoryDao.delete(food))
                .withMessage("Category has null ID: " + food);
    }

    @Test
    void deleteNonExisting() {
        var food = new Category("Food", Color.red);
        food.setId(123L);

        assertThatExceptionOfType(DataAccessException.class)
                .isThrownBy(() -> categoryDao.delete(food))
                .withMessage("Failed to delete non-existing category: " + food);
    }

    @Test
    void deleteWithException() {
        var sqlException = new SQLException();
        CategoryDao failingDao = createFailingDao(sqlException);

        var food = new Category("Food", Color.red);
        food.setId(123L);

        assertThatExceptionOfType(DataAccessException.class)
                .isThrownBy(() -> failingDao.delete(food))
                .withMessage("Failed to delete category " + food)
                .withCause(sqlException);
    }

    @Test
    void update() {
        var food = new Category("Food", Color.red);
        var cars = new Category("Cars", Color.blue);

        categoryDao.create(food);
        categoryDao.create(cars);

        food.setName("newFood");
        food.setColor(Color.red);

        categoryDao.update(food);

        assertThat(categoryDao.findById(food.getId()))
                .isEqualToComparingFieldByField(food);
        assertThat(categoryDao.findById(cars.getId()))
                .isEqualToComparingFieldByField(cars);
    }

    @Test
    void updateWithNullId() {
        var food = new Category("Food", Color.red);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> categoryDao.update(food))
                .withMessage("Category has null ID: " + food);
    }

    @Test
    void updateWithException() {
        var sqlException = new SQLException();
        CategoryDao failingDao = createFailingDao(sqlException);

        var food = new Category("Food", Color.red);
        food.setId(123L);

        assertThatExceptionOfType(DataAccessException.class)
                .isThrownBy(() -> failingDao.update(food))
                .withMessage("Failed to update category " + food)
                .withCause(sqlException);
    }

    private CategoryDao createFailingDao(Throwable exceptionToBeThrown) {
        try {
            var dataSource = mock(DataSource.class);
            when(dataSource.getConnection()).thenAnswer(i -> CategoryDaoTest.dataSource.getConnection());
            var categoryDao = new CategoryDao(dataSource);
            when(dataSource.getConnection()).thenThrow(exceptionToBeThrown);
            return categoryDao;
        } catch (SQLException ex) {
            throw new RuntimeException("Mock configuration failed", ex);
        }
    }
}
