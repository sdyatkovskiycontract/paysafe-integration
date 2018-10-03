package com.magenta.sc.paysafe.error;

import com.paysafe.common.PaysafeException;

public class PaysafeExceptionParser {

    /**
     * Checks whether exception is thrown due to card validation failure.
     * @param ev PaysafeException to be checked.
     * @return true, if it is validation error, false otherwise
     */
    public static boolean isValidationError(PaysafeException ev) {
        switch (ev.getCode()) {
            /**
             * Parse authorization errors.
             * @see https://developer.paysafe.com/en/cards/api/#/introduction/error-summary/authorization-errors
             */
            case "3002": //You submitted an invalid card number or brand or combination of card number and brand with your request.
            case "3004": // The zip/postal code must be provided for an AVS check request.
            case "3005": // You submitted an incorrect CVV value with your request.
            case "3006": // You submitted an expired credit card number with your request.
            case "3007": // Your request has failed the AVS check. Note that the amount has still been reserved on the customer's card and will be released in 3–5 business days. Please ensure the billing address is accurate before retrying the transaction.
            case "3017": // You submitted an invalid credit card number with your request.
            case "3025": // The external processing gateway has reported invalid data.
            case "3049": // The bank has requested that you retrieve the card from the cardholder – the credit card has expired.
            case "3050": // The bank has requested that you retrieve the card from the cardholder – fraudulent activity is suspected.
            case "3051": // The bank has requested that you retrieve the card from the cardholder – contact the acquirer for more information.
            case "3052": // The bank has requested that you retrieve the card from the cardholder – the credit card is restricted.
            case "3053": // The bank has requested that you retrieve the card from the cardholder – please call the acquirer.
            case "3054": // The transaction was declined due to suspected fraud.

            /**
             * Parse common errors.
             * @see https://developer.paysafe.com/en/cards/api/#/introduction/error-summary/common-errors
             */
            case "5068": // Either you submitted a request that is missing a mandatory field or the value of a field does not match the format expected.

                return true;

            default:
                return false;
        }
    }
}
