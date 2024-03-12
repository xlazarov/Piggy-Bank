package model;

import java.awt.*;
import java.util.Objects;

public class Category {

    private Long id;
    private String name;
    private Color color;

    public Category(String name, Color color){
        setName(name);
        setColor(color);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            Category cat = (Category) obj;
            return cat.getName().equals(this.getName());
        }catch (NullPointerException e){
            return false;
        }
    }
}
