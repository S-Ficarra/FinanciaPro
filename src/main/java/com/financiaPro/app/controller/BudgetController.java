package com.financiaPro.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.financiaPro.app.models.BudgetItem;
import com.financiaPro.app.service.BudgetItemService;

@RestController
@RequestMapping("/api/budget")
public class BudgetController {
  @Autowired
  private BudgetItemService budgetItemService;


  @PostMapping("/")
  public ResponseEntity<Object> createBudgetItem(@RequestBody BudgetItem budgetItem) {
    try {
      BudgetItem newBudgetItem = budgetItemService.createBudgetItem(budgetItem);
      return new ResponseEntity<>(newBudgetItem, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> getBudgetItem(@PathVariable Long id) {
    try {
        BudgetItem budgetItem = budgetItemService.getBudgetItemById(id);
        return new ResponseEntity<>(budgetItem, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
