package com.financiaPro.app.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Budget", description = "Gestion des éléments budgétaires")
public class BudgetController {

  @Autowired
  private BudgetItemService budgetItemService;

  @Operation(summary = "Ajouter un élément budgétaire", description = "Crée un nouvel élément dans le budget")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Élément créé avec succès"),
      @ApiResponse(responseCode = "500", description = "Erreur serveur")
  })
  @PostMapping("/budget/add")
  public ResponseEntity<Object> createBudgetItem(
      @Parameter(description = "Données de l'élément budgétaire", required = true)
      @RequestBody BudgetItem budgetItem) {
    try {
      BudgetItem newBudgetItem = budgetItemService.createBudgetItem(budgetItem);
      return new ResponseEntity<>(newBudgetItem, HttpStatus.CREATED);
    } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Operation(summary = "Lister les éléments budgétaires filtrés", description = "Récupère les éléments selon les filtres date, type et montant")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Liste des éléments retournée"),
      @ApiResponse(responseCode = "404", description = "Aucun élément trouvé"),
      @ApiResponse(responseCode = "500", description = "Erreur serveur")
  })
  @GetMapping("/budget")
  public ResponseEntity<Object> getFilteredBudgetItems(
      @Parameter(description = "Filtrer par date") @RequestParam(value = "date", required = false) Date date,
      @Parameter(description = "Filtrer par type") @RequestParam(value = "type", required = false) BudgetType type,
      @Parameter(description = "Filtrer par montant") @RequestParam(value = "amount", required = false) Float amount) {

    try {
        List<BudgetItem> budgetItems = budgetItemService.getBudgetItems(date, type, amount);
        return new ResponseEntity<>(budgetItems, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Operation(summary = "Récupérer un élément budgétaire", description = "Récupère un élément par son identifiant")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Élément trouvé"),
      @ApiResponse(responseCode = "404", description = "Élément non trouvé"),
      @ApiResponse(responseCode = "500", description = "Erreur serveur")
  })
  @GetMapping("/budget/{id}")
  public ResponseEntity<Object> getBudgetItem(
      @Parameter(description = "Identifiant de l'élément", required = true) @PathVariable Long id) {

    try {
        BudgetItem budgetItem = budgetItemService.getBudgetItemById(id);
        return new ResponseEntity<>(budgetItem, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Operation(summary = "Supprimer un élément budgétaire", description = "Supprime un élément selon son identifiant")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Élément supprimé"),
      @ApiResponse(responseCode = "404", description = "Élément non trouvé"),
      @ApiResponse(responseCode = "500", description = "Erreur serveur")
  })
  @DeleteMapping("/budget/{id}")
  public ResponseEntity<Object> deleteBudgetItem(
      @Parameter(description = "Identifiant de l'élément à supprimer", required = true) @PathVariable Long id) {

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
