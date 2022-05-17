# Unit Testing Documentation

Authors: Matteo Favretto, Vincenzo Sagristano, Francesca Silvano, Alberto Castrignanò

Date: 18/05/2021

Version: 1.0

# Contents

- [Black Box Unit Tests](#black-box-unit-tests)




- [White Box Unit Tests](#white-box-unit-tests)


# Black Box Unit Tests

    <Define here criteria, predicates and the combination of predicates for each function of each class.
    Define test cases to cover all equivalence classes and boundary conditions.
    In the table, report the description of the black box test case and (traceability) the correspondence with the JUnit test case writing the 
    class and method name that contains the test case>
    <JUnit test classes must be in src/test/java/it/polito/ezshop   You find here, and you can use,  class TestEzShops.java that is executed  
    to start tests
    >

### **Class *ProductTypeModel* - method *getID()/setID()***

**Criteria for method *getID()/setID()*:**

- Input setter
- Output getter

**Predicates for method *getID()/setID()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | (-inf; +inf) |
| | Null |
| Getter output | (-inf; +inf) |
| | Null |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| n in (-inf; +inf) | n | Valid| T1(n, n; True)|testValidSetters()|
| Null | Null | Valid | T2(Null, Null; True) | testNullOrInvalidSetters() |
| n in (-inf; +inf) | m in (-inf; +inf), m != n | Valid | T3(n, m; False) |testValidSetters()|
| Null | m in (-inf; +inf) | Valid |  T4(Null, m; False) | testNullOrInvalidSetters() |
| n in (-inf; +inf) | Null | Valid | T5(n, Null; False) | testValidSetters() |

### **Class *ProductTypeModel* - method *getQuantity()/setQuantity()***

**Criteria for method *getQuantity()/setQuantity()*:**

- Input setter
- Output getter

**Predicates for method *getQuantity()/setQuantity()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | (-inf; +inf) |
| | Null |
| Getter output | (-inf; +inf) |
| | Null |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| n in (-inf; +inf) | n | Valid| T1(n, n; True)|testValidSetters()|
| Null | Null | Valid | T2(Null, Null; True) | testNullOrInvalidSetters() |
| n in (-inf; +inf) | m in (-inf; +inf), m != n | Valid | T3(n, m; False) |testValidSetters()|
| Null | m in (-inf; +inf) | Valid |  T4(Null, m; False) | testNullOrInvalidSetters() |
| n in (-inf; +inf) | Null | Valid | T5(n, Null; False) | testValidSetters() |

### **Class *ProductTypeModel* - method *getAisleID()/setAisleID()***

**Criteria for method *getAisleID()/setAisleID()*:**

- Input setter
- Output getter

**Predicates for method *getAisleID()/setAisleID()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | (-inf; +inf) |
| | Null |
| Getter output | (-inf; +inf) |
| | Null |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| n in (-inf; +inf) | n | Valid| T1(n, n; True)|testValidSetters()|
| Null | Null | Valid | T2(Null, Null; True) | testNullOrInvalidSetters() |
| n in (-inf; +inf) | m in (-inf; +inf), m != n | Valid | T3(n, m; False) |testValidSetters()|
| Null | m in (-inf; +inf) | Valid |  T4(Null, m; False) | testNullOrInvalidSetters() |
| n in (-inf; +inf) | Null | Valid | T5(n, Null; False) | testValidSetters() |

### **Class *ProductTypeModel* - method *getLevelID()/setLevelID()***

**Criteria for method *getLevelID()/setLevelID()*:**

- Input setter
- Output getter

**Predicates for method *getLevelID()/setLevelID()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | (-inf; +inf) |
| | Null |
| Getter output | (-inf; +inf) |
| | Null |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| n in (-inf; +inf) | n | Valid| T1(n, n; True)|testValidSetters()|
| Null | Null | Valid | T2(Null, Null; True) | testNullOrInvalidSetters() |
| n in (-inf; +inf) | m in (-inf; +inf), m != n | Valid | T3(n, m; False) |testValidSetters()|
| Null | m in (-inf; +inf) | Valid |  T4(Null, m; False) | testNullOrInvalidSetters() |
| n in (-inf; +inf) | Null | Valid | T5(n, Null; False) | testValidSetters() |

### **Class *ProductTypeModel* - method *getBarcode()/setBarcode()***

**Criteria for method *getBarcode()/setBarcode()*:**

- Input setter
- Output getter

**Predicates for method *getBarcode()/setBarcode()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | Any possible String s |
| | Null |
| Getter output | String r = s |
| | Any possible String r != s
| | Null |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| Any possible String s | String r = s | Valid| T1(s, r; True) |testValidSetters()|
| Null| Null | Valid | T2(Null, Null; True) | testNullOrInvalidSetters() |
| Any possible String s | Any possible String r, s != r | Valid | T3(s, r; False) |testValidSetters()|
| Null | Any possible String r | Valid | T4(Null, r; False) | testNullOrInvalidSetters() |
| Any possible String s | Null | Valid | T5(s, Null; False) | testValidSetters() |

### **Class *ProductTypeModel* - method *getProductDescription()/setProductDescription()***

**Criteria for method *getProductDescription()/setProductDescription()*:**

- Input setter
- Output getter

**Predicates for method *getProductDescription()/setProductDescription()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | Any possible String s |
| | Null |
| Getter output | String r = s |
| | Any possible String r != s
| | Null |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| Any possible String s | String r = s | Valid| T1(s, r; True) |testValidSetters()|
| Null| Null | Valid | T2(Null, Null; True) | testNullOrInvalidSetters() |
| Any possible String s | Any possible String r, s != r | Valid | T3(s, r; False) |testValidSetters()|
| Null | Any possible String r | Valid | T4(Null, r; False) | testNullOrInvalidSetters() |
| Any possible String s | Null | Valid | T5(s, Null; False) | testValidSetters() |

### **Class *ProductTypeModel* - method *getNote()/setNote()***

**Criteria for method *getNote()/setNote()*:**

- Input setter
- Output getter

**Predicates for method *getNote()/setNote()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | Any possible String s |
| | Null |
| Getter output | String r = s |
| | Any possible String r != s
| | Null |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| Any possible String s | String r = s | Valid| T1(s, r; True) |testValidSetters()|
| Null| Null | Valid | T2(Null, Null; True) | testNullOrInvalidSetters() |
| Any possible String s | Any possible String r, s != r | Valid | T3(s, r; False) |testValidSetters()|
| Null | Any possible String r | Valid | T4(Null, r; False) | testNullOrInvalidSetters() |
| Any possible String s | Null | Valid | T5(s, Null; False) | testValidSetters() |

### **Class *ProductTypeModel* - method *getRackID()/setRackID()***

**Criteria for method *getRackID()/setRackID()*:**

- Input setter
- Output getter

**Predicates for method *getRackID()/setRackID()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | Any possible String s |
| | Null |
| Getter output | String r = s |
| | Any possible String r != s
| | Null |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| Any possible String s | String r = s | Valid| T1(s, r; True) |testValidSetters()|
| Null| Null | Valid | T2(Null, Null; True) | testNullOrInvalidSetters() |
| Any possible String s | Any possible String r, s != r | Valid | T3(s, r; False) |testValidSetters()|
| Null | Any possible String r | Valid | T4(Null, r; False) | testNullOrInvalidSetters() |
| Any possible String s | Null | Valid | T5(s, Null; False) | testValidSetters() |

### **Class *ProductTypeModel* - method *getPricePerUnit()/setPricePerUnit()***

**Criteria for method *getPricePerUnit()/setPricePerUnit()*:**

- Input setter
- Output getter

**Predicates for method *getPricePerUnit()/setPricePerUnit()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | (-inf; +inf) |
| | Null |
| Getter output | (-inf; +inf) |
| | Null |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| n in (-inf; +inf) | n | Valid| T1(n, n; True)|testValidSetters()|
| Null | Null | Valid | T2(Null, Null; True) | testNullOrInvalidSetters() |
| n in (-inf; +inf) | m in (-inf; +inf), m != n | Valid | T3(n, m; False) |testValidSetters()|
| Null | m in (-inf; +inf) | Valid |  T4(Null, m; False) | testNullOrInvalidSetters() |
| n in (-inf; +inf) | Null | Valid | T5(n, Null; False) | testValidSetters() |

### **Class *ProductTypeModel* - method *getDiscountRate()/setDiscountRate()***

**Criteria for method *getDiscountRate()/setDiscountRate()*:**

- Input setter
- Output getter

**Predicates for method *getDiscountRate()/setDiscountRate()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | (-inf; +inf) |
| | Null |
| Getter output | (-inf; +inf) |
| | Null |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| n in (-inf; +inf) | n | Valid| T1(n, n; True)|testValidSetters()|
| Null | Null | Valid | T2(Null, Null; True) | testNullOrInvalidSetters() |
| n in (-inf; +inf) | m in (-inf; +inf), m != n | Valid | T3(n, m; False) |testValidSetters()|
| Null | m in (-inf; +inf) | Valid |  T4(Null, m; False) | testNullOrInvalidSetters() |
| n in (-inf; +inf) | Null | Valid | T5(n, Null; False) | testValidSetters() |

### **Class *ProductTypeModel* - method *getLocation()/setLocation()***

**Criteria for method *getLocation()/setLocation()*:**

- Input setter
- Output getter

**Predicates for method *getLocation()/setLocation()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | String in the format "\<aisleNumber>-\<rackAlphabeticalIdentifier>-\<levelNumber>" |
| | Any possible String s not in the format "\<aisleNumber>-\<rackAlphabeticalIdentifier>-\<levelNumber>" |
| | Null |
| Getter output | String in the format "\<aisleNumber>-\<rackAlphabeticalIdentifier>-\<levelNumber>" |
| | Null |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| String s in the format "\<aisleNumber>-\<rackAlphabeticalIdentifier>-\<levelNumber>" | s | Valid | T1(s, s; True)|testValidSetters()|
| Any possible String s not in the format "\<aisleNumber>-\<rackAlphabeticalIdentifier>-\<levelNumber>" | * | Invalid | T2(s, *; NumberFormatException) | testNullOrInvalidSetters() |
| Null | Null | Valid | T3(Null, Null; True) |testNullOrInvalidSetters()|
| String s in the format "\<aisleNumber>-\<rackAlphabeticalIdentifier>-\<levelNumber>" | String r in the format "\<aisleNumber>-\<rackAlphabeticalIdentifier>-\<levelNumber>", s != r | Valid |  T4(s, r; False) | testValidSetters() |


### **Class *ProductTypeModel* - method *GTIN13Check()***

**Criteria for method *GTIN13Check()*:**

- Barcode passed to the function

**Predicates for method *GTIN13Check()*:**

| Criteria | Predicate |
| -------- | --------- |
| Barcode  | Any possible String |
| | String shorter than 12 |
| | String longer than 14 |
| | Any string of length between 12 and 14 that is not a valid barcode |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|||


**Combination of predicates**:


| Barcode | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
| Valid barcode b | Valid | T1(b; True) | testGTIN13Check()|
| String s shorter than 12 | Valid | T2(s; False) | testGTIN13Check() |
| String s longer than 14 | Valid | T3(s; False) | testGTIN13Check() |
| String s with length between 12 and 14 that is not a valid barcode | Valid | T4(s; False) | testGTIN13Check |

 ### **Class *UserModel* - method *getId()/setId()***

**Criteria for method *getId()/setId()*:**

 - Setter input
 - Getter output

**Predicates for method *getId()/setId()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | (-inf; +inf) |
| | Null |
| Getter output | (-inf; +inf) |
| | Null |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| n in (-inf; +inf) | n | Valid| T1(n, n; True)|testValidSetters()|
| Null | Null | Valid | T2(Null, Null; True) | testNullOrInvalidSetters() |
| n in (-inf; +inf) | m in (-inf; +inf), m != n | Valid | T3(n, m; False) |testValidSetters()|
| Null | m in (-inf; +inf) | Valid |  T4(Null, m; False) | testNullOrInvalidSetters() |
| n in (-inf; +inf) | Null | Valid | T5(n, Null; False) | testValidSetters() |

### **Class *UserModel* - method *getUsername()/setUsername()***

**Criteria for method *getUsername()/setUsername()*:**

- Setter input
- Getter output

**Predicates for method *getUsername()/setUsername()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | Any possible String s |
| | Null |
| Getter output | String r = s |
| | Any possible String r != s
| | Null |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| Any possible String s | String r = s | Valid| T1(s, r; True) |testValidSetters()|
| Null| Null | Valid | T2(Null, Null; True) | testNullOrInvalidSetters() |
| Any possible String s | Any possible String r, s != r | Valid | T3(s, r; False) |testValidSetters()|
| Null | Any possible String r | Valid | T4(Null, r; False) | testNullOrInvalidSetters() |
| Any possible String s | Null | Valid | T5(s, Null; False) | testValidSetters() |

### **Class *UserModel* - method *getPassword()/setPassword()***

**Criteria for method *getPassword()/setPassword()*:**

- Setter input
- Getter output

**Predicates for method *getPassword()/setPassword()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | Any possible String s |
| | Null |
| Getter output | String r = s |
| | Any possible String r != s |
| | Null |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| Any possible String s | String r = s | Valid| T1(s, r; True) |testValidSetters()|
| Null| Null | Valid | T2(Null, Null; True) | testNullOrInvalidSetters() |
| Any possible String s | Any possible String r, s != r | Valid | T3(s, r; False) |testValidSetters()|
| Null | Any possible String r | Valid | T4(Null, r; False) | testNullOrInvalidSetters() |
| Any possible String s | Null | Valid | T5(s, Null; False) | testValidSetters() |

### **Class *UserModel* - method *getRole()/setRole()***

**Criteria for method *getRole()/setRole()*:**

- Setter input
- Getter output

**Predicates for method *getRole()/setRole()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | String s in {"Cashier", "ShopManager", "Administrator"} |
| | String s NOT in {"Cashier", "ShopManager", "Administrator"} |
| | Null |
| Getter output | String s in {"Cashier", "ShopManager", "Administrator"}  |
| | Null |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| String s in {"Cashier", "ShopManager", "Administrator" } | s | Valid| T1(s, s.toUpperCase(); True) |testValidSetters()|
| String s in {"Cashier", "ShopManager", "Administrator" } | Null | Valid | T2(s, Null; False) | testValidSetters() |
| String s in {"Cashier", "ShopManager", "Administrator" } | String r in {"Cashier", "ShopManager", "Administrator" }, s != r | Valid | T3(s, Null; False) | testValidSetters() |
| String s NOT in {"Cashier", "ShopManager", "Administrator" } | * | Invalid | T3(s, *; InvalidArgumentException()) |testNullOrInvalidSetters()|
| Null | Null | Valid | T4(Null, Null; True) | testNullOrInvalidSetters() |


### **Class *CustomerModel* - method *getCustomerName()/setCustomerName()***

**Criteria for method *getCustomerName()/setCustomerName()*:**

- Setter input
- Getter output

**Predicates for method *getCustomerName()/setCustomerName()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | Any possible String s |
| | Null|
| Getter output | String r = s |
| | Any possible String r != s |
| | Null |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| Any possible String s | String r = s | Valid| T1(s, r; True) |testValidSetters()|
| Any possible String s | Any possible String r, s != r | Valid | T2(s, r; False) |testValidSetters()|
| Any possible String s | Null | Valid | T3(s, Null; False) | testValidSetters() |
| Null | Null | Valid | T4(Null, Null; True) | testNullOrInvalidSetters()|
| Null | Any possible String r | Valid | T5(Null, r; False) | testNullOrInvalidSetters() |


### **Class *CustomerModel* - method *getCustomerID()/setCustomerID()***

**Criteria for method *getCustomerID()/setCustomerID()*:**

- Setter input
- Getter output

**Predicates for method *getCustomerId()/setCustomerId()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | (-inf; +inf) |
| | Null |
| Getter output | (-inf; +inf) |
| | Null |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| n in (-inf; +inf) | n | Valid| T1(n, n; True) | testValidSetters() |
| n in (-inf; +inf) | Null | Valid | T2(n, Null; False) | testValidSetters() |
| n in (-inf; +inf) | m in (-inf; +inf), m != n | Valid | T3(n, m; False) | testValidSetters() |
| Null | Null | Valid | T4(Null, Null; True) | testNullOrInvalidSetters() |
| Null | m in (-inf; +inf) | Valid | T5(Null, m; False) | testNullOrInvalidSetters() |


### **Class *LoyaltyCardModel* - method *getPoints()/setPoints()***

**Criteria for method *getPoints()/setPoints()*:**

- Setter input
- Getter output

**Predicates for method *getPoints()/setPoints()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | (-inf; +inf) |
| | Null |
| Getter output | (-inf; +inf) |
| | Null |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| n in (-inf; +inf) | m = n | Valid| T1(n, m; True) | testValidSetters() |
| n in (-inf; +inf) | Null | Valid | T2(n, Null; False) | testValidSetters() |
| n in (-inf; +inf) | m in (-inf; +inf), n != m | Valid | T3(n, m; False) | testValidSetters() |
| Null | Null | Valid | T4(Null, Null; True) | testNullOrInvalidSetters() |
| Null | m in (-inf; +inf) | Valid | T5(Null, m; False) | testNullOrInvalidSetters() |


### **Class *LoyaltyCardModel* - method *getSerialNumber()/setSerialNumber()***

**Criteria for method *getSerialNumber()/setSerialNumber()*:**

- Setter input
- Getter output

**Predicates for method *getSerialNumber()/setSerialNumber()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | Any possible String s |
| | Null |
| Getter output | String r = s |
| | Any possible String r != s |
| | Null


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| Any possible String s | String r = s | Valid| T1(s, r; True) |testValidSetters()|
| Any possible String s | Any possible String r, s != r | Valid | T2(s, r; False) |testValidSetters()|
| Any possible String s | Null | Valid | T3(s, Null; False) | testValidSetters() |
| Null | Null | Valid | T4(Null, Null; True) | testNullOrInvalidSetters()|
| Null | Any possible String r | Valid | T5(Null, r; False) | testNullOrInvalidSetters() |


### **Class *LoyaltyCardModel* - method *checkSerialNumberFormat()***

**Criteria for method *checkSerialNumberFormat()*:**

- Checker input

**Predicates for method *checkSerialNumberFormat()*:**

| Criteria | Predicate |
| -------- | --------- |
| Checker input | String of 10 digits |
| | String of length != 10 |
| | String with non-numeric characters |
| | Null |


**Combination of predicates**:


| Checker input  | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
| String s of 10 digits | Valid| T1(s; True)|testCheckSerialNumberFormat()|
| String s of length != 10  | Valid | T2(s; False) |testCheckSerialNumberFormat()|
| String s with non-numeric characters | Valid | T3(s; False) | testCheckSerialNumberFormat()
| Null | Invalid | T4(Null; NullPointerException()) | testCheckSerialNumberFormat()

### **Class *TicketEntryModel* - method *getBarcode()/setBarcode()***

**Criteria for method *getBarcode()/setBarcode()*:**

- Input setter
- Output getter

**Predicates for method *getBarcode()/setBarcode()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | Any possible String s |
| | Null |
| Getter output | String r = s |
| | Any possible String r != s |
| | Null


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| Any possible String s | String r = s | Valid| T1(s, r; True) |testValidSetters()|
| Any possible String s | Any possible String r, s != r | Valid | T2(s, r; False) |testValidSetters()|
| Any possible String s | Null | Valid | T3(s, Null; False) | testValidSetters() |
| Null | Null | Valid | T4(Null, Null; True) | testNullOrInvalidSetters()|
| Null | Any possible String r | Valid | T5(Null, r; False) | testNullOrInvalidSetters() |

### **Class *TicketEntryModel* - method *getProductDescription()/setProductDescription()***

**Criteria for method *getProductDescription()/setProductDescription()*:**

- Input setter
- Output getter

**Predicates for method *getProductDescription()/setProductDescription()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | Any possible String s |
| | Null |
| Getter output | String r = s |
| | Any possible String r != s |
| | Null


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| Any possible String s | String r = s | Valid| T1(s, r; True) |testValidSetters()|
| Any possible String s | Any possible String r, s != r | Valid | T2(s, r; False) |testValidSetters()|
| Any possible String s | Null | Valid | T3(s, Null; False) | testValidSetters() |
| Null | Null | Valid | T4(Null, Null; True) | testNullOrInvalidSetters()|
| Null | Any possible String r | Valid | T5(Null, r; False) | testNullOrInvalidSetters() |

### **Class *TicketEntryModel* - method *getAmount()/setAmount()***

**Criteria for method *getAmount()/setAmount()*:**

- Setter input
- Getter output

**Predicates for method *getAmount()/setAmount()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | (-inf; +inf)) |
| Getter output | (-inf; +inf)) |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| n in (-inf; +inf) | n | Valid| T1(n, n; True)|testValidSetters()|
| n in (-inf; +inf) | m in (-inf; +inf), m != n | Valid | T2(n, m; False) |testValidSetters()|

