package com.example.projekt2_gruppe4.repository;

import com.example.projekt2_gruppe4.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class ProductRepository {

    @Autowired
    private DataSource dataSource;

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> tempProductList = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));
                product.setDescription(resultSet.getString("description"));
                product.setPrice(resultSet.getDouble("price"));
                product.setImage(resultSet.getString("img")); // Bruger "img" i stedet for "image"
                tempProductList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Fejl ved hentning af produkter: " + e.getMessage());
        }
        return tempProductList;
    }

    public Product getProductById(int id) throws SQLException {
        Product product = new Product();
        String sql = "SELECT * FROM products WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    product.setId(resultSet.getInt("id"));
                    product.setName(resultSet.getString("name"));
                    product.setDescription(resultSet.getString("description"));
                    product.setPrice(resultSet.getDouble("price"));
                    product.setImage(resultSet.getString("img")); // Bruger "img" i stedet for "image"
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Fejl ved hentning af produkt med ID " + id + ": " + e.getMessage());
        }
        return product;
    }

    public void deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Fejl ved sletning af produkt med ID " + id + ": " + e.getMessage());
        }
    }

    public void saveProduct(Product product) {
        String sql = "INSERT INTO products (name, description, price, img) VALUES (?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());
            statement.setString(4, product.getImage());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Fejl ved gemning af produkt: " + e.getMessage());
        }
    }

    public void update(Product updatedProduct) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, img = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, updatedProduct.getName());
            statement.setString(2, updatedProduct.getDescription());
            statement.setDouble(3, updatedProduct.getPrice());
            statement.setString(4, updatedProduct.getImage());
            statement.setInt(5, updatedProduct.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Fejl ved opdatering af produkt: " + e.getMessage());
        }
    }
}