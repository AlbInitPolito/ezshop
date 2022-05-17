# EZShop Exceptions

`InvalidCreditCardException`: if the credit card number is empty, null or if luhn algorithm does not validate the credit card.
<br />
`InvalidCustomerCardException`: if the customer card is empty, null or if it is not in a valid format (string with 10 digits).
<br />
`InvalidCustomerIdException`: if the id is null, less than or equal to 0.
<br />
`InvalidCustomerNameException`: if the customer name is empty or null.
<br />
`InvalidDiscountRateException`: if the discount rate is less than 0 or if it is greater than or equal to 1.00.
<br />
`InvalidLocationException`: if the product location is in an invalid format (not <aisleNumber>-<rackAlphabeticIdentifier>-<levelNumber>).
<br />
`InvalidOrderIdException`: if the order id is less than or equal to 0 or if it is null.
<br />
`InvalidPasswordException`: if the password has an invalid value (empty or null).
<br />
`InvalidPaymentException`: if the cash is less than or equal to 0.
<br />
`InvalidPricePerUnitException`: if the price per unit si less than or equal to 0.
<br />
`InvalidProductCodeException`: if the product code is null or empty, if it is not a number or if it is not a valid barcode.
<br />
`InvalidProductDescriptionException`: if the product description is null or empty.
<br />
`InvalidProductIdException`: if the product id is less than or equal to 0 or if it is null.
<br />
`InvalidQuantityException`: if the quantity is less than or equal to 0.
<br />
`InvalidRoleException`: if the role has an invalid value (empty, null or not among the set of admissible values).
<br />
`InvalidTransactionIdException`: if the transaction id less than or equal to 0 or if it is null.
<br />
`InvalidUserIdException`: if id is less than or equal to 0 or if it is null.
<br />
`InvalidUsernameException`: if the username is empty or null.
<br />
`UnauthorizedException`: if there is no logged user or if it has not the rights to perform the operation.
<br />