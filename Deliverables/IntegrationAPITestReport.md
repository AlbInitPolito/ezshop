# Integration and API Test Documentation

Authors: Alberto Castrignanò, Francesca Silvano, Matteo Favretto, Vincenzo Sagristano

Date: 01/06/2021

Version: 1.0

# Contents

- [Dependency graph](#dependency graph)

- [Integration approach](#integration)

- [Tests](#tests)

- [Scenarios](#scenarios)

- [Coverage of scenarios and FR](#scenario-coverage)
- [Coverage of non-functional requirements](#nfr-coverage)



# Dependency graph 

     <report the here the dependency graph of the classes in EzShop, using plantuml>
    @startuml
    class BalanceOperationDAO
    class CustomerDAO
    class DbConnection
    class LoyaltyCardDAO
    class mysqlDAO
    class OrderDAO
    class ProductTypeDAO
    class ReturnProductDAO
    class ReturnTransactionDAO
    class SaleTransactionDAO
    class TicketEntryDAO
    class UserDAO
    class AccountBookModel
    class BalanceOperationModel
    class CreditCardModel
    class CustomerModel
    class LoyaltyCardModel
    class OrderModel
    class ProductTypeModel
    class ReturnTransactionModel
    class SaleTransactionModel
    class TicketEntryModel
    class UserModel
    class EZShop
    
    mysqlDAO -up- DbConnection
    
    UserDAO -up- mysqlDAO
    BalanceOperationDAO -up- mysqlDAO
    CustomerDAO -up- mysqlDAO
    SaleTransactionDAO -up- mysqlDAO
    ReturnTransactionDAO -up- mysqlDAO
    LoyaltyCardDAO -up- mysqlDAO
    OrderDAO -up- mysqlDAO
    ProductTypeDAO -up- mysqlDAO
    ReturnProductDAO -up- mysqlDAO
    TicketEntryDAO -up- mysqlDAO
    
    LoyaltyCardModel -up- CustomerDAO
    LoyaltyCardModel -up- LoyaltyCardDAO
    LoyaltyCardModel -up- CustomerModel
    
    OrderModel -up- BalanceOperationDAO
    OrderModel -up- OrderDAO
    OrderModel -up- ProductTypeDAO
    
    ProductTypeModel -up- ProductTypeDAO
    
    ReturnTransactionModel -up- DbConnection
    ReturnTransactionModel -up- SaleTransactionDAO
    ReturnTransactionModel -up- BalanceOperationDAO
    ReturnTransactionModel -up- ReturnTransactionDAO
    ReturnTransactionModel -up- SaleTransactionModel
    ReturnTransactionModel -up- ProductTypeModel
    ReturnTransactionModel -up- TicketEntryModel
    ReturnTransactionModel -up- TicketEntryDAO
    ReturnTransactionModel -up- CreditCardModel
    
    SaleTransactionModel -up- BalanceOperationDAO
    SaleTransactionModel -up- ProductTypeDAO
    SaleTransactionModel -up- SaleTransactionDAO
    SaleTransactionModel -up- TicketEntryDAO
    SaleTransactionModel -up- CreditCardModel
    SaleTransactionModel -up- ProductTypeModel
    
    UserModel -up- UserDAO
    
    AccountBookModel -down- EZShop
    BalanceOperationModel -down- EZShop
    CreditCardModel -down- EZShop
    CustomerModel -down- EZShop
    LoyaltyCardModel -down- EZShop
    OrderModel -down- EZShop
    ProductTypeModel -down- EZShop
    ReturnTransactionModel -down- EZShop
    SaleTransactionModel -down- EZShop
    TicketEntryModel -down- EZShop
    UserModel -down- EZShop
    
    TicketEntryDAO -- ProductTypeDAO
    ReturnProductDAO -- ProductTypeDAO
    OrderDAO -- ProductTypeDAO
    SaleTransactionDAO -- TicketEntryDAO
    SaleTransactionDAO -- BalanceOperationDAO
    @enduml

<img src='http://www.plantuml.com/plantuml/svg/ZPH1RiCW54JtdC8Ny1sIEDcrIjMacsug2gY9Wi75M9LlNqKc-dZyYxlCp7F47-QpwQCCSMXjfKrdd3PEZHzVGNON8PILVL_jf9LEcMDdWenNx_RROtAQeO--DO6K_MVZdJFgpYNfoO_Inh6HGHFNZlstfP_7eFD_UGbU3ogUnywG-6hY45ooK3u7wNgfviSxIMjOutnLDnCFBeQHnBS-Fy9MAJ-ukFF-zkoriNzaaNjDCFeQxtEIIXab3ICXEQMG1Co1P6PaCcbei63iIQR1yz-7zzF5Tq8yxgYgXwwUtwoOmfeikanCPqbGOz21Soy1d9f1vbK3CtKCvNdNmCJEWJtl4PW2RtdoimHu0XU6XQ1Mqt1aSpkB32qT6h1-UKG5YcybKvuIk6n14SSnrtZqLZzWjwdq0MPLWc4X_m-ldHfV8bokSe8yZTELGN00Bam6O74k84lNCBrO6V1uxGK-z5Mj_O-h7wyWviCnS2nqQLFHmzr6cnOJ31mFRBOR265u30Bz19iY1NtnBc9ny5r5PFvShzKkGTWY5kBgBZR6wQ6rlm00'>
     
# Integration approach

    <Write here the integration sequence you adopted, in general terms (top down, bottom up, mixed) and as sequence
    (ex: step1: class A, step 2: class A+B, step 3: class A+B+C, etc)> 
    <Some steps may  correspond to unit testing (ex step1 in ex above), presented in other document UnitTestReport.md>
    <One step will  correspond to API testing>
    
    Incremental bottom-up approach
    step1:
        CreditCardModel
        DbConnection
        ProductTypeModel
        UserModel
        CustomerModel
        LoyaltyCardModel
        TicketEntryModel
        BalanceOperationModel
        OrderModel
        SaleTransactionModel
        ReturnTransactionModel
    step2:
        DbConnection + BalanceOperationDAO
        DbConnection + CustomerDAO
        DbConnection + LoyaltyCardDAO
        DbConnection + mysqlDAO
        DbConnection + OrderDAO
        DbConnection + ProductTypeDAO
        DbConnection + ReturnProductDAO
        DbConnection + ReturnTransactionDAO
        DbConnection + SaleTransactionDAO
        DbConnection + TicketEntryDAO
        DbConnection + UserDAO
    step3:
        DbConnection + BalanceOperationModel + BalanceOperationDAO
        DbConnection + CustomerModel + CustomerDAO 
        DbConnection + LoyaltyCardModel + LoyaltyCardDAO
        DbConnection + mysqlDAO
        DbConnection + OrderModel + OrderDAO
        DbConnection + ProductTypeModel + ProductTypeDAO
        DbConnection + ReturnProductDAO
        DbConnection + ReturnTransactionModel + ReturnTransactionDAO
        DbConnection + SaleTransactionModel + SaleTransactionDAO
        DbConnection + TicketEntryDAO
        DbConnection + UserModel + UserDAO
    step4:
        DbConnection + OrderModel + OrderDAO + ProductTypeModel + ProductTypeDAO
        DbConnection + ReturnProductDAO + ProductTypeModel + ProductTypeDAO
        DbConnection + ReturnTransactionDAO
        DbConnection + SaleTransactionDAO
        DbConnection + TicketEntryDAO + ProductTypeModel + ProductTypeDAO
        DbConnection + BalanceOperationDAO + BalanceOperationModel + AccountBookModel (Driver)
        DbConnection + CustomerModel + CustomerDAO + LoyaltyCardModel
        DbConnection + LoyaltyCardDAO + LoyaltyCardModel + CustomerDAO + CustomerModel
        DbConnection + UserModel (Driver) + UserDAO
        DBConnection + SaleTransaction Drivers + SaleTransactionDAO + ProductTypeModel + ProductTypeDAO + TicketEntryModel
    step5:
        DbConnection + OrderModel + OrderDAO + ProductTypeModel + ProductTypeDAO
        DbConnection + ReturnProductDAO + ProductTypeModel + ProductTypeDAO + TicketEntryModel
        DbConnection + ReturnTransactionModel + ReturnTransactionDAO
        DbConnection + SaleTransactionModel + SaleTransactionDAO + BalanceOperationModel + BalanceOperationDAO
        DbConnection + TicketEntryModel + TicketEntryDAO + ProductTypeModel + ProductTypeDAO
        DbConnection + AccountBookModel + BalanceOperationDAO + BalanceOperationModel
        DbConnection + CustomerDAO + CustomerModel + LoyaltyCardModel
        DbConnection + LoyaltyCardDAO + LoyaltyCardModel + CustomerDAO + CustomerModel
        DbConnection + UserDAO + UserModel
        DBConnection + SaleTransactionModel + SaleTransactionDAO + ProductTypeModel + ProductTypeDAO + TicketEntryModel
    step6:
        DbConnection + TicketEntryModel + ReturnProductDAO + ProductTypeModel + ProductTypeDAO
        DbConnection + SaleTransactionDAO + TicketEntryModel + TicketEntryDAO
        DbConnection + TicketEntryModel + TicketEntryDAO + ProductTypeModel + ProductTypeDAO
    step7:
        DbConnection + TicketEntryModel + ReturnProductDAO + ProductTypeModel + ProductTypeDAO
        DbConnection + SaleTransactionModel + SaleTransactionDAO + TicketEntryModel + TicketEntryDAO
        DbConnection + TicketEntryModel + TicketEntryDAO + ProductTypeModel + ProductTypeDAO
    step8:
        DBConnection + SaleTransactionModel Driver + SaleTransactionDAO + ProductTypeModel + ProductTypeDAO + TicketEntryModel+TicketEntryDAO
        DBConnection + ReturnTransactionModel Driver + ReturnTransactionDAO + SaleTransactionModel + SaleTransactionDAO + ProductTypeModel +    ProductTypeDAO + TicketEntryModel + TicketEntryDAO + ReturnProductDAO

    step9:
        DBConnection + SaleTransactionModel + SaleTransactionDAO + ProductTypeModel + ProductTypeDAO + TicketEntryModel + TicketEntryDAO + BalanceOperationModel +BalanceOperationDAO
        DBConnection + ReturnTransactionModel + ReturnTransactionDAO + SaleTransactionModel + SaleTransactionDAO + ProductTypeModel + ProductTypeDAO + TicketEntryModel + TicketEntryDAO +ReturnProductDAO + BalanceOperationModel + BalanceOperationDAO
    step10:
        DBConnection + ReturnTransactionModel + ReturnTransactionDAO + SaleTransactionModel + SaleTransactionDAO + ProductTypeModel + ProductTypeDAO + TicketEntryModel + TicketEntryDAO +ReturnProductDAO + BalanceOperationModel + BalanceOperationDAO + CustomerModel + CustomerDAO + LoyaltyCardModel + LoyaltyCardDAO + OrderModel + OrderDAO + UserModel + UserDAO + AccountBookModel + CreditCardModel + mysqlDAO
                
    step11:
        DBConnection + ReturnTransactionModel + ReturnTransactionDAO + SaleTransactionModel + SaleTransactionDAO + ProductTypeModel + ProductTypeDAO + TicketEntryModel + TicketEntryDAO +ReturnProductDAO + BalanceOperationModel + BalanceOperationDAO + CustomerModel + CustomerDAO + LoyaltyCardModel + LoyaltyCardDAO + OrderModel + OrderDAO + UserModel + userDAO + AccountBookModel + CreditCardModel + mysqlDAO
#  Tests

   <define below a table for each integration step. For each integration step report the group of classes under test, and the names of
     JUnit test cases applied to them> JUnit test classes should be here src/test/java/it/polito/ezshop

## Step 1
| Classes  | JUnit test cases |
|---|---|
|CreditCardModel|unitTests.TestCreditCardModel|
|DbConnection|unitTests.TestDbConnection|
|ProductTypeModel|unitTests.TestProductTypeModel|
|AccountBookModel|unitTests.UnitTestGettersAndSetters|
|BalanceOperationModel|unitTests.UnitTestGettersAndSetters|
|CreditCardModel|unitTests.UnitTestGettersAndSetters|
|CustomerModel|unitTests.UnitTestGettersAndSetters|
|LoyaltyCardModel|unitTests.UnitTestGettersAndSetters|
|OrderModel|unitTests.UnitTestGettersAndSetters|
|OrderModel|unitTests.TestOrderModel|
|TicketEntryModel|unitTests.TicketEntryModelUnitTest|
|ProductTypeModel|unitTests.UnitTestGettersAndSetters|
|ReturnTransactionModel|unitTests.UnitTestGettersAndSetters|
|SaleTransactionModel|unitTests.UnitTestGettersAndSetters|
|TicketEntryModel|unitTests.UnitTestGettersAndSetters|
|UserModel|unitTests.UnitTestGettersAndSetters|
|LoyaltyCardModel|unitTests.UnitTestLoyaltyCardModel|


## Step 2
| Classes  | JUnit test cases |
|---|---|
|BalanceOperationDAO|Step2.testBalanceOperationDAO|
|CustomerDAO|Step2.testCustomerDAO|
|LoyaltyCardDAO|Step2.testLoyaltyCardDAO|
|mysqlDAO|Step2.testMysqlDAO|
|OrderDAO|Step2.testOrderDAO|
|ProductTypeDAO|Step2.testProductTypeDAO|
|ReturnProductDAO|Step2.testReturnProductDAO|
|ReturnTransactionDAO|Step2.testReturnTransactionDAO|
|SaleTransactionDAO|Step2.testSaleTransactionDAO|
|TicketEntryDAO|Step2.testTicketEntryDAOD|
|UserDAO|Step2.testUserDAO|
|TicketEntryRfidDAO|testTicketEntryRfidDAO|
|ReturnProductRfidDAO|testReturnProductRfidDAO|
|ProductEntryDAO|testProductEntryDAO|

## Step 3
| Classes  | JUnit test cases |
|---|---|
|BalanceOperationDAO|Step3.testBalanceOperationDAO|
|CustomerDAO|Step3.testCustomerDAO|
|LoyaltyCardDAO|Step3.testLoyaltyCardDAO|
|mysqlDAO|Step3.testMysqlDAO|
|OrderDAO|Step3.testOrderDAO|
|ProductTypeDAO|Step3.testProductTypeDAO|
|ReturnProductDAO|Step3.testReturnProductDAO|
|ReturnTransactionDAO|Step3.testReturnTransactionDAO|
|SaleTransactionDAO|Step3.testSaleTransactionDAO|
|TicketEntryDAO|Step3.testTicketEntryDAO|
|userDAO|Step3.testUserDAO|
|TicketEntryRfidDAO|testTicketEntryRfidDAO|
|ReturnProductRfidDAO|testReturnProductRfidDAO|
|ProductEntryDAO|testProductEntryDAO|

## Step 4
| Classes  | JUnit test cases |
|---|---|
|OrderDAO|testOrderDAO|
|ReturnProductDAO|testReturnProductDAO|
|ReturnTransactionDAO|testReturnTransactionDAO|
|SaleTransactionDAO|testSaleTransactionDAO|
|TicketEntryDAO|testTicketEntryDAO|
|LoyaltyCardModel|LoyaltyCardModelDriversStep4|
|ProductTypeModel|ProductTypeModelDrivers|
|UserModel|UserModelDriversStep4|
|AccountBookModel|AccountBookModelDriversStep4|
|CustomerModel|CustomerModelDriversStep4|
|SaleTransactionModel|SaleTransactionDriversStep4|

## Step 5
| Classes  | JUnit test cases |
|---|---|
|AccountBookModel|TestAccountBookModelStep5|
|CustomerModel|TestCustomerModelStep5|
|LoyaltyCardModel|TestLoyaltyCardModelStep5|
|OrderDAO|testOrderDAO|
|ProductTypeModel|TestProductTypeModel|
|ReturnProductDAO|testReturnProductDAO|
|ReturnTransactionDAO|testReturnTransactionDAO|
|SaleTransactionDAO|testSaleTransactionDAO|
|SaleTransactionModel|TestStartSaleTransactionModel|
|TicketEntryDAO|testTicketEntryDAO|
|UserModel|TestUserModelStep5|

## Step 6
| Classes  | JUnit test cases |
|---|---|
|ReturnProductDAO|testReturnProductDAO|
|SaleTransactionDAO|testSaleTransactionDAO|
|TicketEntryDAO|testTicketEntryDAO|
|OrderModel|OrderModelDrivers|
|ProductEntryDAO|testProductEntryDAO|

## Step 7
| Classes  | JUnit test cases |
|---|---|
|TicketEntryDAO|testProductEntryDAO|
|ReturnProductDAO|testReturnProductDAO|
|SaleTransactionDAO|testSaleTransactionDAO|
|TicketEntryDAO|testTicketEntryDAO|
|OrderModel|TestOrderModel|

## Step 8
| Classes  | JUnit test cases |
|---|---|
|ReturnTransactionModel|ReturnTransactionDriversStep8|
|SaleTransactionModel|SaleTransactionDrivers|
|SaleTransactionModel|SaleTransactionRFIDDrivers|

## Step 9
| Classes  | JUnit test cases |
|---|---|
|ReturnTransactionModel|testReturnTransactionModel|
|SaleTransactionModel|TestSaleTransactionModel|
|SaleTransactionModel|TestSaleTransactionRFID|

## Step 10
| Classes  | JUnit test cases |
|---|---|
|EZShop|EZShopDrivers|


## Step 11
| Classes  | JUnit test cases |
|---|---|
|EZShop|EZShopAPITesting|

# Scenarios


<If needed, define here additional scenarios for the application. Scenarios should be named
 referring the UC in the OfficialRequirements that they detail>

## Scenario UCx.y

| Scenario |  name |
| ------------- |:-------------:| 
|  Precondition     |  |
|  Post condition     |   |
| Step#        | Description  |
|  1     |  ... |  
|  2     |  ... |



# Coverage of Scenarios and FR


<Report in the following table the coverage of  scenarios (from official requirements and from above) vs FR. 
Report also for each of the scenarios the (one or more) API JUnit tests that cover it. >




| Scenario ID | Functional Requirements covered | JUnit  Test(s) | 
| ----------- | ------------------------------- | ----------- | 
|1.1|Create product type X|class: TestProductTypeModel - method: testCreateProductTypeModel|
|1.2|Modify product type location|class: TestProductTypeModel - method: testUpdatePosition|
|1.3|Modify product type price per unit|class: TestProductTypeModel - method: testUpdateProductTypeModel|
|2.1|Create user and define rights|class: TestUserModelStep5 - method: testCreateUser|
|2.2|Delete user|class: TestUserModelStep5 - method: testDeleteUser|
|2.3|Modify user rights|class: TestUserModelStep5 - method: testUpdateUserRights|
|3.1|Order of product type X issued|class: TestOrderModel - method: testIssueOrder|
|3.2|Order of product type X payed|class: TestOrderModel - method: testPayOrder/testPayOrderFor|
|3.3|Record order of product type X arrival|class: TestOrderModel - method: testRecordOrderArrival|
|4.1|Create customer record|class: TestCustomerModelStep5 - method: testDefineCustomer|
|4.2|Attach Loyalty Card to customer record|class: TestLoyaltyCardModelStep5 - method: testAttachCardToCustomer|
|4.3|Detach loyalty card from customer record|class: TestCustomerModelStep5 - method: testModifyCustomer|
|4.4|Update customer record|class: TestCustomerModelStep5 - method: testModifyCustomer|
|5.1|Login|class: testUserModelStep5 - method: testLogin|
|5.2|Logout|class: EZShopAPITesting - method: testLogout|
|6.1|Sale of product type X completed|class: step9.TestSaleTransactionModel - method: testEndSaleTransaction|
|6.2|Sale of product type X with product discount|class: step5.TestStartSaleTransactionModel - method: testApplyDiscountRateToProduct|
|6.3|Sale of product type X with sale discount|class: step9.TestSaleTransactionModel - method: testApplyDiscountToSale|
|6.4|Sale of product type X with Loyalty Card update|class: step5.TestLoyaltyCardModel - method: testModifyPointsOnCard / class: step9.TestSaleTransactionModel - method: testComputePointsForSale|
|6.5|Sale of product type X canceled|class: step5.TestStartSaleTransactionModel - method: testDeleteProductFromSale|
|6.6|Sale of product type X completed (Cash)|class: step9.TestSaleTransactionModel - method: testEndSaleTransaction/testReturnCashPayment|
|7.1|Manage payment by valid credit card|class: step9.TestSaleTransactionModel - method: testEndSaleTransaction/testReturnCreditCardPayment|
|7.2|Manage payment by invalid credit card|class: step9.TestSaleTransactionModel - method: testEndSaleTransaction/testReturnCreditCardPayment|
|7.3|Manage credit card payment with not enough credit|class: step9.TestSaleTransactionModel - method: testEndSaleTransaction/testReturnCreditCardPayment|
|7.4|Manage cash payment|class: step9.TestSaleTransactionModel - method: testEndSaleTransaction/testReturnCashPayment|
|8.1|Return transaction of product type X completed, credit card|class: step9.TestSaleTransactionModel - method: testEndSaleTransaction/testReturnCreditCardPayment|
|8.2|Return transaction of product type completed, cash|step9.TestSaleTransactionModel - method: testEndSaleTransaction/testReturnCreditCardPayment|
|9.1|List credits and debits|class: TestAccountBookModelStep5 - method: testGetCreditsAndDebits|
|10.1|Return payment by credit card|class: step9.TestSaleTransactionModel - method: testReturnCreditCardPayment|
|10.2|Return cash payment|class: step9.TestSaleTransactionModel - method: testEndSaleTransaction/testReturnCashPayment|


# Coverage of Non Functional Requirements


<Report in the following table the coverage of the Non Functional Requirements of the application - only those that can be tested with automated testing frameworks.>


### 

| Non Functional Requirement | Test name |
| -------------------------- | --------- |
| NFR2                       | testNFR2  |
| NFR4                       | testGTIN13Check |
| NFR5                       | testLuhnAlgorithm |
| NFR6                       | testCheckSerialNumberFormat |


