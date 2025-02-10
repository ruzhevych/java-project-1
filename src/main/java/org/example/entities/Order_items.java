package org.example.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Order_items {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_items_order"), nullable = false)
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "game_id", foreignKey = @ForeignKey(name = "fk_order_items_game"), nullable = false)
    private Game game;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Game getGame() {
        return game;
    }
    public void setGame(Game game) {
        this.game = game;
    }
    public Orders getOrder() {
        return order;
    }
    public void setOrder(Orders order) {
        this.order = order;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
