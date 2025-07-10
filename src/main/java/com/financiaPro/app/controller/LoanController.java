package com.financiaPro.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.financiaPro.app.DTOs.LoanRequestDTO;
import com.financiaPro.app.models.LoanRequest;
import com.financiaPro.app.models.LoanStatus;
import com.financiaPro.app.models.User;
import com.financiaPro.app.service.LoanService;
import com.financiaPro.app.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/loan")
@Tag(name = "Loans", description = "Gestion des demandes de prêt")
public class LoanController {

    @Autowired
    private LoanService loanService;
    @Autowired
    private UserService userService;

    @Operation(summary = "Faire une demande de prêt", description = "Soumettre une nouvelle demande de prêt")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Demande de prêt créée"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PostMapping("/request")
    public ResponseEntity<Object> requestLoan(
        @Parameter(description = "Données de la demande de prêt", required = true) @RequestBody LoanRequestDTO loanRequestDTO,
        @Parameter(description = "Clé API de l'utilisateur", required = true) @RequestHeader("X-API-KEY") String apiKey) {

        try {
            User requestingUser = userService.getUserByApiKey(apiKey);

            LoanRequest loanRequest = new LoanRequest();

            loanRequest.setBorrowerId(requestingUser.getId());
            loanRequest.setLenderId(loanRequestDTO.getLenderId());
            loanRequest.setBorrowerId(loanRequestDTO.getBorrowerId());
            loanRequest.setAmount(loanRequestDTO.getAmount());
            loanRequest.setDuration(loanRequestDTO.getDuration());
            loanRequest.setInterest(loanRequestDTO.getInterest());
            loanRequest.setStatus(LoanStatus.PENDING);

            LoanRequest newLoanRequest = loanService.createLoanRequest(loanRequest);
            return new ResponseEntity<>(newLoanRequest, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Récupérer les demandes de prêt en attente", description = "Liste des demandes de prêt en attente pour l'utilisateur connecté")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste retournée"),
        @ApiResponse(responseCode = "400", description = "Clé API invalide"),
        @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/incoming")
    public ResponseEntity<Object> getLoanRequests(
        @Parameter(description = "Clé API de l'utilisateur", required = true) @RequestHeader("X-API-KEY") String apiKey) {

        try {
            User user = userService.getUserByApiKey(apiKey);
            List<LoanRequest> allLoanRequests = loanService.getUserPendingLoanRequest(user.getId());

            return new ResponseEntity<>(allLoanRequests, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Récupérer l'historique des demandes de prêt", description = "Liste historique des demandes de prêt de l'utilisateur connecté")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historique retourné"),
        @ApiResponse(responseCode = "400", description = "Clé API invalide"),
        @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/history")
    public ResponseEntity<Object> getHistoryLoanRequest(
        @Parameter(description = "Clé API de l'utilisateur", required = true) @RequestHeader("X-API-KEY") String apiKey) {

        try {
            User user = userService.getUserByApiKey(apiKey);
            List<LoanRequest> allLoanRequests = loanService.getUserHistoryLoanRequest(user.getId());

            return new ResponseEntity<>(allLoanRequests, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Accepter une demande de prêt", description = "Accepte une demande de prêt spécifique")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "202", description = "Demande acceptée"),
        @ApiResponse(responseCode = "206", description = "Demande acceptée avec avertissement sur revenus faibles"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PutMapping("/{id}/accept")
    public ResponseEntity<Object> acceptLoanRequest(
        @Parameter(description = "ID de la demande de prêt à accepter", required = true) @PathVariable Long id,
        @Parameter(description = "Clé API de l'utilisateur", required = true) @RequestHeader("X-API-KEY") String apiKey) {

        try {
            LoanRequest acceptedLoanRequest = loanService.acceptLoanRequest(id, apiKey);
            User borrower = userService.getUserById(acceptedLoanRequest.getBorrowerId());

            if (acceptedLoanRequest.getAmount() > borrower.getRevenues() * 2) {
                return new ResponseEntity<>("Loan request accepted. However, the borrower's revenues are low.", HttpStatus.PARTIAL_CONTENT);
            } else {
                return new ResponseEntity<>(acceptedLoanRequest, HttpStatus.ACCEPTED);
            }

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Refuser une demande de prêt", description = "Refuse une demande de prêt spécifique")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "202", description = "Demande refusée"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PutMapping("/{id}/refuse")
    public ResponseEntity<Object> refuseLoanRequest(
        @Parameter(description = "ID de la demande de prêt à refuser", required = true) @PathVariable Long id,
        @Parameter(description = "Clé API de l'utilisateur", required = true) @RequestHeader("X-API-KEY") String apiKey) {

        try {
            LoanRequest acceptedLoanRequest = loanService.refuseLoanRequest(id, apiKey);
            return new ResponseEntity<>(acceptedLoanRequest, HttpStatus.ACCEPTED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