### **Class *TicketEntryModel* - method *getDiscountRate()/setDiscountRate()***

**Criteria for method *getDiscountRate()/setDiscountRate()*:**

- Setter input
- Getter output

**Predicates for method *getDiscountRate()/setDiscountRate()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | (-inf; +inf)) |
| | Null |
| Getter output | (-inf; +inf)) |
| | Null |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| n in (-inf; +inf) | n | Valid| T1(n, n; True)|testValidSetters()|
| n in (-inf, +inf) | m in (-inf, +inf), n != m | Valid | T2(n, m; False) | testValidSetters()
| Null | m in (-inf, +inf) | Valid | T3(Null, m; False) | testNullOrInvalidSetters()|
| n in (-inf; +inf) | m in (-inf; +inf), m != n | Valid | T2(n, m; False) |testValidSetters()|
| Null | Null | Valid | T3(Null, Null; True) | testNullOrInvalidSetters()|

### **Class *TicketEntryModel* - method *getPricePerUnit()/setPricePerUnit()***

**Criteria for method *getPricePerUnit()/setPricePerUnit()*:**

- Setter input
- Getter output

**Predicates for method *getPricePerUnit()/setPricePerUnit()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | (-inf; +inf)) |
| Getter output | (-inf; +inf)) |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| n in (-inf; +inf) | n | Valid| T1(n, n; True)|testValidSetters()|
| n in (-inf; +inf) | m in (-inf; +inf), m != n | Valid | T2(n, m; False) |testValidSetters()|

