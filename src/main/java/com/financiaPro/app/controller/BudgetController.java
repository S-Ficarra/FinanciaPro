package com.financiaPro.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.sql.Date;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.financiaPro.app.models.BudgetItem;
import com.financiaPro.app.models.BudgetType;
import com.financiaPro.app.service.BudgetItemService;

@RestController
@RequestMapping("/api")
public class BudgetController {
  @Autowired
  private BudgetItemService budgetItemService;

  @PostMapping("/budget/add")
  public ResponseEntity<Object> createBudgetItem(@RequestBody BudgetItem budgetItem) {
    try {
      BudgetItem newBudgetItem = budgetItemService.createBudgetItem(budgetItem);
      return new ResponseEntity<>(newBudgetItem, HttpStatus.CREATED);
    } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/budget")
  public ResponseEntity<Object> getFilteredBudgetItems(@RequestParam(value = "date", required = false) Date date, @RequestParam(value = "type", required = false) BudgetType type, @RequestParam(value = "amount", required = false) Float amount) {
    try {
        List<BudgetItem> budgetItems = budgetItemService.getBudgetItems(date, type, amount);
        return new ResponseEntity<>(budgetItems, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/budget/{id}")
  public ResponseEntity<Object> getBudgetItem(@PathVariable Long id) {
    try {
        BudgetItem budgetItem = budgetItemService.getBudgetItemById(id);
        return new ResponseEntity<>(budgetItem, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/budget/{id}")
  public ResponseEntity<Object> deleteBudgetItem(@PathVariable Long id) {
    try {
        String message = budgetItemService.deleteBudgetItem(id);
        return new ResponseEntity<>(message, HttpStatus.OK);  
    } catch (IllegalArgumentException e) {      
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
