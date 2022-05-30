Feature: Customer CRUD tests

  Scenario: Check customers have only one address
    Given I create 3 customers with all mandatory fields
    Then I check that there are no customers without address

  Scenario: Check that customers don't have same phone and email
    Given I create 3 customers with all mandatory fields
    Then I compare customers phone and email them that are not equal

  Scenario: Check that customer can be create with all mandatory fields
    Given I create 2 customers with all mandatory fields
    Then I verify that 2 customers is created correctly

  Scenario: Check that customer cannot be created without mandatory fields
    Given I create 1 customer without mandatory fields name
    And I cannot save the customer without name
    Given I create 1 customer without mandatory fields address_id
    Then I cannot save the customer without address_id

  Scenario: Check that random customers have all mandatory fields filled
    Given I create 5 customers with all mandatory fields
    When I get random 2 customers
    Then I check that 2 customers mandatory fields are not empty

  Scenario: Check that customer address cannot be created without mandatory fields
    Given I create 1 customer address without city
    And I cannot save customer address without city
    When I create 1 customer address without country
    And I cannot save customer address without country
    When I create 1 customer address without postal_code
    Then I cannot save customer address without postal_code

  Scenario: User can save new product
    Given I create 2 products with all mandatory fields
    When  I check that 2 products is created correctly
    Then I check that no orders are related with 2 products

  Scenario: User cannot save new product without mandatory fields
    Given I create 1 product without product_name
    And I cannot save 1 product without product_name
    When I create 1 product without product_type
    Then I cannot save 1 product without product_type
    When I create 1 product without available_quantity
    Then I cannot save 1 product without available_quantity
    When I create 1 product without price_without_vat
    Then I cannot save 1 product without price_without_vat
    When I create 1 product without warehouse
    Then I cannot save 1 product without warehouse
    When I create 1 product without supplier_id
    Then I cannot save 1 product without supplier_id

  Scenario: User gets random customers and verifies that their orders (if any) have all mandatory fields filled
    Given I create 2 customers with all mandatory fields
    And   I create 2 products with all mandatory fields
    When  I create 2 orders with all mandatory fields
    And   I get random 1 customer
    Then I check that orders mandatory fields are filled

  Scenario: User gets random orders and verifies that customer_id is not null
    Given I create 3 customers with all mandatory fields
    And   I create 3 products with all mandatory fields
    When  I create 3 orders with all mandatory fields
    And I get 1 random order
    Then I check that customer_id is not null

  Scenario: User gets random orders and verifies that all their products exists in the products table
    Given I create 3 customers with all mandatory fields
    And   I create 3 products with all mandatory fields
    When  I create 3 orders with all mandatory fields
    And I get 1 random order
    Then I check that order products exists

  Scenario: User creates order and verify that all fields are not null
    Given I create 1 customer with all mandatory fields
    And   I create 1 product with all mandatory fields
    When  I create 1 order with all mandatory fields
    Then I verify that 1 orders is created correctly