### **Class *BalanceOperationModel* - method *getBalanceId()/setBalanceId()***

**Criteria for method *getBalanceId()/setBalanceId()*:**

- Setter input
- Getter output

**Predicates for method *getBalanceId()/setBalanceId()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | (-inf; +inf)) |
| Getter output | (-inf; +inf)) |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| n in (-inf; +inf) | n | Valid| T1(n, n; True) |testValidSetters()|
| n in (-inf; +inf) | m in (-inf; +inf), m != n | Valid | T2(n, m; False) |testValidSetters()|

### **Class *BalanceOperationModel* - method *getDate()/setDate()***

**Criteria for method *getDate()/setDate()*:**

- Setter input
- Getter output

**Predicates for method *getDate()/setDate()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | Date in LocalDate format (yyyy-mm-dd) |
| | Null |
| Getter output | Date in LocalDate format (yyyy-mm-dd) |
| | Null |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| Date in LocalDate format d | d | Valid| T1(d, d; True) |testValidSetters()|
| Date in LocalDate format d | Date in LocalDate format o, o != d | Valid | T2(d, o; False) |testValidSetters()|
| Date in LocalDate format d | Null | Valid | T3(d, Null; False) | testValidSetters()|
| Null | Null | Valid | T4(Null, Null; True) | testNullOrInvalidSetters() |
| Null | Date in LocalDate format d | Valid | T5(Null, d; False) | testNullOrInvalidSetters() |

