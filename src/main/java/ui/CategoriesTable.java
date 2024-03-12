package ui;

import data.CategoryDao;
import model.Category;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriesTable extends AbstractEntityTableModel<Category> {
    private static final I18N I18N = new I18N(CategoriesTable.class);

    private static final List<Column<?, Category>> COLUMNS = List.of(
            Column.readOnly(I18N.getString("name"), String.class, Category::getName),
            Column.readOnly(I18N.getString("color"), Color.class, Category::getColor)
    );

    private final List<Category> categories;
    private final CategoryDao categoryDao;
    private static Category others;

    public CategoriesTable(CategoryDao categoryDao) {
        super(COLUMNS);
        this.categoryDao = categoryDao;
        this.categories = new ArrayList<>(categoryDao.findAll());
        addDefaultCategory();
    }

    private void updateCategories() {
        for (Category category : categories) {
            categoryDao.update(category);
        }
    }

    private void addDefaultCategory() {
        for (Category category : categories) {
            if (category.getName().equals("Others")) {
                others = category;
                return;
            }
        }
        others = new Category("Others", Color.GRAY);
        this.categories.add(others);
        categoryDao.create(others);
    }

    @Override
    public int getRowCount() {
        return categories.size();
    }

    @Override
    protected Category getEntity(int rowIndex) {
        return categories.get(rowIndex);
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<String> getCategoriesNames(){
        List<String> cats = new ArrayList<>();
        for (Category c : categories){
            cats.add(c.getName());
        }
        return cats;
    }

    public Category getOthers() {
        return others;
    }

    public void addRow(Category category) {
        categoryDao.create(category);
        categories.add(category);
        fireTableDataChanged();
    }

    @Override
    protected void updateEntity(Category category) {
        categoryDao.update(category);
        updateCategories();
        fireTableDataChanged();
    }

    public boolean deleteRow(int rowIndex) {
        if (!categories.get(rowIndex).getName().equals("Others")) {
            categoryDao.delete(categories.get(rowIndex));
            categories.remove(rowIndex);
            fireTableDataChanged();
            return true;
        }
        return false;

    }
}
