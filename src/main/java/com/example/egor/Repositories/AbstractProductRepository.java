package com.example.egor.Repositories;

import com.example.egor.Entities.AbstractProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbstractProductRepository  extends JpaRepository<AbstractProduct, Long> {
}