### **Class *BalanceOperationModel* - method *getMoney()/setMoney()***

**Criteria for method *getMoney()/setMoney()*:**

- Setter input
- Getter output

**Predicates for method *getMoney()/setMoney()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | (-inf; +inf)) |
| Getter output | (-inf; +inf)) |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| n in (-inf; +inf) | n | Valid| T1(n, n; True) |testValidSetters()|
| n in (-inf; +inf) | m in (-inf; +inf), m != n | Valid | T2(n, m; False) |testValidSetters()|

### **Class *BalanceOperationModel* - method *getType()/setType()***

**Criteria for method *getMoney()/setMoney()*:**

- Setter input
- Getter output

**Predicates for method *getType()/setType()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | String s in {"credit", "debit", "return", "sale", "order" (Case insensitive)} |
| | String s NOT in {"credit", "debit", "return", "sale", "order" (Case insensitive)} |
| | Null |
| Getter output | String s in {"CREDIT", "DEBIT", "RETURN", "SALE", "ORDER"}  |
| | Null |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| String s in {"credit", "debit", "return", "sale", "order" (Case insensitive)} | s.toUpperCase() | Valid| T1(s, s.toUpperCase(); True) |testValidSetters()|
| String s in {"credit", "debit", "return", "sale", "order" (Case insensitive)} | Null | Valid | T2(s, Null; False) | testValidSetters() |
| String s NOT in {"credit", "debit", "return", "sale", "order" (Case insensitive)}  | * | Invalid | T3(s, *; InvalidArgumentException()) |testNullOrInvalidSetters()|
| Null | Null | Valid | T4(Null, Null; True) | testNullOrInvalidSetters() |

### **Class *CreditCardModel* - method *hasEnoughMoney()***

**Criteria for method *hasEnoughMoney()*:**

- Credit card balance
- Amount of money to pay

**Predicates for method *hasEnoughMoney()*:**

| Criteria | Predicate |
| -------- | --------- |
| Credit card balance | [0, +inf) |
| Amount of money to pay | [0, +inf) |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
| Credit card balance | 0, +inf |
| Amount of money to pay | 0, +inf |

**Combination of predicates**:


| Credit card balance | Amount of money to pay | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| n in [0, +inf) | m in [0, +inf), m <= m | Valid | T1(n, m; True) </br> T1b(+inf, 0; True) | testHasEnoughMoney()|
| n in [0, +inf) | m in [0, +inf), m > n | Valid | T2(n, m; False) </br> T2b(+inf, 0; False) | testHasEnoughMoney()|

### **Class *CreditCardModel* - method *getCreditCardBalance()/setCreditCardBalance()***

**Criteria for method *getCreditCardBalance()/setCreditCardBalance()*:**

- Setter input
- Getter output

