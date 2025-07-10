package com.financiaPro.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.financiaPro.app.models.Repayement;
import com.financiaPro.app.service.RepaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/repayments")
@Tag(name = "Repayments", description = "Gestion des remboursements de prêts")
public class RepaymentController {

  @Autowired
  private RepaymentService repaymentService;

  @Operation(
    summary = "Effectuer un remboursement",
    description = "Permet à un emprunteur de rembourser une partie ou la totalité d’un prêt"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "202", description = "Remboursement effectué avec succès"),
    @ApiResponse(responseCode = "400", description = "Requête invalide"),
    @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
  })
  @PostMapping("/loan/{loanRequestId}/repay")
  public ResponseEntity<Object> createRepayment(
    @Parameter(description = "Identifiant du prêt", required = true)
    @PathVariable Long loanRequestId,

    @Parameter(description = "Clé API de l'utilisateur", required = true)
    @RequestHeader("X-API-KEY") String userApiKey,

    @Parameter(description = "Données du remboursement", required = true)
    @RequestBody Repayement repayement
  ) {
    try {
      Repayement repayment = repaymentService.createRepayement(loanRequestId, userApiKey, repayement);
      return new ResponseEntity<>(repayment, HttpStatus.ACCEPTED);
    } catch (IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Operation(
    summary = "Lister les remboursements d’un prêt",
    description = "Retourne l’historique des remboursements pour un prêt spécifique"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Liste des remboursements retournée"),
    @ApiResponse(responseCode = "400", description = "Requête invalide"),
    @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
  })
  @GetMapping("/loan/{loanRequestId}/repayments")
  public ResponseEntity<Object> getAllRepayments(
    @Parameter(description = "Identifiant du prêt", required = true)
    @PathVariable Long loanRequestId
  ) {
    try {
      List<Repayement> allRepayements = repaymentService.getAllRepayments(loanRequestId);
      return new ResponseEntity<>(allRepayements, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
