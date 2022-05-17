# Design Document 


Authors: Matteo Favretto, Alberto Castrignanò, Vincenzo Sagristano, Francesca Silvano

Date: 30/04/2021

Version: 1.0


# Contents

- [High level design](#package-diagram)
- [Low level design](#class-diagram)
- [Verification traceability matrix](#verification-traceability-matrix)
- [Verification sequence diagrams](#verification-sequence-diagrams)

# Instructions

The design must satisfy the Official Requirements document, notably functional and non functional requirements

# High level design 

<img src='http://www.plantuml.com/plantuml/svg/JP1HQiGW54N_NSNRmEnX92F3qAO992sbVw_wg5AheY-KKhhtQiA4-2FttdCGF1V6pCkNXr5s3_Ag9X4Y4s21TcFMLa0zwcsoCO4hW52LO01z3BHkc99t6jd50D_MQGjDSqoQbxmXrNl7GWOgmfQWV-d0um_bMYLafXm41RE-AKH2_OaV1DVd6_p-xR4VBkhnM5paB8zPlNPgd6_3qzJQPfzEcoKQMjC1kUzjsUOTFbCmzKl-0G00'>



# Low level design

###### Model package
```
@startuml
note as persistentclasses
    Persistent classes
    ---
    User
    Customer
    ProductType
    LoyaltyCard
    AccountBook
    Order
    SaleTransaction
    BalanceOperation
    ReturnTransaction
end note

class User {
  Username
  Password
  Role
  ID
  createUser()
  getUser()
  getAllUsers()
  deleteUser()
  login()
  updateUserRights()
}

class AccountBook {
    Balance
    recordBalanceUpdate()
    getCreditsAndDebits()
    computeBalance()
}

class BalanceOperation {
    Description
    Amount
    Date
    Type
}

class Order {
    Supplier
    pricePerUnit
    Quantity
    Status
    issueOrder()
    payOrderFor()
    recordOrderArrival()
    recordOrderArrivalRFID()
    payOrder()
    generateRFID()
}

class ReturnTransaction {
    ID 
    transactionID
    returnedValue
    entries

    startReturnTransaction()
    endReturnTransaction()
    deleteReturnTransaction()
    returnProductRFID()
    returnProduct()
    returnCashPayment()
    returnCreditCardPayment()
}

class ProductType {
    ID
    Description
    sellPrice
    Quantity
    discountRate
    Notes
    aisleID
    rackID
    levelID
    createProductType()
    deleteProductType()
    getAllProductTypes()
    getProductTypeByBarCode()
    getProductTypesByDescription()
    GTIN13Check()
    updateProduct()
    updateQuantity()
    updatePosition()
}

class TicketEntry {
    barCode
    productDescription
    amount
    discountRate
    pricePerUnit
    RFIDs
}

class Customer {
    customerName
    customerID
    defineCustomer()
    getCustomer()
    getAllCustomers()
    deleteCustomer()
    modifyCustomer()
}

class CreditCard {
    creditCardNumber
    creditCardBalance
    luhnAlgorithm()
}

class LoyaltyCard {
    serialNumber
    Points
    checkSerialNumberFormat()
    generateSerialNumber()
    createCard()
    attachCardToCustomer()
    modifyPointsOnCard()
}

class SaleTransaction {
    ID
    Date
    Time
    Cost
    paymentType
    discountRate
    addProductToSale()
    deleteProductFromSale()
    applyDiscountRateToSale()
    applyDiscountRateToProduct()
    receiveCashPayment()
    receiveCreditCardPayment()
    computePointsForSale()
}

AccountBook -right- "*" BalanceOperation
Order -right-|> BalanceOperation
Order "*" -left- "*" ProductType
ProductType -down- "*" TicketEntry
TicketEntry"*" -down- SaleTransaction
SaleTransaction -right- "0..1" LoyaltyCard
LoyaltyCard -- "0..1" Customer
SaleTransaction -down- "0..1" CreditCard
SaleTransaction -up- "*" ReturnTransaction
ProductType -left- "*" ReturnTransaction
ReturnTransaction -up-|> BalanceOperation
SaleTransaction -up-|> BalanceOperation
@enduml
```
<img src='http://www.plantuml.com/plantuml/svg/TLRDRYCt3BxxAGOvjGMywABtOlrJ5mQAn7MStXM9YOLef874oMBGzjtNuj0oCvha49CVAPBwH4hpBQ6AsBTkuGD2erBJGKms8NZKJgK4QT5S_Wu5RIIyN2xfzob1967J9mmjAuSOJA_nD7H0-bzXK0w7ZOg6z9NMeVUu3k6Dz8TeUEMZSd2AoYUbqGPFs5evvJKyT11L0O-0VVJI5RnfybuM2ogKIcl-NOmrUjNcKWuNm_T0LHo3oyX-U_cd8oY4xFVJpnVr5L3AA-UocaWtu42ukl1gFKbzPpZ6qRwUCNl_pwN8_L95PKyaHz0X6aQUA0n5fEIR2CPYMdcpXUUBm1OTsgv7u4KYLyKM9zn2qj5sXRzLcoiQJGg113gl4eWEXLS_zbtdB9zH5ws60yGdRyS0V_VAeyLXT4M5VIBHfjG3XU6gEpMGkWkCyDu9NCLetvNxr73SxRUJE8KcdpSBx55sKFK8xsQ_RUWNRvRzbhFc5M3-KQu7Gi1Zj90MfD3CL56v2l3c4nCtpQor9EMPuJrK-0TiezBve8OM_0IdPibZnbR9XXZAmiDiQoHmxf3Fk3vSOnDryl7QClS1-KPGDZcuqgZq6uiEti6nF8wPg4FIK-CyUm9FDuD0ryDQnKqmC6jDwq5ia5t-FEtll_wsEOD-OsGSNq5smQu4V7GCoNAyGl39wZV0Fpp6WGb-7klYgQ7GKx9LcSEAt7hGSdEaah3SkPnDitglMlW0CFq6Ngo7ww8RMH8fh5_19CzekhWDnhuC0hoLLZhnMbm1xlls6U84bFUXwyz-vLv3j7XkPL1-HaJK1D4g9o8UWlMOnk3vT1-5mox4Lk7apf0ER1hRD6TXG24gVSx0ASmoC6PzyBoc53nvpUJOoMlNydbjGa8IEffUQj_vpb36N5iyv2HpWxIBeHKsrNLks8f0OkMiTNhnQB3lKDqynL1VFVAX6XdQXKWvYIBvA2vZVZENpTqlT_LpFpv4xFBVxvyuqDgbWnSE8xz1XDmiJVZkoKKEx4B858Yygi-HYNwh-zSlNxxUoGPTo6PTNZtAoDQHk2nsAspMZdr7rTUlczocPA9sb4Y9EajjdNtUxnjuqxVk1m00'>

###### Data package

<img src='http://www.plantuml.com/plantuml/svg/VPJDRjim3CVlVeeSjqiVOMZYB41FoLBtiXidCOa0MJH8ge0nxDqNEmmgPUvyCdz_Vlpr0JsB0clkusgxxjQDYo2o-lxpzKB3wdUpkdulIHIImpbeCpCcVHFaCdw5Y1r34d0QACtI6VLU-kLhywTfGbBa4pYq_Y_tkBaDOXJKQ-hqxnX1SHfZm6F44apjrp7Me8ZouAt1CPmlAeOZdKFw-ATS3pmm-Uoq6mUi-XWllDIvfQM2ouTGqCsu0Mx9uw8gct6BuZWCqqvMBdvaI1fqhAsHX28pY6JSiqSsCC0uXpjw91OoEcA_Frx9cZcyGwoMCwUA8O-daB3DejGZ6-p9XzDOGzkZ0bh7cjYC6jfPjC3U0AY2kqoWeyNX1mf9PP-A6b7Wntjf4dX_tt2QCfQETSVK5neCGnotGHpbf4TGxCWIFqyeoXtrGrQysTmH5n8cl-JI5d14pPnAiVIwN7X6NHPuRbRxnkJ_F-CpbT5XUCSMv7A0iSUa3mAZ3peTYCcLYRgiuCjLTaat424vV9ilVd6dvY9P9x_5Nq6btlHRaRr1zh8zyVGeF7txvoLwnkHp7_y2'>

###### Exceptions package

<img src='http://www.plantuml.com/plantuml/svg/XT3FIWD13CVnkq_HHodixc51wbfRy1fc0WPc4iaVjJwzwCN3akdvyms4tz4SrAEtm_8wBTjbVJgzF9G6PWVY3sXKhuRAq77vAlZk9BpdTKvm0xDFqPhmMHfcboerYizeHUdFn-4wZtsIYcb11JVKAvCdoQCK6Fpm7C1EVaju2dFfg2VeU2UPGEiuoSU-AB11-OMqcScA1FiPF9q3RXrvjqGmXB-9qZVMVpiYr-Zj1m00'>



# Verification traceability matrix

| FR ID | EZShop | User | AccountBook | BalanceOperation | ReturnTrasaction | ProductType | Order | ProductInTransaction | SaleTransaction | LoyaltyCard | CreditCard | Customer |
| :---: | ------ | ---- | ----------- | ---------------- | ---------------- | ----------- | ----- | -------------------- | --------------- | ----------- | ---------- | -------- |
|   1   | X      | X    |             |                  |                  | X           |       |                      |                 |             |            |          |
|   2   | X      | X    |             |                  |                  |             |       |                      |                 |             |            |          |
|   3   | X      | X    | X           | X                |                  | X           | X     |                      |                 |             |            |          |
|   4   | X      | X    |             |                  |                  |             |       |                      |                 | X           |            | X        |
|   5   | X      | X    |             |                  |                  |             |       |                      |                 |             |            |          |
|   6   | X      | X    | X           | X                |                  | X           |       | X                    | X               | X           |            |          |
|   7   | X      | X    |             |                  |                  |             |       |                      | X               |             | X          |          |
|   8   | X      | X    |             |                  | X                | X           |       | X                    |                 |             |            |          |
|   9   | X      | X    | X           |                  |                  |             |       |                      |                 |             |            |          |
|  10   | X      |      |             |                  | X                |             |       |                      |                 |             | X          |          |

# Verification sequence diagrams 
###### Scenario 1.1 - Create product type X

<img src='http://www.plantuml.com/plantuml/svg/VP71IWCn48RFoLFag8SMn5iFfLX512bLsuktCJlOO9k4oJ-7lhq9Q5sDUXc6_UR_cDai2aYWXxqbHOfwU66nv974c6tXMgauexMpcGJ1XqmHvkRvQPToWwtzXjQI1lNO73Ctd36c9sTLMctzPDuCPcQyC857wRDpMqaRq5BvA-CnxTjNBLyfOQX4rxHJgVWpal67CiDDvgxMcIkC3tRDnKl822bs8rSaopJyYDvkxbONbyiT-xTVdC7pccKR0pguIkYtU52A23Xsu3vvEYtL7JqwR1g6yUzY-MTgmN6e3_uE'>

###### Scenario 1.2 - Modify product type location

<img src='http://www.plantuml.com/plantuml/svg/ROwnJWCn341dvokYJZ0KzWvL6TX0gg0iREREQYDT4ikn1_uU9nMe5VLmEldvFMNN54KjBm5DQx7yHH9mqYe0g_TJvLSiU2HvV9fpAYl01YVBrWAxbgR4M1IUF_ibWDF-VyNsKcURzF3DD3Zh5Vve96VHcs6aORtj5TY0yOnA-zgIfbhkxaDVExNcy4ZwLXVoxQKYf2Obzko3EFvLM6_rMEDw6_sxYOtqfKxu1_ub3sAZ5lkuDMOyE4RtptDvkwCoMrv-0000'>

###### Scenario 1.3 - Modify product type price per unit

<img src='http://www.plantuml.com/plantuml/svg/RP2nJiCm48RdorDOdM0e2B5rg0e58GQg2jA5pJYdnYAnhVFTmDjpJbgfKNf35FtV_PzEtcIsnD9tbHMEGVeVfCewZWIm-6fZ-h317f7kdvhUXmNO3AtqagkaDUzyie7XzRji0YWj_nEsfzY8u_eluS0JJ_0X8ur5TLQ36fRhOe4LI6eiuybmSrjfMg1aPKVapzYXffSDGXOAfbZlIF6valYABh5Pher-LoOZd_8Np8vyOX_3RENPqZOsECtVwlVTm-EsHVTxnUaTxf4EmVCCxYAFgjdvDSbmkT4fZM66jp56k-DSxcumDD9t_m00'>

###### Scenario 2.1 - Create user and define rights

<img src='http://www.plantuml.com/plantuml/svg/TP0noy8m48Rdv5SSdRvlKFSEKX471oTnSJlRemsOQxbSyE-RguXOSChB-pnv0scIecYETuzPHyxnHkAnrL6Sg_QOXa2oEG_ZT4B6lkHT5mDN3fCRSaJs4ug6Dap82eUhcO39qXUv91BhSpdzV15MMxEWXbO8bSpu-_TM5MBU4bdtPZKmFM01NdD2ceLdjkv9ZzrdTNxuXz8GT-KRdW00'>

###### Scenario 2.2 - Delete user

<img src='http://www.plantuml.com/plantuml/svg/LOuzZi8m44Ph-nIZLBl5vW0fLg7W0Y0QkY4P4Kl-YSOpzySJ0P8hM-zzp_9S1LaqHOyg9Mjw47jSfB1pmsbD8OSgZ59uS5ZTfWcptriIbh1Z5ZZVhrlP0QlTEdEhnCQrdVu8uU_VLZ31If64RF7pwnioOxkcdYIN4gbn0-za0YPHpc39Q7xCcAYFksUx7BwziDAdEVxKcvdogYc-0000'>

###### Scenario 2.3 - Modify user rights

<img src='http://www.plantuml.com/plantuml/svg/POwzhi8m38Ndv2cYJlSEz04we3Bm0aKiRAQrsaZDZnpx_OahG8gO4ftld4yU2mEnXDs2S8eIdaWMPavaJ7TPWe--C04dwWmKiqc0Q3CG-zbdYEokZzkMieEYluRS2vBcKbzx3Dtfh2qtECaBC6fZykl6vU_VLg1SsxMm8azfnvfhy1uEZf25ug7kbVSH0hRZNtahSLyBa-1dUTpNaX7Z8c5_0G00'>

###### Scenario 3.1 - Order of product type X issued

<img src='http://www.plantuml.com/plantuml/svg/TP0nJyCm48Ndyw-8Ei7GidUeYX0Zeb1rOJlYKsifFaVdku5_Z-qggWBDzlI-zruktcT5KOk3GzF45hz97FQQ161rlAJn3HdF94_FFWPU0MQuM4HsOwc5Fep82gzVDGbGQDKpTieaLs95pT2x-8cbAbrRXlMkZi0MGiv6BVJmw8fLI1ifw4pwcGOgVZLk5I4ruQv-T2CbCDNQ_j_9PdRhNNSLsowJ7YHvw_KbULf8V1Yo1lrP70Wz7KXE77GXSbHKonFyTtmxRnFyxQVRkzmDx8cznU4N'>

###### Scenario 3.2 - Order of product type X payed

<img src='http://www.plantuml.com/plantuml/svg/PP4nJmCn38LdvrT4dM0ex1sgWiJ0W2fHMDZSdjL6kdCYnn7Yt-Ea17JSPhtl-JrBtcL5qJAD3ej6BjEHnE59em2i3fUONf3nJ7B_C4o1Lu0PBcL2TidMmYaaP8MdZ-e4gDJc6NlF95TIR9gXlGoTHHjdq3_YY7oYVI913P4zPd-ChfMljxK6Df3mguNStBegc5vB39n9N-D8fZVXTq58Yx2lHzs96VfQwvy7DicljzRgDvxfSt6IcOoPerdDz9CUE6irzeW_GoPjySz3ArZIWwAMtE7YxZSf_UJh6_wX7V5Wl_m6'>

###### Scenario 3.3 - Record order of product type X arrival

<img src='http://www.plantuml.com/plantuml/svg/RP0_JmCn3CNdtAU8Ei5Gs3jKxS28-DEoi9axgurqbqIE3Ugtn_5nw0gtlVZdzvwLNM4Wbg5lG3X56JwG6cWvaNEhmpdbHuXmGhhVTqE8AmV5dMM0s6IrXJPaYEmUtkkcSvMglc9l1MaaekeAFL4tiLJb7FfdIfsqVBnaz51yrcTZpUjjxN0Rb-5Y2JUtJHtel3OeE26_fXvrReDV0o4BHLylkYDTc6nMRhnLQPbxel09lKOOzEkj7yN6H_pojZE3Tjx8veVhXe6P--U640j3RF5_V44-CB2KXNZ97J2-24GEV9dI9sxUELokPXAhNVYPewBfK-ol_w0Tnaw6_Xi0'>

###### Scenario 4.1 - Create customer record

<img src='http://www.plantuml.com/plantuml/svg/TOx12i9034JFxbV4cnxq0mfAGJnu5Y_UrcwiXTrioIRu-pP2gwBUXdapazH5F8kcwBnA9aqNPESRoJmxbK7s0-sQhlSai3yVRxa7Nqnza9qMoGdPM3DePrrORIq85GIyTeHZQB5qPayqvhR5y6pCDq0QuyXjwbVT_3TEU8TlQkiLC8eoJNZTeXp2wmEx-ZzK8mLDyG40'>

###### Scenario 4.2 - Attach Loyalty Card to customer record

<img src='http://www.plantuml.com/plantuml/svg/bO-nJiGm38Pd-XHXWu5xW9D09vtO4Cl1mcPSgrV9JIh77kxjYQkBGWi3R9O__t_o7RA2g4tSWMcADdsIT82QvEOzbt4kTCHnXgZX-UDqJdE0xDEAlAGBi5wE8BrZnXKzMjOqaJZ2CdVU71wUl2Ri0mg1acVlxhkoQeJJC52_NDy-XcZCbJDkWupNt3u8gKdy_gdTG7eY6O5V5yFsq5B-jyZcsw0AUFRJjrGD_odmQqLLmR9moUj-bqdhpLQv2QWO_L1hv42njuc_0000'>

###### Scenario 4.3 - Detach loyalty card from customer record

<img src='http://www.plantuml.com/plantuml/svg/POszgi9048NhkqynDvL2V0552QY5ZOtOsAt960Fx4sPd2j_U7M4ZQdUOxtndciIMMBmpLZW6yLSaOrkEz7TEEOwP3kqmsi2mlvpkSGIRD7sGhIIE7abPcxDH5nORBS8IVEo6swEKPeTTFHb7wx662ghvocXvSbpiU-nUE_zh2E9SuVhWPpFhUd_xX2mKW4cmY3hsHHeCdNZt1000'>

###### Scenario 4.4 - Update customer record

<img src='http://www.plantuml.com/plantuml/svg/ROn1gW8n44JNFwVelzE55r2K0V44uiPTd6d7G98EdUw5jpSjH57S5VNgrL35isYApgjGrdH1TduKuhzJRR4q6iPGV1Oud8ytAk2hfG-orogKa8sDBJjpORMp8QmXqHIkzpvQB3VE-XUEDCyuFPN_BMIDiNFx-gLR__OPHJc3i68NxUoB39WdJV41'>

###### Scenario 5.1 - Login

<img src='http://www.plantuml.com/plantuml/svg/LOu_ImSn341d-od2R_B0FxjmSYAEJkBY5g_bhj2c9KtmwzjKZlCtDUIz5xeqGHRDoQ5A8StVWHskKlZcyefjZu4VF_PItv5mw_EBpv5Y4yQkN00Rx9gHN4MMkCQA9F3sPKLdDbsHpnROzjfVDqAuVpOBdY2LBTAj4S8Sxg1YQp-5_UIC_zUsuCUXUGRIb0vkQHSCFVmTcvpjpfQ3ADCmZcXyuaJdTRS4yfhJBm00'>

###### Scenario 5.2 - Logout

<img src='http://www.plantuml.com/plantuml/svg/LOmn3i9030Hh_0hJGS478e7Iy094GsSIAudaiqy--_yaGHHqEplQxLkGHnO1oZ3DycO76iByy6piI0tdBAHGoMCPbaeQU7yzPgkhsnBi7Y-t3R13iSaoJcTOyQ-TUDmlZrVK5Fdvxxn3vqXN34-6ddNC8Xy0'>

###### Scenario 6.1 - Sale of product type X completed

<img src='http://www.plantuml.com/plantuml/svg/hLF1Ri8m3BttAt8iYABT9d6eq803fp417s0brbQjTRBWREg-Va2fbhgYghHTdv_ztekTvOd1SwZA13HRhuudz8axGuKk712hpVV-pReEj8SI3nxe19eBIvtQpbiJD1zgXntyKEXtv0snhplu4vH06fyTUhWEknZ8yaPreQxkX5uwIPfobelANRv0iby1ZnmyoKXgksurilpYQ80e78CnxHlO2pdThcUk0LRMu0mg6uW78emT83FbqTClr2loIm3YWkjp_gYKzINRR3SLzN5hx4dqErjgrhZA7_ezqUONYl1BHK3Q3pQVpoT9H1dmorVM29TWJDgGHvm2-u1Y-N59h1l9Z3xFE-pXHGPdW77yBcEh_vCrxj9OVE9qkeEwGkAXgCVLOp-P_EtDGKZqF5P20qabzU-fbqWcLEKF'>

###### Scenario 6.2 - Sale of product type X with product discount

<img src='http://www.plantuml.com/plantuml/svg/hLJBReCm4Bpp5JuIAKIzLKeb53N98QS-GZvWXLSfAh5TirP5lxuG1zNO8W4rHsPdFJExsAkAG9CvbH7a93KxLAWZrK15NYWGnBO_-m-fUj0UIimqYAffAQJerLwrv2Qdh5ROmxCY_qJQ2j9r3t-64aIEBmerd0zh3IIfLLsoipjFBvv5jfoaNcMGxo79du16CbhuaTXkqsaaQUle2j5p39ntCv0jETvjvie2Qybn3YTf10r767s0dobrHhzaHwGt0u8AgflyJYa99Rji5nNsTMaC9OBEZffOL-b3sEDiVia4VZC7YCDWYyLY5ZcKAtxfp1hX4ZYFBNd4hq3QeBzyj-IjMwcotXHLtdf_1y9CNeRToyOjOtZlqq-wCUaZalrskwTDzkc6H8NK-OqxMzCKxhqD57pqOp70ldwjZE90EFLcMQjtpdfOFmv5iyKWYV_Mskleeqqe7nfE5dJ_F-KA1JUdyXS0'>

###### Scenario 6.3 - Sale of product type X with sale discount

<img src='http://www.plantuml.com/plantuml/svg/hPJFRi8m3CRlVOeSYqJHRfCuL6Z0WTF-00zWDTPMhIHPwcZgddujeLhgYb29NJz_ZhzVRLZL19RSgKgW86t5iKQRc5OgYzA08h7zsNzeCv3sKE71WghRbbAhGUt5QkaAEZG61_gXB3wHjefiCz2Ve09LuBD12-V7kW1PxgSknJaTcvVE4b_ESbQvwcTAzZV08ZchE9BORVePMTubYXXPOf2o_mQwCwUxpTnuOQqbpk6adQ88mjG7E5CUVFgbU4TwTQ2efARb3qhPU6JFTfaYlYwDen7ZpjwQ-LJvmxWdsFnIAFmMWP2EmHQBnIm9B96yT7PDI0bIfjuyuHJ8EcJB3qjytSPKpQQiYoxx6n1UbYQz55dqxTRRP3LIy42azLvCL7Boh_2AEtwlpaWWd7ooOTJ_PZsk7w-X-UA8XF-9-JldQWl5fGWPjzvFkK8btQdw1G00'>

###### Scenario 6.4 - Sale of product type X with Loyalty Card update

<img src='http://www.plantuml.com/plantuml/svg/hLHT2zCm57tlhyXZ1-lmJPXGXjk4WUXasm-uDfTPR9EOtY3rrzkF5TCRKmhwUiwvFUSakJqq19PSNILGaBRYqQ1DJ0ULHMb0aJZ_kdtJPWRTeCAx1TLq8wLMC-vgjNG5tLk3C_nU5j-HpefiEyFVGmMgmCy6BGmVwmDa-UYw5qCwvfTkaf7ESiP4zGp9_XXO96SLhoGkfyaZo_j42qAM6AISpa3tulHosfeHE6g9MwYrKnIjiFu3l5Fk7VrUF96-E51KKjlrzwWij9owlLp4ZzTWO15EJj9iJ9M_2MUyczyBXJ-51wHXiTrkjqayoK9U6bIHbEn8qr6yuYcGTSWltwFOTJ-HRcXBg3wv-YjQxsr-r2rKr1x1oh_9F3hCq0nQeGOnSonqRHpXLPUAcW_QzeNYAn7LCrryK90cg48zYso6xqynXdDZt3jM19LSlTaHzV8EE2E1SFKQ-BVylv_9u_WsLcqaWoRynpZk3aUxKXnQQCQb_zxoW4gwkleD'>

###### Scenario 6.5 - Sale of product type X canceled

<img src='http://www.plantuml.com/plantuml/svg/hL91RiCW4Bpx5HmTAKGzvs15RNB8gQsIFc1bLYsg3HGlhTBNDmcnYa5O77ATdMLccDqC19PStrNGahRiRK1RcJCaMsb04Tlz7ZwqcK07wF1eGGtd5QdLPFPYjN0j7Ky69_XHjfz8EqNsDC4VeGFLuhD12zV7BWPuurNNxEekqgiNbH_p9ffa-H72_mKiahCgZiJssr63DnT7CyJ8CGWn_e6-aElzTcayyAG5Bg7NJb4-Gka3IQOc-FerUqTwTQ18qkcSFnZnL7BCTbDXNxV5L2BP7AdSkseUqfsW-JLJ-CC2e4w3hLQhHHLGPlpIbLNW4eIeFRdW5CWwZCiFHb7Tg4JnUMROyqKw8u2mlClGwfsp2koGC0uWHJvmVgMWDOkz_iQKd7pHaBlrz54VhbWyFDKxdCu6bN1zzmS0'>

###### Scenario 6.6 - Sale of product type X completed (Cash)

<img src='http://www.plantuml.com/plantuml/svg/hLD1QiCm4Bpx5Jew48NU2ZcOq2I7dDgIv06BjRIcZgGgAqhw-jfMJEKLDeRsEZFhcT6kDrS2H_vIPr2ISU9yHPVP1ghAoe8ciVy-lXixW8vGuyc1lZOZbT43xiKPvKiwtImEy5DLVY3jDRdR07-26dI9pnOTT1zh0yWYkAv5buxvvOiiqB9WpAYU8VBNm25vftabSTZr7h9e4qq8MM9Ggdy3quhpmsvf0x0r2fTmCLxJH8Mv7-2TYkZfr-8DwTM3fefkJV-8agbbt-tk8ZxlWub5EjbBPKXLFAGpqURNGkENY80yBRPQhHPP99d8IvrgHafGAW_Y6QT0pYDRVaonTQDMiyzpH3szI6yL4CxUPHprlxkUjuzZrGAPDE5_TpW0ZZQbE3JHZ4l_tdA3MlbB_GC0'>

###### Scenario 7.1 - Manage payment by valid credit card

<img src='http://www.plantuml.com/plantuml/svg/hPDBJiCm48RtEOMNgKGla4NLqbQYAv0Q3Z1oHaaaUspPOo2S7hS1cRXHOD6blvd_4JdUUWR7GQi291idNZswmaRKozu2iJX-dZjZ9-WC2ci7vAEaDpIPxHqsFU_1DHFy20f8uhD51rVD9MUz6SqhuL1Y_uP9_0A3HkBoT7WOeOpmT5WLesAzoJeiMgoAR7j6tY9dR5wO_2kX33JfNAwAT5ZFDkJWIAGb_uTzw6YdsZAnsEX_6Um2Bfi_WJ-I2Msd3U5mjupRRMcq3OoYDWnAxBG9nClLy0DbLFpS7rmbzotbaR_jwy5YENFF-GzR2S9taTEepD52kNpr9ZKzYD-VCx8ulBoPwMYBr0IjlW00'>

###### Scenario 7.2 - Manage payment by invalid credit card

<img src='http://www.plantuml.com/plantuml/svg/ZP5DQeOm48RtEKMM2lK2BiIYNRWhs1vWIAOQY8bCna9x-aQo2ARIVijvtfy9cJum41-R5Q3OatmFI6AFo2YpWsFvyZslVh-W6Io-4RWG8yQxYpOGQiC3a1PdLTEbV2i95PfFpFehV6tek9h6fmHLXDDOYvHekcBDdnMrADmtyGMvOFV1tDzAwq7dkQf57fhR5_91JcPJ-AV-MDspNQhCfl717HzW0_vgp_zziIKOrVCcXTQZq_7-Fm00'>

###### Scenario 7.3 - Manage credit card payment with not enough credit

<img src='http://www.plantuml.com/plantuml/svg/dP5DQiCm48NtEeMMDjGNo2Aan85wLqXxWC6UsW9fP4QZGdhwAcWXh9gqTAdl_Jp1d880ItHMmIYUzNj0Lcj2PZGhaEZBrtNnwmPTmU8R0uKKCPussfbnCd86djIzgZlc_44pZcW-iUYlS7D8qWpzKuPZWaFVgfpeZjNCmufMLUwT-8nIiVrWwJzews4gxwPLvT7j_b0Yaowcy4jzNEZPparXG__73U68ZyjV85p8ntbndl3sduqFiE7dIBdfnfPXKkzthxGJqXIT_GO0'>

###### Scenario 7.4 - Manage cash payment

<img src='http://www.plantuml.com/plantuml/svg/bP4zJiSm34VtdYBZdw4Nw52L3mOc42q7i8958oLEv3YWSdfIma3J8fNr_VoSs1wY04jojW4jdjLBH6v2HaQR02Jg_dESVTYW4InE31IpOZnjiXkmG1eV0p9yXsl3jYzTEiMeqRpZBSJv2HQ799U7kwkcn6rVTVwplgex4DzGAdPIrDw59AWcBs3LjVE9v1_QCyOpvH7bPvvf2NWv-5Yzo4uHVgYQPhD6xU4Q9J7jxDshlmvL35x7AIo7wostqO3qcfpz0W00'>

###### Scenario 8.1 - Return transaction of product type X completed, credit card

<img src='http://www.plantuml.com/plantuml/svg/hLJ1Ri8m3Btp5Jbs4fMsAuUAhN3WD3RW0wB6sgArQUGwchglNwZHrYnLLYHkyEpdz_nmM7SaaLnJBsH5BOfJ1xYm7jALjjAGs7uTtbiRGAz03iqHfUayIRScgEwnLQwYOsyXm8-w-W3Q6i8-m9zaBKq5pnPG3iFE5lA2TLTYy1SfPdSBRiYBg9RWH5Z-AuH3BLvEx3ONBTzyzfPk3TrpvoMIRBTPMlvOjWgMiccTeSGcC_dnLiL1rX0CUkFeDNG-XZ_LF24jW5bJ8gDVT11u7Fpu-QD2SZOlmDsnm1Gpe1JyvLzhXnzhJ9XlaAwofbJ6X5c7HkXW-h2u51uI67Nb8vXcJEqoUh4huQoI12zE6jBKUyAeciuA7SmwnGveZxg2Am-nyxsto3KHsQbykCyOBn6KfbAYsik-WVDPJsOt_lVY8GO-HO1oxY6KtZNYtsBlDHZbcleR'>

###### Scenario 8.2 - Return transaction of product type completed, cash

<img src='http://www.plantuml.com/plantuml/svg/hLJ1Ri8m3Btp5Jbs4fMsAuUA3JXm6Xlm0L5ZZMXj6YMEfkxhbzPeQqYLjH8tUFRpUtuuh2roWwwk5hp4nh2p1RFG7fAbr5mXstqVBuqEe7T0PzJ9S6KzIJOgg1vC8ro9frP3W9za-GcuKsZQ07_X5LSbl6eml1_MMSWBqbsntb-ac3qig24lebg24s7vdv3fQ_5oRB-zQldcpbkwjUku7NgD9Djlbve-RXe1IrutJc5oaqdyUAlYo2i81drGz1AiZ-6ccYUa6H1hJ6JmY_O2pxqVFtzGICwc1QWx5XXZ1fI2llnhxVXhZGZJ3U8iQq9aH9XqQ6WSZ1yMbS930YLcFe9nnjWkWnUxOauBZl3ck4A9hISCgkciZ8D9fsW13qQMCFCGhU-zHww9oCwRfyUCy0stbmDlQ-WEUZIruVyMqHLyiG2bn4CelMN4lyV6Qr32rTKF'>

###### Scenario 9.1 - List credits and debits

<img src='http://www.plantuml.com/plantuml/svg/XO_1IWGn38RFzYbwr8Fx02BBxgetGL2yU4iwmIrEazAcxsypmxZCWinfIhul-VyvL8MYBOqEcWgtX5GS19LoyrcfUAZ-t1AmorsB8MPWzIzV7sV9dTbhHPQTrhyhS0n16kj9vCSuu8gUO0GEz9QfW4PXKr3SbEplznRZ7_mtwLEX8Mezyl1Cs1-tTyvOLonnTjvbf3wtmMMtaBR2Kx-T2Jk6H9Tjm7_l0vg0Cl7heYQWp1V-GbvZrSThS-y0bpeRvb9g_lqj-K0yj3J-0W00'>

###### Scenario 10.1 - Return payment by credit card

<img src='http://www.plantuml.com/plantuml/svg/fL9BJWCn3Dtt55aE4dE1BaOLfGiMV4JX06R6waHAdCXnXChfeHqXasQWbLZ6p-_ZwAsI04lsRW6z13Ql2NaHWSNsDWA9sNninn2BqJDAPdfXe0Izs405UWCEgCV7Y0n7y23PTfFEqknGjiWMt4FsRyZDrM92scxDE5XP0mzpMugsAiR794QnRx6sExZEBMWQ5L3u2VOUIPgxs-iV9CrOdNwvH4KjBtPvf4O7vS6_-nhXZ6Ur0mpwRYxMrhLKMymI--1Z5ZGI19m17pB9kNGZf0s5l1ll0-6-cKZ_ll-qXaj3-5wLy-ilJaT_kLNyshbefoQiUgdG2cd8tdq2'>

###### Scenario 10.2 - Return cash payment

<img src='http://www.plantuml.com/plantuml/svg/bP2zYiCm48HxFOLA6yuluCAO-ockkkCkUO15MR11MedLYf0yVMmhZIsJa7P6twnc-YZ0afnLOCIpFaPa5IPfD6C04lrr_HzyM4b_A8dfm41nWaPFA_STB931du0CYpb7DbtEQJKly0V4uHSk3acgxyyt3SudabhbLqrNd7W65i1kXl4k944jNi3UsHTHnhYBR6ktcl1SZ57LQYir3zeM_CxbxRJvXrcRp7dzjTKZdPApDm00'>