**Predicates for method *getCreditCardBalance()/setCreditCardBalance()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | (-inf; +inf) |
| | Null |
| Getter output | (-inf; +inf) |
| | Null |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| n in (-inf; +inf) | m = n | Valid| T1(n, m; True) | testValidSetters() |
| n in (-inf; +inf) | Null | Valid | T2(n, Null; False) | testValidSetters() |
| Null | Null | Valid | T3(Null, Null; True) | testNullOrInvalidSetters() |
| Null | m in (-inf; +inf) | Valid | T4(Null, m; False) | testNullOrInvalidSetters() |

### **Class *CreditCardModel* - method *getCreditCardNumber()/setCreditCardNumber()***

**Criteria for method *getCreditCardNumber()/setCreditCardNumber()*:**

- Input setter
- Output getter

**Predicates for method *getCreditCardNumber()/setCreditCardNumber()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | Any possible String s |
| | Null |
| Getter output | String r = s |
| | Any possible String r != s |
| | Null


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| Any possible String s | String r = s | Valid| T1(s, r; True) |testValidSetters()|
| Any possible String s | Any possible String r, s != r | Valid | T2(s, r; False) |testValidSetters()|
| Any possible String s | Null | Valid | T3(s, Null; False) | testValidSetters() |
| Null | Null | Valid | T4(Null, Null; True) | testNullOrInvalidSetters()|
| Null | Any possible String r | Valid | T5(Null, r; False) | testNullOrInvalidSetters() |

### **Class *SaleTransactionModel* - method *getTicketNumber()/setTicketNumber()***

**Criteria for method *getTicketNumber()/setTicketNumber()*:**

- Input setter
- Output getter

**Predicates for method *getTicketNumber()/setTicketNumber()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | (-inf; +inf) |
| | Null |
| Getter output | (-inf; +inf) |
| | Null |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| n in (-inf; +inf) | n | Valid| T1(n, n; True) | testValidSetters() |
| n in (-inf; +inf) | Null | Valid | T2(n, Null; False) | testValidSetters() |
| n in (-inf; +inf) | m in (-inf, +inf), m != n | T3(n, m; False) | testValidSetters()
| Null | Null | Valid | T4(Null, Null; True) | testNullOrInvalidSetters() |
| Null | m in (-inf; +inf) | Valid | T5(Null, m; False) | testNullOrInvalidSetters() |

### **Class *SaleTransactionModel* - method *getPaymentType()/setPaymentType()***

**Criteria for method *getPaymentType()/setPaymentType()*:**

- Input setter
- Output getter

**Predicates for method *getPaymentType()/setPaymentType()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | (-inf; +inf) |
| | Null |
| Getter output | (-inf; +inf) |
| | Null |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| n in (-inf; +inf) | n | Valid| T1(n, n; True) | testValidSetters() |
| n in (-inf; +inf) | Null | Valid | T2(n, Null; False) | testValidSetters() |
| n in (-inf; +inf) | m in (-inf, +inf), m != n | T3(n, m; False) | testValidSetters()
| Null | Null | Valid | T4(Null, Null; True) | testNullOrInvalidSetters() |
| Null | m in (-inf; +inf) | Valid | T5(Null, m; False) | testNullOrInvalidSetters() |

### **Class *SaleTransactionModel* - method *getDiscountRate()/setDiscountRate()***

**Criteria for method *getDiscountRate()/setDiscountRate()*:**

- Setter input
- Getter output

**Predicates for method *getDiscountRate()/setDiscountRate()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | (-inf; +inf)) |
| Getter output | (-inf; +inf)) |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| n in (-inf; +inf) | n | Valid| T1(n, n; True) |testValidSetters()|
| n in (-inf; +inf) | m in (-inf; +inf), m != n | Valid | T2(n, m; False) |testValidSetters()|

### **Class *SaleTransactionModel* - method *getPrice()/setPrice()***

**Criteria for method *getPrice()/setPrice()*:**

- Setter input
- Getter output

**Predicates for method *getPrice()/setPrice()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | (-inf; +inf)) |
| Getter output | (-inf; +inf)) |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| n in (-inf; +inf) | n | Valid| T1(n, n; True) |testValidSetters()|
| n in (-inf; +inf) | m in (-inf; +inf), m != n | Valid | T2(n, m; False) |testValidSetters()|

### **Class *SaleTransactionModel* - method *getDate()/setDate()***

**Criteria for method *getDate()/setDate()*:**

- Setter input
- Getter output

**Predicates for method *getDate()/setDate()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | Date in an accepted Java Date format (mostly yyyy-mm-dd) |
| | Null |
| Getter output | Date in an accepted Java Date format (mostly yyyy-mm-dd) |
| | Null |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| Date in an accepted Java Date format d | d | Valid| T1(d, d; True) |testValidSetters()|
| Date in an accepted Java Date format d | Date in an accepted Java Date format o, o != d | Valid | T2(d, o; False) |testValidSetters()|
| Date in an accepted Java Date format d | Null | Valid | T3(d, Null; False) | testValidSetters()|
| Null | Null | Valid | T4(Null, Null; True) | testNullOrInvalidSetters() |
| Null | Date in an accepted Java Date format d | Valid | T5(Null, d; False) | testNullOrInvalidSetters() |

### **Class *SaleTransactionModel* - method *getTime()/setTime()***

**Criteria for method *getTime()/setTime()*:**

- Setter input
- Getter output

**Predicates for method *getTime()/setTime()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | Time in an accepted Java Time format |
| | Null |
| Getter output | Time in an accepted Java Time format |
| | Null |



**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| Time in an accepted Java Time format t | t | Valid| T1(t, t; True) |testValidSetters()|
| Time in an accepted Java Time format t | Time in an accepted Java Time format o, o != t | Valid | T2(t, o; False) |testValidSetters()|
| Time in an accepted Java Time format t | Null | Valid | T3(d, Null; False) | testValidSetters()|
| Null | Null | Valid | T4(Null, Null; True) | testNullOrInvalidSetters() |
| Null | Time in an accepted Java Time format t | Valid | T5(Null, t; False) | testNullOrInvalidSetters() |

### **Class *ReturnTransactionModel* - method *getID()/setID()***

**Criteria for method *getID()/setID()*:**

- Input setter
- Output getter

**Predicates for method *getID()/setID()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | (-inf; +inf) |
| | Null |
| Getter output | (-inf; +inf) |
| | Null |



**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| n in (-inf; +inf) | n | Valid| T1(n, n; True) | testValidSetters() |
| n in (-inf; +inf) | Null | Valid | T2(n, Null; False) | testValidSetters() |
| n in (-inf; +inf) | m in (-inf, +inf), m != n | T3(n, m; False) | testValidSetters()
| Null | Null | Valid | T4(Null, Null; True) | testNullOrInvalidSetters() |
| Null | m in (-inf; +inf) | Valid | T5(Null, m; False) | testNullOrInvalidSetters() |

