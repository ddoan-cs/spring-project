package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.ArrayList;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import com.example.repository.AccountRepository;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Creates a new message after validating its content and associated account.
     *
     * @param message The Message object containing the text, poster ID, and timestamp.
     * @return The new Message entity.
     * @throws IllegalArgumentException if the message text is empty, exceeds 255 characters,
     *                                  or the posting account does not exist.
     */
    public Message createMessage(Message message) {
        String text = message.getMessageText();
        int poster = message.getPostedBy(); 

        if (text.length() <= 0 || text.length() > 255) {
            throw new IllegalArgumentException("Message text length invalid"); 
        }

        if (!(accountRepository.findById(poster).isPresent())) {
            throw new IllegalArgumentException("Account does not exist");
        }

        return messageRepository.save(new Message(poster, text, message.getTimePostedEpoch()));
    }

    /**
     * Retrieves all messages from the repository.
     *
     * @return A list of all Messages.
     */
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>(); 

        for (Message m : messageRepository.findAll()) {
            messages.add(m); 
        }

        return messages; 
    }

    /**
     * Retrieves a message by its ID if it exists.
     *
     * @param message_id The ID of the message to retrieve.
     * @return The corresponding Message entity; otherwise, returns null.
     */
    public Message getMessageById(int message_id) {
        if (messageRepository.existsById(message_id)) {
            return messageRepository.findById(message_id).get(); 
        }

        return null;
    }

    /**
     * Deletes a message by its ID if it exists.
     *
     * @param message_id The ID of the message to delete.
     * @return 1 if deletion was successful, 0 if the message was not found.
     */
    public int deleteMessageById(int message_id) {
        if (messageRepository.existsById(message_id)) {
            messageRepository.deleteById(message_id);
            return 1; 
        }

        return 0; 
    }

    /**
     * Updates the text of an existing message by its ID.
     *
     * @param message_id The ID of the message to update.
     * @param message_text The new message text.
     * @return 1 if update was successful, 0 if the message does not exist or the text is invalid.
     */
    public int updateMessageById(int message_id, String message_text) {
        if (message_text.length() == 0 || message_text.length() > 255) {
            return 0;
        }

        if (messageRepository.existsById(message_id)) {
            Message old_message = messageRepository.getById(message_id);
            old_message.setMessageText(message_text);  
            messageRepository.save(old_message); 

            return 1; 
        }

        return 0; 
    }

    /**
     * Retrieves all messages posted by a specific account.
     *
     * @param account_id The ID of the account.
     * @return A list of all Messages posted by the specified account.
     */
    public List<Message> getAllMessagesById(int account_id) {
        return messageRepository.findByPostedBy(account_id); 
    }
}
