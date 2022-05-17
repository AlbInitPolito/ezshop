# Requirements Document 

Authors: Matteo Favretto, Vincenzo Sagristano, Alberto Castrignanò, Francesca Silvano

Date: 21/04/2021

Version: 1.0

# Contents

- [Essential description](#essential-description)
- [Stakeholders](#stakeholders)
- [Context Diagram and interfaces](#context-diagram-and-interfaces)
	+ [Context Diagram](#context-diagram)
	+ [Interfaces](#interfaces) 
	
- [Stories and personas](#stories-and-personas)
- [Functional and non functional requirements](#functional-and-non-functional-requirements)
	+ [Functional Requirements](#functional-requirements)
	+ [Non functional requirements](#non-functional-requirements)
- [Use case diagram and use cases](#use-case-diagram-and-use-cases)
	+ [Use case diagram](#use-case-diagram)
	+ [Use cases](#use-cases)
    	+ [Relevant scenarios](#relevant-scenarios)
- [Glossary](#glossary)
- [System design](#system-design)
- [Deployment diagram](#deployment-diagram)

# Essential description

Small shops require a simple application to support the owner or manager. A small shop (ex a food shop) occupies 50-200 square meters, sells 500-2000 different item types, has one or a few cash registers 
EZShop is a software application to:

* manage sales
* manage inventory
* manage customers
* support accounting


# Stakeholders


| Stakeholder name  | Description |
| ----------------- |:-----------:|
|   Owner   | The owner of the shop can manage accounts of any type, products, accounting and software setting |
| Manager | The manager of the shop has access to all the functions the owner has access to, except managing "manager" or "owner" type accounts |
| Cashier                   | The cash register operator has access to the cash register functionalities only |
| Regular customer | Anonymous customer who doesn't have a fidelity card |
| Fidelity program customer | Registered customer who has their own fidelity card, and is identified by the card's barcode |
| E-mail service provider   | Supplies e-mail services |
| Team 16 | Manage and maintain the EZShop software |
| Supplier | Who supplies the products at a given cost |

# Context Diagram and interfaces

## Context Diagram



<img src='https://www.planttext.com/api/plantuml/svg/VPBB3eCW44NtV0LBNVW3B1fJq-uQZUow8p9H4WN2eyQa7rzYwYE-bimSBdVka5h7ZFDTYyYBILQ38JWnecvSyhrXaZ7RY5139E_bfZVnY3m5XrQu0HT6rOPrEFFMgMxalHxX4chVChDq48fAg41e5su9wS34EE6gbyaHZO_e8BssY4YHK_8lHuytRPJ6TB0EkXXjpjCS0Tpg8d9dfb8SS0cCJpFi5UU7PzixOadqZ9XIFUjJhtKhBao4n3-2hpPpmEpJF16xskEPUWgIX-_p0m00
'>



## Interfaces
| Actor | Logical Interface | Physical Interface  |
| ------------- |:-------------:| -----:|
| Owner                                       | Administrator GUI | Terminal |
| Manager                                     | Administrator GUI | Terminal |
| Cashier                                     | Cashier GUI<br />Receipt printer driver | Cash register<br />Receipt printer |
| Regular customer, Fidelity program customer | POS API<br />Receipt printer driver | POS<br />Receipt printer |
| Barcode reader | Barcode reader driver | USB/Ethernet cable |

# Stories and personas
- **Matteo Favretto, 24 years old, uni student**
  Matteo is a frequent customer, so he's requested his personal fidelity card. 
  He'd like to obtain his discount easily so that they can be applied automatically, just showing his fidelity card.
  Moreover, he would like to save some money getting coupons and exploiting product offers.
  **Goals**:

  - accumulate shopping points
  - save on shopping

  **Story**
  Matteo is shopping and he decides to buy products on offer in order to save up.
  Also, he decides to use his fidelity card's points - which he knows from the last invoice - in order to obtain a discount on the total cost.
  He's also deciding whether keeping on shopping such with the aim of reach the requested spending threshold for obtaining a coupon.

- **Alberto Castrignano', 22 years old, cashier**
  Alberto is one of the shop's cashier and he wants to be as efficient as possible in sales managing
  so he exploit the automatic coupons managing mechanism and the possibility of selecting products already registered into the
  cash register which would require more time for their checkout.
  For the sake of convenience, he uses the cash register's locking mechanism to not waste time when he needs to get away from the workstation.
  **Goals**:

  - save time in sales managing
  - save time in resuming the activity

  **Story**

  Alberto is serving a customer when he needs to scan a barcode of an heavy water box. So he tells to the customer to keep that article into the
  cart because he'll insert it into the transaction's list of products using the "quick access products" function.
  After completing the transaction, Alberto gets called by the owner and so he decides to lock the cash register using the lock button.
  Once he has come back, he resumes his activity just inserting his own account password.

- **Vincenzo Sagristano, 23 years old, shop manager**
  Vincenzo is a meticulous manager: he wants to keep trace of everything, including expiry dates and the exhaustion of products.
  As soon as necessary to get a new product supply, he looks for the related supplier to contact him.
  He cares about his customers, so he often generates promotions on products, trying to encourage them to shop and supporting the usage of fidelity cards.
  **Goals**:

  - avoid running out of products
  - increase the amount of customer

  **Story**

  Vincenzo, at the end of the day, notices that some products are running out. So he decides to contact the related supplier for those products in order
  to obtain new supply. He also notices that some products are going on expire because they are little purchased, so he decides to create promotions to get rid of them.

- **Francesca Silvano, 23 years old, small shop owner**
  Francesca has a small shop and she has a trustful relationship with her employees, but she doesn't tolerate pointless losing of incomes or lack of competence.
  She keeps trace of the shop's balance daily and she checks that any sale is efficient even using periodically updated charts and diagrams.
  **Goals**:

  - check the income
  - managing the employees

  **Story**
  Francesca gets a notification about the lock of the account of an employee, Alberto, due to multiple incorrect password insertion: it's the fourth time only this month.
  She also notices a decreasing in income and she decides to check what are the cash registers from which she obtains the less income.
  So she find out that the worst cash register in term of income is the one used by Alberto, whom efficiency is not compensating his clumsiness.
  She decides to fire him, so she calls him into her office to warn him. At the end of the day, after the closure of the activity, she provides to remove him account from the system.

- **Angela Orwell, 50 years old, manager**
  Angela has a huge experience as shop manager: during her 25 years of career she has faced a recurrent problem in managing coupons
  for promotions in her small shops. She'd like to have a way to manage promotions, to enable or disable the usage of a given category of coupons
  and to know the expiry date of each class of coupons. She would love to be able to have a detailed view on the employees in order to
  manage all their information.
  **Goals**:

  - manage coupons
  - manage employees

  **Story**

  Angela uses her experience to think about the best expense ranges to set for the generation of coupons. To cover the periods in which there are less customers, she thinks about the best coupon expiration period to tempt her customer into doing shopping.
  Then, she welcomes the new cashier, Andy, and creates an account for her.

- **Andy Warhol, 38 years old, cashier**
  Andy is a 38 years old cashier. She is used to be very lazy and she would like to be able to access to her terminal in a quick way.
  Moreover, she would love to have a snap mechanism which helps her with heavy and frequent products during the checkout phase.
  Some times ago, she was working when she was suddenly needing to go to the restroom,
  but nobody was near her cash register to make sure nobody tries to access and now, thinking about that episode,
  she'd love to be able to lock the cash register in a quick way in order to be allowed to get away safely.
  **Goals**:

  - quick access the cash register
  - manage fast products

  **Story** 

  Andy starts her working day and makes a quick access to the cash register, without many procedures.
  It is an heavy day at job, so she reads as less products as possible, introducing them with the list of fast accessible products.
  Then she decides to make a quick pause and go to the restroom, so she locks the cash register in security and goes away from it.


# Functional and non functional requirements

## Functional Requirements

| ID        | Description  |
| ------------- |:-------------:|
|  **FR1** | **Log in** |
|  FR1.1  | Load roles |
| FR1.2 | Read barcode for username |
| FR1.3 | Recover password |
| FR1.4 | Username does not exist |
| **FR2** | **Log out** |
| **FR3** | **Manage sales** |
| FR3.1 |             Start transaction             |
| FR3.2 |           Read product barcode            |
| FR3.3 |            Add product to cart            |
| FR3.4 |           Set product quantity            |
| *FR3.4.1* |      Manage coupons for transaction       |
| *FR3.4.2* | Generate coupon based on customer expense |
| FR3.5 |                Use coupon                 |
| FR3.6 |      Compute all possible discounts       |
| FR3.7 |      Update points on fidelity card       |
| FR3.8 | Use fidelity card points to get discount  |
| FR3.9 | Remove products from transaction |
| FR3.10 | Compute total price |
| FR3.11 | Use POS |
| FR3.12 | Print invoice |
| FR3.13 | Print receipt |
| FR3.14 | Cancel transaction |
| FR3.15 | End transaction |
| FR3.16 | Register transaction |
| FR3.17 | Manage "Quick access products list" |
| *FR3.17.1* | Add product to list |
| *FR3.17.2* | Remove product from list |
| *FR3.17.3* | Load list |
| *FR3.17.4* | Add product from list to customer cart |
| **FR4** | **Open cash drawer** |
| **FR5** | **Manage inventory** |
| FR5.1 | Add new product |
| *FR5.1.1* | Generate barcode for product |
| FR5.2 | Change product quantity |
| FR5.3 | Remove product |
| FR5.4 | Notify when product is running low |
| FR5.5 | Search and filter products |
| FR5.6 | Restock product |
| *FR5.6.1* | Read restock CSV file |
| *FR5.6.2* | Associate supplier to product |
| FR5.7 | Print product barcode |
| **FR6** | **Manage user discounts** |
| FR6.1 | Manage fidelity card |
| *FR6.1.1* | Add fidelity card |
| *FR6.1.2* | Renew fidelity card |
| *FR6.1.3* | Enable fidelity card |
| *FR6.1.4* | Disable fidelity card |
| *FR6.1.5* | Set fidelity card discounts |
| FR6.2 | Manage coupons |
| *FR6.2.1* | Enable coupons |
| *FR6.2.2* | Disable coupons |
| *FR6.2.3* | Set coupon discounts |
| **FR7** | **Manage cash registers** |
| FR7.1 | Connect cash register |
| FR7.2 | Remove cash register |
| FR7.3 | Lock cash register |
| FR7.4 | Unlock cash register |
| **FR8** | **Manage accounts** |
| FR8.1 | Set account data |
| *FR8.1.1* | Generate account barcode |
| FR8.2 | Set account role |
| FR8.3 | Delete account |
| **FR9** | **Manage product discounts** |
| FR9.1 | Add product discount |
| FR9.2 | Remove product discount |
| **F10** | **Manage accounting** |
| F10.1 | Elaborate accounting data |
| F10.2 | Read accounting data |
| *F10.2.1* | Filter accounting data |
| *F10.2.2* | Generate data graphs |
| F10.3 | Modify accounting data |
| F10.4 | Perform cash register closure |
| **F11** | **Add barcode reader** |
| **F12** | **Use calendar** |
| F12.1 | Display products with active discounts for a given day |
| *F12.1.1* | Display products with discounts expiring on a given day |
| F12.2 | Display products expiring on a given day |
| **F13** | **Manage suppliers** |
| F13.1 | Filter suppliers |
| F13.2 | Display products from a given supplier |
| F13.3 | Add supplier |
| F13.4 | Remove supplier |
| **F14** | **Add POS** |
| **F15** | **Setup system's email account** |

## Non Functional Requirements

\<Describe constraints on functional requirements>

| ID        | Type (efficiency, reliability, ..)           | Description  | Refers to |
| ------------- |:-------------:| :-----:| -----:|
|  NFR1     | Security | Authentication must always be secure | FR1 |
|  NFR2     | Efficiency | Product data should be retrieved in less than 0.5 s | FR3.1, FR3.2 |
|  NFR3     | Availability | Cash register functionalities should always be available when cash register is active and unlocked | FR3, FR7.3, FR7.4, FR10.4 |
| NFR4 | Maintaiability | Function should be available with any barcode reader | FR3.2 |
| NFR5.1 | Usability | All needed functions should allow to read products with less than 0.5 s between each, and to finish the entire sale process in as little time as possible | FR3 |
| NFR5.2 | Usability | Cashiers should be able how to operate the system efficiently in about a working week | FR3 |
| NFR6 | Correctness | Points should be awarded only based on total sale price, considering the customer card after all products' insertions and eventual removals | FR3.7 |
| NFR7 | Correctness | Remaining points amount on fidelity card should always be correct after the transaction ends | FR3.8 |
| NFR8 | Security | Transactions with POS should always be available and secure | FR3.11 |
| NFR9 | Reliability | Transactions cannot be finalized if receipts haven't been correctly printed | FR3.13 |
| NFR10.1 | Security | Cash drawer should be opened only by privileged access or by key | FR4 |
| NFR10.2 | Safety | User should always be notified when cash register drawer is about to open in order to avoid any physical collision | FR4 |
| NFR11 | Correctness | Product quantity in inventory cannot be below 0 | FR5.2 |
| NFR12 | Correctness, Availability | Fidelity cards must always be automatically renewed when expired | FR6.1.2 |
| NFR13.1 | Usability | Cash registers running the client software should be detectable by the server if connected to the same private network | FR7.1 |
| NFR13.2 | Security | Only registered cash registers should be allowed to connect to the system | FR7.1 |
| NFR14.1 | Efficiency | Server must be able to manage efficiently at least 2 cash registers at the same time | FR7 |
| NFR14.2 | Availability | There should always be at least one fully functional cash register | FR7 |
| NFR15 | Security | Roles must always be respected | FR8 |
| NFR16 | Correctness | An active discount should always be applied to a given product | FR9 |


# Use case diagram and use cases

## Use case diagram

<img src='https://www.planttext.com/api/plantuml/svg/bLJBReCm4Bpp5Nii1_817A94LAag9KsQj1zWsHjYrQoH7wL8_VYQ9f0d4bmui3EpuvsrPzOnutoXedJ5YELeKaYCp0yk-Pr2cZ5xaE5VbAuhkgcr-2ZTOkuLCv1vwtHn0rj8WKgw6ZP6vuOLPrGKTVgG-3APmjDHiuF0ZYcCxs7h4WcU3QlGn46blmKaGbVKKB83ew2yqeHrF8poQH4swzr0T8_r8TIDaUJWbRwrv4CvZRbC-rAJRKRm888MrQbbp8Y0VUmK9hwST5wso56MBeOK977b1TfUNkEeOSw5W0_3o3BkfAPHt5CwuRp2SmVldf4BbaTfl63OKERQiOpYtiyfiF778Ob-iiAzczn4CO3MzhnEVG3n652xbICwNLwNmNr2qJEbwk3Ged5TDfaMWzp-lm3jNYoCBaAgad_1d7EqjfDRIkikvQBhczfxY5EqP_pzxdJKD-talkugiFL4ad9OwkgozInymqlu1m00'>


---------


<img src='https://www.planttext.com/api/plantuml/svg/SoWkIImgAStDuRBoJSnBJ4yjibBGBItGhR5Ji79EBCx810fmMVcNvo5Bm5HoSS7LAEWMPQPdbEZQAMWufgGek6McfEPabbGgE2SMfMWYcurN6P8zs5KufEQb01qB0000'>

---------

<img src='https://www.planttext.com/api/plantuml/svg/SoWkIImgAStDuRBoByzBBR9Iq2tAJCyeqQsnKx3oJSnBJ0UASN71cGgwAVdbURfs2XhE-NbvgSab2iw9nHaAGQdfcSKbgKMQs5GwfgGGeIeeDJU_B1LT8S9PfGNuR4LLXSr6L1NORND8pKi1XWK0'>


---------


<img src='https://www.planttext.com/api/plantuml/svg/RP3B2i8m44Nt-OfPsaNx0LcKMd4Xg1LTk1wIs0RJfEH15Vnuqq2rYBlXddDXPYhdqVhGAq8FgB4Lba9kPTlv_5K2tQ3hPCm8RKQTM1WIc6L2FavY9QpsfeKc-6n9k1drp7Ow8sIUe9XG4T55Q0udT6uqbiDL-WwsFKgL0GNny96wHIiCK-Sia4EDbXak_dlfb4cjbM5tM3DcWlPBb_oYRoUjT3HUtfvJGsgc0XUEL36FBti3'>


---------


<img src='https://www.planttext.com/api/plantuml/svg/ROzD2i8m44RtESKixI8zG1QbwjOVK3n0I68Rh9EIJAY2XpU9M4IwVUzz0zC6afxYVM1YBr5snWlWtdOzyLS3OYj3RnDZuZXXSN4iuXipDZV0jPkGDx3QENM3ZE5aEXl8-Eet-vnFtGM7XN8PGvslrscrANQjb8j85GWm3pAe0_ity-ASD1mSsUipznRL4BK9h4qylVu6'>

---------

<img src='https://www.planttext.com/api/plantuml/svg/SoWkIImgAStDuRBoByzBBR9Iq2qjqAsnKx3oJSnBJ0SAS771cGgwHPdf6IMwTWeQplbvQPdffKMf2ixvfGNvUSMQ20FmAUB28gYWLrgW1rJkcfUOb8CaRAMGMb6IcfTIZ5rJIdvv7aYRIsOnBdqEG1BeDm00'>


---------


<img src='https://www.planttext.com/api/plantuml/svg/SoWkIImgAStDuRBoByzBBR9Iq2qjqAsnKx3oJSnBJ0SAS771cGgwHPdf6IMwTWeQplbvQPdffKMf2cwPAQavcIML2iw9HId5cWXpY53BXLjEGQ-qG0-esJKlCIa7KzPA8RMY93KlfHYl4IdvvNaWGrmoYmcOuW0aWQ400000'>


---------


<img src='https://www.planttext.com/api/plantuml/svg/SoWkIImgAStDuRBoByzBBR9Iq2qjqAsnKx3oJSnBJ0SAS771cGgwEQbf9Rfs2XgEAIaA0KNvAQN99GekcSN9-QLv9Pe88_0hGpQmA3Cz0wmqA3KtlomLdw64bNBXb5BG0-lM0-f6brDmhIZ8B4ejrbImKaYjA4dDIodcSW2Im3010000'>


---------


<img src='https://www.planttext.com/api/plantuml/svg/ZPB1Ri8m38RlUGeVnu4-G0-8WRJJs1X3kqU9AT6AZL9d7H8FlwRGjMNjjbkK___ysqxcXMWluP2hzBaaz2bCmt5wda6wqgIpwaAfxsEjpU3ktbfOUxR1oAIhvhYJg6-m43RlWnxB9KNFSgyfmyO2Bq6JE3drlDvb-xhWWyi5VLjFtTv04ev9dQmjBBGtR742AU2d8DbYrB_sZkH_G6py4d7W3so5tvXwW0YjsBhTQGXIevje8vzO8jqei0b4ZZ9uvB9VwdRnlRKiNzzwtVuqn_TBhk-xzPeARSGn_K5T8hf_el8xCdcmE0WqOuqEti7dLMhrExy0'>


---------


<img src='https://www.planttext.com/api/plantuml/svg/ZP0n3i8m34NtdC8Z3Ea1CbGj83eXa3Y1bLfjfE1MgKCPE3mf8601kbZMzt_-iej1C4WyUMNs8rCme6ElhpcO7J8sIL3grO8Ehcb5vx3OEYyKeBIsYooE6zYWuFAzvQzJVPj05liiWOVWM9wI0Re8SJtyeEfkv8axufcW720PQHxvo5kZjz6Z4ApG8zixxDZwMDESt8hIvHDU1UpRp-GYrVJY6m00'>


---------


<img src='https://www.planttext.com/api/plantuml/svg/VP113e8m44NtFSKiTL4Eq0K1zqPZmW5gEo89J4cPYWiFRoL14SJDP3B_vUTbiewrvz3KGXrwGgz0XbO-Kb1xJRgC1o7UAyWQBonJs1nzHGmxxOspkFr55AtLZ90RumBn5-AhyZemkRN_WHCsxZOf-KYkYSppXSO8MDVJqc5CrmtcZP2ycf81bKZwN4yo1NXd9Dk9BCxuvIS0'>


---------


<img src='https://www.planttext.com/api/plantuml/svg/SoWkIImgAStDuRBoByzBBR9Iq2qjqAsnKx3oJSnBJ0SAS771cGgwHPdf6IMwTWeQZYaf2aw9HSdvAQaAGQc9AQb5cWWJyAl2ObfGQcv-MIeH1k9IY30p9pKDRdM0Vp2I8HWo3gbvAK2V0m00'>


---------


<img src='https://www.planttext.com/api/plantuml/svg/SoWkIImgAStDuRBoByzBBR9IqCr9JIlHhR5JiF9Dp4jC1mfnSS6P2hgb1Rfs2Xf1gOaf2dxv9PbfcQd5cWYzM6KTKlDIW3O00000'>


---------


<img src='https://www.planttext.com/api/plantuml/svg/SoWkIImgAStDuR9oJYpEo4mjibBGpKbDAz6rKz08IitDBqjL22ZAJobDBb5mAyhFLGWipKpELd1CJaujBivCoe7B5ciYcr2KcPu1Di8n9GMkFoGVdsw7rBmKe3i0'>


---------


<img src='https://www.planttext.com/api/plantuml/svg/TP7B2eCm44Nt-OeiTU4FP25IQNV5ehlkWfd6G1wIJ12X7zyKAbQ8svnpcRcJ9Y3t68qcxCujb-0PhJIyi7hNbBKyJ2ez4TOjzeWscx0UPDJSqpO6TEPetPG0hN2b3--avsQN22ckps5oCntMW63ANwgcHGyZg1bJHba4NvB_tOHRx5SwXlS5p_XMvTouS3yw0RG7BlOM6J5r4N74PCGvnpC7oOldrw6LLtBAJK_Zko7n76h0YlIL7m00'>


---------


<img src='https://www.planttext.com/api/plantuml/svg/SoWkIImgAStDuR9oJYpEo4mjibBGBSfCpoZHjLFG24WjIiilobK0IIa4fQPd5fUa5Yauv-GNbnQbQd8vfEQb0BK20000'>


---------


<img src='https://www.planttext.com/api/plantuml/svg/VSqn3eCm34RXFQVunpB00J8WAaJN3Zr1bBZKKd0anodNR-40p9_qtbGjgBKjE__SXTIZLru_re-upKh120_MQZWLznXB4-lS5M98PPVXA1HvyzgKi6o1CrwaNuxKmOCbvfQekeaa_Vy_'>


---------


<img src='https://www.planttext.com/api/plantuml/svg/ZP1D2i8m58JtFSKixI8zG1QbODqfzGgF9BI1-1BoGnKyl46mua9nEpFVjvWXPOgvN5mZfvLDb10bY7iFUIAckGRl8jfvoQB7RWg6GHZf1i_OapEiARRDnlon_LQWUw9THO-UDCxHww8oLfiNZ3OfNpYtaB2iND4c_J1fl_8cEbo3ZPRdbp5zI8RAr1SU'>


---------


<img src='https://www.planttext.com/api/plantuml/svg/SoWkIImgAStDuRBoByzBBR9Iq2tAJCyeqQsnKx3oJSnBJ0UASN71cGgwfGMwTWeQRfav9Qb52i6b1GKvcQb5nPh8IbBoo_D0YXnJKWYe24hDpIzBHL44SmPEUr0LeDi1ayLj0nUceGfw8ELwGEN1gGYr2W55-IcboIL0UIk5r8gIrBoKOYu780aCBW00'>

### UC1 - Manage customer sale

| Actors Involved        | Cashier, fidelity program customer OR regular customer, Barcode reader |
| ------------- |:-------------:|
|  Precondition     |             At least one product in transaction              |
|  Post condition     | Receipt emitted, product quantity updated, transaction stored in memory |
|  Nominal Scenario     | Cashier adds all product picked by customer to cart using the barcode reader, total price is computed considering all promotional discounts, the transactions is finalized and stored in database, items quantities in the inventory database are updated |
|  Variants     | The customer may have coupons or a fidelity card, which will be awarder points based on the expense, and which may grant discounts according to the amounts of points at the beginning of the transaction. The card might have expired and consequentially have been renewed, in which case the customer is notified. The customer might pay with credit card using the POS, and the transaction might be declined by the bank. The receipt printer might have run out of paper or ink.  The customer may want to remove some products form the transaction, or cancel the transaction altogether. The customer may ask for the invoice. The customer may have purchased enough item to get a coupon, in which case it is printed. The cashier may add products from the "Quick access products list", or manually input the code for some products. |

##### Scenario 1.1 - NOMINAL

| Scenario 1.1 | |
| ------------- |:-------------:|
|  1     | The Barcode reader reads the product barcode       	|
|  2     | The System loads product information 	      	|
|  3     | The System loads current price 			|
|  4 	 | The Cashier selects product quantity 		|
|  5	 | Repeat until all products have been added 		|
|  6	 | The Cashier selects "Compute total" 			|
|  7	 | The Casher inserts the customer handed cash amount 	|
|  8	 | The System shows the computed change 		|
|  9	 | The Cashier selects "Open cash drawer" 		|
|  10 	 | Cash drawer is open 					|
|  11 	 |       The Cashier selects "Print receipt"       	|
|  12	 |            The receipt is printed             	|
|  13	 | The System stores transaction in database 		|
|  14	 | The System checks coupon requisites 			|
|  15	 | The Cashier selects "End transaction" 		|
|  16	 | The System updates item quantity in inventory 	|
|  17	 | The System resets cashier interface 			|

##### Scenario 1.2 - Customer has fidelity card

| Scenario 1.2  |                                                              |
| ------------- | :----------------------------------------------------------: |
| Precondition  | Customer has fidelity card with enough points to get a discount, total expense is above a threshold |
| 1             |                     The Barcode reader reads product barcode             |
| 2             |                   The System loads product information                   |
| 3             |                   The System loads current price                         |
| 4             |                   The Cashier selects product quantity                   |
| 5             |          Repeat until all products have been added           		   |
| 6             |                  The Barcode reader reads fidelity card barcode          |
| 7             |             The System searches for fidelity card in database            |
| 8             |                 The System applies fidelity card discount                |
| 9             |                    The Cashier selects "Compute total"                   |
| 10            |                    The System computes points to add                     |
| 11            |                   The Casher inserts the customer handed cash amount     |
| 12            |                       The System shows the computed change               |
| 13            |                  The Cashier selects "Open cash drawer"                  |
| 14            |                      The Cash drawer is open                       	   |
| 15            |                    The Cashier selects "Print receipt"                   |
| 16            |                      The Receipt is printed                      	   |
| 17            |                The System stores transaction in database                 |
| 18            |                   The System checks coupon requisites                    |
| 19            |                   The Cashier selects "End transaction"                  |
| 20            |              The System updates item quantity in inventory               |
| 21            |          The System updates points on fidelity card in database          |
| 22            |                   The System resets cashier interface                    |
| Postcondition |     Customer fidelity card has amounts of points updated     |

##### Scenario 1.3 - Customer's fidelity card has expired and has been automatically renewed, with points set to 0

| Scenario 1.3  |                                                              |
| ------------- | :----------------------------------------------------------: |
| Precondition  | Customer has fidelity card, which has been renewed and therefore has no points |
| 1             |                     The Barcode reader reads product barcode                  |
| 2             |                   The System loads product information                        |
| 3             |                      The System loads current price                           |
| 4             |                   The Cashier selects product quantity                        |
| 5             |          Repeat until all products have been added           		        |
| 6             |                  The Barcode reader reads fidelity card barcode               |
| 7             |             The System searches for fidelity card in database      	        |
| 8             | The System displays message "Fidelity card has been renewed, total points: 0" |
| 9             |                    The Cashier selects "Compute total"                    	|
| 10            |                   The Casher inserts the customer handed cash amount          |
| 11            |                        The System shows the computed change                   |
| 12            |                  The Cashier selects "Open cash drawer"                       |
| 13            |                      Cash drawer is open                       		|
| 14            |                    The Cashier selects "Print receipt"                    	|
| 15            |                      Receipt is printed                      			|
| 16            |                The System stores transaction in database                 	|
| 17            |                   The System checks coupon requisites                    	|
| 18            |                   The Cashier selects "End transaction"                   	|
| 19            |              The System updates item quantity in inventory               	|
| 20            |          The System updates points on fidelity card in database          	|
| 21            |                   The System resets cashier interface                    	|
| Postcondition | Customer fidelity card has been notified about card renewal and amount of points is updated |

##### Scenario 1.4 - Not enough points on fidelity card to get a discount

| Scenario 1.4  |                                                              |
| ------------- | :----------------------------------------------------------: |
| Precondition  | Customer has fidelity card but not enough points to get a discount |
| 1             |                     The Barcode reader reads product barcode       |
| 2             |                   The System loads product information             |
| 3             |                      The System loads current price                |
| 4             |                   The Cashier selects product quantity             |
| 5             |          Repeat until all products have been added     	     |
| 6             |          The Barcode reader reads fidelity card barcode            |
| 7             |             The System searches for fidelity card in database      |
| 8             |                    The Cashier selects "Compute total"             |
| 9             |          The Casher inserts the customer handed cash amount        |
| 10            |          The System shows the computed change                      |
| 11            |           The Cashier selects "Open cash drawer"                   |
| 12            |                      Cash drawer is open                           |
| 13            |                    The Cashier selects "Print receipt"             |
| 14            |                      Receipt is printed                      	     |
| 15            |                The System stores transaction in database           |
| 16            |             The System checks coupon requisites                    |
| 17            |                   The Cashier selects "End transaction"            |
| 18            |              The System updates item quantity in inventory         |
| 19            |          The System updates points on fidelity card in database    |
| 20            |                   The System resets cashier interface              |
| Postcondition |            Customer fidelity card points updated             	     |

##### Scenario 1.5 - Customer uses POS

| Scenario 1.5  |                                                      |
| ------------- | :--------------------------------------------------: |
| Precondition  | Customer has debit card, credit card or prepaid card |
|  1     	| The Barcode reader reads the product barcode 	        |
|  2     	| The System loads product information 		        |
|  3    	| The System loads current price 		        |
|  4 		| The Cashier selects product quantity 		        |
|  5 		| Repeat until all products have been added 	        |
|  6 		| The Cashier selects "Compute total" 		        |
|  7            |       The Cashier selects "POS payment"               |
|  8            |          Wait for customer operation on POS           |
|  9            |                Payment gets validated                 |
|  10           |               The Cashier selects "Print receipt"     |
|  11           |                  Receipt is printed                   |
|  12           |            The System stores transaction in database  |
|  13           |               The System checks coupon requisites     |
|  14           |               The Cashier selects "End transaction"   |
|  15           |          The System updates item quantity in inventory|
|  16           |               The System resets cashier interface     |
| Postcondition |       Customer's card or bank account updated         |

##### Scenario 1.6 - Customer's card is declined or POS transaction is canceled

| Scenario 1.6  |                                                      |
| ------------- | :--------------------------------------------------: |
| Precondition  | Customer has debit card, credit card or prepaid card |
|  1     	| The Barcode reader reads the product barcode 	       |
|  2     	| The System loads product information 		       |
|  3    	| The System loads current price 		       |
|  4 		| The Cashier selects product quantity 		       |
|  5 		| Repeat until all products have been added 	       |
|  6 		| The Cashier selects "Compute total" 		       |
|  7            |       The Cashier selects "POS payment"              |
|  8            |          Wait for customer operation on POS          |
|  9            |              POS operation is canceled               |
|  10           |The Casher inserts the customer handed cash amount    |
|  11           |The System shows the computed change                  |
|  12           |The Cashier selects "Open cash drawer"                |
|  13           |Cash drawer is open                                   |
|  14           |The Cashier selects "Print receipt"                   |
|  15           |Receipt is printed                      	       |
|  16           |The System stores transaction in database             |
|  17           |The System checks coupon requisites                   |
|  18           |The Cashier selects "End transaction"                 |
|  19           |The System updates item quantity in inventory         |
|  20           |The System resets cashier interface                   |
| Postcondition |     Customer's card or bank account not updated      |

##### Scenario 1.7 - Customer has valid coupons

| Scenario 1.7 	|                                           |
| ------------ 	| :---------------------------------------: |
| Precondition 	|        Customer has valid coupons         |
|  1     	| The Barcode reader reads the product barcode 	        |
|  2     	| The System loads product information 		        |
|  3    	| The System loads current price 		        |
|  4 		| The Cashier selects product quantity 		        |
|  5 		| Repeat until all products have been added 	        |
|  6           	| The Barcode reader reads coupon barcode  		|
|  7           	| The System checks coupon's expiration date  		|
|  8           	|          Repeat for each coupon           		|
|  9            | The Cashier selects "Compute total"          		|
|  10           |The Casher inserts the customer handed cash amount    	|
|  11           |The System shows the computed change                  	|
|  12           |The Cashier selects "Open cash drawer"                	|
|  13           |Cash drawer is open                                   	|
|  14           |The Cashier selects "Print receipt"                   	|
|  15           |Receipt is printed                      	       	|
|  16           |The System stores transaction in database             	|
|  17           |The System checks coupon requisites                   	|
|  18           |The Cashier selects "End transaction"                 	|
|  19           |The System updates item quantity in inventory         	|
|  20           |The System resets cashier interface                   	|

##### Scenario 1.8 - Customer changes idea on some products

| Scenario 1.8  |                                                       |
| ------------- | :---------------------------------------------------: |
| 1     	| The Barcode reader reads the product barcode 	        |
| 2     	| The System loads product information 		        |
| 3	    	| The System loads current price 		        |
| 4 		| The Cashier selects product quantity 		        |
| 5 		| Repeat until all products have been added 	        |
| 6             |  The Cashier deletes product from list                |
| 7             | Repeat until all undesired products have been removed |
| 8            	| The Cashier selects "Compute total"          		|
| 9           	|The Casher inserts the customer handed cash amount    	|
| 10           	|The System shows the computed change                  	|
| 11           	|The Cashier selects "Open cash drawer"                	|
| 12           	|Cash drawer is open                                   	|
| 13           	|The Cashier selects "Print receipt"                   	|
| 14           	|Receipt is printed                      	       	|
| 15           	|The System stores transaction in database             	|
| 16           	|The System checks coupon requisites                   	|
| 17           	|The Cashier selects "End transaction"                 	|
| 18           	|The System updates item quantity in inventory         	|
| 19           	|The System resets cashier interface                   	|
| Postcondition |        Transaction has only the desired items         |

##### Scenario 1.9 - Customer cancels transaction

| Scenario 1.9  |                                           |
| ------------- | :---------------------------------------: |
| 1     	| The Barcode reader reads the product barcode 	        |
| 2     	| The System loads product information 		        |
| 3	    	| The System loads current price 		        |
| 4 		| The Cashier selects product quantity 		        |
| 5 		| Repeat until all products have been added 	        |
| 6             |         The Cashier selects "Compute total"           |
| 7           	|The Cashier selects "End transaction"                 	|
| 8             |The System resets cashier interface 	                |
| Postcondition | Item quantities not updated in inventory  |

##### Scenario 1.10 - Out of receipt paper

| Scenario 1.10 |                                                   |
| ------------- | :-----------------------------------------------: |
| 1     	| The Barcode reader reads the product barcode 	        |
| 2     	| The System loads product information 		        |
| 3	    	| The System loads current price 		        |
| 4 		| The Cashier selects product quantity 		        |
| 5 		| Repeat until all products have been added 	        |
| 6            	| The Cashier selects "Compute total"          		|
| 7           	|The Casher inserts the customer handed cash amount    	|
| 8           	|The System shows the computed change                  	|
| 9           	|The Cashier selects "Open cash drawer"                	|
| 10           	|Cash drawer is open                                   	|
| 11           	|The Cashier selects "Print receipt"                   	|
| 12            |The System displays message "Receipt printer is out of paper" |
| 13            |                 The Cashier changes paper roll        |
| 14           	|The Cashier selects "Print receipt"                   	|
| 15           	|Receipt is printed                      	       	|
| 16           	|The System stores transaction in database             	|
| 17           	|The System checks coupon requisites                   	|
| 18           	|The Cashier selects "End transaction"                 	|
| 19           	|The System updates item quantity in inventory         	|
| 20           	|The System resets cashier interface                   	|

##### Scenario 1.11 - Out of receipt ink

| Scenario 1.11 |                                                 |
| ------------- | :---------------------------------------------: |
| 1     	| The Barcode reader reads the product barcode 	        |
| 2     	| The System loads product information 		        |
| 3	    	| The System loads current price 		        |
| 4 		| The Cashier selects product quantity 		        |
| 5 		| Repeat until all products have been added 	        |
| 6            	| The Cashier selects "Compute total"          		|
| 7           	|The Casher inserts the customer handed cash amount    	|
| 8           	|The System shows the computed change                  	|
| 9           	|The Cashier selects "Open cash drawer"                	|
| 10           	|Cash drawer is open                                   	|
| 11           	|The Cashier selects "Print receipt"                   	|
| 12            |The System displays message "Receipt printer is out of ink" |
| 13            |                 The Cashier changes paper roll        |
| 14           	|The Cashier selects "Print receipt"                   	|
| 15           	|Receipt is printed                      	       	|
| 16           	|The System stores transaction in database             	|
| 17           	|The System checks coupon requisites                   	|
| 18           	|The Cashier selects "End transaction"                 	|
| 19           	|The System updates item quantity in inventory         	|
| 20           	|The System resets cashier interface                   	|

##### Scenario 1.12 - Customer asks for invoice

| Scenario 1.12 |                                           |
| ------------- | :---------------------------------------: |
| 1     	| The Barcode reader reads the product barcode 	        |
| 2     	| The System loads product information 		        |
| 3	    	| The System loads current price 		        |
| 4 		| The Cashier selects product quantity 		        |
| 5 		| Repeat until all products have been added 	        |
| 6            	| The Cashier selects "Compute total"          		|
| 7           	|The Casher inserts the customer handed cash amount    	|
| 8           	|The System shows the computed change                  	|
| 9           	|The Cashier selects "Open cash drawer"                	|
| 10           	|Cash drawer is open                                   	|
| 11           	|The Cashier selects "Print receipt"                   	|
| 12            |            Receipt is printed             		|
| 13           	|The System stores transaction in database             	|
| 14           	|The System checks coupon requisites                   	|
| 15            |          The Cashier selects "Print invoice"          |
| 16            |            Invoice is printed             		|
| 17           	|The Cashier selects "End transaction"                 	|
| 18           	|The System updates item quantity in inventory         	|
| 19           	|The System resets cashier interface                   	|

##### Scenario 1.13 - Customer has spent enough to get a coupon

| Scenario 1.13 |                                           |
| ------------- | :---------------------------------------: |
| 1     	| The Barcode reader reads the product barcode 	        |
| 2     	| The System loads product information 		        |
| 3	    	| The System loads current price 		        |
| 4 		| The Cashier selects product quantity 		        |
| 5 		| Repeat until all products have been added 	        |
| 6            	| The Cashier selects "Compute total"          		|
| 7           	|The Casher inserts the customer handed cash amount    	|
| 8           	|The System shows the computed change                  	|
| 9           	|The Cashier selects "Open cash drawer"                	|
| 10           	|Cash drawer is open                                   	|
| 11           	|The Cashier selects "Print receipt"                   	|
| 12            |            Receipt is printed             		|
| 13           	|The System stores transaction in database             	|
| 14           	|The System checks coupon requisites                   	|
| 15            |          UC17 - Generate coupon          	 	|
| 16           	|The Cashier selects "End transaction"                 	|
| 17           	|The System updates item quantity in inventory         	|
| 18           	|The System resets cashier interface                   	|

##### Scenario 1.14 - Cashier adds product to list from "Quick access products"

| Scenario 1.14 |                                           |
| ------------- | :---------------------------------------: |
| 1     	| The Barcode reader reads the product barcode 	        |
| 2     	| The System loads product information 		        |
| 3	    	| The System loads current price 		        |
| 4 		| The Cashier selects product quantity 		        |
| 5 		| Repeat until all products have been added 	        |
| 6             |   The Cashier selects "Open 'Quick access products'"  |
| 7             |        The Cashier selects products from list         |
| 8             |The Cashier selects the quantity of the product        |
| 9            	| The Cashier selects "Compute total"          		|
| 10           	|The Casher inserts the customer handed cash amount    	|
| 11           	|The System shows the computed change                  	|
| 12           	|The Cashier selects "Open cash drawer"                	|
| 13           	|Cash drawer is open                                   	|
| 14           	|The Cashier selects "Print receipt"                   	|
| 15            |            Receipt is printed             		|
| 16           	|The System stores transaction in database             	|
| 17           	|The System checks coupon requisites                   	|
| 18           	|The Cashier selects "End transaction"                 	|
| 19           	|The System updates item quantity in inventory         	|
| 20           	|The System resets cashier interface                   	|

##### Scenario 1.15 - Cashier manually inputs some products codes

| Scenario 1.15 |                                           |
| ------------- | :---------------------------------------: |
| 1             |        The Cashier inserts manually the product code        	|
| 2       	| The System loads product information             	 	|
| 3    		| The System loads current price             			|
| 4  		| The Cashier selects product quantity             		|
| 5   		| Repeat until all products have been added           		|
| 6            	| The Cashier selects "Compute total"             	 	|
| 7           	|The Casher inserts the customer handed cash amount      	|
| 8           	|The System shows the computed change                    	|
| 9           	|The Cashier selects "Open cash drawer"                  	|
| 10           |Cash drawer is open                                     	|
| 11           |The Cashier selects "Print receipt"                     	|
| 12           |Receipt is printed                                 		|
| 13           |The System stores transaction in database               	|
| 14           |The System checks coupon requisites                     	|
| 15           |The Cashier selects "End transaction"                   	|
| 16           |The System updates item quantity in inventory           	|
| 17           |The System resets cashier interface                     	|

##### Scenario 1.16 - Cashier insert a coupon not valid

| Scenario 1.16 |                                                          |
| ------------- | :------------------------------------------------------: |
| 1             |       The Barcode reader reads the product barcode       |
| 2             |         The System loads the product information         |
| 3             |              The System loads current price              |
| 4             |           The Cashier selects product quantity           |
| 5             |        Repeat until all products have been added         |
| 6             |         The Barcode reader reads coupon barcode          |
| 7             |        The System checks coupon's expiration date        |
| 8             | The System displays the error message "Coupon not valid" |
| 9             |           The Cashier selects "Compute total"            |
| 10            |   The Cashier inserts the customer handed cash amount    |
| 11            |         The System displays the computed change          |
| 12            |          The cashier selects "Open cash drawer"          |
| 13            |                   Cash drawer is open                    |
| 14            |           The Cashier selects "Print receipt"            |
| 15            |                    Receipt is printed                    |
| 16            |        The System stores transaction in database         |
| 17            |           The system checks coupon requisites            |
| 18            |          The cashier selects "End transaction"           |
| 19            |      The System updates item quantity in inventory       |
| 20            |           The System resets cashier interface            |

### UC2 - User login

| Actors Involved  |                 Cashier OR Manager OR Owner                  |
| ---------------- | :----------------------------------------------------------: |
| Precondition     |          The user's account exists in the database           |
| Post condition   |                User's account data is loaded                 |
| Nominal Scenario | User correctly inputs username and passwords, and requests access on a terminal with a role they have permission to use |
| Variants         | User might input wrong username/passwords, and if they do so for three consecutive times their account gets blocked, and they'll have to contact the owner or manager to have it reenabled. A cashier might try to log in the administrator terminal, which they don't have permission to use. User can also use their personal barcode instead of inputting the username. User might forget the password, and they can recover it notifying the manager or owner. |

##### Scenario 2.1 - NOMINAL

| Scenario 2.1 |                                               |
| ------------ | :-------------------------------------------: |
| 1            |          The User inserts username and password        |
| 2            |          The User selects "Log In" button             	|
| 3            |          The System searches user in database          |
| 4            |              The User is validated               	|
| 5            | User data and role functionalities are loaded 		|
| 6            |                 GUI is loaded                 		|

##### Scenario 2.2 - Incorrect username or password

| Scenario 2.2 |                                                        |
| ------------ | :----------------------------------------------------: |
| 1            |          The User inserts username and password        		|
| 2            |          The User selects "Log In" button             			|
| 3            |          The System searches user in database          		|
| 4            |              The User is not validated               			|
| 5            | The System displays error message "Incorrect username or password" 	|

##### Scenario 2.3 - Cashier tries to log in on owner/manager terminal

| Scenario 2.3 |                                               |
| ------------ | :-------------------------------------------: |
| Actors       |                    Cashier                    |
| Precondition |   Interface used is an administrator client   |
| 1            |          The Cashier inserts username and password        		|
| 2            |          The Cashier selects "Log In" button          			|
| 3            |          The System searches user in database          		|
| 4            |              The User is validated               			|
| 5            | Role is recognized as wrong for the terminal  				|
| 6            |              User is logged out by the system               		|
| 7            |      The System displays error message "Wrong role"       		|

##### Scenario 2.4 - User uses personal barcode instead of username

| Scenario 2.4 |                                               |
| ------------ | :-------------------------------------------: |
| Actors       |                Barcode Reader                 |
| 1            |   The Barcode reader reads personal barcode   |
| 2            |    The System searches barcode in databse     |
| 3            |           The System loads username           |
| 4            |           The User inserts password           |
| 5            |           The User selects "Log In"           |
| 6            |     The System searches user in database      |
| 7            |               User is validated               |
| 8            | User data and role functionalities are loaded |
| 9            |                 GUI is loaded                 |

##### Scenario 2.5 - Barcode not found

| Scenario 2.5 |                                                         |
| ------------ | :-----------------------------------------------------: |
| Actors       |                     Barcode Reader                      |
| 1            |        The Barcode reader reads personal barcode        |
| 2            |         The System searches barcode in databse          |
| 3            | The System displays the error message "Invalid barcode" |

##### Scenario 2.6 - Wrong password inserted three consecutive times

| Scenario 2.6  |                                                              |
| ------------- | :----------------------------------------------------------: |
| 1            |          The User inserts username and password       				|
| 2            |          The User selects "Log In" button             				|
| 3            |          The System searches user in database          			|
| 4             |                    User is not validated                     			|
| 5             |    The System displays error message "Incorrect username or password"    	|
| 6             |                    It is repeated two more times                  	   	|
| 7             |                     Manager is notified by the System                     	|
| 8             |                      Account is blocked by the System                      |
| 9 | Display message "Wrong password too many times, account disabled" |
| 10 | Recovery email is automatically sent to User mail address |
| Postcondition | The account is blocked until the manager or owner intervenes, and the manager has been notified |

##### Scenario 2.7 - User forgets password

| Scenario 2.7 |                                       |
| ------------ | :-----------------------------------: |
| Actors | Email Service Provider |
| 1            |       The User selects "Recover password"      	|
| 2            |        The User inserts username or email      	|
| 3            | The User selects "Send Email" 				|
| 3            | The System sends email with user password to user 	|

### UC3 - User logout

| Actors Involved  |                Cashier OR Manager OR Owner                |
| ---------------- | :-------------------------------------------------------: |
| Precondition     |               User's account data is loaded               |
| Post condition   |              User's account data is unloaded              |
| Nominal Scenario | The user selects "Log out" and all their data is unloaded |
| Variants         |                           None                            |

##### Scenario 3.1 - NOMINAL

| Scenario 3.1 |                                 |
| ------------ | :-----------------------------: |
| 1            |        The User selects "Log out"      |
| 2            | User account's data is unloaded 	|
| 3            |      Log in GUI is loaded       	|

### UC4 - The System locks the cash register if cashier leaves temporarily

| Actors Involved  |                           Cashier                            |
| ---------------- | :----------------------------------------------------------: |
| Precondition     |      Cash register is active and completely functional       |
| Post condition   |                   Cash register is locked                    |
| Nominal Scenario | The cashier locks the cash register and leaves temporarily, the cash register is locked until the cashier comes back and unlocks it, a Log in GUI requesting the password only is loaded |
| Variants         | The cashier might try to lock the cash register while the drawer is open, in which case an error message is displayed |

##### Scenario 4.1 - NOMINAL

| Scenario 4.1 |                                                              |
| ------------ | :----------------------------------------------------------: |
| 1            |                 The Cashier selects "Lock"                	|
| 2            | Cash register is locked and all functionalities are disabled 	|
| 3            |        Log in GUI is loaded, only requesting password        	|

##### Scenario 4.2 - Cashier attempts to lock the register while the drawer is open

| Scenario 4.2 |                                                              |
| ------------ | :----------------------------------------------------------: |
| Precondition |                 Cash register drawer is open                 |
| 1            |                 The Cashier selects "Lock"               			   |
| 2            | The System displays error message "Cannot lock register while the drawer is open" |

### UC5 - The System unlocks the cash register when cashier returns

| Actors Involved  |                           Cashier                            |
| ---------------- | :----------------------------------------------------------: |
| Precondition     |                   Cash register is locked                    |
| Post condition   |      Cash register is active and completely functional       |
| Nominal Scenario | The cash register is unlocked, the cashier GUI is loaded and all their functionalities are active |
| Variants         | The cashier might input wrong password, and if this happens three times the account is blocked, the register remains locked and the manager and/or owner is notified |

##### Scenario 5.1 - NOMINAL

| Scenario 5.1 |                                                              |
| ------------ | :----------------------------------------------------------: |
| 1            |                     The User inserts password                     	 |
| 2            |                      The User is validated                      	 |
| 3            | Cash register is unlocked and all cashier functionalities are activated |

##### Scenario 5.2 - Cashier inputs wrong password

| Scenario 5.2 |                                        |
| ------------ | :------------------------------------: |
| 1            |          The User inserts password          |
| 2            |         The User is not validated          |
| 3            | The System displays error message "Wrong password" |

##### Scenario 5.3 - Cashier inputs wrong password three times

| Scenario 5.3  |                                                              |
| ------------- | :----------------------------------------------------------: |
| 1            |          The User inserts password          			|
| 2            |         The User is not validated          			|
| 3            | The System displays error message "Wrong password" 		|
| 4             |                    It is repeated two more times            	|
| 5             |               Manager and/or owner is notified by the System  |
| 6             |                    The User account blocked                   |
| Postcondition | The user's account is blocked until manager/owner intervenes, cash register remains locked, manager/owner is notified |

### UC6 - Cashier opens the cash drawer

| Actors Involved  |                          Cashier                          |
| ---------------- | :-------------------------------------------------------: |
| Precondition     | Cash register is unlocked, cash register drawer is closed |
| Post condition   |               Cash register drawer is open                |
| Nominal Scenario |             The cashier opens the cash drawer             |
| Variants         |                           None                            |

##### Scenario 6.1 - NOMINAL

| Scenario 6.1 |                           |
| ------------ | :-----------------------: |
| 1            | The Cashier selects "Open cash drawer" |
| 2            |     Cash drawer is open     		|

### UC7 - The User adds a new product to inventory

| Actors Involved  |                       Manager OR Owner                       |
| ---------------- | :----------------------------------------------------------: |
| Precondition     |                                                              |
| Post condition   |             New item added to inventory database             |
| Nominal Scenario | The user inputs the new item's data, and it gets added to the inventory database |
| Variants         | The product's data might be added via barcode, and the user might be trying to add a product that is already present in the database, in which case an error message gets displayed |

##### Scenario 7.1 - NOMINAL

| Scenario 7.1 |                                         |
| ------------ | :-------------------------------------: |
| 1            |        The User selects "Manage Inventory"        			|
| 2            |         The User selects "Add new Item"	     			|
| 3            |         The User inserts new Item's data    				|
| 4            |         The User selects "Confirm"            				|
| 5            | The System checks if product is already in database			|
| 6            |      The System displays confirmation message "Product insert"      	|
| 7            |      The System adds new entry to database       			|

##### Scenario 7.2 - Item already in inventory

| Scenario 7.2  |                                                  |
| ------------- | :----------------------------------------------: |
| 1            |        The User selects "Manage Inventory"        			|
| 2            |         The User selects "Add new Item"     				|
| 3            |         The User inserts new Item's data    				|
| 4            |         The User selects "Confirm"            				|
| 5            | The System checks if product is already in database			|
| 6            | The System displays error message "Product already in database" 	|
| Postcondition |          Database has not been updated           |

##### Scenario 7.3 - User inserts product's data via barcode

| Scenario 7.3 |                                                           |
| ------------ | :-------------------------------------------------------: |
| Actors       |                      Barcode Reader                       |
| 1            |            The User selects "Manage Inventory"            |
| 2            |              The User selects "Add new Item"              |
| 3            |         The Barcode reader reads product barcode          |
| 4            |             The User inserts new Item's data              |
| 5            |                The User selects "Confirm"                 |
| 6            |    The System checks if product is already in database    |
| 7            | The System displays confirmation message "Product insert" |
| 8            |           The System adds new entry to database           |

### UC8 - The User changes product quantity

| Actors Involved  |                       Manager OR Owner                       |
| ---------------- | :----------------------------------------------------------: |
| Precondition     |                  Product exists in database                  |
| Post condition   |            Product's quantity updated in database            |
| Nominal Scenario | The user chooses whether to add or to remove an input quantity from a specified product, and the database entry of the product gets updated accordingly |
| Variants         | The user might try to remove more products than available, therefore bringing the product's quantity below 0, which is not possible. In this case an error message is displayed, and the database is not updated. The user can also restock a product, increasing its quantity by a predefined value and adding the transaction with the supplier to the transactions database. The restock might be performed using a CSV file, which has a fixed structure. The CSV file can also contain both new products to be added to the inventory, and products to restock, and the database will be updated to consider both operations. In case of restocking there is no need to check the consistency of the quantity, because a restock always increases it |

##### Scenario 8.1 - NOMINAL

| Scenario 8.1 |                                 |
| ------------ | :-----------------------------: |
| 1            |    The User selects "Manage inventory"    	|
| 2            |    The User selects product          		|
| 3            | The User inserts the quantity to add or remove |
| 4            |    The User selects "Add" or "Remove"     	|
| 5            |      The System computes new quantity       	|
| 6            |  The System checks quantity (not below 0)   	|
| 7            | UC9 - Notify if below threshold 		|
| 8            |      The System updates product entry       	|

##### Scenario 8.2 - Inconsistent quantity

| Scenario 8.2  |                                                              |
| ------------- | :----------------------------------------------------------: |
| Precondition  | The variation would bring the quantity of the given product below 0 |
| 1            |    The User selects "Manage inventory"    					|
| 2            |    The User selects product          						|
| 3            | The User inserts the quantity to add or remove 				|
| 4            |    The User selects "Add" or "Remove"     					|
| 5            |      The System computes new quantity       					|
| 6            |  The System checks quantity (not below 0)   					|
| 7            | Display error message "Incorrect quantity, cannot have less than 0 items" 	|
| Postcondition |                Inventory database not updated                			|

##### Scenario 8.3 - Restock product

| Scenario 8.3 |                                      |
| ------------ | :----------------------------------: |
| Actors       |               Supplier               |
| 1            | The User selects "Manage inventory"  |
| 2            |       The User selects product       |
| 3            | The User inserts the quantity to add |
| 4            |      The User selects "Restock"      |
| 5            |   The System computes new quantity   |
| 6            |   The System updates product entry   |

##### Scenario 8.4 - Restock product via barcode

| Scenario 8.4 |                           |
| ------------ | :-----------------------: |
| 1            |    The User selects "Manage inventory"    					|
| 2            |    The Barcode reader reads the product barcode 				|
| 3            | The User inserts the quantity to add 		 				|
| 4            |     The User selects "Restock" 					     	|
| 5            |   The System computes new quantity 					   	|
| 6            |   The System updates product entry 				   		|

##### Scenario 8.5 - Restock products via CSV file

| Scenario 8.5 |                                                              |
| ------------ | :----------------------------------------------------------: |
| Precondition | All products in CSV file are already in the inventory database |
| Actors       |                           Supplier                           |
| 1            |             The User selects "Manage inventory"              |
| 2            |              The User selects "Upload CSV file"              |
| 3            |                The User selects the CSV file                 |
| 4            |                  The User selects supplier                   |
| 5            |                    The User selects "Add"                    |
| 6            |             The System loads data from CSV file              |
| 7            |           The System searchs products in database            |
| 8            |               The System computes new quantity               |
| 9            |               The System updates product entry               |
| 10           |           The System adds transaction to database            |

##### Scenario 8.6 - The User adds new products and restock products via CSV file

| Scenario 8.5 |                                                              |
| ------------ | :----------------------------------------------------------: |
| Precondition | Some products in CSV file are not in the inventory database  |
| Actors       |                           Supplier                           |
| 1            |             The User selects "Manage inventory"              |
| 2            |              The User selects "Upload CSV file"              |
| 3            |                The User selects the CSV file                 |
| 4            |                  The User selects supplier                   |
| 5            |                    The User selects "Add"                    |
| 6            |             The System loads data from CSV file              |
| 7            |           The System searchs products in database            |
| 8            | The System creates new products database entries, with quantity: 0 |
| 9            |               The System computes new quantity               |
| 10           |               The System updates product entry               |
| 11           |           The System adds transaction to database            |

### UC9 - The System notifies when a product is running low

| Actors Involved  |                                                              |
| ---------------- | :----------------------------------------------------------: |
| Precondition     | Product's quantity has just been updated and it dropped below specified threshold |
| Post condition   |             A notification is added to database              |
| Nominal Scenario | When a product's quantity gets updated and the new quantity is below the specified quantity threshold for the product, a new notification for the manager/owner is generated |
| Variants         |                             None                             |

##### Scenario 9.1 - NOMINAL

| Scenario 9.1 |                                    |
| ------------ | :--------------------------------: |
| 1            | The System checks product quantity in database |
| 2            | The System adds notification in database    	|

### UC10 - The User removes product from inventory database

| Actors Involved  |                       Manager OR Owner                       |
| ---------------- | :----------------------------------------------------------: |
| Precondition     |                Product is present in database                |
| Post condition   |             Product entry removed from database              |
| Nominal Scenario | The manager/owner removes a product from the database, all its data are canceled |
| Variants         |                             None                             |

##### Scenario 10.1 - NOMINAL

| Scenario 10.1 |                           |
| ------------- | :-----------------------: |
| 1             | The User selects "Manage inventory" 	|
| 2             | The User selects product       	|
| 3             | The User selects "X"  		|
| 4             | The System asks for confirmation    	|
| 5             | The User selects "Confirm"      	|
| 6             | The System shows confirmation message |
| 7             | The System removes product entry    	|

### UC11 - Browse and filter products

| Actors Involved  |                       Manager OR Owner                       |
| ---------------- | :----------------------------------------------------------: |
| Precondition     |                                                              |
| Post condition   |        Products that match the filters are displayed         |
| Nominal Scenario | The manager/owner can browse the inventory and filter it to view only products that meet the specified criteria |
| Variants         | The manager/owner can also search for a specific product by its ID or barcode. There might not be products that match the filters. After having selected a product, the manager/owner can print its barcode |

##### Scenario 11.1 - NOMINAL

| Scenario 11.1 |                                                         |
| ------------- | :-----------------------------------------------------: |
| 1             |                The User selects "Manage inventory"                	|
| 2             |                The User chooses filters                  		|
| 3             |                The User selects "Apply"                      		|
| 4             | The System searches the database for products that match the filters 	|
| 5             |         The System displays products that match the filters          	|

##### Scenario 11.2 - The User searchs for a given product via barcode

| Scenario 11.2 |                                                              |
| ------------- | :----------------------------------------------------------: |
| Actors        |                        Barcode Reader                        |
| 1             |             The User selects "Manage inventory"              |
| 2             |           The Barcode reader reads product barcode           |
| 3             | The System searches the database for products that has corresponding barcode |
| 4             |      The System displays product with specified barcode      |

##### Scenario 11.3 - No product found

| Scenario 11.3 |                                                         |
| ------------- | :-----------------------------------------------------: |
| 1             |                The User selects "Manage inventory"                	|
| 2             |                The User chooses filters                  		|
| 3             |                The User selects "Apply"                      		|
| 4             | The System searches the database for products that match the filters 	|
| 5             | The System displays message "No product matches the filters"     	|

##### Scenario 11.4 - The User prints product barcode

| Scenario 11.4 |                                                         |
| ------------- | :-----------------------------------------------------: |
| 1             |                The User selects product                 |
| 2             |             The User selects "Print product barcode"    |
| 3             |               Product barcode is printed                |

### UC12 - The User adds a fidelity card

| Actors Involved  |              Cashier, Customer, Barcode Reader               |
| ---------------- | :----------------------------------------------------------: |
| Precondition     |                                                              |
| Post condition   |         New fidelity card entry is added in databse          |
| Nominal Scenario | The user inputs the customer's data and a new fidelity card is issued and added to the database |
| Variants         | The same operation can be performed by the manager in their GUI. A customer might have lost their card and ask for a new one, in which case the original fidelity card entry is modified, instead of creating a new one |

##### Scenario 12.1 - NOMINAL

| Scenario 12.1 |                                        |
| ------------- | :------------------------------------: |
| 1             |       The User selects "Add fidelity card"       	|
| 2             |        The User inserts customer tax code         	|
| 3             |       The Barcode reader scans fidelity card barcode  |
| 4             | The User selects "Confirm" 				|
| 5             | The System creates fidelity card entry in database 	|
| 6             |      The System displays confirmation message      	|
| 7             |                 The System updates the database       |

##### Scenario 12.2 - Manager adds new fidelity card from their GUI

| Scenario 12.2 |                                     |
| ------------- | :---------------------------------: |
| 1             |     The Manager selects "Add fidelity card"      	|
| 2             |      The Manager inserts customer tax code       	|
| 3             |     The Barcode reader scans fidelity card barcode    |
| 4             | The Manager selects "Confirm" 			|
| 5             |    The System displays confirmation message     	|
| 6             | The System adds fidelity card entry in database 	|

##### Scenario 12.3 - Customer has lost fidelity card and asks for a new one

| Scenario 12.3 |                                                              |
| ------------- | :----------------------------------------------------------: |
| Precondition  | There is a fidelity card entry with customer's tax code in database |
| 2             |                  The User selects "Add fidelity card"                  		|
| 3             |                  The User inserts customer tax code                    		|
| 4             | The System displays message "Tax code already associated to existing fidelity card." 	|
| 5             |                    The User selects "Update entry"                     		|
| 6             |                  The Barcode reader scans fidelity card barcode                  	|
| 8             |                  The System shows confirmation message                   		|
| 9             |                  The System updates fidelity card entry                  		|

### UC13 - Renew expired fidelity cards

| Actors Involved  |                                                              |
| ---------------- | :----------------------------------------------------------: |
| Precondition     | Fidelity card has reached expiration date, new day starts or system is started |
| Post condition   | Expired fidelity cards have new expiration date, one year from present day, and 0 points, and "Unnotified" flag is set to 1 |
| Nominal Scenario | Expired cards get automatically renewed when the day following the expiration date starts, or the system is started for the first time after the expiration date. The cards' "Unnotified" flag is set to 1 |
| Variants         |                             None                             |

##### Scenario 13.1 - NOMINAL

| Scenario 13.1 |                                                              |
| ------------- | :----------------------------------------------------------: |
| 1             |           The System scans database for expired fidelity cards           			|
| 2             |         The System sets each expired fidelity card's points to 0         			|
| 3             | The System sets each expired fidelity card's expiration date to one year from present day 	|

### UC14 - User  adds a new cash register

| Actors Involved  |                       Manager OR Owner                       |
| ---------------- | :----------------------------------------------------------: |
| Precondition     | Cash register connected to the same private network of the system |
| Post condition   |             New cash register entry in database              |
| Nominal Scenario | The user prompts the system to look for cash registers that are in the same private network but not in the database, connect them and communicate them their ID, so that they can save it in their "Settings" file |
| Variants         |                             None                             |

##### Scenario 14.1 - NOMINAL

| Scenario 14.1 |                                                              |
| ------------- | :----------------------------------------------------------: |
| 1             |                The User selects "System management"	                		  |
| 2             |                The User selects "Manage cash registers"                		  |
| 3             |                The User selects "Add new cash register"              			  |
| 4             |      The System searches for cash registers on the private networks     		  |
| 5             |          The System searches for cash registers in the database           		  |
| 6             | The System compares lists to find available cash registers that are not in the database |
| 7             |       The System displays unconnected cash registers' IP addresses     		  |
| 8             |     The User selects cash registers' IP addresses to add from list   			  |
| 9             |                    The USer selects "Add to system"                  			  |
| 10            |            The System adds cash registers entries in database          		  |
| 11            |     The System communicates to each newly added cash register its ID   		  |
| 12            | Each newly added cash register saves its ID in its "Settings" file			  |
| 13            |                 The System displays confirmation message               		  |

### UC15 - The User connects cash register

| Actors Involved  |                       Manager OR Owner                       |
| ---------------- | :----------------------------------------------------------: |
| Precondition     | Cash register connected to the same private network of the system, cash register entry already in database |
| Post condition   |     Cash register connected to the system and operative      |
| Nominal Scenario | After having added the cash register to the system, the user can connect it to system in order to make it fully operational and ready to be used |
| Variants         | The user may request a connection from cash register that is not present in the database, which would generate an error message. The user may also disconnect the cash register |

##### Scenario 15.1 - NOMINAL

| Scenario 15.1 |                                                            |
| ------------- | :--------------------------------------------------------: |
| 1             |               Cash register is powered on                		|
| 2             |             The Cas Regiser sends a request connection to the server	|
| 3             | The System  searches for the cash register's ID in the database 	|
| 4             |                     The System confirms connection                    |

##### Scenario 15.2 - Connection requested from cash register not in database

| Scenario 15.2 |                                                            |
| ------------- | :--------------------------------------------------------: |
| 1             |               Cash register is powered on                		|
| 2             |             The Cas Regiser sends a request connection to the server	|
| 3             | The System  searches for the cash register's ID in the database 	|
| 4             |             The System displays "Connection error" message            |

##### Scenario 15.3 - Cash register is disconnected from the system

| Scenario 15.3 |                                           |
| ------------- | :---------------------------------------: |
| Precondition  |         Cash register is active         |
| 1             | The Cash register receives disconnection message from server 	|
| 2             |       The Cash register is disconnected        		|
| 3             |      The System displays "Disconnected" message       	|

### UC16 - The User removes the cash register

| Actors Involved  |                      Manager OR Owner                      |
| ---------------- | :--------------------------------------------------------: |
| Precondition     |              Cash register entry in database               |
| Post condition   |         Cash register entry removed from database          |
| Nominal Scenario | The user removes the cash register entry from the database |
| Variants         |                            None                            |

##### Scenario 16.1 - NOMINAL

| Scenario 16.1 |                                                |
| ------------- | :--------------------------------------------: |
| 1             |         The User selects "System management"	        |
| 2             |         The User selects "Manage cash registers"      |
| 3             |         The User selects cash register to remove      |
| 4             |                Select "X"                 		|
| 5             |         The System asks for confirmation              |
| 6             |         The User selects "Confirm"                	|
| 7             | The System disconnects selected cash register if connected |
| 8             |        The System removes cash register entry           |
| 9             |          The System displays confirmation message          |

### UC17 - The User generates a coupon

| Actors Involved  |                  Cashier, Regular customer                   |
| ---------------- | :----------------------------------------------------------: |
| Precondition     | Customer has just completed a transaction sufficient for a coupon, discounts are enabled in "Settings" file |
| Post condition   |                   New coupon is generated                    |
| Nominal Scenario | If the customer has completed a transactions that qualifies them for a coupon, the coupon is generated according to the specified ranges in the "Settings" file |
| Variants         |                             None                             |

##### Scenario 17.1 - NOMINAL

| Scenario 17.1 |                                                              |
| ------------- | :----------------------------------------------------------: |
| 1             | The System compares the total amount of the transaction to the price ranges to determine coupon value |
| 2             | The System sets coupon expiring date to a given number of days from present day 			|
| 3             |                   The System generates coupon barcode                    				|
| 4             |                     The System prints coupon barcode                     				|

### UC18 - The User configures coupon

| Actors Involved  |                    Manager OR Owner                    |
| ---------------- | :----------------------------------------------------: |
| Precondition     |                                                        |
| Post condition   |         Database and settings file are updated         |
| Nominal Scenario | The user configures the coupon settings and enables it |
| Variants         |   The user may choose to deactivate coupon discounts   |

##### Scenario 18.1 - NOMINAL

| Scenario 18.1 |                                                 |
| ------------- | :---------------------------------------------: |
| 1             |                The User selects "Settings"                |
| 2             |           The User selects "Configure coupons"            |
| 3             |             The User checks "Enable" checkbox             |
| 4             |         The User inserts minimum expense threshold        |
| 5             |              The User inserts coupon value                |
| 6             | The User inserts number of days from issuing to expiration|
| 7             |          The User selects "Save coupon settings"          |
| 8             |               The System updates coupon entry             |
| 9             |         The System updates "Coupons settings" file        |
| 10            |          The System displays confirmation message         |

##### Scenario 18.2 - The User deactivates coupon discounts

| Scenario 18.2 |                                |
| ------------- | :----------------------------: |
| 1             |       The User selects "Settings"        		    |
| 2             |           The User selects "Configure coupons"            |
| 3             |             The User checks "Enable" checkbox             |
| 4             |          The User selects "Save coupon settings"          |
| 5             |         The System updates "Coupons settings" file        |
| 6             |          The System displays confirmation message         |

##### Scenario 18.3 - The User removes a coupon rule

| Scenario 18.3 |                                              |
| ------------- | :------------------------------------------: |
| 1             |         The User selects "Settings"          |
| 2             |     The User selects "Configure coupons"     |
| 3             | The user press "remove" for the desired rule |
| 4             |            The User selects "Yes"            |
| 5             |  The System updates "Coupons settings" file  |
| 6             |   The System displays confirmation message   |

### UC19 - The User adds account

| Actors Involved  |                       Manager OR Owner                       |
| ---------------- | :----------------------------------------------------------: |
| Precondition     |                                                              |
| Post condition   |                New account entry in database                 |
| Nominal Scenario |                 The user adds a new account                  |
| Variants         | The user may try to add an account with a tax code already associated to another account, or to add an account with a role that they have not the permission to add. Both of these operations generate error messages |

##### Scenario 19.1 - NOMINAL

| Scenario 19.1 |                              |
| ------------- | :--------------------------: |
| 1             |  The User selects "Manage employees"     |
| 2             |   The User selects "Add new employee"    |
| 3             |      The User inserts employee data      |
| 4             |     The User selects employee role       |
| 5             |       The User selects "Confirm"         |
| 6             | The System adds new account to database  |
| 7             | The System displays confirmation message |

##### Scenario 19.2 - New account's tax code already present in database

| Scenario 19.2 |                                                              |
| ------------- | :----------------------------------------------------------: |
| 1             |  The User selects "Manage employees"     						|
| 2             |   The User selects "Add new employee"    						|
| 3             |      The User inserts employee data      						|
| 4             |     The User selects employee role       						|
| 5             |       The User selects "Confirm"         						|
| 6             | The System displays error message "Tax code already associated to an existing account"|

##### Scenario 19.3 - Manager tries to create a new Manager account

| Scenario 19.3 |                                                              |
| ------------- | :----------------------------------------------------------: |
| Precondition  |                      User is a Manager                       |
| 1             |  The User selects "Manage employees"     								|
| 2             |   The User selects "Add new employee"    								|
| 3             |      The User inserts employee data      								|
| 4             |     The User selects employee role       								|
| 5             |       The User selects "Confirm"         								|
| 6             | The System displays error message "This account doesn't have permission to create a Manager account"  |

##### Scenario 19.4 - Manager tries to create a new Owner account

| Scenario 19.4 |                                                              |
| ------------- | :----------------------------------------------------------: |
| Precondition  |                      User is a Manager                       |
| 1             |  The User selects "Manage employees"     								|
| 2             |   The User selects "Add new employee"    								|
| 3             |      The User inserts employee data      								|
| 4             |     The User selects employee role       								|
| 5             |       The User selects "Confirm"         								|
| 6             | The System displays error message "This account doesn't have permission to create an Owner account"   |

### UC20 - The User removes account

| Actors Involved  |                       Manager OR Owner                       |
| ---------------- | :----------------------------------------------------------: |
| Precondition     |              Account entry present in database               |
| Post condition   |             Account entry removed from database              |
| Nominal Scenario |             The user removes an existing account             |
| Variants         | The user may try to remove an account with a role that they have not the permission to remove, generating an error messages |

##### Scenario 20.1 - NOMINAL

| Scenario 20.1 |                              |
| ------------- | :--------------------------: |
| 1             |  The User selects "Manage employees"		|
| 2             |   The User selects account to remove   	|
| 3             |   The User selects "Remove discount"   |
| 4             |     The System asks for confirmation     	|
| 5             |       The User selects "Confirm"       	|
| 6             |  The System deletes entry from database  	|
| 7             | The System displays confirmation message 	|

##### Scenario 20.2 - Manager tries to remove a Manager account

| Scenario 20.2 |                                                              |
| ------------- | :----------------------------------------------------------: |
| Precondition  |                      User is a Manager                       |
| 1             |             The User selects "Manage employees"              |
| 2             |              The User selects account to remove              |
| 3             |              The User selects "Remove discount"              |
| 4             |               The System asks for confirmation               |
| 5             |                  The User selects "Confirm"                  |
| 6             | The System displays error message "This account doesn't have permission to remove a Manager account" |

##### Scenario 20.3 - Manager tries to remove an Owner account

| Scenario 20.3 |                                                              |
| ------------- | :----------------------------------------------------------: |
| Precondition  |                      User is a Manager                       |
| 1             |             The User selects "Manage employees"              |
| 2             |              The User selects account to remove              |
| 3             |              The User selects "Remove discount"              |
| 4             |               The System asks for confirmation               |
| 5             |                  The User selects "Confirm"                  |
| 6             | The System displays error message "This account doesn't have permission to remove an Owner account" |

### UC21 - The User updates account

| Actors Involved  |                       Manager OR Owner                       |
| ---------------- | :----------------------------------------------------------: |
| Precondition     |              Account entry present in database               |
| Post condition   |              Account entry updated in database               |
| Nominal Scenario |        The user edits and updates an existing account        |
| Variants         | The user may try to edit an account with a role that they have not the permission to edit, generating an error messages. The user may reenable an account that has been blocked after three wrong passwords |

##### Scenario 21.1 - NOMINAL

| Scenario 21.1 |                              |
| ------------- | :--------------------------: |
| 1             |  The User selects "Manage employees"    |
| 2             |   The User selects account to modify    |
| 3             |   The User selects "Modify employee"    |
| 4             |  The User modifies account information  |
| 5             |       The User selects "Confirm"        |
| 6             |   The System updates entry in database  |
| 7             | The System displays confirmation message|

##### Scenario 21.2 - Manager tries to modify a Manager account

| Scenario 21.2 |                                                              |
| ------------- | :----------------------------------------------------------: |
| Precondition  |                      User is a Manager                       |
| 1             |  The User selects "Manage employees"    								|
| 2             |   The User selects account to modify    								|
| 3             |   The User selects "Modify employee"    								|
| 4             |  The User modifies account information  								|
| 5             |       The User selects "Confirm"        								|
| 6             | The System displays error message "This account doesn't have permission to modify a Manager account" 	|
| Postcondition |                   Database is not updated                    |

##### Scenario 21.3 - Manager tries to modify an Owner account

| Scenario 21.3 |                                                              |
| ------------- | :----------------------------------------------------------: |
| Precondition  |                      User is a Manager                       |
| 1             |  The User selects "Manage employees"    								|
| 2             |   The User selects account to modify    								|
| 3             |   The User selects "Modify employee"    								|
| 4             |  The User modifies account information  								|
| 5             |       The User selects "Confirm"        								|
| 6             | The System displays error message "This account doesn't have permission to modify an Owner account" 	|
| Postcondition |                   Database is not updated                    |

##### Scenario 21.4 - Manager tries to set an account role to Manager

| Scenario 21.4 |                                                              |
| ------------- | :----------------------------------------------------------: |
| Precondition  |                      User is a Manager                       |
| 1             |  The User selects "Manage employees"    								|
| 2             |   The User selects account to modify    								|
| 3             |   The User selects "Modify employee"    								|
| 4             |  The User modifies account information  								|
| 5             |       The User set the account role to "Manager"        						|
| 6             |       The User selects "Confirm"        								|
| 7             | The System displays error message "This account doesn't have permission to change the role of an account to Manager" |
| Postcondition |                   Database is not updated                    |

##### Scenario 21.5 - Manager tries to set an account role to Owner

| Scenario 21.5 |                                                              |
| ------------- | :----------------------------------------------------------: |
| Precondition  |                      User is a Manager                       |
| 1             |  The User selects "Manage employees"    								|
| 2             |   The User selects account to modify    								|
| 3             |   The User selects "Modify employee"    								|
| 4             |  The User modifies account information  								|
| 5             |       The User set the account role to "Manager"        						|
| 6             |       The User selects "Confirm"        								|
| 7             | The System displays error message "This account doesn't have permission to change the role of an account to Owner" |
| Postcondition |                   Database is not updated                    |

##### Scenario 21.6 - The User reenables account after three wrong passwords

| Scenario 21.6 |                                                       |
| ------------- | :---------------------------------------------------: |
| Precondition  | Account has been disabled after three wrong passwords |
| 1             |  The User selects "Manage employees"    	       		|
| 2             |             The User selects the disabled account             |
| 4             |               The User checks "Enable"              	 	|
| 5             |                 The System asks for confirmation              |
| 6             |                   The User selects "Confirm"                  |
| 7             |           The System updates account entry in database        |

### UC22 - The User adds product discount

| Actors Involved  |                       Manager OR Owner                       |
| ---------------- | :----------------------------------------------------------: |
| Precondition     |                                                              |
| Post condition   |               Discount is created in database                |
| Nominal Scenario |      The user adds a new discount for selected products      |
| Variants         | The user may add a discount that has an expiration date. The user may try to add a discount for products that already have one, overwriting the previous discount if the operation is confirmed |

##### Scenario 22.1 - NOMINAL

| Scenario 22.1 |                                        |
| ------------- | :------------------------------------: |
| 1             |        The User selects "Manage Inventory"        	|
| 2             |            The User selects product             	|
| 3             |         The User selects the price to discount      	|
| 4             |       The User inserts discount percentage        	|
| 5             |            The User selects "Confirm"            	|
| 6             | The System checks if product is already discounted 	|
| 7             |     The System updates product's current price     	|
| 8             |      The System displays confirmation message      	|

##### Scenario 22.2 - The User adds a discount with expiration date

| Scenario 22.2 |                                        |
| ------------- | :------------------------------------: |
| 1             |        The User selects "Manage Inventory"        	|
| 2             |            The User selects product             	|
| 3             |         The User selects the price to discount      	|
| 4             |       The User inserts discount percentage        	|
| 5             |     The User inserts discount expiration date      	|
| 6             |            The User selects "Confirm"            	|
| 7             | The System checks if product is already discounted 	|
| 8             |     The System updates product's current price     	|
| 9             |      The System displays confirmation message      	|

##### Scenario 22.3 - Discount for product already existing

| Scenario 22.3 |                                                              |
| ------------- | :----------------------------------------------------------: |
| 1             |        The User selects "Manage Inventory"        	|
| 2             |            The User selects product             	|
| 3             |         The User selects the price to discount      	|
| 4             |       The User inserts discount percentage        	|
| 6             |            The User selects "Confirm"            	|
| 5             | The System checks if product is already discounted 	|
| 6             |The System displays current discount                   |
| 7             | The System displays message: "Product already discounted. Overwrite previous discount?" |
| 6             |            The User selects "Confirm"            	|
| 8             |     The System updates product's current price     	|
| 9             |      The System displays confirmation message      	|

### UC23 - The User removes product discount

| Actors Involved  |                       Manager OR Owner                       |
| ---------------- | :----------------------------------------------------------: |
| Precondition     |              A discount exists for the product               |
| Post condition   |              Discount is removed from database               |
| Nominal Scenario | The user removes an existing discount for selected products  |
| Variants         | The user may try to remove a discount from a product that doesn't have one, in which case the "Remove button" is disabled. An existing discount may expire |

##### Scenario 23.1 - NOMINAL

| Scenario 23.1 |                                        |
| ------------- | :------------------------------------: |
| 1             |        The User selects "Manage Inventory"        	|
| 2             |            The User selects product             	|
| 3             |         The User selects the price discounted      	|
| 4             |        The User selects "Remove discount"        	|
| 5             |         The System asks for confirmation          	|
| 6             |            The User selects "Confirm"            	|
| 7             |    Restore product's original price    		|
| 8             |      Display confirmation message      		|

##### Scenario 23.2 - Product is not discounted

| Scenario 23.2 |                                                     |
| ------------- | :-------------------------------------------------: |
| Precondition  | Selected product has not a discount in the database |
| 1             |        The User selects "Manage Inventory"        	|
| 2             |            The User selects product             	|
| 3             |      The System displays the price without the discount|
| Postcondition |                   Nothing happens                   |

##### Scenario 23.3 - Product discount expires

| Scenario 23.3 |                                                              |
| ------------- | :----------------------------------------------------------: |
| Precondition  | Product discount has expired and new day starts or system is started for the first time after expiration date |
| 1             |               The System checks all discounts in database                |
| 2             | The System filters out discounts without expiration date or that haven't expired yet |
| 3             |      The System restores original prices for all remaining products      |

### UC24 - The User configures fidelity cards discounts

| Actors Involved  |                       Manager OR Owner                       |
| ---------------- | :----------------------------------------------------------: |
| Precondition     |                                                              |
| Post condition   |          "Fidelity cards settings" file is updated           |
| Nominal Scenario | The user configures the parameters for fidelity card discounts, such as ranges and whether they are enabled or not |
| Variants         |            The user may deactivate fidelity cards            |

##### Scenario 24.1 - NOMINAL

| Scenario 24.1 |                                                         |
| ------------- | :-----------------------------------------------------: |
| 1             |                    The User selects "Settings"                |
| 2             |             The User selects "Configure fidelity discount"    |
| 3             |            The User checks "Enable" checkbox  of the discount	|
| 4             |                     The User selects "Save"                   |
| 5             |                  The System asks for confirmation           	|
| 6             |                   The User selects "Confirm"                	|

##### Scenario 24.2 - the User deactivates fidelity cards discount

| Scenario 24.2 |                                   |
| ------------- | :-------------------------------: |
| 1             |                    The User selects "Settings"                |
| 2             |             The User selects "Configure fidelity discount"    |
| 3             |         The User unchecks "Enable" checkbox  of the discount	|
| 4             |                     The User selects "Save"                   |
| 5             |                  The System asks for confirmation           	|
| 6             |                   The User selects "Confirm"                	|

##### Scenario 24.3 -  The User adds new fidelity cards discount

| Scenario 24.3 |                                                         |
| ------------- | :-----------------------------------------------------: |
| 1             |                    The User selects "Settings"                    	|
| 2             |             The User selects "Configure fidelity discount"        	|
| 3             |            The User selects "Add new discount"    			|
| 4             | The User defines fidelity card points - discounts correspondence 	|
| 5		| 	The User inserts the minimum expense required		  	|
| 6             |                 The User inserts expiration date                  	|
| 7             |                 The User selects "Confirm"                     	|
| 8             |                 The System update the database                     	|

### UC25 - The User modifies product

| Actors Involved  |              Manager OR Owner              |
| ---------------- | :----------------------------------------: |
| Precondition     |         Product exists in database         |
| Post condition   |    Product entry is updated in database    |
| Nominal Scenario | The user can edit a product's entry's data |
| Variants         |                                            |

##### Scenario 25.1 - NOMINAL

| Scenario 25.1 |                                    |
| ------------- | :--------------------------------: |
| 1             |     The User selects "Manage inventory"      	|
| 2             |     The User selects product           	|
| 3             |     The User selects "Modify product"       	|
| 4             |     The User inserts new data           	|
| 5             |     The User selects "Confirm"          	|
| 6             | The System updates product's entry in database|
| 7             |     The System shows confirmation message     |

### UC26 - The User filters accounting data

| Actors Involved  |                       Manager OR Owner                       |
| ---------------- | :----------------------------------------------------------: |
| Precondition     |                                                              |
| Post condition   | Accounting data that matches the specified filters is displayed, as well as balance |
| Nominal Scenario | The user can specify filters to visualize requested accounting data |
| Variants         | The user may save or print the collected data, and/or specify a time period over which compute the balance |

##### Scenario 26.1 - NOMINAL

| Scenario 26.1 |                                                  |
| ------------- | :----------------------------------------------: |
| 1             |             The User selects "Accounting data"             	|
| 2             |             The User inserts filters                   	|
| 3             |             The User selects "Apply"                  	|
| 4             | The System retrieves data matching the filters from database 	|
| 5             |        The System computes total balance               	|
| 6             |             The System display data                   	|

##### Scenario 26.2 - The System saves accounting data to file

| Scenario 26.2 |                                                  |
| ------------- | :----------------------------------------------: |
| 1             |             The User selects "Accounting data"             	|
| 2             |             The User inserts filters                   	|
| 3             |             The User selects "Apply"                  	|
| 4             | The System retrieves data matching the filters from database 	|
| 5             |        The System computes total balance               	|
| 6             |             The System display data                   	|
| 7             |          The User selects "Save as file"               	|
| 8             |      The System displays File Explorer dialogue window       	|
| 9             |                   The System creates file                    	|

##### Scenario 26.3 - The User prints accounting data

| Scenario 26.3 |                                                  |
| ------------- | :----------------------------------------------: |
| 1             |             The User selects "Accounting data"             	|
| 2             |             The User inserts filters                   	|
| 3             |             The User selects "Apply"                  	|
| 4             | The System retrieves data matching the filters from database 	|
| 5             |        The System computes total balance               	|
| 6             |             The System display data                   	|
| 7             |                 The User selects "Print"                  	|
| 8             |             The System prints accounting data               	|

##### Scenario 26.4 - The User generates a graph

| Scenario 26.4 |                                                  |
| ------------- | :----------------------------------------------: |
| 1             |             The User selects "Accounting data"             	|
| 2             |             The User inserts filters                   	|
| 3             |             The User selects "Apply"                  	|
| 4             | The System retrieves data matching the filters from database 	|
| 5             |        The System computes total balance               	|
| 6             |             The System display data                   	|
| 7             |            The User selects "Generate graph"              	|
| 8             |            The System generates and display graph             |

##### Scenario 26.5 - Save Graph in Home

| Scenario 26.5 |                                                  |
| ------------- | :----------------------------------------------: |
| 1             |             The User selects "Accounting data"             	|
| 2             |             The User inserts filters                   	|
| 3             |             The User selects "Apply"                  	|
| 4             | The System retrieves data matching the filters from database 	|
| 5             |        The System computes total balance               	|
| 6             |             The System display data                   	|
| 7             |            The User selects "Generate graph"              	|
| 8             |            The System generates and display graph             |
| 9             |            The User selects "Save graph in home"             	|
| 10            |            The System save the graph in Hompage             	|

### UC27 - The User adds barcode reader

| Actors Involved  |            Manager OR Owner, Barcode Reader             |
| ---------------- | :-----------------------------------------------------: |
| Precondition     |                                                         |
| Post condition   |      Barcode reader is registered in the software       |
| Nominal Scenario |  The user can add a new barcode reader to the software  |
| Variants         | The system may not be able to detect any barcode reader |

##### Scenario 27.1 - NOMINAL

| Scenario 27.1 |                                                       |
| ------------- | :---------------------------------------------------: |
| 1             |         The User selects "System management"          |
| 2             | The User selects the section "Manage barcode readers" |
| 3             |       The User selects "Detect barcode reader"        |
| 4             |     The System searches for barcode reader driver     |
| 5             |    The System adds barcode reader to the software     |
| 6             |       The System displays confirmation message        |

##### Scenario 27.2 - No barcode reader found

| Scenario 27.2 |                                                 |
| ------------- | :---------------------------------------------: |
| 1             |     The User selects "System management"     		|
| 2             |  The User selects the section "Manage barcode readers"|
| 3             |   The User selects "Detect barcode reader"   		|
| 4             |  The System searches for barcode reader driver  	|
| 5             | The System displays error message "No barcode reader found" |

### UC28 - The User prints user barcode

| Actors Involved  |                       Manager OR Owner                       |
| ---------------- | :----------------------------------------------------------: |
| Precondition     |                                                              |
| Post condition   |                  Account barcode is printed                  |
| Nominal Scenario | The user can print the barcode associated to a specified account |
| Variants         |                                                              |

##### Scenario 28.1 - NOMINAL

| Scenario 28.1 |                                          |
| ------------- | :--------------------------------------: |
| 1             |    The User selects "Manage employee"    |
| 2             |     The User selects desired account     |
| 3             | The User selects "Print account barcode" |
| 4             |     The System asks for confirmation     |
| 5             |        The User selects "Confirm"        |
| 6             |    The System prints account barcode     |

### UC29 - The User read notifications

| Actors Involved  |            Manager OR Owner             |
| ---------------- | :-------------------------------------: |
| Precondition     |     User has pending notifications      |
| Post condition   |    User has no pending notifications    |
| Nominal Scenario | The user visualizes their notifications |
| Variants         |                                         |

##### Scenario 29.1 - NOMINAL

| Scenario 29.1 |                                              |
| ------------- | :------------------------------------------: |
| 1             |         The User selects "Homepage"          |
| 2             |      The User selects  "Notifications"       |
| 3             | The System loads notifications from database |
| 4             |       The notification are set "Read"        |

### UC30 - The User prints receipt

| Actors Involved  |                Cashier                 |
| ---------------- | :------------------------------------: |
| Precondition     |   "Print receipt" has been selected    |
| Post condition   |           Receipt is printed           |
| Nominal Scenario | The receipt printer prints the receipt |
| Variants         |                                        |

##### Scenario 30.1 - NOMINAL

| Scenario 30.1 |                       |
| ------------- | :-------------------: |
| 1             | The System formats receipt output |
| 2             | The System sends data to printer  |

### UC31 - Add transaction to database

| Actors Involved  |                                                |
| ---------------- | :--------------------------------------------: |
| Precondition     |           A receipt has been printed           |
| Post condition   |               Receipt is printed               |
| Nominal Scenario | The transaction has been added to the database |
| Variants         |                                                |

##### Scenario 31.1 - NOMINAL

| Scenario 31.1 |                                                 |
| ------------- | :---------------------------------------------: |
| 1             | The Cashier sets transaction amount and details |
| 2             |     The System adds transaction to database     |

### UC32 - The User adds product to "Quick access products" list

| Actors Involved  |                           Cashier                            |
| ---------------- | :----------------------------------------------------------: |
| Precondition     |                                                              |
| Post condition   | Product is added to "Quick access products" list in cash register database |
| Nominal Scenario |       The user specifies a product to add to the list        |
| Variants         |                                                              |

##### Scenario 32.1 - NOMINAL

| Scenario 32.1 |                                                           |
| ------------- | :-------------------------------------------------------: |
| 1             |                The User selects "Settings"                |
| 2             |  The User selects "Manage 'Quick access products' list"   |
| 3             |              The User selects "Add product "              |
| 4             |        The Barcode reader reads product's barcode         |
| 5             | The System adds product to list in cash register database |

### UC33 - The User removes product from "Quick access products" list

| Actors Involved  |                           Cashier                            |
| ---------------- | :----------------------------------------------------------: |
| Precondition     |          "Quick access products" list is not empty           |
| Post condition   | Product is removed from "Quick access products" list in cash register database |
| Nominal Scenario |     The user specifies a product to remove from the list     |
| Variants         |                                                              |

##### Scenario 33.1 - NOMINAL

| Scenario 33.1 |                                                              |
| ------------- | :----------------------------------------------------------: |
| 1             |                 The User selects "Settings"                  |
| 2             |    The User selects "Manage 'Quick access products' list"    |
| 3             |            The User selects the product to remove            |
| 4             |                     The User selects "X"                     |
| 5             |               The System asks for confirmation               |
| 6             |                  The User selects "Confirm"                  |
| 7             | The System emove product from list in cash register database |

### UC34 - Perform cash register closure

| Actors Involved  |                           Cashier                            |
| ---------------- | :----------------------------------------------------------: |
| Precondition     |  Transactions of present day not flagged as "In a closure"   |
| Post condition   |                    Data correctly flagged                    |
| Nominal Scenario |        The cashier performs the cash register closure        |
| Variants         | The cash register memory might be full and need replacement before storing the closure |

##### Scenario 34.1 - NOMINAL

| Scenario 34.1 |                                                              |
| ------------- | :----------------------------------------------------------: |
| 1             |          The Cashier selects "Close Cash register"           |
| 2             |               The System asks for confirmation               |
| 3             |                 The Cashier selects"Confirm"                 |
| 4             | The System searchs for transactions not already in a closure |
| 5             |               The System computes total amount               |
| 6             |      The System stores closure document in local memory      |
| 7             |    The System sends closure document to government entity    |
| 8             |           The System displays confirmation message           |

##### Scenario 34.2 - Local memory full

| Scenario 34.2 |                                                              |
| ------------- | :----------------------------------------------------------: |
| 1             |          The Cashier selects "Close Cash register"           |
| 2             |               The System asks for confirmation               |
| 3             |                The Cashier selects "Confirm"                 |
| 4             | The System searches for transactions not already in a closure |
| 5             |               The System computes total amount               |
| 6             |    The System displays error message "Local memory full"     |
| 7             |                  The System replaces memory                  |
| 8             |      The System stores closure document in local memory      |
| 9             |    The System sends closure document to government entity    |
| 10            |           The System displays confirmation message           |

### UC35 - The Cashier adds POS to cash register

| Actors Involved  |                     Cashier                     |
| ---------------- | :---------------------------------------------: |
| Precondition     |                                                 |
| Post condition   |        POS is registered in the software        |
| Nominal Scenario | The cashier adds a new POS to the cash register |
| Variants         |                                                 |

##### Scenario 35.1 - NOMINAL

| Scenario 35.1 |                              |
| ------------- | :--------------------------: |
| 1             |     The Cashier selects "Connect POS"  |
| 2             |     The System searches for POS        |
| 3             |      Wait for POS setup      		 |
| 4             |   The System adds POS to cash register |
| 5             | The System display confirmation message|


##### Scenario 35.2 - Error in POS Setup

| Scenario 35.2 |                              |
| ------------- | :--------------------------: |
| 1             |     The Cashier selects "Connect POS"  |
| 2             |     The System searches for POS        |
| 3             |      Wait for POS setup      		 |
| 4             | The System displays error message	 |
### UC36 - The User opens a calendar day

| Actors Involved  |                       Manage OR Owner                        |
| ---------------- | :----------------------------------------------------------: |
| Precondition     |                                                              |
| Post condition   |                                                              |
| Nominal Scenario | The user can display information regarding a given day, such as products with discount active on the day |
| Variants         |              The research may find no products               |

##### Scenario 36.1 - NOMINAL

| Scenario 36.1 |                                                              |
| ------------- | :----------------------------------------------------------: |
| 1             |               The User selects day on calendar               |
| 2             | The System loads all products with discounts active on the day |
| 3             | The System marks products with discounts expiring on the day as yellow |
| 4             |      The System loads all products expiring on the day       |
| 5             |             The System displays loaded products              |

##### Scenario 36.2 - There are no discounted products

| Scenario 36.2 |                                                              |
| ------------- | :----------------------------------------------------------: |
| 1             |               The User selects day on calendar               |
| 2             |      No products with discounts active on the day found      |
| 3             | The System loads all products expiring on the day and mark them as red |
| 4             |             The System displays loaded products              |

##### Scenario 36.3 - There are no expiring products

| Scenario 36.3 |                                                              |
| ------------- | :----------------------------------------------------------: |
| 1             |               The User selects day on calendar               |
| 2             | The System loads all products with discounts active on the day |
| 3             | The System marks products with discounts expiring on the day as yellow |
| 4             |                No expiring products are found                |
| 5             |             The System displays loaded products              |

### UC37 - The User adds supplier

| Actors Involved  |                       Manage OR Owner                        |
| ---------------- | :----------------------------------------------------------: |
| Precondition     |                                                              |
| Post condition   |              New supplier is added to database               |
| Nominal Scenario | The user can add a new supplier and its relative data to the database |
| Variants         |              The research may find no products               |

##### Scenario 37.1 - NOMINAL

| Scenario 37.1 |                              |
| ------------- | :--------------------------: |
| 1             |  The User selects "Manage suppliers"   	|
| 2             |  The User selects "Add new supplier"   	|
| 3             |    The User inserts supplier's data     	|
| 4             |   The User selects "Confirm"    		|
| 5             |   The System adds supplier to database   	|
| 6             | The System displays confirmation message 	|

### UC38 - The User filters supplier

| Actors Involved  |                       Manage OR Owner                        |
| ---------------- | :----------------------------------------------------------: |
| Precondition     |                                                              |
| Post condition   |                                                              |
| Nominal Scenario |   The user can browse and filter suppliers in the database   |
| Variants         | The user may browse a supplier's products, or remove a supplier |

##### Scenario 38.1 - NOMINAL

| Scenario 38.1 |                                                   |
| ------------- | :-----------------------------------------------: |
| 1             |        The User selects "Manage suppliers"        |
| 2             |             The User inserts filters              |
| 3             |             The User selects "Apply"              |
| 4             | The System loads suppliers that match the filters |
| 5             |       The System displays loaded suppliers        |

##### Scenario 38.2 - The User sees supplier products

| Scenario 38.2 |                                                              |
| ------------- | :----------------------------------------------------------: |
| 1             |             The User selects "Manage suppliers"              |
| 2             |                 The User selects a Supplier                  |
| 3             | The User selects "Show supplier products" for desired supplier |
| 4             |       The System searches the product for the supplier       |
| 5             |       The System displays the product for the supplier       |

##### Scenario 38.3 - Remove supplier

| Scenario 38.3 |                                           |
| ------------- | :---------------------------------------: |
| 1             |    The User selects "Manage suppliers"    |
| 2             |        The User selects a Supplier        |
| 3             | The User selects "X" for desired supplier |
| 4             |     The System asks for confirmation      |
| 5             |        The User selects "Confirm"         |
| 6             | The System removes supplier from database |

### UC39 - First software use

| Actors Involved  |                            Owner                             |
| ---------------- | :----------------------------------------------------------: |
| Precondition     |           The software has never been used before            |
| Post condition   |        Owner account created, software email is setup        |
| Nominal Scenario | The user creates an Owner account and configures the software's email |
| Variants         | The specified email may not exists, generating an error message |

##### Scenario 39.1 - NOMINAL

| Scenario 39.1 |                                                  |
| ------------- | :----------------------------------------------: |
| 1             | The System checks if there are users in database |
| 2             |      The Sytsem displays "Add account" GUI       |
| 3             |          The Owner inserts account data          |
| 4             |    The Owner inserts software's email account    |
| 5             |           The Owner selects "Confirm"            |
| 6             |       The System configures email service        |
| 7             |       The System saves account in database       |
| 8             |            The System loads Login GUI            |

##### Scenario 39.2 - Email does not exist

| Scenario 39.2 |                                                   |
| ------------- | :-----------------------------------------------: |
| 1             | The System checks if there are users in database  |
| 2             |       The Sytsem displays "Add account" GUI       |
| 3             |          The Owner inserts account data           |
| 4             |    The Owner inserts software's email account     |
| 5             |            The Owner selects "Confirm"            |
| 6             |        The System configures email service        |
| 7             |            Email account is not found             |
| 8             | The System displays error message "Invalid email" |

### UC40 - Software shut down

| Actors Involved  |         Owner OR Manager          |
| ---------------- | :-------------------------------: |
| Precondition     |      The software is active       |
| Post condition   | All cash register is disconnected |
| Nominal Scenario | The user shuts down the software  |
| Variants         |                                   |

##### Scenario 40.1 - NOMINAL

| Scenario 40.1 |                                                     |
| ------------- | :-------------------------------------------------: |
| 1             |      The User selects "Shut down the software"      |
| 2             |          The System asks for confirmation           |
| 3             |             The User selects "Confirm"              |
| 4             | The System disconnects all connected cash registers |
| 5             |                Software is shut down                |

### UC41 - User changes password

| Actors Involved  |              Cashier OR Manager OR Owner              |
| ---------------- | :---------------------------------------------------: |
| Post condition   |          User's account password is updated           |
| Nominal Scenario | The user selects 'modify password' and writes new one |
| Variants         |                         None                          |

##### Scenario 41.1 - NOMINAL

| Scenario 41.1 |                                          |
| ------------- | :--------------------------------------: |
| 1             |    The User selects "modify password"    |
| 2             |  The User inserts old and new password   |
| 3             |         The User selects "Apply"         |
| 4             |     The System asks for confirmation     |
| 5             |          The User selects "yes"          |
| 6             | The System updates password in database  |
| 7             | The System displays confirmation message |

# Glossary

<img src='https://www.planttext.com/api/plantuml/svg/rLdRRXit47ttLn1v2L1O-eGYZkEa1jhOiTCMXPC7Yajv2NF93I-I1VJZEyFxIXQG8BweVl7kSZWtpioPeTvOHutpWoGVVh-ulZwv-kVevefRfzaTERt-Yoo5vEJBqHT5Pl0NdgxzE4h1JNYvDBhpp16XbffGrH6d7PM4QUjQuevyyrGvuJPVoQpJQpKZHOmpL5dAdD0AjX3ugzBYs-V0az-FN5bUlZPwn0zE3C2I3YDy-Nhqv7RCqOuvkJbtfDFSgjSELPI22KVMlIR24jTNTMqITmoBWlMafx2koQ9I35HvAkMciE7Tl2oUIAkFYN2lBQ7M-e6ZYi1aLVTJHoIde32ALPnnQwdPb6McfH-KdMzvvsdSJ-P-dDSp1b-CoSD5HlOayMD7dL2tP2rSNzOLNnFDc3U6Ap0bkoqlttB53NKStD6QaAtxFnvAYQWTrqxFuoCTAl_BNYjWx8T5GtysK249xJe3uf977f_fhhwGPGx-MJuD9w6Oz1qSLNOtKL7Q4-ary-EClFhbrJFMc2VA8ZGYryiJnhHN8L7UKKVtY7bB9OLygGAe1CStwjcwyG7bSxGydx0IHPbPIb589wWCH2zhAaaPLf8FRQZJ45iQF5Q8W3xG7OH9ZSWEHSRyi9WWaq0f2J88XPJyc0pqFZorIGI64cso_SU1Edu-RkhLVVc6wvKO5Z9H-PWJSlxyFfePSTkxbAV8uRjpA7bYNqe-6Yyobtpf4E4jML4fEg8L005TkZPV8I0Db7KnEepSMjS1jvW3yj481Q3n-ChbueiSKyvme34SePR5MfX39y5Rmc387DCWLDT5t6BG2SRUf-UArCCVwMO0pYZ4y5jXmVnWJrmDe4stR7fSKqCLn9f-SqfjNvJOm_1KgwMuzT0SeD3K-AKbgQrlJ3z5Y_FB9oyWJYaBhYkUaY9qW6ZGomadCup4b2ldQgL5atrJ1IxptjG7VJTnwTY-UNJz0ZHVSBVc40fjnvHZFOGrredjO4i1D3bvhZeaFIWtfbAtHu4htOXyImtJNNt_qtAZ9dq9T7zhRMfIn9vaKbEVM50fLdzGHM_1iGOP0nJgTMXfymPlmrT8Tjk7bishM9HhLtknLkr-7lRxfcd1_KCIK_Q_ZBLPt2BnObevAXe02YNRLyweuyNV7y-k81fc__uA_ZhvUF81Ne_AKreuFRd-xJmivAUqy6oHyvAP4flEa2cNsvFR_WPzfvtzb8UyKkvxQDDxBRjwH1VG2GW8AgmfjLITXidFbO_ea0FH909Ih2K_LevVy4HZUPygm2QYCgCWiYJIjvPjQGacxB7aTqQiiBND558psjQ50x3SkDmdXIMO5CPwEf6JuRGBva1WhA79wRn1uAgza0tTLeH4jcNIOPYPE2HB4O1N6FLWhQDtmPWqHZK0NSU7S1s8L4kZX-WhlBnWYIiCP805Ig-FevSAiCVTG3yWGTW70I31EcW8c80dwbslJKp0W_3KvSLrzbB7LvZ2wvut7Bc41jve9TZ3emtsq0Fj-ClRCncbJhKVzpRRstXqTZ_Ym30PDDy96_6D3Wdc3icyzwBZ4gC8E7HxzFWCe-x4p3XZZ1fQLhl7ziF0svk7o3BLT-3BatbdSXDrlG473LleCN75L6BH2YPCGDl9Fjju5WFNxW6b99E7zcYR91pU4yJzuLjzK1rHojEV3UAVsKxcGOc0zmhlyIrtY5e0sLHj8cHNi8-PFYVdhanApQYRHoG0D4D7qS4WOGCexBP1yNe54Q7dT5KzgWrPU7ct2MYfbmw6wONse623k1vt0WkAHMUXdOE86YcximTsnFd2wSehXQ2llODEm8cbODEWTLa6mYkCNbqxVuOT6uHQjTj-eGDDmnwQbd1xM6VJCrgliSAlDRKrwyHooOFEHO7vWRhm_VHtagKsko5LxeHnNcp39VzYXRUx-JUqwVrl_PfkW4M3IlLwcTSYsEo77NfYplJgU6SDw5aojDfGjh43dkBoQRoZ6mkb0aw1HvzW-DKWpcoROCRfe4u14hfWMbLSnzlJMvY7IeXZ9j05XdH94ftyFoS_5jC7wiIiJvTHLBHsZ7Xjg9m7VZY8zBp6s5RG1E-5c-P1hy3ROGufH-2EymKr45WlWnWe4NWXBTeAn9l8Yuer4VW63imFyZy0'>

# System Design
<img src='https://www.planttext.com/api/plantuml/svg/SoWkIImgAStDuOfsIYnApaz9LGXAJKn9BOfLKD9IKj3LpLU8TauipW2AfcSMbm35kQ83YZBpG2mSilx1E6HG-GZpNQE2C_8BW0eSKt8pynH2Kek0L69EB9eiJlUXQUDoICrB0Ve80000'>

# Deployment Diagram 

<img src='https://www.planttext.com/api/plantuml/svg/VLBDQWCX4Bxx58ENG267x1DacrnwA0bfNs1MJJBWkZAQvb3wxlLdQJT2AuW_tzyuk0qHETv6Az92PrGHS0COGFPw90VlXjE2TXNWJDO42Py2qlXXopr6730OcLLwQD1NJ2QzzvOKHffSOOqeliGYi2k1EmnNO7EX40t_dNTaSea0UwOFmyNFwvPpU8F--591z9JYtAHD-wY2Ovonad-Qys5doRXO68hLejh60vwHLTQV3Eh7iWRKcuNkqVzVMITIO8JrpQz1FidSrdpKqztLYsezCr8xyvRfSeqLpXMdsIYwUfSbfIc_9ossnkdq2xu1'>