### **Class *ReturnTransactionModel* - method *getReturnedValue()/setReturnedValue()***

**Criteria for method *getReturnedValue()/setReturnedValue()*:**

- Input setter
- Output getter

**Predicates for method *getReturnedValue()/setReturnedValue()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | (-inf; +inf) |
| | Null |
| Getter output | (-inf; +inf) |
| | Null |



**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| n in (-inf; +inf) | n | Valid| T1(n, n; True) | testValidSetters() |
| n in (-inf; +inf) | Null | Valid | T2(n, Null; False) | testValidSetters() |
| n in (-inf; +inf) | m in (-inf, +inf), m != n | T3(n, m; False) | testValidSetters()
| Null | Null | Valid | T4(Null, Null; True) | testNullOrInvalidSetters() |
| Null | m in (-inf; +inf) | Valid | T5(Null, m; False) | testNullOrInvalidSetters() |

### **Class *ReturnTransactionModel* - method *getTransactionID()/setTransactionID()***

**Criteria for method *getTransactionID()/setTransactionID()*:**

- Input setter
- Output getter

**Predicates for method *getTransactionID()/setTransactionID()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | (-inf; +inf) |
| | Null |
| Getter output | (-inf; +inf) |
| | Null |



**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| n in (-inf; +inf) | n | Valid| T1(n, n; True) | testValidSetters() |
| n in (-inf; +inf) | Null | Valid | T2(n, Null; False) | testValidSetters() |
| n in (-inf; +inf) | m in (-inf, +inf), m != n | T3(n, m; False) | testValidSetters()
| Null | Null | Valid | T4(Null, Null; True) | testNullOrInvalidSetters() |
| Null | m in (-inf; +inf) | Valid | T5(Null, m; False) | testNullOrInvalidSetters() |

### **Class *OrderModel* - method *getBalanceId()/setBalanceId()***

**Criteria for method *getBalanceId()/setBalanceId()*:**

- Setter input
- Getter output

**Predicates for method *getBalanceId()/setBalanceId()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | (-inf; +inf) |
| | Null |
| Getter output | (-inf; +inf) |
| | Null |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| n in (-inf; +inf) | n | Valid| T1(n, n; True) | testValidSetters() |
| n in (-inf; +inf) | Null | Valid | T2(n, Null; False) | testValidSetters() |
| n in (-inf; +inf) | m in (-inf, +inf), m != n | T3(n, m; False) | testValidSetters()
| Null | Null | Valid | T4(Null, Null; True) | testNullOrInvalidSetters() |
| Null | m in (-inf; +inf) | Valid | T5(Null, m; False) | testNullOrInvalidSetters() |

### **Class *OrderModel* - method *getOrderId()/setOrderId()***

**Criteria for method *getOrderId()/setOrderId()*:**

- Setter input
- Getter output

**Predicates for method *getOrderId()/setOrderId()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | (-inf; +inf) |
| | Null |
| Getter output | (-inf; +inf) |
| | Null |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| n in (-inf; +inf) | n | Valid| T1(n, n; True) | testValidSetters() |
| n in (-inf; +inf) | m in (-inf, +inf), m != n | Valid | T2(n, m, False) | TestValidSetters() |
| n in (-inf; +inf) | Null | Valid | T3(n, Null; False) | testValidSetters() |
| Null | Null | Valid | T4(Null, Null; True) | testNullOrInvalidSetters() |
| Null | m in (-inf; +inf) | Valid | T5(Null, m; False) | testNullOrInvalidSetters() |

### **Class *OrderModel* - method *getProductCode()/setProductCode()***

**Criteria for method *getProductCode()/setProductCode()*:**

- Input setter
- Output getter

**Predicates for method *getProductCode()/setProductCode()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | Any possible String s |
| | Null |
| Getter output | String r = s |
| | Any possible String r != s |
| | Null


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| Any possible String s | String r = s | Valid| T1(s, r; True) |testValidSetters()|
| Any possible String s | Any possible String r, s != r | Valid | T2(s, r; False) |testValidSetters()|
| Any possible String s | Null | Valid | T3(s, Null; False) | testValidSetters() |
| Null | Null | Valid | T4(Null, Null; True) | testNullOrInvalidSetters()|
| Null | Any possible String r | Valid | T5(Null, r; False) | testNullOrInvalidSetters() |

### **Class *OrderModel* - method *getPricePerUnit()/setPricePerUnit()***

**Criteria for method *getPricePerUnit()/setPricePerUnit()*:**

- Input setter
- Output getter

**Predicates for method *getPricePerUnit()/setPricePerUnit()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | (-inf, +inf)) |
| Getter output | (-inf, +inf)) |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| n in (-inf, +inf) | n | Valid| T1(n, n; True) |testValidSetters()|
| n in (-inf, +inf) | m in (-inf, +inf), m != n | Valid| T1(n, ; True) |testValidSetters()|

### **Class *OrderModel* - method *getQuantity()/setQuantity()***

**Criteria for method *getQuantity()/setQuantity()*:**

- Input setter
- Output getter

**Predicates for method *getQuantity()/setQuantity()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | (-inf, +inf)) |
| Getter output | (-inf, +inf)) |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| n in (-inf, +inf) | n | Valid| T1(n, n; True) |testValidSetters()|
| n in (-inf, +inf) | m in (-inf, +inf), m != n | Valid| T1(n, ; True) |testValidSetters()|

### **Class *OrderModel* - method *getStatus()/setStatus()***

**Criteria for method *getStatus()/setStatus()*:**

- Setter input
- Getter output

**Predicates for method *getStatus()/setStatus()*:**

| Criteria | Predicate |
| -------- | --------- |
| Setter input | String s in {"ISSUED", "ORDERED", "PAYED", "COMPLETED"} |
| | String s NOT in {"ISSUED", "ORDERED", "PAYED", "COMPLETED"} |
| | Null |
| Getter output | String s in {"ISSUED", "ORDERED", "PAYED", "COMPLETED"}  |
| | Null |


**Combination of predicates**:


| Setter input | Getter output | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| String s in {"ISSUED", "ORDERED", "PAYED", "COMPLETED"} | s | Valid| T1(s, s.toUpperCase(); True) |testValidSetters()|
| String s in {"ISSUED", "ORDERED", "PAYED", "COMPLETED"} | Null | Valid | T2(s, Null; False) | testValidSetters() |
| String s in {"ISSUED", "ORDERED", "PAYED", "COMPLETED"} | String r in {"Cashier", "ShopManager", "Administrator" }, s != r | Valid | T3(s, Null; False) | testValidSetters() |
| String s NOT in {"ISSUED", "ORDERED", "PAYED", "COMPLETED"} | * | Invalid | T3(s, *; InvalidArgumentException()) |testNullOrInvalidSetters()|

