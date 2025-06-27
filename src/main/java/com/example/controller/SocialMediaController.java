package com.example.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List; 

import com.example.entity.*;
import com.example.service.*;
import com.example.exception.*;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    private final AccountService accountService;
    private final MessageService messageService; 

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.messageService = messageService; 
        this.accountService = accountService;
    }

    /**
     * Registers a new account with the given username and password.
     *
     * @param body The Account object containing username and password.
     * @return The new Account created.
     */
    @PostMapping("/register") 
    public ResponseEntity<Account> registerAccount(@RequestBody Account body) {
        Account account = accountService.registerAccount(body);
        return ResponseEntity.ok(account); 
    }

    /**
     * Logs in a user with the provided credentials.
     *
     * @param body The Account object containing username and password.
     * @return The Account object if credentials are valid; 401 if not.
     */
    @PostMapping("login")
    public ResponseEntity<Account> loginToAccount(@RequestBody Account body) {
        Account account = accountService.loginToAccount(body);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); 
        }

        return ResponseEntity.ok(account); 
    }

    /**
     * Creates a new message.
     *
     * @param body The Message object to be created.
     * @return The new Message object.
     */
    @PostMapping("messages") 
    public  ResponseEntity<Message> createMessage(@RequestBody Message body) {
        Message message = messageService.createMessage(body); 
        return ResponseEntity.ok(message);
    } 

    
    /**
     * Retrieves all messages.
     *
     * @return A list of all Messages.
     */
    @GetMapping("messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    /**
     * Retrieves a message by its ID.
     *
     * @param message_id The ID of the message to retrieve.
     * @return The corresponding Message object, or 200 with no body if not found.
     */
    @GetMapping("messages/{message_id}")
    public ResponseEntity<Message> getMessageById(@PathVariable int message_id) {
        Message message = messageService.getMessageById(message_id);
        if (message == null) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.ok(message);
    }

    /**
     * Deletes a message by ID.
     *
     * @param message_id The ID of the message to delete.
     * @return 1 if deleted successfully, 0 if not found.
     */
    @DeleteMapping("messages/{message_id}") 
    public ResponseEntity<Integer> deleteMessageById(@PathVariable int message_id) {
        int rows_updated = messageService.deleteMessageById(message_id);
        if (rows_updated == 0) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.ok(rows_updated);
    }

    /**
     * Updates a message by ID.
     *
     * @param message_id The ID of the message to update.
     * @param body The Message object containing new message text.
     * @return 1 if updated successfully, or 400 if input is invalid.
     */
    @PatchMapping("messages/{message_id}") 
    public ResponseEntity<Integer> updateMessageById(@PathVariable int message_id, @RequestBody Message body) {
        int rows_updated = messageService.updateMessageById(message_id, body.getMessageText());
        if (rows_updated == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(rows_updated);
    }

    /**
     * Retrieves all messages posted by a specific account.
     *
     * @param account_id The ID of the account.
     * @return A list of messages posted by the account.
     */
    @GetMapping("accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getAllMessagesById(@PathVariable int account_id) {
        List<Message> messages = messageService.getAllMessagesById(account_id);
        return ResponseEntity.ok(messages);
    }

    /**
     * Handles UsernameAlreadyExistsException by returning a 409 Conflict status.
     */
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
        return ex.getMessage();
    }

    /**
     * Handles IllegalArgumentException by returning a 400 Bad Request status.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException ex) {
        return ex.getMessage();
    }
}