### **Class *OrderModel* - method *generateRFID***

**Criteria for method *generateRFID*:**

- baseRFID
- Quantity

**Predicates for method *generateRFID*:**

| Criteria | Predicate |
| -------- | --------- |
| baseRFID | Any possible String s |
| | Null |
| quantity | Any possible integer |

**Combination of predicates**:

| baseRFID | Quantity | Valid / Invalid | Description | JUnit test case |
|-------|----------|-----------------|-------------|-----------------|
| A numerical string which length is 12 | Any integer >= 0 | Valid | T1(baseRFID, quantity; true) | testGenerateRFID() |
| A numerical string which length is 12 | Any integer < 0  | Valid | T2(baseRFID, quantity; true) | testGenerateRFID() |
| A numerical string which length is 12 but contains letters | Any integer < 0  | Valid | T3(baseRFID, quantity; false) | testGenerateRFID() |
| A numerical string which length is not 12 | Any integer  | Valid | T4(baseRFID, quantity; false) | testGenerateRFID() |
| Null | Any integer  | Valid | T5(baseRFID, quantity; false) | testGenerateRFID() |


### **Class *CreditCardModel* - method *loadCreditCardsFromFile***

**Criteria for method *loadCreditCardsFromFile*:**

- Pathname input
- List of CreditCardModel output

**Predicates for method *loadCreditCardsFromFile*:**

| Criteria | Predicate |
| -------- | --------- |
| Pathname | Existing file |
| | Any string which doesn't contain a reference to an existing file |
| | Null | 
| List of CreditCardModel | Contains all the credit cards which are contained into the file passed as parameter (Pathname) (maybe empty) |
| | Null |

**Combination of predicates**:

| Pathname | List of CreditCardModel | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| Valid Pathname | Correct list with all the Credit Cards from the input file (Pathname) | valid | T1(Pathname, List; true) | testLoadCreditCardsFromFile() |
| Valid Pathname | Null | valid | T2(Pathname, Null; false) | testLoadCreditCardsFromFile() |
| Not valid Pathname or Null | Null | valid | T3(Not valid Pathname or Null, Null; false) | testLoadCreditCardsFromFile() |
| Not valid Pathname or Null | Null | valid | T4(Not valid Pathname or Null, Null; false) | testLoadCreditCardsFromFile() |

### **Class *CreditCardModel* - method *executePayment***

**Criteria for method *executePayment*:**

- Pathname input
- Amount

**Predicates for method *executePayment*:**

| Criteria | Predicate |
| -------- | --------- |
| Pathname | Existing file |
| | Any string which doesn't contain a reference to an existing file |
| | Null | 
| Amount | \>= 0.0 |
| | < 0.0 |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
| Amount   | [0.0, +inf) |

**Combination of predicates**:

| Pathname | Amount | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| Valid Pathname | a in \[0.0, credit_card_balance\] | valid | T1(Pathname, a in \[0.0, credit_card_balance\]; true) | testLoadCreditCardsFromFile() |
| Valid Pathname | a in [credit_card_balance, +inf) | valid | T2(Pathname, a in [credit_card_balance, +inf); false) | testLoadCreditCardsFromFile() |
| * | a in (-inf, 0.0) | valid | T3(*, a in (-inf, 0.0); false) | testLoadCreditCardsFromFile() |
| Valid Pathname | Null | valid | T4(Valid Pathname, Null; false) | testLoadCreditCardsFromFile() |
| Not valid Pathname or Null | a in \[0.0, credit_card_balance\] | valid | T5(Not valid Pathname or Null, a in \[0.0, credit_card_balance\]; false) | testLoadCreditCardsFromFile() |
| Not valid Pathname or Null | a in [credit_card_balance, +inf) | valid | T6(Not valid Pathname or Null, a in [credit_card_balance, +inf); false) | testLoadCreditCardsFromFile() |
| Not valid Pathname or Null | Null | valid | T7(Not valid Pathname or Null, Null; false) | testLoadCreditCardsFromFile() |

### **Class *CreditCardModel* - method *luhnAlgorithm***

**Criteria for method *luhnAlgorithm*:**

- Credit Card Number input

**Predicates for method *luhnAlgorithm*:**

| Criteria | Predicate |
| -------- | --------- |
| Credit Card Number | Valid Credit Card Number |
| | Invalid Credit Card Number |
| | Null |


**Combination of predicates**:

| Credit Card Number |  Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
| Valid Credit Card Number | valid | T1(Valid Credit Card Number; true) | testLuhnAlgorithm() | 
| Invalid Credit Card Number | valid | T2(Invalid Credit Card Number; false) | testLuhnAlgorithm() |
| Null | valid | T3(Null; false) | testLuhnAlgorithm() | 

### **Class *DbConnection* - method *DBconnect***

**Criteria for method *DBconnect*:**

- Url
- Username
- Password
- DBname

**Predicates for method *DBconnect*:**

| Criteria | Predicate |
| -------- | --------- |
| Url | Any possible String s |
| | Null |
| Username | Any possible String s |
| | Null |
| Password | Any possible String s |
| | Null |
| DBname | Any possible String s |
| | Null |


**Combination of predicates**:

| Url | Username | Password | DBname | Valid / Invalid | Description | JUnit test case |
|-----|----------|----------|--------|-----------------|-------------|-----------------|
| null | Any string u | Any string p | Any string d | Invalid | T1(null,u,p,d;MissingDAOParameterException) | testDBconnect() |
| Any string u | null | Any string p | Any string d | Invalid | T2(u,null,p,d;MissingDAOParameterException) | testDBconnect() |
| Any string ur | Any string us | null | Any string d | Invalid | T3(ur,us,null,d;MissingDAOParameterException) | testDBconnect() |
| Any string ur | Any string us | Any string p | null | Invalid | T4(ur,us,p,null;MissingDAOParameterException) | testDBconnect() |
| null | Any string u | Any string p | Any string d | Invalid | T5(u,p,d;MissingDAOParameterException) | testDBconnect() |
| Any string ur != "jdbc:mysql://localhost/" | Any string us | Any string p | Any string d | valid | T6(ur,us,p,d;false) | testDBconnect() |
| ur = "jdbc:mysql://localhost/" | Any string us != "root" | Any string p | Any string d | valid | T7(ur,us,p,d;false) | testDBconnect() |
| ur = "jdbc:mysql://localhost/" | us = "root" | Any string p != "EZShop2021" | Any string d | valid | T8(ur,us,p,d;false) | testDBconnect() |
| ur = "jdbc:mysql://localhost/" | us = "root" | p = "EZShop2021" | Any string d != "ezshopdb" | valid | T9(ur,us,p,d;false) | testDBconnect() |
| ur = "jdbc:mysql://localhost/" | us = "root" | p = "EZShop2021" | d = "ezshopdb" | valid | T10(ur,us,p,d;true) | testDBconnect() |

### **Class *DbConnection* - method *executeQuery***

**Criteria for method *executeQuery*:**

- Query
- connection

**Predicates for method *executeQuery*:**

| Criteria | Predicate |
| -------- | --------- |
| Query | Any possible String s |
| | Null |
| connection | Any Connection instance |
| | Null |

**Combination of predicates**:

| Query | connection | Valid / Invalid | Description | JUnit test case |
|-------|----------|-----------------|-------------|-----------------|
| null | Any DriverManager i | Valid | T1(null,i;null) | testExecuteQuery() |
| q = "SELECT * FROM user" | null | Valid | T2(q,null;null) | testExecuteQuery() |
| q = "SELECT * FROM user" | Any Connection i | Valid | T3(q,i;ArrayList<String>) | testExecuteQuery() |
| q = "SELECT * FROM testtable" | Any Connection i | Valid | T4(q,i;null) | testExecuteQuery() |

### **Class *DbConnection* - method *executeUpdate***

**Criteria for method *executeUpdate*:**

- Query
- connection

**Predicates for method *executeUpdate*:**

| Criteria | Predicate |
| -------- | --------- |
| Query | Any possible String s |
| | Null |
| connection | Any Connection instance |
| | Null |

**Combination of predicates**:

| connection | Query | Valid / Invalid | Description | JUnit test case |
|-------|----------|-----------------|-------------|-----------------|
| Any Connection i | q = "UPDATE user SET testfield='testuser' WHERE id=1;" | Valid | T1(i,q;false) | testExecuteUpdate() |
| Any Connection i | q = "UPDATE user SET username='testuser' WHERE id=1;" | Valid | T2(i,q;true) | testExecuteUpdate() |

### **Class *DbConnection* - method *DBdisconnect***

**Criteria for method *DBdisconnect*:**

- Query
- connection

**Predicates for method *DBdisconnect*:**

| Criteria | Predicate |
| -------- | --------- |
| connection | Any Connection instance |
| | Null |

**Combination of predicates**:

| connection | Valid / Invalid | Description | JUnit test case |
|------------|-----------------|-------------|-----------------|
| null | Valid | T1(null;null) | testDBdisconnect() |
| Any Connection i | Valid | T2(i;null) | testDBdisconnect() |

### **Class *DbConnection* - method *reset***

**Criteria for method *reset*:**

- instance
- connection
- newInstance
- newConnection

**Predicates for method *reset*:**

| Criteria | Predicate |
| -------- | --------- |
| instance | Any Connection instance |
| connection | Any Connection instance |
| newInstance | Any Connection instance |
| newConnection | Any Connection instance |

**Combination of predicates**:

| instance | connection | newInstance | newConnection | Valid / Invalid | Description | JUnit test case |
|----------|------------|-------------|---------------|-----------------|-------------|-----------------|
| Any DbConnection i | Any Connection c | Any DbConnection ni | Any Connection nc | Valid | T1(i, c, ni, nc;true,true) | testReset() |



# White Box Unit Tests

### Test cases definition
    
    <JUnit test classes must be in src/test/java/it/polito/ezshop>
    <Report here all the created JUnit test cases, and the units/classes under test >
    <For traceability write the class and method name that contains the test case>


| Unit name | JUnit test case |
| --- | --- |
| hasEnoughMoney | testClass: TestCreditCardModel - method: testHasEnoughMoney |
| generateValidCreditCardNumber | testClass: TestCreditCardModel - method: testGenerateValidCreditCardNumber |
| loadCreditCardsFromFile | testClass: TestCreditCardModel - method: testLoadCreditCardsFromFile |
| executePayment | testClass: TestCreditCardModel - method: testExecutePayment |
| luhnAlgorithm | testClass: TestCreditCardModel - method: testLuhnAlgorithm |
| GTIN13Check | testClass: TestProductTypeModel - method: testGTIN13Check |
| checkSerialNumberFormat | testClass: UnitTestLoyaltyCardModel - method: testCheckSerialNumberFormat | 
| getInstance | testClass: TestDbConnection - method: testGetInstance |
| DBconnect | testClass: TestDbConnection - method : testDBconnect |
| executeQuery | testClass: TestDbConnection - method : testExecuteQuery |
| executeUpdate | testClass: TestDbConnection - method : testExecuteUpdate |
| DBdisconnect | testClass: TestDbConnection - method : testDBdisconnect |
| DBreset | testClass: TestDbConnection - method : testReset |

### Code coverage report

    <Add here the screenshot report of the statement and branch coverage obtained using
    the Eclemma tool. >

<img src='https://i.ibb.co/k9Pv7LB/coverage.jpg'>

### Loop coverage analysis

    <Identify significant loops in the units and reports the test cases
    developed to cover zero, one or multiple iterations >

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --- | --- | --- | --- |
| class: CreditCardModel - method: generateValidCreditCardNumber | from line 92 to line 99 | 16 | testGenerateValidCreditCardNumber |
| class: CreditCardModel - method: generateValidCreditCardNumber | from line 103 to line 104 | 16 | testGenerateValidCreditCardNumber |
| class: CreditCardModel - method: loadCreditCardsFromFile | from line 124 to line 135 | It depends on the amount of lines contained in the file | testLoadCreditCardsFromFile |
| class: CreditCardModel - method: executePayment | from line 165 to line 166 | It depends on the amount of lines contained in the file | testExecutePayment |
| class: CreditCardModel - method: executePayment | from line 169 to line 175 | It depends on the amount of lines contained in the file | testExecutePayment |
| class: CreditCardModel - method: luhnAlgorithm | from line 210 to line 214 | 15 | testLuhnAlgorithm |
| class: CreditCardModel - method: luhnAlgorithm | from line 216 to line 218 | 15 | testLuhnAlgorithm |
| class: CreditCardModel - method: luhnAlgorithm | from line 219 to line 228 | best case: 15, worst case: 30 | testLuhnAlgorithm |
| class: ProductTypeModel - method: GTIN13Check | from line 647 to line 651 | 11 or 12 or 13 | testGTIN13Check |
| class: ProductTypeModel - method: GTIN13Check | from line 653 to line 657 | 10 or 11 or 12 | testGTIN13Check |
| class: ProductTypeModel - method: GTIN13Check | from line 659 to line 660 | 11 or 12 or 13 | testGTIN13Check |
| class: LoyaltyCardModel - method: checkSerialNumberFormat | from line 37 to line 39 | best case: 1, worst case: 10 | testCheckSerialNumberFormat
| class: DbConnection - method: executeQuery | from line 57 to line 61 | best case: 0, worst case: entries of table * colums of table | testExecuteQuery |
